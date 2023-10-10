package com.example.ead_mobile.ui.reservation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.ead_mobile.MainActivity;
import com.example.ead_mobile.MainActivity2;
import com.example.ead_mobile.R;
import com.example.ead_mobile.ui.dashboard.DashboardFragment;
import com.example.ead_mobile.ui.home.HomeFragment;

public class ReservationSummary extends AppCompatActivity {

    private Button confirm;
    private Button cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_summary);

        confirm = findViewById(R.id.btn_confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ReservationSummary.this, MainActivity2.class);
                startActivity(i);
            }
        });

        cancel = findViewById(R.id.btn_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ReservationSummary.this, MainActivity2.class);
                startActivity(i);
            }
        });
    }
}