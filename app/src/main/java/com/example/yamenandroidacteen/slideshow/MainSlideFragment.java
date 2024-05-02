package com.example.yamenandroidacteen.slideshow;

import android.content.Context;
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

    // Define an interface for button click callbacks
    public interface OnButtonClickListener {
        void onActivistButtonClicked();
        void onOrganizationButtonClicked();
    }

    private OnButtonClickListener buttonClickListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Ensure that the activity implements the interface
        if (context instanceof OnButtonClickListener) {
            buttonClickListener = (OnButtonClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnButtonClickListener");
        }
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_slide, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        View activistButton = view.findViewById(R.id.ActivistBtn);
        View organizationButton = view.findViewById(R.id.OrganizationBtn);


        activistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Notify the activity that the activist button is clicked
                if (buttonClickListener != null) {
                    buttonClickListener.onActivistButtonClicked();
                }
            }
        });

        organizationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Notify the activity that the organization button is clicked
                if (buttonClickListener != null) {
                    buttonClickListener.onOrganizationButtonClicked();
                }
            }
        });



    }
}
