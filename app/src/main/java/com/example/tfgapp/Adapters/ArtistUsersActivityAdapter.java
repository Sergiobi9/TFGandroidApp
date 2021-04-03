package com.example.tfgapp.Adapters;

import android.app.Dialog;
import android.content.Context;
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
import com.example.tfgapp.Entities.Concert.ConcertActivity;
import com.example.tfgapp.Fragments.Artist.ArtistFragment;
import com.example.tfgapp.Global.CircleTransform;
import com.example.tfgapp.R;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ArtistUsersActivityAdapter extends RecyclerView.Adapter<ArtistUsersActivityAdapter.ViewHolder> {

    private List<ConcertActivity> concertActivityArrayList;
    private final String TAG = "ArtistUsersActivityAdapter";
    private Context context;

    public ArtistUsersActivityAdapter(Context context, List<ConcertActivity> concertActivityArrayList) {
        this.context = context;
        this.concertActivityArrayList = concertActivityArrayList;
    }

    @NonNull
    @Override
    public ArtistUsersActivityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        return new ArtistUsersActivityAdapter.ViewHolder(inflater.inflate(R.layout.home_users_activity_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistUsersActivityAdapter.ViewHolder holder, int position) {
        ConcertActivity concertActivity = concertActivityArrayList.get(position);

        TextView activityInfo = holder.userActivityInfo;
        String concertName = concertActivity.getConcertName();
        String userName = concertActivity.getUserName();

        String message = "";
        if (concertActivity.getActivityType().equals("BOOKING")){
            int numberOfTickets = concertActivity.getTicketsAmount();
            message = userName + " ha comprado " + numberOfTickets + " entradas para el concierto '" + concertName + "'";
            activityInfo.setText(message);
        } else if (concertActivity.getActivityType().equals("RATING")){
            String comment = concertActivity.getUserComment();
            double rating = concertActivity.getUserRate();

            if (rating != -1 && comment.isEmpty()){
                message = userName + " ha valorado '" + concertName + "' con " + rating + " estrellas";
            } else if (rating != -1 && !comment.isEmpty()){
                message = userName + " ha comentado '" + comment + "' y ha valorado '" + concertName + "' con " + rating + " estrellas";
            }
        }

        activityInfo.setText(message);
        String firstNameLetterStr = String.valueOf(concertActivity.getUserName().charAt(0)).toUpperCase();
        holder.userFirstLetter.setText(firstNameLetterStr);
    }

    @Override
    public int getItemCount() {
        return concertActivityArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView userFirstLetter;
        private TextView userActivityInfo;
        private LinearLayout container;

        public ViewHolder(@NotNull View view) {
            super(view);

            this.userFirstLetter = (TextView) view.findViewById(R.id.user_first_name_letter);
            this.userActivityInfo = view.findViewById(R.id.activity_info);
            this.container = view.findViewById(R.id.container);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }

    public interface OnActivityListener {
        void onActivityClicked(int position);
    }
}