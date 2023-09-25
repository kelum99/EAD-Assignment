import React, { useState } from 'react';
import { Layout, Typography, Menu } from 'antd';
import {
    UserOutlined,
    BarcodeOutlined,
    IdcardOutlined, CarOutlined,
} from '@ant-design/icons';
import { Link, useLocation } from 'react-router-dom';

export default function MainLayout(props) {
    const { Content, Footer, Sider, Header } = Layout;
    const location = useLocation();
    return (
        <Layout>
            {/*<Header style={{ height: 85 }}>*/}
            {/*    <div*/}
            {/*        style={{*/}
            {/*            display: 'flex',*/}
            {/*            flexDirection: 'row',*/}
            {/*            justifyContent: 'flex-start',*/}
            {/*            backgroundColor: 'rgb(0, 21, 41)',*/}
            {/*            paddingTop: 10,*/}
            {/*            marginLeft: -15*/}
            {/*        }}>*/}
            {/*        <Typography.Title style={{ margin: '10px 0px 10px 35%', color: '#fff' }}>*/}
            {/*            Studio 73 and Color Lab*/}
            {/*        </Typography.Title>*/}
            {/*    </div>*/}
            {/*</Header>*/}
            <Layout hasSider>
                <Sider width={220} collapsible>
                    <Menu
                        mode="inline"
                        theme="dark"
                        defaultSelectedKeys={['/user-management']}
                        selectedKeys={[location.pathname]}>
                        <Menu.Item key={['/user-management']}>
                            <Link to={'/user-management'}>
                                <UserOutlined />
                                <span>User Management</span>
                            </Link>
                        </Menu.Item>

                        <Menu.Item key={['/traveler-management']}>
                            <Link to={'/traveler-management'}>
                                <IdcardOutlined />
                                <span>Traveler Management</span>
                            </Link>
                        </Menu.Item>

                        <Menu.Item key={['/ticket-booking-management']}>
                            <Link to={'/ticket-booking-management'}>
                                <BarcodeOutlined />
                                <span>Ticket Booking Management</span>
                            </Link>
                        </Menu.Item>

                        <Menu.Item key={['/train-management']}>
                            <Link to={'/train-management'}>
                                <CarOutlined />
                                <span>Train Management</span>
                            </Link>
                        </Menu.Item>
                    </Menu>
                </Sider>

                <Content style={{ padding: '0 24px 24px', minHeight: '100vh' }}>
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
