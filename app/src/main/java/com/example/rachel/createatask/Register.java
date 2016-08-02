package com.example.rachel.createatask;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.BundleCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import static com.example.rachel.createatask.R.id.register_button;


public class Register extends AppCompatActivity implements View.OnClickListener{

    Button bRegisterButton, bCheckDatabase;
    EditText etUsername, etPassword;

    DatabaseHelp helper = new DatabaseHelp(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_screen);

        etUsername = (EditText) findViewById(R.id.insert_username);
        etPassword = (EditText) findViewById(R.id.insert_password);
        bRegisterButton = (Button) findViewById(R.id.register_button);
        bCheckDatabase = (Button) findViewById(R.id.check_database);

        bRegisterButton.setOnClickListener(this);
        bCheckDatabase.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_button:

                EditText username = (EditText) findViewById(R.id.register_username);
                EditText password = (EditText) findViewById(R.id.register_password);

                String usernamestring = username.getText().toString();
                String passwordstring = password.getText().toString();

                //Passing through database
                User c = new User();
                c.setUsername(usernamestring);
                c.setPassword(passwordstring);

                helper.insertContact(c);

                break;

            case R.id.check_database:

                Intent dbmanager = new Intent(this, AndroidDatabaseManager.class);
                startActivity(dbmanager);

                //Uncomment to clear username records (Also in DatabaseHelp.class)
//            case R.id.clear_usernames:
//
//                helper.deleteAll();

        }
    }


//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.register_button:
//
//                String username = etUsername.getText().toString();
//                String password = etPassword.getText().toString();
//
//                User userData = new User(username, password);
//
//                break;
//
//        }
//    }
}
