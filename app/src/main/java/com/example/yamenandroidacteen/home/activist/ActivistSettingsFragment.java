package com.example.yamenandroidacteen.home.activist;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.yamenandroidacteen.MainActivity;
import com.example.yamenandroidacteen.R;
import com.google.firebase.auth.FirebaseAuth;

public class ActivistSettingsFragment extends Fragment {

    private FirebaseAuth mAuth;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activist_settings, container, false);

        // Initialize views
        Switch switchNotifications = view.findViewById(R.id.switchNotifications);
        Switch switchReminders = view.findViewById(R.id.switchReminders);
        TextView textViewEditProfile = view.findViewById(R.id.textViewEditProfile);
        TextView textViewSignOut = view.findViewById(R.id.textViewSignOut);

        // Set up switches
        switchNotifications.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Handle notifications enabling/disabling
            }
        });

        switchReminders.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Handle reminders enabling/disabling
            }
        });

        // Set up edit profile click listener
        textViewEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToFragment(new com.example.yamenandroidacteen.auth.activist.ActivistEditProfileFragment());
            }
        });

        // Set up sign out click listener
        textViewSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                // Redirect user to the main activity
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                // Finish the current activity to prevent the user from navigating back to it
                getActivity().finish();
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
