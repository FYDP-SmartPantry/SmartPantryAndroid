package com.uwaterloo.smartpantry.ui.foodinventory;

import android.content.Context;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.uwaterloo.smartpantry.inventory.Food;
import com.uwaterloo.smartpantry.ui.foodinventory.FoodItem;
import com.uwaterloo.smartpantry.R;

import java.util.ArrayList;

public class CustomListAdapter extends BaseAdapter {
    private Context context; //context
    private int resource;
    private ArrayList<FoodItem> items; //data source of the list adapter

    //public constructor
    public CustomListAdapter(Context context, int resource, ArrayList<FoodItem> items) {
        this.context = context;
        this.resource = resource;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size(); //returns total of items in the list
    }

    @Override
    public Object getItem(int position) {
        return items.get(position); //returns list item at the specified position
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // inflate the layout for each list row
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.listview_row_items, parent, false);
        }

        // get current item to be displayed
        FoodItem currentItem = (FoodItem) getItem(position);

        // get the TextView for item name and item description
        TextView textViewName = (TextView)
                convertView.findViewById(R.id.food_name);
        TextView textViewQuantity = (TextView)
                convertView.findViewById(R.id.food_quantity);
        TextView textViewExpiration = (TextView)
                convertView.findViewById(R.id.food_expiration);

        //sets the text for item name and item description from the current item object
        textViewName.setText(currentItem.getName());
        textViewQuantity.setText(currentItem.getQuantity());
        textViewExpiration.setText(currentItem.getExpiration());

        // returns the view for the current row
        return convertView;
    }
}
