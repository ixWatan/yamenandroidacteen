package com.example.yamenandroidacteen.home.organization;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.yamenandroidacteen.R;
import com.example.yamenandroidacteen.home.activist.ActivistHomeActivity;
import com.example.yamenandroidacteen.home.activist.ActivistShowPostFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class OrganizationShowPostFragment extends Fragment {


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



    com.google.android.material.imageview.ShapeableImageView postImageIv;






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_organization_show_post, container, false);

        mAuth = FirebaseAuth.getInstance();

        initViewsAndData(view);


        postImageIv.setOnClickListener(v -> showPostImageDialog());




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




    private void initViewsAndData(View view) {

        TextView nameOrgTv = (TextView) view.findViewById(R.id.showNameOrg);
        TextView postDescreptionTv = (TextView) view.findViewById(R.id.showDescreption);
        TextView postTimePostedTv = (TextView) view.findViewById(R.id.showPostTimePosted);
        postImageIv = (com.google.android.material.imageview.ShapeableImageView) view.findViewById(R.id.showImagePost);
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

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((OrganizationHomeActivity) requireActivity()).hideSystemNavigationBar();


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        /*Fragment fragment = requireActivity().getSupportFragmentManager().findFragmentByTag("goingToOrgShowPostFromMyPosts"");

        if (fragment instanceof OrganizationMyPostsFragment) {
            OrganizationMyPostsFragment organizationPostFragment = (OrganizationMyPostsFragment) fragment;
            organizationPostFragment.isPostClicked = false;

        } else {
            ((OrganizationHomeActivity) requireActivity()).showSystemNavigationBar();
        }*/
    }

    private void showPostImageDialog() {

        // Create a dialog to show the profile image
        Dialog dialog = new Dialog(requireContext()) {
            public boolean onTouchEvent(MotionEvent event) {
                // Tap anywhere to close dialog.
                this.dismiss();
                return true;
            }
        };

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_expanded_post_image);

        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        // Find the ImageView in the dialog layout
        ImageView expandedImageView = dialog.findViewById(R.id.expandedImageView);



        // Set profile image to expandedImageView
        expandedImageView.setImageDrawable(postImageIv.getDrawable());

        dialog.show();
    }



}