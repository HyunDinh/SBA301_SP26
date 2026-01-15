import React from 'react';
import { Outlet } from 'react-router-dom';
import NavBar from '../components/NavBar';
import Footer from '../components/Footer';
import CustomCarousel from '../components/Carousel';

function MainLayout() {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', minHeight: '100vh' }}>
      <NavBar />
      <CustomCarousel /> 
      <main style={{ flex: 1, paddingBottom: '2rem' }}>
        <Outlet /> 
      </main>
      <Footer />
    </div>
  );
}
export default MainLayout;