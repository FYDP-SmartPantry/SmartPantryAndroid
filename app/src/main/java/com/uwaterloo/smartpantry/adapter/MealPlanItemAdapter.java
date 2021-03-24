package com.uwaterloo.smartpantry.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.uwaterloo.smartpantry.R;
import com.uwaterloo.smartpantry.inventory.MealPlanItem;

import java.util.ArrayList;
import java.util.List;

public class MealPlanItemAdapter extends RecyclerView.Adapter<MealPlanItemAdapter.MealPlanItemHolder> {
    private List<MealPlanItem> items = new ArrayList<>();

    @NonNull
    @Override
    public MealPlanItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.mealplanitem, parent, false);
        MealPlanItemHolder vHolder = new MealPlanItemHolder(itemView);
        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MealPlanItemHolder holder, int position) {
        MealPlanItem item = items.get(position);
        holder.name.setText(item.getTitle());
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<MealPlanItem> mealPlanList) {
        this.items = mealPlanList;
        notifyDataSetChanged();
    }

    public MealPlanItem getItemAt(int position) {
        return items.get(position);
    }

    class MealPlanItemHolder extends RecyclerView.ViewHolder {

        private TextView name;
        public MealPlanItemHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.meal_plan_item);
        }
    }
}
