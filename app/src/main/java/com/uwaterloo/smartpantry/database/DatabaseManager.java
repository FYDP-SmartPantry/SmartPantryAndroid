package com.uwaterloo.smartpantry.database;
import android.content.Context;

import androidx.annotation.NonNull;

import com.couchbase.lite.CouchbaseLite;
import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.DatabaseChange;
import com.couchbase.lite.DatabaseChangeListener;
import com.couchbase.lite.Database;
import com.couchbase.lite.DatabaseConfiguration;
import com.couchbase.lite.ListenerToken;

import java.util.Map;

public class DatabaseManager {

    private static DatabaseManager instance = null;
    private static Map<String, Database> mDbMap;
    private static Object CouchbaseLiteException;
    private ListenerToken listenerToken;

    public  String currentUser = null;

    protected DatabaseManager() {

    }

    public static DatabaseManager getSharedInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    public static Database getDatabase(String database_name) throws Exception {
        if(database_name != null && !database_name.trim().isEmpty()) {
            // throw exception here
            throw new Exception("database signature is null or empty");
        } else {
            Database db = mDbMap.get(database_name);
            if (db != null) {
                return db;
            } else {
                // throw exception here
                throw new Exception("database is null");
            }
        }
    }

    public void initCouchbaseLite(Context context) {
        CouchbaseLite.init(context);
    }

    public void openOrCreateDatabaseForUser(Context context, String username) {
        DatabaseConfiguration config = new DatabaseConfiguration();
        config.setDirectory(String.format("%s/%s", context.getFilesDir(), username));

        currentUser = username;
        try {
            Database userdb = new Database("user", config);
            Database userInventory = new Database("inventory", config);
            mDbMap.put("user", userdb);
            mDbMap.put("inventory", userInventory);
        } catch (com.couchbase.lite.CouchbaseLiteException e) {
            e.printStackTrace();
        }
    }

    public void closeDatabaseForUser() {
        try {
            if (!mDbMap.isEmpty()) {
                for (Map.Entry<String, Database> entry: mDbMap.entrySet()) {
                    Database db = entry.getValue();
                    db.close();
                    mDbMap.remove(entry.getKey());
                }
            }
        } catch (com.couchbase.lite.CouchbaseLiteException e) {
            e.printStackTrace();
        }
    }

    //TODO: INVESTIGATE LIVE QUERY OPTION AND APPLICATION

}
