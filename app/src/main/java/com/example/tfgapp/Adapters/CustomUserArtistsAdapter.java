package com.example.tfgapp.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tfgapp.Entities.Artist.ArtistReducedInfo;
import com.example.tfgapp.Entities.Artist.ArtistUserRegisterSelection;
import com.example.tfgapp.Entities.CustomUserLikes.MusicStyle;
import com.example.tfgapp.Global.CircleTransform;
import com.example.tfgapp.R;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CustomUserArtistsAdapter extends RecyclerView.Adapter<CustomUserArtistsAdapter.ViewHolder> {

    private ArrayList<ArtistUserRegisterSelection> artistUserRegisterSelectionArrayList;
    private final String TAG = "CustomUserArtistsAdapter";
    private Context context;
    private CustomUserArtistsAdapter.OnArtistListener onArtistListener;

    public CustomUserArtistsAdapter(Context context, ArrayList<ArtistUserRegisterSelection> artistUserRegisterSelectionArrayList, OnArtistListener onArtistListener) {
        this.context = context;
        this.onArtistListener = onArtistListener;
        this.artistUserRegisterSelectionArrayList = artistUserRegisterSelectionArrayList;
    }

    @NonNull
    @Override
    public CustomUserArtistsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        return new CustomUserArtistsAdapter.ViewHolder(inflater.inflate(R.layout.custom_likes_register_list, parent, false), onArtistListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomUserArtistsAdapter.ViewHolder holder, int position) {
        ArtistUserRegisterSelection currentArtist = artistUserRegisterSelectionArrayList.get(position);

        String imageUrl = currentArtist.getProfileUrl();

        Picasso.get().load(imageUrl).transform(new CircleTransform()).into(holder.image);

        TextView musicStyleName = (TextView) holder.musicStyleName;
        ImageView musicStyleCover = (ImageView) holder.image;

        musicStyleName.setText(currentArtist.getArtistName());

        boolean isSelected = currentArtist.isSelected();

        if (isSelected){
            musicStyleCover.setAlpha(0.5f);
            musicStyleName.setTypeface(null, Typeface.BOLD);
        } else {
            musicStyleCover.setAlpha(1.0f);
            musicStyleName.setTypeface(null, Typeface.NORMAL);
        }
    }

    @Override
    public int getItemCount() {
        return artistUserRegisterSelectionArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView image;
        private TextView musicStyleName;
        public CustomUserArtistsAdapter.OnArtistListener onArtistListener;

        public ViewHolder(@NotNull View view, CustomUserArtistsAdapter.OnArtistListener onArtistListener) {
            super(view);

            this.image = (ImageView) view.findViewById(R.id.image);
            this.musicStyleName = (TextView) view.findViewById(R.id.name);
            this.onArtistListener = onArtistListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onArtistListener.onArtistClicked(getAdapterPosition());
        }
    }

    public interface OnArtistListener {
        void onArtistClicked(int position);
    }
}