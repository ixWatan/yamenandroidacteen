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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.yamenandroidacteen.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class OrganizationCreatePostThirdFragment extends Fragment {


    Button selectStartTimeBtn, selectEndTimeBtn,selectDateBtn;

    ImageView backButton;
    TextView showStartTimeDataTv, showEndTimeDataTv, showDateDataTv;

    String[] hashTagsList = {"#Environment", "#Volunteering", "#Protests", "#Women's Rights", "#Human Rights", "#Racism", "#LGBTQ+", "#Animals", "#Petitions", "#Education"};

    AutoCompleteTextView autoCompleteTextView;

    ArrayAdapter<String> adapterItems;

    String selectHashTag;

    TextView selectedHashtagsTv;
    List<String> selectedHashtags;

    LinearLayout hashtagsContainer;

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

        // drop down menu for regions
        autoCompleteTextView = view.findViewById(R.id.autocomplete_Tv);
        adapterItems = new ArrayAdapter<String>(getActivity(), R.layout.list_item, hashTagsList);
        autoCompleteTextView.setAdapter(adapterItems);
        selectedHashtagsTv = view.findViewById(R.id.selected_hashtags_tv);
        selectedHashtags = new ArrayList<>();
        hashtagsContainer = view.findViewById(R.id.hashtags_container);

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

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String selectedHashtag = adapterView.getItemAtPosition(position).toString();

                if (selectedHashtags.size() >= 3) {
                    Toast.makeText(getActivity(), "You can select only 3 hashtags", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!selectedHashtags.contains(selectedHashtag)) {
                    selectedHashtags.add(selectedHashtag);
                    TextView hashtagTv = new TextView(getActivity());
                    hashtagTv.setText("  " + selectedHashtag + " x");
                    hashtagTv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            selectedHashtags.remove(selectedHashtag);
                            hashtagsContainer.removeView(view);
                        }
                    });
                    hashtagsContainer.addView(hashtagTv);
                    autoCompleteTextView.setText("");
                } else {
                    Toast.makeText(getActivity(), "You have already selected this hashtag", Toast.LENGTH_SHORT).show();
                }

                // Clear the AutoCompleteTextView text
                autoCompleteTextView.setText("");
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