package com.example.yamenandroidacteen.home.organization;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
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
                showCreateDialog();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        showExitConfirmationDialog();
    }

    public void hideSystemNavigationBar() {
        binding.bottomNavMenuOrg.setVisibility(View.GONE);


    }

    public void showSystemNavigationBar() {

        binding.bottomNavMenuOrg.setVisibility(View.VISIBLE);

    }



    private void showCreateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setItems(R.array.create_options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            // Navigate to the Create Post fragment
                            replaceFragment(new OrganizationCreatePostFragment());
                        } else if (i == 1) {

                            replaceFragment(new OrganizationSendNotificationFragment());
                        }
                    }
                });
        builder.create().show();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            // If there are no fragments left in the back stack, show a confirmation dialog
            showExitConfirmationDialog();
        } else {
            // If there are fragments left, proceed with the default back button behavior
            super.onBackPressed();
        }
    }

    private void showExitConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit");
        builder.setMessage("Are you sure you want to exit?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Exit the app
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Dismiss the dialog and do nothing
                dialogInterface.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void navigateToFragmentWithAnimation(Fragment fragment) {

        // Begin the transaction
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction().setCustomAnimations(
                R.anim.slide_in,  // enter
                R.anim.fade_out,  // exit
                R.anim.fade_in,   // popEnter
                R.anim.slide_out  // popExit
        );

        ft.replace(R.id.frameLayoutOrg, fragment);

        // Add the transaction to the back stack (optional)
        ft.addToBackStack(null);

        // Commit the transaction
        ft.commit();
    }

    public void navigateToFragment(Fragment fragment) {

        // Begin the transaction
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        ft.replace(R.id.frameLayoutOrg, fragment);

        // Add the transaction to the back stack (optional)
        ft.addToBackStack(null);

        // Commit the transaction
        ft.commit();
    }




}