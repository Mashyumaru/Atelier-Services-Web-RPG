package fr.epsi.rpgbackend.dto;

import java.util.List;

public class CharacterDto {

    private Long id;
    private String name;
    private String playerName;
    private String raceIndex;
    private String classIndex;
    private int level;
    private int hpMax;
    private int hpCurrent;
    private int armorClass;
    private int speed;
    private int statStr;
    private int statDex;
    private int statCon;
    private int statInt;
    private int statWis;
    private int statCha;
    private String notes;
    private List<CharacterEquipmentDto> equipment;
    private List<CharacterSpellDto> spells;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPlayerName() { return playerName; }
    public void setPlayerName(String playerName) { this.playerName = playerName; }

    public String getRaceIndex() { return raceIndex; }
    public void setRaceIndex(String raceIndex) { this.raceIndex = raceIndex; }

    public String getClassIndex() { return classIndex; }
    public void setClassIndex(String classIndex) { this.classIndex = classIndex; }

    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }

    public int getHpMax() { return hpMax; }
    public void setHpMax(int hpMax) { this.hpMax = hpMax; }

    public int getHpCurrent() { return hpCurrent; }
    public void setHpCurrent(int hpCurrent) { this.hpCurrent = hpCurrent; }

    public int getArmorClass() { return armorClass; }
    public void setArmorClass(int armorClass) { this.armorClass = armorClass; }

    public int getSpeed() { return speed; }
    public void setSpeed(int speed) { this.speed = speed; }

    public int getStatStr() { return statStr; }
    public void setStatStr(int statStr) { this.statStr = statStr; }

    public int getStatDex() { return statDex; }
    public void setStatDex(int statDex) { this.statDex = statDex; }

    public int getStatCon() { return statCon; }
    public void setStatCon(int statCon) { this.statCon = statCon; }

    public int getStatInt() { return statInt; }
    public void setStatInt(int statInt) { this.statInt = statInt; }

    public int getStatWis() { return statWis; }
    public void setStatWis(int statWis) { this.statWis = statWis; }

    public int getStatCha() { return statCha; }
    public void setStatCha(int statCha) { this.statCha = statCha; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public List<CharacterEquipmentDto> getEquipment() { return equipment; }
    public void setEquipment(List<CharacterEquipmentDto> equipment) { this.equipment = equipment; }

    public List<CharacterSpellDto> getSpells() { return spells; }
    public void setSpells(List<CharacterSpellDto> spells) { this.spells = spells; }
}
