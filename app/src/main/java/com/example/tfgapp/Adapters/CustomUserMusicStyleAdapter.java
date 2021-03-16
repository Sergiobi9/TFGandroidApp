package com.example.tfgapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tfgapp.Entities.CustomUserLikes.MusicStyle;
import com.example.tfgapp.Global.CircleTransform;
import com.example.tfgapp.R;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CustomUserMusicStyleAdapter extends RecyclerView.Adapter<CustomUserMusicStyleAdapter.ViewHolder> {

    private ArrayList<MusicStyle> musicStyleArrayList;
    private final String TAG = "CustomUserLikesAdapter";
    private Context context;
    private CustomUserMusicStyleAdapter.OnMusicStyleListener onMusicStyleListener;

    public CustomUserMusicStyleAdapter(Context context, ArrayList<MusicStyle> musicStyleArrayList, OnMusicStyleListener onMusicStyleListener) {
        this.context = context;
        this.onMusicStyleListener = onMusicStyleListener;
        this.musicStyleArrayList = musicStyleArrayList;
    }

    @NonNull
    @Override
    public CustomUserMusicStyleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        return new CustomUserMusicStyleAdapter.ViewHolder(inflater.inflate(R.layout.custom_likes_register_list, parent, false), onMusicStyleListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomUserMusicStyleAdapter.ViewHolder holder, int position) {
        MusicStyle currentMusicStyle = musicStyleArrayList.get(position);

        String imageUrl = currentMusicStyle.getImageUrl();

        Picasso.get().load(imageUrl).transform(new CircleTransform()).into(holder.image);

        holder.musicStyleName.setText(currentMusicStyle.getName());

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isSelected = currentMusicStyle.isSelected();

                if (isSelected)
                    holder.image.setAlpha(0.5f);
                else
                    holder.image.setAlpha(1.0f);
            }
        });
    }

    @Override
    public int getItemCount() {
        return musicStyleArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView image;
        private TextView musicStyleName;
        public CustomUserMusicStyleAdapter.OnMusicStyleListener onMusicStyleListener;

        public ViewHolder(@NotNull View view, CustomUserMusicStyleAdapter.OnMusicStyleListener onMusicStyleListener) {
            super(view);

            this.image = (ImageView) view.findViewById(R.id.image);
            this.musicStyleName = (TextView) view.findViewById(R.id.name);
            this.onMusicStyleListener = onMusicStyleListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onMusicStyleListener.onMusicStyleClicked(getAdapterPosition());
        }
    }

    public interface OnMusicStyleListener {
        void onMusicStyleClicked(int position);
    }
}