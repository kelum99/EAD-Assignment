import React, {useState, useEffect} from 'react';
import {
    Space,
    Table,
    Modal,
    message, Descriptions
} from "antd";
import MainLayout from "../components/MainLayout";
import useRequest from "./services/RequestContext";

const TravelerManagement = () => {
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [selected, setSelected] = useState();
    const {request} = useRequest()
    const showModal = (record) => {
        setSelected(record);
        setIsModalOpen(true);
    };
    const handleOk = () => {
        try {
            request.put(`user/update-user-status/${selected?.id}`, {status: selected?.status === 'Active' ? 'Deactivated' : 'Active'}).then((res) => {
                if (res.status === 200) {
                    message.success(`Account ${selected?.status === 'Active' ? 'Deactivated' : 'Activated'} Successfully!`)
                    getUsers();
                    handleCancel();
                } else {
                    handleCancel();
                }
            })
        } catch (e) {
            console.log('error', e)
            handleCancel();
        }
    };
    const handleCancel = () => {
        setSelected(undefined);
        setIsModalOpen(false);
    };

    const [travelers, setTravelers] = useState([]);

    const getUsers = async () => {
        await request.get('user/get-all-users')
            .then((response) => {
                if (response.status === 200) {
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
                    {record.status === 'Active' ? (
                        <a onClick={() => showModal(record)}>Deactivate</a>
                    ) : (
                        <a onClick={() => showModal(record)}>Activate</a>
                    )}
                </Space>
            ),
        },
    ];
    return (
        <MainLayout title={"Traveler Management"}>
            <div>
                <Table columns={columns} dataSource={travelers}/>

                <Modal
                    title="Traveler Account Summery"
                    open={isModalOpen}
                    onOk={handleOk}
                    okText={selected?.status === 'Active' ? 'Deactivate' : 'Activate'}
                    onCancel={handleCancel}
                >
                    {selected && (
                        <div>
                            <Descriptions column={1}>
                            <Descriptions.Item label="NIC">{selected?.nic}</Descriptions.Item>
                            <Descriptions.Item label="Name">{selected?.name}</Descriptions.Item>
                            <Descriptions.Item label="E-mail">{selected?.email}</Descriptions.Item>
                            <Descriptions.Item label="Mobile">{selected?.mobile}</Descriptions.Item>
                            <Descriptions.Item label="Status">{selected?.status}</Descriptions.Item>
                            </Descriptions>
                            <span style={{fontSize:16, fontWeight:'600', textAlign:'center', color:'red'}}>{`Your are going to ${selected?.status === 'Active' ? 'Deactivate' : 'Activate'} this traveler account!`}</span>
                        </div>
                    )}

                </Modal>
            </div>
        </MainLayout>
    );
};

export default TravelerManagement;
