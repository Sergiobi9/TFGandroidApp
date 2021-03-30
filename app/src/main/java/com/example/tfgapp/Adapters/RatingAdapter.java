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
import com.example.tfgapp.Entities.Rating.Rating;
import com.example.tfgapp.Entities.Rating.RatingSimplified;
import com.example.tfgapp.Global.Helpers;
import com.example.tfgapp.Global.Utils;
import com.example.tfgapp.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;

public class RatingAdapter extends RecyclerView.Adapter<RatingAdapter.ViewHolder> {

    private ArrayList<RatingSimplified> ratingArrayList;
    private final String TAG = "RatingAdapter";
    private Context context;
    private OnRatingListener onRatingListener;

    public RatingAdapter(Context context, ArrayList<RatingSimplified> ratingArrayList, OnRatingListener onLoyaltyCardClicked) {
        this.context = context;
        this.onRatingListener = onLoyaltyCardClicked;
        this.ratingArrayList = ratingArrayList;
    }

    @NonNull
    @Override
    public RatingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        return new RatingAdapter.ViewHolder(inflater.inflate(R.layout.concert_rating_list, parent, false), onRatingListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RatingAdapter.ViewHolder holder, int position) {
        RatingSimplified rating = ratingArrayList.get(position);

        Glide.with(context).load(rating.getConcertCover())
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
    }

    @Override
    public int getItemCount() {
        return ratingArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView concertCover;
        private TextView ticketReference;
        private TextView ticketsPurchased;
        private TextView concertName;
        private TextView concertStreet;
        private TextView concertDate;
        private ImageView directionsImg;
        public OnRatingListener onRatingListener;

        public ViewHolder(@NotNull View view, OnRatingListener onRatingListener) {
            super(view);

            this.concertCover = (ImageView) view.findViewById(R.id.concert_cover);
            this.ticketsPurchased = (TextView) view.findViewById(R.id.concert_tickets_purchased);
            this.concertName = (TextView) view.findViewById(R.id.concert_name);
            this.concertStreet = (TextView) view.findViewById(R.id.concert_place);
            this.concertDate = (TextView) view.findViewById(R.id.concert_date);
            this.directionsImg = (ImageView) view.findViewById(R.id.directions);
            this.onRatingListener = onRatingListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onRatingListener.onRatingClicked(getAdapterPosition());
        }
    }

    public interface OnRatingListener {
        void onRatingClicked(int position);
    }
}