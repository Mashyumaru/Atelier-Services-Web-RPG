import React, { useState, useEffect } from 'react';

const CharacterList = () => {
  const [characters, setCharacters] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // Replace with your local backend URL
    const fetchCharacters = async () => {
      try {
        const response = await fetch('http://localhost:5000/api/characters');
        const data = await response.json();
        setCharacters(data);
      } catch (err) {
        console.error("Failed to fetch archives:", err);
      } finally {
        setLoading(false);
      }
    };
    fetchCharacters();
  }, []);

  if (loading) {
    return (
      <div className="text-center p-20 font-medieval text-3xl animate-pulse">
        Consulting the Great Archives...
      </div>
    );
  }

  return (
    <div className="p-8 font-medieval max-w-7xl mx-auto">
      <header className="mb-12 text-center">
        <h1 className="text-5xl font-bold text-sepia-950 uppercase tracking-widest">
          Adventurer Registry
        </h1>
        <div className="h-1 w-40 bg-sepia-700 mx-auto mt-4"></div>
      </header>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-10">
        {characters.map((char) => (
          <div 
            key={char.id} 
            className="bg-parchment-100 border-2 border-sepia-700 p-6 shadow-parchment relative hover:-translate-y-1 transition-all duration-300"
          >
            {/* Level Seal */}
            <div className="absolute -top-4 -left-4 w-14 h-14 bg-sepia-900 border-2 border-sepia-950 rounded-full flex items-center justify-center text-parchment-100 shadow-lg">
              <span className="text-xs uppercase mr-1">Lvl</span>
              <span className="text-xl font-bold">{char.level}</span>
            </div>

            <h2 className="text-3xl font-bold text-sepia-950 border-b-2 border-sepia-300 mb-2 truncate pt-2">
              {char.name || "Unknown Hero"}
            </h2>

            <div className="flex justify-between font-sans italic text-sepia-700 mb-6 text-sm uppercase font-bold tracking-tighter">
              <span>{char.race}</span>
              <span>{char.class}</span>
            </div>

            <div className="grid grid-cols-3 gap-3 mb-6">
              {Object.entries(char.stats).map(([ability, value]) => (
                <div key={ability} className="bg-sepia-200/50 border border-sepia-300 rounded p-2 text-center">
                  <div className="text-[10px] font-sans font-black text-sepia-600 uppercase leading-none">
                    {ability}
                  </div>
                  <div className="text-xl font-bold text-sepia-950">{value}</div>
                </div>
              ))}
            </div>

            <div className="bg-white/40 p-3 rounded border border-dashed border-sepia-400 min-h-[80px]">
              <span className="text-[10px] uppercase font-bold text-sepia-500 block mb-1">Chronicle:</span>
              <p className="text-sm font-sans italic text-sepia-800 line-clamp-3 leading-tight">
                {char.notes || "No tales yet told of this wanderer..."}
              </p>
            </div>

            <button className="mt-6 w-full py-2 bg-sepia-950 text-parchment-100 hover:bg-black transition-colors uppercase tracking-widest text-xs font-bold">
              View Full Scroll
            </button>
          </div>
        ))}
      </div>
      
      {characters.length === 0 && (
        <div className="text-center py-20 border-2 border-dashed border-sepia-300 text-sepia-600">
          The registry is currently empty. No heroes have been recorded yet.
        </div>
      )}
    </div>
  );
};

export default CharacterList;