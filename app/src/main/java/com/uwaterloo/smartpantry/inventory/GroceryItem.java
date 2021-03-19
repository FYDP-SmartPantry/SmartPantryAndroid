package com.uwaterloo.smartpantry.inventory;

public class GroceryItem {

    final static String nameString = "foodname";
    final static String numberString = "count";

    String name = null;
    int number = 0;

    public GroceryItem() {}
    public GroceryItem(String name, int quantity) {
        this.name = name;
        this.number = quantity;
    }

    public String getName() { return this.name; }

    public void setName(String name) { this.name = name; }

    public int getNumber() { return this.number; }

    public void setNumber(int number) { this.number = number; }
}
