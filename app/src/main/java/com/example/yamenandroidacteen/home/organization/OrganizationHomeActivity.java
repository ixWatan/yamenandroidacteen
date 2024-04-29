package com.example.yamenandroidacteen.home.organization;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.yamenandroidacteen.MainActivity;
import com.example.yamenandroidacteen.R;
import com.example.yamenandroidacteen.classes.other.BaseActivity;
import com.example.yamenandroidacteen.databinding.ActivityActivistHomeBinding;
import com.example.yamenandroidacteen.databinding.ActivityOrganizationHomeBinding;
import com.example.yamenandroidacteen.home.activist.ActivistHomeFragment;
import com.example.yamenandroidacteen.home.activist.ActivistNotificationsFragment;
import com.example.yamenandroidacteen.home.activist.ActivistProfileFragment;
import com.example.yamenandroidacteen.home.activist.ActivistSearchFragment;
import com.google.firebase.auth.FirebaseAuth;

public class OrganizationHomeActivity extends BaseActivity {

    private Button signOutBtn;

    private ActivityOrganizationHomeBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization_home);

       /* signOutBtn = findViewById(R.id.signoutBtn);

        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });*/

        binding = ActivityOrganizationHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new OrganizationHomeFragment());



        binding.bottomNavMenuOrg.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.homeOrg) {
                replaceFragment(new OrganizationHomeFragment());
            } else if (itemId == R.id.createOrg) {
                replaceFragment(new OrganizationCreatePostFragment());
            } else if (itemId == R.id.profileOrg) {
                replaceFragment(new OrganizationPorfileFragment());
            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutOrg, fragment);
        fragmentTransaction.commit();
    }

    public void hideSystemNavigationBar() {
        binding.bottomNavMenuOrg.setVisibility(View.GONE);


    }

    public void showSystemNavigationBar() {

        binding.bottomNavMenuOrg.setVisibility(View.VISIBLE);

    }

    private void signOut() {
        // Sign out from Firebase Authentication
        FirebaseAuth.getInstance().signOut();

        // Show a toast message
        Toast.makeText(this, "Signed out successfully", Toast.LENGTH_SHORT).show();

        // Start the MainActivity
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("profilePictureUrl", ""); // Clear the current profile picture URL
        startActivity(intent);
        finish(); // Optionally, call finish() to prevent the user from returning to the UserProfileActivity using the back button
    }

}