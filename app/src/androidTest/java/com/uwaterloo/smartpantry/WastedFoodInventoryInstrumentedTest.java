package com.uwaterloo.smartpantry;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import com.uwaterloo.smartpantry.database.DatabaseManager;
import com.uwaterloo.smartpantry.inventory.Category;
import com.uwaterloo.smartpantry.inventory.Food;
import com.uwaterloo.smartpantry.inventory.FoodInventory;
import com.uwaterloo.smartpantry.inventory.Inventory;
import com.uwaterloo.smartpantry.inventory.Item;
import com.uwaterloo.smartpantry.inventory.WastedFood;
import com.uwaterloo.smartpantry.inventory.WastedFoodInventory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static com.uwaterloo.smartpantry.database.DatabaseManager.getSharedInstance;
import static org.junit.Assert.assertEquals;

public class WastedFoodInventoryInstrumentedTest {
    private WastedFoodInventory WastedfoodInventory = new WastedFoodInventory();
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


        WastedfoodInventory.saveInventory();
        WastedfoodInventory.clearInventory();
        WastedfoodInventory.loadInventory();

        WastedFood item = WastedfoodInventory.getWastedFood("apple");
        assertEquals(Category.CategoryEnum.FRUIT, item.getCategory());
        assertEquals(5, item.getNumber());
        assertEquals("lbs", item.getStockType());
        assertEquals("expired", item.getReason());

        item = WastedfoodInventory.getWastedFood("orange");
        assertEquals(Category.CategoryEnum.FRUIT, item.getCategory());
        assertEquals(6, item.getNumber());
        assertEquals("cnt", item.getStockType());
        assertEquals("expired", item.getReason());
    }

    private void addAppleItem() {
        WastedFood toShop1 = new WastedFood();
        toShop1.setName("apple");
        toShop1.setCategory(Category.CategoryEnum.FRUIT);
        toShop1.setStockType("lbs");
        toShop1.setNumber(5);
        toShop1.setExpirationDate("2021/1/3");
        toShop1.addReason("expired");
        try {
            WastedfoodInventory.addItemToInventory(toShop1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addOrangeItem() {
        WastedFood toShop2 = new WastedFood();
        toShop2.setName("orange");
        toShop2.setCategory(Category.CategoryEnum.FRUIT);
        toShop2.setStockType("cnt");
        toShop2.setNumber(6);
        toShop2.setExpirationDate("2021/1/2");
        toShop2.addReason("expired");
        try {
            WastedfoodInventory.addItemToInventory(toShop2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @After
    public void clearDatabase() {
        try {
            WastedfoodInventory.deleteInventory();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
