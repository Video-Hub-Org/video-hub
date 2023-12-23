import React, {useState} from 'react';
import {Input, Button, message, Card, Checkbox} from 'antd';
import {DownloadOutlined} from "@ant-design/icons";
import axios from 'axios';
import './RedBookPage.css';

const RedBookPage = () => {
  const [nodeUrl, setNodeUrl] = useState('');
  const [downloadLink, setDownloadLink] = useState('');
  const [loading, setLoading] = useState(false);
  const [batchDownload, setBatchDownload] = useState(false);
  const [multiUserDownload, setMultiUserDownload] = useState(false);
  const {TextArea} = Input;

  const handleDownload = async () => {
    try {
      setLoading(true);
      if (nodeUrl == null || nodeUrl.length < 1) {
        message.error("输入链接后再点下载哦~~~");
        return;
      }
      let apiUrl = '';
      if (batchDownload && multiUserDownload) {
        message.error('只能选择一种下载方式哦~~~');
        return;
      } else if (batchDownload) {
        apiUrl = 'https://localhost:6088/api/redbook/node/batch';
      } else if (multiUserDownload) {
        apiUrl = 'https://localhost:6088/api/redbook/user/batch';
      } else {
        message.error('请选择下载方式哦~~~');
        return;
      }
      const response = await axios.post(apiUrl, {url: nodeUrl});
      if (response.data.code === 1) {
        message.success(response.data.message);
      } else {
        message.error(response.data.message);
      }
      // setDownloadLink(response.data);
    } catch (error) {
      console.error('Error downloading video:', error);
      message.error('下载失败，稍后再试吧~~~');
    } finally {
      setLoading(false);
    }
  };

  const handleClear = () => {
    setNodeUrl(''); // 清空输入框的值
  };

  const handleBatchDownloadChange = (e) => {
    setBatchDownload(e.target.checked);
    if (e.target.checked) {
      // 如果批量下载笔记被选中，取消批量下载多用户笔记的选中状态
      setMultiUserDownload(false);
    }
  };

  const handleMultiUserDownloadChange = (e) => {
    setMultiUserDownload(e.target.checked);
    if (e.target.checked) {
      // 如果批量下载多用户笔记被选中，取消批量下载笔记的选中状态
      setBatchDownload(false);
    }
  };

  return (
    <div className="redbook-page">
      <h2>小红书</h2>
      <Card className="card-container">
        <TextArea
          placeholder="输入笔记或用户链接"
          value={nodeUrl}
          onChange={(e) => setNodeUrl(e.target.value)}
          className="video-input"
          autoSize={{minRows: 3, maxRows: 15}} // 根据需要调整行数
        />
        <Checkbox checked={batchDownload} onChange={handleBatchDownloadChange}>批量下载笔记</Checkbox>
        <Checkbox checked={multiUserDownload} onChange={handleMultiUserDownloadChange}>批量下载多用户笔记</Checkbox>
        <ul className="explanation-list">
          <li>说明：</li>
          <li>① 批量下载笔记：小红书【笔记】分享链接放在上面的文本框里，多个链接换行放，支持点击分享按钮生成的链接和web端的
            explore 链接。
            链接可以是一个用户的也可以是多个用户的。
          </li>
          <li>② 批量下载多用户笔记：小红书【用户】分享链接放在上面的文本框里，支持点击分享按钮生成的链接和web 端的profile
            链接。
            链接可以是一个用户也可以是多个用户，点击下载会下载用户的所有笔记。
          </li>
        </ul>
        <Button type="primary" icon={<DownloadOutlined/>} onClick={handleDownload} loading={loading}
                className="download-button">
          下载
        </Button>
        <Button onClick={handleClear} className="download-button">
          清空
        </Button>
      </Card>
      {/*       {downloadLink && (
        <Card className="card-container">
          <video controls className="video-container">
            <source src={downloadLink} type="video/mp4"/>
            您的浏览器不支持视频播放。
          </video>
          <a href={downloadLink} download>
            <Button type="primary" className="download-button">
            下载
            </Button>
          </a>
        </Card>
      )} */}
    </div>
  );
};

export default RedBookPage;
