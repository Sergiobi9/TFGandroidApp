package com.example.tfgapp.Fragments.Navigation.User.Tickets;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.tfgapp.Adapters.TicketsAdapter;
import com.example.tfgapp.Entities.Booking.Booking;
import com.example.tfgapp.Entities.Booking.BookingTicketsList;
import com.example.tfgapp.Entities.Concert.ConcertReduced;
import com.example.tfgapp.Fragments.Artist.ArtistFragment;
import com.example.tfgapp.Global.Api;
import com.example.tfgapp.Global.CurrentUser;
import com.example.tfgapp.Global.Helpers;
import com.example.tfgapp.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TicketsFragment extends Fragment implements TicketsAdapter.OnConcertTicketListener {

    private final String TAG = "TicketsFragment";
    private View view;
    private Context context;
    private ImageView ticketQR;

    private ArrayList<BookingTicketsList> ticketsArrayList;
    private RecyclerView ticketsRecyclerView;
    private TicketsAdapter ticketsAdapter;
    private TicketsAdapter.OnConcertTicketListener onTicketListener;

    public TicketsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tickets, container, false);
        context = getContext();
        onTicketListener = this;

        initView();

        return view;
    }

    private void initView(){
        ticketsRecyclerView = view.findViewById(R.id.tickets_recycler_view);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        ticketsRecyclerView.setLayoutManager(mLayoutManager);
        ticketsRecyclerView.setNestedScrollingEnabled(false);

        getTickets();
    }

    private void getTickets(){
        String userId = CurrentUser.getInstance(context).getCurrentUser().getUser().getId();
        String currentDate = Helpers.getTimeStamp();
        Call<ArrayList<BookingTicketsList>> call = Api.getInstance().getAPI().getUserTicketsBooked(userId, currentDate);
        call.enqueue(new Callback<ArrayList<BookingTicketsList>>() {
            @Override
            public void onResponse(Call<ArrayList<BookingTicketsList>> call, Response<ArrayList<BookingTicketsList>> response) {
                switch (response.code()) {
                    case 200:
                        Log.d(TAG, "Tickets concerts success " + response.body());
                        ticketsArrayList = response.body();

                        if (ticketsArrayList != null && ticketsArrayList.size() > 0)
                            initTicketsList();
                        else {
                            view.findViewById(R.id.no_tickets_yet).setVisibility(View.VISIBLE);
                            ticketsRecyclerView.setVisibility(View.GONE);
                        }
                        break;
                    default:
                        Log.d(TAG, "Tickets concerts default " + response.code());
                        view.findViewById(R.id.no_tickets_yet).setVisibility(View.VISIBLE);
                        ticketsRecyclerView.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onFailure(Call<ArrayList<BookingTicketsList>> call, Throwable t) {
                Log.d(TAG, "Tickets concerts failure " + t.getLocalizedMessage());
                view.findViewById(R.id.no_tickets_yet).setVisibility(View.VISIBLE);
                ticketsRecyclerView.setVisibility(View.GONE);
            }
        });
    }

    private void initTicketsList(){
        ticketsAdapter = new TicketsAdapter(context, ticketsArrayList, onTicketListener);
        ticketsRecyclerView.setAdapter(ticketsAdapter);
    }

    @Override
    public void onConcertTicketClicked(int position) {
        ArrayList<Booking> bookings = ticketsArrayList.get(position).getBookings();
        String concertName = ticketsArrayList.get(position).getConcertName();
        String concertPlace = ticketsArrayList.get(position).getConcertPlaceName();
        String concertDate = ticketsArrayList.get(position).getConcertStarts();
        goTicketsQR(bookings, concertName, concertPlace, concertDate);
    }

    private void goTicketsQR(ArrayList<Booking> bookings, String concertName, String concertPlace, String concertDate){
        Bundle bundle = new Bundle();
        bundle.putSerializable("bookings", bookings);
        bundle.putString("concertName", concertName);
        bundle.putString("concertPlace", concertPlace);
        bundle.putString("concertDate", concertDate);

        TicketsQRFragment ticketsQRFragment = new TicketsQRFragment();
        ticketsQRFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.main_fragment, ticketsQRFragment).addToBackStack(null).commit();
    }
}