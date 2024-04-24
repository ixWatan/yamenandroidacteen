package com.example.yamenandroidacteen.home.activist;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.yamenandroidacteen.MainActivity;
import com.example.yamenandroidacteen.R;
import com.example.yamenandroidacteen.auth.ForgotPasswordFragment;
import com.example.yamenandroidacteen.classes.adapters.AdapterPosts;
import com.example.yamenandroidacteen.classes.models.ModelPost;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class ActivistProfileFragment extends Fragment {



    TextView usernameTv, emailTv, cityTv, regionTv, settingsArrowTv, savedPostsTv;
    ImageView profileIv;
    private FirebaseAuth mAuth;

    private View savedPostsLayout;
    private View settingsLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_activist_profile, container, false);
        mAuth = FirebaseAuth.getInstance();




        initAndShowData(view);

        return view;


    }

    private void initAndShowData(View view) {

        usernameTv = view.findViewById(R.id.activistUsernameTv);
        emailTv = view.findViewById(R.id.activistEmailTv);
        cityTv = view.findViewById(R.id.userCityTvAnswer);
        regionTv = view.findViewById(R.id.userRegionTvAnswer);
        profileIv = view.findViewById(R.id.profileImageView);
        settingsArrowTv = view.findViewById(R.id.settingsTvAnswer);
        savedPostsTv = view.findViewById(R.id.savedPostsTvAnswer);
        settingsLayout = view.findViewById(R.id.settingsLayout);
        savedPostsLayout = view.findViewById(R.id.savedPostsLayout);

        settingsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToFragment(new ActivistSettingsFragment());
            }
        });

        savedPostsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToFragment(new ActivistShowSavedPostsFragment());
            }
        });



        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("teenActivists")
                    .document(user.getUid())
                    .get()
                    .addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            DocumentSnapshot document = task1.getResult();
                            if (document != null && document.exists()) {
                                String storedEmail = document.getString("email");
                                String storedUsername = document.getString("name");
                                String storedCity = document.getString("city");
                                String storedRegion = document.getString("region");
                                String profilePictureUrl = document.getString("profilePictureUrl");


                                usernameTv.setText(storedUsername);
                                emailTv.setText(storedEmail);
                                cityTv.setText(storedCity);
                                regionTv.setText(storedRegion);

                                // Update the profile picture ImageView with the new URL
                                if (profilePictureUrl != null && !profilePictureUrl.isEmpty()) {
                                    Glide.with(this)
                                            .load(profilePictureUrl)
                                            .into(profileIv);
                                } else {
                                    // Display the default profile picture
                                    Glide.with(this)
                                            .load(R.drawable.icon_account)
                                            .into(profileIv);
                                }


                            } else {
                                Log.d(TAG, "Failed to get user document.", task1.getException());
                                }
                            } else {
                                Log.d(TAG, "Failed to get user document.", task1.getException());
                            }

                    });
        }

    }

    public void navigateToFragment(Fragment fragment) {

        // Begin the transaction
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

        ft.replace(R.id.frameLayoutActivist, fragment);

        // Add the transaction to the back stack (optional)
        ft.addToBackStack(null);

        // Commit the transaction
        ft.commit();
    }





}