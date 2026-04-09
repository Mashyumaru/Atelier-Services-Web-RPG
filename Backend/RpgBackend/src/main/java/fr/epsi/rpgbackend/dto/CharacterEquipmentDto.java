package fr.epsi.rpgbackend.dto;

public class CharacterEquipmentDto {

    private Long id;
    private String itemIndex;
    private int quantity;
    private boolean equipped;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getItemIndex() { return itemIndex; }
    public void setItemIndex(String itemIndex) { this.itemIndex = itemIndex; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public boolean isEquipped() { return equipped; }
    public void setEquipped(boolean equipped) { this.equipped = equipped; }
}
