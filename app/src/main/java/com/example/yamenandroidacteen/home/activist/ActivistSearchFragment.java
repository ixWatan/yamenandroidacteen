package com.example.yamenandroidacteen.home.activist;
import java.util.Collections;


import android.graphics.Rect;
import android.media.Image;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
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
    public SearchView search;

    public boolean isSearchBarOpen = false;

    private View rootView;

    private LinearLayout filterContainer;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activist_search, container, false);

        search = view.findViewById(R.id.simpleSearchView);
        mAuth = FirebaseAuth.getInstance();
        rootView = view;

        filterContainer = view.findViewById(R.id.filterContainer);

// Your hashTagsList array
        String[] hashTagsList = {"#Environment", "#Volunteering", "#Protests", "#Women's Rights", "#Human Rights", "#Racism", "#LGBTQ+", "#Animals", "#Petitions", "#Education"};
        LayoutInflater inflater2 = LayoutInflater.from(requireContext());
        for (String hashtag : hashTagsList) {
            LinearLayout viewForText = (LinearLayout) inflater2.inflate(R.layout.filter_option, filterContainer, false);

            // Find the TextView inside the inflated LinearLayout
            TextView textView = viewForText.findViewById(R.id.showTagLabel1);

            // Set the text of the TextView
            textView.setText(hashtag);

            // Set an onClickListener to handle filter selection
            viewForText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Access the text from the TextView
                    String selectedTag = textView.getText().toString();
                    searchPosts(selectedTag, false);
                }
            });

            // Add the LinearLayout (with the TextView inside) to the filter container
            filterContainer.addView(viewForText);
        }




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

        searchPosts("", true); // Empty search query to retrieve all posts



        search.setQueryHint("Search");

        detectKeyboardState();



        search.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                isSearchBarOpen = hasFocus;
                if (hasFocus) {
                    // Search bar opened, hide system navigation bar
                    ((ActivistHomeActivity) requireActivity()).hideSystemNavigationBar();
                }
            }
        });

        // Assuming 'search' is your SearchView
        search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // Check if the key event is KeyEvent.ACTION_DOWN and the keyCode is KeyEvent.KEYCODE_BACK
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
                    // Handle the event here, for example, keyboard hiding
                    ((ActivistHomeActivity) requireActivity()).showSystemNavigationBar();
                    return true; // Return true to consume the event
                }
                return false; // Return false to allow normal processing of the event
            }
        });

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                //user pressed on search button
                search.clearFocus();

                if(!TextUtils.isEmpty(s)) {
                    searchPosts(s, false);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //user presses any button
                // Call searchPosts with the current search query
                searchPosts(s, false);
                return false;
            }
        });




        // Inflate the layout for this fragment
        return view;
    }


    private void addFilterOptions(String[] hashTagsList) {
        for (String tag : hashTagsList) {
            addFilterOption(tag);
        }
    }

    private void addFilterOption(String text) {
        LinearLayout filterOption = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.filter_option, filterContainer, false);
        TextView filterTextView = filterOption.findViewById(R.id.filter_option);
        filterTextView.setText(text);
        filterContainer.addView(filterOption);
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

    private void searchPosts(String searchQuery, boolean shuffle) {
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

                if(shuffle) {
                    Collections.shuffle(postList);
                }


                recyclerView.setAdapter(adapterPosts);






            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // in case of error
                if(getContext() != null){
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
                .replace(R.id.frameLayoutActivist, activistShowPostFragment) // Use the container ID of your fragment container
                .addToBackStack(null)
                .commit();


    }

    private void detectKeyboardState() {
        View rootView = requireActivity().getWindow().getDecorView().getRootView();

        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            private Rect r = new Rect();

            @Override
            public void onGlobalLayout() {
                rootView.getWindowVisibleDisplayFrame(r);
                int screenHeight = rootView.getHeight();
                int keyboardHeight = screenHeight - r.bottom;

                if (keyboardHeight > screenHeight * 0.15) {
                    // Keyboard is open
                    onKeyboardOpened();
                } else {
                    // Keyboard is closed
                    onKeyboardClosed();
                }
            }
        });
    }

    private void onKeyboardOpened() {
        search.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                isSearchBarOpen = hasFocus;
                if (hasFocus) {
                    Toast.makeText(getActivity(), "keyboard opened", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void onKeyboardClosed() {

        search.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                isSearchBarOpen = hasFocus;
                if (hasFocus) {
                    Toast.makeText(getActivity(), "keyobard closed", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Hide the system navigation bar when the fragment is displayed
        ((ActivistHomeActivity) requireActivity()).showSystemNavigationBar();
    }






}