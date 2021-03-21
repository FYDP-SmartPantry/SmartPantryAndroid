package com.uwaterloo.smartpantry.ui.shoppinglist;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.uwaterloo.smartpantry.R;
import com.uwaterloo.smartpantry.adapter.ShoppingItemAdapter;
import com.uwaterloo.smartpantry.inventory.GroceryItem;
import com.uwaterloo.smartpantry.inventory.ShoppingList;
import com.uwaterloo.smartpantry.ui.addshoppingitem.AddItemToShoppinglistFragment;
import com.uwaterloo.smartpantry.ui.camera.CameraFragment;
import com.uwaterloo.smartpantry.ui.recommendation.RecommendationFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link com.uwaterloo.smartpantry.ui.shoppinglist.ShoppinglistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShoppinglistFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private boolean isOpen = false;
    private List<GroceryItem> shoppingList = new ArrayList<>();

    public ShoppinglistFragment() {
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
    public static com.uwaterloo.smartpantry.ui.shoppinglist.ShoppinglistFragment newInstance(String param1, String param2) {
        com.uwaterloo.smartpantry.ui.shoppinglist.ShoppinglistFragment fragment = new com.uwaterloo.smartpantry.ui.shoppinglist.ShoppinglistFragment();
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
        ShoppingList shoppingListobj = ShoppingList.getInstance();
        shoppingListobj.loadTestData();
        shoppingList = shoppingListobj.getShoppingList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_shoppinglist, container, false);

        FloatingActionButton buttonAddShoppingItem = view.findViewById(R.id.shoppinglistadd_fap);
        buttonAddShoppingItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddItemToShoppinglistFragment fragment = new AddItemToShoppinglistFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(((ViewGroup)getView().getParent()).getId(), fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });


        RecyclerView recyclerView = view.findViewById(R.id.shopping_list_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        ShoppingItemAdapter adapter = new ShoppingItemAdapter();
        // ShoppingItemAdapter adapter = new ShoppingItemAdapter(getContext(), shoppingList);
        adapter.setShoppingList(shoppingList);
        recyclerView.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                ShoppingList shoppingListobj = ShoppingList.getInstance();
                shoppingListobj.removeItemFromInventory(adapter.getGroceryItemAt(viewHolder.getAdapterPosition()));
                adapter.notifyDataSetChanged();
                Toast.makeText(getActivity(), "item removed", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        //for edit item. do we need this?
//        adapter.setOnItemClickListener(new ShoppingItemAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(GroceryItem item) {
//                AddItemToShoppinglistFragment fragment = new AddItemToShoppinglistFragment();
//                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(((ViewGroup)getView().getParent()).getId(), fragment);
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();
//            }
//        });

//        FloatingActionButton buttonRecommendation = view.findViewById(R.id.shoppinglistrecommd_fap);
//        buttonRecommendation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                RecommendationFragment fragment = new RecommendationFragment();
//                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(((ViewGroup)getView().getParent()).getId(), fragment);
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();
//            }
//        });

        FloatingActionButton buttonClear = view.findViewById(R.id.shoppinglistclear_fap);
        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShoppingList shoppingListobj = ShoppingList.getInstance();
                shoppingListobj.clearInventory();
                buttonAddShoppingItem.setVisibility(View.INVISIBLE);
//                buttonRecommendation.setVisibility(View.INVISIBLE);
                buttonClear.setVisibility(View.INVISIBLE);
                adapter.notifyDataSetChanged();
                isOpen = false;
            }
        });

        isOpen = false;
        FloatingActionButton buttonMenu = view.findViewById(R.id.shoppinglistmenu_fap);
        buttonMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpen) {
                    buttonAddShoppingItem.setVisibility(View.INVISIBLE);
//                    buttonRecommendation.setVisibility(View.INVISIBLE);
                    buttonClear.setVisibility(View.INVISIBLE);
                    isOpen = false;
                } else {
                    buttonAddShoppingItem.setVisibility(View.VISIBLE);
//                    buttonRecommendation.setVisibility(View.VISIBLE);
                    buttonClear.setVisibility(View.VISIBLE);
                    isOpen = true;
                }
            }
        });


        return view;
    }
}
