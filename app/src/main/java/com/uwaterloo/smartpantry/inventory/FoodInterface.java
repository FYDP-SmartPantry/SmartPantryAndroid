package com.uwaterloo.smartpantry.inventory;

/*
* Food:
* ----name
* ----stockType
* ----number
* ----category
* ----expirationDate
* */
public interface FoodInterface extends Item{
    String getExpirationDate();
    void setExpirationDate(String expiration_date);
}
