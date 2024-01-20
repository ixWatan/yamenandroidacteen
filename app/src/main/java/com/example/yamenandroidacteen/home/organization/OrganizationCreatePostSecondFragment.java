package com.example.yamenandroidacteen.home.organization;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yamenandroidacteen.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;


import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class OrganizationCreatePostSecondFragment extends Fragment implements  OnMapReadyCallback, GoogleMap.OnMapClickListener
         {


    private final int FINE_PERMISSION_CODE = 1;

    ImageView userLocationBtn;

    TextView locationShowTv;
    Location currentLocation;

    private LatLng selectedLocationLatLang;
    private String selectedLocationURL;

    private String selectedLocationText;

    private String mapsUri;


    FusedLocationProviderClient fusedLocationProviderClient;
    private GoogleMap myMap;
    TextView nextTv;

    private Marker selectedMarker;

    ImageButton backButton;

    private SearchView mapSearchView;

    private static String TAG = "Info";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_organization_create_post_second, container, false);

        // Inside onCreate method
        if (!Places.isInitialized()) {
            Places.initialize(getContext(), "AIzaSyCA3V8IqoAIos01hitHRuBzwjD7qVkILJY");
        }


        backButton = view.findViewById(R.id.backButtonSecodStep);
        nextTv = view.findViewById(R.id.NextTvSecondStepSecond);
        locationShowTv = view.findViewById(R.id.locationShowTv);
        userLocationBtn = view.findViewById(R.id.btnMyLocation);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(OrganizationCreatePostSecondFragment.this);

        PlacesClient placesClient = Places.createClient(getContext());

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);


        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                updateLocationInfo(place.getLatLng());






                // Animate the camera to the selected place
                if (myMap != null && selectedLocationLatLang != null) {


                    // Add a marker at the selected place
                    if (selectedMarker != null) {
                        selectedMarker.remove(); // Remove the previous marker if exists
                    }
                    selectedMarker = myMap.addMarker(new MarkerOptions().position(selectedLocationLatLang).title(place.getName()));
                }

            }

            @Override
            public void onError(@NonNull com.google.android.gms.common.api.Status status) {
                // Handle errors
            }
        });




        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getActivity().getSupportFragmentManager().popBackStack();
                } else {
                    navigateToFragment(new OrganizationCreatePostFragment());
                }
            }
        });


        nextTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToFragment(new OrganizationCreatePostThirdFragment());
            }
        });

        userLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCurrentLocation();

            }
        });

        // maps stuff

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        getLastLocation();



        return view;

    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PERMISSION_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null) {
                    currentLocation = location;

                    SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
                    mapFragment.getMapAsync(OrganizationCreatePostSecondFragment.this);

                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == FINE_PERMISSION_CODE) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ) {
                getLastLocation();
            } else {
                Toast.makeText(getActivity(), "Permission Denied please accept it", Toast.LENGTH_SHORT).show();
            }
        }
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

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Hide the system navigation bar when the fragment is displayed
        ((OrganizationHomeActivity) requireActivity()).hideSystemNavigationBar();
    }



    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        myMap = googleMap;
        myMap.setOnMapClickListener(this);


    }

             private String getAddressFromLatLng(LatLng latLng) {
                 Geocoder geocoder = new Geocoder(requireActivity(), Locale.getDefault());

                 try {
                     List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                     Address obj = addresses.get(0);
                     String add = obj.getAddressLine(0);
                     return add;
                 } catch (IOException e) {
                     e.printStackTrace();
                     return "Unknown";
                 }
             }


             @Override
             public void onMapClick(@NonNull LatLng latLng) {
                 // Add a marker at the tapped location and move the camera to that location.
                 myMap.clear(); // Clear existing markers

                updateLocationInfo(latLng);





             }

             public void updateLocationInfo(LatLng latLng) {
                 // Get the selected place's LatLng
                 selectedLocationLatLang = latLng;
                 selectedLocationText = getAddressFromLatLng(latLng);
                 selectedLocationURL = getAddressFromLatLng(selectedLocationLatLang);
                 locationShowTv.setText("Location: " + getAddressFromLatLng(latLng));

                 myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f), 1000, null);
                 selectedMarker = myMap.addMarker(new MarkerOptions().position(latLng).title(selectedLocationText));
                 mapsUri = "http://maps.google.com/maps?q=" + Uri.encode(selectedLocationURL);
                 Log.d("Location Selected", mapsUri);
             }

             private void getCurrentLocation() {
                 if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                     fusedLocationProviderClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                         @Override
                         public void onSuccess(Location location) {
                             if (location != null) {
                                 // Move the camera to the user's location
                                 updateLocationInfo(new LatLng(location.getLatitude(), location.getLongitude()));
                             }
                         }
                     });
                 }
             }
         }