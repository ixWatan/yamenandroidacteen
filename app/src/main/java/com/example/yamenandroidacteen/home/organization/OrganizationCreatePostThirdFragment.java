package com.example.yamenandroidacteen.home.organization;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.yamenandroidacteen.R;

import java.util.Calendar;


public class OrganizationCreatePostThirdFragment extends Fragment {


    Button selectStartTimeBtn, selectEndTimeBtn,selectDateBtn;

    ImageView backButton;
    TextView showStartTimeDataTv, showEndTimeDataTv, showDateDataTv;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_organization_create_post_third, container, false);

        selectStartTimeBtn = view.findViewById(R.id.selectStartTimeBtn);
        selectEndTimeBtn = view.findViewById(R.id.selectEndTimeBtn);
        selectDateBtn = view.findViewById(R.id.selectDateBtn);
        backButton = view.findViewById(R.id.backButtonThirdStep);


        showStartTimeDataTv = view.findViewById(R.id.showStartTimeDataTv);
        showEndTimeDataTv = view.findViewById(R.id.showEndTimeDataTv);
        showDateDataTv = view.findViewById(R.id.showDateDataTv);


        dateAndTimePickerInit();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getActivity().getSupportFragmentManager().popBackStack();
                } else {
                    navigateToFragment(new OrganizationCreatePostFragment());
                }
            }
        });


        return view;
    }

    private void dateAndTimePickerInit() {

        selectStartTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);
                // time picker dialog
                TimePickerDialog picker = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                String formattedHour = String.format("%02d", sHour);
                                String formattedMinute = String.format("%02d", sMinute);
                                showStartTimeDataTv.setText(formattedHour + ":" + formattedMinute);
                            }
                        }, hour, minutes, true);
                picker.show();
            }
        });

        selectEndTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);
                // time picker dialog
                TimePickerDialog picker = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                String formattedHour = String.format("%02d", sHour);
                                String formattedMinute = String.format("%02d", sMinute);
                                showEndTimeDataTv.setText(formattedHour + ":" + formattedMinute);
                            }
                        }, hour, minutes, true);
                picker.show();
            }
        });

        selectDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                DatePickerDialog picker = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // Note: monthOfYear is zero-based, so we add 1 to get the correct month number
                                String formattedDate = String.format("%04d-%02d-%02d", year, monthOfYear + 1, dayOfMonth);
                                showDateDataTv.setText(formattedDate);
                            }
                        }, year, month, day);
                picker.show();
            }
        });
    }

    public void navigateToFragment(Fragment fragment) {

        // Begin the transaction
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

        ft.replace(R.id.frameLayoutOrg, fragment);

        // Add the transaction to the back stack (optional)
        ft.addToBackStack(null);

        // Commit the transaction
        ft.commit();
    }

}