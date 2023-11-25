package com.example.yamenandroidacteen.auth.activist;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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
import com.example.yamenandroidacteen.classes.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignupActivistFragment extends Fragment {

    private static final String TAG = "SignUpActivity";
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
    private FirebaseAuth mAuth;
    private EditText emailEditText;

    private ProgressDialog pd;

    String[] regionsList = {"Jerusalem", "Northern District", "Haifa", "West Bank", "Central District", "Tel Aviv", "Southern District"};

    AutoCompleteTextView autoCompleteTextView;

    ArrayAdapter<String> adapterItems;


    private CheckBox checkBox;

    private Button ContinueBtn;

    private Boolean checkBoxState;

    private TextView checkBoxText;

    private TextView guideLinesTv;

    private EditText passwordEditText;
    private EditText nameEditText;
    private EditText ageEditText;
    private EditText cityEditText;
    private EditText password;

    String selectedRegion;

    private User person = new User(null, null, null, null, null, null);


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_signup_activist, container, false);

        mAuth = FirebaseAuth.getInstance();


        // drop down menu for regions
        autoCompleteTextView = view.findViewById(R.id.autocomplete_Tv);
        adapterItems = new ArrayAdapter<String>(view.getContext(), R.layout.list_item, regionsList);

        autoCompleteTextView.setAdapter(adapterItems);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                selectedRegion = adapterView.getItemAtPosition(position).toString();
                Toast.makeText(getActivity(), "Region:" + selectedRegion, Toast.LENGTH_SHORT).show();


            }
        });

        guideLinesTv = view.findViewById(R.id.checkBoxText);
        guideLinesTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenTermsAndGuidelines();
            }
        });

        ContinueBtn = view.findViewById(R.id.continueBtn);

        ContinueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToHome();
            }
        });





        checkBox = view.findViewById(R.id.simpleCheckBox);
        emailEditText = view.findViewById(R.id.signup_email);
        passwordEditText = view.findViewById(R.id.signup_pass);
        nameEditText = view.findViewById(R.id.signup_name);
        ageEditText = view.findViewById(R.id.signup_age);
        cityEditText = view.findViewById(R.id.signup_city);
        password = view.findViewById(R.id.signup_pass);

        ProgressDialog pd;
        // Inflate the layout for this fragment
        return view;
    }

    public void goToHome() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String name = nameEditText.getText().toString();
        String age = ageEditText.getText().toString();
        String city = cityEditText.getText().toString();

        Boolean checkBoxState = checkBox.isChecked();

        if (email.isEmpty() || password.isEmpty() || name.isEmpty() || age.isEmpty() || selectedRegion.isEmpty() || city.isEmpty() || checkBoxState == false) {
            Toast.makeText(getActivity(), "Please enter email, password, name .., Or accept terms and conditions", Toast.LENGTH_SHORT).show();
        } else {
            this.person.setEmail(email);
            this.person.setPassword(password);
            this.person.setName(name);
            this.person.setAge(age);
            this.person.setCity(city);
            this.person.setRegion(selectedRegion);


            autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    String regionsList = adapterView.getItemAtPosition(position).toString();

                }
            });

            createUser(person);
        }
    }

    public void OpenTermsAndGuidelines() {
        String url = getString(R.string.privacy_policy);
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }


    public void createUser(User person) {

        // Reference for InterestsFragment and passing data to it (User Info)
        InterestsFragment interestsFragment = new InterestsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("User", person);
        interestsFragment.setArguments(bundle);

        // transition to interests fragment
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayout, interestsFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

}