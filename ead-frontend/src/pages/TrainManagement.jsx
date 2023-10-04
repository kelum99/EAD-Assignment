import React, { useState } from "react";
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
const { Column, ColumnGroup } = Table;
import MainLayout from "../components/MainLayout";

const onChange = (time, timeString) => {
  console.log(time, timeString);
};

const TrainManagement = () => {
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
    <MainLayout title={"Train Management"}>
      <div>
        <Table columns={columns} />
        <Button type="primary" onClick={showModal} style={{ marginTop: 16 }}>
          Add Train
        </Button>

        {/* Train Create */}
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
      </div>
    </MainLayout>
  );
};
export default TrainManagement;
