package com.example.yamenandroidacteen.auth.activist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.yamenandroidacteen.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ActivistEditProfileFragment extends Fragment {

    private EditText editTextAge;
    private EditText editTextCity;
    private EditText editTextEmail;
    private EditText editTextName;
    private EditText editTextPassword;
    private EditText editTextRegion;
    private ImageView imageViewProfilePicture;
    private Button buttonSave;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activist_edit_profile, container, false);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser();

        // Initialize views
        editTextAge = view.findViewById(R.id.editTextAge);
        editTextCity = view.findViewById(R.id.editTextCity);
        editTextEmail = view.findViewById(R.id.editTextEmail);
        editTextName = view.findViewById(R.id.editTextName);
        editTextPassword = view.findViewById(R.id.editTextPassword);
        editTextRegion = view.findViewById(R.id.editTextRegion);
        imageViewProfilePicture = view.findViewById(R.id.imageViewProfilePicture);
        buttonSave = view.findViewById(R.id.buttonSave);

        // Fetch and populate user data from Firestore
        fetchAndPopulateUserData();

        // Set click listener for save button
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle save button click
            }
        });

        return view;
    }

    private void fetchAndPopulateUserData() {
        if (currentUser != null) {
            String userId = currentUser.getUid();
            db.collection("teenActivists").document(userId).get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null && document.exists()) {
                                // Populate EditTexts with user data
                                editTextAge.setText(document.getString("age"));
                                editTextCity.setText(document.getString("city"));
                                editTextEmail.setText(document.getString("email"));
                                editTextName.setText(document.getString("name"));
                                editTextPassword.setText(document.getString("password"));
                                editTextRegion.setText(document.getString("region"));


                                String profilePictureUrl = document.getString("profilePictureUrl");
                                Glide.with(requireContext()).load(profilePictureUrl).into(imageViewProfilePicture);
                            }
                        } else {
                            // Error fetching document
                        }
                    });
        }
    }
}
