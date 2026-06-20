import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

const STARTING_SCORE = 8;
const MAX_SCORE = 15;
const TOTAL_POINTS_POOL = 27;
const POINT_COSTS = { 8: 0, 9: 1, 10: 2, 11: 3, 12: 4, 13: 5, 14: 7, 15: 9 };

const CharacterCreator = () => {
  const navigate = useNavigate();
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
          fetch('https://www.dnd5eapi.co/api/2014/races'),
          fetch('https://www.dnd5eapi.co/api/2014/classes')
        ]);
        const raceData = await raceRes.json();
        const classData = await classRes.json();
        setRaces(raceData.results || []);
        setClasses(classData.results || []);
      } catch (err) {
        console.error("API Fetch Error:", err);
        setRaces([]);
        setClasses([]);
      } finally {
        setLoading(false);
      }
    };
    fetchInitialData();
  }, []);

   useEffect(() => {
     if (character.race) {
       fetch(`https://www.dnd5eapi.co/api/2014/races/${character.race}`)
         .then(res => res.json())
         .then(data => setSelectedRaceDetails(data));
     }
     if (character.class) {
       fetch(`https://www.dnd5eapi.co/api/2014/classes/${character.class}`)
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

  const getMod = (val) => Math.floor((val - 10) / 2);
  const conMod = getMod(character.stats.CON);
  const dexMod = getMod(character.stats.DEX);
  
  const hp = selectedClassDetails ? selectedClassDetails.hit_die + conMod : 0;
  const ac = 10 + dexMod;

  const handleSave = async () => {
  try {
    // Mapper les stats du format {STR: val} vers statStr, statDex, etc.
    const statFields = {
      STR: 'statStr',
      DEX: 'statDex', 
      CON: 'statCon',
      INT: 'statInt',
      WIS: 'statWis',
      CHA: 'statCha'
    };
    
    const characterData = {
      name: character.name,
      playerName: character.playerName || '',
      raceIndex: character.race,
      classIndex: character.class,
      level: character.level,
      hpMax: hp,
      hpCurrent: hp,
      armorClass: ac,
      speed: selectedRaceDetails?.speed || 30,
      statStr: character.stats.STR,
      statDex: character.stats.DEX,
      statCon: character.stats.CON,
      statInt: character.stats.INT,
      statWis: character.stats.WIS,
      statCha: character.stats.CHA,
      notes: character.notes || '',
      equipment: [],
      spells: []
    };

    const response = await fetch('http://localhost:8080/api/characters', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(characterData)
    });

    if (response.ok) {
      navigate('/'); 
    } else {
      const errorData = await response.json();
      console.error("The scribe failed to record the scroll:", errorData);
    }
  } catch (err) {
    console.error("Connection to the archives lost:", err);
  }
};

  if (loading) return <div className="text-center p-20 font-medieval text-3xl">Opening the Grimoire...</div>;

  return (
    <div className="p-4 md:p-8 min-h-screen font-medieval">
      <div className="max-w-5xl mx-auto bg-parchment-100 border-4 border-sepia-700 rounded-lg shadow-2xl p-6 text-sepia-900">
        <h1 className="text-5xl font-bold mb-10 text-center text-sepia-950 tracking-widest uppercase">Adventurer Registration</h1>

        <div className="grid md:grid-cols-3 gap-10">
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
            {/* Restored Description Field */}
            <div>
              <label className="block text-xl font-bold mb-1">Chronicle Notes</label>
              <textarea 
                className="w-full p-2 border-2 border-sepia-600 rounded bg-white/60 font-sans text-sepia-950 h-32 resize-none"
                onChange={(e) => setCharacter({...character, notes: e.target.value})}
                placeholder="Appearance, backstory, or dark omens..."
              />
            </div>
          </div>

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

            <div className="grid grid-cols-2 gap-6 pt-4">
                <div className="bg-sepia-200 border-2 border-sepia-400 p-4 rounded text-center">
                    <div className="text-xl uppercase font-bold text-sepia-800 tracking-tighter text-shadow-none">Armor Class</div>
                    <div className="text-4xl font-black">{ac}</div>
                </div>
                <div className="bg-sepia-200 border-2 border-sepia-400 p-4 rounded text-center">
                    <div className="text-xl uppercase font-bold text-sepia-800 tracking-tighter">Hit Points</div>
                    <div className="text-4xl font-black">{hp > 0 ? hp : '--'}</div>
                </div>
            </div>
          </div>
        </div>

        <button 
          onClick={handleSave}
          className="mt-12 w-full bg-sepia-900 text-parchment-100 py-4 rounded font-bold text-2xl hover:bg-sepia-950 shadow-lg transition-transform active:scale-95 uppercase tracking-widest"
        >
          Seal the Scroll
        </button>
      </div>
    </div>
  );
};

export default CharacterCreator;