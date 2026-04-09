package fr.epsi.rpgbackend.dto;

public class CharacterSpellDto {

    private Long id;
    private String spellIndex;
    private boolean prepared;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSpellIndex() { return spellIndex; }
    public void setSpellIndex(String spellIndex) { this.spellIndex = spellIndex; }

    public boolean isPrepared() { return prepared; }
    public void setPrepared(boolean prepared) { this.prepared = prepared; }
}
