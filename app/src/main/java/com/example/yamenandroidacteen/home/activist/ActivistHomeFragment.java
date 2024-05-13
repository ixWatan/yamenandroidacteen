package com.example.yamenandroidacteen.home.activist;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.yamenandroidacteen.R;
import com.example.yamenandroidacteen.classes.adapters.AdapterPosts;
import com.example.yamenandroidacteen.classes.interfaces.SelectListener;
import com.example.yamenandroidacteen.classes.models.ModelPost;
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


public class ActivistHomeFragment extends Fragment implements SelectListener {






    private ImageView profileImageView;


    private FirebaseAuth mAuth;


    RecyclerView recyclerView;
    List<ModelPost> postList;
    AdapterPosts adapterPosts;


    private ImageButton profileImageButton;


    private ProgressDialog pd;

    private Button signOutBtn;


    private ImageButton homePageButton;


    private ImageButton notificationButton;
    private ImageButton searchButton;


    private String profilePictureUrl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_activist_home, container, false);



        //profile pic on top init
        profileImageView = view.findViewById(R.id.profileImageView);
        mAuth = FirebaseAuth.getInstance();


        // post recycler view properties
        recyclerView = view.findViewById(R.id.postRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        //show newest first
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);


        // set layout to recyclerview
        recyclerView.setLayoutManager(layoutManager);


        ProgressDialog pd;


        // init post list
        postList = new ArrayList<>();


        // init the adapter
        adapterPosts = new AdapterPosts(postList, new AdapterPosts.OnPostClickListener() {
            @Override
            public void onPostClick(int position) {
                // Handle post click, e.g., navigate to another fragment
/*
                navigateToPostDetailsFragment(postList.get(position));
*/
                Toast.makeText(getActivity(), postList.get(position).getpTitle() , Toast.LENGTH_SHORT).show();

            }
        });

        recyclerView.setAdapter(adapterPosts);


        loadPosts();




        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("teenActivists").document(userId).get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {

                                profilePictureUrl = document.getString("profilePictureUrl");


                                // Update the profile picture ImageView with the new URL
                                if (profilePictureUrl != null && !profilePictureUrl.isEmpty()) {
                                    Glide.with(this)
                                            .load(profilePictureUrl)
                                            .into(profileImageView);
                                } else {
                                    // Display the default profile picture
                                    Glide.with(this)
                                            .load(R.drawable.icon_account)
                                            .into(profileImageView);
                                }


                            }
                        } else {
                            Toast.makeText(getActivity(), "An error occurred. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }


        // Inflate the layout for this fragment
        return view;
    }

    private void loadPosts() {
        /*pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading Posts...");
        pd.show();*/
        //path of  all posts
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");


        // get all data from this ref
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ModelPost modelPost = ds.getValue(ModelPost.class);


                    postList.add(modelPost);

                }

                adapterPosts = new AdapterPosts(postList, new AdapterPosts.OnPostClickListener() {
                    @Override
                    public void onPostClick(int position) {
                        // Handle post click, e.g., navigate to another fragment

                        onItemClicked(postList.get(position));

                    }
                });
                recyclerView.setAdapter(adapterPosts);



            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Check if the activity is not null before showing the Toast
                if (getActivity() != null) {
                    Toast.makeText(getActivity(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }


        });
    }


    @Override
    public void onItemClicked(ModelPost modelPost) {

        // Putting data from the post into the fragment transfer
        Bundle bundle = new Bundle();
        bundle.putSerializable("org_name", modelPost.getuName());
        bundle.putSerializable("post_image", modelPost.getpImage());
        bundle.putSerializable("post_description", modelPost.getpDescription());
        bundle.putSerializable("post_timePosted", modelPost.getpTime());
        bundle.putSerializable("post_title", modelPost.getpTitle());
        bundle.putSerializable("post_user_pfp", modelPost.getuDp());
        bundle.putSerializable("post_endT", modelPost.getpEndT());
        bundle.putSerializable("post_startT", modelPost.getpStartT());
        bundle.putSerializable("post_date", modelPost.getpDate());
        bundle.putSerializable("post_tags", modelPost.getpHashtags());
        bundle.putSerializable("post_locationLink", modelPost.getpLocationLink());
        bundle.putSerializable("post_location", modelPost.getpLocationLinkReal());
        bundle.putSerializable("post_id", modelPost.getpId());
        bundle.putSerializable("post_comments", modelPost.getpComments());
        bundle.putSerializable("post_likes", modelPost.getpLikes());
        bundle.putSerializable("userProfilePicUrl", profilePictureUrl);

        // end of Putting data from the post into the fragment transfer


        // need to transition to fragment
        ActivistShowPostFragment activistShowPostFragment = new ActivistShowPostFragment();

        activistShowPostFragment.setArguments(bundle);

        // Use FragmentManager to replace the current fragment with the details fragment
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayoutActivist, activistShowPostFragment, "ActivistShowPostFragment") // Use the container ID of your fragment container
                .addToBackStack(null)
                .commit();


    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Hide the system navigation bar when the fragment is displayed
        ((ActivistHomeActivity) requireActivity()).showSystemNavigationBar();
    }






    }
