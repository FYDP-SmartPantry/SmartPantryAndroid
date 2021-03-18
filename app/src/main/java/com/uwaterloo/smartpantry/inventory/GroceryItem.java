package com.uwaterloo.smartpantry.inventory;

public class GroceryItem implements Item {

    final static String nameString = "foodname";
    final static String stockTypeString = "stockType";
    final static String numberString = "count";
    final static String categoryString = "category";

    String name = null;
    String stockType = null;
    int number = 0;
    Category.CategoryEnum category = null;

    public GroceryItem() {}
    public GroceryItem(String name, int quantity) {
        this.name = name;
        this.number = quantity;
    }

    public GroceryItem(Item item) {
        this.name = item.getName();
        this.stockType = item.getStockType();
        this.number = item.getNumber();
        this.category = item.getCategory();
    }

    @Override
    public String getName() { return this.name; }

    @Override
    public void setName(String name) { this.name = name; }

    @Override
    public String getStockType() { return this.stockType; }

    @Override
    public void setStockType(String stockType) { this.stockType = stockType; }

    @Override
    public int getNumber() { return this.number; }

    @Override
    public void setNumber(int number) { this.number = number; }

    @Override
    public Category.CategoryEnum getCategory() { return this.category; }

    @Override
    public void setCategory(Category.CategoryEnum category) { this.category = category; }
}
