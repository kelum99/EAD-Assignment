package com.example.ead_mobile.ui.profile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.ead_mobile.LoginActivity;
import com.example.ead_mobile.databinding.FragmentProfileBinding;
import com.example.ead_mobile.models.UserEntity;
import com.example.ead_mobile.services.AuthService;
import com.example.ead_mobile.services.DatabaseService;
import com.example.ead_mobile.services.UserService;
import com.example.ead_mobile.ui.UpdateProfile;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    AuthService authService;
    UserService userService;
    DatabaseService databaseService;
    TextView nic;
    TextView name;
    TextView email;
    TextView mobile;
    TextView status;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        final Button logout_btn = binding.logoutBtn;
        final Button update_btn = binding.updateBtn;
        final Button deactivate_btn = binding.deactivateBtn;
        authService = AuthService.getInstance();
        databaseService = DatabaseService.getInstance();
        userService = UserService.getInstance();

        this.nic = binding.nicText;
        this.name = binding.nameText;
        this.email = binding.emailText;
        this.mobile = binding.mobileText;
        this.status = binding.statusText;

        logout_btn.setOnClickListener(view -> logout());
        update_btn.setOnClickListener(view -> navigateUpdate());
        deactivate_btn.setOnClickListener(view -> deactivateConfirm());

        //Load locally saved user details form local DB
        new Thread(() -> {
            UserEntity userEntity = databaseService.db().iLocalService().getAll().get(0);

            this.nic.setText(userEntity.nic);
            this.name.setText(userEntity.name);
            this.status.setText(userEntity.status);
            this.mobile.setText(userEntity.mobile);
            this.email.setText(userEntity.email);
        }).start();
        return root;
    }

    //Logout function
    public void logout() {
        authService.logout();
        Intent i = new Intent(getActivity(), LoginActivity.class);
        startActivity(i);
        (getActivity()).overridePendingTransition(0, 0);
    }

    // Navigation to update screen
    public void navigateUpdate() {
        Intent i = new Intent(getActivity(), UpdateProfile.class);
        startActivity(i);
        (getActivity()).overridePendingTransition(0, 0);
    }

    //Confirmation Alert to deactivate account
    public void deactivateConfirm() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage("Are you sure to deactivate your account?");
        builder.setTitle("Deactivating Alert!");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
            deactivate();
        });

        builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
            dialog.cancel();
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    //Deactivate account function
    public void deactivate() {
        String id = authService.getLoggedUserId();
        final String status = "Deactivated";
        userService.deactivateProfile(
                id,
                status,
                () -> handleSuccess(),
                error -> handleError(error)
        );
    }

    //Handle success output from deactivate function
    public void handleSuccess() {
        Toast.makeText(getActivity(), "Your profile successfully deactivated!", Toast.LENGTH_LONG).show();
        logout();
    }

    //Handle failed output from deactivate function
    public void handleError(String error) {
        Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}