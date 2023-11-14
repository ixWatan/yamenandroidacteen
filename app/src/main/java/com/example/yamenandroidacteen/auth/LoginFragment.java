package com.example.yamenandroidacteen.auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginFragment extends Fragment {

    private static final String TAG = "LoginFragment";

    private FirebaseAuth mAuth;
    private EditText email;
    private EditText password;
    private ProgressDialog pd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        mAuth = FirebaseAuth.getInstance();
        email = view.findViewById(R.id.signin_email);
        password = view.findViewById(R.id.signup_pass);

        // Other initialization code can go here

        return view;
    }

    public void loginUser(View view) {
        String emailText = email.getText().toString();
        String passwordText = password.getText().toString();
        // Call the method from the original MainActivity to handle login
        ((MainActivity) requireActivity()).loginUser(emailText, passwordText);
    }

    public void goToOrgOrActActivity(View view) {
        Intent intent = new Intent(getActivity(), OrgOrActActivity.class);
        startActivity(intent);
        requireActivity().overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
    }

    public void goToSignUp(View view) {
        Intent intent = new Intent(getActivity(), SignUpActivity.class);
        startActivity(intent);
        requireActivity().overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
    }
}