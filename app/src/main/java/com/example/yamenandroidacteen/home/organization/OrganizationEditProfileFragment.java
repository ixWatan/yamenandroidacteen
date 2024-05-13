package com.example.yamenandroidacteen.home.organization;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.bumptech.glide.Glide;
import com.example.yamenandroidacteen.R;
import com.example.yamenandroidacteen.classes.other.CropperActivity;
import com.example.yamenandroidacteen.home.activist.ActivistHomeActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.util.HashMap;
import java.util.Map;


public class OrganizationEditProfileFragment extends Fragment {


    private EditText editTextPhone;
    private EditText editTextDescription;
    private EditText editTextEmail;
    private EditText editTextName;
    private EditText editTextPassword;
    private EditText editTextWeb;
    private ImageView imageViewProfilePicture;
    private Button buttonSave;


    Uri resultUri;
    ActivityResultLauncher<String> mGetContent;




    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;

    ProgressDialog pd;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_organization_edit_profile, container, false);


        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser();
        pd = new ProgressDialog(getActivity());


        // Initialize views
        editTextPhone= view.findViewById(R.id.editTextPhone);
        editTextDescription = view.findViewById(R.id.editTextDescription);
        editTextEmail = view.findViewById(R.id.editTextEmail);
        editTextName = view.findViewById(R.id.editTextName);
        editTextPassword = view.findViewById(R.id.editTextPassword);
        editTextWeb = view.findViewById(R.id.editTextWeb);
        imageViewProfilePicture = view.findViewById(R.id.imageViewProfilePicture);
        buttonSave = view.findViewById(R.id.buttonSave);


        // Fetch and populate user data from Firestore
        fetchAndPopulateUserData();


        // Set click listener for save button
        // Set click listener for save button
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get updated data from EditText fields
                String phone = editTextPhone.getText().toString().trim();
                String description = editTextDescription.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                String name = editTextName.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                String web = editTextWeb.getText().toString().trim();


                // Update user data in Firestore
                updateUserData(phone, description, email, name, password, web);
            }
        });
        // Inside your fragment's onCreateView() method after initializing views
        LinearLayout linearLayoutEditProfilePicture = view.findViewById(R.id.linearLayoutEditProfilePictureOrg);
        linearLayoutEditProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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


    private void fetchAndPopulateUserData() {
        if (currentUser != null) {
            String userId = currentUser.getUid();
            db.collection("organizations").document(userId).get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null && document.exists()) {
                                // Populate EditTexts with user data
                                // Inside fetchAndPopulateUserData() method
                                editTextPhone.setText(document.getString("phone"));
                                editTextDescription.setText(document.getString("description"));
                                editTextEmail.setText(document.getString("email"));
                                editTextName.setText(document.getString("organization_name"));
                                editTextPassword.setText(document.getString("password"));
                                editTextWeb.setText(document.getString("website Link"));





                                String profilePictureUrl = document.getString("profilePictureUrl");
                                Glide.with(requireContext()).load(profilePictureUrl).into(imageViewProfilePicture);
                            }
                        } else {
                            // Error fetching document
                        }
                    });
        }
    }


    private void updateUserData(String phone, String description, String email, String name, String password, String web) {
        pd.setMessage("Updating Info..");
        pd.show();
        if (currentUser != null) {
            String userId = currentUser.getUid();


            // Check if there's a new profile picture to upload
            if (resultUri != null) {
                // Create a reference to the profile picture in Firebase Storage
                StorageReference profilePictureRef = FirebaseStorage.getInstance().getReference()
                        .child("profile_pictures")
                        .child(userId + ".jpg");


                // Upload the profile picture to Firebase Storage
                profilePictureRef.putFile(resultUri)
                        .addOnSuccessListener(taskSnapshot -> {
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isSuccessful()) ;


                            String downloadUri = uriTask.getResult().toString();




                            // Get the download URL of the uploaded image
                            profilePictureRef.getDownloadUrl()
                                    .addOnSuccessListener(uri -> {
                                        String profilUePictureUrl = downloadUri;


                                        // Update user data in Firestore with the new profile picture URL
                                        updateUserDataInFirestore(userId, phone, description, email, name, password, web, profilUePictureUrl);
                                    })
                                    .addOnFailureListener(e -> {
                                        // Handle failure to get download URL
                                        Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        })
                        .addOnFailureListener(e -> {
                            // Handle failure to upload profile picture
                            Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            } else {
                // If there's no new profile picture, update user data in Firestore without uploading the image
                updateUserDataInFirestore(userId, phone, description, email, name, password, web, null);
            }
        }
    }


    private void updateUserDataInFirestore(String userId, String phone, String description, String email, String name, String password, String web, String profilePictureUrl) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("phone", phone);
        userData.put("description", description);
        userData.put("email", email);
        userData.put("organization_name", name);
        userData.put("password", password);
        userData.put("website Link", web);
        if (profilePictureUrl != null) {
            userData.put("profilePictureUrl", profilePictureUrl);
        }


        // Update user data in Firestore
        db.collection("organizations").document(userId)
                .update(userData)
                .addOnSuccessListener(aVoid -> {
                    pd.dismiss();
                    Toast.makeText(getContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Failed to update profile", Toast.LENGTH_SHORT).show();
                });
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        // Hide the system navigation bar when the fragment is displayed
        ((OrganizationHomeActivity) requireActivity()).hideSystemNavigationBar();
    }





    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == -1 && requestCode == 101) {
            String result = data.getStringExtra("RESULT");
            resultUri = null;
            if(result != null) {
                resultUri = Uri.parse(result);
            }


            imageViewProfilePicture.setImageURI(resultUri);
        }
    }






}

