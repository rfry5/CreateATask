package com.example.rachel.createatask;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnMenuTabClickListener;
import com.roughike.bottombar.OnTabSelectedListener;

public class EditTask extends AppCompatActivity {

    private BottomBar mBottomBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_task);


        mBottomBar = BottomBar.attach(this, savedInstanceState);
        mBottomBar.useFixedMode();
        mBottomBar.setItems(R.menu.bottombar_menu);
        mBottomBar.setOnMenuTabClickListener(new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                if (menuItemId == R.id.bottomBarItemTwo) {
                    // The user selected item number one.
                }
            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {
                if (menuItemId == R.id.bottomBarItemTwo) {
                    // The user reselected item number one, scroll your content to top.
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Necessary to restore the BottomBar's state, otherwise we would
        // lose the current tab on orientation change.
        mBottomBar.onSaveInstanceState(outState);

    }

}

//    public void sendMessage(View v) {
//
//        if (v.getId() == R.id.create_task) {
//            Intent intent = new Intent(this, CreateTask.class);
//            startActivity(intent);
//
//        } else if (v.getId() == R.id.edit_task) {
//            Intent intent = new Intent(this, CreateTask.class);
//            startActivity(intent);
//
//        } else if (v.getId() == R.id.home_button) {
//            Intent intent = new Intent(this, CreateTask.class);
//            startActivity(intent);
//
//        } else if (v.getId() == R.id.dashboard_button) {
//            Intent intent = new Intent(this, CreateTask.class);
//            startActivity(intent);
//
//        }
//    }

