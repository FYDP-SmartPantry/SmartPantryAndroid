package com.uwaterloo.smartpantry.inventory;

public class Category {

    static final String meat = "MEAT";
    static final String fruit = "FRUIT";
    static final String vegetable = "VEGETABLE";
    static final String other = "OTHER";

    public enum CategoryEnum {
        MEAT,
        FRUIT,
        VEGETABLE,
        OTHER
    }

    public static String CategoryToString(CategoryEnum category) {
        if (category == CategoryEnum.MEAT) {
            return meat;
        } else if (category == CategoryEnum.FRUIT) {
            return fruit;
        } else if (category == CategoryEnum.VEGETABLE) {
            return vegetable;
        } else if (category == CategoryEnum.OTHER) {
            return other;
        } else {
            // should never happen
            return null;
        }
    }

    public static CategoryEnum StringToCategory(String category) {
        if (category.equalsIgnoreCase(meat)) {
            return CategoryEnum.MEAT;
        } else if (category.equalsIgnoreCase(fruit)) {
            return CategoryEnum.FRUIT;
        } else if (category.equalsIgnoreCase(vegetable)) {
            return CategoryEnum.VEGETABLE;
        } else if(category.equalsIgnoreCase(other)) {
            return CategoryEnum.OTHER;
        } else {
            // should never happen
            return null;
        }
    }

    // not allowed for construction
    protected Category() {}

}
