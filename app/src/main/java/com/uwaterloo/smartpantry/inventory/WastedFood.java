package com.uwaterloo.smartpantry.inventory;

import com.couchbase.lite.Array;

import java.util.ArrayList;
import java.util.List;

public class WastedFood extends Food implements Item{

    private ArrayList<String> reasonlist = new ArrayList<>();

    final static String nameString = "foodname";
    final static String stockTypeString = "stockType";
    final static String numberString = "count";
    final static String categoryString = "category";
    final static String expirationDateString = "expiration_date";
    final static String reason = "reason";

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
        reasonlist.add(reason);
    }

    public void removeReason(String reason) {
        reasonlist.remove(reason);
    }

    public com.couchbase.lite.Array getReasons() {
        com.couchbase.lite.Array arr = new com.couchbase.lite.Array();
    }
    // TODO better use a builder pattern
}
