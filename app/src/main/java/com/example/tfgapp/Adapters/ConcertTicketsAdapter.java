package com.example.tfgapp.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.example.tfgapp.Entities.Concert.Pricing.ConcertIntervalPricingDetails;
import com.example.tfgapp.Global.Globals;
import com.example.tfgapp.Global.Helpers;
import com.example.tfgapp.Global.Utils;
import com.example.tfgapp.R;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ConcertTicketsAdapter extends RecyclerView.Adapter<ConcertTicketsAdapter.ViewHolder> {

    private ArrayList<ConcertIntervalPricingDetails> concertIntervalPricingDetails;
    private Context context;
    private Activity activity;

    public ConcertTicketsAdapter(Context context, ArrayList<ConcertIntervalPricingDetails> concertIntervalPricingDetails, Activity activity) {
        this.context = context;
        this.concertIntervalPricingDetails = concertIntervalPricingDetails;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ConcertTicketsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        return new ConcertTicketsAdapter.ViewHolder(inflater.inflate(R.layout.concert_tickets_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ConcertTicketsAdapter.ViewHolder holder, int position) {
        ConcertIntervalPricingDetails concertIntervalPricingDetail = concertIntervalPricingDetails.get(position);

        holder.ticketName.setText(concertIntervalPricingDetail.getName());
        holder.ticketDescription.setText(concertIntervalPricingDetail.getDescription());
        holder.remaining.setText(concertIntervalPricingDetail.getRemaining() + " disponibles");

        if (concertIntervalPricingDetail.getCost() == 0){
            holder.ticketCost.setText("GRATIS");
        } else{
            holder.ticketCost.setText(new DecimalFormat("###.#").format(concertIntervalPricingDetail.getCost()) + "€");
        }

        if (concertIntervalPricingDetail.getDescription() == null || concertIntervalPricingDetail.getDescription().isEmpty()){
            holder.ticketDescription.setVisibility(View.GONE);
        }

        LinearLayout details = holder.linearLayout;
        Utils.responsiveViewWidth(details, 0.7, activity);

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (concertIntervalPricingDetail.getTicketsBought() - 1  < 0){
                    Globals.displayShortToast(context, "Minimo una entrada para comprar");
                } else {
                    concertIntervalPricingDetail.setTicketsBought(concertIntervalPricingDetail.getTicketsBought() - 1);
                    holder.numberTickets.setText(String.valueOf(concertIntervalPricingDetail.getTicketsBought()));
                }
            }
        });

        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (concertIntervalPricingDetail.getTicketsBought() + 1 > concertIntervalPricingDetail.getRemaining()){
                    Globals.displayShortToast(context, "No puedes comprar más entradas de las que hay disponibles");
                } else {
                    concertIntervalPricingDetail.setTicketsBought(concertIntervalPricingDetail.getTicketsBought() + 1);
                    holder.numberTickets.setText(String.valueOf(concertIntervalPricingDetail.getTicketsBought()));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return concertIntervalPricingDetails.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView ticketName;
        private TextView ticketDescription;
        private TextView ticketCost;
        private TextView remaining;
        private ImageView remove;
        private ImageView add;
        private TextView numberTickets;

        private LinearLayout linearLayout;

        public ViewHolder(@NotNull View view) {
            super(view);

            ticketName = view.findViewById(R.id.ticket_name);
            ticketDescription = view.findViewById(R.id.ticket_description);
            ticketCost = view.findViewById(R.id.ticket_cost);
            remaining = view.findViewById(R.id.ticket_remaining);
            remove = view.findViewById(R.id.remove_tickets);
            numberTickets = view.findViewById(R.id.current_tickets_counter_to_book);
            add = view.findViewById(R.id.add_tickets);

            linearLayout = view.findViewById(R.id.tickets_info);
        }
    }

}