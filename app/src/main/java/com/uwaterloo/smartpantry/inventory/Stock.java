package com.uwaterloo.smartpantry.inventory;

public class Stock {
    private String mType;
    private int mNumber;

    public Stock(String type, int number) {
        mType = type;
        mNumber = number;
    }

    public int getNumber() {
        return mNumber;
    }

    public String getType() {
        return mType;
    }

    public void setNumber(int mNumber) {
        this.mNumber = mNumber;
    }

    public void setType(String mType) {
        this.mType = mType;
    }
}
