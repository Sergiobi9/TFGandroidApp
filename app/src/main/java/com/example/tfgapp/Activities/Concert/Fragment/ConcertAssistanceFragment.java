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

public class ConcertAssistanceFragment extends Fragment {

    private View view;
    private EditText concertAssistance;
    private Button nextBtn;

    public ConcertAssistanceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_concert_assistance, container, false);
        initView();
        return view;
    }

    private void initView(){
        concertAssistance = view.findViewById(R.id.concert_assistance);

        nextBtn = view.findViewById(R.id.assistance_next_btn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int concertAssistanceVal = Integer.parseInt(concertAssistance.getText().toString());

                setConcertAssistance(concertAssistanceVal);
                getFragmentManager().beginTransaction().replace(R.id.concert_fragment, new ConcertImagesFragment()).addToBackStack(null).commit();
            }
        });
    }

    private void setConcertAssistance(int concertAssistanceVal){
        Concert concert = CreateConcertActivity.getRegisteredConcert();
        concert.setNumberAssistants(concertAssistanceVal);
        CreateConcertActivity.setRegisteredConcert(concert);
    }
}