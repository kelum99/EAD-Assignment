import React from 'react';
import { Form, Input, Button, Card, Typography, message } from 'antd';
import { useNavigate } from 'react-router-dom';
import './Styles.css';

export default function Login() {
    const [form] = Form.useForm();
    const navigate = useNavigate();
    const user = {
        email: 'admin@gmail.com',
        password: 'admin123'
    };

    const onFinish = (values) => {
        // console.log(values);
        // if (values.email === user.email && values.password === user.password) {
        //     navigate('amateurOrder');
        // } else {
        //     message.error('Email or Password Incorrect. Try Again!');
        // }
        navigate('user-management');
    };

    return (
        <div className="login-main">
            <Typography.Title
                style={{ textAlign: 'center', fontWeight: 'bold', marginBottom: 30, color: '#fff' }}>
                Welcome to Studio 73 and Color Lab
            </Typography.Title>
            <div className="login-main-component">
                <Card
                    className="loginCard"
                    title="Sign In"
                    headStyle={{ fontSize: 30, fontWeight: 'bold', border: 'none' }}>
                    <Form
                        style={{ width: 400 }}
                        layout="vertical"
                        name="basic"
                        labelCol={{ span: 8 }}
                        wrapperCol={{ span: 16 }}
                        initialValues={{ remember: true }}
                        onFinish={onFinish}
                        form={form}
                        autoComplete="off">
                        <Form.Item
                            className="lableText"
                            label="Email"
                            name="email"
                            // rules={[
                            //     {
                            //         required: true,
                            //         message: 'Please input your Email!'
                            //     }
                            // ]}
                        >
                            <Input
                                type="email"
                                style={{ width: 400, borderRadius: 4, height: 40 }}
                                placeholder="abc@gmail.com"
                            />
                        </Form.Item>

                        <Form.Item
                            className="lableText"
                            label="Password"
                            name="password"
                            // rules={[
                            //     {
                            //         required: true,
                            //         message: 'Please input your password!'
                            //     }
                            // ]}
                        >
                            <Input.Password
                                style={{ width: 400, borderRadius: 4, height: 40 }}
                                placeholder="Enter Password"
                            />
                        </Form.Item>

                        <Form.Item style={{ alignItems: 'center' }}>
                            <Button key="submit" type="primary" htmlType="submit">
                                Login
                            </Button>
                        </Form.Item>
                    </Form>
                </Card>
            </div>
        </div>
    );
}
