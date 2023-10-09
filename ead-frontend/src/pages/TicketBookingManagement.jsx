import React, { useState, useEffect } from 'react';
import MainLayout from "../components/MainLayout";
import {
    Space,
    Table,
    Tag,
    TimePicker,
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

const onChange = (time, timeString) => {
    console.log(time, timeString);
};

const TicketBookingManagement = () => {

    const [isModalOpen, setIsModalOpen] = useState(false);
    const [date, setDate] = useState(null);

    const showModal = () => {
        setIsModalOpen(true);
    };
    const handleOk = () => {
        setIsModalOpen(false);
    };
    const handleCancel = () => {
        setIsModalOpen(false);
    };

    const [componentSize, setComponentSize] = useState("default");
    const onFormLayoutChange = ({ size }) => {
        setComponentSize(size);
    };

    useEffect(() => {
        const currentDate = new Date();
        const currentDateTimeString = currentDate.toISOString();
        setDate(currentDateTimeString);
    }, []);

    const columns = [
        {
            title: "Booking ID",
            dataIndex: "Id",
            key: "Id",
        },
        {
            title: "User ID",
            dataIndex: "UserID",
            key: "UserID",
        },
        {
            title: "Booking Date",
            dataIndex: "BookingDate",
            key: "BookingDate",
        },
        {
            title: "Reservation Date",
            dataIndex: "ReservationDate",
            key: "ReservationDate",
        },
        {
            title: "No Of Tickets",
            dataIndex: "NoOfTickets",
            key: "NoOfTickets",
        },
        {
            title: "Train",
            dataIndex: "Train",
            key: "Train",
        },
        {
            title: "Time",
            dataIndex: "Time",
            key: "Time",
        },
        {
            title: "User Type",
            dataIndex: "UserType",
            key: "UserType",
        },
        {
            title: "Action",
            dataIndex: "Action",
            key: "Action",
        },
    ];

    return (
        <MainLayout title={"Ticket Booking Management"}>
            <div>
                <Table columns={columns} />
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
                        labelCol={{
                            span: 8,
                        }}
                        wrapperCol={{
                            span: 14,
                        }}
                        layout="horizontal"
                        initialValues={{
                            size: componentSize,
                        }}
                        onValuesChange={onFormLayoutChange}
                        size={componentSize}
                        style={{
                            maxWidth: 600,
                        }}
                    >

                        <Form.Item label="Traveller ID">
                            <Select>
                                <Select.Option value="everyDay">EveryDay</Select.Option>
                                <Select.Option value="weekDay">WeekDay</Select.Option>
                                <Select.Option value="weekEnd">WeekEnd</Select.Option>
                                <Select.Option value="poyaDay">PoyaDay</Select.Option>
                            </Select>
                        </Form.Item>

                        <Form.Item label="Booking Date">
                            <Input
                                disabled
                                value={date ? date : ''}
                                style={{ marginTop: '8px' }}
                            />
                        </Form.Item>

                        <Form.Item label="Reservation Date">
                            <DatePicker />
                        </Form.Item>

                        <Form.Item label="No Of Tickets">
                            <InputNumber min={1} max={10} defaultValue={3}/>
                        </Form.Item>

                        <Form.Item label="Train">
                        <Select>
                                <Select.Option value="everyDay">EveryDay</Select.Option>
                                <Select.Option value="weekDay">WeekDay</Select.Option>
                                <Select.Option value="weekEnd">WeekEnd</Select.Option>
                                <Select.Option value="poyaDay">PoyaDay</Select.Option>
                            </Select>
                        </Form.Item>

                        <Form.Item label="Time">
                        <Select>
                                <Select.Option value="everyDay">EveryDay</Select.Option>
                                <Select.Option value="weekDay">WeekDay</Select.Option>
                                <Select.Option value="weekEnd">WeekEnd</Select.Option>
                                <Select.Option value="poyaDay">PoyaDay</Select.Option>
                            </Select>
                        </Form.Item>

                        
                    </Form>
                </Modal>
            </div>
        </MainLayout>
    )
}
export default TicketBookingManagement;
