package com.example.yamenandroidacteen.home.organization;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import android.content.DialogInterface;



import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
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
import com.example.yamenandroidacteen.classes.User;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;


public class OrganizationCreatePostThirdFragment extends Fragment {


    FirebaseAuth firebaseAuth;

    Boolean done;

    private MaterialAlertDialogBuilder dialogBuilder; // Declare dialogBuilder variable
    private AlertDialog dialog; // Declare dialog variable


    Button selectStartTimeBtn, selectEndTimeBtn,selectDateBtn;

    ImageView backButton;
    TextView showStartTimeDataTv, showEndTimeDataTv, showDateDataTv;

    String[] hashTagsList = {"#Environment", "#Volunteering", "#Protests", "#Women's Rights", "#Human Rights", "#Racism", "#LGBTQ+", "#Animals", "#Petitions", "#Education"};

    AutoCompleteTextView autoCompleteTextView;

    ArrayAdapter<String> adapterItems;

    String selectedDate, selectedStartTime, selectedEndTime, pTitle, pDescription, pImage, pLocationLinkReal, pLocationLink, uid, name, email, dp;

    List<String> selectedHashtags;

    private TextView postTv;

    FlexboxLayout hashtagsContainer;

    ProgressDialog pd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_organization_create_post_third, container, false);
        firebaseAuth = FirebaseAuth.getInstance();

        selectStartTimeBtn = view.findViewById(R.id.selectStartTimeBtn);
        selectEndTimeBtn = view.findViewById(R.id.selectEndTimeBtn);
        selectDateBtn = view.findViewById(R.id.selectDateBtn);
        backButton = view.findViewById(R.id.backButtonThirdStep);
        postTv = view.findViewById(R.id.postTv);


        showStartTimeDataTv = view.findViewById(R.id.showStartTimeDataTv);
        showEndTimeDataTv = view.findViewById(R.id.showEndTimeDataTv);
        showDateDataTv = view.findViewById(R.id.showDateDataTv);

        // drop down menu for regions
        autoCompleteTextView = view.findViewById(R.id.autocomplete_Tv);
        adapterItems = new ArrayAdapter<String>(getActivity(), R.layout.list_item, hashTagsList);
        autoCompleteTextView.setAdapter(adapterItems);
        selectedHashtags = new ArrayList<>();
        hashtagsContainer = view.findViewById(R.id.hashtags_container);

        pd = new ProgressDialog(getActivity());



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

        postTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPostTvClick();

            }
        });

        dateAndTimePickerInit();

        hashtagSelectorInit();




        return view;
    }

    private void onPostTvClick() {

        FirebaseUser user = firebaseAuth.getCurrentUser();


        selectedEndTime = showEndTimeDataTv.getText().toString();
        selectedStartTime = showStartTimeDataTv.getText().toString();
        selectedDate = showDateDataTv.getText().toString();
        pTitle = getArguments().getString("post_title");
        pDescription = getArguments().getString("post_description");
        pLocationLinkReal = getArguments().getString("pLocationLinkReal");
        pLocationLink = getArguments().getString("pLocationLink");
        pImage = getArguments().getString("pImage");
        uid = user.getUid();



        // Check if at least one hashtag is selected
        if (selectedHashtags.isEmpty()) {
            Toast.makeText(getActivity(), "You must select at least one hashtag", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedDate.isEmpty()) {
            Toast.makeText(getActivity(), "You must select a date", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if start hour is selected
        if (selectedStartTime.isEmpty()) {
            Toast.makeText(getActivity(), "You must select a start time", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedEndTime.isEmpty()) {
            Toast.makeText(getActivity(), "You must select an end time", Toast.LENGTH_SHORT).show();
            return;
        }

        pd.setMessage("Publishing post...");
        pd.show();


        //for post-image name, post-id, post-publish-time
        String timeStamp = String.valueOf(System.currentTimeMillis());
        String filePathAndName = "Posts/" + "post_" + timeStamp;

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        HashMap<Object, String> hashMap = new HashMap<>();

        db.collection("organizations").document(uid).get()
                .addOnCompleteListener(task -> {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        name = document.getString("organization_name");
                        email = document.getString("email");
                        dp = document.getString("profilePictureUrl");

                        //add date, start time and end time
                        hashMap.put("pDate", selectedDate);
                        hashMap.put("pStartT", selectedStartTime);
                        hashMap.put("pEndT", selectedEndTime);
                        hashMap.put("pComments", "0");
                        hashMap.put("pLikes", "0");



                        // Add the link to the post data
                        hashMap.put("pLocationLinkReal", pLocationLinkReal);


                        // Add the link to the post data
                        hashMap.put("pLocationLink", pLocationLink);

                        // Add org name and org pfp
                        hashMap.put("uName", name);
                        hashMap.put("uDp", dp);

                        // Convert list of hashtags to a comma-separated string
                        String joinedHashtags = TextUtils.join(", ", selectedHashtags);
                        hashMap.put("pHashtags", joinedHashtags);
                    }
                });

        if ( !pImage.isEmpty() ) {
            //post with image
            StorageReference ref = FirebaseStorage.getInstance().getReference().child(filePathAndName);
            ref.putFile(Uri.parse(pImage))
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //image is upload to firebase storage, now get it's url
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isSuccessful()) ;

                            String downloadUri = uriTask.getResult().toString();

                            if (uriTask.isSuccessful()) {

                                //url is received upload post to fireBase storage
                                //put post info
                                hashMap.put("uid", uid);
                                hashMap.put("uEmail", email);
                                hashMap.put("pId", timeStamp);
                                hashMap.put("pTitle", pTitle);
                                hashMap.put("pDescription", pDescription);
                                hashMap.put("pImage", downloadUri);
                                hashMap.put("pTime", timeStamp);

                                //path to store post data
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
                                //put data in this ref
                                ref.child(timeStamp).setValue(hashMap)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                //added in database
                                                pd.dismiss();
                                                showSuccessAlert();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                //failed adding post in database
                                                pd.dismiss();
                                                Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });


                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //failed uploading image
                            pd.dismiss();
                            Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(getActivity(), "error when trying to post the post", Toast.LENGTH_SHORT).show();

        }
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

    private void hashtagSelectorInit() {
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


                    // Create a new TextView for the selected hashtag
                    TextView hashtagTv = new TextView(getActivity());
                    hashtagTv.setLayoutParams(new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    ));

                    hashtagTv.setText("  " + selectedHashtag);
                    hashtagTv.setBackgroundResource(R.drawable.tag_event_background);
                    hashtagTv.setPadding(15, 10, 15, 10);
                    hashtagTv.setTextColor(getResources().getColor(android.R.color.white));
                    hashtagTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
                    hashtagTv.setMaxLines(1);
                    hashtagTv.setTypeface(null, Typeface.BOLD);
                    hashtagTv.setLetterSpacing(0.1f);
                    hashtagTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_close, 0);
                    hashtagTv.setCompoundDrawableTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));

                    hashtagTv.setGravity(Gravity.CENTER);

                    hashtagTv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            selectedHashtags.remove(selectedHashtag);
                            hashtagsContainer.removeView(view);
                        }
                    });


                    ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    );
                    int marginInPixels = 16; // Set the desired right margin in pixels
                    layoutParams.rightMargin = marginInPixels;
                    layoutParams.topMargin = marginInPixels;

                    hashtagTv.setLayoutParams(layoutParams);

                    hashtagsContainer.addView(hashtagTv);

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

    private void showSuccessAlert() {

        done = false;
        dialogBuilder = new MaterialAlertDialogBuilder(getActivity())
                .setTitle("Post Published Successfully")
                .setMessage("Moving to Homepage in")
                .setPositiveButton("Discard", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle discard button click
                        dialog.dismiss(); // Dismiss the dialog
                        done = true;
                    }
                })
                .setCancelable(false);

        dialog = dialogBuilder.create(); // Create the dialog

        dialog.show(); // Show the dialog

        // Start a countdown timer
        new CountDownTimer(6000, 1000) {
            public void onTick(long millisUntilFinished) {
                // Update the content with the remaining time
                dialog.setMessage("Moving to Homepage in " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                // Dismiss the dialog or navigate to the homepage
                dialog.dismiss(); // Dismiss the dialog
                if(!done) {
                    navigateToFragment(new OrganizationHomeFragment());

                        ((OrganizationHomeActivity) requireActivity()).showSystemNavigationBar();

                }
            }
        }.start();
    }

}