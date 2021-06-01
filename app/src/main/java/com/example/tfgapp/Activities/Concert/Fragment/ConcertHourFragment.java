package com.example.tfgapp.Activities.Concert.Fragment;

import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.tfgapp.Activities.Concert.CreateConcertActivity;
import com.example.tfgapp.Entities.Concert.Concert;
import com.example.tfgapp.Global.Helpers;
import com.example.tfgapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ConcertHourFragment extends Fragment {

    private View view;
    private TextView concertHourTextView;
    private TimePicker timePicker;
    private Button nextBtn;

    private String concertDate;

    public ConcertHourFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_concert_hour, container, false);
        initView();
        return view;
    }

    private void initView(){
        concertHourTextView = view.findViewById(R.id.concert_hour_tv);

        String concertName = CreateConcertActivity.getRegisteredConcert().getName();
        String concertHourText = "¿Que hora se va a dar lugar '" + concertName + "'?";

        concertHourTextView.setText(concertHourText);

        concertDate = CreateConcertActivity.getRegisteredConcert().getDateStarts();

        timePicker = view.findViewById(R.id.concert_time_picker);
        Calendar concertDateAsCalendar = Helpers.getDateAsCalendar(concertDate);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePicker.setHour(concertDateAsCalendar.get(Calendar.HOUR_OF_DAY));
            timePicker.setMinute(concertDateAsCalendar.get(Calendar.MINUTE));
            timePicker.setIs24HourView(true);
        }

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                Date date = null;
                try {
                    date = Helpers.timePattern.parse(concertDate);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute);
                    calendar.set(Calendar.MILLISECOND, 0);
                    calendar.set(Calendar.SECOND, 0);
                    concertDate = Helpers.getDateWithPattern(calendar.getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        nextBtn = view.findViewById(R.id.concert_hour_next_btn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setConcertHour();
                getFragmentManager().beginTransaction().replace(R.id.concert_fragment, new ConcertIntervalPricingFragment()).addToBackStack(null).commit();
            }
        });
    }

    private void setConcertHour(){
        Concert concert = CreateConcertActivity.getRegisteredConcert();
        concert.setDateStarts(concertDate);
        CreateConcertActivity.setRegisteredConcert(concert);
    }
}