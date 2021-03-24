package com.uwaterloo.smartpantry.ui.addshoppingitem;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.uwaterloo.smartpantry.R;
import com.uwaterloo.smartpantry.adapter.ShoppingItemAdapter;
import com.uwaterloo.smartpantry.inventory.GroceryItem;
import com.uwaterloo.smartpantry.inventory.ShoppingList;

import java.util.ArrayList;
import java.util.List;

public class AddItemToShoppinglistFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText editTextName;
    private EditText editTextQuantity;
    private EditText editTextStockType;
    public AddItemToShoppinglistFragment() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShoppinglistFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static com.uwaterloo.smartpantry.ui.addshoppingitem.AddItemToShoppinglistFragment newInstance(String param1, String param2) {
        com.uwaterloo.smartpantry.ui.addshoppingitem.AddItemToShoppinglistFragment fragment = new com.uwaterloo.smartpantry.ui.addshoppingitem.AddItemToShoppinglistFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //container.removeAllViews();
        View view =  inflater.inflate(R.layout.fragment_add_shopping_item, container, false);

        editTextName = view.findViewById(R.id.edit_shoppingitem_name);
        editTextQuantity = view.findViewById(R.id.edit_shoppingitem_quantity);
        editTextStockType = view.findViewById(R.id.edit_shoppingitem_stockType);
        Button backButton = view.findViewById(R.id.backAddItem);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStackImmediate();
            }
        });

        Button saveButton = view.findViewById(R.id.SaveAddItem);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveItem();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStackImmediate();
            }
        });

        return view;
    }

    private void saveItem() {
        String name = editTextName.getText().toString();
        String quantity = editTextQuantity.getText().toString();
        String stock = editTextStockType.getText().toString();
        if (name.trim().isEmpty() || quantity.trim().isEmpty() || stock.trim().isEmpty()) {
            Toast.makeText(getContext(), "please enter valid item name and quantity", Toast.LENGTH_SHORT).show();
            return;
        }

        ShoppingList shoppingList = ShoppingList.getInstance();
        shoppingList.addItemToInventory(new GroceryItem(name, Double.parseDouble(quantity), stock));
        shoppingList.saveInventory();
    }
}
