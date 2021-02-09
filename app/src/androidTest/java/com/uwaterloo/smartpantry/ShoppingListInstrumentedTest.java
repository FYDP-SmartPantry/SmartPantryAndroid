package com.uwaterloo.smartpantry;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.runner.RunWith;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import com.couchbase.lite.Database;
import com.uwaterloo.smartpantry.database.DatabaseManager;
import com.uwaterloo.smartpantry.inventory.Category;
import com.uwaterloo.smartpantry.inventory.GroceryItem;
import com.uwaterloo.smartpantry.inventory.Inventory;
import com.uwaterloo.smartpantry.inventory.Item;
import com.uwaterloo.smartpantry.inventory.ShoppingList;

import org.junit.Test;

import static com.uwaterloo.smartpantry.database.DatabaseManager.getDatabase;
import static com.uwaterloo.smartpantry.database.DatabaseManager.getSharedInstance;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class ShoppingListInstrumentedTest {

    private Inventory shoppingList = new ShoppingList();
    private DatabaseManager dbManager = getSharedInstance();
    private Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

    @Before
    public void setUpShoppingList() throws Exception {
        Item toShop1 = new GroceryItem();
        toShop1.setName("apple");
        toShop1.setCategory(Category.CategoryEnum.FRUIT);
        toShop1.setStockType("lbs");
        toShop1.setNumber(5);
        shoppingList.addItemToInventory(toShop1);

        Item toShop2 = new GroceryItem();
        toShop2.setName("orange");
        toShop2.setCategory(Category.CategoryEnum.FRUIT);
        toShop2.setStockType("lbs");
        toShop2.setNumber(5);
        shoppingList.addItemToInventory(toShop2);

        // db init
        dbManager.initCouchbaseLite(appContext);
        dbManager.openOrCreateDatabaseForUser(appContext, "test");
    }

    @Test
    public void saveDatabaseManager() throws Exception {


        shoppingList.saveInventory();
        shoppingList.loadInventory();

        Item item = shoppingList.getItem("apple");
        assertEquals(Category.CategoryEnum.FRUIT, item.getCategory());
        assertEquals(5, item.getNumber());
        assertEquals("lbs", item.getStockType());

        shoppingList.deleteInventory();
    }
}
