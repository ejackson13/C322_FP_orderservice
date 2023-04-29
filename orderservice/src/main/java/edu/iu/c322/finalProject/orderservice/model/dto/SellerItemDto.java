package edu.iu.c322.finalProject.orderservice.model.dto;

public class SellerItemDto {
    private int sellerItemId;
    private String description;

    private String name;

    private int price;

    private int inventory;

    private int sellerId;

    public SellerItemDto(){

    }

    public int getSellerItemId() {
        return sellerItemId;
    }

    public void setSellerItemId(int sellerItemId) {
        this.sellerItemId = sellerItemId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    public int getInventory() {
        return inventory;
    }

    public void setInventory(int inventory) {
        this.inventory = inventory;
    }
}
