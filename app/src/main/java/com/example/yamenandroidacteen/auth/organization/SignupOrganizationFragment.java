package com.example.yamenandroidacteen.auth.organization;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
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
import com.google.firebase.auth.FirebaseAuth;


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

    String  selectedType;

    String[] typeList = {"Environment" , "Volunteering", "Protests", "Women's Rights", "Human Rights","Racism", "LGBTQ+", "Animals", "Petitions", "Education"};

    AutoCompleteTextView autoCompleteTextView;

    ArrayAdapter<String> adapterItems;

    private TextView checkBoxText;

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



        // drop down menu for regions
        autoCompleteTextView = view.findViewById(R.id.autocomplete_Tv);
        adapterItems = new ArrayAdapter<String>( view.getContext() , R.layout.list_item, typeList);

        autoCompleteTextView.setAdapter(adapterItems);


        //7
        descriptionOfOrg = view.findViewById(R.id.DescOfOrg);

        ProgressDialog pd;




        // Inflate the layout for this fragment
        return view;
    }

    public void OpenTermsAndGuidelines(View view) {
        String url = getString(R.string.privacy_policy);
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }
}