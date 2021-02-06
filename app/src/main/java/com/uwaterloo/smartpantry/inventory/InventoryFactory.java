package com.uwaterloo.smartpantry.inventory;

public class InventoryFactory {

    public static Inventory getInventory(String inventoryType) {
        if (inventoryType == null) {
            return null;
        } else if (inventoryType.equalsIgnoreCase("ShoppingList")) {
            return new ShoppingList();
        } else if (inventoryType.equalsIgnoreCase("FoodInventory")) {
            return new FoodInventory();
        }
        return null;
    }
}
