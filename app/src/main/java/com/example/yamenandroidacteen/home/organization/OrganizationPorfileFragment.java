package com.example.yamenandroidacteen.home.organization;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.yamenandroidacteen.R;
import com.example.yamenandroidacteen.home.activist.ActivistSettingsFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class OrganizationPorfileFragment extends Fragment {


    TextView usernameTv, emailTv, orgTypeTv, orgPhoneTv, settingsArrowTv, myPostsArrowTv;
    ImageView profileIv;
    private FirebaseAuth mAuth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_organization_porfile, container, false);
        mAuth = FirebaseAuth.getInstance();




        initAndShowData(view);

        return view;
    }

    private void initAndShowData(View view) {

        usernameTv = view.findViewById(R.id.orgUsernameTv);
        emailTv = view.findViewById(R.id.orgEmailTv);
        orgTypeTv = view.findViewById(R.id.orgTypeTvAnswer);
        orgPhoneTv = view.findViewById(R.id.orgPhoneTvAnswer);
        profileIv = view.findViewById(R.id.profileImageView);
        settingsArrowTv = view.findViewById(R.id.settingsTvAnswer);
        myPostsArrowTv = view.findViewById(R.id.savedPostsTvAnswer);

        settingsArrowTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToFragment(new OrganizationSettingsFragment());
            }
        });

        myPostsArrowTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToFragment(new OrganizationMyPostsFragment());
            }
        });




        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("organizations")
                    .document(user.getUid())
                    .get()
                    .addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            DocumentSnapshot document = task1.getResult();
                            if (document != null && document.exists()) {
                                String storedEmail = document.getString("email");
                                String storedUsername = document.getString("organization_name");
                                String storedType = document.getString("orgType");
                                String storedPhone = document.getString("phone");
                                String profilePictureUrl = document.getString("profilePictureUrl");


                                usernameTv.setText(storedUsername);
                                emailTv.setText(storedEmail);
                                orgTypeTv.setText(storedType);
                                orgPhoneTv.setText(storedPhone);

                                // Update the profile picture ImageView with the new URL
                                if (profilePictureUrl != null && !profilePictureUrl.isEmpty()) {
                                    Glide.with(this)
                                            .load(profilePictureUrl + "?timestamp=" + System.currentTimeMillis())
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

        ft.replace(R.id.frameLayoutOrg, fragment);

        // Add the transaction to the back stack (optional)
        ft.addToBackStack(null);

        // Commit the transaction
        ft.commit();
    }



}