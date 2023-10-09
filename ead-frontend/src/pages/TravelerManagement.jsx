import React, { useState, useEffect } from 'react';
import axios from 'axios';
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
import MainLayout from "../components/MainLayout";

const onChange = (time, timeString) => {
    console.log(time, timeString);
};
const TravelerManagement = () => {
    const [isModalOpen, setIsModalOpen] = useState(false);
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

    const [travelers, setTravelers] = useState([]);
    const [newTraveler, setNewTraveler] = useState({ nic: '', name: '' });

    useEffect(() => {
        // Fetch travelers from the backend API and set the 'travelers' state.
        axios.get('/api/traveler')
            .then((response) => setTravelers(response.data))
            .catch((error) => console.error('Error fetching travelers', error));
    }, []);

    const handleCreateTraveler = () => {
        // Send a POST request to create a new traveler using 'newTraveler' state.
        axios.post('/api/traveler/create', newTraveler)
            .then((response) => {
                setTravelers([...travelers, response.data]);
                setNewTraveler({ nic: '', name: '' });
            })
            .catch((error) => console.error('Error creating traveler', error));
    };

    // Implement similar functions for updating, deleting, and activating/deactivating travelers.
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
            title: "Action",
            dataIndex: "action",
            key: "action",
        },
    ];
    return (
        <MainLayout title={"Traveler Management"}>
        <div>
            <Table columns={columns} />
            <Button type="primary" onClick={showModal} style={{ marginTop: 16 }}>
                Add Train
            </Button>

            <Modal
                title="Create Train Schedule"
                open={isModalOpen}
                onOk={handleOk}
                onCancel={handleCancel}
            >
                <Form
                    labelCol={{
                        span: 4,
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
                    <Form.Item label="Input">
                        <Input />
                    </Form.Item>

                    <Form.Item label="Schedule">
                        <Select>
                            <Select.Option value="everyDay">EveryDay</Select.Option>
                            <Select.Option value="weekDay">WeekDay</Select.Option>
                            <Select.Option value="weekEnd">WeekEnd</Select.Option>
                            <Select.Option value="poyaDay">PoyaDay</Select.Option>
                        </Select>
                    </Form.Item>

                    <Form.Item label="Route">
                        <Select>
                            <Select.Option value="everyDay">Colombo to Galle</Select.Option>
                            <Select.Option value="weekDay">WeekDay</Select.Option>
                            <Select.Option value="weekEnd">WeekEnd</Select.Option>
                            <Select.Option value="poyaDay">PoyaDay</Select.Option>
                        </Select>
                    </Form.Item>

                    <Form.Item label="Time">
                        <Space wrap>
                            {/* <TimePicker use12Hours onChange={onChange} /> */}
                            {/* <TimePicker use12Hours format="h:mm:ss A" onChange={onChange} /> */}
                            <TimePicker use12Hours format="h:mm a" onChange={onChange} />
                        </Space>
                    </Form.Item>

                    <Form.Item label="Updated Date">
                        <DatePicker />
                    </Form.Item>
                </Form>
            </Modal>
            {/* List of Travelers */}
            {/*<div>*/}
            {/*    <h2>List of Travelers</h2>*/}
            {/*    <table>*/}
            {/*        <thead>*/}
            {/*        <tr>*/}
            {/*            <th>NIC</th>*/}
            {/*            <th>Name</th>*/}
            {/*            <th>Actions</th>*/}
            {/*        </tr>*/}
            {/*        </thead>*/}
            {/*        <tbody>*/}
            {/*        {travelers.map((traveler) => (*/}
            {/*            <tr key={traveler._id}>*/}
            {/*                <td>{traveler.nic}</td>*/}
            {/*                <td>{traveler.name}</td>*/}
            {/*                <td>*/}
            {/*                    <button onClick={() => handleUpdateTraveler(traveler._id)}>Edit</button>*/}
            {/*                    <button onClick={() => handleDeleteTraveler(traveler._id)}>Delete</button>*/}
            {/*                    {traveler.is_active ? (*/}
            {/*                        <button onClick={() => handleActivateDeactivate(traveler._id, 'deactivate')}>*/}
            {/*                            Deactivate*/}
            {/*                        </button>*/}
            {/*                    ) : (*/}
            {/*                        <button onClick={() => handleActivateDeactivate(traveler._id, 'activate')}>*/}
            {/*                            Activate*/}
            {/*                        </button>*/}
            {/*                    )}*/}
            {/*                </td>*/}
            {/*            </tr>*/}
            {/*        ))}*/}
            {/*        </tbody>*/}
            {/*    </table>*/}
            {/*</div>*/}
        </div>
        </MainLayout>
    );
};

export default TravelerManagement;
