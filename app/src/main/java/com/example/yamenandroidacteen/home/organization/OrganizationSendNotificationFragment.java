package com.example.yamenandroidacteen.home.organization;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.yamenandroidacteen.R;
import com.example.yamenandroidacteen.classes.models.ModelPost;
import com.example.yamenandroidacteen.home.activist.ActivistHomeActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrganizationSendNotificationFragment extends Fragment {

    FirebaseUser user;
    List<ModelPost> postList;

    FirebaseAuth mAuth;

    List<Pair<String, String>> pairsList = new ArrayList<>();
    List<String> postsTitlesList = new ArrayList<>();

    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;
    String selectedPostTitle;
    String selectedPostId;

    Button sendNotificationBtn;

    EditText notificationTitleEditText;
    EditText notificationBodyEditText;
    EditText notificationLinkEditText;

    TextView previewTitle;

    TextView previewBody;

    ImageButton backBtn;

    ProgressDialog pd;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_organization_send_notification, container, false);
        pd = new ProgressDialog(getActivity());

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        // Initialize views
        autoCompleteTextView = view.findViewById(R.id.autocomplete_Tv_title);
        sendNotificationBtn = view.findViewById(R.id.sendNotificationBtn);
         notificationTitleEditText = view.findViewById(R.id.edittext_notification_title);
         notificationBodyEditText = view.findViewById(R.id.edittext_notification_body);
         notificationLinkEditText = view.findViewById(R.id.edittext_notification_link);

         previewTitle = view.findViewById(R.id.notification_title_preview);
         previewBody = view.findViewById(R.id.notification_body_preview);
        backBtn = view.findViewById(R.id.backButtonFromSendNotif);

        postList = new ArrayList<>();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        navigateToFragment(new OrganizationHomeFragment());

                    }
                }, 300);
            }
        });

        typeListener();


        // Get data from Firebase
        getData();

        // Set up the ArrayAdapter
        adapterItems = new ArrayAdapter<>(view.getContext(), R.layout.list_item, postsTitlesList);
        autoCompleteTextView.setAdapter(adapterItems);

        // Set item click listener for AutoCompleteTextView
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected post title
                selectedPostTitle = (String) parent.getItemAtPosition(position);

                // Find the corresponding ID from pairsList
                for (Pair<String, String> pair : pairsList) {
                    if (pair.first.equals(selectedPostTitle)) {
                        selectedPostId = pair.second;
                        break;
                    }
                }

                // Now you have the selected post title and its ID
                // You can use selectedPostTitle and selectedPostId as needed
                Toast.makeText(getContext(), "Selected Post: " + selectedPostTitle + " (ID: " + selectedPostId + ")", Toast.LENGTH_LONG).show();
            }
        });

        sendNotificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Retrieve the text from the EditText views
                String selectedPost = autoCompleteTextView.getText().toString().trim();
                String notificationTitle = notificationTitleEditText.getText().toString();
                String notificationBody = notificationBodyEditText.getText().toString();
                String notificationLink = notificationLinkEditText.getText().toString();

                // Check if the selected post is empty
                if (selectedPost.isEmpty()) {
                    Toast.makeText(requireContext(), "Please select a post", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (notificationTitle.isEmpty()) {
                    Toast.makeText(requireContext(), "Title is empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (notificationBody.isEmpty()) {
                    Toast.makeText(requireContext(), "Body is empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (notificationLink.isEmpty()) {
                    sendTestNotificationToTopic(selectedPostId, notificationTitle, notificationBody, "");
                } else {
                    sendTestNotificationToTopic(selectedPostId, notificationTitle, notificationBody, notificationLink);
                }
            }
        });



        return view;
    }

    private void typeListener() {
        // Update notification preview when text changes in EditTexts
        notificationTitleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateNotificationPreview();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        notificationBodyEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateNotificationPreview();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void updateNotificationPreview() {
        // Update notification title and body based on EditTexts content
        previewTitle.setText(notificationTitleEditText.getText().toString());
        previewBody.setText(notificationBodyEditText.getText().toString());
    }

    private void getData() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");

        // get all data from this ref
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ModelPost modelPost = ds.getValue(ModelPost.class);
                    if (modelPost != null && modelPost.getuEmail().equals(user.getEmail())) {
                        postList.add(modelPost);
                        pairsList.add(new Pair<>(modelPost.getpTitle(), modelPost.getpId()));
                    }
                }
                // Update the ArrayAdapter with the new data
                postsTitlesList.clear();
                for (Pair<String, String> pair : pairsList) {
                    postsTitlesList.add(pair.first);
                }
                adapterItems.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // in case of error
                Toast.makeText(getActivity(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendTestNotificationToTopic(String topic, String notificationTitle, String notificationBody, String notificationLink) {
        // Replace with your FCM server project server key (not recommended for production!)

        pd.setMessage("Sending notification...");
        pd.show();

        String serverKey = "AAAAUAwfJ44:APA91bFusWu0Ujb7RSxyDKNNxASFJ3z4v4MDWRJcIZypgHRGB9CHeoniHqvfszAUQc1n0Z6DEhH1cBJUzbJ308MsG0h2RKNPTeDqg6cPSjQpLqZ1TOz0cBlGCnF5CjWcQ_N6kkzoxlGA";

        // Prepare the JSON message payload
        JSONObject messageJson = new JSONObject();

        if (notificationLink.isEmpty()) {
            try {
                messageJson.put("to", "/topics/" + topic);
                JSONObject notification = new JSONObject();
                notification.put("title", user.getDisplayName());
                notification.put("body",  "\uD83D\uDCCC  " + notificationTitle + ": " + notificationBody);
                messageJson.put("notification", notification);
            } catch (JSONException e) {
                e.printStackTrace();

            }

        } else {

            try {
                messageJson.put("to", "/topics/" + topic);
                JSONObject notification = new JSONObject();
                notification.put("title",  user.getDisplayName());
                notification.put("body",  "\uD83D\uDCCC  " + notificationTitle + ": " + notificationBody);
                notification.put("click_action", "OPEN_ACTIVITY");
                notification.put("URL", notificationLink);


                messageJson.put("notification", notification);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        String url = "https://fcm.googleapis.com/fcm/send";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, messageJson,
                response -> {
                    pd.dismiss();
                    Toast.makeText(getActivity(), "Sent Notification Successfully", Toast.LENGTH_SHORT).show();
                    clearBackStack();
                    navigateToFragment(new OrganizationHomeFragment());
                },
                error -> {
                    Toast.makeText(getActivity(), "error" , Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "key=" + serverKey);
                return headers;
            }
        };
        Volley.newRequestQueue(getActivity()).add(request);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Hide the system navigation bar when the fragment is displayed
        ((OrganizationHomeActivity) requireActivity()).hideSystemNavigationBar();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        ((OrganizationHomeActivity) requireActivity()).showSystemNavigationBar();
    }

    public void navigateToFragment(Fragment fragment) {

        // Begin the transaction
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

        ft.replace(R.id.frameLayoutOrg, fragment);

        // Add the transaction to the back stack (optional)
        ft.addToBackStack(null);

        // Commit the transaction
        ft.commit();
    }

    private void clearBackStack() {
        // Get the fragment manager
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();

        // Clear the back stack
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
}
