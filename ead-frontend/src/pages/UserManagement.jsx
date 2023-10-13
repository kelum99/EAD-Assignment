import React, {useEffect, useState} from 'react';
import MainLayout from "../components/MainLayout";
import {Button, Form, Input, message, Modal, Popconfirm, Select, Space, Table} from 'antd';
import useRequest from "./services/RequestContext";

const UserManagement = () => {
    // State to store user data.
    const [data, setData] = useState([]);
    // State to control user edit mode.
    const [edit, setEdit] = useState(false);
    // State to store the selected user's ID.
    const [selectedId, setSelectedId] = useState();
    // State to control the user management modal.
    const [isModalOpen, setIsModalOpen] = useState(false);
    // Access the request function from the RequestContext.
    const {request} = useRequest();
    // Create a form instance to handle user input.
    const [form] = Form.useForm();

    // Define the columns for the user data table.
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

    // Function to open the user management modal.
    const showModal = () => {
        setIsModalOpen(true);
    };
    // Function to reset the form fields.
    const onReset = () => {
        form.resetFields();
    };
    // Function to handle the OK action in the modal.
    const handleOk = () => {
        form.submit();
    };
    // Function to close the modal and reset the selected user data.
    const handleCancel = () => {
        setIsModalOpen(false);
        setSelectedId(undefined);
        setEdit(false);
        onReset()
    };

    // Function to show the edit form with user data.
    const showEdit = (data) => {
        setSelectedId(data.id);
        setEdit(true);
        showModal();
        form.setFieldsValue(data);
    }
    // Function to fetch user data from the server.
    const getAdminData = async () => {
        try {
            await request.get('admin/get-all-admins').then((res) => {
                setData(res.data);
            }).catch((err) => message.error('Data Fetching Failed!', err));
        } catch (e) {
            console.log('Error', e)
        }
    }

    // Function to handle form submission (user creation or update).
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

    // Function to delete a user.
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
