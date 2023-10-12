import React, {useEffect, useState} from 'react';
import {Layout, Typography, Menu} from 'antd';
import {
    UserOutlined,
    BarcodeOutlined,
    IdcardOutlined, CarOutlined,
} from '@ant-design/icons';
import {Link, useLocation, useNavigate} from 'react-router-dom';
import useRequest from "../pages/services/RequestContext";
import useUser from "../pages/services/UserContex";

export default function MainLayout(props) {
    const {Content, Footer, Sider, Header} = Layout;
    const [role, setRole] = useState();
    const location = useLocation();
    const {UpdateToken} = useRequest()
    const navigate = useNavigate();
    const {user} = useUser()
    const logout = () => {
        UpdateToken(undefined);
        return navigate('/');
    }

    useEffect(() => {
        if (user) {
            setRole(user.Role)
        }
    }, [user])
    return (
        <Layout>
            <Layout hasSider>
                <Sider width={220} collapsible>
                    <Menu
                        mode="inline"
                        theme="dark"
                        defaultSelectedKeys={['/user-management']}
                        selectedKeys={[location.pathname]}>
                        {role === 'Admin' && (
                            <Menu.Item key={['/user-management']}>
                                <Link to={'/user-management'}>
                                    <UserOutlined/>
                                    <span>User Management</span>
                                </Link>
                            </Menu.Item>

                        )}
                        {(role === 'Admin' || role === 'Back Officer') && (
                            <Menu.Item key={['/traveler-management']}>
                                <Link to={'/traveler-management'}>
                                    <IdcardOutlined/>
                                    <span>Traveler Management</span>
                                </Link>
                            </Menu.Item>
                        )}
                        {(role === 'Admin' || role === 'Travel Agent') && (
                            <Menu.Item key={['/ticket-booking-management']}>
                                <Link to={'/ticket-booking-management'}>
                                    <BarcodeOutlined/>
                                    <span>Ticket Booking Management</span>
                                </Link>
                            </Menu.Item>
                        )}
                        {(role === 'Admin' || role === 'Back Officer') && (
                            <Menu.Item key={['/train-management']}>
                                <Link to={'/train-management'}>
                                    <CarOutlined/>
                                    <span>Train Management</span>
                                </Link>
                            </Menu.Item>
                        )}
                        <Menu.Item key={['log-out']}>
                            {/*<CarOutlined />*/}
                            <span onClick={logout}>Log Out</span>
                        </Menu.Item>
                    </Menu>
                </Sider>

                <Content style={{padding: '0 24px 24px', minHeight: '100vh'}}>
                    <div>
                        {props.title && (
                            <div>
                                <h2>{props.title}</h2>
                            </div>
                        )}
                        <div>{props.children}</div>
                    </div>
                </Content>
            </Layout>
            <Footer
                style={{
                    textAlign: 'center',
                    padding: 12
                }}>
                Copyright 2022 ©Studio 73. All Right Reserved.
            </Footer>
        </Layout>
    );
}
