package com.uwaterloo.smartpantry.inventory;

import java.util.ArrayList;
import java.util.List;

public class WastedFood extends Food implements Item{

    private List<String> reasonlist = new ArrayList<>();

    public WastedFood() {};

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

    public List<String> getReasons() {
        return reasonlist;
    }
    // TODO better use a builder pattern
}
