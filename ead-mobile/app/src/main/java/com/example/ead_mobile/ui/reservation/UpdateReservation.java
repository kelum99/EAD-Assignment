package com.example.ead_mobile.ui.reservation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.ead_mobile.MainActivity2;
import com.example.ead_mobile.R;
import com.example.ead_mobile.databinding.ActivityUpdateReservationBinding;
import com.example.ead_mobile.ui.dashboard.DashboardFragment;

public class UpdateReservation extends AppCompatActivity {
    private ActivityUpdateReservationBinding binding;
    private Spinner routeSpinner;
    private Spinner trainSpinner;
    private Spinner startSpinner;
    private Spinner destinationSpinner;
    private Button update;
    private Button cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_reservation);

        binding = ActivityUpdateReservationBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        routeSpinner = binding.etRoute;
        trainSpinner = binding.etTrain;
        startSpinner = binding.etStart;
        destinationSpinner = binding.etDestination;

        ArrayAdapter<CharSequence> routeAdapter = ArrayAdapter.createFromResource(
                UpdateReservation.this,
                R.array.route_array,
                android.R.layout.simple_spinner_item
        );
        routeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        routeSpinner.setAdapter(routeAdapter);

        ArrayAdapter<CharSequence> trainAdapter = ArrayAdapter.createFromResource(
                UpdateReservation.this,
                R.array.train_array,
                android.R.layout.simple_spinner_item
        );
        trainAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        trainSpinner.setAdapter(trainAdapter);

        ArrayAdapter<CharSequence> startAdapter = ArrayAdapter.createFromResource(
                UpdateReservation.this,
                R.array.station_array,
                android.R.layout.simple_spinner_item
        );
        startAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        startSpinner.setAdapter(startAdapter);

        ArrayAdapter<CharSequence> destAdapter = ArrayAdapter.createFromResource(
                UpdateReservation.this,
                R.array.station_array,
                android.R.layout.simple_spinner_item
        );
        destAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        destinationSpinner.setAdapter(destAdapter);

        update = findViewById(R.id.btn_update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(UpdateReservation.this, ReservationSummary.class);
                startActivity(i);
            }
        });

        cancel = findViewById(R.id.btn_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(UpdateReservation.this, MainActivity2.class);
                startActivity(i);
            }
        });
    }
}