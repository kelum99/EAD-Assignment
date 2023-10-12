package com.example.ead_mobile.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.ead_mobile.interfaces.IUserService;
import com.example.ead_mobile.models.LoginRequestModel;
import com.example.ead_mobile.models.LoginResponseModel;
import com.example.ead_mobile.models.RegisterRequestModel;
import com.example.ead_mobile.models.User;
import com.example.ead_mobile.models.UserEntity;

import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthService {
    private static AuthService singleton;
    private final IUserService userService;
    private final DatabaseService databaseService;

    private final String login_status = "login_status";
    private final String is_logged = "is_logged";
    private final String user_nic = "user_nic";
    private final String user_id = "user_id";

    public static AuthService getInstance() {
        if (singleton == null)
            singleton = new AuthService();

        return singleton;
    }

    private AuthService() {
        userService = NetworkService.getInstance().createService(IUserService.class);
        databaseService = DatabaseService.getInstance();
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
                                User user = new User();
                                user.id = response.body().id;
                                user.nic = response.body().nic;
                                user.name = response.body().name;
                                user.email = response.body().email;
                                user.mobile = response.body().mobile;
                                user.status = response.body().status;
                                saveUserDetails(user);
                                setLoggingStatus(true,user.nic,user.id);
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

    public void saveUserDetails(User user) {
        new Thread(() -> {
            databaseService.db().iLocalService().removeAll();
            databaseService.db().iLocalService().insert(UserEntity.fromDto(user));
        }).start();
    }

    public void setLoggingStatus(boolean isLoggedIn, String nic,String id){
        Context context = ContextService.getInstance().getApplicationContext();
        SharedPreferences.Editor editor = context.getSharedPreferences(login_status, Context.MODE_PRIVATE).edit();
        editor.putBoolean(is_logged, isLoggedIn);
        editor.putString(user_nic, nic);
        editor.putString(user_id, id);
        editor.apply();
    }

    public boolean getLoggingStatus(){
        Context context = ContextService.getInstance().getApplicationContext();
        SharedPreferences preference = context.getSharedPreferences(login_status, Context.MODE_PRIVATE);
        return preference.getBoolean(is_logged, false);
    }

    public String getLoggedUserNic(){
        Context context = ContextService.getInstance().getApplicationContext();
        SharedPreferences preference = context.getSharedPreferences(login_status, Context.MODE_PRIVATE);
        return preference.getString(user_nic,"DEFAULT");
    }

    public String getLoggedUserId(){
        Context context = ContextService.getInstance().getApplicationContext();
        SharedPreferences preference = context.getSharedPreferences(login_status, Context.MODE_PRIVATE);
        return preference.getString(user_id,"DEFAULT_ID");
    }

    public void logout() {
        new Thread(() -> {
            Context context = ContextService.getInstance().getApplicationContext();
            SharedPreferences.Editor editor = context.getSharedPreferences(login_status, Context.MODE_PRIVATE).edit();
            editor.clear();
            editor.apply();
            databaseService.db().iLocalService().removeAll();
        }).start();
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
