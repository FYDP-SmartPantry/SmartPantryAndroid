package com.uwaterloo.smartpantry.ui.foodinventory;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.couchbase.lite.internal.utils.StringUtils;
import com.uwaterloo.smartpantry.R;
import com.uwaterloo.smartpantry.inventory.Category;
import com.uwaterloo.smartpantry.inventory.Food;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

public class EditInventoryDialog extends DialogFragment {
    private EditText ingredientName;
    private EditText stockType;
    private EditText quantity;
    private Spinner category;
    private EditText expirationDate;

    private Food food;
    private AddInventoryDialog.InventoryDialogListener listener;

    public EditInventoryDialog(Food food) {
        this.food = food;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (AddInventoryDialog.InventoryDialogListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + " must implement DialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Inventory Edit");

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_additem, null);

        ingredientName = view.findViewById(R.id.input_ingredient_name);
        stockType = view.findViewById(R.id.input_stock_type);
        quantity = view.findViewById(R.id.input_quantity);
        category = view.findViewById(R.id.input_category);
        expirationDate = view.findViewById(R.id.input_expiration_date);

        ingredientName.setText(food.getName());
        stockType.setText(food.getStockType());
        quantity.setText(Double.toString(food.getNumber()));
        category.setAdapter(new ArrayAdapter<Category.CategoryEnum>(getContext(), android.R.layout.simple_spinner_item ,Category.CategoryEnum.values()));
        category.setSelection(food.getCategory().ordinal());
        expirationDate.setText(food.getExpirationDate());

        builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                editInventory();
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

    public void editInventory() {
        if (StringUtils.isEmpty(ingredientName.getText().toString())
                || StringUtils.isEmpty(quantity.getText().toString())
                || StringUtils.isEmpty(expirationDate.getText().toString())) {
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        String ingredient = ingredientName.getText().toString();
        String stock = stockType.getText().toString();
        Double quantity = Double.parseDouble(this.quantity.getText().toString());
        Category.CategoryEnum categoryEnum = Category.CategoryEnum.valueOf(category.getSelectedItem().toString());
        String expirationDate = this.expirationDate.getText().toString();

        Food result = new Food();
        result.setName(ingredient);
        result.setStockType(stock);
        result.setNumber(quantity);
        result.setCategory(categoryEnum);
        result.setExpirationDate(expirationDate);

        listener.onEditDialogClick(result);
    }

}
