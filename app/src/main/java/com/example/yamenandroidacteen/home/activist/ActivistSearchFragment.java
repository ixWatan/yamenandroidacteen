package com.example.yamenandroidacteen.home.activist;

import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
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

public class ActivistSearchFragment extends Fragment implements SelectListener {


    private FirebaseAuth mAuth;

    ImageView profileImageView;

    private String profilePictureUrl;


    RecyclerView recyclerView;
    List<ModelPost> postList;
    AdapterPosts adapterPosts;
    private SearchView search;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activist_search, container, false);

        search = view.findViewById(R.id.simpleSearchView);
        mAuth = FirebaseAuth.getInstance();



        // -=-=-=-=-=-
        // search post stuff start
        // -=-=-=-=-=-


        // post recycler view properties
        recyclerView = view.findViewById(R.id.postRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        //show newest first
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);

        recyclerView.setLayoutManager(layoutManager);

        postList = new ArrayList<>();

        adapterPosts = new AdapterPosts(postList, new AdapterPosts.OnPostClickListener() {
            @Override
            public void onPostClick(int position) {

                Toast.makeText(getActivity(), postList.get(position).getpTitle() , Toast.LENGTH_SHORT).show();

            }
        });

        recyclerView.setAdapter(adapterPosts);

        // -=-=-=-=-=-
        // search post stuff end
        // -=-=-=-=-=-

        searchPosts(""); // Empty search query to retrieve all posts


        search.setQueryHint("Search");

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                //user pressed on search button
                if(!TextUtils.isEmpty(s)) {
                    searchPosts(s);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //user presses any button
                // Call searchPosts with the current search query
                searchPosts(s);
                return false;
            }
        });



        // Inflate the layout for this fragment
        return view;
    }

    private void loadProfilePic() {
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
    }

    private void searchPosts(String searchQuery) {
        //path of  all posts
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");




        // get all data from this ref
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for(DataSnapshot ds: dataSnapshot.getChildren()) {
                    ModelPost modelPost = ds.getValue(ModelPost.class);

                    if(modelPost.getpTitle().toLowerCase().contains(searchQuery.toLowerCase())
                            || modelPost.getpDescription().toLowerCase().contains(searchQuery.toLowerCase())
                            || modelPost.getpHashtags().toLowerCase().contains(searchQuery.toLowerCase())
                            || modelPost.getpAddress().toLowerCase().contains(searchQuery.toLowerCase())
                    ){
                        postList.add(modelPost);
                    }

                    adapterPosts = new AdapterPosts(postList, new AdapterPosts.OnPostClickListener() {
                        @Override
                        public void onPostClick(int position) {
                            // Handle post click, e.g., navigate to another fragment

                            onItemClicked(postList.get(position));

                        }
                    });


                }

                recyclerView.setAdapter(adapterPosts);






            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // in case of error
                Toast.makeText(getActivity(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
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
                .replace(R.id.frameLayoutActivist, activistShowPostFragment) // Use the container ID of your fragment container
                .addToBackStack(null)
                .commit();


    }
}