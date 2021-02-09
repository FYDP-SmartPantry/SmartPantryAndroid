package com.uwaterloo.smartpantry.inventory;

public class Food implements FoodInterface {

    final static String nameString = "foodname";
    final static String stockTypeString = "stockType";
    final static String numberString = "count";
    final static String categoryString = "category";
    final static String expirationDateString = "expiration_date";

    String name = null;
    String stockType = null;
    int number = 0;
    Category.CategoryEnum category = null;
    String expiration_date = null;

    public Food() {}
    public Food(FoodInterface food) {
        this.name = food.getName();
        this.stockType = food.getStockType();
        this.number = food.getNumber();
        this.category = food.getCategory();
        this.expiration_date = food.getExpirationDate();
    }

    @Override
    public String getExpirationDate() { return this.expiration_date; }

    @Override
    public void setExpirationDate(String expiration_date) { this.expiration_date = expiration_date; }

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
