package com.example.yamenandroidacteen.auth.activist;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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

    private Boolean checkBoxState;

    private TextView checkBoxText;
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


        checkBoxText = view.findViewById(R.id.checkBoxText);
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
}