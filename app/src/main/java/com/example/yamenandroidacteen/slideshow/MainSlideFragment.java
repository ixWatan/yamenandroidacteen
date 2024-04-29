package com.example.yamenandroidacteen.slideshow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.yamenandroidacteen.R;

public class MainSlideFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_slide, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        View activistButton = view.findViewById(R.id.ActivistBtn);
        View organizationButton = view.findViewById(R.id.OrganizationBtn);

        // Set onClickListener for the activist button
        activistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the activist fragment with a slide animation
/*
                Navigation.findNavController(view).navigate(R.id.activistSlide1);
*/
            }
        });

        // Set onClickListener for the organization button
        organizationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the organization fragment with a slide animation

/*

                Navigation.findNavController(view).navigate(R.id.orgSlide1);
*/


            }
        });
    }
}
