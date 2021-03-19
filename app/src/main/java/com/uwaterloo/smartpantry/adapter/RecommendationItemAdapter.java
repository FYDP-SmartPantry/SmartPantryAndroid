package com.uwaterloo.smartpantry.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.uwaterloo.smartpantry.R;
import com.uwaterloo.smartpantry.inventory.GroceryItem;
import com.uwaterloo.smartpantry.inventory.ShoppingList;

import org.w3c.dom.Text;

import java.util.List;

public class RecommendationItemAdapter extends BaseAdapter {

    private List<GroceryItem> list;

    public RecommendationItemAdapter(List<GroceryItem> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public GroceryItem getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public boolean isChecked(int position) {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recommendationitem, parent, false);
        }

        GroceryItem item = getItem(position);
        TextView tv_name = convertView.findViewById(R.id.RecommendationItemName);
        TextView tv_quantity = convertView.findViewById(R.id.RecommendationItemQuantity);
        tv_name.setText(item.getName());
        tv_quantity.setText(String.valueOf(item.getNumber()));
        CheckBox cb_check = convertView.findViewById(R.id.rowCheckBox);

        cb_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((CompoundButton) view).isChecked()){
                    System.out.println("Checked");
                    ShoppingList shpObj = ShoppingList.getInstance();
                    System.out.println("we have somehting");
                    shpObj.recommendList.add(item);
                } else {
                    System.out.println("Un-Checked");
                    ShoppingList shpObj = ShoppingList.getInstance();
                    System.out.println("we have somehting");
                    shpObj.recommendList.remove(item);
                }
            }
        });

        return convertView;
    }
}
