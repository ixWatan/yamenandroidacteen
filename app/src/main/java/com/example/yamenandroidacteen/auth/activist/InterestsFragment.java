package com.example.yamenandroidacteen.auth.activist;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.yamenandroidacteen.R;
import com.example.yamenandroidacteen.classes.User;
import com.example.yamenandroidacteen.home.activist.ActivistHomeActivity;
import com.example.yamenandroidacteen.home.activist.ActivistHomeFragment;
import com.example.yamenandroidacteen.home.organization.OrganizationHomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.protobuf.Value;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InterestsFragment extends Fragment {
    // Define the array of hashtags
    String[] hashTagsList = {"#Environment", "#Volunteering", "#Protests", "#Women's Rights", "#Human Rights", "#Racism", "#LGBTQ+", "#Animals", "#Petitions", "#Education"};

    private List<String> interestsList = new ArrayList<>();
    private LinearLayout interestsLayout;
    private int selectedCount = 0; // Track the number of selected interests

    private String name;
    private String email;
    private String password;
    private int age;
    private String city;
    private String region;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private ProgressDialog pd;

    User person;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_interests, container, false);
        pd = new ProgressDialog(getActivity());


        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        for (String hashTag : hashTagsList) {
            interestsList.add(hashTag);
        }

        if (getArguments() != null) {
            person = (User) getArguments().getSerializable("User");


        }

        // Initialize the layout
        interestsLayout = view.findViewById(R.id.interestsLayout);

        // Add checkboxes dynamically
        for (String interest : interestsList) {
            CheckBox checkBox = new CheckBox(getContext());
            checkBox.setText(interest);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        if (selectedCount >= 3) {
                            checkBox.setChecked(false); // Prevent checking more than three interests
                            Toast.makeText(getActivity(), "Maximum three interests allowed", Toast.LENGTH_SHORT).show();
                        } else {
                            selectedCount++;
                        }
                    } else {
                        selectedCount--;
                    }
                }
            });
            interestsLayout.addView(checkBox);
        }

        // Next button click listener
        Button buttonNext = view.findViewById(R.id.buttonNext);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pd.setMessage("Creating Account...");
                pd.show();

                // Get selected interests
                List<String> selectedInterests = new ArrayList<>();
                for (int i = 0; i < interestsLayout.getChildCount(); i++) {
                    View childView = interestsLayout.getChildAt(i);
                    if (childView instanceof CheckBox) {
                        CheckBox checkBox = (CheckBox) childView;
                        if (checkBox.isChecked()) {
                            selectedInterests.add(checkBox.getText().toString());
                        }
                    }
                }



                // Retrieve User object
                mAuth.createUserWithEmailAndPassword(person.getEmail(), person.getPassword())
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // User creation successful
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    String uid = user.getUid();
                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    Map<String, Object> userData = new HashMap<>();
                                    userData.put("email", person.getEmail());
                                    userData.put("password", person.getPassword());
                                    userData.put("name", person.getName());
                                    userData.put("age", person.getAge());
                                    userData.put("region", person.getRegion());
                                    userData.put("city", person.getCity());
                                    userData.put("profilePictureUrl","https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png");
                                    userData.put("uid", uid);
                                    userData.put("interests", selectedInterests); // Add selected interests to userData

                                    db.collection("teenActivists").document(uid).set(userData)
                                            .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task1) {
                                                    if (task1.isSuccessful()) {
                                                        // User data saved successfully
                                                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(person.getName()).build();
                                                        user.updateProfile(profileUpdates)
                                                                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task2) {
                                                                        if (task2.isSuccessful()) {
                                                                            // User profile updated successfully
                                                                            Log.d(TAG, "User profile updated.");

                                                                            // Log the user in automatically
                                                                            mAuth.signInWithEmailAndPassword(person.getEmail(), person.getPassword())
                                                                                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<AuthResult> task3) {
                                                                                            if (task3.isSuccessful()) {
                                                                                                // User logged in successfully
                                                                                                Log.d(TAG, "User logged in successfully.");

                                                                                                // Navigate to the interests page after successfully creating the user.
                                                                                                Toast.makeText(getActivity(), "You're in", Toast.LENGTH_SHORT).show();
                                                                                                Intent intent = new Intent(getActivity(), ActivistHomeActivity.class);
                                                                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                                                startActivity(intent);
                                                                                            } else {
                                                                                                // Failed to log in
                                                                                                Log.w(TAG, "Failed to log in.", task3.getException());
                                                                                                Toast.makeText(getActivity(), "Failed to log in.", Toast.LENGTH_SHORT).show();
                                                                                            }
                                                                                        }
                                                                                    });
                                                                        } else {
                                                                            // Failed to update user profile
                                                                            Log.w(TAG, "Failed to update user profile.", task2.getException());
                                                                        }
                                                                    }
                                                                });
                                                    } else {
                                                        // Failed to save user data
                                                        Log.w(TAG, "Failed to set user data.", task1.getException());
                                                        Toast.makeText(getActivity(), "Failed to set user data.", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                } else {
                                    // User creation failed
                                    Exception exception = task.getException();
                                    if (exception instanceof FirebaseAuthUserCollisionException) {
                                        // Email is already registered
                                        Toast.makeText(getActivity(), "This email is already registered.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        // Other authentication failure
                                        Log.w(TAG, "Authentication failed.", exception);
                                        Toast.makeText(getActivity(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
            }
        });



        return view;
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

