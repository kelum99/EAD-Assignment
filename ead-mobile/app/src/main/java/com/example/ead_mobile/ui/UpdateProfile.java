package com.example.ead_mobile.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ead_mobile.R;
import com.example.ead_mobile.models.UserEntity;
import com.example.ead_mobile.services.DatabaseService;
import com.example.ead_mobile.services.UserService;

public class UpdateProfile extends AppCompatActivity {
    Button cancel_btn;
    Button update_btn;
    String userId;
    EditText nic;
    EditText name;
    EditText email;
    EditText mobile;
    DatabaseService databaseService;
    UserService userService;
    UserEntity userEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        this.getSupportActionBar().hide();

        databaseService = DatabaseService.getInstance();
        userService = UserService.getInstance();
        this.cancel_btn = findViewById(R.id.cancel_btn);
        this.update_btn = findViewById(R.id.update_btn);
        this.nic = findViewById(R.id.update_nic);
        this.name = findViewById(R.id.update_name);
        this.email = findViewById(R.id.update_email);
        this.mobile = findViewById(R.id.update_mobile);


        cancel_btn.setOnClickListener(view -> navigateBack());
        update_btn.setOnClickListener(view -> update());

        //Load locally saved user details form local DB
        new Thread(() -> {
            userEntity = databaseService.db().iLocalService().getAll().get(0);

            this.userId = userEntity.id;
            this.nic.setText(userEntity.nic);
            this.name.setText(userEntity.name);
            this.mobile.setText(userEntity.mobile);
            this.email.setText(userEntity.email);
        }).start();
    }

    //User account update function
    public void update() {
        String nic = this.nic.getText().toString();
        String name = this.name.getText().toString();
        String email = this.email.getText().toString();
        String mobile = this.mobile.getText().toString();

        if (!this.userId.isEmpty() && !nic.isEmpty() && !name.isEmpty() && !email.isEmpty() && !mobile.isEmpty()) {
            userEntity.nic = nic;
            userEntity.name = name;
            userEntity.email = email;
            userEntity.mobile = mobile;

            new Thread(() -> {
                databaseService.db().iLocalService().update(userEntity);
            }).start();

            userService.updateProfile(
                    this.userId,
                    nic,
                    name,
                    email,
                    mobile,
                    () -> handleSuccess(),
                    error -> handleFailed(error)
            );
        } else {
            Toast.makeText(this, "All Fields are required!", Toast.LENGTH_LONG).show();
        }
    }

    //Handle success output from update function
    private void handleSuccess() {
        Toast.makeText(this, "Profile Update Success!", Toast.LENGTH_LONG).show();
        super.onBackPressed();
    }

    //Handle failed output from deactivate function
    private void handleFailed(String error) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }

    //Navigation to previous screen
    public void navigateBack() {
        super.onBackPressed();
    }

}