package com.example.tfgapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tfgapp.Entities.CustomUserLikes.MusicStyle;
import com.example.tfgapp.Global.CircleTransform;
import com.example.tfgapp.R;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MusicStyleFollowingAdapter extends RecyclerView.Adapter<MusicStyleFollowingAdapter.ViewHolder> {

    private ArrayList<MusicStyle> musicStyleArrayList;
    private final String TAG = "MusicStyleFollowingAdapter";
    private Context context;

    public MusicStyleFollowingAdapter(Context context, ArrayList<MusicStyle> musicStyleArrayList) {
        this.context = context;
        this.musicStyleArrayList = musicStyleArrayList;
    }

    @NonNull
    @Override
    public MusicStyleFollowingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        return new MusicStyleFollowingAdapter.ViewHolder(inflater.inflate(R.layout.following_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MusicStyleFollowingAdapter.ViewHolder holder, int position) {
        MusicStyle musicStyle = musicStyleArrayList.get(position);

        String imageUrl = musicStyle.getImageUrl();

        Picasso.get().load(imageUrl).transform(new CircleTransform()).into(holder.image);

        TextView artistName = (TextView) holder.artistName;
        artistName.setText(musicStyle.getName());

    }

    @Override
    public int getItemCount() {
        return musicStyleArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView image;
        private TextView artistName;

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

    public interface OnMusicStyleListener {
        void onMusicStyleClicked(int position);
    }
}