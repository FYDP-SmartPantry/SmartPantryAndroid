package com.uwaterloo.smartpantry.database;
import android.content.Context;
import android.util.Log;

import com.couchbase.lite.CouchbaseLite;
import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.DatabaseConfiguration;
import com.couchbase.lite.ListenerToken;

import java.util.Map;

public class DatabaseManager {

    public static final String shoppingListDbStr = "shoppingListDB";
    public static final String inventoryDbStr = "InventoryDB";
    public static final String userInfoDbStr = "userDB";
    public static final String wastedFoodDbStr = "wastedFoodDb";

    private String tag = "dbManager";

    private static DatabaseManager instance = null;
    private static Database inventoryDb;
    private static Database shoppingListDb;
    private static Database userInfoDb;
    private static Database wastedFoodDb;

    public  String currentUser = null;

    protected DatabaseManager() {}

    public static DatabaseManager getSharedInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    public static Database getDatabase(String database_name) {
        if (database_name.equalsIgnoreCase(userInfoDbStr)) {
            return userInfoDb;
        } else if (database_name.equalsIgnoreCase(inventoryDbStr)) {
            return inventoryDb;
        } else if (database_name.equalsIgnoreCase(shoppingListDbStr)) {
            Log.d("asd","return shoppinglistdb");
            return shoppingListDb;
        } else if (database_name.equalsIgnoreCase(wastedFoodDbStr)) {
            return wastedFoodDb;
        } else {
            return null;
        }
    }

    public void initCouchbaseLite(Context context) {
        CouchbaseLite.init(context);
    }

    public void openOrCreateDatabaseForUser(Context context, String username) {
        DatabaseConfiguration config = new DatabaseConfiguration();
        config.setDirectory(String.format("%s/%s", context.getFilesDir().getAbsolutePath(), username));
        Log.d(tag, String.format("%s/%s", context.getFilesDir().getAbsolutePath(), username));
        currentUser = username;
        try {
            userInfoDb = new Database(userInfoDbStr, config);
            inventoryDb = new Database(inventoryDbStr, config);
            shoppingListDb = new Database(shoppingListDbStr, config);
            wastedFoodDb = new Database(wastedFoodDbStr, config);
        } catch (com.couchbase.lite.CouchbaseLiteException e) {
            e.printStackTrace();
        }
    }

    public void closeDatabaseForUser() {
        try {
            if (userInfoDb != null) {
                userInfoDb.close();
            }
            if (inventoryDb != null) {
                inventoryDb.close();
            }
            if (shoppingListDb != null) {
                shoppingListDb.close();
            }
            if (wastedFoodDb != null) {
                shoppingListDb.close();
            }
        } catch (com.couchbase.lite.CouchbaseLiteException e) {
            e.printStackTrace();
        }
    }

    public void deleteDatabaseForUser(String dbName) {
        try {
            if (dbName.equals(inventoryDbStr)) {
                inventoryDb.delete();
                Log.d(tag, String.format("%s is deleted", inventoryDbStr));
            } else if (dbName.equals(shoppingListDbStr)) {
                shoppingListDb.delete();
                Log.d(tag, String.format("%s is deleted", shoppingListDbStr));
            } else if (dbName.equals(userInfoDbStr)) {
                userInfoDb.delete();
                Log.d(tag, String.format("%s is deleted", userInfoDbStr));
            } else if (dbName.equals(wastedFoodDbStr)) {
                wastedFoodDb.delete();
                Log.d(tag, String.format("%s is deleted", wastedFoodDbStr));
            } else {
                Log.d(tag, String.format("Unrecognized dbName %s", dbName));
            }
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
    }

    //TODO: INVESTIGATE LIVE QUERY OPTION AND APPLICATION
}
