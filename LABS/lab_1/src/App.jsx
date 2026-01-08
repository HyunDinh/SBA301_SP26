import NavBar from './components/NavBar';
import Orchids from './components/Orchids';
import Footer from './components/Footer';

function App() {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', minHeight: '100vh' }}>
      <NavBar />
      <main style={{ flex: 1 }}>
        <Orchids />
      </main>

      <Footer />
    </div>
  );
}

export default App;