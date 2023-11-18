package com.example.yamenandroidacteen.auth;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.app.ProgressDialog;
import android.widget.Button;
import android.widget.FrameLayout;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.yamenandroidacteen.R;
import com.example.yamenandroidacteen.home.activist.ActivistHomeActivity;
import com.example.yamenandroidacteen.home.organization.OrganizationHomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class LoginFragment extends Fragment {

    private static final String TAG = "LoginFragment";

    private FirebaseAuth mAuth;


    private EditText email;
    private EditText password;
    private TextView TvCreateAccount;

    private Button loginBtn;

    private ProgressDialog pd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        mAuth = FirebaseAuth.getInstance();
        email = view.findViewById(R.id.signin_email);
        password = view.findViewById(R.id.signup_pass);

        loginBtn = view.findViewById(R.id.signin);
        TvCreateAccount = view.findViewById(R.id.createnewaccount);


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToHome();
            }
        });

        TvCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToOrgOrActActivity();
            }
        });



        return view;
    }

    public void goToHome() {
        loginUser(email.getText().toString(), password.getText().toString());
    }

    public void goToOrgOrActActivity() {
        // Create a new fragment instance
        Fragment newFragment = new OrgOrActivistFragment();

        // Begin the transaction
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

        ft.replace(R.id.frameLayout, newFragment);

        // Add the transaction to the back stack (optional)
        ft.addToBackStack(null);

        // Commit the transaction
        ft.commit();
    }


    private void loginUser(String email, String password) {

        pd = new ProgressDialog(getActivity());
        pd.setMessage("Signing User ...");
        pd.show();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getActivity(), "Email and password cannot be empty.", Toast.LENGTH_SHORT).show();
            return;
        }


        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) getContext(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                db.collection("teenActivists")
                                        .document(user.getUid())
                                        .get()
                                        .addOnCompleteListener(task1 -> {
                                            if (task1.isSuccessful()) {
                                                DocumentSnapshot document = task1.getResult();
                                                if (document != null && document.exists()) {
                                                    String storedEmail = document.getString("email");
                                                    if (email.equalsIgnoreCase(storedEmail)) {
                                                        // User's email is in the system
                                                        // Proceed to the next activity
                                                        Intent intent = new Intent(getActivity(), ActivistHomeActivity.class);
                                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                        startActivity(intent);
                                                    } else {
                                                        checkForOrganizationUser(email, user.getUid());
                                                    }
                                                } else {
                                                    checkForOrganizationUser(email, user.getUid());
                                                }
                                            } else {
                                                Log.d(TAG, "Failed to get user document.", task1.getException());
                                            }
                                        });
                            }
                        } else {
                            Snackbar.make(getActivity().findViewById(android.R.id.content),
                                    Objects.requireNonNull(task.getException()).getLocalizedMessage(), Snackbar.LENGTH_LONG).show();
                        }

                        pd.dismiss();
                    }
                });
    }

    private void checkForOrganizationUser(String email, String uid) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("organizations")
                .document(uid)
                .get()
                .addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        DocumentSnapshot document = task1.getResult();
                        if (document != null && document.exists()) {
                            String storedEmail = document.getString("email");
                            if (email.equalsIgnoreCase(storedEmail)) {
                                // User's email is in the system
                                // Proceed to the next activity
                                Intent intent = new Intent(getActivity(), OrganizationHomeActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);

                            } else {
                                // User's email is not in the system
                                Toast.makeText(getActivity(),
                                        "Your email is not registered.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // User's email is not in the system
                            Toast.makeText(getActivity(),
                                    "Your email is not registered.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.d(TAG, "Failed to get user document.", task1.getException());
                    }
                });
    }




}
