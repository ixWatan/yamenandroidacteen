package com.example.yamenandroidacteen.classes.other;

import android.transition.Fade;
import android.transition.Slide;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.yamenandroidacteen.R;

import java.util.List;


public class OnboardingPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;
    private LinearLayout dotsLayout;
    private ViewPager viewPager;

    public OnboardingPagerAdapter(ViewPager viewPager, FragmentManager fm, List<Fragment> fragments, LinearLayout dotsLayout) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.viewPager = viewPager;
        this.fragments = fragments;
        this.dotsLayout = dotsLayout;
        viewPager.setAdapter(this); // Set adapter to the ViewPager
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                setDots(position); // Update dots when page changes
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public void setDots(int position) {
        for (int i = 0; i < dotsLayout.getChildCount(); i++) {
            ImageView dot = (ImageView) dotsLayout.getChildAt(i);
            dot.setImageResource(i == position ? R.drawable.dot_active : R.drawable.dot_inactive);
        }
    }
}

