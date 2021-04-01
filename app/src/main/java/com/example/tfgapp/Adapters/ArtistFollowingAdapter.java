package com.example.tfgapp.Adapters;

import android.app.Activity;
import android.app.Dialog;
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
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tfgapp.Entities.Artist.ArtistSimplified;
import com.example.tfgapp.Fragments.Artist.ArtistFragment;
import com.example.tfgapp.Fragments.Navigation.Artist.Profile.ArtistProfileFragment;
import com.example.tfgapp.Fragments.Navigation.User.ConcertInfoFragment;
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
    private Dialog dialog;

    public ArtistFollowingAdapter(Context context, ArrayList<ArtistSimplified> artistSimplifiedArrayList, Dialog dialog) {
        this.context = context;
        this.artistSimplifiedArrayList = artistSimplifiedArrayList;
        this.dialog = dialog;
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

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                Bundle bundle = new Bundle();
                bundle.putString("artistId", currentArtist.getArtistId());
                ArtistFragment artistFragment = new ArtistFragment();
                artistFragment.setArguments(bundle);
                ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, artistFragment).commit();
            }
        });

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
            this.container = view.findViewById(R.id.container);
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