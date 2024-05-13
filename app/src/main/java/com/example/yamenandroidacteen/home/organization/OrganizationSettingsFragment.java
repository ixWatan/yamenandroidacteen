package com.example.yamenandroidacteen.home.organization;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;



import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import com.example.yamenandroidacteen.MainActivity;
import com.example.yamenandroidacteen.R;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class OrganizationSettingsFragment extends Fragment {


    private FirebaseAuth mAuth;

    private EditText postIDEditText;
    private EditText msgTitleEditText;
    private EditText msgBodyEditText;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_organization_settings, container, false);


        // Initialize views

        TextView textViewEditProfile = view.findViewById(R.id.textViewEditProfile);
        TextView textViewSignOut = view.findViewById(R.id.textViewSignOut);



        // Set up edit profile click listener
        textViewEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToFragment(new OrganizationEditProfileFragment());
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


        ft.replace(R.id.frameLayoutOrg, fragment);


        // Add the transaction to the back stack (optional)
        ft.addToBackStack(null);


        // Commit the transaction
        ft.commit();
    }


    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        // Hide the system navigation bar when the fragment is displayed
        ((OrganizationHomeActivity) requireActivity()).hideSystemNavigationBar();
    }


}



