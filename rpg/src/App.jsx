import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import CharacterCreator from './CharacterCreator';
import CharacterList from './CharacterList';

function App() {
  return (
    <Router>
      <div className="min-h-screen bg-transparent">
        {/* Navigation simple */}
        <nav className="p-6 flex justify-center gap-10 font-medieval border-b border-sepia-200 bg-parchment-100/50 backdrop-blur-sm">
          <Link to="/" className="text-2xl text-sepia-900 hover:text-sepia-600 transition-colors uppercase border-b-2 border-transparent hover:border-sepia-600">
            Archives
          </Link>
          <Link to="/create" className="text-2xl text-sepia-900 hover:text-sepia-600 transition-colors uppercase border-b-2 border-transparent hover:border-sepia-600">
            Création
          </Link>
        </nav>

        <Routes>
          <Route path="/" element={<CharacterList />} />
          <Route path="/create" element={<CharacterCreator />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;