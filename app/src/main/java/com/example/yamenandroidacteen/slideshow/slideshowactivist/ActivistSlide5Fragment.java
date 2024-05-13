package com.example.yamenandroidacteen.slideshow.slideshowactivist;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.yamenandroidacteen.MainActivity;
import com.example.yamenandroidacteen.R;
import com.example.yamenandroidacteen.auth.LoginFragment;
import com.example.yamenandroidacteen.auth.activist.SignupActivistFragment;
import com.example.yamenandroidacteen.slideshow.MainSlideFragment;


public class ActivistSlide5Fragment extends Fragment {


    private Button login,signup;

    ViewPager viewPager;
    LinearLayout dotsLayout;

    private SharedPreferences sharedPreferences;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_activist_slide5, container, false);

        login = view.findViewById(R.id.LoginBtnSlideAct);
        signup = view.findViewById(R.id.SignupBtnSlideAct);




        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    MainActivity mainActivity = (MainActivity) getActivity();
                    mainActivity.hideViewPagerAndDotsAct();

                navigateToFragment(new LoginFragment());

            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    MainActivity mainActivity = (MainActivity) getActivity();
                    mainActivity.hideViewPagerAndDotsAct();
                navigateToFragment(new SignupActivistFragment());

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