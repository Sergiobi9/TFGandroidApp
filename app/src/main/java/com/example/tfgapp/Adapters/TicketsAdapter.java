package com.example.tfgapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.tfgapp.Entities.Booking.BookingTicketsList;
import com.example.tfgapp.Entities.Concert.ConcertReduced;
import com.example.tfgapp.Global.Helpers;
import com.example.tfgapp.Global.Utils;
import com.example.tfgapp.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class TicketsAdapter extends RecyclerView.Adapter<TicketsAdapter.ViewHolder> {

    private ArrayList<BookingTicketsList> ticketsArrayList;
    private final String TAG = "TicketsAdapter";
    private Context context;
    private TicketsAdapter.OnConcertTicketListener onLoyaltyCardClicked;

    public TicketsAdapter(Context context, ArrayList<BookingTicketsList> ticketsArrayList, OnConcertTicketListener onLoyaltyCardClicked) {
        this.context = context;
        this.onLoyaltyCardClicked = onLoyaltyCardClicked;
        this.ticketsArrayList = ticketsArrayList;
    }

    @NonNull
    @Override
    public TicketsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        return new TicketsAdapter.ViewHolder(inflater.inflate(R.layout.tickets_list, parent, false), onLoyaltyCardClicked);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketsAdapter.ViewHolder holder, int position) {
        BookingTicketsList booking = ticketsArrayList.get(position);

        String imageUrl = booking.getConcertCover();

        Glide.with(context).load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                }).into(holder.concertCover);

        holder.ticketsPurchased.setText("x" + booking.getBookings().size());

        String concertDateStr = booking.getConcertStarts();

        Calendar concertDate = Helpers.getDateAsCalendar(concertDateStr);
        holder.concertDate.setText(concertDate.get(Calendar.DATE) + " " + Utils.getMonthSimplified(concertDate.get(Calendar.MONTH)) + " " +  concertDate.get(Calendar.YEAR));

        holder.concertStreet.setText(booking.getConcertPlaceName());
        holder.concertName.setText(booking.getConcertName());
        holder.directionsImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Uri gmmIntentUri = Uri.parse(String.format("google.navigation:q=%s,%s&mode=d", booking.getConcertLatitude(), booking.getConcertLongitude()));
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    context.startActivity(mapIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return ticketsArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView concertCover;
        private TextView ticketReference;
        private TextView ticketsPurchased;
        private TextView concertName;
        private TextView concertStreet;
        private TextView concertDate;
        private ImageView directionsImg;
        public TicketsAdapter.OnConcertTicketListener onConcertTicketListener;

        public ViewHolder(@NotNull View view, TicketsAdapter.OnConcertTicketListener onConcertTicketListener) {
            super(view);

            this.concertCover = (ImageView) view.findViewById(R.id.concert_cover);
            this.ticketsPurchased = (TextView) view.findViewById(R.id.concert_tickets_purchased);
            this.concertName = (TextView) view.findViewById(R.id.concert_name);
            this.concertStreet = (TextView) view.findViewById(R.id.concert_place);
            this.concertDate = (TextView) view.findViewById(R.id.concert_date);
            this.directionsImg = (ImageView) view.findViewById(R.id.directions);
            this.onConcertTicketListener = onConcertTicketListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onConcertTicketListener.onConcertTicketClicked(getAdapterPosition());
        }
    }

    public interface OnConcertTicketListener {
        void onConcertTicketClicked(int position);
    }
}