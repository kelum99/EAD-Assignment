package com.example.ead_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ead_mobile.services.AuthService;
import com.example.ead_mobile.services.ContextService;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    Button register  ;
    Button login  ;
    EditText nic;
    EditText password;

    AuthService authService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login1);
        ContextService.getInstance().setApplicationContext(getApplicationContext());
        Objects.requireNonNull(this.getSupportActionBar()).hide();

        authService = AuthService.getInstance();

        if(authService.getLoggingStatus()){
            handleLoginSuccess();
        }

        this.nic = findViewById(R.id.nic);
        this.password = findViewById(R.id.password);
        this.register = findViewById(R.id.signup);
        this.login = findViewById(R.id.login);

        this.register.setOnClickListener(view -> navigateToRegister());

        this.login.setOnClickListener(view -> onLogin());
    }

    private void onLogin(){
        if(!nic.getText().toString().isEmpty() && !password.getText().toString().isEmpty()) {
            authService.login(
                    nic.getText().toString(),
                    password.getText().toString(),
                    () -> handleLoginSuccess(),
                    error -> handleLoginFailed(error));
        } else{
            Toast.makeText(this, "NIC and Password Required!", Toast.LENGTH_LONG).show();
        }
    }

    //on success login
    private void handleLoginSuccess(){
        Toast.makeText(this, "Login Success!", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, MainActivity2.class);
        startActivity(intent);
        finish();
    }


    //on failed login
    private void handleLoginFailed(String error){
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }

    //navigation to register screen
    private void navigateToRegister() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}