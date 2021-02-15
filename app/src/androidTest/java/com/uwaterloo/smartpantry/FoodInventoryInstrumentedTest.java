package com.uwaterloo.smartpantry;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import com.uwaterloo.smartpantry.database.DatabaseManager;
import com.uwaterloo.smartpantry.inventory.Category;
import com.uwaterloo.smartpantry.inventory.Food;
import com.uwaterloo.smartpantry.inventory.FoodInventory;
import com.uwaterloo.smartpantry.inventory.GroceryItem;
import com.uwaterloo.smartpantry.inventory.Inventory;
import com.uwaterloo.smartpantry.inventory.Item;
import com.uwaterloo.smartpantry.inventory.ShoppingList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static com.uwaterloo.smartpantry.database.DatabaseManager.getSharedInstance;
import static org.junit.Assert.assertEquals;

public class FoodInventoryInstrumentedTest {
    private FoodInventory foodInventory = new FoodInventory();
    private DatabaseManager dbManager = getSharedInstance();
    private Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

    @Before
    public void setUpShoppingList() throws Exception {

        addAppleItem();
        addOrangeItem();
        // db init
        dbManager.initCouchbaseLite(appContext);
        dbManager.openOrCreateDatabaseForUser(appContext, "test");
    }

    @Test
    public void saveDatabaseManager() throws Exception {


        foodInventory.saveInventory();
        foodInventory.clearInventory();
        foodInventory.loadInventory();

        Food item = foodInventory.getFood("apple");
        assertEquals(Category.CategoryEnum.FRUIT, item.getCategory());
        assertEquals(5, item.getNumber());
        assertEquals("lbs", item.getStockType());
        assertEquals("2021/1/3", item.getExpirationDate());

        item = foodInventory.getFood("orange");
        assertEquals(Category.CategoryEnum.FRUIT, item.getCategory());
        assertEquals(6, item.getNumber());
        assertEquals("cnt", item.getStockType());
        assertEquals("2021/1/3", item.getExpirationDate());

    }

    private void addAppleItem() {
        Food toShop1 = new Food();
        toShop1.setName("apple");
        toShop1.setCategory(Category.CategoryEnum.FRUIT);
        toShop1.setStockType("lbs");
        toShop1.setNumber(5);
        toShop1.setExpirationDate("2021/1/3");
        try {
            foodInventory.addItemToInventory(toShop1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addOrangeItem() {
        Food toShop2 = new Food();
        toShop2.setName("orange");
        toShop2.setCategory(Category.CategoryEnum.FRUIT);
        toShop2.setStockType("cnt");
        toShop2.setNumber(6);
        toShop2.setExpirationDate("2021/1/3");
        try {
            foodInventory.addItemToInventory(toShop2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @After
    public void clearDatabase() {
        try {
            foodInventory.deleteInventory();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
