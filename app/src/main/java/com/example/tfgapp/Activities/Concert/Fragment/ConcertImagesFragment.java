package com.example.tfgapp.Activities.Concert.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tfgapp.Global.Globals;
import com.example.tfgapp.Global.Helpers;
import com.example.tfgapp.Global.Permissions;
import com.example.tfgapp.Global.Utils;
import com.example.tfgapp.R;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class ConcertImagesFragment extends Fragment {

    private View view;
    private Context context;
    private LinearLayout imagePicker, photosLayout;
    private TextView addMorePhotos;
    private Button nextButton;

    private final int PICK_IMAGE_MULTIPLE = 1;
    private ArrayList<ImageView> concertImages;
    private ArrayList<CardView> concertCardViews;


    public ConcertImagesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_concert_images, container, false);
        context = getContext();
        initView();
        return view;
    }

    private void initView(){
        imagePicker = view.findViewById(R.id.image_picker_layout);
        photosLayout = view.findViewById(R.id.photos_layout);

        concertImages = new ArrayList<>();
        concertImages.add(view.findViewById(R.id.concert_image_one));
        concertImages.add(view.findViewById(R.id.concert_image_two));
        concertImages.add(view.findViewById(R.id.concert_image_three));
        concertImages.add(view.findViewById(R.id.concert_image_four));

        concertCardViews = new ArrayList<>();
        concertCardViews.add(view.findViewById(R.id.concert_card_view_one));
        concertCardViews.add(view.findViewById(R.id.concert_card_view_two));
        concertCardViews.add(view.findViewById(R.id.concert_card_view_three));
        concertCardViews.add(view.findViewById(R.id.concert_card_view_four));

        imagePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Permissions.checkMediaPermission(context)){
                    Permissions.requestMediaPermission(getActivity());
                } else {
                    selectMultiplePhotos();
                }
            }
        });

        addMorePhotos = view.findViewById(R.id.add_more_photos_tv);
        addMorePhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Permissions.checkMediaPermission(context)){
                    Permissions.requestMediaPermission(getActivity());
                } else {
                    selectMultiplePhotos();
                }
            }
        });

        nextButton = view.findViewById(R.id.register_photos_next_btn);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setImagesToUpload();
                getFragmentManager().beginTransaction().replace(R.id.concert_fragment, new ConcertDescriptionFragment()).addToBackStack(null).commit();
            }
        });
    }

    private void selectMultiplePhotos(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE_MULTIPLE);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if(requestCode == PICK_IMAGE_MULTIPLE){
                imagePicker.setVisibility(View.GONE);
                photosLayout.setAlpha(1.0f);
                int width  = photosLayout.getMeasuredWidth();

                if (data.getClipData() != null) {
                    for (int i = 0; i < concertCardViews.size(); i++){
                        concertImages.get(i).setVisibility(View.GONE);
                        concertCardViews.get(i).setVisibility(View.GONE);
                    }

                    int photosSize = data.getClipData().getItemCount();
                    if (photosSize > 4) photosSize = 4;

                    for (int i = 0; i < photosSize; i++) {
                        Uri imageURI = data.getClipData().getItemAt(i).getUri();
                        ImageView imageView = getImageViewPhoto(i);
                        CardView cardView = getCardViewPhoto(i);

                        imageView.setVisibility(View.VISIBLE);
                        cardView.setVisibility(View.VISIBLE);

                        imageView.setImageURI(imageURI);
                        imageView.getLayoutParams().width = (int) (width/2.2);
                        imageView.getLayoutParams().height = (int) (width/2.2);
                        imageView.requestLayout();
                    }
                } else {
                    Uri onlyImage = data.getData();

                    if (onlyImage == null){
                        Globals.displayShortToast(context, "Por favor, selecciona almenos una foto");
                    } else {
                        for (int i = 0; i < concertCardViews.size(); i++){
                            concertImages.get(i).setVisibility(View.GONE);
                            concertCardViews.get(i).setVisibility(View.GONE);
                        }

                        ImageView imageView = getImageViewPhoto(0);
                        CardView cardView = getCardViewPhoto(0);

                        imageView.setVisibility(View.VISIBLE);
                        cardView.setVisibility(View.VISIBLE);

                        imageView.setImageURI(onlyImage);
                        imageView.getLayoutParams().width = (int) (width);
                        imageView.getLayoutParams().height = (int) (width);
                        imageView.requestLayout();
                    }

                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private ImageView getImageViewPhoto(int position){
        return concertImages.get(position);
    }

    private CardView getCardViewPhoto(int position){
        return concertCardViews.get(position);
    }

    private void setImagesToUpload(){

    }
}