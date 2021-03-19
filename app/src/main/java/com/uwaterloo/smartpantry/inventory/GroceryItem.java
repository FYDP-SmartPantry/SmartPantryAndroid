package com.uwaterloo.smartpantry.inventory;

public class GroceryItem {

    final static String nameString = "foodname";
    final static String numberString = "count";
    final static String stockString = "stockType";
    String name = null;
    int number = 0;
    String stockType = null;


    public GroceryItem() {}
    public GroceryItem(String name, int quantity, String stockType) {
        this.name = name;
        this.number = quantity;
        this.stockType = stockType;
    }

    public String getName() { return this.name; }

    public void setName(String name) { this.name = name; }

    public int getNumber() { return this.number; }

    public void setNumber(int number) { this.number = number; }

    public String getStockType() { return this.stockType; }

    public void setStockType(String stockType) { this.stockType = stockType; }
}
