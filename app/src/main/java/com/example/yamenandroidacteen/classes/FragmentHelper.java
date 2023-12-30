package com.example.yamenandroidacteen.classes;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.yamenandroidacteen.R;
import com.example.yamenandroidacteen.auth.LoginFragment;
import com.example.yamenandroidacteen.home.activist.ActivistHomeActivity;

public class FragmentHelper {

    public static void navigateToLoginFragment(ActivistHomeActivity activity) {
        // Create an instance of the LoginFragment
        LoginFragment loginFragment = new LoginFragment();

        // Get the FragmentManager and start a transaction
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Replace the existing fragment or add it to the container with ID frameLayoutLogin
        fragmentTransaction.replace(R.id.frameLayoutLogin, loginFragment);
        fragmentTransaction.addToBackStack(null); // Optional: Adds the transaction to the back stack

        // Commit the transaction
        fragmentTransaction.commit();
    }
}
