package com.example.yamenandroidacteen.home.activist;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;




import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;




import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.yamenandroidacteen.R;
import com.example.yamenandroidacteen.auth.LoginFragment;
import com.example.yamenandroidacteen.auth.OrgOrActivistFragment;
import com.example.yamenandroidacteen.auth.activist.InterestsFragment;
import com.example.yamenandroidacteen.classes.FragmentHelper;
import com.example.yamenandroidacteen.classes.adapters.AdapterPosts;
import com.example.yamenandroidacteen.classes.interfaces.SelectListener;
import com.example.yamenandroidacteen.classes.models.ModelPost;
import com.example.yamenandroidacteen.databinding.ActivityActivistHomeBinding;
import com.example.yamenandroidacteen.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;




import java.util.ArrayList;
import java.util.List;




public class ActivistHomeActivity extends AppCompatActivity {


    private ActivityActivistHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


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


}

