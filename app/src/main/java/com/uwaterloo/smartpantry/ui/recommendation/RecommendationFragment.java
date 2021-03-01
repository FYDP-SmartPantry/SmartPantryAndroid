package com.uwaterloo.smartpantry.ui.recommendation;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.uwaterloo.smartpantry.R;
import com.uwaterloo.smartpantry.data.model.Recommendation;
import com.uwaterloo.smartpantry.database.DatabaseManager;
import com.uwaterloo.smartpantry.datalink.DataLink;
import com.uwaterloo.smartpantry.datalink.DataLinkREST;
import com.uwaterloo.smartpantry.datalink.VolleyResponseListener;
import com.uwaterloo.smartpantry.inventory.FoodInventory;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.uwaterloo.smartpantry.database.DatabaseManager.getSharedInstance;

public class RecommendationFragment extends Fragment implements VolleyResponseListener {

    private RecommendationViewModel mViewModel;
    private Recommendation recommendation;
    private ArrayAdapter<String> arrayAdapter;

    private FoodInventory foodInventory = new FoodInventory();
    private DatabaseManager dbManager = getSharedInstance();

    public static RecommendationFragment newInstance() {
        return new RecommendationFragment();
    }
    // TODO: Add swiperefreshlayout

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recommendation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DataLink.getInstance().initDataLink(this.getContext());

        mViewModel = new ViewModelProvider(getActivity()).get(RecommendationViewModel.class);
        Recommendation rec = mViewModel.getRecommendation();

        arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, new ArrayList<String>());
        ListView listView = view.findViewById(R.id.recommended_list);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(this::onItemClick);

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
        dbManager.initCouchbaseLite(getContext());
        // TODO: Update to actual username
        dbManager.openOrCreateDatabaseForUser(getContext(), "test");
        foodInventory.loadInventory();
        Set<String> foodNames = foodInventory.getKeySet();
        recommendation.clearRecipes();
        // TODO: Swap to ingredient limited version
        for(String foodName : foodNames) {
            DataLinkREST.GetRecipeRecommendation(foodName, this);
        }
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        DataLinkREST.GetRecipeInstructions(recommendation.getRecipeId(i), this);
    }

    @Override
    public void onSuccess(JSONArray response, String type) {
        switch(type) {
            case "Recommendation":
                System.out.println();
                recommendation.parseAndAddRecipes(response);
                mViewModel.setRecommendation(recommendation);
                arrayAdapter.clear();
                arrayAdapter.addAll(recommendation.getRecipeNames());
                arrayAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }

    @Override
    public void onSuccess(JSONObject response, String type) {
        switch(type) {
            case "RecipeInformation":
                WebView webView = new WebView(getContext());
                webView.loadUrl(recommendation.getSourceUrl(response));
                break;
            default:
                break;
        }
    }

    @Override
    public void onFailure(VolleyError error) {
        Log.d("Recommendation", error.toString());
    }
}