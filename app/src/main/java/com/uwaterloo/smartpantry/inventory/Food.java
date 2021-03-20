package com.uwaterloo.smartpantry.inventory;

public class Food implements FoodInterface {

    final static String nameString = "foodname";
    final static String stockTypeString = "stockType";
    final static String numberString = "count";
    final static String categoryString = "category";
    final static String expirationDateString = "expiration_date";

    String name = null;
    String stockType = null;
    Double number = 0.0;
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
    
    public Food(Item item) {
        this.name = item.getName();
        this.stockType = item.getStockType();
        this.number = item.getNumber();
        this.category = item.getCategory();
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
    public Double getNumber() { return this.number; }

    @Override
    public void setNumber(Double number) { this.number = number; }

    @Override
    public Category.CategoryEnum getCategory() { return this.category; }

    @Override
    public void setCategory(Category.CategoryEnum category) { this.category = category; }
}
