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
import androidx.recyclerview.widget.RecyclerView;

import com.example.tfgapp.Entities.Artist.ArtistSimplified;
import com.example.tfgapp.Fragments.Artist.ArtistFragment;
import com.example.tfgapp.Global.CircleTransform;
import com.example.tfgapp.R;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ArtistFollowingAdapter extends RecyclerView.Adapter<ArtistFollowingAdapter.ViewHolder> {

    private ArrayList<ArtistSimplified> artistSimplifiedArrayList;
    private final String TAG = "ArtistFollowingAdapter";
    private Context context;

    public ArtistFollowingAdapter(Context context, ArrayList<ArtistSimplified> artistSimplifiedArrayList) {
        this.context = context;
        this.artistSimplifiedArrayList = artistSimplifiedArrayList;
    }

    @NonNull
    @Override
    public ArtistFollowingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        return new ArtistFollowingAdapter.ViewHolder(inflater.inflate(R.layout.following_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistFollowingAdapter.ViewHolder holder, int position) {
        ArtistSimplified currentArtist = artistSimplifiedArrayList.get(position);

        String imageUrl = currentArtist.getProfileUrl();

        Picasso.get().load(imageUrl).transform(new CircleTransform()).into(holder.image);

        TextView artistName = (TextView) holder.artistName;
        artistName.setText(currentArtist.getArtistName());

    }

    @Override
    public int getItemCount() {
        return artistSimplifiedArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView image;
        private TextView artistName;
        private LinearLayout container;

        public ViewHolder(@NotNull View view) {
            super(view);

            this.image = (ImageView) view.findViewById(R.id.image);
            this.artistName = (TextView) view.findViewById(R.id.name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }

    public interface OnArtistListener {
        void onArtistClicked(int position);
    }
}