package com.example.tfgapp.Fragments.Navigation.User.Search;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.tfgapp.Adapters.ConcertsAdapter;
import com.example.tfgapp.Entities.Concert.ConcertReduced;
import com.example.tfgapp.Fragments.Navigation.User.ConcertInfoFragment;
import com.example.tfgapp.Global.Api;
import com.example.tfgapp.Global.Globals;
import com.example.tfgapp.Global.Helpers;
import com.example.tfgapp.R;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchConcertFragment extends Fragment implements ConcertsAdapter.OnConcertListener {

    private final String TAG = "SearchFragment";
    private View view;
    private Context context;
    private SearchView concertsSearch;

    private ArrayList<ConcertReduced> concertsReducedArrayList = new ArrayList<>();
    private RecyclerView concertsRecyclerView;
    private ConcertsAdapter concertsAdapter;
    private ConcertsAdapter.OnConcertListener onConcertListener;

    private String startDate;
    private String endDate;

    private EditText selectDate;
    private AVLoadingIndicatorView loading;

    public SearchConcertFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_concert_search, container, false);
        context = getContext();

        initView();
        return view;
    }

    private void initView(){
        onConcertListener = this;

        concertsRecyclerView = view.findViewById(R.id.searched_concerts_recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        concertsRecyclerView.setLayoutManager(mLayoutManager);
        concertsRecyclerView.setNestedScrollingEnabled(false);

        loading = view.findViewById(R.id.loading);

        concertsSearch = view.findViewById(R.id.concerts_search);
        concertsSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String arg0) {
                concertsAdapter.filter(arg0);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String arg0) {
                concertsAdapter.filter(arg0);
                return false;
            }
        });

        MaterialDatePicker.Builder<Pair<Long, Long>> materialDateBuilder = MaterialDatePicker.Builder.dateRangePicker();
        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();
        materialDatePicker.addOnPositiveButtonClickListener(
                new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
                    @Override
                    public void onPositiveButtonClick(Pair<Long,Long> selection) {

                        Long first = selection.first;
                        Long last = selection.second;

                        startDate = Helpers.getDateStartStringFromLong(first);
                        endDate = Helpers.getDateFinalStringFromLong(last);

                        // if the user clicks on the positive
                        // button that is ok button update the
                        // selected date
                        selectDate.setText(materialDatePicker.getHeaderText());
                        // in the above statement, getHeaderText
                        // will return selected date preview from the
                        // dialog

                        getConcerts();
                    }
                });

        selectDate = view.findViewById(R.id.select_date);
        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDatePicker.show(getFragmentManager(), "MATERIAL_DATE_PICKER");
            }
        });

        startDate = Helpers.getTimeStamp();
        endDate  = Helpers.addYearToTimestamp();

        getConcerts();

    }

    private void getConcerts(){
        loading.setVisibility(View.VISIBLE);
        Call<ArrayList<ConcertReduced>> call = Api.getInstance().getAPI().getAllConcertsActiveByCurrentDate(startDate, endDate, Helpers.getTimeStamp());
        call.enqueue(new Callback<ArrayList<ConcertReduced>>() {
            @Override
            public void onResponse(Call<ArrayList<ConcertReduced>> call, Response<ArrayList<ConcertReduced>> response) {
                switch (response.code()) {
                    case 200:
                        Log.d(TAG, "Get concerts for search success " + response.body());
                        concertsReducedArrayList = response.body();
                        initConcertsView();
                        loading.setVisibility(View.GONE);
                        break;
                    default:
                        Log.d(TAG, "Get concerts for search default " + response.code());
                        Globals.displayShortToast(context, "Algo ha pasado, prueba de nuevo en unos minutos");
                        break;
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ConcertReduced>> call, Throwable t) {
                Log.d(TAG, "Get concerts for search failure " + t.getLocalizedMessage());
                Globals.displayShortToast(context, "Algo ha pasado, prueba de nuevo en unos minutos");
            }
        });
    }

    private void initConcertsView(){
        concertsAdapter = new ConcertsAdapter(context, concertsReducedArrayList, onConcertListener, getActivity());
        concertsRecyclerView.setAdapter(concertsAdapter);
    }


    @Override
    public void onConcertClicked(int position) {
        /* Open fragment specific concert info */
        String concertId = concertsReducedArrayList.get(position).getConcertId();

        openConcertInfo(concertId);
    }

    private void openConcertInfo(String concertId){
        Bundle bundle = new Bundle();
        bundle.putString("concertId", concertId);
        ConcertInfoFragment concertInfoFragment = new ConcertInfoFragment();
        concertInfoFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.main_fragment, concertInfoFragment).addToBackStack(null).commit();
    }

}

