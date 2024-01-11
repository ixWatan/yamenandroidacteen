package com.example.yamenandroidacteen.home.activist;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.yamenandroidacteen.MainActivity;
import com.example.yamenandroidacteen.R;
import com.google.firebase.auth.FirebaseAuth;


public class ActivistProfileFragment extends Fragment {

    Button signOutBtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_activist_profile, container, false);

        signOutBtn = view.findViewById(R.id.signoutBtn);

        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    private void signOut() {
        // Sign out from Firebase Authentication
        FirebaseAuth.getInstance().signOut();

        // Show a toast message
        Toast.makeText(getActivity(), "Signed out successfully", Toast.LENGTH_SHORT).show();


        // need to transition to fragment

        Intent intent = new Intent(getActivity(), MainActivity.class);

        // Set flags to clear the activity stack and create a new task and prevent coming back after sign out
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);

    }
}