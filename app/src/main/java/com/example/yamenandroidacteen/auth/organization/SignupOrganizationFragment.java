package com.example.yamenandroidacteen.auth.organization;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yamenandroidacteen.R;
import com.example.yamenandroidacteen.auth.LoginFragment;
import com.example.yamenandroidacteen.home.activist.ActivistHomeActivity;
import com.example.yamenandroidacteen.home.organization.OrganizationHomeActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class SignupOrganizationFragment extends Fragment {
    private static final String TAG = "SignUpActivity";
    private FirebaseAuth mAuth;

    private CheckBox checkBox;

    private Boolean checkBoxState;

    private ProgressDialog pd;


    //1
    EditText email;

    //2
    EditText descriptionOfOrg;

    //3
    EditText password;

    //4
    EditText phoneNum;

    //5
    EditText orgname;

    //6
    EditText websiteLink;

    //7

    String  selectedType, orgTypeStr, descriptionStr;

    String[] typeList = {"Environment" , "Volunteering", "Protests", "Women's Rights", "Human Rights","Racism", "LGBTQ+", "Animals", "Petitions", "Education"};

    AutoCompleteTextView autoCompleteTextView;

    ArrayAdapter<String> adapterItems;

    private TextView checkBoxText;

    private TextView guideLinesTv;

    private View goToLoginBtn;

    Button signupBtn;

    private EditText password1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_signup_organization, container, false);

        this.checkBoxText = view.findViewById(R.id.checkBoxText);
        this.checkBox = view.findViewById(R.id.simpleCheckBox);
        mAuth = FirebaseAuth.getInstance();

        email = view.findViewById(R.id.signup_email);
        password = view.findViewById(R.id.signup_pass);
        orgname = view.findViewById(R.id.signup_orgname);
        phoneNum = view.findViewById(R.id.signup_phone);
        websiteLink = view.findViewById(R.id.OrgWebLink);
        descriptionOfOrg = view.findViewById(R.id.DescOfOrg);
        orgTypeStr = selectedType;
        descriptionStr = descriptionOfOrg.getText().toString();






        // drop down menu for regions
        autoCompleteTextView = view.findViewById(R.id.autocomplete_Tv);
        adapterItems = new ArrayAdapter<String>( view.getContext() , R.layout.list_item, typeList);

        autoCompleteTextView.setAdapter(adapterItems);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                selectedType = adapterView.getItemAtPosition(position).toString();
                Toast.makeText(getActivity(), "Region:" + selectedType, Toast.LENGTH_SHORT).show();


            }
        });


        //7
        descriptionOfOrg = view.findViewById(R.id.DescOfOrg);

        ProgressDialog pd;

        goToLoginBtn = view.findViewById(R.id.goToLoginLayout2);

        goToLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToLogin();
            }
        });

        guideLinesTv = view.findViewById(R.id.checkBoxText);
        guideLinesTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenTermsAndGuidelines();
            }
        });

        signupBtn = view.findViewById(R.id.signupOrg);

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToHome();
            }
        });




        // Inflate the layout for this fragment
        return view;
    }

    public void OpenTermsAndGuidelines(View view) {
        String url = getString(R.string.privacy_policy);
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    public void goToLogin() {
        Fragment newFragment = new LoginFragment();

        // Begin the transaction
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

        ft.replace(R.id.frameLayout, newFragment);

        // Add the transaction to the back stack (optional)
        ft.addToBackStack(null);

        // Commit the transaction
        ft.commit();
    }

    public void OpenTermsAndGuidelines() {
        String url = getString(R.string.privacy_policy);
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    public void goToHome() {

        //1
        String emailStr = email.getText().toString();

        //2
        String passwordStr = password.getText().toString();

        //3
        String orgNameStr = orgname.getText().toString();

        //4
        String phoneNumStr = phoneNum.getText().toString();

        //5
        String orgTypeStr = selectedType;

        //6
        String descriptionStr = descriptionOfOrg.getText().toString();

        //7
        String webLink = websiteLink.getText().toString();


        Boolean checkBoxState = checkBox.isChecked();

        if (emailStr.isEmpty() || passwordStr.isEmpty() || orgNameStr.isEmpty() || phoneNumStr.isEmpty() || orgTypeStr.isEmpty() || webLink.isEmpty() || descriptionStr.isEmpty() || checkBoxState == false) {
            Toast.makeText(getActivity(), "Please enter Organization name, Type Of Organization, email, password, Phone Number, Website Link and Description Of The WebSite", Toast.LENGTH_SHORT).show();
        } else {
            create_user(emailStr, passwordStr, orgNameStr, phoneNumStr, orgTypeStr, webLink, descriptionStr);
        }
    }

    public void create_user(String email, String password, String name, String phoneNumStr, String orgTypeStr, String webLink, String descriptionStr) {
        pd = new ProgressDialog(getActivity());
        pd.setMessage("Creating Account...");
        pd.show();
        {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(getActivity(), task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            String uid = user.getUid();
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            Map<String, Object> userData = new HashMap<>();


                            //1
                            userData.put("email", email);

                            //2
                            userData.put("password", password);

                            //3
                            userData.put("organization_name", name);

                            //4
                            userData.put("phone",phoneNumStr );

                            //5
                            userData.put("description", descriptionStr);

                            //6
                            userData.put("website Link", webLink);

                            //7
                            userData.put("orgType", orgTypeStr);

                            userData.put("profilePictureUrl", "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png");
                            userData.put("uid", uid);

                            db.collection("organizations").document(uid)   // Changed the collection to "organizations"
                                    .set(userData)
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                    .setDisplayName(name)
                                                    .build();
                                            user.updateProfile(profileUpdates)
                                                    .addOnCompleteListener(task2 -> {
                                                        if (task2.isSuccessful()) {
                                                            Log.d(TAG, "User profile updated.");
                                                        }
                                                    });

                                            // Navigate to the interests page after successfully creating the user.
                                            Toast.makeText(getActivity(), "You're in", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getActivity(), OrganizationHomeActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);


                                        } else {
                                            Log.w(TAG, "set:failure", task1.getException());
                                            Toast.makeText(getActivity(), "Failed to set user data.", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                        } else {
                            // Check the type of exception
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(getActivity(), "This email is already registered.", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(getActivity(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }


    }
}