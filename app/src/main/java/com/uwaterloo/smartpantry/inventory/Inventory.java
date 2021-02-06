package com.uwaterloo.smartpantry.inventory;

public interface Inventory {
    void addItemToInventory(Item item);
    boolean removeItemFromInventory(Item item);
    int InventorySize();
}
