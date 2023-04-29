package edu.iu.c322.finalProject.orderservice.model.dto;

public class updateInventoryDto {
    private int inventory;

    private int purchased;

    private int itemId;

    public updateInventoryDto(){

    }

    public int getInventory() {
        return inventory;
    }

    public void setInventory(int inventory) {
        this.inventory = inventory;
    }

    public int getPurchased() {
        return purchased;
    }

    public void setPurchased(int purchased) {
        this.purchased = purchased;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }
}
