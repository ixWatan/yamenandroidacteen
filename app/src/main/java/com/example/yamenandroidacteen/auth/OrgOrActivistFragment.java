package com.example.yamenandroidacteen.auth;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.yamenandroidacteen.R;
import com.example.yamenandroidacteen.auth.activist.SignupActivistFragment;
import com.example.yamenandroidacteen.auth.organization.SignupOrganizationFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrgOrActivistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrgOrActivistFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView TvCreateAccount;

    private Button goToSignupActivistBtn;
    private Button goToSignupOrganizationBtn;

    private TextView TvGoToLogin;

    public OrgOrActivistFragment() {
        // Required empty public constructor
    }

    public static OrgOrActivistFragment newInstance(String param1, String param2) {
        OrgOrActivistFragment fragment = new OrgOrActivistFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_org_or_activist, container, false);

        TvGoToLogin = view.findViewById(R.id.gotologin);
        goToSignupActivistBtn = view.findViewById(R.id.goToSignUpActivist);
        goToSignupOrganizationBtn = view.findViewById(R.id.goToSignUpOrganization);


        TvGoToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToLogin();
            }
        });

        goToSignupActivistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToSignUpActivist();
            }
        });

        goToSignupOrganizationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToSignUpOrganization();
            }
        });

        return view;

    }

    public void goToSignUpActivist() {
        Fragment newFragment = new SignupActivistFragment();

        // Begin the transaction
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

        ft.replace(R.id.frameLayout, newFragment);

        // Add the transaction to the back stack (optional)
        ft.addToBackStack(null);

        // Commit the transaction
        ft.commit();
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

    public void goToSignUpOrganization() {
        Fragment newFragment = new SignupOrganizationFragment();

        // Begin the transaction
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

        ft.replace(R.id.frameLayout, newFragment);

        // Add the transaction to the back stack (optional)
        ft.addToBackStack(null);

        // Commit the transaction
        ft.commit();
    }
}