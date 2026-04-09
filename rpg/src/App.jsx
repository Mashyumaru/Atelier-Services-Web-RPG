import React, { useState, useEffect } from 'react';

const STARTING_SCORE = 8;
const MAX_SCORE = 15;
const TOTAL_POINTS_POOL = 27;
const POINT_COSTS = { 8: 0, 9: 1, 10: 2, 11: 3, 12: 4, 13: 5, 14: 7, 15: 9 };

const CharacterCreator = () => {
  const [races, setRaces] = useState([]);
  const [classes, setClasses] = useState([]);
  const [selectedRaceDetails, setSelectedRaceDetails] = useState(null);
  const [selectedClassDetails, setSelectedClassDetails] = useState(null);
  const [loading, setLoading] = useState(true);

  const [character, setCharacter] = useState({
    name: '',
    race: '',
    class: '',
    level: 1,
    notes: '',
    stats: { STR: 8, DEX: 8, CON: 8, INT: 8, WIS: 8, CHA: 8 }
  });

  const [pointsRemaining, setPointsRemaining] = useState(TOTAL_POINTS_POOL);

  useEffect(() => {
    const fetchInitialData = async () => {
      try {
        const [raceRes, classRes] = await Promise.all([
          fetch('https://www.dnd5eapi.co/api/races'),
          fetch('https://www.dnd5eapi.co/api/classes')
        ]);
        const raceData = await raceRes.json();
        const classData = await classRes.json();
        setRaces(raceData.results);
        setClasses(classData.results);
      } catch (err) {
        console.error("API Fetch Error:", err);
      } finally {
        setLoading(false);
      }
    };
    fetchInitialData();
  }, []);

  useEffect(() => {
    if (character.race) {
      fetch(`https://www.dnd5eapi.co/api/races/${character.race}`)
        .then(res => res.json())
        .then(data => setSelectedRaceDetails(data));
    }
    if (character.class) {
      fetch(`https://www.dnd5eapi.co/api/classes/${character.class}`)
        .then(res => res.json())
        .then(data => setSelectedClassDetails(data));
    }
  }, [character.race, character.class]);

  const handleStatChange = (stat, delta) => {
    const currentScore = character.stats[stat];
    const newScore = currentScore + delta;
    if (newScore < STARTING_SCORE || newScore > MAX_SCORE) return;

    const costDiff = POINT_COSTS[newScore] - POINT_COSTS[currentScore];
    if (pointsRemaining - costDiff >= 0) {
      setCharacter({ ...character, stats: { ...character.stats, [stat]: newScore } });
      setPointsRemaining(pointsRemaining - costDiff);
    }
  };

  // Calculations
  const getMod = (val) => Math.floor((val - 10) / 2);
  const conMod = getMod(character.stats.CON);
  const dexMod = getMod(character.stats.DEX);
  
  // HP formula: Base Hit Die + CON mod (Simplified for Level 1)
  const hp = selectedClassDetails ? selectedClassDetails.hit_die + conMod : 0;
  // AC formula: 10 + DEX mod (Unarmored)
  const ac = 10 + dexMod;

  if (loading) return <div className="text-center p-20 font-medieval text-3xl">Opening the Grimoire...</div>;

  return (
    <div className="p-4 md:p-8 min-h-screen font-medieval">
      <div className="max-w-5xl mx-auto bg-parchment-100 border-4 border-sepia-700 rounded-lg shadow-2xl p-6 text-sepia-900">
        <h1 className="text-5xl font-bold mb-10 text-center text-sepia-950 tracking-widest uppercase">Adventurer Registration</h1>

        <div className="grid md:grid-cols-3 gap-10">
          {/* Section I: Identity */}
          <div className="space-y-6">
            <h2 className="text-3xl text-sepia-950 border-b-2 border-sepia-400 pb-2 italic">I. Identity</h2>
            <div>
              <label className="block text-xl font-bold mb-1">Hero Name</label>
              <input 
                className="w-full p-2 border-2 border-sepia-600 rounded bg-white/60 font-sans text-sepia-950"
                onChange={(e) => setCharacter({...character, name: e.target.value})}
                placeholder="Name"
              />
            </div>
            <div className="grid grid-cols-2 gap-2">
                <div>
                    <label className="block text-xl font-bold mb-1">Level</label>
                    <input 
                        type="number"
                        min="1"
                        max="20"
                        value={character.level}
                        className="w-full p-2 border-2 border-sepia-600 rounded bg-white/60 font-sans text-sepia-950"
                        onChange={(e) => setCharacter({...character, level: parseInt(e.target.value) || 1})}
                    />
                </div>
                <div>
                    <label className="block text-xl font-bold mb-1">Speed</label>
                    <div className="p-2 border-2 border-dashed border-sepia-400 rounded text-center text-2xl font-bold">
                        {selectedRaceDetails?.speed || 0}
                    </div>
                </div>
            </div>
            <div>
              <label className="block text-xl font-bold mb-1">Race</label>
              <select 
                className="w-full p-2 border-2 border-sepia-600 rounded bg-white/60 text-sepia-950"
                onChange={(e) => setCharacter({...character, race: e.target.value})}
              >
                <option value="">-- Select Race --</option>
                {races.map(r => <option key={r.index} value={r.index}>{r.name}</option>)}
              </select>
            </div>
            <div>
              <label className="block text-xl font-bold mb-1">Class</label>
              <select 
                className="w-full p-2 border-2 border-sepia-600 rounded bg-white/60 text-sepia-950"
                onChange={(e) => setCharacter({...character, class: e.target.value})}
              >
                <option value="">-- Select Class --</option>
                {classes.map(c => <option key={c.index} value={c.index}>{c.name}</option>)}
              </select>
            </div>
            <div>
              <label className="block text-xl font-bold mb-1">Description Notes</label>
              <textarea 
                className="w-full p-2 border-2 border-sepia-600 rounded bg-white/60 font-sans text-sepia-950 h-24"
                onChange={(e) => setCharacter({...character, notes: e.target.value})}
                placeholder="Appearance, backstory..."
              />
            </div>
          </div>

          {/* Section II: Abilities */}
          <div className="md:col-span-2 space-y-6">
            <div className="flex justify-between items-end border-b-2 border-sepia-400 pb-2">
              <h2 className="text-3xl text-sepia-950 italic ">II. Ability Scores</h2>
              <span className="bg-sepia-800 text-parchment-100 px-4 py-1 rounded-full text-xl font-bold">
                Points: {pointsRemaining}
              </span>
            </div>
            <div className="grid grid-cols-2 sm:grid-cols-3 gap-4">
              {Object.keys(character.stats).map(stat => (
                <div key={stat} className="bg-white/60 border-2 border-sepia-300 rounded-lg p-4 text-center">
                  <div className="text-sm font-sans font-bold text-sepia-700 uppercase tracking-widest">{stat}</div>
                  <div className="text-5xl font-bold my-2 text-sepia-950">{character.stats[stat]}</div>
                  <div className="flex justify-center gap-4">
                    <button 
                      onClick={() => handleStatChange(stat, -1)}
                      className="w-10 h-10 flex items-center justify-center bg-sepia-300 text-sepia-950 rounded-full font-bold hover:bg-sepia-400"
                    >-</button>
                    <button 
                      onClick={() => handleStatChange(stat, 1)}
                      className="w-10 h-10 flex items-center justify-center bg-sepia-700 text-white rounded-full font-bold hover:bg-sepia-900"
                    >+</button>
                  </div>
                </div>
              ))}
            </div>

            {/* Combat Quick Look */}
            <div className="grid grid-cols-2 gap-6 pt-4">
                <div className="bg-sepia-200 border-2 border-sepia-400 p-4 rounded text-center">
                    <div className="text-xl uppercase font-bold text-sepia-800 tracking-tighter">Armor Class</div>
                    <div className="text-4xl font-black">{ac}</div>
                </div>
                <div className="bg-sepia-200 border-2 border-sepia-400 p-4 rounded text-center">
                    <div className="text-xl uppercase font-bold text-sepia-800 tracking-tighter">Hit Points</div>
                    <div className="text-4xl font-black">{hp > 0 ? hp : '--'}</div>
                </div>
            </div>
          </div>
        </div>

        {/* Section III: Final Scroll */}
        <div className="mt-12 p-8 bg-sepia-950 text-parchment-100 rounded-lg shadow-inner relative overflow-hidden">
          <div className="absolute top-0 right-0 p-4 text-sepia-400 text-6xl opacity-20 font-sans font-black">LVL {character.level}</div>
          <h2 className="text-3xl mb-8 text-center tracking-widest border-b border-sepia-700 pb-4 uppercase">Final Character Scroll</h2>
          
          <div className="grid md:grid-cols-2 gap-10">
            <div className="space-y-4">
              <p className="text-2xl"><span className="text-sepia-400">Name:</span> {character.name || "..."}</p>
              <div className="flex gap-10">
                  <p className="text-2xl"><span className="text-sepia-400">Race:</span> {selectedRaceDetails?.name || "..."}</p>
                  <p className="text-2xl"><span className="text-sepia-400">Class:</span> {selectedClassDetails?.name || "..."}</p>
              </div>
              <div className="flex gap-8 py-2 border-y border-sepia-800">
                <p className="text-xl"><span className="text-sepia-400">HP:</span> {hp}</p>
                <p className="text-xl"><span className="text-sepia-400">AC:</span> {ac}</p>
                <p className="text-xl"><span className="text-sepia-400">Speed:</span> {selectedRaceDetails?.speed}ft</p>
              </div>
              <div className="pt-2">
                  <span className="text-sepia-400 block text-sm uppercase mb-1">Chronicle Notes:</span>
                  <p className="text-lg italic font-sans leading-tight opacity-90 whitespace-pre-wrap">{character.notes || "No tales yet told..."}</p>
              </div>
            </div>

            <div className="grid grid-cols-3 gap-3">
              {Object.entries(character.stats).map(([key, val]) => {
                const mod = getMod(val);
                return (
                  <div key={key} className="border-2 border-sepia-800 p-3 rounded-lg text-center bg-black/20">
                    <div className="text-xs text-sepia-500 uppercase font-sans font-bold">{key}</div>
                    <div className="text-3xl font-bold">{val}</div>
                    <div className="text-lg bg-parchment-100 text-sepia-950 rounded-md font-black mt-1">
                      {mod >= 0 ? `+${mod}` : mod}
                    </div>
                  </div>
                );
              })}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default CharacterCreator;