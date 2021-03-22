package com.uwaterloo.smartpantry.ui.foodinventory;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
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
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.error.VolleyError;
import com.couchbase.lite.internal.utils.StringUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.uwaterloo.smartpantry.MainActivity;
import com.uwaterloo.smartpantry.R;
import com.uwaterloo.smartpantry.adapter.InventoryItemAdapter;
import com.uwaterloo.smartpantry.adapter.MealPlanItemAdapter;
import com.uwaterloo.smartpantry.database.DatabaseManager;
import com.uwaterloo.smartpantry.datalink.DataLink;
import com.uwaterloo.smartpantry.datalink.DataLinkREST;
import com.uwaterloo.smartpantry.datalink.VolleyResponseListener;
import com.uwaterloo.smartpantry.inventory.Category;
import com.uwaterloo.smartpantry.inventory.Food;
import com.uwaterloo.smartpantry.inventory.FoodInventory;
import com.uwaterloo.smartpantry.inventory.MealPlanItem;
import com.uwaterloo.smartpantry.inventory.UpdateInventory;
import com.uwaterloo.smartpantry.inventory.WastedFood;
import com.uwaterloo.smartpantry.inventory.WastedFoodInventory;
import com.uwaterloo.smartpantry.ui.camera.CameraFragment;
import com.uwaterloo.smartpantry.ui.recommendation.RecommendationMealPlanFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link com.uwaterloo.smartpantry.ui.foodinventory.FoodinventoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FoodinventoryFragment extends Fragment implements VolleyResponseListener,
        SelectIngredientDialog.DialogListener, AddInventoryDialog.InventoryDialogListener,
        InventoryItemAdapter.OnItemClickListener{
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

    private List<Food> foodsList = new ArrayList<Food>();
    private InventoryItemAdapter adapter = new InventoryItemAdapter();

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_foodinventory, container, false);
        FloatingActionButton buttonAddInventoryItem = view.findViewById(R.id.inventoryadd_fap);
        DataLink.getInstance().initDataLink(this.getContext());
        dbManager.initCouchbaseLite(getContext());
        dbManager.openOrCreateDatabaseForUser(getContext(), DatabaseManager.currentUser);

        UpdateInventory updateInventory = new UpdateInventory();
        try {
            ArrayList<String> alerts = updateInventory.DateCheck();
            SendNotifications(alerts);
        } catch (Exception e) {
            e.printStackTrace();
        }

        foodInventory.loadInventory();
        foodsList = foodInventory.exportInventory();

        RecyclerView recyclerView = view.findViewById(R.id.inventory_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        adapter.setItems(foodsList);
        adapter.setOnItemClickListener(this::onItemClick);
        recyclerView.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Food food = foodsList.remove(viewHolder.getAdapterPosition());

                foodInventory.removeItemFromInventory(food);
                foodInventory.saveInventory();

                adapter.setItems(foodsList);
                adapter.notifyDataSetChanged();
                Toast.makeText(getContext(), "Item removed from inventory", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

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
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();
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
                            case R.id.inventory_sync:
                                FoodInventory foodInventory = new FoodInventory();
                                foodInventory.loadInventory();
                                List<Food> foods = foodInventory.exportInventory();

                                WastedFoodInventory wastedFoodInventory = new WastedFoodInventory();
                                wastedFoodInventory.loadInventory();
                                List<WastedFood> wastedFoods = wastedFoodInventory.exportInventory();

                                DataLinkREST.SyncInventories(foods, wastedFoods, FoodinventoryFragment.this);
                                break;
                            default:
                                break;
                        }
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
            case "InventorySync":
                Toast.makeText(getContext(), "Inventory Synced", Toast.LENGTH_SHORT).show();
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
    public void onAddDialogClick(Food food) {
        try {
            foodInventory.addItemToInventory(food);
            foodInventory.saveInventory();
            foodsList = foodInventory.exportInventory();
            adapter.setItems(foodsList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onEditDialogClick(Food food) {
        try {
            foodInventory.updateItem(food.getName(), food);
            foodInventory.saveInventory();
            foodsList = foodInventory.exportInventory();
            adapter.setItems(foodsList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(Food food) {
        EditInventoryDialog editInventoryDialog = new EditInventoryDialog(food);
        editInventoryDialog.setTargetFragment(this, 1);
        editInventoryDialog.show(getFragmentManager(), "EditIngredient");
    }

    public void SendNotifications(ArrayList<String> alerts) {
        String CHANNEL_ID = "CHANNEL_ID";
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getContext());
        notificationManager.createNotificationChannel(new NotificationChannel(CHANNEL_ID, "SmartPantry", NotificationManager.IMPORTANCE_DEFAULT));
        Intent intent = new Intent(getContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), (int) System.currentTimeMillis(), intent, 0);
        for (int i = 0; i < alerts.size(); i++) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                    .setContentTitle("Food Inventory")
                    .setContentIntent(pendingIntent)
                    .setContentText(alerts.get(i))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
            notificationManager.notify(1, builder.build());
        }
    }
}