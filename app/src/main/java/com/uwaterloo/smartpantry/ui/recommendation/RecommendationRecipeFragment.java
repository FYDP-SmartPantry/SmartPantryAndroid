package com.uwaterloo.smartpantry.ui.recommendation;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.error.VolleyError;
import com.uwaterloo.smartpantry.R;
import com.uwaterloo.smartpantry.data.UserData;
import com.uwaterloo.smartpantry.data.UserInfo;
import com.uwaterloo.smartpantry.data.model.Recommendation;
import com.uwaterloo.smartpantry.database.DatabaseManager;
import com.uwaterloo.smartpantry.datalink.DataLink;
import com.uwaterloo.smartpantry.datalink.DataLinkREST;
import com.uwaterloo.smartpantry.datalink.VolleyResponseListener;
import com.uwaterloo.smartpantry.inventory.FoodInventory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.uwaterloo.smartpantry.database.DatabaseManager.getSharedInstance;

public class RecommendationRecipeFragment extends Fragment implements VolleyResponseListener, RecipeFilterDialog.RecipeDialogListener {
    private RecommendationViewModel mViewModel;
    private Recommendation recommendation;
    private ArrayAdapter<String> arrayAdapter;

    private FoodInventory foodInventory = new FoodInventory();
    private DatabaseManager dbManager = getSharedInstance();

    private ArrayList<String> filteredIngredients = new ArrayList<String>();
    private ArrayList<String> filteredType = new ArrayList<String>();
    private ArrayList<String> filteredCuisine = new ArrayList<String>();
    private ArrayList<String> filteredDiet = new ArrayList<String>();

    private ArrayList<String> recipeTitles = new ArrayList<String>();
    private UserData userData = new UserData();

    private ListView listView;

    public RecommendationRecipeFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recommendationrecipe, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DataLink.getInstance().initDataLink(this.getContext());
        dbManager.initCouchbaseLite(getContext());
        dbManager.openOrCreateDatabaseForUser(getContext(), DatabaseManager.currentUser);
        foodInventory.loadInventory();
        Set<String> ingredients = foodInventory.getKeySet();

//        userData.loadUser();
//        if (!userData.getUserInfo().isSet()) {
//            initMealPlan();
//        }
        userData.loadUser();


        mViewModel = new ViewModelProvider(getActivity()).get(RecommendationViewModel.class);
        Recommendation rec = mViewModel.getRecommendation();

        Button filterButton = view.findViewById(R.id.filter_button);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecipeFilterDialog recipeFilterDialog = new RecipeFilterDialog(ingredients);
                recipeFilterDialog.setTargetFragment(RecommendationRecipeFragment.this, 1);
                recipeFilterDialog.show(getFragmentManager(), "FilterRecipe");
            }
        });

        arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_multiple_choice, recipeTitles);

        listView = view.findViewById(R.id.recommended_list);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemLongClickListener(this::onItemLongClick);

        Button addButton = view.findViewById(R.id.add_recipe_btn);
        addButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                System.out.println("Clicked Add");
                SparseBooleanArray positions = listView.getCheckedItemPositions();
                List<Pair<Integer, String>> recipes = recommendation.getRecipes();
                List<Pair<Integer, String>> checkedRecipes = new ArrayList<Pair<Integer, String>>();
                for (int i = 0; i < listView.getCount(); i++) {
                    if (positions.get(i)) {
                        checkedRecipes.add(recipes.get(i));
                    }
                }
                long time = System.currentTimeMillis();
                time = time / 1000;
                DataLinkREST.AddMeal(checkedRecipes, Long.toString(time), userData.getUserInfo(), RecommendationRecipeFragment.this);
            }
        });

        if (rec != null) {
            System.out.println("Importing View Model");
            recommendation = rec;
            arrayAdapter.clear();
            arrayAdapter.addAll(recommendation.getRecipeNames());
            arrayAdapter.notifyDataSetChanged();
        } else {
            System.out.println("New Recommendation");
            recommendation = new Recommendation();
            initLoad();
        }
    }


    public void initLoad() {
        DataLinkREST.GetRandomRecipes(this);
    }

    public void initMealPlan() {
//        DataLinkREST.SetupMealPlan(DatabaseManager.currentUser, this);
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername("api-74219-ericw-p12gmail-com");
        userInfo.setHash("497fd1c6ddb064f51028dd498365538f6a32e2a7");
        userData.setUserInfo(userInfo);
        userData.saveUser();

    }

    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        DataLinkREST.GetRecipeInstructions(recommendation.getRecipeId(i), this);
        return true;
    }

    @Override
    public void onSuccess(JSONArray response, String type) {
        switch(type) {
            default:
                break;
        }
    }

    @Override
    public void onSuccess(JSONObject response, String type) {
        switch(type) {
            case "Recommendation":
                System.out.println();
                recommendation.parseRecipes(response);
                mViewModel.setRecommendation(recommendation);
                arrayAdapter.clear();
                arrayAdapter.addAll(recommendation.getRecipeNames());
                arrayAdapter.notifyDataSetChanged();
                break;
            case "RandomRecommendation":
                recommendation.parseRandom(response);
                mViewModel.setRecommendation(recommendation);
                arrayAdapter.clear();
                arrayAdapter.addAll(recommendation.getRecipeNames());
                arrayAdapter.notifyDataSetChanged();
                break;
            case "RecipeInformation":
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(recommendation.getSourceUrl(response))));
                break;
            case "MealPlanRegister":
                UserInfo userInfo = new UserInfo();
                try {
                    userInfo.setUsername(response.getString("username"));
                    userInfo.setHash(response.getString("hash"));
                    userData.setUserInfo(userInfo);
                    userData.saveUser();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            case "MealPlanAdd":
                System.out.println("response");
                listView.clearChoices();
                listView.requestLayout();
                Toast.makeText(getContext(), "Meals registered to meal plan", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onFailure(VolleyError error) {
        Log.d("Recommendation", error.toString());
    }

    @Override
    public void onFilterDialogClick(ArrayList<String> filteredIngredients, ArrayList<String> filteredType, ArrayList<String> filteredCuisine, ArrayList<String> filteredDiet) {
        this.filteredIngredients = filteredIngredients;
        this.filteredType = filteredType;
        this.filteredCuisine = filteredCuisine;
        this.filteredDiet = filteredDiet;
        listView.clearChoices();
        listView.requestLayout();
        DataLinkREST.SearchRecipes(filteredIngredients, filteredType, filteredCuisine, filteredDiet, this);
    }
}

