package com.uwaterloo.smartpantry.inventory;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
* For shopping list, we store the previous data
* */
public class ShoppingList {

    public List<GroceryItem> shoppingList = new ArrayList<>();
    private static ShoppingList instance;

    private ShoppingList(){}

    public static synchronized ShoppingList getInstance() {
        if (instance == null) {
            instance = new ShoppingList();
        }
        return instance;
    }

    /*
    * Since we are using the item name as different String for mapping. that requires name to be different
    * Under such case, no two duplicate name is allowed.
    * */
    public void addItemToInventory(GroceryItem item) {
        shoppingList.add(item);
    }

    public void removeItemFromInventory(GroceryItem item) {
        shoppingList.remove(item);
    }

    public int InventorySize() {
        return shoppingList.size();
    }

    public void clearInventory() {
        shoppingList.clear();
    }

    public List<GroceryItem> getShoppingList() {
        return shoppingList;
    }

    public void loadTestData() {
        shoppingList.add(new GroceryItem("banana", 2));
        shoppingList.add(new GroceryItem("apple", 3));
        shoppingList.add(new GroceryItem("orange", 4));
        shoppingList.add(new GroceryItem("strawberry", 5));
        shoppingList.add(new GroceryItem("mango", 20));
    }
    /*
    * Load inventory regarding the database operations
    * */
//    public boolean loadInventory() {
//        try {
//            Database database = DatabaseManager.getDatabase(DatabaseManager.shoppingListDbStr);
//            Query query = QueryBuilder.select(
//                    SelectResult.expression(Meta.id),
//                    SelectResult.property(GroceryItem.nameString),
//                    SelectResult.property(GroceryItem.categoryString),
//                    SelectResult.property(GroceryItem.stockTypeString),
//                    SelectResult.property(GroceryItem.numberString)).from(DataSource.database(database)).orderBy(Ordering.expression(Meta.id));
//            try {
//                ResultSet rs = query.execute();
//                for (Result result : rs) {
//                    GroceryItem item = new GroceryItem();
//                    item.setName(result.getString(GroceryItem.nameString));
//                    item.setCategory(Category.StringToCategory(result.getString(GroceryItem.categoryString)));
//                    item.setStockType(result.getString(GroceryItem.stockTypeString));
//                    item.setNumber(result.getInt(GroceryItem.numberString));
//                    shoppingList.put(item.getName(), item);
//                }
//            } catch (CouchbaseLiteException e) {
//                e.printStackTrace();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return false;
//    }

//    public boolean saveInventory() {
//        try {
//            Database database = DatabaseManager.getDatabase(DatabaseManager.shoppingListDbStr);
//            for (Map.Entry<String, Item> item : shoppingList.entrySet()) {
//                MutableDocument mutableDocument = new MutableDocument();
//                mutableDocument.setString(GroceryItem.nameString, item.getValue().getName());
//                mutableDocument.setString(GroceryItem.stockTypeString, item.getValue().getStockType());
//                mutableDocument.setInt(GroceryItem.numberString, item.getValue().getNumber());
//                mutableDocument.setString(GroceryItem.categoryString, Category.CategoryToString(item.getValue().getCategory()));
//                try {
//                    database.save(mutableDocument);
//                } catch (CouchbaseLiteException e) {
//                    e.printStackTrace();
//                }
//            }
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return false;
//    }

    public boolean syncInventory() {
        return true;
    }

    public boolean deleteInventory() {
        DatabaseManager dbmgr = DatabaseManager.getSharedInstance();
        dbmgr.deleteDatabaseForUser(DatabaseManager.shoppingListDbStr);
        return true;
    }

//    public UserInfo getUserInfo() {
//        try {
//            Database database = DatabaseManager.getDatabase(DatabaseManager.userInfoDbStr);
//            Query query = QueryBuilder.select(
//                    SelectResult.expression(Meta.id),
//                    SelectResult.property(UserInfo.usernameString),
//                    SelectResult.property(UserInfo.hashString)).from(DataSource.database(database)).orderBy(Ordering.expression(Meta.id));
//            try {
//                ResultSet rs = query.execute();
//                // Should only return 1 relevant result
//                Result result = rs.next();
//                UserInfo userInfo = new UserInfo();
//                userInfo.setUsername(result.getString(UserInfo.usernameString));
//                userInfo.setHash(result.getString(UserInfo.hashString));
//
//                return userInfo;
//            } catch (CouchbaseLiteException e) {
//                e.printStackTrace();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return new UserInfo();
//    }

//    public boolean hasMealPlanAccount() {
//        UserInfo userInfo = getUserInfo();
//        if (StringUtils.isEmpty(userInfo.getUsername()) || StringUtils.isEmpty(userInfo.getHash())) {
//            return false;
//        } else {
//            return true;
//        }
//    }

//    public boolean uploadInventory() {
//        JSONArray jsonArrayForUpload = new JSONArray();
//        try {
//            Database database = DatabaseManager.getDatabase(DatabaseManager.shoppingListDbStr);
//            Query query = QueryBuilder.select(
//                    SelectResult.expression(Meta.id),
//                    SelectResult.property(GroceryItem.nameString),
//                    SelectResult.property(GroceryItem.categoryString),
//                    SelectResult.property(GroceryItem.stockTypeString),
//                    SelectResult.property(GroceryItem.numberString)).from(DataSource.database(database)).orderBy(Ordering.expression(Meta.id));
//            try {
//                ResultSet rs = query.execute();
//                for (Result result : rs) {
//                    JSONObject jsonObject = catIntoJSONObject(result);
//                    jsonArrayForUpload.put(jsonObject);
//                }
//            } catch (CouchbaseLiteException e) {
//                e.printStackTrace();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        //TODO context management. we are assume that datalink is already init
//        if (DataLink.getInstance().isDataLinkInitialized() == false) {
//            return false;
//        } else {
//            try {
//                String url ="http://3.18.111.90:5000/";
//                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, url, jsonArrayForUpload, (Response.Listener<JSONArray>) response ->
//                    System.out.println(response), (Response.ErrorListener) error -> System.out.println(error.getMessage()));
//                DataLink.getInstance().addToRequestQueue(jsonArrayRequest);
//                return true;
//            }catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return false;
//    }

    public boolean downloadInventory(User user) {
        String url ="http://3.18.111.90:5000/";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, url, null, (Response.Listener<JSONArray>) response ->
                System.out.println(response), (Response.ErrorListener) error -> System.out.println(error.getMessage()));
        DataLink.getInstance().addToRequestQueue(jsonArrayRequest);
        return false;
    }

//    private JSONObject catIntoJSONObject(Result result) throws Exception {
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put(GroceryItem.nameString, result.getString(GroceryItem.nameString));
//        jsonObject.put(GroceryItem.categoryString, Category.StringToCategory(result.getString(GroceryItem.categoryString)));
//        jsonObject.put(GroceryItem.stockTypeString, result.getString(GroceryItem.stockTypeString));
//        jsonObject.put(GroceryItem.numberString, new Integer(result.getInt(GroceryItem.numberString)));
//        return jsonObject;
//    }
}
