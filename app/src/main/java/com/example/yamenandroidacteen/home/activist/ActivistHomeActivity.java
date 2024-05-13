package com.example.yamenandroidacteen.home.activist;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.example.yamenandroidacteen.R;
import com.example.yamenandroidacteen.databinding.ActivityActivistHomeBinding;


public class ActivistHomeActivity extends AppCompatActivity  {


    private ActivityActivistHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        askNotificationPermission();



        binding = ActivityActivistHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new ActivistHomeFragment());



        binding.bottomNavMenuActivist.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.homeActivist) {
                replaceFragment(new ActivistHomeFragment());
            } else if (itemId == R.id.searchActivist) {
                replaceFragment(new ActivistSearchFragment());
            } else if (itemId == R.id.notificationActivist) {
                replaceFragment(new ActivistNotificationsFragment());
            } else if (itemId == R.id.profileActivist) {
                replaceFragment(new ActivistProfileFragment());
            }

            return true;
        });



    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutActivist, fragment);
        fragmentTransaction.commit();
    }

    public void hideSystemNavigationBar() {
        binding.bottomNavMenuActivist.setVisibility(View.GONE);


    }

    public void showSystemNavigationBar() {

        binding.bottomNavMenuActivist.setVisibility(View.VISIBLE);

    }

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // FCM SDK (and your app) can post notifications.
                } else {
                    // TODO: Inform user that that your app will not show notifications.
                }
            });

    private void askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED) {
                // FCM SDK (and your app) can post notifications.

            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {

                //Build Cutomize Alert Dialog

                new AlertDialog.Builder(this)
                        .setTitle("Notification Permission")
                        .setMessage("Please Allow Notification Permission")
                        .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setNegativeButton("Don't Allow", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .create()
                        .show();


            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
      }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        showExitConfirmationDialog();
    }

    @Override
    public void onBackPressed() {
        // Handle back button press here
        // For example, you can show a toast message
        Toast.makeText(ActivistHomeActivity.this, "Back button pressed", Toast.LENGTH_SHORT).show();

        // Call super.onBackPressed() to allow default back button behavior
        super.onBackPressed();
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
}



