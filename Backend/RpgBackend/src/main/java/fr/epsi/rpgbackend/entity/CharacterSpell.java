package fr.epsi.rpgbackend.entity;

import jakarta.persistence.*;

@Entity
@Table(
    name = "character_spells",
    uniqueConstraints = @UniqueConstraint(columnNames = {"character_id", "spell_index"})
)
public class CharacterSpell {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_id", nullable = false)
    private Character character;

    @Column(name = "spell_index", nullable = false, length = 50)
    private String spellIndex;

    @Column(name = "is_prepared", nullable = false)
    private boolean isPrepared = true;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Character getCharacter() { return character; }
    public void setCharacter(Character character) { this.character = character; }

    public String getSpellIndex() { return spellIndex; }
    public void setSpellIndex(String spellIndex) { this.spellIndex = spellIndex; }

    public boolean isPrepared() { return isPrepared; }
    public void setPrepared(boolean prepared) { isPrepared = prepared; }
}
