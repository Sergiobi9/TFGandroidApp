package com.example.tfgapp.Activities.Concert.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.tfgapp.Activities.Concert.CreateConcertActivity;
import com.example.tfgapp.Entities.Concert.Concert;
import com.example.tfgapp.R;

public class ConcertPriceFragment extends Fragment {

    private View view;
    private EditText concertPrice;
    private Button nextBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_concert_price, container, false);
        initView();
        return view;
    }

    private void initView(){
        concertPrice = view.findViewById(R.id.concert_price);
        nextBtn = view.findViewById(R.id.concert_price_next_btn);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String concertPriceText = concertPrice.getText().toString();

                double concertPriceVal = 0;
                if (concertPriceText != null && !concertPriceText.isEmpty())
                    concertPriceVal = Double.parseDouble(concertPrice.getText().toString());

                setConcertPrice(concertPriceVal);
                getFragmentManager().beginTransaction().replace(R.id.concert_fragment, new ConcertAssistanceFragment()).addToBackStack(null).commit();
            }
        });
    }

    private void setConcertPrice(double concertPriceVal){
        Concert concert = CreateConcertActivity.getRegisteredConcert();
        CreateConcertActivity.setRegisteredConcert(concert);
    }
}