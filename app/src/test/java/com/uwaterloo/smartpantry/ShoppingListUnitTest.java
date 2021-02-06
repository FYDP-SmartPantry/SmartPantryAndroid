package com.uwaterloo.smartpantry;
import com.uwaterloo.smartpantry.inventory.Inventory;
import com.uwaterloo.smartpantry.inventory.InventoryFactory;
import com.uwaterloo.smartpantry.inventory.Item;
import com.uwaterloo.smartpantry.inventory.ItemFactory;
import com.uwaterloo.smartpantry.inventory.Stock;

import org.junit.Test;

import static com.uwaterloo.smartpantry.inventory.InventoryFactory.getInventory;
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ShoppingListUnitTest {
    @Test
    public void testAddingItem() {
        Inventory shoppingList = InventoryFactory.getInventory("ShoppingList");
        Item toShop1 = ItemFactory.getItem("FOOD");
        toShop1.setName("apple");
        toShop1.setStock(new Stock[] {new Stock("Fruit", 2)});
        shoppingList.addItemToInventory(toShop1);
        assertEquals(shoppingList.InventorySize(), 1);

        shoppingList.removeItemFromInventory(toShop1);
        assertEquals(shoppingList.InventorySize(), 0);
    }
}
