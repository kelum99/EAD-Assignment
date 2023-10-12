import React from 'react';
import {Form, Input, Button, Card, Typography, message} from 'antd';
import {useNavigate} from 'react-router-dom';
import './Styles.css';
import useRequest from "./services/RequestContext";
import useUser from "./services/UserContex";

export default function Login() {
    const [form] = Form.useForm();
    const navigate = useNavigate();
    const {request, UpdateToken} = useRequest()
    const {decodeToken} = useUser()

    const onFinish = async (values) => {
        await request.post('auth/admin-login', {email: values.email, password: values.password}).then((res) => {
            if (res?.status === 404) {
                return message.error('User Not Found!');
            } else if (res?.status === 401) {
                return message.error('Unauthorized! Please check your credentials!');
            } else if (res?.status === 200) {
                const token = res.data.token;
                const role = res.data.role;
                UpdateToken(token);
                decodeToken(token);
                message.success('Login Success!');
                return navigate(role === 'Admin' ? 'user-management' : role === 'Back Officer' ? 'traveler-management'  : role === 'Travel Agent' ? 'ticket-booking-management' : '/');
            } else {
                return message.error('Internal Server Error!');
            }

        })
    };

    return (
        <div className="login-main">
            <Typography.Title
                style={{textAlign: 'center', fontWeight: 'bold', marginBottom: 30, color: '#fff'}}>
                Welcome to Studio 73 and Color Lab
            </Typography.Title>
            <div className="login-main-component">
                <Card
                    className="loginCard"
                    title="Sign In"
                    headStyle={{fontSize: 30, fontWeight: 'bold', border: 'none'}}>
                    <Form
                        style={{width: 400}}
                        layout="vertical"
                        name="basic"
                        labelCol={{span: 8}}
                        wrapperCol={{span: 16}}
                        initialValues={{remember: true}}
                        onFinish={onFinish}
                        form={form}
                        autoComplete="off">
                        <Form.Item
                            className="lableText"
                            label="Email"
                            name="email"
                            rules={[
                                {
                                    required: true,
                                    message: 'Please input your Email!'
                                }
                            ]}
                        >
                            <Input
                                type="email"
                                style={{width: 400, borderRadius: 4, height: 40}}
                                placeholder="abc@gmail.com"
                            />
                        </Form.Item>

                        <Form.Item
                            className="lableText"
                            label="Password"
                            name="password"
                            rules={[
                                {
                                    required: true,
                                    message: 'Please input your password!'
                                }
                            ]}
                        >
                            <Input.Password
                                style={{width: 400, borderRadius: 4, height: 40}}
                                placeholder="Enter Password"
                            />
                        </Form.Item>

                        <Form.Item style={{alignItems: 'center'}}>
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
