package com.uwaterloo.smartpantry.inventory;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class ShoppingList implements Inventory {

    public Map<String, Item> shoppingListMap = new HashMap<>();

    @Override
    public void addItemToInventory(Item item) {
        if (shoppingListMap.containsKey(item.getName())) {
            Item itemToStore = new Food();
            Item currItem = shoppingListMap.get(item.getName());

            //itemToStore.setStock(new Stock[] {currItem.getStock(), item.getStock()});
            shoppingListMap.put(item.getName(), itemToStore);
        } else {
            shoppingListMap.put(item.getName(), item);
        }
    }

    @Override
    public boolean removeItemFromInventory(Item item) {
        if (shoppingListMap.containsKey(item.getName())) {
            shoppingListMap.remove(item.getName());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int InventorySize() {
        return shoppingListMap.size();
    }

    @Override
    public void clearInventory() {
        shoppingListMap.clear();
    }

    @Override
    public Item getItem(String item_name) {
        return shoppingListMap.get(item_name);
    }

    @Override
    public void updateItem(String item_name, Item item) {
        if (shoppingListMap.containsKey(item_name)) {
            shoppingListMap.remove(item_name);
        }
        shoppingListMap.put(item_name, item);
    }
}
