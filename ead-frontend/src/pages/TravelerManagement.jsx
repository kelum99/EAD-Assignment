import React, { useState, useEffect } from 'react';

import {
    Space,
    Table,
    Modal
} from "antd";
import MainLayout from "../components/MainLayout";
import useRequest from "./services/RequestContext";

const TravelerManagement = () => {
    const [isModalOpen, setIsModalOpen] = useState(false);
    const {request} = useRequest()
    const showModal = () => {
        setIsModalOpen(true);
    };
    const handleOk = () => {
        setIsModalOpen(false);
    };
    const handleCancel = () => {
        setIsModalOpen(false);
    };

    const [travelers, setTravelers] = useState([]);

    const getUsers = async () => {
        await request.get('user/get-all-users')
            .then((response) => {
                if(response.status === 200){
                    setTravelers(response.data);
                }
            })
            .catch((error) => console.error('Error getting traveler', error));
    };

    useEffect(() => {
        getUsers();
    }, [])

    // Implement similar functions for updating, deleting, and activating/deactivating travelers.
    const columns = [
        {
            title: "NIC",
            dataIndex: "nic",
            key: "nic",
        },
        {
            title: "Name",
            dataIndex: "name",
            key: "nme",
        },
        {
            title: "Email",
            dataIndex: "email",
            key: "email",
        },
        {
            title: "Mobile",
            dataIndex: "mobile",
            key: "mobile",
        },
        {
            title: "Status",
            dataIndex: "status",
            key: "status",
        },
        {
            title: 'Action',
            key: 'action',
            render: (_, record) => (
                <Space size="middle">
                    <a onClick={showModal}>Activate</a>
                    <a onClick={showModal}>Deactivate</a>
                </Space>
            ),
        },
    ];
    return (
        <MainLayout title={"Traveler Management"}>
        <div>
            <Table columns={columns} dataSource={travelers} />

            <Modal
                title="Travel Summery"
                open={isModalOpen}
                onOk={handleOk}
                onCancel={handleCancel}
            >
                <text>NIC: </text> <br/>
                <text>Name: </text> <br/>
                <text>Email: </text> <br/>
                <text>Mobile: </text> <br/>
                <text>Status: </text> <br/>
            </Modal>
        </div>
        </MainLayout>
    );
};

export default TravelerManagement;
