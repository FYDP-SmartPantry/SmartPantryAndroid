package com.uwaterloo.smartpantry.ui.foodstatus;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FoodstatusViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public FoodstatusViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}