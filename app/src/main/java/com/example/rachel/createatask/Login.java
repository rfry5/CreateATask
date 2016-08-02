package com.example.rachel.createatask;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.rachel.createatask.R.id.insert_password;
import static com.example.rachel.createatask.R.id.insert_username;
import static com.example.rachel.createatask.R.id.login_button;
import static com.example.rachel.createatask.R.id.register_button;


public class Login extends AppCompatActivity implements View.OnClickListener {

    Button bLoginButton, bRegisterButton;
    EditText etInsertUsername, etInsertPassword;

    DatabaseHelp helper = new DatabaseHelp(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        etInsertUsername = (EditText) findViewById(insert_username);
        String str = etInsertUsername.getText().toString();
        etInsertPassword = (EditText) findViewById(insert_password);
        String pass = etInsertPassword.getText().toString();

        bLoginButton = (Button) findViewById(login_button);
        bRegisterButton = (Button) findViewById(R.id.register_button);

        bLoginButton.setOnClickListener(this);
        bRegisterButton.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_button:

                etInsertUsername = (EditText) findViewById(insert_username);
                String str = etInsertUsername.getText().toString();
                etInsertPassword = (EditText) findViewById(insert_password);
                String pass = etInsertPassword.getText().toString();

                String password = helper.searchPass(str);

                if(pass.equals(password))
                {
                    startActivity(new Intent(this, MainActivity.class));
                    break;
                }
                else
                {
                    Toast temp = Toast.makeText(Login.this, "Not a Valid Username and Password", Toast.LENGTH_SHORT);
                    temp.show();
                }
                break;

            case R.id.register_button:

                startActivity(new Intent(this, Register.class));
                break;


        }
    }


//    //Button to Home Menu Activity
//    public void sendMessage(View view) {
//        Intent startNewActivity = new Intent(this, HomeMenu.class);
//        startActivity(startNewActivity);
//    }

}


