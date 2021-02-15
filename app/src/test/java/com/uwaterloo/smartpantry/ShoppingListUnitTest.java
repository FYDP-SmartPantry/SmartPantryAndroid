package com.uwaterloo.smartpantry;

import com.uwaterloo.smartpantry.inventory.Category;
import com.uwaterloo.smartpantry.inventory.GroceryItem;
import com.uwaterloo.smartpantry.inventory.Inventory;
import com.uwaterloo.smartpantry.inventory.Item;
import com.uwaterloo.smartpantry.inventory.ShoppingList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class ShoppingListUnitTest {

    /*
    * Use Cases for Shopping List
    * 1. to add item to the shopping list
    * 2. to remove item in the shopping list
    * 3. clear the shopping list
    * 4. edit the item inside of the shopping list. about its properties values
    * */
    private Inventory shoppingList = new ShoppingList();

    @Before
    public void init() {

    }

    @After
    public void tearDown() { shoppingList = null; }

    @Test
    public void testAddingItem() {
        Item toShop1 = new GroceryItem();
        toShop1.setName("apple");
        toShop1.setCategory(Category.CategoryEnum.FRUIT);
        toShop1.setStockType("lbs");
        toShop1.setNumber(5);
        try {
            shoppingList.addItemToInventory(toShop1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(1, shoppingList.InventorySize());

        Item toShop2 = new GroceryItem();
        toShop2.setName("orange");
        try {
            shoppingList.addItemToInventory(toShop2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(2, shoppingList.InventorySize());

    }

    @Test
    public void testRemoveItem() {
        Item toShop1 = new GroceryItem();
        toShop1.setName("apple");
        toShop1.setCategory(Category.CategoryEnum.FRUIT);
        toShop1.setStockType("lbs");
        toShop1.setNumber(5);
        try {
            shoppingList.addItemToInventory(toShop1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(1, shoppingList.InventorySize());

        shoppingList.removeItemFromInventory(toShop1);
        assertEquals(0, shoppingList.InventorySize());
    }

    @Test
    public void testClearShoppingList() {
        Item toShop1 = new GroceryItem();
        toShop1.setName("apple");
        toShop1.setCategory(Category.CategoryEnum.FRUIT);
        toShop1.setStockType("lbs");
        toShop1.setNumber(5);

        try {
            shoppingList.addItemToInventory(toShop1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(1, shoppingList.InventorySize());

        shoppingList.clearInventory();
        assertEquals(0, shoppingList.InventorySize());
    }

    @Test
    public void testEditItemInShoppingList() {
        Item toShop1 = new GroceryItem();
        toShop1.setName("apple");
        toShop1.setCategory(Category.CategoryEnum.FRUIT);
        toShop1.setStockType("lbs");
        toShop1.setNumber(5);
        toShop1.setCategory(Category.CategoryEnum.FRUIT);

        try {
            shoppingList.addItemToInventory(toShop1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals("apple", toShop1.getName());
        assertEquals(Category.CategoryEnum.FRUIT, toShop1.getCategory());
        //assertTrue(toShop1.getStock().equals(new Stock("Individual", 2)));

        Item toModify = shoppingList.getItem(toShop1.getName());
        toModify.setName("Tomato");
        toModify.setCategory(Category.CategoryEnum.VEGETABLE);
        toModify.setStockType("lbs");
        toModify.setNumber(5);
        shoppingList.updateItem(toShop1.getName(), toModify);

        Item temp = shoppingList.getItem(toModify.getName());
        assertEquals("Tomato", temp.getName());
        assertEquals(Category.CategoryEnum.VEGETABLE, temp.getCategory());
        //assertTrue(temp.getStockType().equals(new Stock("Individual", 3)));
    }

}
