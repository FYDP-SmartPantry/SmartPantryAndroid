package com.uwaterloo.smartpantry.inventory;

public interface Item {
    String getName();
    void setName(String name);

    Stock getStock();
    void setStock(Stock[] stock);

    Category getCategory();
    void setCategory(Category category);

    String getExpirationDate();
    void setExpirationDate(String expiration_date);

}
