package com.example.ead_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    Button register  ;
    Button login  ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login1);
        Objects.requireNonNull(this.getSupportActionBar()).hide();

        register = findViewById(R.id.signup);
        register.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(LoginActivity.this,RegisterActivity.class);
                        startActivity(i);
                    }
                }
        );

        login = findViewById(R.id.login);
        login.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(LoginActivity.this,MainActivity2.class);
                        startActivity(i);
                    }
                }
        );
    }
}