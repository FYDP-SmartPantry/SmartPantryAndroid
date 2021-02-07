package com.uwaterloo.smartpantry.inventory;

public interface Inventory {
    void addItemToInventory(Item item);
    boolean removeItemFromInventory(Item item);
    int InventorySize();
    void clearInventory();

    Item getItem(String item_name);
    void updateItem(String item_name, Item item);
}
