package com.uwaterloo.smartpantry.inventory;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.request.JsonArrayRequest;
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
import com.couchbase.lite.internal.utils.StringUtils;
import com.uwaterloo.smartpantry.data.UserInfo;
import com.uwaterloo.smartpantry.database.DatabaseManager;
import com.uwaterloo.smartpantry.datalink.DataLink;
import com.uwaterloo.smartpantry.user.User;

import org.json.JSONArray;
import org.json.JSONObject;

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
                    item.setNumber(result.getDouble(GroceryItem.numberString));
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
                mutableDocument.setNumber(GroceryItem.numberString, item.getValue().getNumber());
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

    public UserInfo getUserInfo() {
        try {
            Database database = DatabaseManager.getDatabase(DatabaseManager.userInfoDbStr);
            Query query = QueryBuilder.select(
                    SelectResult.expression(Meta.id),
                    SelectResult.property(UserInfo.usernameString),
                    SelectResult.property(UserInfo.hashString)).from(DataSource.database(database)).orderBy(Ordering.expression(Meta.id));
            try {
                ResultSet rs = query.execute();
                // Should only return 1 relevant result
                Result result = rs.next();
                UserInfo userInfo = new UserInfo();
                userInfo.setUsername(result.getString(UserInfo.usernameString));
                userInfo.setHash(result.getString(UserInfo.hashString));

                return userInfo;
            } catch (CouchbaseLiteException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new UserInfo();
    }

    public boolean hasMealPlanAccount() {
        UserInfo userInfo = getUserInfo();
        if (StringUtils.isEmpty(userInfo.getUsername()) || StringUtils.isEmpty(userInfo.getHash())) {
            return false;
        } else {
            return true;
        }
    }

    public boolean uploadInventory() {
        JSONArray jsonArrayForUpload = new JSONArray();
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
                    JSONObject jsonObject = catIntoJSONObject(result);
                    jsonArrayForUpload.put(jsonObject);
                }
            } catch (CouchbaseLiteException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //TODO context management. we are assume that datalink is already init
        if (DataLink.getInstance().isDataLinkInitialized() == false) {
            return false;
        } else {
            try {
                String url ="http://3.18.111.90:5000/";
                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, url, jsonArrayForUpload, (Response.Listener<JSONArray>) response ->
                    System.out.println(response), (Response.ErrorListener) error -> System.out.println(error.getMessage()));
                DataLink.getInstance().addToRequestQueue(jsonArrayRequest);
                return true;
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean downloadInventory(User user) {
        String url ="http://3.18.111.90:5000/";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, url, null, (Response.Listener<JSONArray>) response ->
                System.out.println(response), (Response.ErrorListener) error -> System.out.println(error.getMessage()));
        DataLink.getInstance().addToRequestQueue(jsonArrayRequest);
        return false;
    }

    private JSONObject catIntoJSONObject(Result result) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(GroceryItem.nameString, result.getString(GroceryItem.nameString));
        jsonObject.put(GroceryItem.categoryString, Category.StringToCategory(result.getString(GroceryItem.categoryString)));
        jsonObject.put(GroceryItem.stockTypeString, result.getString(GroceryItem.stockTypeString));
        jsonObject.put(GroceryItem.numberString, new Integer(result.getInt(GroceryItem.numberString)));
        return jsonObject;
    }
}
