package com.example.rachel.createatask;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by Rachel on 6/16/16.
 */

public class HomeMenu extends AppCompatActivity {
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_menu);
    }

    public void sendMessage(View v) {

        if (v.getId() == R.id.create_task) {
            Intent intent = new Intent(this, CreateTask.class);
            startActivity(intent);

//        } else if (v.getId() == R.id.edit_task) {
//            Intent intent = new Intent(this, CreateTask.class);
//            startActivity(intent);

        } else if (v.getId() == R.id.home_button) {
            Intent intent = new Intent(this, CreateTask.class);
            startActivity(intent);

        } else if (v.getId() == R.id.dashboard_button) {
            Intent intent = new Intent(this, CreateTask.class);
            startActivity(intent);

        }
    }
}


//    @Override
//    public boolean onOptionsItemSelected(MenuItem item){
//        //Tell it what to do
//        int id= item.getItemId();
//
//        if (id == R.id.home_button){
//            Intent startNewActivity = new Intent(this, CreateTask.class);
//            startActivity(startNewActivity);
//
//        }
//
//        if (id == R.id.create_task_button){
//            Intent startNewActivity = new Intent(this, CreateTask.class);
//            startActivity(startNewActivity);
//
//        }
//
//        if (id == R.id.edit_task_button){
//            Intent startNewActivity = new Intent(this, CreateTask.class);
//            startActivity(startNewActivity);
//
//        }
//
//        if (id == R.id.dashboard_button){
//            Intent startNewActivity = new Intent(this, CreateTask.class);
//            startActivity(startNewActivity);
//
//        }
//
//        return true;
//    }




