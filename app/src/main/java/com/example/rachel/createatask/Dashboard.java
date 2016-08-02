package com.example.rachel.createatask;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

import java.util.ArrayList;

import io.karim.MaterialTabs;


public class Dashboard extends AppCompatActivity{

    //For Bottom Bar navigation
    private BottomBar mBottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

//        //Bottom Bar navigation
//        mBottomBar = BottomBar.attach(this, savedInstanceState);
//        mBottomBar.useFixedMode(); //Shows all titles with more than 3 buttons
//        mBottomBar.setItems(R.menu.bottombar_menu);
//        mBottomBar.setOnMenuTabClickListener(new OnMenuTabClickListener() {
//            @Override
//            public void onMenuTabSelected(@IdRes int menuItemId) {
//                if (menuItemId == R.id.bottomBarItemHome) {
//                    // The user selected item number one.
//                    startActivity(new Intent(Dashboard.this, MainActivity.class));
//                } else if (menuItemId == R.id.bottomBarItemTwo) {
//                    startActivity(new Intent(Dashboard.this, SearchLibrary.class));
//                } else if (menuItemId == R.id.bottomBarItemThree) {
//                    startActivity(new Intent(Dashboard.this, Dashboard.class));
//                }
//            }
//
//            @Override
//            public void onMenuTabReSelected(@IdRes int menuItemId) {
//                if (menuItemId == R.id.bottomBarItemTwo) {
//                    // The user reselected item number one, scroll your content to top.
//                }
//            }
//        });
//
//    }
//
//    //Bottom bar navigation
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//
//        // Necessary to restore the BottomBar's state, otherwise we would
//        // lose the current tab on orientation change.
//        mBottomBar.onSaveInstanceState(outState);
//    }
    }
}
