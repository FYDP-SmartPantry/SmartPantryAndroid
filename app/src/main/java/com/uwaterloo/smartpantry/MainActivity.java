package com.uwaterloo.smartpantry;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.uwaterloo.smartpantry.ui.addshoppingitem.AddItemToShoppinglistFragment;
import com.uwaterloo.smartpantry.ui.foodcamera.FoodcameraFragment;
import com.uwaterloo.smartpantry.ui.foodinventory.FoodinventoryFragment;
import com.uwaterloo.smartpantry.ui.foodstatus.FoodstatusFragment;
import com.uwaterloo.smartpantry.ui.myprofile.MyprofileFragment;
import com.uwaterloo.smartpantry.ui.recommendation.RecommendationFragment;
import com.uwaterloo.smartpantry.ui.shoppinglist.ShoppinglistFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import android.view.MenuItem;
import androidx.annotation.NonNull;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        openFragment(FoodcameraFragment.newInstance("", ""));
        /*registerButton.setOnClickListener(new View.OnClickListener() {
            // OnClick -> the login_register activity
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginRegActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(LoginRegActivity.this, LoginActivity.class);
                startActivity(loginIntent);
            }
        });*/
    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.navigation_foodcamera:
                            openFragment(FoodcameraFragment.newInstance("", ""));
                            return true;
                        case R.id.navigation_foodinventory:
                            openFragment(FoodinventoryFragment.newInstance("", ""));
                            return true;
                        case R.id.navigation_foodstatus:
                            openFragment(FoodstatusFragment.newInstance("", ""));
                            return true;
                        case R.id.navigation_shoppinglist:
                            openFragment(ShoppinglistFragment.newInstance("", ""));
                            return true;
                        case R.id.navigation_myprofile:
                            openFragment(MyprofileFragment.newInstance("", ""));
                            return true;
                    }
                    return false;
                }
            };

}