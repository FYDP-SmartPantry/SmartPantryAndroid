package com.uwaterloo.smartpantry.ui.recommendation;

import androidx.lifecycle.ViewModel;

import com.uwaterloo.smartpantry.data.model.Recommendation;

public class RecommendationViewModel extends ViewModel {
    private Recommendation recommendation;

    public Recommendation getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(Recommendation recommendation) {
        this.recommendation = recommendation;
    }
}