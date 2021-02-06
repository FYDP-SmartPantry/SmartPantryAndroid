package com.uwaterloo.smartpantry.inventory;

import java.util.ArrayList;
import java.util.List;

public class WastedFood extends Food{

    private List<String> reasonlist = new ArrayList<>();

    public WastedFood(Food food, String reason) {
        this.name = food.name;
        this.category = food.category;
        this.stock = food.stock;
        this.expiration_date = food.expiration_date;
        reasonlist.add(reason);
    }

    public void addReason(String reason) {
        reasonlist.add(reason);
    }

    public void removeReason(String reason) {
        reasonlist.remove(reason);
    }

    public List<String> getReasons() {
        return reasonlist;
    }
    // TODO better use a builder pattern
}
