import React, {useEffect, useState} from 'react';
import MainLayout from "../components/MainLayout";
import {message, Space, Table, Tag} from 'antd';
import useRequest from "./services/RequestContext";

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
                <a>Edit</a>
                <a>Delete</a>
            </Space>
        ),
    },
];
const UserManagement = () => {
    const [data, setData] = useState();
    const {request} = useRequest();
    const getAdminData = async () => {
        try{
            await request.get('admin/get-all-admins').then((res) => {
                setData(res.data);
            }).catch((err) => message.error('Data Fetching Failed!', err));
        } catch (e) {
            console.log('Error', e)
        }
    }

    useEffect(() => {
        getAdminData();
    }, [])
    return (
        <MainLayout title={"User Management"}>
            <Table columns={columns} dataSource={data} />
        </MainLayout>
    )
}
export default UserManagement;
