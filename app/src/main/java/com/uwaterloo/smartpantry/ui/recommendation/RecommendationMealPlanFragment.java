package com.uwaterloo.smartpantry.ui.recommendation;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.error.VolleyError;
import com.uwaterloo.smartpantry.R;
import com.uwaterloo.smartpantry.adapter.MealPlanItemAdapter;
import com.uwaterloo.smartpantry.data.UserData;
import com.uwaterloo.smartpantry.database.DatabaseManager;
import com.uwaterloo.smartpantry.datalink.DataLink;
import com.uwaterloo.smartpantry.datalink.DataLinkREST;
import com.uwaterloo.smartpantry.datalink.VolleyResponseListener;
import com.uwaterloo.smartpantry.inventory.MealPlanItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.uwaterloo.smartpantry.database.DatabaseManager.getSharedInstance;

public class RecommendationMealPlanFragment extends Fragment implements VolleyResponseListener {
    private UserData userData = new UserData();
    private DatabaseManager dbManager = getSharedInstance();
    private List<MealPlanItem> mealPlanItemList = new ArrayList<MealPlanItem>();
    private MealPlanItemAdapter adapter = new MealPlanItemAdapter();


    public RecommendationMealPlanFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recommendation_meal_plan, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DataLink.getInstance().initDataLink(this.getContext());
        dbManager.initCouchbaseLite(getContext());
        dbManager.openOrCreateDatabaseForUser(getContext(), DatabaseManager.currentUser);
        userData.loadUser();

        RecyclerView recyclerView = view.findViewById(R.id.meal_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        adapter.setItems(mealPlanItemList);
        recyclerView.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                MealPlanItem item = mealPlanItemList.remove(viewHolder.getAdapterPosition());
                DataLinkREST.RemoveMeal(item.getId(), userData.getUserInfo(), RecommendationMealPlanFragment.this);

                adapter.setItems(mealPlanItemList);
                adapter.notifyDataSetChanged();
            }
        }).attachToRecyclerView(recyclerView);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            loadMealPlan();
        }
    }

    public void loadMealPlan() {
        LocalDate localDate = LocalDate.now();
        DataLinkREST.GetMealPlan(localDate.toString(), userData.getUserInfo(), this);
    }

    @Override
    public void onSuccess(JSONArray response, String type) {

    }

    @Override
    public void onSuccess(JSONObject response, String type) {
        switch(type){
            case "GetMealPlan":
                List<MealPlanItem> mPlan = MealPlanItem.parseItems(response);
                this.mealPlanItemList = mPlan;

                adapter.setItems(this.mealPlanItemList);
                adapter.notifyDataSetChanged();
                break;
            case "MealPlanItemDelete":
                Toast.makeText(getActivity(), "Item removed from plan", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onFailure(VolleyError error) {
        Log.v("Error", error.getMessage());
    }
}
