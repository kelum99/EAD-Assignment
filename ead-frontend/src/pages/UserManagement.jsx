import React, {useEffect, useState} from 'react';
import MainLayout from "../components/MainLayout";
import {Button, Form, Input, message, Modal, Popconfirm, Select, Space, Table} from 'antd';
import useRequest from "./services/RequestContext";

const UserManagement = () => {
    const [data, setData] = useState([]);
    const [edit, setEdit] = useState(false);
    const [selectedId, setSelectedId] = useState();
    const [isModalOpen, setIsModalOpen] = useState(false);
    const {request} = useRequest();
    const [form] = Form.useForm();

    const columns = [
        {
            title: 'Name',
            dataIndex: 'name',
            key: 'name',
        },
        {
            title: 'Email',
            dataIndex: 'email',
            key: 'email',
        },
        {
            title: 'Role',
            dataIndex: 'role',
            key: 'role',
        },
        {
            title: 'Action',
            key: 'action',
            render: (_, record) => (
                <Space size="middle">
                    <a onClick={() => showEdit(record)}>Edit</a>
                    <Popconfirm
                        title="Remove User"
                        description="Are you sure to remove this user?"
                        onConfirm={() => deleteUser(record.id)}
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
        onReset()
    };

    const showEdit = (data) => {
        setSelectedId(data.id);
        setEdit(true);
        showModal();
        form.setFieldsValue(data);
    }

    const getAdminData = async () => {
        try {
            await request.get('admin/get-all-admins').then((res) => {
                setData(res.data);
            }).catch((err) => message.error('Data Fetching Failed!', err));
        } catch (e) {
            console.log('Error', e)
        }
    }

    const onFinish = (values) => {
        try {
            if (edit && selectedId) {
                const updates = {
                    Name: values.name,
                    Email: values.email,
                    Role: values.role
                }
                request.put(`admin/update-admin/${selectedId}`, updates).then((res) => {
                    if (res.status === 200) {
                        getAdminData();
                        message.success('Update Success!');
                        handleCancel();
                    }
                }).catch(() => message.error('Update Failed!'));
            } else {
                const admin = {
                    Name: values.name,
                    Email: values.email,
                    Password: values.password,
                    Role: values.role
                }
                request.post(`admin/create-admin`, admin).then((res) => {
                    if (res.status === 201) {
                        getAdminData();
                        message.success('User Create Success!');
                        handleCancel();
                    }
                }).catch(() => message.error('User Create Failed!'));
            }

        } catch (e) {
            console.log('error', e)
        }
    }

    const deleteUser = (id) => {
        try{
            request.delete(`admin/delete-admin/${id}`).then((res) => {
                if (res.status === 200) {
                    getAdminData();
                    message.success('User Remove Success!');
                }
            }).catch(() => message.error('User Remove Failed!'));
        } catch (e) {
            console.log('error', e)
        }
    }

    useEffect(() => {
        getAdminData();
    }, [])
    return (
        <MainLayout title={"User Management"}>
            <Table columns={columns} dataSource={data} rowKey={(record) => record.id}/>
            <Button type={"primary"} onClick={showModal}>Create User</Button>
            <Modal
                title={'Update User Details'}
                okText={edit ? 'Update' : 'Create'}
                open={isModalOpen} onOk={handleOk}
                maskClosable={false}
                onCancel={handleCancel}>
                <Form onFinish={onFinish} form={form} layout="horizontal" autoComplete="off" labelCol={{span: 4}}
                      wrapperCol={{span: 16}}>
                    <Form.Item
                        name="name"
                        label="Name"
                        rules={[
                            {
                                required: true,
                                message: 'Name Required!'
                            }
                        ]}
                    >
                        <Input/>
                    </Form.Item>
                    <Form.Item
                        name="email"
                        label="Email"
                        rules={[
                            {
                                required: true,
                                message: 'Email Required!',
                            },
                            {
                                type: 'email',
                                message: 'Email Not Valid!',
                            }
                        ]}
                    >
                        <Input/>
                    </Form.Item>
                    {!edit && (
                        <Form.Item
                            name="password"
                            label="Password"
                            rules={[
                                {
                                    required: true,
                                    message: 'Password Required!'
                                }
                            ]}
                        >
                            <Input.Password/>
                        </Form.Item>
                    )}

                    <Form.Item
                        name="role"
                        label="Role"
                        rules={[
                            {
                                required: true,
                                message: 'Role Required!'
                            }
                        ]}
                    >
                        <Select>
                            <Select.Option value="Admin">Admin</Select.Option>
                            <Select.Option value="Travel Agent">Travel Agent</Select.Option>
                            <Select.Option value="Back Officer">Back Officer</Select.Option>
                        </Select>
                    </Form.Item>
                </Form>
            </Modal>
        </MainLayout>
    )
}
export default UserManagement;
