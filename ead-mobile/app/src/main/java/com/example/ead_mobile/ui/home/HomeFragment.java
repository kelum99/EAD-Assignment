package com.example.ead_mobile.ui.home;

import android.content.Intent;
import android.os.Bundle;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.ead_mobile.R;
import com.example.ead_mobile.databinding.FragmentHomeBinding;
import com.example.ead_mobile.services.AuthService;
import com.example.ead_mobile.ui.reservation.ReservationSummary;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private TextView nicEditText;
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
    AuthService authService;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        //Get instance of the authService to capture user
        authService = AuthService.getInstance();

        //spinners for route, train, time, start and destination
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
        routeSpinner.setAdapter(routeAdapter);;

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

        //Get the current date and format for booking date
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = dateFormat.format(new Date());

        bookingDateEditText.setText(currentDate);
        nicEditText.setText(authService.getLoggedUserNic());

        submit = root.findViewById(R.id.btn_submit);
        submit.setOnClickListener(new View.OnClickListener() {

            //Navigating to summary page with parameters for adding confirmation
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

                if (nic.isEmpty() || bookingDate.isEmpty() || reservationDate.isEmpty() || noOfTickets.isEmpty()
                        || routeSpinner.getSelectedItem().equals("Select Route") || trainSpinner.getSelectedItem().equals("Select Train") || startSpinner.getSelectedItem().equals("Select Start") || destinationSpinner.getSelectedItem().equals("Select Destination") || timeSpinner.getSelectedItem().equals("Select Time")) {
                    // Display an error message because some fields are empty
                    Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {

                    Intent intent = new Intent(requireContext(), ReservationSummary.class);
                    intent.putExtra("id", "");
                    intent.putExtra("userNIC", authService.getLoggedUserNic());
                    intent.putExtra("bookingDate", bookingDate);
                    intent.putExtra("reservationDate", reservationDate);
                    intent.putExtra("noOfTickets", noOfTickets);
                    intent.putExtra("route", route);
                    intent.putExtra("train", train);
                    intent.putExtra("startingPoint", start);
                    intent.putExtra("destination", dest);
                    intent.putExtra("time", time);
                    intent.putExtra("agentID", authService.getLoggedUserNic());
                    intent.putExtra("action", "Add");
                    startActivityForResult(intent, 201);
                }
            }
        });

        cancel = root.findViewById(R.id.btn_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {

            //Reset Fields
            @Override
            public void onClick(View view) {
                reserveDateEditText.setText("");
                noOfTicketsEditText.setText("");
                routeSpinner.setSelection(0);
                trainSpinner.setSelection(0);
                startSpinner.setSelection(0);
                destinationSpinner.setSelection(0);
                timeSpinner.setSelection(0);
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
