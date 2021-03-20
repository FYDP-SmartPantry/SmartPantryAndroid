package com.uwaterloo.smartpantry.inventory;

/*
* For a certain Item, there are 4 types
* */
public interface Item {

    String getName();
    void setName(String name);

    String getStockType();
    void setStockType(String stockType);

    Double getNumber();
    void setNumber(Double number);

    Category.CategoryEnum getCategory();
    void setCategory(Category.CategoryEnum category);

}
