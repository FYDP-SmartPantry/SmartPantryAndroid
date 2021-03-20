package com.uwaterloo.smartpantry.inventory;

public class GroceryItem {

    final static String nameString = "foodname";
    final static String numberString = "count";
    final static String stockString = "stockType";
    String name = null;
    String stockType = null;
    Double number = 0.0;
    Category.CategoryEnum category = null;


    public GroceryItem() {}
    public GroceryItem(String name, Double quantity, String stockType) {
        this.name = name;
        this.number = quantity;
        this.stockType = stockType;
    }

    public String getName() { return this.name; }

    public void setName(String name) { this.name = name; }

    public String getStockType() { return this.stockType; }

    public void setStockType(String stockType) { this.stockType = stockType; }

    public Double getNumber() { return this.number; }

    public void setNumber(Double number) { this.number = number; }

}
