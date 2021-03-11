package com.example.tfgapp.Fragments.Navigation;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.tfgapp.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class TicketsFragment extends Fragment {

    private View view;
    private ImageView ticketQR;
    private int screenWidth;

    public TicketsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tickets, container, false);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;

        initView();

        return view;
    }

    private void initView(){
        ticketQR = view.findViewById(R.id.ticket_qr);
        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        try{
            BitMatrix bitMatrix = qrCodeWriter.encode("www.google.com", BarcodeFormat.QR_CODE, 150, 150);
            Bitmap bitmap = Bitmap.createBitmap(150, 150, Bitmap.Config.RGB_565);

            for (int x = 0; x < 150; x++){
                for (int y = 0; y < 150; y++){
                    bitmap.setPixel(x, y, bitMatrix.get(x,y)? Color.BLACK : Color.WHITE);
                }
            }

            ViewGroup.LayoutParams params = ticketQR.getLayoutParams();
            params.height = (int) (screenWidth * 0.5);
            params.width = (int) (screenWidth * 0.5);
            ticketQR.setLayoutParams(params);

            ticketQR.setImageBitmap(bitmap);

        } catch (Exception exception){
            exception.printStackTrace();
        }
    }
}