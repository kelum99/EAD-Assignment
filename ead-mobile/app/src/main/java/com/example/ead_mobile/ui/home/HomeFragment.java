package com.example.ead_mobile.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.ead_mobile.LoginActivity;
import com.example.ead_mobile.R;
import com.example.ead_mobile.RegisterActivity;
import com.example.ead_mobile.databinding.FragmentHomeBinding;
import com.example.ead_mobile.ui.dashboard.DashboardFragment;
import com.example.ead_mobile.ui.reservation.ReservationSummary;
import com.example.ead_mobile.ui.reservation.UpdateReservation;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private Spinner routeSpinner;
    private Spinner trainSpinner;
    private Spinner startSpinner;
    private Spinner destinationSpinner;
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
                R.array.station_array,
                android.R.layout.simple_spinner_item
        );
        startAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        startSpinner.setAdapter(startAdapter);

        ArrayAdapter<CharSequence> destAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.station_array,
                android.R.layout.simple_spinner_item
        );
        destAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        destinationSpinner.setAdapter(destAdapter);

        submit = root.findViewById(R.id.btn_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(requireContext(), ReservationSummary.class);
                startActivity(i);
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
