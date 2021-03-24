package com.uwaterloo.smartpantry.data;

import com.uwaterloo.smartpantry.inventory.Food;
import com.uwaterloo.smartpantry.inventory.WastedFood;

import java.util.Map;

public class InventoryMaps{
    private Map<String, Food> latestFoodMap;
    private Map<String, Food> oldestFoodMap;
    private Map<String, WastedFood> wastedFoodMap;

    public InventoryMaps(){}

    public Map<String, Food> getLatestFoodMap() {
        return latestFoodMap;
    }

    public void setLatestFoodMap(Map<String, Food> latestFoodMap) {
        this.latestFoodMap = latestFoodMap;
    }

    public Map<String, Food> getOldestFoodMap() {
        return oldestFoodMap;
    }

    public void setOldestFoodMap(Map<String, Food> oldestFoodMap) {
        this.oldestFoodMap = oldestFoodMap;
    }

    public Map<String, WastedFood> getWastedFoodMap() {
        return wastedFoodMap;
    }

    public void setWastedFoodMap(Map<String, WastedFood> wastedFoodMap) {
        this.wastedFoodMap = wastedFoodMap;
    }

}