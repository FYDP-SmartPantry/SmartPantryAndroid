package com.uwaterloo.smartpantry.inventory;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;

import com.uwaterloo.smartpantry.MainActivity;

import java.util.ArrayList;
import java.util.Date;

public class UpdateInventory {
    public UpdateInventory() {}
    public ArrayList DateCheck() throws Exception {
        FoodInventory currentInv = new FoodInventory();
        WastedFoodInventory currentWInv = new WastedFoodInventory();

        currentInv.loadInventory();
        currentWInv.loadInventory();

        ArrayList<String> FoodNames = currentInv.ListOfFoods();
        Date CurrentTime = new Date();
        ArrayList<String> AlertList = new ArrayList();

        for(String name : FoodNames){
            Food CurrentItem = currentInv.getFood(name);
            String NumDate = CurrentItem.getExpirationDate();
            String ExpYear = NumDate.substring(0,3);
            String ExpMonth = NumDate.substring(5,6);
            String ExpDay = NumDate.substring(8,9);
            Date ExpDate = new Date(Integer.parseInt(ExpYear)-1900,Integer.parseInt(ExpMonth),Integer.parseInt(ExpDay));
            if(	CurrentTime.after(ExpDate)){
                WastedFood food = new WastedFood();
                food.setName(CurrentItem.getName());
                food.setCategory(CurrentItem.getCategory());
                food.setStockType(CurrentItem.getStockType());
                food.setNumber(CurrentItem.getNumber());
                food.addReason("Expired");
                currentWInv.addItemToInventory(food);
                currentInv.removeItemFromInventory(CurrentItem);
                AlertList.add(CurrentItem.getName()+": Expired");
            }else{
                if(CurrentTime.compareTo(ExpDate) < 4){
                    String DayLeft = String.valueOf(CurrentTime.compareTo(ExpDate));
                    AlertList.add(CurrentItem.getName()+": "+ DayLeft + " days left");
                }
            }
        }
        currentInv.saveInventory();
        currentWInv.saveInventory();
        return AlertList;
        //return an ArrayList of strings that have a list of all food need to alert to users
    }
}
