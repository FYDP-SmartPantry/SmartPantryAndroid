package com.uwaterloo.smartpantry.inventory;
import com.couchbase.lite.Array;
import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.DataSource;
import com.couchbase.lite.Database;
import com.couchbase.lite.Meta;
import com.couchbase.lite.MutableDocument;
import com.couchbase.lite.Ordering;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryBuilder;
import com.couchbase.lite.Result;
import com.couchbase.lite.ResultSet;
import com.couchbase.lite.SelectResult;
import com.uwaterloo.smartpantry.database.DatabaseManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class WastedFoodInventory {
    private Map<String, WastedFood> inventoryMap = new HashMap<>();

    public WastedFoodInventory() {}

    //@Override
    public void addItemToInventory(WastedFood food) throws Exception {
        if (!inventoryMap.containsKey(food.getName())) {
            inventoryMap.put(food.getName(), food);
        } else {
            WastedFood currItem = inventoryMap.get(food.getName());
            if (food.getStockType() == currItem.getStockType()) {
                // we already have a item in the shopping list that named this way but in different type stock
                // this is not allowed
                throw new Exception(String.format("Already have %s in stock, type as %s", currItem.getName(), currItem.getStockType()));
            } else {
                WastedFood itemToStore = new WastedFood(food);
                itemToStore.setNumber(food.getNumber() + currItem.getNumber());
                inventoryMap.put(food.getName(), itemToStore);
            }
        }
    }

    //@Override
    public void removeItemFromInventory(WastedFood item) {
        if (inventoryMap.containsKey(item.getName())) {
            inventoryMap.remove(item.getName());
        }
    }

    //@Override
    public int InventorySize() { return inventoryMap.size(); }

    //@Override
    public void clearInventory() { inventoryMap.clear(); }

    //@Override
    public WastedFood getWastedFood(String item_name) { return inventoryMap.get(item_name); }

    //@Override
    public void updateItem(String item_name, WastedFood item) {
        if (inventoryMap.containsKey(item_name)) {
            inventoryMap.remove(item_name);
        }
        inventoryMap.put(item_name, item);
    }

    //@Override
    public boolean loadInventory() {
        try {
            Database database = DatabaseManager.getDatabase(DatabaseManager.wastedFoodDbStr);
            Query query = QueryBuilder.select(
                    SelectResult.expression(Meta.id),
                    SelectResult.property(WastedFood.nameString),
                    SelectResult.property(WastedFood.categoryString),
                    SelectResult.property(WastedFood.stockTypeString),
                    SelectResult.property(WastedFood.numberString),
                    SelectResult.property(WastedFood.reasonString)).from(DataSource.database(database)).orderBy(Ordering.expression(Meta.id));
            try {
                ResultSet rs = query.execute();
                for (Result result : rs) {
                    WastedFood food = new WastedFood();
                    food.setName(result.getString(WastedFood.nameString));
                    food.setCategory(Category.StringToCategory(result.getString(WastedFood.categoryString)));
                    food.setStockType(result.getString(WastedFood.stockTypeString));
                    food.setNumber(result.getInt(WastedFood.numberString));
                    food.addReason(result.getString(WastedFood.reasonString));
                    inventoryMap.put(food.getName(), food);
                }
            } catch (CouchbaseLiteException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //@Override
    public boolean saveInventory() {
        try {
            Database database = DatabaseManager.getDatabase(DatabaseManager.wastedFoodDbStr);
            for (Map.Entry<String, WastedFood> food : inventoryMap.entrySet()) {
                MutableDocument mutableDocument = new MutableDocument();
                mutableDocument.setString(Food.nameString, food.getValue().getName());
                mutableDocument.setString(Food.stockTypeString, food.getValue().getStockType());
                mutableDocument.setInt(Food.numberString, food.getValue().getNumber());
                mutableDocument.setString(Food.categoryString, Category.CategoryToString(food.getValue().getCategory()));
                mutableDocument.setString(WastedFood.reasonString, food.getValue().getReason());
                // FIXME reason should be a list instead of just a string.
                //mutableDocument.setArray("some", new ArrayList<String>());
                try {
                    database.save(mutableDocument);
                } catch (CouchbaseLiteException e) {
                    e.printStackTrace();
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //@Override
    public boolean syncInventory() {
        return false;
    }

    //@Override
    public boolean deleteInventory() throws Exception {
        DatabaseManager dbmgr = DatabaseManager.getSharedInstance();
        dbmgr.deleteDatabaseForUser(DatabaseManager.inventoryDbStr);
        return true;
    }
}

