package com.uwaterloo.smartpantry.ui.recommendation;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.uwaterloo.smartpantry.R;

import java.util.ArrayList;
import java.util.Set;

public class RecipeFilterDialog extends DialogFragment {
    public static String[] MEALTYPE = new String[] {"Main Course", "Breakfast", "Side Dish", "Dessert",
            "Appetizer", "Salad", "Bread", "Soup", "Beverage", "Sauce", "Marinade", "Fingerfood", "Snack", "Drink"};

    public static String[] CUISINE = new String[] {"African", "American", "British", "Cajun",
            "Caribbean", "Chinese", "Eastern European", "European", "French", "German", "Greek", "Indian",
    "Irish", "Italian", "Japanese", "Jewish", "Korean", "Latin American", "Mediterranian", "Mexican",
    "Middle Eastern", "Nordic", "Southern", "Spanish", "Thai", "Vietnamese"};

    public static String[] DIET = new String[] {"Gluten Free", "Ketogenic", "Vegetarian", "Lacto-Vegetarian",
    "Ovo-Vegetarian", "Vegan", "Pescetarian", "Paleo", "Primal", "Whole30"};

    private ArrayList<String> filteredIngredients;
    private ArrayList<String> filteredType;
    private ArrayList<String> filteredCuisine;
    private ArrayList<String> filteredDiet;

    public ArrayList<String> ingredients;

    public RecipeFilterDialog(Set<String> ingredients) {
        this.ingredients = new ArrayList<String>(ingredients);
    }

    public interface RecipeDialogListener {
        public void onFilterDialogClick(ArrayList<String> filteredIngredients, ArrayList<String> filteredType,
                                        ArrayList<String> filteredCuisine, ArrayList<String> filteredDiet);
    }

    RecipeDialogListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (RecipeDialogListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + " must implement DialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Recipe Filter");

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_filterrecipe, null);
        filteredIngredients = new ArrayList<String>();
        filteredType = new ArrayList<String>();
        filteredCuisine = new ArrayList<String>();
        filteredDiet = new ArrayList<String>();

        ListView ingredientFilter = view.findViewById(R.id.ingredient_filter);
        ArrayAdapter<String> ingredientAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_multiple_choice, this.ingredients);
        ingredientFilter.setAdapter(ingredientAdapter);

        TextView ingredientTitle = view.findViewById(R.id.ingredient_filter_title);
        ingredientTitle.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                switch (ingredientFilter.getVisibility()) {
                    case View.GONE:
                        ingredientFilter.setVisibility(View.VISIBLE);
                        break;
                    case View.VISIBLE:
                        ingredientFilter.setVisibility(View.GONE);
                        break;
                    default:
                        break;
                }
            }
        });

        ingredientFilter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SparseBooleanArray positions = ingredientFilter.getCheckedItemPositions();
                filteredIngredients.clear();
                for (int j = 0; j < adapterView.getAdapter().getCount(); j++) {
                    if (positions.get(j)) {
                        filteredIngredients.add(ingredients.get(j));
                    }
                }
            }
        });

        ListView typeFilter = view.findViewById(R.id.recipe_meal_filter);
        typeFilter.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_multiple_choice, MEALTYPE));
        typeFilter.setVisibility(View.GONE);

        TextView mealTypeTitle = view.findViewById(R.id.recipe_meal_title);
        mealTypeTitle.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                switch (typeFilter.getVisibility()) {
                    case View.GONE:
                        typeFilter.setVisibility(View.VISIBLE);
                        break;
                    case View.VISIBLE:
                        typeFilter.setVisibility(View.GONE);
                        break;
                    default:
                        break;
                }
            }
        });

        typeFilter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SparseBooleanArray positions = typeFilter.getCheckedItemPositions();
                filteredType.clear();
                for (int j = 0; j < adapterView.getAdapter().getCount(); j++) {
                    if (positions.get(j)) {
                        filteredType.add(MEALTYPE[j]);
                    }
                }
            }
        });

        ListView cuisineFilter = view.findViewById(R.id.recipe_cuisine_filter);
        cuisineFilter.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_multiple_choice, CUISINE));
        cuisineFilter.setVisibility(View.GONE);

        TextView cuisineTitle = view.findViewById(R.id.recipe_cuisine_title);
        cuisineTitle.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                switch (cuisineFilter.getVisibility()) {
                    case View.GONE:
                        cuisineFilter.setVisibility(View.VISIBLE);
                        break;
                    case View.VISIBLE:
                        cuisineFilter.setVisibility(View.GONE);
                        break;
                    default:
                        break;
                }
            }
        });

        cuisineFilter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SparseBooleanArray positions = cuisineFilter.getCheckedItemPositions();
                filteredCuisine.clear();
                for (int j = 0; j < adapterView.getAdapter().getCount(); j++) {
                    if (positions.get(j)) {
                        filteredCuisine.add(CUISINE[j]);
                    }
                }
            }
        });

        ListView dietFilter = view.findViewById(R.id.recipe_diet_filter);
        dietFilter.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_multiple_choice, DIET));
        dietFilter.setVisibility(View.GONE);

        TextView dietTitle = view.findViewById(R.id.recipe_diet_title);
        dietTitle.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                switch (dietFilter.getVisibility()) {
                    case View.GONE:
                        dietFilter.setVisibility(View.VISIBLE);
                        break;
                    case View.VISIBLE:
                        dietFilter.setVisibility(View.GONE);
                        break;
                    default:
                        break;
                }
            }
        });

        dietFilter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SparseBooleanArray positions = dietFilter.getCheckedItemPositions();
                filteredDiet.clear();
                for (int j = 0; j < adapterView.getAdapter().getCount(); j++) {
                    if (positions.get(j)) {
                        filteredDiet.add(DIET[j]);
                    }
                }
            }
        });

        builder.setPositiveButton("Filter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.onFilterDialogClick(filteredIngredients, filteredType, filteredCuisine, filteredDiet);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getDialog().dismiss();
            }
        });

        builder.setView(view);
        return builder.create();
    }
}
