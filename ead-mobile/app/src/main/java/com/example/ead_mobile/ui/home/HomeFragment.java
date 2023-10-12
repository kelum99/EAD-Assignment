package com.example.ead_mobile.ui.home;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.ead_mobile.LoginActivity;
import com.example.ead_mobile.MainActivity2;
import com.example.ead_mobile.R;
import com.example.ead_mobile.RegisterActivity;
import com.example.ead_mobile.databinding.FragmentHomeBinding;
import com.example.ead_mobile.services.ReservationService;
import com.example.ead_mobile.ui.dashboard.DashboardFragment;
import com.example.ead_mobile.ui.reservation.ReservationSummary;
import com.example.ead_mobile.ui.reservation.UpdateReservation;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private EditText nicEditText;
    private TextView bookingDateEditText;
    private EditText reserveDateEditText;
    private EditText noOfTicketsEditText;
    private Spinner routeSpinner;
    private Spinner trainSpinner;
    private Spinner startSpinner;
    private Spinner destinationSpinner;
    private Spinner timeSpinner;
    private Button submit;
    private Button cancel;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        routeSpinner = binding.etRoute;
        trainSpinner = binding.etTrain;
        startSpinner = binding.etStart;
        destinationSpinner = binding.etDestination;
        timeSpinner = binding.timeSPinner;

        ArrayAdapter<CharSequence> routeAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.route_array,
                android.R.layout.simple_spinner_item
        );
        routeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        routeSpinner.setAdapter(routeAdapter);

        ArrayAdapter<CharSequence> trainAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.train_array,
                android.R.layout.simple_spinner_item
        );
        trainAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        trainSpinner.setAdapter(trainAdapter);

        ArrayAdapter<CharSequence> startAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.start_station_array,
                android.R.layout.simple_spinner_item
        );
        startAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        startSpinner.setAdapter(startAdapter);

        ArrayAdapter<CharSequence> destAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.end_station_array,
                android.R.layout.simple_spinner_item
        );
        destAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        destinationSpinner.setAdapter(destAdapter);

        ArrayAdapter<CharSequence> timeAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.time_array,
                android.R.layout.simple_spinner_item
        );
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSpinner.setAdapter(timeAdapter);

        nicEditText = root.findViewById(R.id.et_nic);
        bookingDateEditText = root.findViewById(R.id.et_bookingDate);
        reserveDateEditText = root.findViewById(R.id.et_reserveDate);
        noOfTicketsEditText = root.findViewById(R.id.et_tickets);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = dateFormat.format(new Date());

        bookingDateEditText.setText(currentDate);

        submit = root.findViewById(R.id.btn_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nic = nicEditText.getText().toString();
                String bookingDate = bookingDateEditText.getText().toString();
                String reservationDate = reserveDateEditText.getText().toString();
                String noOfTickets = noOfTicketsEditText.getText().toString();
                String route = routeSpinner.getSelectedItem().toString();
                String train = trainSpinner.getSelectedItem().toString();
                String start = startSpinner.getSelectedItem().toString();
                String dest = destinationSpinner.getSelectedItem().toString();
                String time = timeSpinner.getSelectedItem().toString();

                Intent intent = new Intent(requireContext(), ReservationSummary.class);
                intent.putExtra("id", "");
                intent.putExtra("userNIC", nic);
                intent.putExtra("bookingDate", bookingDate);
                intent.putExtra("reservationDate", reservationDate);
                intent.putExtra("noOfTickets", noOfTickets);
                intent.putExtra("route", route);
                intent.putExtra("train", train);
                intent.putExtra("startingPoint", start);
                intent.putExtra("destination", dest);
                intent.putExtra("time", time);
                intent.putExtra("agentID", nic);
                intent.putExtra("action", "Add");
                startActivityForResult(intent, 201);

            }
        });

        cancel = root.findViewById(R.id.btn_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
