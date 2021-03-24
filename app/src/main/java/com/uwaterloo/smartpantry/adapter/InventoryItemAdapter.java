package com.uwaterloo.smartpantry.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.uwaterloo.smartpantry.R;
import com.uwaterloo.smartpantry.inventory.Food;
import com.uwaterloo.smartpantry.inventory.GroceryItem;

import java.util.ArrayList;
import java.util.List;

public class InventoryItemAdapter extends RecyclerView.Adapter<InventoryItemAdapter.InventoryItemHolder>{
    private List<Food> foods = new ArrayList<>();
    private OnItemClickListener listener;

    @NonNull
    @Override
    public InventoryItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.inventoryitem, parent, false);
        InventoryItemHolder vHolder = new InventoryItemHolder(itemView);
        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull InventoryItemHolder holder, int position) {
        Food food = foods.get(position);
        holder.name.setText(food.getName());
        holder.stock.setText(food.getStockType());
        holder.quantity.setText(Double.toString(food.getNumber()));
        holder.expiration.setText(food.getExpirationDate());

        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                listener.onItemClick(food);
            }
        });
    }

    @Override
    public int getItemCount() {
        return foods.size();
    }

    public void setItems(List<Food> foodsList) {
        this.foods = foodsList;
        notifyDataSetChanged();
    }

    public Food getItemAt(int position) {
        return foods.get(position);
    }

    class InventoryItemHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private TextView stock;
        private TextView quantity;
        private TextView expiration;

        public InventoryItemHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.inventory_name);
            stock = itemView.findViewById(R.id.inventory_stock);
            quantity = itemView.findViewById(R.id.inventory_quantity);
            expiration = itemView.findViewById(R.id.inventory_expiration);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Food food);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
