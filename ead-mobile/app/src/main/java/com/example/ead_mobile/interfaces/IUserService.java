package com.example.ead_mobile.interfaces;

import com.example.ead_mobile.models.DeactivateProfileRequestModel;
import com.example.ead_mobile.models.LoginRequestModel;
import com.example.ead_mobile.models.LoginResponseModel;
import com.example.ead_mobile.models.RegisterRequestModel;
import com.example.ead_mobile.models.UpdateRequestModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

//Backend API crud interface methods
public interface IUserService {
    @POST("auth/user-login")
    Call<LoginResponseModel> login(@Body LoginRequestModel loginRequest);

    @POST("user/create-user")
    Call<LoginResponseModel> register(@Body RegisterRequestModel registerRequestModel);

    @PUT("user/update-user/{id}")
    Call<Void> updateProfile(@Path("id") String id, @Body UpdateRequestModel updateRequestModel);

    @PUT("user/update-user-status/{id}")
    Call<Void> deactivateProfile(@Path("id") String id, @Body DeactivateProfileRequestModel deactivateProfileRequestModel);
}
