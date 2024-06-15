package com.example.yamenandroidacteen.home.activist;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.yamenandroidacteen.MainActivity;
import com.example.yamenandroidacteen.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;

public class ActivistSettingsFragment extends Fragment {

    private FirebaseAuth mAuth;



    private MaterialAlertDialogBuilder dialogBuilder; // Declare dialogBuilder variable
    private AlertDialog dialog; // Declare dialog variable

    public boolean isActSettings;
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

        ImageButton backBtn = view.findViewById(R.id.backButtonSignupAct);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requireActivity().getSupportFragmentManager().popBackStack();
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
                isActSettings = true;
                ((ActivistHomeActivity) requireActivity()).navigateToFragmentWithAnimation(new ActivistEditProfileFragment());

            }
        });

        // Set up sign out click listener
        textViewSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmationAlertSignout();
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

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Hide the system navigation bar when the fragment is displayed
        ((ActivistHomeActivity) requireActivity()).hideSystemNavigationBar();
    }

    private void showConfirmationAlertSignout() {

        dialogBuilder = new MaterialAlertDialogBuilder(getActivity())
                .setTitle("Are You Sure?")
                .setMessage("Are you sure that you want to signout?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle discard button click
                        dialog.dismiss(); // Dismiss the dialog

                        //logout
                        mAuth.signOut();
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                        getActivity().finish();

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle discard button click
                        dialog.dismiss(); // Dismiss the dialog
                    }
                })
                .setCancelable(false);

        dialog = dialogBuilder.create(); // Create the dialog

        dialog.show(); // Show the dialog
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if(isActSettings) {
            Toast.makeText(getActivity(), "maintaining nav hidden", Toast.LENGTH_SHORT).show();
        } else {
            ((ActivistHomeActivity) requireActivity()).showSystemNavigationBar();
        }


    }
}
