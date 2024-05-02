package com.example.yamenandroidacteen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.yamenandroidacteen.auth.LoginFragment;
import com.example.yamenandroidacteen.classes.other.BaseActivity;
import com.example.yamenandroidacteen.classes.other.OnboardingPagerAdapter;
import com.example.yamenandroidacteen.home.activist.ActivistHomeActivity;
import com.example.yamenandroidacteen.home.organization.OrganizationHomeActivity;
import com.example.yamenandroidacteen.slideshow.MainSlideFragment;
import com.example.yamenandroidacteen.slideshow.slideshowactivist.ActivistSlide1Fragment;
import com.example.yamenandroidacteen.slideshow.slideshowactivist.ActivistSlide2Fragment;
import com.example.yamenandroidacteen.slideshow.slideshowactivist.ActivistSlide3Fragment;
import com.example.yamenandroidacteen.slideshow.slideshowactivist.ActivistSlide4Fragment;
import com.example.yamenandroidacteen.slideshow.slideshowactivist.ActivistSlide5Fragment;
import com.example.yamenandroidacteen.slideshow.slideshoworganization.OrgSlide1Fragment;
import com.example.yamenandroidacteen.slideshow.slideshoworganization.OrgSlide2Fragment;
import com.example.yamenandroidacteen.slideshow.slideshoworganization.OrgSlide3Fragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends BaseActivity implements MainSlideFragment.OnButtonClickListener {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private Button orgBtn, actBtn;

    private View dotsOrg, dotsAct;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        FirebaseMessaging.getInstance().subscribeToTopic("notification");

        dotsAct = findViewById(R.id.layoutDotsAct);
        dotsOrg = findViewById(R.id.layoutDotsOrg);
        // -x-x-
        dotsAct.setVisibility(View.GONE);
        dotsOrg.setVisibility(View.GONE);







        checkIfFirstTime();
    }

    // Implement the interface method to handle button click events
    @Override
    public void onActivistButtonClicked() {
        // Handle activist button click event
        // Get the current fragment by its container view ID
        deleteCurrentFragment();
        showIntroductionPageActivist();
    }


    @Override
    public void onOrganizationButtonClicked() {
        // Handle organization button click event
        deleteCurrentFragment();
        showIntroductionPageOrg();
    }

    private void deleteCurrentFragment() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frameLayout);

        // Check if the current fragment is MainSlideFragment
        if (currentFragment instanceof MainSlideFragment) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.remove(currentFragment);
            transaction.commit();
        }
    }

    private void checkIfFirstTime() {
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        boolean isFirstTime = sharedPreferences.getBoolean("isFirstTime", true);
        if (isFirstTime) {
            showMainSlide();

/*
            showIntroductionPage();
*/
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isFirstTime", false);
            editor.apply();
        } else {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                autoLogin(currentUser.getUid());
            } else {
                if (findViewById(R.id.frameLayout) != null) {
                    LoginFragment loginFragment = new LoginFragment();
                    getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, loginFragment).commit();
                }
            }
        }
    }

    private void showMainSlide() {
        MainSlideFragment mainSlideFragment = new MainSlideFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, mainSlideFragment).commit();

    }

    private void showIntroductionPageActivist() {
        ViewPager viewPager = findViewById(R.id.viewPager);
        LinearLayout dotsLayout = findViewById(R.id.layoutDotsAct);

        List<Fragment> fragments = Arrays.asList(new ActivistSlide1Fragment(), new ActivistSlide2Fragment(), new ActivistSlide3Fragment(), new ActivistSlide4Fragment(), new ActivistSlide5Fragment());
        OnboardingPagerAdapter pagerAdapter = new OnboardingPagerAdapter(viewPager, getSupportFragmentManager(), fragments, dotsLayout);
        viewPager.setAdapter(pagerAdapter);

        dotsAct.setVisibility(View.VISIBLE);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                pagerAdapter.setDots(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        pagerAdapter.setDots(0); // Set initial dot position
    }

    private void showIntroductionPageOrg() {
        ViewPager viewPager = findViewById(R.id.viewPager);
        LinearLayout dotsLayout = findViewById(R.id.layoutDotsOrg);

        List<Fragment> fragments = Arrays.asList(new OrgSlide1Fragment(), new OrgSlide2Fragment(), new OrgSlide3Fragment());
        OnboardingPagerAdapter pagerAdapter = new OnboardingPagerAdapter(viewPager, getSupportFragmentManager(), fragments, dotsLayout);
        viewPager.setAdapter(pagerAdapter);

        dotsOrg.setVisibility(View.VISIBLE);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                pagerAdapter.setDots(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        pagerAdapter.setDots(0); // Set initial dot position
    }

    private void autoLogin(String userId) {
        db.collection("teenActivists").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        startActivity(new Intent(this, ActivistHomeActivity.class));
                        finish();
                    } else {
                        startActivity(new Intent(this, OrganizationHomeActivity.class));
                        finish();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error fetching user data!", Toast.LENGTH_SHORT).show();
                    LoginFragment loginFragment = new LoginFragment();
                    getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, loginFragment).commit();
                });
    }


    public void hideViewPagerAndDotsAct() {
        // Find the ViewPager and dots layout in the activity's layout
        ViewPager viewPager = findViewById(R.id.viewPager);
        LinearLayout dotsLayout = findViewById(R.id.layoutDotsAct);

        // Hide the ViewPager and dots layout
        viewPager.setVisibility(View.GONE);
        dotsLayout.setVisibility(View.GONE);
    }

    public void hideViewPagerAndDotsOrg() {
        // Find the ViewPager and dots layout in the activity's layout
        ViewPager viewPager = findViewById(R.id.viewPager);
        LinearLayout dotsLayout = findViewById(R.id.layoutDotsOrg);

        // Hide the ViewPager and dots layout
        viewPager.setVisibility(View.GONE);
        dotsLayout.setVisibility(View.GONE);
    }
}
