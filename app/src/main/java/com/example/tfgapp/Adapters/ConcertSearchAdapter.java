package com.example.tfgapp.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tfgapp.Entities.Artist.ArtistInfo;
import com.example.tfgapp.Entities.Artist.ArtistSimplified;
import com.example.tfgapp.Entities.Concert.ConcertReduced;
import com.example.tfgapp.Fragments.Navigation.User.ConcertInfoFragment;
import com.example.tfgapp.Global.CircleTransform;
import com.example.tfgapp.Global.Helpers;
import com.example.tfgapp.Global.Utils;
import com.example.tfgapp.R;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

public class ConcertSearchAdapter extends RecyclerView.Adapter<ConcertSearchAdapter.ViewHolder> {

    private ArrayList<ConcertReduced> concertsArrayList;
    private ArrayList<ConcertReduced> concertsInitArrayList = new ArrayList<>();
    private final String TAG = "ConcertSearchAdapter";
    private Context context;
    private Activity activity;
    private OnConcertListener onConcertListener;

    public ConcertSearchAdapter(Context context, ArrayList<ConcertReduced> concertsArrayList, OnConcertListener onConcertListener, Activity activity) {
        this.context = context;
        this.onConcertListener = onConcertListener;
        this.concertsArrayList = concertsArrayList;
        this.concertsInitArrayList.addAll(concertsArrayList);
        this.activity = activity;
    }

    @NonNull
    @Override
    public ConcertSearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        return new ConcertSearchAdapter.ViewHolder(inflater.inflate(R.layout.search_concert_list, parent, false), onConcertListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ConcertSearchAdapter.ViewHolder holder, int position) {
        ConcertReduced currentConcert = concertsArrayList.get(position);

        String imageUrl = currentConcert.getConcertCoverImage();
        ImageView concertCover = holder.concertImage;
        Utils.responsiveView(concertCover, 1, 0.5, activity);
        Picasso.get().load(imageUrl).into(holder.concertImage);

        TextView concertName = (TextView) holder.concertName;
        TextView concertArtists = (TextView) holder.concertArtists;
        LinearLayout concertInfoLayout = (LinearLayout) holder.concertInfoLayout;

        Utils.responsiveViewWidth(concertInfoLayout, 0.55, activity);


        Calendar concertDate = Helpers.getDateAsCalendar(currentConcert.getDateStarts());
        TextView concertDay = (TextView) holder.concertDay;
        TextView concertYear = (TextView) holder.concertYear;

        concertName.setText(currentConcert.getName());

        String concertArtistsStr = getCurrentArtist(currentConcert.getArtists());
        concertArtists.setText(concertArtistsStr);

        String concertDayStr = Utils.getMonthSimplified(concertDate.get(Calendar.MONTH)) + " " + concertDate.get(Calendar.DATE);
        concertDay.setText(concertDayStr);

        String concertYearStr = String.valueOf(concertDate.get(Calendar.YEAR));
        concertYear.setText(concertYearStr);
    }

    private String getCurrentArtist(ArrayList<ArtistInfo> artists) {
        String artistsToReturn = "";
        for (int i = 0; i < artists.size(); i++){
            String artistName = artists.get(i).getName();
            if (i == 0){
                artistsToReturn = artistsToReturn + artistName;
            } else if (i == 1){
                artistsToReturn = artistsToReturn + " ft " + artistName;
            } else {
                artistsToReturn = artistsToReturn + ", " + artistName;
            }
        }
        return artistsToReturn;
    }

    public void filter(String searchStr){
        concertsArrayList.clear();
        if (searchStr.isEmpty()){
            concertsArrayList.addAll(concertsInitArrayList);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                List<ConcertReduced> collected = concertsInitArrayList.stream().
                        filter(i -> i.getName().toLowerCase().contains(searchStr))
                        .collect(Collectors.toList());
                concertsArrayList.addAll(collected);
            } else {
                for (ConcertReduced concertReduced : concertsInitArrayList){
                    if (concertReduced.getName().toLowerCase().contains(searchStr)){
                        concertsArrayList.add(concertReduced);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return concertsArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView concertImage;
        private TextView concertName, concertArtists, concertDay, concertYear;
        private LinearLayout concertInfoLayout;
        public OnConcertListener onConcertListener;

        public ViewHolder(@NotNull View view, OnConcertListener onConcertListener) {
            super(view);

            this.concertImage = (ImageView) view.findViewById(R.id.concert_image);
            this.concertName = (TextView) view.findViewById(R.id.concert_name);
            this.concertArtists = (TextView) view.findViewById(R.id.concert_artists);
            this.concertDay = (TextView) view.findViewById(R.id.concert_day);
            this.concertYear = (TextView) view.findViewById(R.id.concert_year);
            this.onConcertListener = onConcertListener;
            this.concertInfoLayout = view.findViewById(R.id.concert_info_layout);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onConcertListener.onConcertClicked(getAdapterPosition());
        }
    }

    public interface OnConcertListener {
        void onConcertClicked(int position);
    }
}