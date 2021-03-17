package com.uwaterloo.smartpantry.stats;

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
import com.uwaterloo.smartpantry.inventory.Food;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RecipeTrackingList {

    private Map<String, RecipeTracking> trackingMap = new HashMap<>();

    public Set<String> getKeySet() {
        return trackingMap.keySet();
    }

    public void addItemToInventory(RecipeTracking recipeTracking) throws Exception {
        if (!trackingMap.containsKey(recipeTracking.getRecipeId())) {
            trackingMap.put(recipeTracking.getRecipeId(), recipeTracking);
        } else {
            RecipeTracking currItem = trackingMap.get(recipeTracking.getRecipeId());
            currItem.setCount(currItem.getCount() + recipeTracking.getCount());
            trackingMap.put(recipeTracking.getRecipeId(), currItem);
        }
    }

    public void removeItemFromInventory(RecipeTracking item) {
        if (trackingMap.containsKey(item.getRecipeId())) {
            trackingMap.remove(item.getRecipeId());
        }
    }

    public int InventorySize() { return trackingMap.size(); }

    public void clearInventory() { trackingMap.clear(); }

    public RecipeTracking getTracking(String recipeId) { return trackingMap.get(recipeId); }

    public void updateItem(String recipeId, RecipeTracking item) {
        if (trackingMap.containsKey(recipeId)) {
            trackingMap.remove(recipeId);
        }
        trackingMap.put(recipeId, item);
    }

    public boolean load() {
        try {
            Database database = DatabaseManager.getDatabase(DatabaseManager.recipeTrackDbStr);
            Query query = QueryBuilder.select(
                    SelectResult.expression(Meta.id),
                    SelectResult.property(RecipeTracking.recipeIdString),
                    SelectResult.property(RecipeTracking.countString)).from(DataSource.database(database)).orderBy(Ordering.expression(Meta.id));
            try {
                ResultSet rs = query.execute();
                for (Result result : rs) {
                    RecipeTracking recipeTracking = new RecipeTracking();
                    recipeTracking.setRecipeId(result.getString(RecipeTracking.recipeIdString));
                    recipeTracking.setCount(result.getInt(RecipeTracking.countString));

                    trackingMap.put(recipeTracking.getRecipeId(), recipeTracking);
                }
            } catch (CouchbaseLiteException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean save() {
        try {
            Database database = DatabaseManager.getDatabase(DatabaseManager.recipeTrackDbStr);
            for (Map.Entry<String, RecipeTracking> trackingEntry : trackingMap.entrySet()) {
                MutableDocument mutableDocument = new MutableDocument();
                mutableDocument.setString(RecipeTracking.recipeIdString, trackingEntry.getValue().getRecipeId());
                mutableDocument.setInt(RecipeTracking.countString, trackingEntry.getValue().getCount());
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

    public boolean deleteTrackingList() throws Exception {
        DatabaseManager dbmgr = DatabaseManager.getSharedInstance();
        dbmgr.deleteDatabaseForUser(DatabaseManager.recipeTrackDbStr);
        return true;
    }

}
