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

/*
* For shopping list, we store the previous data
* */
public class ShoppingList implements Inventory {

    public Map<String, Item> shoppingListMap = new HashMap<>();

    /*
    * Since we are using the item name as different String for mapping. that requires name to be different
    * Under such case, no two duplicate name is allowed.
    * */
    @Override
    public void addItemToInventory(Item item) throws Exception {
        if (!shoppingListMap.containsKey(item.getName())) {
            shoppingListMap.put(item.getName(), item);
        } else {
            Item currItem = shoppingListMap.get(item.getName());
            if (item.getStockType() == currItem.getStockType()) {
                // we already have a item in the shopping list that named this way but in different type stock
                // this is not allowed
                throw new Exception(String.format("Already have %s in stock, type as %s", currItem.getName(), currItem.getStockType()));
            } else {
                Item itemToStore = new GroceryItem(item);
                itemToStore.setNumber(item.getNumber() + currItem.getNumber());
                shoppingListMap.put(item.getName(), itemToStore);
            }
        }
    }

    @Override
    public void removeItemFromInventory(Item item) {
        if (shoppingListMap.containsKey(item.getName())) {
            shoppingListMap.remove(item.getName());
        }
    }

    @Override
    public int InventorySize() { return shoppingListMap.size(); }

    @Override
    public void clearInventory() { shoppingListMap.clear(); }

    @Override
    public Item getItem(String item_name) { return shoppingListMap.get(item_name); }

    @Override
    public void updateItem(String item_name, Item item) {
        if (shoppingListMap.containsKey(item_name)) {
            shoppingListMap.remove(item_name);
        }
        shoppingListMap.put(item_name, item);
    }

    /*
    * Load inventory regarding the database operations
    * */
    @Override
    public boolean loadInventory() {
        try {
            Database database = DatabaseManager.getDatabase(DatabaseManager.shoppingListDbStr);
            Query query = QueryBuilder.select(
                    SelectResult.expression(Meta.id),
                    SelectResult.property(GroceryItem.nameString),
                    SelectResult.property(GroceryItem.categoryString),
                    SelectResult.property(GroceryItem.stockTypeString),
                    SelectResult.property(GroceryItem.numberString)).from(DataSource.database(database)).orderBy(Ordering.expression(Meta.id));
            try {
                ResultSet rs = query.execute();
                for (Result result : rs) {
                    GroceryItem item = new GroceryItem();
                    item.setName(result.getString(GroceryItem.nameString));
                    item.setCategory(Category.StringToCategory(result.getString(GroceryItem.categoryString)));
                    item.setStockType(result.getString(GroceryItem.stockTypeString));
                    item.setNumber(result.getInt(GroceryItem.numberString));
                    shoppingListMap.put(item.getName(), item);
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
            Database database = DatabaseManager.getDatabase(DatabaseManager.shoppingListDbStr);
            for (Map.Entry<String, Item> item : shoppingListMap.entrySet()) {
                MutableDocument mutableDocument = new MutableDocument();
                mutableDocument.setString(GroceryItem.nameString, item.getValue().getName());
                mutableDocument.setString(GroceryItem.stockTypeString, item.getValue().getStockType());
                mutableDocument.setInt(GroceryItem.numberString, item.getValue().getNumber());
                mutableDocument.setString(GroceryItem.categoryString, Category.CategoryToString(item.getValue().getCategory()));
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
        return true;
    }

    @Override
    public boolean deleteInventory() {
        DatabaseManager dbmgr = DatabaseManager.getSharedInstance();
        dbmgr.deleteDatabaseForUser(DatabaseManager.shoppingListDbStr);
        return true;
    }
}
