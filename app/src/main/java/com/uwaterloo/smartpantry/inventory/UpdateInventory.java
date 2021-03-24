package com.uwaterloo.smartpantry.inventory;

import java.time.LocalDate;
import java.util.ArrayList;

public class UpdateInventory {
    public UpdateInventory() {}
    public ArrayList DateCheck() throws Exception {
        FoodInventory currentInv = new FoodInventory();
        WastedFoodInventory currentWInv = new WastedFoodInventory();

        currentInv.loadInventory();
        currentWInv.loadInventory();

        ArrayList<String> FoodNames = currentInv.ListOfFoods();
        LocalDate CurrentTime = LocalDate.now();
        ArrayList<String> AlertList = new ArrayList();

        for(String name : FoodNames){
            Food CurrentItem = currentInv.getFood(name);
            LocalDate localDate = LocalDate.parse(CurrentItem.getExpirationDate());
            if(	CurrentTime.isAfter(localDate)){
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
                if(CurrentTime.compareTo(localDate) < 4){
                    String DayLeft = String.valueOf(CurrentTime.compareTo(localDate));
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
