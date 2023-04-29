package edu.iu.c322.finalProject.orderservice.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "SellerItem")
public class SellerItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seller_item_id")
    private int sellerItemId;

    @Column(name = "description")
    private String description;

    @Column(name = "inventory")
    private int inventory;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private int price;

    @Column(name = "sellerId")
    private int sellerId;

    public SellerItem() {
    }

    public SellerItem(String description, int inventory) {
        this.description = description;
        this.inventory = inventory;
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

    public int getInventory() {
        return inventory;
    }

    public void setInventory(int inventory) {
        this.inventory = inventory;
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

    @Override
    public String toString() {
        return "SellerItem{" +
                "sellerItemId=" + sellerItemId +
                ", description='" + description + '\'' +
                ", inventory=" + inventory +
                '}';
    }
}