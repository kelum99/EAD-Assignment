package com.example.ead_mobile.ui.reservation;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ead_mobile.MainActivity2;
import com.example.ead_mobile.R;
import com.example.ead_mobile.services.ReservationService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class ReservationSummary extends AppCompatActivity {

    private Button confirm;
    private Button cancel;
    private TextView nicTextView;
    private TextView routeTextView;
    private TextView bookingDateTextView;
    private TextView reserveDateTextView;
    private TextView noOfTicketsTextView;
    private TextView timeTextView;
    private TextView trainTextView;
    private TextView startTextView;
    private TextView destTextView;
    private ReservationService reservationService;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_summary);

        //Get instance of the reservation service
        reservationService = ReservationService.getInstance();

        //Get intent from add, update, delete pages
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
        String action = intent.getStringExtra("action");
        String agentId = intent.getStringExtra("agentID");
        ;

        nicTextView = findViewById(R.id.tv_nic);
        bookingDateTextView = findViewById(R.id.tv_bookingDate);
        reserveDateTextView = findViewById(R.id.tv_reserveDate);
        noOfTicketsTextView = findViewById(R.id.tv_tickets);
        trainTextView = findViewById(R.id.tv_train);
        routeTextView = findViewById(R.id.tv_route);
        startTextView = findViewById(R.id.tv_start);
        destTextView = findViewById(R.id.tv_destination);
        timeTextView = findViewById(R.id.tv_time);

        //set the received values to fields
        nicTextView.setText(userNIC);
        bookingDateTextView.setText(bookingDate.subSequence(0, 10));
        reserveDateTextView.setText(reservationDate.subSequence(0, 10));
        noOfTicketsTextView.setText(noOfTickets);
        trainTextView.setText(train);
        routeTextView.setText(route);
        startTextView.setText(startingPoint);
        destTextView.setText(destination);
        timeTextView.setText(time);

        //confirm the action after viewing summary
        confirm = findViewById(R.id.btn_confirm);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ("Edit".equals(action)) {
                    progressDialog = new ProgressDialog(ReservationSummary.this);
                    progressDialog.setMessage("Updating Reservation...");
                    progressDialog.show();

                    //ReservationService update method
                    reservationService.updateReservation(id, userNIC, bookingDate, reservationDate, noOfTickets, route, train, startingPoint, destination, time, agentId,
                            () -> handleReservationUpdateSuccessful(),
                            error -> handleReservationUpdateFailed(error));
                } else if ("Delete".equals(action)) {
                    progressDialog = new ProgressDialog(ReservationSummary.this);
                    progressDialog.setMessage("Deleting Reservation...");
                    progressDialog.show();

                    //ReservationService delete method
                    reservationService.cancelReservation(id, () -> handleReservationDeleteSuccessful(),
                            error -> handleReservationDeleteFailed(error));
                } else if ("Add".equals(action)) {
                    progressDialog = new ProgressDialog(ReservationSummary.this);
                    progressDialog.setMessage("Adding Reservation...");
                    progressDialog.show();

                    //Reservations add method
                    reservationService.addReservation(userNIC, bookingDate, reservationDate, noOfTickets, route, train, startingPoint, destination, time, agentId,
                            () -> handleReservationSuccessful(),
                            error -> handleReservationFailed(error));
                }
            }
        });

        //cancel action and navigate back
        cancel = findViewById(R.id.btn_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ReservationSummary.this, MainActivity2.class);
                startActivity(i);
            }
        });

        //view only without performing any action
        if ("View".equals(action)) {
            confirm.setEnabled(false);
        }

        //add reservation validations
        long timeGapInDays = calculateTimeGapInDays(bookingDate, reservationDate);
        if (("Add".equals(action)) && (timeGapInDays > 30)) {
            confirm.setEnabled(false);
        }
    }

    //Method to handle successful reservation creation and notify user
    private void handleReservationSuccessful() {
        progressDialog.dismiss();
        Toast.makeText(this, "Successful!", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, MainActivity2.class);
        startActivity(intent);
    }

    //Method to handle failed reservation creation and notify user
    private void handleReservationFailed(String error) {
        progressDialog.dismiss();
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }

    //Method to handle successful reservation update and notify user
    private void handleReservationUpdateSuccessful() {
        progressDialog.dismiss();
        Toast.makeText(this, "Update Successful!", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, MainActivity2.class);
        startActivity(intent);
    }

    //Method to handle failed reservation update and notify user
    private void handleReservationUpdateFailed(String error) {
        progressDialog.dismiss();
        Toast.makeText(this, "Update Failed: " + error, Toast.LENGTH_LONG).show();
    }

    //Method to handle successful reservation cancel and notify user
    private void handleReservationDeleteSuccessful() {
        progressDialog.dismiss();
        Toast.makeText(this, "Delete Successful!", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, MainActivity2.class);
        startActivity(intent);
    }

    //Method to handle failed reservation cancel and notify user
    private void handleReservationDeleteFailed(String error) {
        progressDialog.dismiss();
        Toast.makeText(this, "Delete Failed: " + error, Toast.LENGTH_LONG).show();
    }

    // Calculate the time gap in days
    private long calculateTimeGapInDays(String startDate, String endDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date start = dateFormat.parse(startDate);
            Date end = dateFormat.parse(endDate);

            long timeGapInMilliseconds = end.getTime() - start.getTime();
            return TimeUnit.MILLISECONDS.toDays(timeGapInMilliseconds);
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
    }

}
