package com.uwaterloo.smartpantry.inventory;

import java.util.HashMap;
import java.util.Map;

public class ShoppingList implements Inventory {

    private Map<String, Item> shoppingListMap = new HashMap<>();

    @Override
    public void addItemToInventory(Item item) {
        if (shoppingListMap.containsKey(item.getName())) {
            Item itemToStore = new Food();
            Item currItem = shoppingListMap.get(item.getName());

            itemToStore.setStock(new Stock[] {currItem.getStock(), item.getStock()});
            shoppingListMap.put(item.getName(), itemToStore);
        } else {
            shoppingListMap.put(item.getName(), item);
        }
    }

    @Override
    public boolean removeItemFromInventory(Item item) {
        if (shoppingListMap.containsKey(item.getName())) {
            return false;
        } else {
            shoppingListMap.remove(item.getName());
            return true;
        }
    }

    @Override
    public int InventorySize() {
        return shoppingListMap.size();
    }
}
