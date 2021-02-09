package com.uwaterloo.smartpantry.inventory;

/*
* For a certain Item, there are 4 types
* */
public interface Item {

    String getName();
    void setName(String name);

    String getStockType();
    void setStockType(String stockType);

    int getNumber();
    void setNumber(int number);

    Category.CategoryEnum getCategory();
    void setCategory(Category.CategoryEnum category);

}
