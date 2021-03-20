package com.uwaterloo.smartpantry.ui.foodinventory;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.error.VolleyError;
import com.couchbase.lite.internal.utils.StringUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.uwaterloo.smartpantry.R;
import com.uwaterloo.smartpantry.database.DatabaseManager;
import com.uwaterloo.smartpantry.datalink.DataLink;
import com.uwaterloo.smartpantry.datalink.DataLinkREST;
import com.uwaterloo.smartpantry.datalink.VolleyResponseListener;
import com.uwaterloo.smartpantry.inventory.Category;
import com.uwaterloo.smartpantry.inventory.Food;
import com.uwaterloo.smartpantry.inventory.FoodInventory;
import com.uwaterloo.smartpantry.ui.camera.CameraFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link com.uwaterloo.smartpantry.ui.foodinventory.FoodinventoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FoodinventoryFragment extends Fragment implements VolleyResponseListener, SelectIngredientDialog.DialogListener, AddInventoryDialog.InventoryDialogListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public static final int PICK_IMAGE = 1;

    private DatabaseManager dbManager = DatabaseManager.getSharedInstance();
    private FoodInventory foodInventory = new FoodInventory();

    public FoodinventoryFragment() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FoodinventoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static com.uwaterloo.smartpantry.ui.foodinventory.FoodinventoryFragment newInstance(String param1, String param2) {
        com.uwaterloo.smartpantry.ui.foodinventory.FoodinventoryFragment fragment = new com.uwaterloo.smartpantry.ui.foodinventory.FoodinventoryFragment();
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
        getParentFragmentManager().setFragmentResultListener("requestKey", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                ArrayList<String> ingredients = result.getStringArrayList("Ingredients");
                SelectIngredientDialog selectIngredientDialog = new SelectIngredientDialog(ingredients);
                selectIngredientDialog.setTargetFragment(FoodinventoryFragment.this, 1);
                selectIngredientDialog.show(getFragmentManager(), "SelectIngredient");
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        foodInventory.clearInventory();
        foodInventory.loadInventory();
    }

    public void initLoad() {
        foodInventory.loadInventory();
        Food toShop1 = new Food();
        toShop1.setName("orange");
        toShop1.setCategory(Category.CategoryEnum.FRUIT);
        toShop1.setStockType("lbs");
        toShop1.setNumber(5.0);
        toShop1.setExpirationDate("2021-1-3");
        try {
            foodInventory.addItemToInventory(toShop1);
            foodInventory.saveInventory();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_foodinventory, container, false);
        FloatingActionButton buttonAddInventoryItem = view.findViewById(R.id.inventoryadd_fap);
        DataLink.getInstance().initDataLink(this.getContext());
        dbManager.initCouchbaseLite(getContext());
        dbManager.openOrCreateDatabaseForUser(getContext(), DatabaseManager.currentUser);
        foodInventory.loadInventory();
//        initLoad();

        buttonAddInventoryItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(getContext(), buttonAddInventoryItem);
                popupMenu.getMenuInflater().inflate(R.menu.inventory_add_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                        switch (menuItem.getItemId()) {
                            case R.id.camera_add:
                                CameraFragment cameraFragment = new CameraFragment();
                                fragmentTransaction.replace(android.R.id.content, cameraFragment);
                                break;
                            case R.id.gallery_add:
                                Intent intent = new Intent();
                                intent.setType("image/*");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
                                break;
                            case R.id.manual_add:
                                onIngredientDialogClick("");
                                break;
                            default:
                                break;
                        }
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();

                        return true;
                    }
                });
                popupMenu.show();

            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == PICK_IMAGE) {
            Uri uri = data.getData();
            File file = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    "Image_" + System.currentTimeMillis() + ".jpg");
            try {
                Bitmap bitmap = null;
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
                try (FileOutputStream out = new FileOutputStream(file)) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            DataLinkREST.GetPrediction(file.getAbsolutePath(), this);
        }
    }

    @Override
    public void onSuccess(JSONArray response, String type) {
        System.out.println(response);
    }

    @Override
    public void onSuccess(JSONObject response, String type) {
        System.out.println(response);
        switch (type) {
            case "Predict":
                try {
                    JSONObject result = response.getJSONObject("result");
                    JSONArray prediction = result.getJSONArray("prediction");
                    ArrayList<String> ingredients = new ArrayList<String>();
                    for (int i = 0; i < prediction.length(); i++) {
                        if (!StringUtils.isEmpty(prediction.getString(i)) && !prediction.getString(i).equals("can not recognize item")) {
                            ingredients.add(prediction.getString(i));
                        }
                    }
                    ingredients.add("No Matches");
                    SelectIngredientDialog selectIngredientDialog = new SelectIngredientDialog(ingredients);
                    selectIngredientDialog.setTargetFragment(FoodinventoryFragment.this, 1);
                    selectIngredientDialog.show(getFragmentManager(), "SelectIngredient");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void onFailure(VolleyError error) {
        Log.v("Error", error.getMessage());
    }

    @Override
    public void onIngredientDialogClick(String ingredient) {
        System.out.println(ingredient);
        AddInventoryDialog addInventoryDialog = new AddInventoryDialog(ingredient);
        addInventoryDialog.setTargetFragment(this, 1);
        addInventoryDialog.show(getFragmentManager(), "AddIngredient");
    }

    @Override
    public void onAddDialogClick(String ingredient, String stock, Double quantity, Category.CategoryEnum category, String expirationDate) {
        Food food = new Food();
        food.setName(ingredient);
        food.setStockType(stock);
        food.setCategory(category);
        food.setNumber(quantity);
        food.setExpirationDate(expirationDate);
        try {
            foodInventory.addItemToInventory(food);
            foodInventory.saveInventory();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}