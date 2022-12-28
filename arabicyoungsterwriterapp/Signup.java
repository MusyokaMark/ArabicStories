package com.example.arabicyoungsterwriterapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

public class Signup extends AppCompatActivity {

    MaterialButton createaccount;
    Button login;
    TextView username;
    TextView email;
    TextView password;
    TextView confirmpassword;
    TextView condition;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        final TextView username = (TextView) findViewById(R.id.username);
        final TextView email = (TextView) findViewById(R.id.email);
        final TextView password = (TextView) findViewById(R.id.password);
        final TextView confirmpassword = (TextView) findViewById(R.id.confirmpassword);
        final TextView condition = (TextView) findViewById(R.id.condition);
        final MaterialButton createaccount = (MaterialButton) findViewById(R.id.createaccount);
        progressDialog = new ProgressDialog(this);
        final Button login =  (Button) findViewById(R.id.login);

        login.setOnClickListener(v -> {
            Intent i = new Intent(Signup.this,Login.class);
            startActivity(i);
            //finish();
        });
        createaccount.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Signup.this,HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }
}