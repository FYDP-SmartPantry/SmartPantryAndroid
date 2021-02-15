package com.uwaterloo.smartpantry.inventory;

import com.couchbase.lite.Array;

import java.util.ArrayList;
import java.util.List;

public class WastedFood extends Food implements Item{

    private String reason;

    final static String nameString = "foodname";
    final static String stockTypeString = "stockType";
    final static String numberString = "count";
    final static String categoryString = "category";
    final static String expirationDateString = "expiration_date";
    final static String reasonString = "reason";

    public WastedFood() {};
    public WastedFood(Item item) {
        this.name = item.getName();
        this.stockType = item.getStockType();
        this.number = item.getNumber();
        this.category = item.getCategory();
    }

    public WastedFood(FoodInterface food) {
        this.name = food.getName();
        this.stockType = food.getStockType();
        this.number = food.getNumber();
        this.category = food.getCategory();
        this.expiration_date = food.getExpirationDate();
    }

    public WastedFood(Food food) {
        this.name = food.name;
        this.category = food.category;
        this.expiration_date = food.expiration_date;
    }

    public void addReason(String reason) {
        this.reason = reason;
    }

    public void removeReason(String reason) {
        this.reason = null;
    }

    public String getReason() {
        return this.reason;
    }
    // TODO better use a builder pattern
}
