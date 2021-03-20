package com.example.tfgapp.Activities.Concert.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.tfgapp.Activities.Concert.CreateConcertActivity;
import com.example.tfgapp.Entities.Concert.Concert;
import com.example.tfgapp.Global.Globals;
import com.example.tfgapp.R;

public class ConcertNameFragment extends Fragment {

    private View view;
    private Context context;
    private EditText concertName;
    private Button nextBtn;

    public ConcertNameFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_concert_name, container, false);
        context = getContext();

        initView();
        return view;
    }

    private void initView(){
        concertName = view.findViewById(R.id.register_concert_name);
        nextBtn = view.findViewById(R.id.register_email_btn);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String concertNameStr = concertName.getText().toString();

                if (concertNameStr.isEmpty()) {
                    Globals.displayShortToast(context, "Por favor, pon nombre a tu concierto");
                    return;
                }

                setConcertName(concertNameStr);
                getFragmentManager().beginTransaction().replace(R.id.concert_fragment, new ConcertDateFragment()).addToBackStack(null).commit();
            }
        });
    }

    private void setConcertName(String concertNameStr){
        Concert concert = CreateConcertActivity.getRegisteredConcert();
        concert.setName(concertNameStr);
        CreateConcertActivity.setRegisteredConcert(concert);
    }
}