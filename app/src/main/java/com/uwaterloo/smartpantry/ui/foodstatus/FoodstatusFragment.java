package com.uwaterloo.smartpantry.ui.foodstatus;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.volley.error.VolleyError;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.uwaterloo.smartpantry.R;

import com.uwaterloo.smartpantry.data.InventoryMaps;
import com.uwaterloo.smartpantry.data.Nutrition;
import com.uwaterloo.smartpantry.data.UserData;
import com.uwaterloo.smartpantry.data.Utility;
import com.uwaterloo.smartpantry.database.DatabaseManager;
import com.uwaterloo.smartpantry.datalink.DataLink;
import com.uwaterloo.smartpantry.datalink.DataLinkREST;
import com.uwaterloo.smartpantry.datalink.VolleyResponseListener;
import com.uwaterloo.smartpantry.inventory.Category;
import com.uwaterloo.smartpantry.inventory.Food;
import com.uwaterloo.smartpantry.inventory.WastedFood;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import okhttp3.internal.Util;

import static com.uwaterloo.smartpantry.database.DatabaseManager.getSharedInstance;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link com.uwaterloo.smartpantry.ui.foodstatus.FoodstatusFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FoodstatusFragment extends Fragment implements VolleyResponseListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    PieChart pie_chart;
    TextView pie_chartDescription;

    BarChart barChart;
    TextView barchartDescription;

    private UserData userData = new UserData();
    private DatabaseManager dbManager = getSharedInstance();

    public FoodstatusFragment() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FoodstatusFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static com.uwaterloo.smartpantry.ui.foodstatus.FoodstatusFragment newInstance(String param1, String param2) {
        com.uwaterloo.smartpantry.ui.foodstatus.FoodstatusFragment fragment = new com.uwaterloo.smartpantry.ui.foodstatus.FoodstatusFragment();
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
    public void onResume() {
        super.onResume();
        loadMealPlan();
        loadInventories();
    }

    public void loadMealPlan() {
        LocalDate localDate = LocalDate.now();
        DataLinkREST.GetMealPlan(localDate.toString(), userData.getUserInfo(), this);
    }

    public void loadInventories() {
        DataLinkREST.RetrieveInventories(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_foodstatus, container, false);
        DataLink.getInstance().initDataLink(this.getContext());
        dbManager.initCouchbaseLite(getContext());
        dbManager.openOrCreateDatabaseForUser(getContext(), DatabaseManager.currentUser);
        userData.loadUser();

        pie_chart = v.findViewById(R.id.pie_chart);

//        pie_chart.setData(addPieChartData());
        pie_chart.setBackgroundColor(Color.WHITE);
        pie_chart.setDrawSlicesUnderHole(false);
        pie_chart.setDrawHoleEnabled(true);
        pie_chart.setCenterText(generateCenterSpannableText());
        pie_chart.getDescription().setEnabled(false);
        pie_chart.animateXY(2000, 2000);

        pie_chartDescription = v.findViewById(R.id.pie_chart_description);
//        pie_chartDescription.setText(getpieChartDescription());

        barChart = v.findViewById(R.id.bar_chart);


//        BarDataSet barDataSet1 = new BarDataSet(populateLastCycleData(), "Last Cycle");
//        barDataSet1.setColor(Color.LTGRAY);
//
//        BarDataSet barDataSet2 = new BarDataSet(populateCurrentCycleData(), "Current Cycle");
//        barDataSet2.setColor(Color.GREEN);

//        BarData data = new BarData(barDataSet1, barDataSet2);
//        barChart.setData(data);


//        XAxis xAxis = barChart.getXAxis();
//        xAxis.setValueFormatter(new IndexAxisValueFormatter(populateLabels()));
//        xAxis.setCenterAxisLabels(true);
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setGranularity(1);
//        xAxis.setGranularityEnabled(true);
//
//        barChart.setDragEnabled(true);
//        barChart.setVisibleXRangeMaximum(6);
//        float barSpace = 0.1f;
//        float groupspace = 0.5f;
//        data.setBarWidth(0.15f);
//        barChart.getXAxis().setAxisMinimum(0);
//        //barChart.getXAxis().setAxisMaximum(0+barChart.getBarData().getGroupWidth(groupspace, barSpace) * 7);
//        barChart.groupBars(0, groupspace, barSpace);
//        barChart.invalidate();
//        barChart.getDescription().setEnabled(false);
//
//        barchartDescription = v.findViewById(R.id.bar_chart_description);
//        barchartDescription.setText("The highest wasted in current cycle is Fruit");
        return v;
    }

    private PieData addPieChartData(Nutrition nutrition) {
        ArrayList<PieEntry> myPieData = new ArrayList<>();
        float total = nutrition.getCarbohydrates() + nutrition.getFat() + nutrition.getProtein();
        float fatDist = nutrition.getFat() / total * 100;
        float carbDist = nutrition.getCarbohydrates() / total * 100;
        float protDist = nutrition.getProtein() / total * 100;

        myPieData.add(new PieEntry(fatDist, "Fat"));
        myPieData.add(new PieEntry(carbDist, "Carbs"));
        myPieData.add(new PieEntry(protDist, "Protein"));

        PieDataSet pieDataSet = new PieDataSet(myPieData, "Nutrition Data");
        pieDataSet.setSliceSpace(3f);
        pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        pieDataSet.setValueTextSize(25f);

        PieData pieData = new PieData(pieDataSet);
        return pieData;
    }

    private BarData addBarChartData() {
        ArrayList<BarEntry> myBarList1 = new ArrayList<>();
        ArrayList<BarEntry> myBarList2 = new ArrayList<>();
        ArrayList<BarEntry> myBarList3 = new ArrayList<>();
        ArrayList<BarEntry> myBarList4 = new ArrayList<>();
        ArrayList<BarEntry> myBarList5 = new ArrayList<>();

        myBarList1.add(new BarEntry(5, 5));
        myBarList1.add(new BarEntry(5, 10));
        myBarList1.add(new BarEntry(6, 11));
        myBarList2.add(new BarEntry(6,12));
        myBarList2.add(new BarEntry(7, 13));
        myBarList2.add(new BarEntry(7, 15));
        //myBarList3.add(new BarEntry(7, 5));


        BarDataSet barDataSet1 = new BarDataSet(myBarList1, "Last Cycle");
        BarDataSet barDataSet2 = new BarDataSet(myBarList2, "This Cycle");
        //BarDataSet barDataSet3 = new BarDataSet(myBarList3, "fruit");
        barDataSet1.setColor(Color.rgb(104, 241, 175));
        barDataSet2.setColor(Color.rgb(164, 228, 251));
        //barDataSet3.setColors(ColorTemplate.MATERIAL_COLORS);
        ArrayList<String> thelabs = new ArrayList<>();
        thelabs.add("asd");
        thelabs.add("fds");
        BarData barData = new BarData(barDataSet1, barDataSet2);
        return barData;
    }

    private SpannableString generateCenterSpannableText() {

        SpannableString s = new SpannableString("NutritionChart\n Under  Current Cycle");
        s.setSpan(new RelativeSizeSpan(1.4f), 0, 14, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 14, s.length() - 15, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 14, s.length() - 15, 0);
        s.setSpan(new RelativeSizeSpan(.8f), 14, s.length() - 15, 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 14, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 14, s.length(), 0);
        return s;
    }

    private String getpieChartDescription(Nutrition nutrition) {
        float total = nutrition.getCarbohydrates() + nutrition.getFat() + nutrition.getProtein();
        float fatDist = nutrition.getFat() / total * 100;
        float carbDist = nutrition.getCarbohydrates() / total * 100;
        float protDist = nutrition.getProtein() / total * 100;

        List<Float> distList = new ArrayList<Float>();
        distList.add(fatDist);
        distList.add(carbDist);
        distList.add(protDist);
        float max = Collections.max(distList);
        int index = distList.indexOf(max);
        String[] nutrients = new String[] {"fats", "carbohydrates", "proteins"};
        return String.format("Your highest nutrient consumption is %s, which is %f%% of your nutrition intake", nutrients[index], max);
    }

    private ArrayList<BarEntry> populateLastCycleData() {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(1, 0.32f));
        barEntries.add(new BarEntry(2, 0.79f));
        barEntries.add(new BarEntry(3, 0.63f));
        barEntries.add(new BarEntry(4, 0.78f));
        barEntries.add(new BarEntry(5, 0.27f));
        barEntries.add(new BarEntry(6, 0.5f));
        barEntries.add(new BarEntry(7, 0.21f));
        return barEntries;
    }

    private ArrayList<BarEntry> populateCurrentCycleData() {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(1, 0.9f));
        barEntries.add(new BarEntry(2, 0.69f));
        barEntries.add(new BarEntry(3, 0.1f));
        barEntries.add(new BarEntry(4, 0.38f));
        barEntries.add(new BarEntry(5, 0.27f));
        barEntries.add(new BarEntry(6, 0.5f));
        barEntries.add(new BarEntry(7, 0.11f));
        return barEntries;
    }

    private String[] populateLabels() {
        String[] categories = new String[] {"Meat", "Fruit", "Veges", "bread", "Fats", "Milk", "Cheese"};
        return  categories;
    }

    public void loadCurrentCycle(InventoryMaps inventoryMaps) {
        Map<String, Food> oldestFoodMap = inventoryMaps.getOldestFoodMap();
        Map<String, WastedFood> wastedFoodMap = inventoryMaps.getWastedFoodMap();
        ArrayList<BarEntry> barEntries = new ArrayList<>();

        float totalMeat = Utility.getFoodQuantityCategorized(Category.CategoryEnum.MEAT, oldestFoodMap);
        float wastedMeat = Utility.getWastedFoodQuantityCategorized(Category.CategoryEnum.MEAT, wastedFoodMap);
        float wastedMeatPercent = wastedMeat / totalMeat * 100;
        barEntries.add(new BarEntry(0, wastedMeatPercent));

        float totalFruit = Utility.getFoodQuantityCategorized(Category.CategoryEnum.FRUIT, oldestFoodMap);
        float wastedFruit = Utility.getWastedFoodQuantityCategorized(Category.CategoryEnum.FRUIT, wastedFoodMap);
        float wastedFruitPercent = wastedFruit / totalFruit * 100;
        barEntries.add(new BarEntry(1, wastedFruitPercent));

        float totalVegetable = Utility.getFoodQuantityCategorized(Category.CategoryEnum.VEGETABLE, oldestFoodMap);
        float wastedVegetable = Utility.getWastedFoodQuantityCategorized(Category.CategoryEnum.VEGETABLE, wastedFoodMap);
        float wastedVegetablePercent = wastedVegetable / totalVegetable * 100;
        barEntries.add(new BarEntry(2, wastedVegetablePercent));

        Map<String, Food> otherFoodMap = Utility.getCategorized(Category.CategoryEnum.OTHER, oldestFoodMap);
        Map<String, WastedFood> otherWastedFoodMap = Utility.getWastedCategorized(Category.CategoryEnum.OTHER, wastedFoodMap);

        ArrayList<String> labels = new ArrayList<String>();
        labels.add("Meat");
        labels.add("Fruit");
        labels.add("Vegetables");

        otherWastedFoodMap.keySet().forEach((key) -> {
            WastedFood wastedFood = otherWastedFoodMap.get(key);
            labels.add(wastedFood.getName());
        });

        for (int i = 3; i < labels.size(); i++) {
            float totalQuantity = otherFoodMap.get(labels.get(i)).getNumber().floatValue();
            float wastedQuantity = otherWastedFoodMap.get(labels.get(i)).getNumber().floatValue();
            float wastedPercent = wastedQuantity / totalQuantity * 100;
            barEntries.add(new BarEntry(i, wastedPercent));
        }

        BarDataSet barDataSet = new BarDataSet(barEntries, "Current Cycle");
        barDataSet.setColor(Color.GREEN);

        BarData data = new BarData(barDataSet);
        barChart.setData(data);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels.toArray(new String[labels.size()])));
        xAxis.setCenterAxisLabels(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1);
        xAxis.setGranularityEnabled(true);

        barChart.setDragEnabled(true);
        barChart.setVisibleXRangeMaximum(6);

        data.setBarWidth(0.15f);
        barChart.getXAxis().setAxisMinimum(0);

        barChart.invalidate();
        barChart.getDescription().setEnabled(false);

        barchartDescription = getView().findViewById(R.id.bar_chart_description);
        barchartDescription.setText("");

    }

    @Override
    public void onSuccess(JSONArray response, String type) {

    }

    @Override
    public void onSuccess(JSONObject response, String type) {
        switch(type){
            case "GetMealPlan":
                Nutrition nutrition = Nutrition.parseNutrients(response);
                pie_chart.setData(addPieChartData(nutrition));
                pie_chartDescription.setText(getpieChartDescription(nutrition));
                break;
            case "InventoryRetrieve":
                InventoryMaps inventoryMaps = Utility.parseInventories(response);
                loadCurrentCycle(inventoryMaps);
                break;
            default:
                break;
        }
    }

    @Override
    public void onFailure(VolleyError error) {

    }
}