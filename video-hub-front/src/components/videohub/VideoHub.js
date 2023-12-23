// video-hub-front/src/components/videohub/VideoHub.js
import React, {useState} from 'react';
import {
  HomeTwoTone, BookTwoTone, VideoCameraTwoTone, InfoCircleTwoTone
} from '@ant-design/icons';
import './VideoHub.css';
import IndexPage from '../index/IndexPage'; // 导入 IndexPage 组件
import RedBookPage from '../redbook/RedBookPage'; // 导入 RedBookPage 组件
import TikTokPage from '../tiktok/TikTokPage'; // 导入 TikTokPage 组件
import AboutPage from '../about/AboutPage'; // 导入 AboutPage 组件
import {Layout, Menu} from 'antd'; // 导入 Layout 组件

const {Footer} = Layout; // 从 Layout 中解构出 Footer

const VideoHub = () => {
  const [selectedMenu, setSelectedMenu] = useState('index');

  const renderPage = () => {
    switch (selectedMenu) {
      case 'index':
        return <IndexPage/>;
      case 'redbook':
        return <RedBookPage/>;
      case 'tiktok':
        return <TikTokPage/>;
      case 'about':
        return <AboutPage/>;
      default:
        return null;
    }
  };

  return (
    <Layout className="video-hub-container">
      <div>
        <Menu
          theme="light"
          mode="horizontal"
          selectedKeys={[selectedMenu]}
          onClick={({ key }) => setSelectedMenu(key)}
        >
          <Menu.Item key="index" icon={<HomeTwoTone />}>
            首页
          </Menu.Item>
          <Menu.Item key="redbook" icon={<BookTwoTone />}>
            小红书
          </Menu.Item>
          <Menu.Item key="tiktok" icon={<VideoCameraTwoTone />}>
            TikTok
          </Menu.Item>
          <Menu.Item key="about" icon={<InfoCircleTwoTone />}>
            关于
          </Menu.Item>
        </Menu>
      </div>
      <Layout className="content-layout">
        <div className="content">
          {renderPage()}
        </div>
        <Footer className="ant-layout-footer">Video-Hub-Org ©2023 Created by @fmz200</Footer>
      </Layout>
    </Layout>
  );
};

export default VideoHub;
