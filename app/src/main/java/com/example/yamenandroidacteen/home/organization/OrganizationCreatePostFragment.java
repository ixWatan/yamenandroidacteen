package com.example.yamenandroidacteen.home.organization;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.yamenandroidacteen.R;
import com.example.yamenandroidacteen.classes.other.CropperActivity;
import com.example.yamenandroidacteen.home.activist.ActivistShowPostFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;


public class OrganizationCreatePostFragment extends Fragment {



    //views
    EditText titleEt, descripionEt;
    ImageButton backButton;


    ImageView imageIv;
    TextView  nextTv, newPostTv;

    private static final String PREF_IMAGE_URI = "image_uri";




    ActivityResultLauncher<String> mGetContent;

    private String lastClickedButton = ""; // Variable to store the last clicked button

    Uri resultUri;

    Bundle bundleEdit;

    Boolean flag;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_organization_create_post, container, false);





        imageIv = view.findViewById(R.id.imageIvNewPost);
        titleEt = view.findViewById(R.id.addPostTitleTv);
        descripionEt = view.findViewById(R.id.addPostDescreptionTv);
        backButton = view.findViewById(R.id.backButton);
        nextTv = view.findViewById(R.id.NextTv);
        newPostTv = view.findViewById(R.id.NewPostTv);
        bundleEdit = getArguments();


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        String imageUriString = sharedPreferences.getString(PREF_IMAGE_URI, null);
        if (imageUriString != null) {
            resultUri = Uri.parse(imageUriString);
            imageIv.setImageURI(resultUri);
        }

        checkIfComingToEditAndFillData();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lastClickedButton = "Button Back";
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        navigateToFragment(new OrganizationHomeFragment());

                    }
                }, 300);
            }
        });

        nextTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkForEmptyInputAndMoveToNextStep();
            }
        });

        imageIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGetContent.launch("image/*");
            }
        });

        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if (result != null) {
                    Intent intent = new Intent(getContext(), CropperActivity.class);
                    intent.putExtra("DATA", result.toString());
                    startActivityForResult(intent, 101);
                } else {

                    Toast.makeText(getContext(), "No image selected", Toast.LENGTH_SHORT).show();
                }
            }
        });





        return view;
    }

    private void checkIfComingToEditAndFillData() {
        if (bundleEdit != null) {
            if(bundleEdit.getString("comingToEdit").equals("T")) {
                // Retrieve data from the bundle
                flag = true;
                Toast.makeText(getActivity(), "comingToEdit", Toast.LENGTH_SHORT).show();

                String postImage = bundleEdit.getString("post_image");
                String postDescreption = bundleEdit.getString("post_description");
                String postTitle = bundleEdit.getString("post_title");
                String postTags = bundleEdit.getString("post_tags");
                String postLocationString = bundleEdit.getString("post_locationLink");
                String locationLinkReal = bundleEdit.getString("post_location");
                String postTimeS = bundleEdit.getString("post_startT");
                String postTimeE = bundleEdit.getString("post_endT");
                String postDateString = bundleEdit.getString("post_date");
                String postId = bundleEdit.getString("post_id");


                titleEt.setText(postTitle);
                descripionEt.setText(postDescreption);
                Picasso.get().load(postImage).into(imageIv);
                resultUri = Uri.parse(postImage);
                newPostTv.setText("Edit Post");
            }
        }

    }

    private void saveImageUri(Uri uri) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREF_IMAGE_URI, uri.toString());
        editor.apply();
    }

    private void checkForEmptyInputAndMoveToNextStep() {
        //get data(title, description) from EditText
        String title = titleEt.getText().toString().trim();
        String description = descripionEt.getText().toString().trim();


        if (TextUtils.isEmpty(title)) {
            Toast.makeText(getActivity(), "Title Is Empty ....", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(description)) {
            Toast.makeText(getActivity(), "Description is empty ...", Toast.LENGTH_SHORT).show();
            return;
        }

        if (resultUri == null) {
            Toast.makeText(getActivity(), "Add an image ...", Toast.LENGTH_SHORT).show();
            return;
        }
        Bundle bundle = new Bundle();

        lastClickedButton = "Button Next";

        if(flag != null) {
                if(flag) {
                    bundle.putSerializable("comingToEdit", "T");
                    bundle.putSerializable("post_tags", bundleEdit.getString("post_tags"));
                    bundle.putSerializable("post_locationLink", bundleEdit.getString("post_locationLink"));
                    bundle.putSerializable("post_location", bundleEdit.getString("post_location"));
                    bundle.putSerializable("post_startT", bundleEdit.getString("post_startT"));
                    bundle.putSerializable("post_endT", bundleEdit.getString("post_endT"));
                    bundle.putSerializable("post_date", bundleEdit.getString("post_date"));
                    bundle.putSerializable("post_id", bundleEdit.getString("post_id"));
                }}

        bundle.putSerializable("pImage", String.valueOf(resultUri));

        saveImageUri(resultUri);

        bundle.putSerializable("post_description", description);
        bundle.putSerializable("post_title", title);

        Fragment organizationCreatePostSecondFragment = new OrganizationCreatePostSecondFragment();
        organizationCreatePostSecondFragment.setArguments(bundle);

        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(
                R.anim.slide_in,  // enter
                R.anim.fade_out,  // exit
                R.anim.fade_in,   // popEnter
                R.anim.slide_out  // popExit
        );

        ft.replace(R.id.frameLayoutOrg, organizationCreatePostSecondFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void navigateToFragment(Fragment fragment) {

        // Begin the transaction
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right, R.anim.slide_in_left, R.anim.slide_out_right);
        ft.replace(R.id.frameLayoutOrg, fragment);

        // Add the transaction to the back stack (optional)
        ft.addToBackStack(null);

        // Commit the transaction
        ft.commit();
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Hide the system navigation bar when the fragment is displayed
        ((OrganizationHomeActivity) requireActivity()).hideSystemNavigationBar();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Use lastClickedButton to determine which button was clicked
        if ("Button Back".equals(lastClickedButton)) {
            ((OrganizationHomeActivity) requireActivity()).showSystemNavigationBar();
        }

        // Show the system navigation bar when the fragment is destroyed
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == -1 && requestCode == 101) {
            String result = data.getStringExtra("RESULT");
            resultUri = null;
            if(result != null) {
                resultUri = Uri.parse(result);
            }

            imageIv.setImageURI(resultUri);
        }
    }
}