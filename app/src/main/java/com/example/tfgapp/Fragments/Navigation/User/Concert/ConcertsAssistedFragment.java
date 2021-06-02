package com.example.tfgapp.Fragments.Navigation.User.Concert;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.tfgapp.Adapters.RatingAdapter;
import com.example.tfgapp.Entities.Rating.RatingSimplified;
import com.example.tfgapp.Global.Api;
import com.example.tfgapp.Global.CurrentUser;
import com.example.tfgapp.Global.Helpers;
import com.example.tfgapp.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConcertsAssistedFragment extends Fragment implements RatingAdapter.OnRatingListener {

    private View view;
    private Context context;
    private final String TAG = "ConcertsAssistedFrag";

    private ArrayList<RatingSimplified> ratingArrayList = new ArrayList<>();
    private RecyclerView ratingsRecyclerView;
    private RatingAdapter ratingAdapter;
    private RatingAdapter.OnRatingListener onRatingListener;

    public ConcertsAssistedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_concerts_assisted, container, false);
        context = getContext();
        onRatingListener = this;

        initView();
        return view;
    }

    private void initView(){

        ratingsRecyclerView = view.findViewById(R.id.asisted_concerts_recicler_view);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        ratingsRecyclerView.setLayoutManager(mLayoutManager);
        ratingsRecyclerView.setNestedScrollingEnabled(false);

        getUserConcertRatings();
    }

    private void getUserConcertRatings(){
        String userId = CurrentUser.getInstance(context).getCurrentUser().getUser().getId();
        String currentDate = Helpers.getTimeStamp();
        Call<ArrayList<RatingSimplified>> call = Api.getInstance().getAPI().getUserConcertRatings(userId, currentDate);
        call.enqueue(new Callback<ArrayList<RatingSimplified>>() {
            @Override
            public void onResponse(Call<ArrayList<RatingSimplified>> call, Response<ArrayList<RatingSimplified>> response) {
                switch (response.code()) {
                    case 200:
                        Log.d(TAG, "Tickets concerts success " + response.body());
                        ratingArrayList = response.body();

                        if (ratingArrayList != null && ratingArrayList.size() > 0)
                            initRatingsList();
                        else {
                            view.findViewById(R.id.no_ratings_yet).setVisibility(View.VISIBLE);
                            ratingsRecyclerView.setVisibility(View.GONE);
                        }
                        break;
                    default:
                        Log.d(TAG, "Tickets concerts default " + response.code());
                        view.findViewById(R.id.no_ratings_yet).setVisibility(View.VISIBLE);
                        ratingsRecyclerView.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onFailure(Call<ArrayList<RatingSimplified>> call, Throwable t) {
                Log.d(TAG, "Tickets concerts failure " + t.getLocalizedMessage());
                view.findViewById(R.id.no_ratings_yet).setVisibility(View.VISIBLE);
                ratingsRecyclerView.setVisibility(View.GONE);
            }
        });
    }

    private void initRatingsList(){
        ratingAdapter = new RatingAdapter(context, ratingArrayList, onRatingListener);
        ratingsRecyclerView.setAdapter(ratingAdapter);
    }

    @Override
    public void onRatingClicked(int position) {
        showRatingDialog(position);
    }

    private void showRatingDialog(int position){
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_rate, null);
        dialog.setContentView(view);

        RatingSimplified ratingSimplified = ratingArrayList.get(position);

        TextView title = view.findViewById(R.id.title);

        String concertName = ratingSimplified.getConcertName();
        title.setText("Valora tu experiencia en " + concertName);

        double ratingStars = ratingSimplified.getRate();

        RatingBar ratingBar = view.findViewById(R.id.rating);
        if (ratingStars == -1){
            ratingBar.setRating(2);
        } else {
            ratingBar.setRating((float) ratingStars);
        }

        EditText comment = view.findViewById(R.id.comment);
        comment.setText(ratingSimplified.getComment());

        Animation alhpa = AnimationUtils.loadAnimation(context, R.anim.fade_in);

        RelativeLayout all = view.findViewById(R.id.body);
        all.startAnimation(alhpa);

        Button button = view.findViewById(R.id.next_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ratingSimplified.setComment(comment.getText().toString());
                ratingSimplified.setRate(ratingBar.getRating());
                publishRate(ratingSimplified, dialog, position);
            }
        });

        dialog.show();
    }

    private void publishRate(RatingSimplified rating, Dialog dialog, int positionToReplace){
        String currentDate = Helpers.getTimeStamp();
        Call<RatingSimplified> call = Api.getInstance().getAPI().updateUserConcertRating(currentDate, rating);
        call.enqueue(new Callback<RatingSimplified>() {
            @Override
            public void onResponse(Call<RatingSimplified> call, Response<RatingSimplified> response) {
                switch (response.code()) {
                    case 200:
                        Log.d(TAG, "Rating updated success " + response.body());

                        ratingArrayList.set(positionToReplace, rating);
                        ratingAdapter.notifyItemChanged(positionToReplace);
                        dialog.cancel();
                        break;
                    default:
                        Log.d(TAG, "Rating updated default " + response.code());
                        break;
                }
            }

            @Override
            public void onFailure(Call<RatingSimplified> call, Throwable t) {
                Log.d(TAG, "Rating updated failure " + t.getLocalizedMessage());
            }
        });
    }
}