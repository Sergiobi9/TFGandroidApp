package com.example.tfgapp.Adapters;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tfgapp.Entities.Artist.ArtistInfo;
import com.example.tfgapp.Entities.Concert.ConcertReduced;
import com.example.tfgapp.Entities.Concert.Pricing.ConcertIntervalPricing;
import com.example.tfgapp.Global.Globals;
import com.example.tfgapp.Global.Helpers;
import com.example.tfgapp.Global.Utils;
import com.example.tfgapp.R;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

public class ConcertTicketsRegisterPricingAdapter extends RecyclerView.Adapter<ConcertTicketsRegisterPricingAdapter.ViewHolder> {

    private ArrayList<ConcertIntervalPricing> concertIntervalPricing;
    private Context context;
    private Activity activity;

    public ConcertTicketsRegisterPricingAdapter(Context context, ArrayList<ConcertIntervalPricing> concertIntervalPricing, Activity activity) {
        this.context = context;
        this.concertIntervalPricing = concertIntervalPricing;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ConcertTicketsRegisterPricingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ConcertTicketsRegisterPricingAdapter.ViewHolder(inflater.inflate(R.layout.concert_tickets_register_pricing_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ConcertTicketsRegisterPricingAdapter.ViewHolder holder, int position) {

        ImageView removeIcon = holder.removeItem;
        removeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position == 0){
                    Globals.displayShortToast(context, "No se puede eliminar, almenos registra un precio para las entradas");
                } else {
                    try {
                        holder.ticketPrice.setText("");
                        holder.ticketNumber.setText("");
                        holder.ticketName.setText("");
                        holder.ticketDescription.setText("");

                        concertIntervalPricing.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, concertIntervalPricing.size());
                    } catch (IllegalArgumentException ie){

                    }
                }
            }
        });

        holder.ticketName.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                String pricingName = s.toString();
                concertIntervalPricing.get(position).setName(pricingName);
                concertIntervalPricing.set(position, concertIntervalPricing.get(position));
            }
        });

        holder.ticketDescription.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                String pricingDescription = s.toString();
                concertIntervalPricing.get(position).setDescription(pricingDescription);
                concertIntervalPricing.set(position, concertIntervalPricing.get(position));
            }
        });

        holder.ticketNumber.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                try{
                    if (s.toString() != null && !s.toString().equals("")){
                        int numberTickets = Integer.parseInt(s.toString());
                        concertIntervalPricing.get(position).setNumberTickets(numberTickets);
                        concertIntervalPricing.set(position, concertIntervalPricing.get(position));
                    }
                } catch (NumberFormatException numberFormatException){

                }
            }
        });

        holder.ticketPrice.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                try{
                    if (s.toString() != null && !s.toString().equals("")){
                        double price = Double.parseDouble(s.toString());
                        concertIntervalPricing.get(position).setCost(price);
                        concertIntervalPricing.set(position, concertIntervalPricing.get(position));
                    }
                } catch (NumberFormatException numberFormatException){

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return concertIntervalPricing.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        EditText ticketName, ticketDescription, ticketNumber, ticketPrice;
        ImageView removeItem;

        public ViewHolder(@NotNull View view) {
            super(view);

            removeItem = view.findViewById(R.id.remove);
            ticketName = view.findViewById(R.id.ticket_name);
            ticketDescription = view.findViewById(R.id.ticket_description);
            ticketNumber = view.findViewById(R.id.number_tickets);
            ticketPrice = view.findViewById(R.id.ticket_price);
        }


    }

}