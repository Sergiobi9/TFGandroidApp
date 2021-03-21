package com.example.tfgapp.Activities.Concert;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.example.tfgapp.Activities.Concert.Fragment.ConcertCoverFragment;
import com.example.tfgapp.Entities.Concert.Concert;
import com.example.tfgapp.Entities.Concert.ConcertLocation;
import com.example.tfgapp.Global.Storage;
import com.example.tfgapp.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class CreateConcertActivity extends AppCompatActivity {

    private static Concert registeredConcert;
    private static ConcertLocation registeredConcertLocation;
    private static final String TAG = "CreateConcertActivity";
    private static AmazonS3 s3;
    private static TransferUtility transferUtility;
    private static ArrayList<Uri> concertImagesArrayList = new ArrayList<>();
    private static Uri coverImage = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_create_concert);

        if (Build.VERSION.SDK_INT == 26) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        registeredConcert = new Concert();
        registeredConcertLocation = new ConcertLocation();

        createCredentialsProviders();
        setTransferUtility();

        getSupportFragmentManager().beginTransaction().replace(R.id.concert_fragment, new ConcertCoverFragment()).commit();
    }

    public static void setConcertImagesArrayList(ArrayList<Uri> newConcertImagesArrayList) {
        concertImagesArrayList = newConcertImagesArrayList;
    }

    public static Uri getCoverImage() {
        return coverImage;
    }

    public static void setCoverImage(Uri coverImageUri) {
        coverImage = coverImageUri;
    }

    public static Concert getRegisteredConcert() {
        return registeredConcert;
    }

    public static void setRegisteredConcert(Concert concert) {
        registeredConcert = concert;
        Log.d(TAG, registeredConcert.toString());
    }

    public static ConcertLocation getRegisteredConcertLocation() {
        return registeredConcertLocation;
    }

    public static void setRegisteredConcertLocation(ConcertLocation concertLocation) {
        registeredConcertLocation = concertLocation;
        Log.d(TAG, registeredConcertLocation.toString());
    }

    private void createCredentialsProviders(){
        CognitoCachingCredentialsProvider cognitoCachingCredentialsProvider = new CognitoCachingCredentialsProvider(getApplicationContext(),
                "us-east-2:973c9e9f-c633-448d-8e99-91b3d61ad7d2", // ID del grupo de identidades
                Regions.US_EAST_2);

        setAmazonS3Client(cognitoCachingCredentialsProvider);
    }

    private void setAmazonS3Client(CognitoCachingCredentialsProvider cognitoCachingCredentialsProvider){
        s3 = new AmazonS3Client(cognitoCachingCredentialsProvider);
        s3.setRegion(Region.getRegion(Regions.US_EAST_2));
    }

    private void setTransferUtility(){
        transferUtility = TransferUtility.builder().s3Client(s3).context(getApplicationContext()).build();
    }

    private static void uploadPlaceImagesToAWS(String concertId, int photoNumber, File fileToUpload){
        TransferObserver transferObserver = transferUtility.upload(
                Storage.AWS_CONCERT_IMAGES_BUCKET,
                concertId + "_" + photoNumber + ".png",
                fileToUpload
        );
        transferObserverListener(transferObserver);
    }

    private static void transferObserverListener(TransferObserver transferObserver){

        transferObserver.setTransferListener(new TransferListener() {
            @Override
            public void onStateChanged(int id, TransferState state) {

            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                Log.d(TAG, "percentage " + (int)(bytesCurrent/bytesTotal * 100));
            }

            @Override
            public void onError(int id, Exception ex) {
                Log.d(TAG, "exception " + ex.getLocalizedMessage());
            }
        });
    }

    public static void createConcert(Context context){
        String concertId = "whola";
        convertConcertPlaceImagesUriToFile(context, concertId);
        convertCoverUriToFile(context, concertId);
    }

    private static void convertCoverUriToFile(Context context, String concertId){
        File coverFileImage = new File(coverImage.getPath());//create path from uri
        uploadCoverToAWS(concertId, coverFileImage);
    }

    private static void uploadCoverToAWS(String concertId, File coverFileImage){
        TransferObserver transferObserver = transferUtility.upload(
                Storage.AWS_CONCERT_IMAGES_BUCKET,
                concertId + "_cover" + ".png",
                coverFileImage
        );
        transferObserverListener(transferObserver);
    }


    private static void convertConcertPlaceImagesUriToFile(Context context, String concertId){
        for (int i = 0; i < concertImagesArrayList.size(); i++){
            Uri fileUri = concertImagesArrayList.get(i);
            File photoFile = null;
            try { photoFile = getFile(context, fileUri); }
            catch (IOException e) { e.printStackTrace(); }

            uploadPlaceImagesToAWS(concertId, i, photoFile);
        }
    }

    private static File getFile(Context context, Uri uri) throws IOException {
        File destinationFilename = new File(context.getFilesDir().getPath() + File.separatorChar + queryName(context, uri));
        try (InputStream ins = context.getContentResolver().openInputStream(uri)) {
            createFileFromStream(ins, destinationFilename);
        } catch (Exception ex) {
            Log.e("Save File", ex.getMessage());
            ex.printStackTrace();
        }
        return destinationFilename;
    }

    public static void createFileFromStream(InputStream ins, File destination) {
        try (OutputStream os = new FileOutputStream(destination)) {
            byte[] buffer = new byte[4096];
            int length;
            while ((length = ins.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
            os.flush();
        } catch (Exception ex) {
            Log.e("Save File", ex.getMessage());
            ex.printStackTrace();
        }
    }

    private static String queryName(Context context, Uri uri) {
        Cursor returnCursor =
                context.getContentResolver().query(uri, null, null, null, null);

        assert returnCursor != null;
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        String name = returnCursor.getString(nameIndex);
        returnCursor.close();

        return name;
    }

}