package com.example.yamenandroidacteen.home.organization;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.yamenandroidacteen.R;
import com.example.yamenandroidacteen.home.activist.ActivistSettingsFragment;
import com.example.yamenandroidacteen.home.activist.ActivistShowSavedPostsFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class OrganizationPorfileFragment extends Fragment {


    TextView usernameTv, emailTv, orgTypeTv, orgPhoneTv;
    ImageView profileIv;
    private FirebaseAuth mAuth;

    private View myPostsLayout;
    private View settingsLayout;

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
        settingsLayout = view.findViewById(R.id.settingsLayout2);
        myPostsLayout = view.findViewById(R.id.myPostsLayout);

        profileIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProfileImageDialog();
            }
        });

        myPostsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((OrganizationHomeActivity) requireActivity()).navigateToFragmentWithAnimation(new OrganizationMyPostsFragment());
            }
        });

        settingsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((OrganizationHomeActivity) requireActivity()).navigateToFragmentWithAnimation(new OrganizationSettingsFragment());
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

                                if(getActivity() != null) {
                                    // Update the profile picture ImageView with the new URL
                                    if (profilePictureUrl != null && !profilePictureUrl.isEmpty() && getActivity() != null) {
                                        Glide.with(this)
                                                .load(profilePictureUrl)
                                                .into(profileIv);
                                    } else {
                                        // Display the default profile picture
                                        Glide.with(this)
                                                .load(R.drawable.icon_account)
                                                .into(profileIv);
                                    }
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

    private void showProfileImageDialog() {
        // Create a dialog to show the profile image
        Dialog dialog = new Dialog(requireContext()) {
            public boolean onTouchEvent(MotionEvent event) {
                // Tap anywhere to close dialog.
                this.dismiss();
                return true;
            }
        };

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_expanded_profile_image);

        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        // Find the ImageView in the dialog layout
        ImageView expandedImageView = dialog.findViewById(R.id.expandedImageView);


        // Set profile image to expandedImageView
        expandedImageView.setImageDrawable(profileIv.getDrawable());

        dialog.show();
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((OrganizationHomeActivity) requireActivity()).showSystemNavigationBar();

    }


}