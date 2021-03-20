package com.example.tfgapp.Activities.Concert.Fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.tfgapp.Activities.Concert.CreateConcertActivity;
import com.example.tfgapp.Entities.Concert.Concert;
import com.example.tfgapp.Global.Helpers;
import com.example.tfgapp.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class ConcertDateFragment extends Fragment {

    private View view;
    private TextView concertDateTextView;
    private DatePicker concertDatePicker;
    private Button nextBtn;

    private String concertDate;

    public ConcertDateFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_concert_date, container, false);

        initView();

        return view;
    }

    private void initView() {
        concertDateTextView = view.findViewById(R.id.concert_date_tv);

        concertDate = CreateConcertActivity.getRegisteredConcert().getDateStarts();

        concertDatePicker = view.findViewById(R.id.concert_date_picker);
        concertDatePicker.setMinDate(new Date().getTime());

        if (concertDate == null) {
            concertDate = Helpers.getTimeStamp();
        }

        Calendar concertDateAsCalendar = Helpers.getDateAsCalendar(concertDate);
        concertDatePicker.updateDate(concertDateAsCalendar.get(Calendar.YEAR), concertDateAsCalendar.get(Calendar.MONTH), concertDateAsCalendar.get(Calendar.DATE));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            concertDatePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    Date date = null;
                    try {
                        date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(concertDate);
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date);
                        calendar.set(Calendar.DATE, dayOfMonth);// for 6 hour
                        calendar.set(Calendar.MONTH, monthOfYear);// for 0 min
                        calendar.set(Calendar.YEAR, year);// for 0 sec
                        concertDate = Helpers.getDateWithPattern(calendar.getTime());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
            });
        }

        String concertName = CreateConcertActivity.getRegisteredConcert().getName();
        String concertDateText = "¿Que día se va a dar lugar '" + concertName + "'?";

        concertDateTextView.setText(concertDateText);

        nextBtn = view.findViewById(R.id.concert_date_next_btn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setConcertDate();
                getFragmentManager().beginTransaction().replace(R.id.concert_fragment, new ConcertHourFragment()).addToBackStack(null).commit();
            }
        });
    }

    private void setConcertDate(){
        Concert concert = CreateConcertActivity.getRegisteredConcert();
        concert.setDateStarts(concertDate);
        CreateConcertActivity.setRegisteredConcert(concert);
    }
}