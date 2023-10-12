package com.example.ead_mobile.ui.reservation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ead_mobile.MainActivity2;
import com.example.ead_mobile.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UpdateReservation extends AppCompatActivity {
    private TextView nicEditText;
    private TextView bookingDateEditText;
    private EditText reserveDateEditText;
    private EditText noOfTicketsEditText;
    private EditText routeEditText;
    private EditText trainEditText;
    private EditText startEditText;
    private EditText destEditText;
    private TextView timeEditText;
    private Button update;
    private Button cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_reservation);

        nicEditText = findViewById(R.id.et_nic);
        bookingDateEditText = findViewById(R.id.et_bookingDate);
        reserveDateEditText = findViewById(R.id.et_reserveDate);
        noOfTicketsEditText = findViewById(R.id.et_tickets);
        routeEditText = findViewById(R.id.et_route);
        trainEditText = findViewById(R.id.et_train);
        startEditText = findViewById(R.id.et_start);
        destEditText = findViewById(R.id.et_destination);
        timeEditText = findViewById(R.id.et_time);
        update = findViewById(R.id.btn_update);
        cancel = findViewById(R.id.btn_cancel);

        // Retrieve data from the intent
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String userNIC = intent.getStringExtra("userNIC");
        String bookingDate = intent.getStringExtra("bookingDate");
        String reservationDate = intent.getStringExtra("reservationDate");
        String noOfTickets = intent.getStringExtra("noOfTickets");
        String route = intent.getStringExtra("route");
        String train = intent.getStringExtra("train");
        String startingPoint = intent.getStringExtra("startingPoint");
        String destination = intent.getStringExtra("destination");
        String time = intent.getStringExtra("time");
        String agentID = intent.getStringExtra("agentID");

        nicEditText.setText(userNIC);
        bookingDateEditText.setText(bookingDate.subSequence(0, 10));
        reserveDateEditText.setText(reservationDate.subSequence(0, 10));
        noOfTicketsEditText.setText(noOfTickets);
        routeEditText.setText(route);
        trainEditText.setText(train);
        startEditText.setText(startingPoint);
        destEditText.setText(destination);
        timeEditText.setText(time);

        // Check the time gap between the current date and reservation date
        if (isReservationDateValid(reservationDate)) {
            // Disable all editable fields and the update button
            disableEditableFields();
        }

        // Update button click listener
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String reservationDateEdit = reserveDateEditText.getText().toString();
                String noOfTicketsEdit = noOfTicketsEditText.getText().toString();
                String routeEdit = routeEditText.getText().toString();
                String trainEdit = trainEditText.getText().toString();
                String startEdit = startEditText.getText().toString();
                String destEdit = destEditText.getText().toString();

                //Navigate to reservation summary with edit action
                Intent intent = new Intent(UpdateReservation.this, ReservationSummary.class);
                intent.putExtra("id", id);
                intent.putExtra("userNIC", userNIC);
                intent.putExtra("bookingDate", bookingDate);
                intent.putExtra("reservationDate", reservationDateEdit);
                intent.putExtra("noOfTickets", noOfTicketsEdit);
                intent.putExtra("route", routeEdit);
                intent.putExtra("train", trainEdit);
                intent.putExtra("startingPoint", startEdit);
                intent.putExtra("destination", destEdit);
                intent.putExtra("time", time);
                intent.putExtra("agentID", agentID);
                intent.putExtra("action", "Edit");
                startActivityForResult(intent, 201);
            }
        });

        // Cancel button click listener
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(UpdateReservation.this, MainActivity2.class);
                startActivity(i);
            }
        });
    }

    //Method to check whether reservation is valid for update and delete
    private boolean isReservationDateValid(String reservationDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date currentDate = new Date();
            Date parsedReservationDate = dateFormat.parse(reservationDate);

            // Calculate the time gap in days
            long timeGapInDays = calculateTimeGapInDays(currentDate, parsedReservationDate);

            return timeGapInDays <= 5;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method to Disable all editable fields and the update button
    private void disableEditableFields() {
        reserveDateEditText.setEnabled(false);
        noOfTicketsEditText.setEnabled(false);
        routeEditText.setEnabled(false);
        trainEditText.setEnabled(false);
        startEditText.setEnabled(false);
        destEditText.setEnabled(false);
        update.setEnabled(false);
    }

    // Method to Calculate the time gap in days
    private long calculateTimeGapInDays(Date currentDate, Date reservationDate) {
        long timeGapInMilliseconds = reservationDate.getTime() - currentDate.getTime();
        return timeGapInMilliseconds / (1000 * 60 * 60 * 24);
    }
}
