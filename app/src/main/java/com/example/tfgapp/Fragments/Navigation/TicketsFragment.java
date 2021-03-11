package com.example.tfgapp.Fragments.Navigation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.tfgapp.Adapters.TicketsAdapter;
import com.example.tfgapp.Entities.Concert.ConcertHome;
import com.example.tfgapp.Global.Api;
import com.example.tfgapp.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TicketsFragment extends Fragment implements TicketsAdapter.OnConcertTicketListener {

    private final String TAG = "TicketsFragment";
    private View view;
    private Context context;
    private ImageView ticketQR;
    private int screenWidth;

    private ArrayList<ConcertHome> ticketsArrayList;
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
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;

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
        Call<ArrayList<ConcertHome>> call = Api.getInstance().getAPI().getHomeConcerts("hello");
        call.enqueue(new Callback<ArrayList<ConcertHome>>() {
            @Override
            public void onResponse(Call<ArrayList<ConcertHome>> call, Response<ArrayList<ConcertHome>> response) {
                switch (response.code()) {
                    case 200:
                        Log.d(TAG, "Tickets concerts success " + response.body());
                        ticketsArrayList = response.body();
                        initTicketsList();
                        break;
                    default:
                        Log.d(TAG, "Tickets concerts default " + response.code());
                        break;
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ConcertHome>> call, Throwable t) {
                Log.d(TAG, "Tickets concerts failure " + t.getLocalizedMessage());
            }
        });
    }

    private void initTicketsList(){
        ticketsAdapter = new TicketsAdapter(context, ticketsArrayList, onTicketListener);
        ticketsRecyclerView.setAdapter(ticketsAdapter);
    }

    @Override
    public void onConcertTicketClicked(int position) {

    }

    /*private void generateQR(){
        ticketQR = view.findViewById(R.id.ticket_qr);
        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        try{
            BitMatrix bitMatrix = qrCodeWriter.encode("www.google.com", BarcodeFormat.QR_CODE, 150, 150);
            Bitmap bitmap = Bitmap.createBitmap(150, 150, Bitmap.Config.RGB_565);

            for (int x = 0; x < 150; x++){
                for (int y = 0; y < 150; y++){
                    bitmap.setPixel(x, y, bitMatrix.get(x,y)? Color.BLACK : Color.WHITE);
                }
            }

            ViewGroup.LayoutParams params = ticketQR.getLayoutParams();
            params.height = (int) (screenWidth * 0.5);
            params.width = (int) (screenWidth * 0.5);
            ticketQR.setLayoutParams(params);

            ticketQR.setImageBitmap(bitmap);

        } catch (Exception exception){
            exception.printStackTrace();
        }
    }*/
}