package com.uwaterloo.smartpantry.inventory;


import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.request.JsonArrayRequest;
import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.DataSource;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
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
    public List<GroceryItem> recommendList = new ArrayList<>();

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
        shoppingList.add(new GroceryItem("banana", 2.0, "lbs"));
        shoppingList.add(new GroceryItem("apple", 3.0, "lbs"));
        shoppingList.add(new GroceryItem("orange", 4.0, "lbs"));
        shoppingList.add(new GroceryItem("strawberry", 5.0, "lbs"));
        shoppingList.add(new GroceryItem("mango", 20.0, "lbs"));
    }

    /*
    * Load inventory regarding the database operations
    * */
    public boolean loadInventory() {
        try {
            Database database = DatabaseManager.getDatabase(DatabaseManager.shoppingListDbStr);
            clearInventory();
            Query query = QueryBuilder.select(
                    SelectResult.expression(Meta.id),
                    SelectResult.property(GroceryItem.nameString),
                    SelectResult.property(GroceryItem.stockString),
                    SelectResult.property(GroceryItem.numberString)).from(DataSource.database(database)).orderBy(Ordering.expression(Meta.id));
            try {
                ResultSet rs = query.execute();
                for (Result result : rs) {
                    GroceryItem item = new GroceryItem(result.getString(GroceryItem.nameString), result.getDouble(GroceryItem.numberString), result.getString(GroceryItem.stockString));
                    shoppingList.add(item);
                }
            } catch (CouchbaseLiteException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean saveInventory() {
        try {
            Database database = DatabaseManager.getDatabase(DatabaseManager.shoppingListDbStr);
            wipeDatabase();
            for (GroceryItem item : shoppingList) {
                MutableDocument mutableDocument = new MutableDocument();
                mutableDocument.setString(GroceryItem.nameString, item.getName());
                mutableDocument.setString(GroceryItem.stockString, item.getStockType());
                mutableDocument.setDouble(GroceryItem.numberString, item.getNumber());
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

    public void wipeDatabase() {
        try {
            Database database = DatabaseManager.getDatabase(DatabaseManager.shoppingListDbStr);
            Query query = QueryBuilder.select(SelectResult.expression(Meta.id), SelectResult.all())
                    .from(DataSource.database(database));
            ResultSet rs = query.execute();
            for (Result result : rs) {
                String id = result.getString(0);
                Document doc = database.getDocument(id);
                database.delete(doc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public boolean syncInventory() {
//        return true;
//    }

    public boolean deleteInventory() {
        DatabaseManager dbmgr = DatabaseManager.getSharedInstance();
        dbmgr.deleteDatabaseForUser(DatabaseManager.shoppingListDbStr);
        return true;
    }

    //TODO: I do not think we need to upload the shopping list to cloud anymore
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
//        jsonObject.put(GroceryItem.numberString, new Integer(result.getInt(GroceryItem.numberString)));
//        return jsonObject;
//    }
}
