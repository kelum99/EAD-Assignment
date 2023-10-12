package com.example.ead_mobile.services;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.ead_mobile.interfaces.IUserService;
import com.example.ead_mobile.models.DeactivateProfileRequestModel;
import com.example.ead_mobile.models.UpdateRequestModel;

import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserService {
    private static UserService singleton;
    private final IUserService iUserService;

    //Return UserService singleton object
    public static UserService getInstance() {
        if (singleton == null)
            singleton = new UserService();

        return singleton;
    }

    private UserService() {
        iUserService = NetworkService.getInstance().createService(IUserService.class);
    }

    //updateProfile method implementation
    public void updateProfile(
            String id,
            String nic,
            String name,
            String email,
            String mobile,
            Runnable onSuccess,
            Consumer<String> onError
    ) {
        if (!NetworkService.getInstance().isNetworkAvailable()) {
            onError.accept("No Internet Connectivity!");
            return;
        }
        UpdateRequestModel updateRequestModel = new UpdateRequestModel(nic,mobile,email,name);
        iUserService.updateProfile(id,updateRequestModel).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if(response.code() == 200){
                    onSuccess.run();
                } else {
                    onError.accept("Something went wrong! Try again.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Log.e("Error in Update:", t.toString());
                onError.accept("Internal Server Error!");
            }
        });
    }

    //deactivateProfile method implementation
    public void deactivateProfile(
            String id,
            String status,
            Runnable onSuccess,
            Consumer<String> onError
    ) {
        if (!NetworkService.getInstance().isNetworkAvailable()) {
            onError.accept("No Internet Connectivity!");
            return;
        }
        DeactivateProfileRequestModel deactivateProfileRequestModel = new DeactivateProfileRequestModel(status);
        iUserService.deactivateProfile(id,deactivateProfileRequestModel).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if(response.code() == 200){
                    onSuccess.run();
                } else {
                    onError.accept("Something went wrong! Try again.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Log.e("Error in Deactivating Profile:", t.toString());
                onError.accept("Internal Server Error!");
            }
        });
    }
}
