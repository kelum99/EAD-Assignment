package com.example.ead_mobile.services;

import android.util.Log;

import com.example.ead_mobile.interfaces.IReservationService;
import com.example.ead_mobile.models.ReservationRequestModel;
import com.example.ead_mobile.models.ReservationResponseModel;

import java.util.List;
import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReservationService {

    private static ReservationService singleton;
    private IReservationService reservationService;

    //Singleton instance for the ReservationService
    public static ReservationService getInstance() {
        if (singleton == null)
            singleton = new ReservationService();

        return singleton;
    }

    private ReservationService() {
        reservationService = NetworkService.getInstance().createService(IReservationService.class);
    }

    //create a reservation
    public void addReservation(
            String UserNIC,
            String BookingDate,
            String ReservationDate,
            String noOfTickets,
            String Route,
            String Train,
            String StartingPoint,
            String Destination,
            String Time,
            String AgentID,
            Runnable onSuccess,
            Consumer<String> onError
    ) {
        {
            if (!NetworkService.getInstance().isNetworkAvailable()) {
                onError.accept("No internet connectivity");
                return;
            }

            ReservationRequestModel request = new ReservationRequestModel(UserNIC, BookingDate, ReservationDate, noOfTickets, Route, Train, StartingPoint, Destination, Time, AgentID);


            reservationService.addReservation(request).enqueue(new Callback<ReservationResponseModel>() {
                @Override
                public void onResponse(Call<ReservationResponseModel> call, Response<ReservationResponseModel> response) {
                    if (response.isSuccessful()) {
                        onSuccess.run();
                    } else {
                        onError.accept("An error occurred when creating the reservation");
                    }
                }

                @Override
                public void onFailure(Call<ReservationResponseModel> call, Throwable t) {
                    Log.e("Error in Login:", t.toString());
                    onError.accept("Internal Server Error!");
                }
            });

        }
    }

    //update a reservation
    public void updateReservation(
            String id,
            String UserNIC,
            String BookingDate,
            String ReservationDate,
            String noOfTickets,
            String Route,
            String Train,
            String StartingPoint,
            String Destination,
            String Time,
            String AgentID,
            Runnable onSuccess,
            Consumer<String> onError
    ) {
        if (!NetworkService.getInstance().isNetworkAvailable()) {
            onError.accept("No internet connectivity");
            return;
        }

        ReservationRequestModel request = new ReservationRequestModel(UserNIC, BookingDate, ReservationDate, noOfTickets, Route, Train, StartingPoint, Destination, Time, AgentID);

        reservationService.updateReservation(id, request).enqueue(new Callback<ReservationResponseModel>() {
            @Override
            public void onResponse(Call<ReservationResponseModel> call, Response<ReservationResponseModel> response) {
                if (response.isSuccessful()) {
                    onSuccess.run();
                } else {
                    onError.accept("An error occurred when updating the reservation");
                }
            }

            @Override
            public void onFailure(Call<ReservationResponseModel> call, Throwable t) {
                Log.e("Error in Login:", t.toString());
                onError.accept("Internal Server Error!");
            }
        });
    }


    //cancel a reservation
    public void cancelReservation(
            String id,
            Runnable onSuccess,
            Consumer<String> onError
    ) {
        if (!NetworkService.getInstance().isNetworkAvailable()) {
            onError.accept("No internet connectivity");
            return;
        }

        reservationService.cancelReservation(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    onSuccess.run();
                } else {
                    onError.accept("An error occurred when cancelling the reservation");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                onError.accept("Unknown error occurred when removing the reservation");
            }
        });
    }

    //view reservations
    public void viewReservations(
            Consumer<List<ReservationResponseModel>> onSuccess,
            Consumer<String> onError
    ) {
        if (!NetworkService.getInstance().isNetworkAvailable()) {
            onError.accept("No internet connectivity");
            return;
        }

        reservationService.getAllReservations().enqueue(new Callback<List<ReservationResponseModel>>() {
            @Override
            public void onResponse(Call<List<ReservationResponseModel>> call, Response<List<ReservationResponseModel>> response) {
                if (response.isSuccessful()) {

                    List<ReservationResponseModel> reservations = response.body();
                    onSuccess.accept(reservations);
                } else {
                    onError.accept("An error occurred when fetching reservations");
                }
            }

            @Override
            public void onFailure(Call<List<ReservationResponseModel>> call, Throwable t) {
                onError.accept("Error occurred when fetching reservations");
            }
        });
    }

}
