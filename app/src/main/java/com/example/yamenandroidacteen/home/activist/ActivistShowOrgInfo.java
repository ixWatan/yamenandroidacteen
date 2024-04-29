package com.example.yamenandroidacteen.home.activist;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Intent;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.yamenandroidacteen.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class ActivistShowOrgInfo extends Fragment {

    private FirebaseFirestore db;

    String orgNameData;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_activist_show_org_info, container, false);



        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Get the organization name from the arguments
        Bundle bundle = getArguments();
        if (bundle != null) {
            orgNameData = bundle.getString("orgName");
            // Use orgName as needed
        }

        // Get the organization name from the arguments


        // Retrieve the organization data from Firestore
        db.collection("organizations")
                .whereEqualTo("organization_name", orgNameData)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        QuerySnapshot querySnapshot = task.getResult();
                        if (!querySnapshot.isEmpty()) {
                            DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                            Map<String, Object> orgData = documentSnapshot.getData();

                            // Extract the organization data from the map
                            String description = (String) orgData.get("description");
                            String email = (String) orgData.get("email");
                            String orgType = (String) orgData.get("orgType");
                            String password = (String) orgData.get("password");
                            String phone = (String) orgData.get("phone");
                            String profilePictureUrl = (String) orgData.get("profilePictureUrl");
                            String uid = (String) orgData.get("uid");
                            String websiteLink = (String) orgData.get("website Link");

                            ImageView orgProfilePicImageView = view.findViewById(R.id.imageViewProfilePictureOrgInfo);
                            Glide.with(requireContext()).load(profilePictureUrl).into(orgProfilePicImageView);


                            // Display the organization data in the UI
                            TextView orgNameTextView = view.findViewById(R.id.orgNameValueTextView2);
                            orgNameTextView.setText(orgNameData);

                            TextView orgTypeTextView = view.findViewById(R.id.orgTypeValueTextView2);
                            orgTypeTextView.setText(orgType);

                            TextView descriptionTextView = view.findViewById(R.id.descriptionValueTextView2);
                            descriptionTextView.setText(description);

                            TextView emailTextView = view.findViewById(R.id.emailValueTextView2);
                            emailTextView.setText(email);

                            TextView phoneTextView = view.findViewById(R.id.phoneValueTextView2);
                            phoneTextView.setText(phone);

                            TextView websiteTextView = view.findViewById(R.id.websiteValueTextView2);
                            websiteTextView.setText(websiteLink);




                        } else {
                            // Organization not found
                            showToast("Organization not found.");
                        }
                    } else {
                        // Error retrieving the organization data
                        showToast("Error retrieving organization data: " + task.getException().getMessage());
                    }
                });



        return view;
    }

    public void onEmailClicked(View view) {
        String email = ((TextView) view).getText().toString();
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + email));
        startActivity(intent);
    }

    // Handle phone click
    public void onPhoneClicked(View view) {
        String phone = ((TextView) view).getText().toString();
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phone));
        startActivity(intent);
    }

    // Handle website click
    public void onWebsiteClicked(View view) {
        String website = ((TextView) view).getText().toString();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(website));
        startActivity(intent);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Check if the hosting activity's fragment manager contains the fragment
        Fragment fragment = requireActivity().getSupportFragmentManager().findFragmentByTag("ActivistShowPostFragment");

        if (fragment != null && fragment instanceof ActivistShowPostFragment) {
            ActivistShowPostFragment showPostFragment = (ActivistShowPostFragment) fragment;
            if (showPostFragment.wentToShowInfo) {
                showPostFragment.testTest = "fromShowInfo";
            }
        }

    }

    private void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }





}
