package com.example.ead_mobile.ui.profile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.ead_mobile.LoginActivity;
import com.example.ead_mobile.databinding.FragmentProfileBinding;
import com.example.ead_mobile.services.AuthService;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    AuthService authService;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileViewModel profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final Button logout_btn = binding.logoutBtn;
        authService = AuthService.getInstance();

        Log.i("User Nic", authService.getLoggedUserNic());
        Log.i("User Id", authService.getLoggedUserId());
        logout_btn.setOnClickListener(view -> logout());
        return root;
    }

    public void logout() {
        authService.logout();
        Intent i = new Intent(getActivity(), LoginActivity.class);
        startActivity(i);
        ((Activity) getActivity()).overridePendingTransition(0, 0);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}