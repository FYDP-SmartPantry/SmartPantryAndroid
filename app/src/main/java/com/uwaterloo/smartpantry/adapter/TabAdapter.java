package com.uwaterloo.smartpantry.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.uwaterloo.smartpantry.ui.recommendation.RecommendationMealPlanFragment;
import com.uwaterloo.smartpantry.ui.recommendation.RecommendationRecipeFragment;

public class TabAdapter extends FragmentPagerAdapter {

    public TabAdapter(FragmentManager fm) {
        super(fm);
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                return new RecommendationRecipeFragment();
            case 1:
                return new RecommendationMealPlanFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
