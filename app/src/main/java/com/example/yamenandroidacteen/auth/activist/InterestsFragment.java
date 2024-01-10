package com.example.yamenandroidacteen.auth.activist;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.yamenandroidacteen.R;
import com.example.yamenandroidacteen.classes.User;
import com.google.protobuf.Value;

public class InterestsFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_interests, container, false);
        if (getArguments() != null) {
            User user = (User) getArguments().getSerializable("User");

            if (user != null) {
                String userName = user.getName();
                Toast.makeText(getActivity(), userName, Toast.LENGTH_SHORT).show();

            }
        }

        // Inflate the layout for this fragment
        return view;
    }


}
