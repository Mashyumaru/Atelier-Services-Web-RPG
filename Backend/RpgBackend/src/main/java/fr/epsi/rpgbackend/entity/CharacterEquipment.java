package fr.epsi.rpgbackend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "character_equipment")
public class CharacterEquipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_id", nullable = false)
    private Character character;

    @Column(name = "item_index", nullable = false, length = 50)
    private String itemIndex;

    @Column(nullable = false)
    private int quantity = 1;

    @Column(nullable = false)
    private boolean equipped = false;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Character getCharacter() { return character; }
    public void setCharacter(Character character) { this.character = character; }

    public String getItemIndex() { return itemIndex; }
    public void setItemIndex(String itemIndex) { this.itemIndex = itemIndex; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public boolean isEquipped() { return equipped; }
    public void setEquipped(boolean equipped) { this.equipped = equipped; }
}
