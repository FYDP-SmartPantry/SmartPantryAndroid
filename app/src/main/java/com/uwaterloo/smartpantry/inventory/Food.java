package com.uwaterloo.smartpantry.inventory;

import java.util.Arrays;

public class Food implements Item {

    String name = null;
    Stock stock = null;
    Category category = null;
    String expiration_date = null;

    public Food() {
    }

    public String getName() {
        return this.name;
    }

    public Stock getStock() {
        return this.stock;
    }

    public Category getCategory() {
        return this.category;
    }

    public String getExpirationDate() {
        return this.expiration_date;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setExpirationDate(String expiration_date) {
        this.expiration_date = expiration_date;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStock(Stock[] stock) {
        if (stock.length == 1) {
            this.stock = stock[0];
        }
        int stock_cnt = 0;
        for (Stock cur : stock) {
            stock_cnt += cur.getNumber();
        }
        this.stock.setNumber(stock_cnt);
    }

}
