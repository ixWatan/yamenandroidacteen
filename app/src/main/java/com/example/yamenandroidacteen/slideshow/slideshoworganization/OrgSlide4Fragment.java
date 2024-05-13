package com.example.yamenandroidacteen.slideshow.slideshoworganization;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.yamenandroidacteen.MainActivity;
import com.example.yamenandroidacteen.R;
import com.example.yamenandroidacteen.auth.LoginFragment;
import com.example.yamenandroidacteen.auth.organization.SignupOrganizationFragment;
import com.example.yamenandroidacteen.home.organization.OrganizationHomeActivity;


public class OrgSlide4Fragment extends Fragment {



    private Button login,signup;

    View view;

    ViewPager viewPager;
    LinearLayout dotsLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_org_slide4, container, false);


        login = view.findViewById(R.id.LoginBtnSlideOrg);
        signup = view.findViewById(R.id.SignupBtnSlideOrg);

        viewPager = view.findViewById(R.id.viewPager);
        dotsLayout = view.findViewById(R.id.layoutDotsOrg);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.hideViewPagerAndDotsOrg();

                navigateToFragment(new LoginFragment());
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.hideViewPagerAndDotsOrg();

                navigateToFragment(new SignupOrganizationFragment());
            }
        });

        return view;
    }

    public void navigateToFragment(Fragment fragment) {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            Fragment currentFragment = fragmentManager.findFragmentById(R.id.frameLayout);

            setFirstTimeToFalse();

            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.frameLayout, fragment);
            ft.addToBackStack(null);
            ft.commit();
        }
    }

    public void setFirstTimeToFalse() {
        ((MainActivity) requireActivity()).setFirstTimeToFalse();
    }

}