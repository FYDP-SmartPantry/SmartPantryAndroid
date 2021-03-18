package com.uwaterloo.smartpantry.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.uwaterloo.smartpantry.R;
import com.uwaterloo.smartpantry.inventory.GroceryItem;

import java.util.ArrayList;
import java.util.List;

public class ShoppingItemAdapter extends RecyclerView.Adapter<ShoppingItemAdapter.ShoppingItemHolder>{

    private List<GroceryItem> shoppingList = new ArrayList<>();
    //Context context;

//    public ShoppingItemAdapter(Context context, List<GroceryItem> shoppingList) {
//        this.context = context;
//        this.shoppingList = shoppingList;
//    }

    @NonNull
    @Override
    public ShoppingItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.shoppinglistitem, parent, false);
        ShoppingItemHolder vHolder = new ShoppingItemHolder(itemView);
        return vHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ShoppingItemHolder holder, int position) {
        GroceryItem item = shoppingList.get(position);
        holder.textViewTitle.setText(item.getName());
        holder.textViewQuantity.setText(String.valueOf(item.getNumber()));
    }

    @Override
    public int getItemCount() {
        return shoppingList.size();
    }

    public void setShoppingList(List<GroceryItem> shoppingList) {
        this.shoppingList = shoppingList;
        notifyDataSetChanged();
    }

    class ShoppingItemHolder extends RecyclerView.ViewHolder {

        private TextView textViewTitle;
        private TextView textViewQuantity;

        public ShoppingItemHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_shoppingitemname);
            textViewQuantity = itemView.findViewById(R.id.text_view_quantity);
        }
    }
}
