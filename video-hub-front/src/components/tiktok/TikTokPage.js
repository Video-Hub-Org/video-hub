import React, {useState} from 'react';
import {Input, Button, message, Spin, Card} from 'antd';
import axios from 'axios';
import './TikTokPage.css';
import '../../common/Common.css';

const TikTokPage = () => {
  const [videoUrl, setVideoUrl] = useState('');
  const [downloadLink, setDownloadLink] = useState('');
  const [loading, setLoading] = useState(false);
  const [carouselImages, setCarouselImages] = useState([]);
  const {TextArea} = Input;

  const handleDownload = async () => {
    try {
      message.info("别捉急，代码还没写呢~~~");

      // setLoading(true);
      // const response = await axios.post('http://localhost:6088/api/video/download', {url: videoUrl});
      // setDownloadLink(response.data);
      // setCarouselImages([]);
    } catch (error) {
      console.error('Error downloading video:', error);
      message.error('Failed to download video. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  const handleClear = () => {
    setVideoUrl(''); // 清空输入框的值
  };

  return (
    <div className="tiktok-page">
      <h2>TikTok&抖音</h2>
      <Card className="card-container shadow">
        <TextArea
          placeholder="输入TikTok或抖音视频链接"
          value={videoUrl}
          onChange={(e) => setVideoUrl(e.target.value)}
          className="video-input"
          autoSize={{minRows: 3, maxRows: 5}} // 根据需要调整行数
        />
        <Button type="primary" onClick={handleDownload} loading={loading} className="download-button">
          下载
        </Button>
        <Button onClick={handleClear} className="download-button">
          清空
        </Button>
        {loading && <Spin style={{marginLeft: '8px'}}/>}
      </Card>
      {/*       {downloadLink && (
        <div className="right-section">
          <Card className="card-container">
            <div className="preview-container">
              <video controls className="video-container">
                <source src={downloadLink} type="video/mp4"/>
                您的浏览器不支持视频播放。
              </video>
              <div className="image-carousel-container">
                <Carousel>
                  {carouselImages.map((image, index) => (
                    <div key={index}>
                      <img src={image} alt={`carousel-${index}`}/>
                    </div>
                  ))}
                </Carousel>
              </div>
            </div>
          </Card>
        </div>
      )} */}
    </div>
  );
};

export default TikTokPage;
