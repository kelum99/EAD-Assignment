import React, { useState, useEffect } from "react";
import MainLayout from "../components/MainLayout";
import {
  Space,
  Table,
  message,
  Popconfirm,
  Button,
  Modal,
  DatePicker,
  Form,
  Input,
  InputNumber,
  Select,
} from "antd";

import { Stations, Routes } from "./services/Stations";
import useRequest from "./services/RequestContext";
import useUser from "./services/UserContex";
import dayjs from "dayjs";

const TicketBookingManagement = () => {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [date, setDate] = useState(null);
  const [edit, setEdit] = useState(false);
  const [selectedId, setSelectedId] = useState();
  const [selectedTrain, setSelectedTrain] = useState(null);
  const [activeTrains, setActiveTrains] = useState([]);
  const [travellers, setTravellers] = useState([]);
  const [reservations, setReservations] = useState();
  const [selectedNIC, setSelectedNIC] = useState();
  const [selectedRoute, setSelectedRoute] = useState();
  const [start, setStart] = useState();
  const [destination, setDestination] = useState();
  const { request } = useRequest();
  const [form] = Form.useForm();
  const { user } = useUser();
  const dateFormat = "YYYY/MM/DD";

  const showModal = () => {
    setIsModalOpen(true);
  };
  const handleOk = () => {
    form.submit();
  };
  const handleCancel = () => {
    form.resetFields();
    setIsModalOpen(false);
  };

  const [componentSize, setComponentSize] = useState("default");
  const onFormLayoutChange = ({ size }) => {
    setComponentSize(size);
  };

  //Retrieve active trains for the selected route
  const getActiveTrainsForRoute = async (route) => {
    setSelectedRoute(route);
    try {
      await request
        .get(`train/get-trains/${route}`)
        .then((res) => {
          setActiveTrains(res.data);
        })
        .catch((err) => message.error("Data Fetching Failed!", err));
    } catch (e) {
      console.log("Error", e);
    }
  };

  //Retrieve all the travellers registered in the system
  const fetchTravellers = async () => {
    try {
      await request
        .get("user/get-all-users")
        .then((res) => {
          setTravellers(res.data);
        })
        .catch((err) => message.error("Data Fetching Failed!", err));
    } catch (e) {
      console.log("Error", e);
    }
  };

  //Retrieve all existing reservations
  const fetchAllReservations = async () => {
    try {
      const response = await request.get("reservation/get-all-reservations");
      if (response.status === 200) {
        const data = response.data.map((reservation) => ({
          ...reservation,
          bookingDate: dayjs(reservation.bookingDate).format(dateFormat),
          reservationDate: dayjs(reservation.reservationDate).format(
            dateFormat
          ),
        }));
        setReservations(data);
      } else {
        message.error("Data Fetching Failed!");
      }
    } catch (error) {
      console.error("Error", error);
    }
  };

  //Disable the dates before current date and after 30 days from current date
  const disabledDate = (current) => {
    const currentDate = new Date();
    const thirtyDaysFromNow = new Date();
    thirtyDaysFromNow.setDate(thirtyDaysFromNow.getDate() + 31);
    return (
      current &&
      (current < currentDate.setHours(0, 0, 0, 0) ||
        current > thirtyDaysFromNow)
    );
  };

  useEffect(() => {
    const currentDate = new Date();
    setDate(currentDate);
    fetchTravellers();
    fetchAllReservations();
    if (selectedTrain) {
      form.setFieldsValue({
        time: selectedTrain.time || "",
      });
    }
  }, [form, selectedTrain]);

  //Create and update reservations
  const onFinish = async (values) => {
    try {
      if (edit && selectedId) {
        const updateReservation = {
          userNIC: values.userNIC,
          route: values.route,
          train: values.train,
          startingPoint: values.startingPoint,
          destination: values.destination,
          agentID: user.Id,
          time: values.time,
          noOfTickets: values.noOfTickets,
          bookingDate: values.bookingDate,
          reservationDate: values.reservationDate,
        };
        await request
          .put(
            `reservation/update-reservation/${selectedId}`,
            updateReservation
          )
          .then((res) => {
            if (res.status === 204) {
              fetchAllReservations();
              message.success("Reservation Update Success!");
              handleCancel();
            }
          })
          .catch((error) => message.error(error.response.data));
      } else {
        const reservation = {
          ...values,
          userNIC: selectedNIC,
          route: selectedRoute,
          train: selectedTrain.name,
          startingPoint: start,
          destination: destination,
          agentID: user.Id,
        };
        const response = await request.post(
          "reservation/add-reservation",
          reservation
        );

        if (response.status === 201) {
          handleCancel();
          fetchAllReservations();
          message.success("Reservation Success!");
        } else {
          message.error("Internal Server Error!");
        }
      }
    } catch (error) {
      message.error(error.response.data);
    }
  };

  //Cancel reservations
  const cancelReservation = async (id) => {
    try {
      await request
        .delete(`reservation/cancel-reservation/${id}`)
        .then((res) => {
          if (res.status === 204) {
            fetchAllReservations();
            message.success("Reservation Cancelled Successfully!");
          }
        })
        .catch((error) => message.error(error.response.data));
    } catch (e) {
      console.log("error", e);
    }
  };

  //validations for input fields
  const validateMessages = {
    required: "${label} is required!",
  };

  //set values for edit form
  const showEdit = (data) => {
    setSelectedId(data.id);
    setEdit(true);
    showModal();
    console.log(data);
    const fieldsData = {
      userNIC: data.userNIC,
      bookingDate: dayjs(data.bookingDate, dateFormat),
      reservationDate: dayjs(data.reservationDate, dateFormat),
      noOfTickets: data.noOfTickets,
      route: data.route,
      train: data.train,
      startingPoint: data.startingPoint,
      destination: data.destination,
      time: selectedTrain ? selectedTrain.time : "",
    };

    form.setFieldsValue(fieldsData);
  };

  const columns = [
    {
      title: "Booking ID",
      dataIndex: "id",
      key: "id",
    },
    {
      title: "User NIC",
      dataIndex: "userNIC",
      key: "userNIC",
    },
    {
      title: "Booking Date",
      dataIndex: "bookingDate",
      key: "bookingDate",
    },
    {
      title: "Reservation Date",
      dataIndex: "reservationDate",
      key: "reservationDate",
    },
    {
      title: "No Of Tickets",
      dataIndex: "noOfTickets",
      key: "noOfTickets",
    },
    {
      title: "Route",
      dataIndex: "route",
      key: "route",
    },
    {
      title: "Train",
      dataIndex: "train",
      key: "train",
    },
    {
      title: "Start",
      dataIndex: "startingPoint",
      key: "startingPoint",
    },
    {
      title: "Destination",
      dataIndex: "destination",
      key: "destination",
    },
    {
      title: "Time",
      dataIndex: "time",
      key: "time",
    },
    {
      title: "Agent ID",
      dataIndex: "agentID",
      key: "agentID",
    },
    {
      title: "Action",
      key: "action",
      render: (_, record) => (
        <Space size="middle">
          <a onClick={() => showEdit(record)}>Edit</a>
          <Popconfirm
            title="Cancel Reservation"
            description="Are you sure to cancel this reservation?"
            onConfirm={() => cancelReservation(record.id)}
            okText="Yes"
            cancelText="No"
          >
            <a>Delete</a>
          </Popconfirm>
        </Space>
      ),
    },
  ];

  return (
    <MainLayout title={"Ticket Booking Management"}>
      <div>
        <Table
          columns={columns}
          dataSource={reservations}
          rowKey={(record) => record.id}
        />
        <Button type="primary" onClick={showModal} style={{ marginTop: 16 }}>
          Add Reservation
        </Button>

        <Modal
          title={edit ? "Update Reservation" : "Create Reservation"}
          okText={edit ? "Update" : "Create"}
          open={isModalOpen}
          onOk={handleOk}
          onCancel={handleCancel}
        >
          <Form
            form={form}
            onFinish={onFinish}
            labelCol={{
              span: 8,
            }}
            wrapperCol={{
              span: 14,
            }}
            layout="horizontal"
            initialValues={{
              size: componentSize,
              userNIC: "",
              bookingDate: date ? date : "",
              reservationDate: "",
              noOfTickets: 1,
              route: "",
              train: "",
              startingPoint: "",
              destination: "",
              time: "",
            }}
            onValuesChange={onFormLayoutChange}
            size={componentSize}
            style={{
              maxWidth: 600,
            }}
            validateMessages={validateMessages}
          >
            <Form.Item
              label="Traveller ID"
              name="userNIC"
              rules={[
                {
                  required: true,
                },
              ]}
            >
              <Select onChange={setSelectedNIC} disabled={edit}>
                {travellers.length > 0 &&
                  travellers.map((traveller) => (
                    <Select.Option key={traveller.nic} value={traveller.nic}>
                      {traveller.nic}
                    </Select.Option>
                  ))}
              </Select>
            </Form.Item>

            <Form.Item label="Booking Date" name="bookingDate">
              <Input
                disabled
                value={date ? date : ""}
                style={{ marginTop: "8px" }}
              />
            </Form.Item>

            <Form.Item
              label="Reservation Date"
              name="reservationDate"
              rules={[
                {
                  required: true,
                },
              ]}
            >
              <DatePicker disabledDate={disabledDate} />
            </Form.Item>

            <Form.Item
              label="No Of Tickets"
              name="noOfTickets"
              rules={[
                {
                  required: true,
                },
              ]}
            >
              <InputNumber min={1} max={10} defaultValue={1} />
            </Form.Item>

            <Form.Item
              label="Route"
              name="route"
              rules={[
                {
                  required: true,
                },
              ]}
            >
              <Select options={Routes} onChange={getActiveTrainsForRoute} />
            </Form.Item>

            <Form.Item
              label="Train"
              name="train"
              rules={[
                {
                  required: true,
                },
              ]}
            >
              <Select
                onChange={(value) => {
                  const selectedValue = activeTrains.find(
                    (train) => train.name === value
                  );
                  setSelectedTrain(selectedValue);
                }}
              >
                {activeTrains.length > 0 &&
                  activeTrains.map((value) => (
                    <Select.Option key={value.name} value={value.name}>
                      {value.name}
                    </Select.Option>
                  ))}
              </Select>
            </Form.Item>

            <Form.Item
              label="Start"
              name="startingPoint"
              rules={[
                {
                  required: true,
                },
              ]}
            >
              <Select options={Stations} onChange={setStart} />
            </Form.Item>

            <Form.Item
              label="Destination"
              name="destination"
              rules={[
                {
                  required: true,
                },
              ]}
            >
              <Select options={Stations} onChange={setDestination} />
            </Form.Item>

            <Form.Item label="Time" name="time">
              <Input
                disabled
                value={selectedTrain ? selectedTrain.time : ""}
                style={{ marginTop: "8px" }}
              />
            </Form.Item>
          </Form>
        </Modal>
      </div>
    </MainLayout>
  );
};
export default TicketBookingManagement;
