package com.example.tfgapp.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tfgapp.Entities.Artist.ArtistSimplified;
import com.example.tfgapp.Global.CircleTransform;
import com.example.tfgapp.R;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SelectArtistsAdapter extends RecyclerView.Adapter<SelectArtistsAdapter.ViewHolder> {

    private ArrayList<ArtistSimplified> artistSimplifiedArrayList;
    private ArrayList<ArtistSimplified> artistInitArrayList = new ArrayList<>();
    private final String TAG = "CustomUserArtistsAdapter";
    private Context context;
    private SelectArtistsAdapter.OnArtistListener onArtistListener;

    public SelectArtistsAdapter(Context context, ArrayList<ArtistSimplified> artistSimplifiedArrayList, OnArtistListener onArtistListener) {
        this.context = context;
        this.onArtistListener = onArtistListener;
        this.artistSimplifiedArrayList = artistSimplifiedArrayList;
        this.artistInitArrayList.addAll(artistSimplifiedArrayList);
    }

    @NonNull
    @Override
    public SelectArtistsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        return new SelectArtistsAdapter.ViewHolder(inflater.inflate(R.layout.custom_likes_register_list, parent, false), onArtistListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectArtistsAdapter.ViewHolder holder, int position) {
        ArtistSimplified currentArtist = artistSimplifiedArrayList.get(position);

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

    public void filter(String searchStr){
        artistSimplifiedArrayList.clear();
        if (searchStr.isEmpty()){
            artistSimplifiedArrayList.addAll(artistInitArrayList);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                List<ArtistSimplified> collected = artistInitArrayList.stream().
                        filter(i -> i.getArtistName().toLowerCase().contains(searchStr.toLowerCase()))
                        .collect(Collectors.toList());
                artistSimplifiedArrayList.addAll(collected);
            } else {
                for (ArtistSimplified artistSimplified : artistInitArrayList){
                    if (artistSimplified.getArtistName().toLowerCase().contains(searchStr.toLowerCase())){
                        artistSimplifiedArrayList.add(artistSimplified);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return artistSimplifiedArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView image;
        private TextView musicStyleName;
        public SelectArtistsAdapter.OnArtistListener onArtistListener;

        public ViewHolder(@NotNull View view, SelectArtistsAdapter.OnArtistListener onArtistListener) {
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