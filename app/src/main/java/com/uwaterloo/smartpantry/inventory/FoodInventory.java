package com.uwaterloo.smartpantry.inventory;

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

import java.util.HashMap;
import java.util.Map;

public class FoodInventory implements Inventory {

    private Map<String, Food> inventoryMap = new HashMap<>();

    public FoodInventory() {}

    @Override
    public void addItemToInventory(Item food) throws Exception {
        if (!inventoryMap.containsKey(food.getName())) {
            inventoryMap.put(food.getName(), new Food(food));
        } else {
            Food currItem = inventoryMap.get(food.getName());
            if (food.getStockType() == currItem.getStockType()) {
                // we already have a item in the shopping list that named this way but in different type stock
                // this is not allowed
                throw new Exception(String.format("Already have %s in stock, type as %s", currItem.getName(), currItem.getStockType()));
            } else {
                Food itemToStore = new Food(food);
                itemToStore.setNumber(food.getNumber() + currItem.getNumber());
                inventoryMap.put(food.getName(), itemToStore);
            }
        }
    }

    @Override
    public void removeItemFromInventory(Item item) {
        if (inventoryMap.containsKey(item.getName())) {
            inventoryMap.remove(item.getName());
        }
    }

    @Override
    public int InventorySize() { return inventoryMap.size(); }

    @Override
    public void clearInventory() { inventoryMap.clear(); }

    @Override
    public Item getItem(String item_name) { return inventoryMap.get(item_name); }

    @Override
    public void updateItem(String item_name, Item item) {
        if (inventoryMap.containsKey(item_name)) {
            inventoryMap.remove(item_name);
        }
        inventoryMap.put(item_name, (Food) item);
    }

    @Override
    public boolean loadInventory() {
        try {
            Database database = DatabaseManager.getDatabase(DatabaseManager.inventoryDbStr);
            Query query = QueryBuilder.select(
                    SelectResult.expression(Meta.id),
                    SelectResult.property(Food.nameString),
                    SelectResult.property(Food.categoryString),
                    SelectResult.property(Food.stockTypeString),
                    SelectResult.property(Food.numberString),
                    SelectResult.property(Food.expirationDateString)).from(DataSource.database(database)).orderBy(Ordering.expression(Meta.id));
            try {
                ResultSet rs = query.execute();
                for (Result result : rs) {
                    Food food = new Food();
                    food.setName(result.getString(Food.nameString));
                    food.setCategory(Category.StringToCategory(result.getString(Food.categoryString)));
                    food.setStockType(result.getString(Food.stockTypeString));
                    food.setNumber(result.getInt(Food.numberString));
                    food.setExpirationDate(result.getString(Food.expirationDateString));
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

    @Override
    public boolean saveInventory() {
        try {
            Database database = DatabaseManager.getDatabase(DatabaseManager.inventoryDbStr);
            for (Map.Entry<String, Food> food : inventoryMap.entrySet()) {
                MutableDocument mutableDocument = new MutableDocument();
                mutableDocument.setString(Food.nameString, food.getValue().getName());
                mutableDocument.setString(Food.stockTypeString, food.getValue().getStockType());
                mutableDocument.setInt(Food.numberString, food.getValue().getNumber());
                mutableDocument.setString(Food.categoryString, Category.CategoryToString(food.getValue().getCategory()));
                mutableDocument.setString(Food.expirationDateString, food.getValue().getExpirationDate());
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

    @Override
    public boolean syncInventory() {
        return false;
    }

    @Override
    public boolean deleteInventory() throws Exception {
        DatabaseManager dbmgr = DatabaseManager.getSharedInstance();
        dbmgr.deleteDatabaseForUser(DatabaseManager.inventoryDbStr);
        return true;
    }
}
