package com.uwaterloo.smartpantry;
import com.uwaterloo.smartpantry.inventory.Category;
import com.uwaterloo.smartpantry.inventory.Inventory;
import com.uwaterloo.smartpantry.inventory.InventoryFactory;
import com.uwaterloo.smartpantry.inventory.Item;
import com.uwaterloo.smartpantry.inventory.ItemFactory;
import com.uwaterloo.smartpantry.inventory.ShoppingList;
import com.uwaterloo.smartpantry.inventory.Stock;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;

import static com.uwaterloo.smartpantry.inventory.Category.FRUIT;
import static com.uwaterloo.smartpantry.inventory.Category.VEGETABLE;
import static com.uwaterloo.smartpantry.inventory.InventoryFactory.getInventory;


public class ShoppingListUnitTest {

    /*
    * Use Cases for Shopping List
    * 1. to add item to the shopping list
    * 2. to remove item in the shopping list
    * 3. clear the shopping list
    * 4. edit the item inside of the shopping list. about its properties values
    * */
    private Inventory shoppingList = null;

    @Before
    public void init() {
        shoppingList = InventoryFactory.getInventory("ShoppingList");
    }

    @After
    public void tearDown() { shoppingList = null; }

    @Test
    public void testAddingItem() {
        Item toShop1 = ItemFactory.getItem("FOOD");
        toShop1.setName("apple");
        toShop1.setStock(new Stock("Fruit", 2));
        shoppingList.addItemToInventory(toShop1);
        assertEquals(1, shoppingList.InventorySize());

        Item toShop2 = ItemFactory.getItem("FOOD");
        toShop2.setName("orange");
        shoppingList.addItemToInventory(toShop2);
        assertEquals(2, shoppingList.InventorySize());

    }

    @Test
    public void testRemoveItem() {
        Item toShop1 = ItemFactory.getItem("FOOD");
        toShop1.setName("apple");
        toShop1.setStock(new Stock("Fruit", 2));
        shoppingList.addItemToInventory(toShop1);
        assertEquals(1, shoppingList.InventorySize());

        shoppingList.removeItemFromInventory(toShop1);
        assertEquals(0, shoppingList.InventorySize());
    }

    @Test
    public void testClearShoppingList() {
        Item toShop1 = ItemFactory.getItem("FOOD");
        toShop1.setName("apple");
        toShop1.setStock(new Stock("Fruit", 2));

        shoppingList.addItemToInventory(toShop1);
        assertEquals(1, shoppingList.InventorySize());

        shoppingList.clearInventory();
        assertEquals(0, shoppingList.InventorySize());
    }

    @Test
    public void testEditItemInShoppingList() {
        Item toShop1 = ItemFactory.getItem("FOOD");
        toShop1.setName("apple");
        toShop1.setStock(new Stock("Individual", 2));
        toShop1.setCategory(FRUIT);

        shoppingList.addItemToInventory(toShop1);
        assertEquals("apple", toShop1.getName());
        assertEquals(toShop1.getCategory(), FRUIT);
        assertTrue(toShop1.getStock().equals(new Stock("Individual", 2)));

        Item toModify = shoppingList.getItem(toShop1.getName());
        toModify.setName("Tomato");
        toModify.setCategory(VEGETABLE);
        toModify.setStock(new Stock ("Individual", 3));
        shoppingList.updateItem(toShop1.getName(), toModify);

        Item temp = shoppingList.getItem(toModify.getName());
        assertEquals("Tomato", temp.getName());
        assertEquals(VEGETABLE, temp.getCategory());
        assertTrue(temp.getStock().equals(new Stock("Individual", 3)));

    }

}
