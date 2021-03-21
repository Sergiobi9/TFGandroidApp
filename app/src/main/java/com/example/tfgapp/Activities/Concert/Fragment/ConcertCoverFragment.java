package com.example.tfgapp.Activities.Concert.Fragment;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.tfgapp.Activities.Concert.CreateConcertActivity;
import com.example.tfgapp.Global.Globals;
import com.example.tfgapp.Global.Permissions;
import com.example.tfgapp.R;
import com.yalantis.ucrop.UCrop;

import java.io.File;

import static android.app.Activity.RESULT_OK;
import static android.provider.OpenableColumns.DISPLAY_NAME;

public class ConcertCoverFragment extends Fragment {

    private View view;
    private Context context;
    private LinearLayout imagePicker, photosLayout;
    private TextView addNewPhoto, coverTopText;
    private Button nextBtn;

    private final int RESULT_CANCELED = 0, GALLERY = 1;
    private final String TAG = "ConcertCoverFragment";


    public ConcertCoverFragment() {
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
        view = inflater.inflate(R.layout.fragment_concert_cover, container, false);
        context = getContext();

        initView();
        return view;
    }

    private void initView(){
        photosLayout = view.findViewById(R.id.cover_photos_layout);
        imagePicker = view.findViewById(R.id.cover_picker);

        imagePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Permissions.checkMediaPermission(context)){
                    Permissions.requestMediaPermission(getActivity());
                } else {
                    selectPhoto();
                }
            }
        });

        addNewPhoto = view.findViewById(R.id.add_new_photo);
        addNewPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Permissions.checkMediaPermission(context)){
                    Permissions.requestMediaPermission(getActivity());
                } else {
                    selectPhoto();
                }
            }
        });

        coverTopText = view.findViewById(R.id.cover_top_tv);
        String concertName = CreateConcertActivity.getRegisteredConcert().getName();
        String concertDateText = "Sube una imagen cover para '" + concertName + "'";
        coverTopText.setText(concertDateText);

        nextBtn = view.findViewById(R.id.register_cover_btn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri coverUri = CreateConcertActivity.getCoverImage();

                if (coverUri == null){
                    Globals.displayShortToast(context, "Por favor, selecciona una imagen cover para tu concierto");
                }

                CreateConcertActivity.createConcert(context);
            }
        });
    }

    private void selectPhoto(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), GALLERY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_CANCELED){
            return;
        }
        else if (requestCode == GALLERY && resultCode == RESULT_OK){
            Log.d(TAG, "Selecting image");
            if (data != null){
                Uri contentUri = data.getData();

                if (contentUri != null){
                    cropImage(contentUri);
                }
            }
        }
        else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK){
            Log.d(TAG, "Crop retrieved");
            if (data != null){
                Uri imageUriResultCrop = UCrop.getOutput(data);

                if (imageUriResultCrop != null){
                    imagePicker.setVisibility(View.GONE);
                    photosLayout.setAlpha(1.0f);
                    int width  = photosLayout.getMeasuredWidth();

                    ImageView imageView = view.findViewById(R.id.concert_cover_image);
                    CardView cardView = view.findViewById(R.id.concert_cover_card_view);

                    imageView.setVisibility(View.VISIBLE);
                    cardView.setVisibility(View.VISIBLE);

                    imageView.setImageURI(imageUriResultCrop);
                    imageView.getLayoutParams().width = (int) (width);
                    imageView.getLayoutParams().height = (int) (width/1.5);
                    imageView.requestLayout();
                    CreateConcertActivity.setCoverImage(imageUriResultCrop);

                }
            }
        }
    }

    private void cropImage(Uri sourceUri){
        Uri destinationUri = Uri.fromFile(new File(context.getCacheDir(), queryName(context.getContentResolver(), sourceUri)));
        UCrop.Options options = new UCrop.Options();
        options.setHideBottomControls(true);
        options.setFreeStyleCropEnabled(false);
        options.setToolbarColor(ContextCompat.getColor(context,R.color.white));
        options.setStatusBarColor(ContextCompat.getColor(context,R.color.black));

        options.withAspectRatio(16,9);
        options.withMaxResultSize(1080,720);

        UCrop.of(sourceUri, destinationUri)
                .withOptions(options)
                .start(getActivity(), ConcertCoverFragment.this);
    }

    private static String queryName(ContentResolver resolver, Uri uri){
        Cursor returnCursor = resolver.query(uri, null, null, null, null);
        assert returnCursor != null;

        int nameIndex = returnCursor.getColumnIndex(DISPLAY_NAME);
        returnCursor.moveToFirst();
        String name = returnCursor.getString(nameIndex);
        returnCursor.close();
        return name;
    }
}