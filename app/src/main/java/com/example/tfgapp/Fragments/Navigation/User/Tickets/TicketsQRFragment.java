package com.example.tfgapp.Fragments.Navigation.User.Tickets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.tfgapp.Entities.Booking.Booking;
import com.example.tfgapp.Global.Helpers;
import com.example.tfgapp.Global.Utils;
import com.example.tfgapp.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.jama.carouselview.CarouselScrollListener;
import com.jama.carouselview.CarouselView;
import com.jama.carouselview.CarouselViewListener;
import com.jama.carouselview.enums.IndicatorAnimationType;
import com.jama.carouselview.enums.OffsetType;

import java.util.ArrayList;

public class TicketsQRFragment extends Fragment {

    private View view;
    private Context context;
    private CarouselView QRCarousel;

    public TicketsQRFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_tickets_q_r, container, false);
        context = getContext();

        ArrayList<Booking> bookings= (ArrayList<Booking>)getArguments().getSerializable("bookings");
        String concertName = this.getArguments().getString("concertName");
        String concertPlace = this.getArguments().getString("concertPlace");

        QRCarousel = view.findViewById(R.id.qr_carousel);

        showCarouselQR(bookings, concertName, concertPlace);

        return view;
    }

    private void showCarouselQR(ArrayList<Booking> bookings, String concertNameStr, String concertPlaceStr){
        QRCarousel.setSize(bookings.size());
        QRCarousel.setResource(R.layout.qr_tickets_carousel_view);
        QRCarousel.setAutoPlay(false);
        QRCarousel.setAutoPlayDelay(3000);
        QRCarousel.hideIndicator(true);
        QRCarousel.setIndicatorAnimationType(IndicatorAnimationType.THIN_WORM);
        QRCarousel.setCarouselOffset(OffsetType.CENTER);
        QRCarousel.setCarouselViewListener(new CarouselViewListener() {
            @Override
            public void onBindView(View view, final int position) {
                String bookingId = bookings.get(position).getId();

                TextView numBookingPosition = view.findViewById(R.id.ticket_number);
                numBookingPosition.setText("Entrada " + (position + 1) + " de " + bookings.size());

                TextView ticketId = view.findViewById(R.id.ticket_id);
                ticketId.setText(bookings.get(position).getId());

                TextView concertName = view.findViewById(R.id.ticket_concert_name);
                concertName.setText(concertNameStr);
                TextView concertPlace = view.findViewById(R.id.ticket_concert_place);
                concertPlace.setText(concertPlaceStr);

                ImageView qr = view.findViewById(R.id.qr);
                generateQR(qr, bookingId);
            }
        });
        QRCarousel.show();

        getActivity().overridePendingTransition(R.anim.left_to_right,
                R.anim.right_to_left);
    }

    private void generateQR(ImageView ticketQR, String id){
        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;

        int width = (int) (screenWidth * 0.85);
        int height = (int) (screenWidth * 0.85);

        try{
            BitMatrix bitMatrix = qrCodeWriter.encode(id, BarcodeFormat.QR_CODE, width, height);
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

            for (int x = 0; x < width; x++){
                for (int y = 0; y < height; y++){
                    bitmap.setPixel(x, y, bitMatrix.get(x,y)? Color.BLACK : Color.WHITE);
                }
            }

            Utils.responsiveViewWidth(ticketQR, 1, getActivity());
            ticketQR.setImageBitmap(bitmap);

        } catch (Exception exception){
            exception.printStackTrace();
        }
    }
}