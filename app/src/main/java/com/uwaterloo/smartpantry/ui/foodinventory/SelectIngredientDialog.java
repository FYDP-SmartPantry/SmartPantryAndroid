package com.uwaterloo.smartpantry.ui.foodinventory;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;

public class SelectIngredientDialog extends DialogFragment implements DialogInterface.OnClickListener{
    private ArrayList<String> ingredients;

    public interface DialogListener {
        public void onIngredientDialogClick(String ingredient);
    }

    DialogListener listener;

    public SelectIngredientDialog(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (DialogListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + " must implement DialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select an ingredient")
                .setItems(ingredients.toArray(new String[0]), this::onClick);
        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        listener.onIngredientDialogClick(ingredients.get(i));
    }
}
