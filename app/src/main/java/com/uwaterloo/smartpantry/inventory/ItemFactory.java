package com.uwaterloo.smartpantry.inventory;

public class ItemFactory {
    public static Item getItem(String itemType) {
        if (itemType == null) {
            return null;
        } else if (itemType.equalsIgnoreCase("FOOD")) {
            return new Food();
        } else if (itemType.equalsIgnoreCase("WASTEDFOOD")) {
            return new WastedFood();
        }
        return null;
    }
}
