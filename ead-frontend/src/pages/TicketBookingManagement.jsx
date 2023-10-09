import React, { useState, useEffect } from 'react';
import MainLayout from "../components/MainLayout";
import {
    Space,
    Table,
    message,
    Popconfirm,
    Button,
    Modal,
    Cascader,
    DatePicker,
    Form,
    Input,
    InputNumber,
    Radio,
    Select,
    Switch,
    TreeSelect,
} from "antd";
const { Column, ColumnGroup } = Table;
import { Stations, Routes } from "./services/Stations";
import useRequest from "./services/RequestContext";
import useUser from "./services/UserContex";



const TicketBookingManagement = () => {

    const [isModalOpen, setIsModalOpen] = useState(false);
    const [date, setDate] = useState(null);
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
    const { user } = useUser()

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

    const getActiveTrainsForRoute = async (route) => {
        setSelectedRoute(route)
        try {
            await request.get(`train/get-trains/${route}`).then((res) => {
                setActiveTrains(res.data);
            }).catch((err) => message.error('Data Fetching Failed!', err));
        } catch (e) {
            console.log('Error', e)
        }
    }

    const fetchTravellers = async () => {
        try {
            await request.get('user/get-all-users').then((res) => {
                setTravellers(res.data);
            }).catch((err) => message.error('Data Fetching Failed!', err));
        } catch (e) {
            console.log('Error', e)
        }
    }

    const fetchAllReservations = async () => {
        try {
            await request.get('reservation/get-all-reservations').then((res) => {
                setReservations(res.data);
                console.log(res.data)
            }).catch((err) => message.error('Data Fetching Failed!', err));
        } catch (e) {
            console.log('Error', e)
        }
    }

    const disabledDate = (current) => {
        const currentDate = new Date();
        const thirtyDaysFromNow = new Date();
        thirtyDaysFromNow.setDate(thirtyDaysFromNow.getDate() + 31);
        return current && (current < currentDate.setHours(0, 0, 0, 0) || current > thirtyDaysFromNow);
    };

    useEffect(() => {
        const currentDate = new Date();
        const currentDateTimeString = currentDate.toISOString();
        setDate(currentDate);
        fetchTravellers();
        fetchAllReservations();
        if (selectedTrain) {
            form.setFieldsValue({
                time: selectedTrain.time || '',
            });
        }
    }, [form, selectedTrain]);

    const onFinish = async (values) => {

        const reservation = {
            ...values,
            userNIC: selectedNIC,
            route: selectedRoute,
            train: selectedTrain.name,
            startingPoint: start,
            destination: destination,
            agentID: user.Id,
        };

        await request.post('reservation/add-reservation', reservation).then((res) => {
            if (res?.status === 400) {
                const errors = res.data.errors;
                console.log(errors)
                if (errors && errors.length > 0) {
                    const errorMessage = errors[0];
                    message.error(errorMessage);
                }
            } else if (res?.status === 201) {
                handleCancel();
                fetchAllReservations();
                return message.success('Reservation Success!');

            } else {
                return message.error('Internal Server Error!');
            }

        })
    };

    const cancelReservation = async (id) => {
        try {
            await request.delete(`reservation/cancel-reservation/${id}`).then((res) => {
                if (res.status === 200) {
                    fetchAllReservations();
                    message.success('Reservation Cancelled Successfully!');
                }
            }).catch(() => message.error('Reservation Cancel Failed!'));
        } catch (e) {
            console.log('error', e)
        }
    }

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
            title: 'Action',
            key: 'action',
            render: (_, record) => (
                <Space size="middle">
                    <a>Edit</a>
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
                <Table columns={columns} dataSource={reservations} />
                <Button type="primary" onClick={showModal} style={{ marginTop: 16 }}>
                    Add Reservation
                </Button>

                <Modal
                    title="Create Reservation"
                    open={isModalOpen}
                    onOk={handleOk}
                    onCancel={handleCancel}
                >
                    <Form
                        form={form}
                        labelCol={{
                            span: 8,
                        }}
                        wrapperCol={{
                            span: 14,
                        }}
                        layout="horizontal"
                        initialValues={{
                            size: componentSize,
                            userNIC: '', 
                            bookingDate: date ? date : '', 
                            reservationDate: '', 
                            noOfTickets: 1, 
                            route: '', 
                            train: '', 
                            startingPoint: '',
                            destination: '', 
                            time: '',
                        }}
                        onValuesChange={onFormLayoutChange}
                        size={componentSize}
                        style={{
                            maxWidth: 600,
                        }}
                        onFinish={onFinish}
                    >

                        <Form.Item label="Traveller ID" name="userNIC">
                            <Select onChange={setSelectedNIC}>
                                {travellers.length > 0 &&
                                    travellers.map((traveller) => (
                                        <Select.Option key={traveller.nic} value={traveller.nic}>
                                            {traveller.nic}
                                        </Select.Option>
                                    ))
                                }
                            </Select>
                        </Form.Item>

                        <Form.Item label="Booking Date" name="bookingDate">
                            <Input
                                disabled
                                value={date ? date : ''}
                                style={{ marginTop: '8px' }}
                            />
                        </Form.Item>

                        <Form.Item label="Reservation Date" name="reservationDate">
                            <DatePicker disabledDate={disabledDate} />
                        </Form.Item>

                        <Form.Item label="No Of Tickets" name="noOfTickets">
                            <InputNumber min={1} max={10} defaultValue={1} />
                        </Form.Item>

                        <Form.Item label="Route" name="route">
                            <Select options={Routes} onChange={getActiveTrainsForRoute} />
                        </Form.Item>

                        <Form.Item label="Train" name="train">
                            <Select onChange={(value) => {
                                const selectedValue = activeTrains.find((train) => train.name === value);
                                setSelectedTrain(selectedValue);
                            }}>
                                {activeTrains.length > 0 &&
                                    activeTrains.map((value) => (
                                        <Select.Option key={value.name} value={value.name}>
                                            {value.name}
                                        </Select.Option>
                                    ))
                                }
                            </Select>
                        </Form.Item>

                        <Form.Item label="Start" name="startingPoint">
                            <Select onChange={setStart}>
                                <Select.Option value="Colombo">Colombo</Select.Option>
                                <Select.Option value="Galle">Galle</Select.Option>
                                <Select.Option value="Matara">Matara</Select.Option>
                                <Select.Option value="Kandy">Kandy</Select.Option>
                            </Select>
                        </Form.Item>

                        <Form.Item label="Destination" name="destination">
                            <Select onChange={setDestination}>
                                <Select.Option value="Colombo">Colombo</Select.Option>
                                <Select.Option value="Galle">Galle</Select.Option>
                                <Select.Option value="Matara">Matara</Select.Option>
                                <Select.Option value="Kandy">Kandy</Select.Option>
                            </Select>
                        </Form.Item>

                        <Form.Item label="Time" name="time">
                            <Input
                                disabled
                                value={selectedTrain ? selectedTrain.time : ''}
                                style={{ marginTop: '8px' }}
                            />
                        </Form.Item>


                    </Form>
                </Modal>
            </div>
        </MainLayout>
    )
}
export default TicketBookingManagement;
