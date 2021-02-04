package com.uwaterloo.smartpantry.ui.foodcamera;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FoodcameraViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public FoodcameraViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}