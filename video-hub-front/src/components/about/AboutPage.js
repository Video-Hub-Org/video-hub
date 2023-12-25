import React from 'react';
import './AboutPage.css';
import '../../common/Common.css';
import {Card} from "antd";

const AboutPage = () => {
  return (
    <div className="about-page">
      <h2 className="">关于</h2>
      <Card className="card-container shadow">
        <p>欢迎使用 Video Hub，一个简单的图片、视频下载和预览应用。</p>
        <p>版本号：1.0.0</p>
        <p>GitHub 仓库：
          <a href="https://github.com/Video-Hub-Org/video-hub" target="_blank" rel="noopener noreferrer">Video-Hub</a>
        </p>
        {/* 添加其他介绍信息 */}
      </Card>
    </div>
  );
};

export default AboutPage;
