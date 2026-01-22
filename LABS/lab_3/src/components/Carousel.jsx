import React from 'react';
import { Carousel } from 'react-bootstrap';
import { BannerData } from '../data/Banner';

function CustomCarousel() {
  return (
    <Carousel fade>
      {BannerData.map((item) => (
        <Carousel.Item key={item.id} style={{ height: '450px' }}>
          <img
            className="d-block w-100 h-100"
            src={item.image}
            alt={item.title}
            style={{ objectFit: 'cover' }}
          />
          <Carousel.Caption>
            <h3>{item.title}</h3>
            <p>{item.description}</p>
          </Carousel.Caption>
        </Carousel.Item>
      ))}
    </Carousel>
  );
}
export default CustomCarousel;