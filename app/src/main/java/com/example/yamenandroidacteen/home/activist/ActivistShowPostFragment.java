package com.example.yamenandroidacteen.home.activist;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.CalendarContract;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.yamenandroidacteen.MainActivity;
import com.example.yamenandroidacteen.R;
import com.example.yamenandroidacteen.classes.adapters.AdapterPosts;


/*import com.example.meet_workshop.homepage.homeactivist.HomeActivity;
import com.example.meet_workshop.homepage.homeorganization.AddEventOrgActivity;*/


import com.example.yamenandroidacteen.classes.models.ModelComment;
import com.example.yamenandroidacteen.classes.models.ModelPost;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


import okhttp3.internal.Util;

public class ActivistShowPostFragment extends Fragment {

    String orgName;
    String postImage;


    RecyclerView recyclerView;


    ProgressDialog pd;


    String postDescreption;


    String postTimePosted;


    String postTitle;
    String uDp;


    String postTags;


    String postLocationString, DateAndTime;


    String locationLinkReal;


    String postTimeS;
    String postTimeE, postId;


    String postDateString, postLikes, postComments;;


    String LocationAndTime, UserProfilePictureUrl;


    //Add To Calender Btn
    Button addToCalender;



    private FirebaseAuth mAuth;

    private View nav;









    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_activist_show_post, container, false);

        mAuth = FirebaseAuth.getInstance();

        initViewsAndData(view);

        loadUserData(view);




        // Get a reference to the button
        addToCalender = view.findViewById(R.id.addToCalenderBtn);


        // Set an onClick listener for the button
        addToCalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addToCalenderFunc();

            }
        });
        //End


        loadTags(view);


        return view;
    }





    // --==--==--==--==--==--==--==--==--==

    // private fucntions

    // --==--==--==--==--==--==--==--==--==

    private void addToCalenderFunc() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        Date startDate = null;
        Date endDate = null;
        try {
            startDate = dateFormat.parse(postDateString + " " + postTimeS);
            endDate = dateFormat.parse(postDateString + " " + postTimeE);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        // Check if startDate and endDate are null, if they are, return from this function early
        if(startDate == null || endDate == null) {
            Toast.makeText(getActivity(), "Invalid date/time format.", Toast.LENGTH_SHORT).show();
            return;
        }


        // Create an intent to open the Calendar app
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startDate.getTime())
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endDate.getTime())
                .putExtra(CalendarContract.Events.TITLE, postTitle)
                .putExtra(CalendarContract.Events.DESCRIPTION,postDescreption)
                .putExtra(CalendarContract.Events.EVENT_LOCATION, locationLinkReal);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        } else {
            // Show an error message if no app can handle the intent
            Toast.makeText(getActivity(), "No application can handle this action.", Toast.LENGTH_SHORT).show();
        }
    }


    private void loadUserData(View view) {
        // load user info
        //------
        //---


        ImageView userProfileIv = (ImageView) view.findViewById(R.id.showPostProfileImgComment);




        FirebaseUser user = mAuth.getCurrentUser();



        String userId = user.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("teenActivists").document(userId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String userName = document.getString("name");
                            String email = document.getString("email");
                            String profilePictureUrl = document.getString("profilePictureUrl");


                            // Update the profile picture ImageView with the new URL
                            if (profilePictureUrl != null && !profilePictureUrl.isEmpty()) {
                                Glide.with(this)
                                        .load(profilePictureUrl + "?timestamp=" + System.currentTimeMillis())
                                        .into(userProfileIv);
                            } else {
                                // Display the default profile picture
                                Glide.with(this)
                                        .load(R.drawable.icon_account)
                                        .into(userProfileIv);
                            }


                        }
                    } else {
                        Toast.makeText(getActivity(), "An error occurred. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });




        // Load and display the user's profile picture using a library like Glide or Picasso
        String profilePictureUrl = getArguments().getString("profilePictureUrl");
        if (profilePictureUrl != null && !profilePictureUrl.isEmpty()) {
            Glide.with(this)
                    .load(profilePictureUrl)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(userProfileIv);


        } else {
            // Set the default profile picture
            Glide.with(this)
                    .load(R.drawable.icon_account)
                    .into(userProfileIv);
        }




        //---
        //------
        // load user info







    }

    private void initViewsAndData(View view) {

        TextView nameOrgTv = (TextView) view.findViewById(R.id.showNameOrg);
        TextView postDescreptionTv = (TextView) view.findViewById(R.id.showDescreption);
        TextView postTimePostedTv = (TextView) view.findViewById(R.id.showPostTimePosted);
        ImageView postImageIv = (ImageView) view.findViewById(R.id.showImagePost);
        TextView postTitleTv = (TextView) view.findViewById(R.id.showPostTitle);
        ImageView postPorfileIv = (ImageView) view.findViewById(R.id.showPostProfileImg);
        TextView postLocation = (TextView) view.findViewById(R.id.locationTvShowPost);
        TextView postDate = (TextView) view.findViewById(R.id.DateTvShowPost);



        postImage = getArguments().getString("post_image");
        uDp = getArguments().getString("post_user_pfp");
        orgName = getArguments().getString("org_name");
        postDescreption = getArguments().getString("post_description");
        postTimePosted = getArguments().getString("post_timePosted");
        postTitle = getArguments().getString("post_title");
        postTags = getArguments().getString("post_tags");
        postLocationString = getArguments().getString("post_locationLink");
        locationLinkReal = getArguments().getString("post_location");
        postTimeS = getArguments().getString("post_startT");
        postTimeE = getArguments().getString("post_endT");
        postDateString = getArguments().getString("post_date");
        postId = getArguments().getString("post_id");
        postComments = getArguments().getString("post_comments");
        postLikes =  getArguments().getString("post_likes");
        UserProfilePictureUrl = getArguments().getString("userProfilePicUrl");



        // calendar stuff
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(postTimePosted));
        android.text.format.DateFormat df = new android.text.format.DateFormat();
        String pTime = df.format("dd/MM/yyyy hh:mm aa", calendar).toString();


        DateAndTime = postDateString + ", " + postTimeS + "-" + postTimeE;

        // end of calendar stuff





        // filling the views with the data from the post

        nameOrgTv.setText(orgName);
        postDescreptionTv.setText(postDescreption);
        postLocation.setText(locationLinkReal);
        postDate.setText(DateAndTime);
        postTitleTv.setText(postTitle);
        postTimePostedTv.setText(pTime);
        try {
            Picasso.get().load(postImage).into(postImageIv);


        } catch (Exception e ) {
            Toast.makeText(getActivity(), "shit", Toast.LENGTH_SHORT).show();
        }


        try {
            Picasso.get().load(uDp).into(postPorfileIv);
        } catch (Exception e) {


        }
    }

    private void loadTags(View view) {


        TextView Tag1 = (TextView) view.findViewById(R.id.showTagLabel1);
        TextView Tag2 = (TextView) view.findViewById(R.id.showTagLabel2);
        TextView Tag3 = (TextView) view.findViewById(R.id.showTagLabel3);


        String[] myArray = postTags.split(",");


        int numberOfHashtags = myArray.length;






        if (numberOfHashtags >= 1) {
            Tag1.setText(myArray[0]);
            Tag1.setVisibility(View.VISIBLE);
        } else {
            Tag1.setVisibility(View.GONE);
        }


        if (numberOfHashtags >= 2) {
            Tag2.setText(myArray[1]);
            Tag2.setVisibility(View.VISIBLE);
        } else {
            Tag2.setVisibility(View.GONE);
        }


        if (numberOfHashtags >= 3) {
            Tag3.setText(myArray[2]);
            Tag3.setVisibility(View.VISIBLE);
        } else {
            Tag3.setVisibility(View.GONE);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Hide the system navigation bar when the fragment is displayed
        ((ActivistHomeActivity) requireActivity()).hideSystemNavigationBar();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Show the system navigation bar when the fragment is destroyed
        ((ActivistHomeActivity) requireActivity()).showSystemNavigationBar();
    }


}




