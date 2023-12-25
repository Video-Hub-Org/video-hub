import React, {useEffect, useState} from 'react';
import './IndexPage.css';
import '../../common/Common.css';
import {Card, Typography} from "antd";

const {Paragraph} = Typography;

const IndexPage = () => {
  const [ipInfo, setIpInfo] = useState(null);

  useEffect(() => {
    fetchIpInfo().then(() => console.log("end fetch"));
  }, []);

  const fetchIpInfo = async () => {
    try {
      const unixTime = Date.now();
      const url = `https://pubstatic.b0.upaiyun.com/?_upnode&t=${unixTime}`;
      const response = await fetch(url);
      const data = await response.json();
      setIpInfo(data);
    } catch (error) {
      console.error('Error fetching IP info:', error);
    }
  };

  return (
    <div className="index-page">
      <h2>首页</h2>
      <Card className="card-container shadow">
        {ipInfo && (
          <div>
            <Paragraph>
              <strong>IP地址:</strong> {ipInfo.remote_addr}
            </Paragraph>
            <Paragraph>
              <strong>位置:</strong> {ipInfo.remote_addr_location.province}, {ipInfo.remote_addr_location.city}
            </Paragraph>
            <Paragraph>
              <strong>时间:</strong> {new Date().toLocaleString()}
            </Paragraph>
          </div>
        )}
      </Card>
    </div>
  );
};

export default IndexPage;
