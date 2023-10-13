import React, { useEffect, useState } from "react";
import {
  Space,
  Table,
  TimePicker,
  Button,
  Modal,
  DatePicker,
  Form,
  Input,
  Select,
  message,
  Popconfirm,
  InputNumber
} from "antd";

import MainLayout from "../components/MainLayout";
import {Stations, Routes, Schedule} from "./services/Stations";
import useRequest from "./services/RequestContext";
import dayjs from "dayjs";

export default function TrainManagement() {
  const [form] = Form.useForm();
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [edit, setEdit] = useState(false);
  const [selectedId, setSelectedId] = useState();
  const [time, setTime] = useState();
  const [data, setData] = useState([]);
  const { request } = useRequest();

  const columns = [
    {
      title: "Train Name",
      dataIndex: "name",
      key: "name",
    },
    {
      title: "Schedule",
      dataIndex: "schedule",
      key: "schedule",
    },
    {
      title: "Time",
      dataIndex: "time",
      key: "time",
    },
    {
      title: "Route",
      dataIndex: "route",
      key: "route",
    },
    {
      title: "Status",
      dataIndex: "status",
      key: "status",
      render: (text) =>
        text === 0 ? "Inactive" : text === 1 ? "Active" : "Published",
    },
    {
      title: "Action",
      key: "action",
      render: (_, record) => (
        <Space size="middle">
          <a onClick={() => showEdit(record)}>Edit</a>
          <Popconfirm
            title="Delete Train"
            description="Are you sure to cancel this train?"
            onConfirm={() => deleteTrain(record.id)}
            okText="Yes"
            cancelText="No"
          >
            <a>Delete</a>
          </Popconfirm>
        </Space>
      ),
    },
  ];

  const showModal = () => {
    setIsModalOpen(true);
  };
  const onReset = () => {
    form.resetFields();
  };
  const handleOk = () => {
    form.submit();
  };
  const handleCancel = () => {
    setIsModalOpen(false);
    setSelectedId(undefined);
    setEdit(false);
    onReset();
  };

  const showEdit = (data) => {
    setSelectedId(data.id);
    setEdit(true);
    showModal();

    // Set the time retrieved from the database
    setTime(dayjs(data.time, 'HH:mm').format('h:mm A'));



    //set values for edit form
    const fieldsData = {
      Name: data.name,
      Schedule: data.schedule,
      Route: data.route,
      Stations: data.stations,
      Status: data.status,
    };
    form.setFieldsValue(fieldsData);
  };

  //Create and update trains
  const onFinish = async (values) => {
    try {
      console.log("eeeeeeeee", values);
      if (edit && selectedId) {
        const updateTrain = {
          Name: values.Name,
          Schedule: values.Schedule,
          Time: time,
          Route: values.Route,
          Stations: values.Stations,
          Status: values.Status,
        };

        console.log("updatetrain",updateTrain)
        await request
          .put(`train/update-train/${selectedId}`, updateTrain)
          .then((res) => {
            if (res.status === 204) {
              getTrainData();
              message.success("Train update Success!");
              handleCancel();
            }
          })
          .catch((error) => message.error(error.response.data));
      } else {
        const train = {
          ...values,
          Name: values.Name,
          Schedule: values.Schedule,
          Time: time,
          Route: values.Route,
          Stations: values.Stations,
          Status: values.Status,
        };
        const response = await request.post("train/add-train", train);
        if (response.status === 201) {
          handleCancel();
          getTrainData();
          message.success("Train added success!");
        } else {
          message.error("Internal Server error!");
        }
      }
    } catch (error) {
      message.error(error.response.data);
    }
  };


//Delete trains
  const deleteTrain = async (id) => {
    try {
      request
        .delete(`train/delete-train/${id}`)
        .then((res) => {
          if (res.status === 204) {
            getTrainData();
            message.success("Train Delete Successfully!");
          }
        })
        .catch((error) => message.error("Train remove"));
    } catch (e) {
      console.log("error", e);
    }
  };

//Retrieve all trains details
  const getTrainData = async () => {
    try {
      console.log("Fetching train data...");
      await request
        .get("train/get-all-trains")
        .then((res) => {
          console.log("Response:", res);
          setData(res.data);
        })
        .catch((err) => {
          console.error("Error:", err);
          message.error("Data Fetching Failed!", err);
        });
    } catch (e) {
      console.error("Error:", e);
    }
  };

  useEffect(() => {
    getTrainData();
  }, []);


  return (
    <MainLayout title={"Train Management"}>
      <div>
        <Table
          columns={columns}
          dataSource={data}
          rowKey={(record) => record.id}
        />
        <Button type="primary" onClick={showModal} style={{ marginTop: 16 }}>
          Add Train
        </Button>

        <Modal
          title={edit ? "Update Train" : "Create Train Schedule"}
          okText={edit ? "Update" : "Create"}
          width={800}

          onOk={handleOk}
          open={isModalOpen}
          maskClosable={false}
          onCancel={handleCancel}

        >
          <Form
            form={form}
            onFinish={onFinish}
            labelCol={{
              span: 4,
            }}
            wrapperCol={{
              span: 14,
            }}
            layout="horizontal"

          >
            <Form.Item label="Train Name" name="Name" rules={[
              {
                required: true,
              },
            ]}>
              <Input />
            </Form.Item>

            <Form.Item label="Schedule" name="Schedule"
                       rules={[
              {
                required: true,
              },
            ]}>
              <Select options={Schedule} />
            </Form.Item>

            <Form.Item label="Route" name="Route" rules={[
              {
                required: true,
              },
            ]}>
              <Select options={Routes} />
            </Form.Item>

            <Form.Item label="Time" name="Time" rules={[
              {
                required: true,
              },
            ]}>
              <Space wrap>
                <TimePicker
                  use12Hours
                  format="h:mm A"
                  value={edit ? dayjs(time, 'h:mm A') : undefined}
                  onChange={(time, timeString) => {
                    setTime(timeString);
                    form.setFieldsValue({ Time: timeString }); // Update the form field value
                  }}
                />
              </Space>
            </Form.Item>

            <Form.Item label="Status" name="Status" initialValue={0}>
              <Select>
                <Select.Option value={0}>Inactive</Select.Option>
                <Select.Option value={1}>Active</Select.Option>
                <Select.Option value={2}>Published</Select.Option>
              </Select>
            </Form.Item>

            <Form.Item label="Stations" name="Stations" rules={[
              {
                required: true,
              },
            ]}>
              <Select
                mode="multiple"
                allowClear
                style={{
                  width: "100%",
                }}
                placeholder="Please select"
                options={Stations}
              />
            </Form.Item>
          </Form>
        </Modal>
      </div>
    </MainLayout>
  );
}
