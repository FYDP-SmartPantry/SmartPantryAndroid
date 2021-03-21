package com.uwaterloo.smartpantry.ui.foodinventory;

public class FoodItem {
    private String name;
    private String quantity;
    private String expiration;

    public FoodItem(String name, String quantity, String expiration) {
        this.name = name;
        this.quantity = quantity;
        this.expiration = expiration;
    }

    public String getName() {
        return this.name;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getExpiration() {
        return expiration;
    }
}
