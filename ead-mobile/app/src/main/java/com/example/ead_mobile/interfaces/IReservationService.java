package com.example.ead_mobile.interfaces;

import com.example.ead_mobile.models.ReservationRequestModel;
import com.example.ead_mobile.models.ReservationResponseModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Path;

//Reservation Interface with the APIs for CRUD operations
public interface IReservationService {
    @POST("reservation/add-reservation")
    Call<ReservationResponseModel> addReservation(@Body ReservationRequestModel request);

    @PUT("reservation/update-reservation/{id}")
    Call<ReservationResponseModel> updateReservation(
            @Path("id") String id,
            @Body ReservationRequestModel request
    );

    @DELETE("reservation/cancel-reservation/{id}")
    Call<Void> cancelReservation(@Path("id") String id);

    @GET("reservation/get-all-reservations")
    Call<List<ReservationResponseModel>> getAllReservations();
}
