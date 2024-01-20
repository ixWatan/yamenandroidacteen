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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;


public class OrganizationCreatePostFragment extends Fragment {


    FirebaseAuth firebaseAuth;
    ActionBar actionBar;

    AutoCompleteTextView hashtagsAutoCompleteTextView;


    //permissions
    private static final int UCROP_REQUEST_CODE = 942;

    private static final int AUTOCOMPLETE_REQUEST_CODE = 1;

    //picked image will be saved in this uri
    Uri image_uri = null;



    //views
    EditText titleEt, descripionEt;
    ImageButton backButton;


    //Date, Start Time, End Time
    EditText editTextDate, editTextStart, editTextEnd;

    String selectedDate;
    String selectedStartTime;
    String selectedEndTime;

    ImageView imageIv;
    Button uploadbtn, pLocationBtn;

    //show location TV
    TextView locationTagTextView, nextTv;

    //user info
    String name, email, uid, dp;

    //progress bar
    ProgressDialog pd;

    private String locationAddressInAddEvent;

    private double latitude;
    private double longitude;
    private String mapsUri;

    SpannableString locationLink;


    private static final int PICK_IMAGE = 1;

    //#

    String[] hashTagsList = {"#Environment", "#Volunteering", "#Protests", "#Women's Rights", "#Human Rights", "#Racism", "#LGBTQ+", "#Animals", "#Petitions", "#Education"};

    AutoCompleteTextView autoCompleteTextView;

    ArrayAdapter<String> adapterItems;

    String selectHashTag;

    TextView selectedHashtagsTv;
    List<String> selectedHashtags;

    LinearLayout hashtagsContainer;

    ActivityResultLauncher<String> mGetContent;

    private String lastClickedButton = ""; // Variable to store the last clicked button



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_organization_create_post, container, false);





        imageIv = view.findViewById(R.id.imageIvNewPost);
        backButton = view.findViewById(R.id.backButton);
        nextTv = view.findViewById(R.id.NextTv);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastClickedButton = "Button Back";
                navigateToFragment(new OrganizationHomeFragment());
            }
        });

        nextTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastClickedButton = "Button Next";

                navigateToFragment(new OrganizationCreatePostSecondFragment());
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
                Intent intent = new Intent(getContext(), CropperActivity.class);
                intent.putExtra("DATA", result.toString());
                startActivityForResult(intent, 101);
                }
            }
        );



        return view;
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
            Uri resultUri = null;
            if(result != null) {
                resultUri = Uri.parse(result);
            }

            imageIv.setImageURI(resultUri);
        }
    }
}