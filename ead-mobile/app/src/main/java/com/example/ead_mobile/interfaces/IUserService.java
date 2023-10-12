package com.example.ead_mobile.interfaces;

import com.example.ead_mobile.models.LoginRequestModel;
import com.example.ead_mobile.models.LoginResponseModel;
import com.example.ead_mobile.models.RegisterRequestModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface IUserService {
    @POST("auth/user-login")
    Call<LoginResponseModel> login(@Body LoginRequestModel loginRequest);

    @POST("user/create-user")
    Call<LoginResponseModel> register(@Body RegisterRequestModel registerRequestModel);
}
