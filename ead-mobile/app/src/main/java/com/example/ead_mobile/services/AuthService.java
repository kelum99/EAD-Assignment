package com.example.ead_mobile.services;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.ead_mobile.interfaces.IUserService;
import com.example.ead_mobile.models.LoginRequestModel;
import com.example.ead_mobile.models.LoginResponseModel;
import com.example.ead_mobile.models.RegisterRequestModel;

import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthService {
    private static AuthService singleton;
    private final IUserService userService;

    public static AuthService getInstance() {
        if (singleton == null)
            singleton = new AuthService();

        return singleton;
    }

    private AuthService() {
        userService = NetworkService.getInstance().createService(IUserService.class);
        //databaseManager = DatabaseManager.getInstance();
    }

    public void login(
            String nic,
            String password,
            Runnable onSuccess,
            Consumer<String> onError
    ) {
        if (!NetworkService.getInstance().isNetworkAvailable()) {
            onError.accept("No Internet Connectivity!");
            return;
        }

        LoginRequestModel request = new LoginRequestModel(nic, password);
        userService.login(request)
                .enqueue(new Callback<LoginResponseModel>() {
                    @Override
                    public void onResponse(@NonNull Call<LoginResponseModel> call, @NonNull Response<LoginResponseModel> response) {
                        if (response.code() == 200) {
                            assert response.body() != null;
                            if (response.body().status.equalsIgnoreCase("Active")) {
                                onSuccess.run();
                            } else {
                                onError.accept("This Account is Deactivated!");
                            }

                        } else {
                            onError.accept("Incorrect Credentials");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<LoginResponseModel> call, @NonNull Throwable t) {
                        Log.e("Error in Login:", t.toString());
                        onError.accept("Internal Server Error!");
                    }
                });

    }

    public void register(
            String nic,
            String password,
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
        RegisterRequestModel registerRequestModel = new RegisterRequestModel(nic,password,mobile,email,name);
        userService.register(registerRequestModel)
                .enqueue(new Callback<LoginResponseModel>() {
                    @Override
                    public void onResponse(@NonNull Call<LoginResponseModel> call, @NonNull Response<LoginResponseModel> response) {
                        if (response.code() == 201) {
                            assert response.body() != null;
                            onSuccess.run();

                        } else {
                            onError.accept("Something went wrong! Try again.");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<LoginResponseModel> call, @NonNull Throwable t) {
                        Log.e("Error in Register:", t.toString());
                        onError.accept("Internal Server Error!");
                    }
                });

    }
}
