package com.example.rachel.createatask;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //Define button variables
    Button logout_button, bCreateButton, bEditButton, bSearchLib, bDashboard;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // This displays the home screen for the app

        //Remember to call buttons!!!
        logout_button = (Button) findViewById(R.id.logout_button);
        bCreateButton = (Button) findViewById(R.id.create_task);
        bEditButton = (Button) findViewById(R.id.edit_task);
        bSearchLib = (Button) findViewById(R.id.search_library);
        bDashboard = (Button) findViewById(R.id.review_dash);

        //Remember to set onclick for all buttons!
        logout_button.setOnClickListener(this);
        bCreateButton.setOnClickListener(this);
        bSearchLib.setOnClickListener(this);
        bDashboard.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logout_button:
                //What happens when logout button is clicked

                //Clear user data
//                userStorage.clearUserData();
//                userStorage.setUserLogin(false);

                //Return to Login screen
                startActivity(new Intent(this, Login.class));
                break;


            case R.id.create_task:
                startActivity(new Intent(this, CreateTask.class));
                break;

            case R.id.review_dash:
                startActivity(new Intent(this, Dashboard.class));
                break;

            case R.id.search_library:
                System.out.println("Inside search library button on home screen");
                startActivity(new Intent(this, SearchableActivity.class));
                break;

        }
    }


//    //Button to Create Task Activity
//    public void sendMessage(View view) {
//        Intent startNewActivity = new Intent(this, HomeMenu.class);
//        startActivity(startNewActivity);
//    }
//
//    //Button to Edit Task Activity
//
//    public void sendMessage(View view) {
//        Intent startNewActivity = new Intent(this, HomeMenu.class);
//        startActivity(startNewActivity);
//    }
//
//    // Button to Search Prompt Library Activity
//
//    public void sendMessage(View view) {
//        Intent startNewActivity = new Intent(this, HomeMenu.class);
//        startActivity(startNewActivity);
//    }
//
//    //Button to Review Dashboard Activity
//
//    public void sendMessage(View view) {
//        Intent startNewActivity = new Intent(this, HomeMenu.class);
//        startActivity(startNewActivity);
//    }


}