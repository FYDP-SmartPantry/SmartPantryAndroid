package com.uwaterloo.smartpantry.ui.foodinventory;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FoodinventoryViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public FoodinventoryViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}