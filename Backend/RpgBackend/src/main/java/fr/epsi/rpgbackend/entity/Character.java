package fr.epsi.rpgbackend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "characters")
public class Character {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "player_name", length = 100)
    private String playerName;

    @Column(name = "race_index", nullable = false, length = 50)
    private String raceIndex;

    @Column(name = "class_index", nullable = false, length = 50)
    private String classIndex;

    @Column(nullable = false)
    private int level = 1;

    @Column(name = "hp_max", nullable = false)
    private int hpMax;

    @Column(name = "hp_current", nullable = false)
    private int hpCurrent;

    @Column(name = "armor_class", nullable = false)
    private int armorClass;

    @Column(nullable = false)
    private int speed;

    @Column(name = "stat_str")
    private int statStr = 10;

    @Column(name = "stat_dex")
    private int statDex = 10;

    @Column(name = "stat_con")
    private int statCon = 10;

    @Column(name = "stat_int")
    private int statInt = 10;

    @Column(name = "stat_wis")
    private int statWis = 10;

    @Column(name = "stat_cha")
    private int statCha = 10;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "character", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CharacterEquipment> equipment = new ArrayList<>();

    @OneToMany(mappedBy = "character", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CharacterSpell> spells = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

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

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public List<CharacterEquipment> getEquipment() { return equipment; }
    public void setEquipment(List<CharacterEquipment> equipment) { this.equipment = equipment; }

    public List<CharacterSpell> getSpells() { return spells; }
    public void setSpells(List<CharacterSpell> spells) { this.spells = spells; }
}
