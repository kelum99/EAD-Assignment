package com.example.ead_mobile.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.ead_mobile.R;
import com.example.ead_mobile.databinding.FragmentDashboardBinding;
import com.example.ead_mobile.ui.reservation.ReservationSummary;
import com.example.ead_mobile.ui.reservation.UpdateReservation;
import com.example.ead_mobile.models.ReservationRequestModel;
import com.example.ead_mobile.models.ReservationResponseModel;
import com.example.ead_mobile.services.ReservationService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.concurrent.TimeUnit;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private ListView reservationsListView;
    private ListView reservationHistoryList;
    private List<Reservation> reservationList;
    private List<Reservation> historyReservations;
    private ReservationAdapter reservationAdapter;
    private ReservationHistoryAdapter reservationHistoryAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        reservationsListView = root.findViewById(R.id.reservationListView);
        reservationHistoryList = root.findViewById(R.id.reservationHistoryList);
        reservationList = new ArrayList<>();
        historyReservations = new ArrayList<>();

        reservationAdapter = new ReservationAdapter();
        reservationsListView.setAdapter(reservationAdapter);

        reservationHistoryAdapter = new ReservationHistoryAdapter();
        reservationHistoryList.setAdapter(reservationHistoryAdapter);

        fetchAndPopulateReservations();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void fetchAndPopulateReservations() {
        ReservationService.getInstance().viewReservations(
                new Consumer<List<ReservationResponseModel>>() {
                    @Override
                    public void accept(List<ReservationResponseModel> reservations) {
                        // Clear existing data
                        reservationList.clear();
                        historyReservations.clear();

                        // Get the current date
                        Date currentDate = new Date();

                        // Populate with fetched reservations and categorize them
                        for (ReservationResponseModel reservationResponseModel : reservations) {
                            // Parse reservation date as a Date object
                            Date reservationDate;
                            try {
                                reservationDate = new SimpleDateFormat("yyyy-MM-dd").parse(reservationResponseModel.getReservationDate());
                            } catch (ParseException e) {
                                // Handle parsing error
                                e.printStackTrace();
                                continue;
                            }

                            // Check if the reservation date is after the current date (upcoming) or before (history)
                            if (reservationDate != null) {
                                Reservation reservation = new Reservation(
                                        reservationResponseModel.getId(),
                                        reservationResponseModel.getUserNIC(),
                                        reservationResponseModel.getBookingDate(),
                                        reservationResponseModel.getReservationDate(),
                                        reservationResponseModel.getNoOfTickets(),
                                        reservationResponseModel.getRoute(),
                                        reservationResponseModel.getTrain(),
                                        reservationResponseModel.getStartingPoint(),
                                        reservationResponseModel.getDestination(),
                                        reservationResponseModel.getTime(),
                                        reservationResponseModel.getAgentID()
                                );
                                if (reservationDate.after(currentDate)) {
                                    reservationList.add(reservation);
                                } else {
                                    historyReservations.add(reservation);
                                }
                            }
                        }

                        // Notify the adapter that the data has changed for both lists
                        reservationAdapter.notifyDataSetChanged();
                        reservationHistoryAdapter.notifyDataSetChanged();
                    }
                },
                new Consumer<String>() {
                    @Override
                    public void accept(String error) {
                        // Handle the error, e.g., show an error message
                        // You can display a toast or handle it according to your UI.
                        Toast.makeText(requireContext(), "Error: " + error, Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private class ReservationAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return reservationList.size();
        }

        @Override
        public Object getItem(int position) {
            return reservationList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(requireContext()).inflate(R.layout.activity_reservation, parent, false);
            }

            TextView RouteTextView = convertView.findViewById(R.id.RouteTextView);
            TextView trainTextView = convertView.findViewById(R.id.trainTextView);
            Button updateReservationButton = convertView.findViewById(R.id.updateReservationButton);
            Button cancelReservationButton = convertView.findViewById(R.id.cancelReservationButton);

            final Reservation reservation = reservationList.get(position);

            RouteTextView.setText(reservation.getRoute());
            trainTextView.setText(reservation.getTrain());

            // Parse reservation date as a Date object
            Date reservationDate;
            try {
                reservationDate = new SimpleDateFormat("yyyy-MM-dd").parse(reservation.getReservationDate());
            } catch (ParseException e) {
                // Handle parsing error
                e.printStackTrace();
                return convertView;
            }

            // Calculate the time gap in days
            long timeGapInDays = calculateTimeGapInDays(reservationDate);

            if (timeGapInDays <= 5) {
                // If the time gap is less than or equal to 5 days, disable the buttons
                updateReservationButton.setEnabled(false);
                cancelReservationButton.setEnabled(false);

                // Add a click event for the list item to redirect to the reservation summary page
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        redirectToReservationSummary(reservation);
                    }
                });
            } else {
                // Enable the buttons
                updateReservationButton.setEnabled(true);
                cancelReservationButton.setEnabled(true);

                // Clear the click event
                convertView.setOnClickListener(null);
            }

            updateReservationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(requireContext(), UpdateReservation.class);
                    intent.putExtra("id", reservation.getId());
                    intent.putExtra("userNIC", reservation.getUserNIC());
                    intent.putExtra("bookingDate", reservation.getBookingDate());
                    intent.putExtra("reservationDate", reservation.getReservationDate());
                    intent.putExtra("noOfTickets", reservation.getNoOfTickets());
                    intent.putExtra("route", reservation.getRoute());
                    intent.putExtra("train", reservation.getTrain());
                    intent.putExtra("startingPoint", reservation.getStartingPoint());
                    intent.putExtra("destination", reservation.getDestination());
                    intent.putExtra("time", reservation.getTime());
                    intent.putExtra("agentID", reservation.getAgentID());
                    intent.putExtra("action", "Edit");
                    startActivityForResult(intent, 201);
                }
            });

            cancelReservationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Create an intent for canceling the reservation
                    Intent intent = new Intent(requireContext(), ReservationSummary.class);
                    intent.putExtra("id", reservation.getId());
                    intent.putExtra("userNIC", reservation.getUserNIC());
                    intent.putExtra("bookingDate", reservation.getBookingDate());
                    intent.putExtra("reservationDate", reservation.getReservationDate());
                    intent.putExtra("noOfTickets", reservation.getNoOfTickets());
                    intent.putExtra("route", reservation.getRoute());
                    intent.putExtra("train", reservation.getTrain());
                    intent.putExtra("startingPoint", reservation.getStartingPoint());
                    intent.putExtra("destination", reservation.getDestination());
                    intent.putExtra("time", reservation.getTime());
                    intent.putExtra("agentID", reservation.getAgentID());
                    intent.putExtra("action", "Delete");
                    startActivityForResult(intent, 201);
                }
            });

            return convertView;
        }

        // Helper method to calculate the time gap between the reservation date and the current date
        private long calculateTimeGapInDays(Date reservationDate) {
            Date currentDate = new Date();
            long timeGapInMilliseconds = reservationDate.getTime() - currentDate.getTime();
            return TimeUnit.MILLISECONDS.toDays(timeGapInMilliseconds);
        }

        // Helper method to redirect to the reservation summary page
        private void redirectToReservationSummary(Reservation reservation) {
            Intent intent = new Intent(requireContext(), ReservationSummary.class);
            intent.putExtra("id", reservation.getId());
            intent.putExtra("userNIC", reservation.getUserNIC());
            intent.putExtra("bookingDate", reservation.getBookingDate());
            intent.putExtra("reservationDate", reservation.getReservationDate());
            intent.putExtra("noOfTickets", reservation.getNoOfTickets());
            intent.putExtra("route", reservation.getRoute());
            intent.putExtra("train", reservation.getTrain());
            intent.putExtra("startingPoint", reservation.getStartingPoint());
            intent.putExtra("destination", reservation.getDestination());
            intent.putExtra("time", reservation.getTime());
            intent.putExtra("agentID", reservation.getAgentID());
            intent.putExtra("action", "View");
            startActivityForResult(intent, 201);
        }
    }


    private class ReservationHistoryAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return historyReservations.size();
        }

        @Override
        public Object getItem(int position) {
            return historyReservations.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(requireContext()).inflate(R.layout.activity_reservation_history_item, parent, false);
            }

            TextView reservationTextView = convertView.findViewById(R.id.reserveDateTextView);
            TextView RouteTextView = convertView.findViewById(R.id.RouteTextView);
            TextView trainTextView = convertView.findViewById(R.id.trainTextView);

            final Reservation reservation = historyReservations.get(position);

            reservationTextView.setText(reservation.getReservationDate().subSequence(0,10));
            RouteTextView.setText(reservation.getRoute());
            trainTextView.setText(reservation.getTrain());

            // Add a click event for the list item to redirect to the reservation summary page with "View" action
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    redirectToReservationSummary(reservation);
                }
            });

            return convertView;
        }

        // Helper method to redirect to the reservation summary page with "View" action
        private void redirectToReservationSummary(Reservation reservation) {
            Intent intent = new Intent(requireContext(), ReservationSummary.class);
            intent.putExtra("id", reservation.getId());
            intent.putExtra("userNIC", reservation.getUserNIC());
            intent.putExtra("bookingDate", reservation.getBookingDate());
            intent.putExtra("reservationDate", reservation.getReservationDate());
            intent.putExtra("noOfTickets", reservation.getNoOfTickets());
            intent.putExtra("route", reservation.getRoute());
            intent.putExtra("train", reservation.getTrain());
            intent.putExtra("startingPoint", reservation.getStartingPoint());
            intent.putExtra("destination", reservation.getDestination());
            intent.putExtra("time", reservation.getTime());
            intent.putExtra("agentID", reservation.getAgentID());
            intent.putExtra("action", "View");
            startActivityForResult(intent, 201);
        }
    }


    private class Reservation {
        private String Id;
        private String UserNIC;
        private String BookingDate;
        private String ReservationDate;
        private String NoOfTickets;
        private String Route;
        private String Train;
        private String StartingPoint;
        private String Destination;
        private String Time;
        private String AgentID;

        public Reservation(String id,String userNIC, String bookingDate, String reservationDate, String noOfTickets, String route, String train, String startingPoint, String destination, String time, String agentID) {
            Id = id;
            UserNIC = userNIC;
            BookingDate = bookingDate;
            ReservationDate = reservationDate;
            NoOfTickets = noOfTickets;
            Route = route;
            Train = train;
            StartingPoint = startingPoint;
            Destination = destination;
            Time = time;
            AgentID = agentID;
        }

        public String getId() {
            return Id;
        }

        public String getUserNIC() {
            return UserNIC;
        }

        public String getBookingDate() {
            return BookingDate;
        }

        public String getReservationDate() {
            return ReservationDate;
        }

        public String getNoOfTickets() {
            return NoOfTickets;
        }

        public String getRoute() {
            return Route;
        }

        public String getTrain() {
            return Train;
        }

        public String getStartingPoint() {
            return StartingPoint;
        }

        public String getDestination() {
            return Destination;
        }

        public String getTime() {
            return Time;
        }

        public String getAgentID() {
            return AgentID;
        }
    }
}
