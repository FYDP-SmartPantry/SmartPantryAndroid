package com.uwaterloo.smartpantry.inventory;

import com.couchbase.lite.Database;
import com.couchbase.lite.MutableDocument;
import com.uwaterloo.smartpantry.database.DatabaseManager;

import java.util.HashMap;
import java.util.Map;

public class FoodInventory implements Inventory {
    private Map<String, Food> inventoryMap;

    FoodInventory() {

        this.inventoryMap = new HashMap<>();
    }

    public void addFoodMap(Food food) {

    }

    public void removeFromFoodMap(Food food) {
        inventoryMap.remove(food.getName());
    }

    public Map<String, Food> returnInventoryMap() {
        return inventoryMap;
    }


    @Override
    public void addItemToInventory(Item item) {

    }

    @Override
    public void removeItemFromInventory(Item item) {

    }

    @Override
    public int InventorySize() {
        return 0;
    }

    @Override
    public void clearInventory() {

    }

    @Override
    public Item getItem(String item_name) {
        return null;
    }

    @Override
    public void updateItem(String item_name, Item item) {

    }

    @Override
    public boolean loadInventory() {
        return false;
    }

    @Override
    public boolean saveInventory() {
        try {
            Database database = DatabaseManager.getDatabase("inventory");
            for (Map.Entry<String, Food> food : inventoryMap.entrySet()) {
                MutableDocument mutableDocument = new MutableDocument();
                //mutableDocument.setString("")
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean syncInventory() {
        return false;
    }

    @Override
    public boolean deleteInventory() throws Exception {
        return false;
    }
}
