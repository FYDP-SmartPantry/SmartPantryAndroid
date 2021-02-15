package com.uwaterloo.smartpantry.inventory;

public interface Inventory {

    void addItemToInventory(Item item) throws Exception;
    void removeItemFromInventory(Item item);

    int InventorySize();
    void clearInventory();

    Item getItem(String item_name);
    void updateItem(String item_name, Item item);

    boolean loadInventory();
    boolean saveInventory();

    boolean syncInventory();
    boolean deleteInventory() throws Exception;
}
