package com.uwaterloo.smartpantry.data;

import com.uwaterloo.smartpantry.inventory.Category;
import com.uwaterloo.smartpantry.inventory.Food;
import com.uwaterloo.smartpantry.inventory.GroceryItem;
import com.uwaterloo.smartpantry.inventory.WastedFood;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class Utility {
    public static List<GroceryItem> parseShopping(JSONObject obj) {
        List<GroceryItem> groceryItems = new ArrayList<GroceryItem>();
        try {
            JSONArray aisles = obj.getJSONArray("aisles");
            for (int i = 0; i < aisles.length(); i++) {
                JSONObject aisle = aisles.getJSONObject(i);
                JSONArray items = aisle.getJSONArray("items");
                for (int j = 0; j < items.length(); j++) {
                    JSONObject item = items.getJSONObject(j);
                    GroceryItem groceryItem = new GroceryItem();
                    groceryItem.setName(item.getString("name"));
                    JSONObject measures = item.getJSONObject("measures");
                    JSONObject metric = measures.getJSONObject("metric");
                    groceryItem.setStockType(metric.getString("unit"));
                    groceryItem.setNumber(metric.getDouble("amount"));
                    groceryItems.add(groceryItem);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return groceryItems;
    }

    public static Map<String, Food> getCategorized(Category.CategoryEnum categoryEnum, Map<String, Food> foodMap) {
        Map<String, Food> resultMap = new HashMap<>();
        foodMap.keySet().forEach((key) -> {
            Food food = foodMap.get(key);
            if (food.getCategory() == categoryEnum) {
                resultMap.put(food.getName(), food);
            }
        });

        return resultMap;
    }

    public static float getFoodQuantityCategorized(Category.CategoryEnum categoryEnum, Map<String, Food> foodMap) {
        AtomicReference<Float> total = new AtomicReference<>((float) 0);
        foodMap.keySet().forEach((key) -> {
            Food food = foodMap.get(key);
            if (food.getCategory() == categoryEnum) {
                total.updateAndGet(v -> new Float((float) (v + food.getNumber())));
            }
        });
        return total.get();
    }

    public static float getWastedFoodQuantityCategorized(Category.CategoryEnum categoryEnum, Map<String, WastedFood> wastedFoodMap) {
        AtomicReference<Float> total = new AtomicReference<>((float) 0);
        wastedFoodMap.keySet().forEach((key) -> {
            WastedFood wastedFood = wastedFoodMap.get(key);
            if (wastedFood.getCategory() == categoryEnum) {
                total.updateAndGet(v -> new Float((float) (v + wastedFood.getNumber())));
            }
        });
        return total.get();
    }

    public static Map<String, WastedFood> getWastedCategorized(Category.CategoryEnum categoryEnum, Map<String, WastedFood> wastedFoodMap) {
        Map<String, WastedFood> resultMap = new HashMap<>();
        wastedFoodMap.keySet().forEach((key) -> {
            WastedFood wastedFood = wastedFoodMap.get(key);
            if (wastedFood.getCategory() == categoryEnum) {
                resultMap.put(wastedFood.getName(), wastedFood);
            }
        });

        return resultMap;
    }

    public static InventoryMaps parseInventories(JSONObject obj) {
        Map<String, Food> latestFoodMap = new HashMap<>();
        Map<String, Food> oldestFoodMap = new HashMap<>();

        Map<String, WastedFood> wastedFoodMap = new HashMap<>();

        try {
            JSONArray records = obj.getJSONArray("records");
            for (int i = 0; i < records.length(); i++) {
                JSONObject record = records.getJSONObject(i);
                JSONArray foodInventory = record.getJSONArray("food_inventory");
                JSONArray wasteInventory = record.getJSONArray("waste_inventory");
                String date = record.getJSONObject("timestamp").getString("$date");
                date = date.substring(0, 10);

                LocalDate curDate = LocalDate.parse(date);
                LocalDate cutoff = LocalDate.now();
                cutoff = cutoff.with(TemporalAdjusters.previous(DayOfWeek.MONDAY));
                if (i == 0) {
                    for (int j = 0; j < foodInventory.length(); j++) {
                        Food food = new Food();
                        JSONObject item = foodInventory.getJSONObject(j);
                        food.setName(item.getString("name"));
                        food.setStockType(item.getString("stockType"));
                        food.setNumber(item.getDouble("count"));
                        food.setCategory(Category.CategoryEnum.valueOf(item.getString("category")));
                        food.setExpirationDate(item.getString("expiration_date"));
                        latestFoodMap.put(food.getName(), food);
                    }
                    for (int j = 0; j < wasteInventory.length(); j++) {
                        WastedFood wastedFood = new WastedFood();
                        JSONObject item = wasteInventory.getJSONObject(j);
                        wastedFood.setName(item.getString("name"));
                        wastedFood.setStockType(item.getString("stockType"));
                        wastedFood.setNumber(item.getDouble("count"));
                        wastedFood.setCategory(Category.CategoryEnum.valueOf(item.getString("category")));
                        wastedFood.addReason(item.getString("reason"));
                        wastedFoodMap.put(wastedFood.getName(), wastedFood);
                    }
                }
                if (curDate.isBefore(cutoff) || i + 1 == records.length()) {
                    for (int j = 0; j < foodInventory.length(); j++) {
                        Food food = new Food();
                        JSONObject item = foodInventory.getJSONObject(j);
                        food.setName(item.getString("name"));
                        food.setStockType(item.getString("stockType"));
                        food.setNumber(item.getDouble("count"));
                        food.setCategory(Category.CategoryEnum.valueOf(item.getString("category")));
                        food.setExpirationDate(item.getString("expiration_date"));
                        oldestFoodMap.put(food.getName(), food);
                    }
                    break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        InventoryMaps inventoryMaps = new InventoryMaps();
        inventoryMaps.setLatestFoodMap(latestFoodMap);
        inventoryMaps.setOldestFoodMap(oldestFoodMap);
        inventoryMaps.setWastedFoodMap(wastedFoodMap);

        return inventoryMaps;
    }
}
