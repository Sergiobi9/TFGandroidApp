package com.example.tfgapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
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
import org.w3c.dom.Text;

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

        holder.concertName.setText(rating.getConcertName());

        double ratingStars = rating.getRate();
        if (ratingStars == -1){
            holder.ratingBar.setVisibility(View.GONE);
            holder.noRatedYet.setVisibility(View.VISIBLE);
        } else {
            holder.ratingBar.setRating((float) ratingStars);
            holder.noRatedYet.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return ratingArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView concertCover;
        private RatingBar ratingBar;
        private TextView concertName;
        private TextView noRatedYet;

        public OnRatingListener onRatingListener;

        public ViewHolder(@NotNull View view, OnRatingListener onRatingListener) {
            super(view);

            this.concertCover = (ImageView) view.findViewById(R.id.concert_cover);
            this.ratingBar = view.findViewById(R.id.rating);

            this.concertName = view.findViewById(R.id.concert_name);
            this.onRatingListener = onRatingListener;
            this.noRatedYet = view.findViewById(R.id.concert_rate_now_text);
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