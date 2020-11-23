package com.learntodroid.androidqrcodescanner;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.learntodroid.androidqrcodescanner.model.Model;

public class LoginActivity extends AppCompatActivity {
    EditText emailFiled ;
    EditText passwordFiled ;
    Button signIn_btn;
    ProgressDialog LoadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailFiled  = findViewById(R.id.email);
        passwordFiled = findViewById(R.id.password);
        signIn_btn = findViewById(R.id.signIn_btn);

        LoadingBar = new ProgressDialog(this);

        signIn_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailFiled.getText().toString();
                String password = passwordFiled.getText().toString();
                // Toast.makeText(MainActivity.this,"Email: "+ email+ "password: "+password, Toast.LENGTH_LONG).show();
                Model model = Model.getInstance(LoginActivity.this.getApplication());
                model.login(email,password);
            }
        });
    }
}