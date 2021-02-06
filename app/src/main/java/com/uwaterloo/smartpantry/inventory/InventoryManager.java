package com.uwaterloo.smartpantry.inventory;
import com.couchbase.lite.Database;
import com.couchbase.lite.MutableDocument;
import com.uwaterloo.smartpantry.database.DatabaseManager;
import com.uwaterloo.smartpantry.user.User;

import java.util.Map;

public class InventoryManager {

    private FoodInventory foodInventory;
    private FoodInventory wastedFoodInventory;
    private User currentUser = null;

    // The inventory should be per user based
    public InventoryManager(User user) {
        currentUser = user;
        foodInventory = new FoodInventory();
        wastedFoodInventory = new FoodInventory();
    }

    // TODO is there better design for this
    public void addFoodToFoodInventory(Food food) {
        foodInventory.addFoodMap(food);
    }

    public void removeFoodFromFoodInventory(Food food) {
        foodInventory.removeFromFoodMap(food);
    }


    public void moveFoodToWastedFoodList(Food food) {
        WastedFood wastedFood = new WastedFood(food);
        wastedFoodInventory.addFoodMap(wastedFood);
    }

    public void removeFromWastedFood(WastedFood wastedFood) {
        wastedFoodInventory.removeFromFoodMap(wastedFood);
    }


    public void flushToDatabase() {
        // TODO store whatever on the current Inventory to database?

    }

    // TODO once the app starts, we need to load to map
    public void loadInventory() {

    }
    // TODO handle the case of switching user
}
