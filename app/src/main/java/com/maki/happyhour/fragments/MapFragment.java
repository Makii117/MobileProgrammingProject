package com.maki.happyhour.fragments;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.maki.happyhour.R;
import com.maki.happyhour.googlePlaceAPI.GetNearbyPlaces;
import com.maki.happyhour.models.UserLocation;

import java.io.IOException;
import java.util.List;

public class MapFragment extends Fragment
        implements OnMapReadyCallback,
        OnMyLocationButtonClickListener,
        OnMyLocationClickListener,
        OnClickListener {

    private GoogleMap mMap;
    SupportMapFragment mapFrag;
    GoogleApiClient mGoogleApiClient;
    private boolean permissionDenied = false;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private double latitude, longitude;
    private final int ProximityRadius = 1000;
    ImageButton restaurant, cafes, pubs;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth;
    private double lat;
    private double lon;
    ImageView search_address;

    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.map_fragment, container, false);
        mAuth=FirebaseAuth.getInstance();

        mapFrag = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map);
        setUpMapIfNeeded();
        restaurant = rootView.findViewById(R.id.restaurants_nearby);
        restaurant.setOnClickListener(this);
        cafes = rootView.findViewById(R.id.cafes_nearby);
        cafes.setOnClickListener(this);
        pubs = rootView.findViewById(R.id.pubs_nearby);
        pubs.setOnClickListener(this);
        search_address = rootView.findViewById(R.id.search_address);
        search_address.setOnClickListener(this);

        DocumentReference docRef=db.collection("users").document(mAuth.getCurrentUser().getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document=task.getResult();
                    if(document.exists()){
                        Log.d("Val Of dOc", String.valueOf(document.getData()));
                        UserLocation loc=document.toObject(UserLocation.class);
                        Log.d("LocationValue", String.valueOf(loc.getLat())+" "+String.valueOf(loc.getLon()));
                        lat=loc.getLat();
                        lon=loc.getLon();
                    }else {
                        Log.d("ERRORTAG", "NO DOC");
                    }
                }else{
                    Log.d("FAiled", String.valueOf(task.getException()));
                }


            }
        });



        return rootView;



    }



    private String getUrl(double latitude, double longitude, String nearbyPlace){

        StringBuilder googleURL= new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googleURL.append("location=" + lat + "," + lon);
        googleURL.append("&radius=" + ProximityRadius);
        googleURL.append("&type=" + nearbyPlace);
        googleURL.append("&sensor=true");
        googleURL.append("&key=" + "AIzaSyDoGoLDEXZ61j99cH-lHkEv-ItxkRCN_YM");
        Log.d("GoogleMapsActivity","url = " + googleURL.toString());

        return googleURL.toString();
    }

    public void onClick(View v){
        String restaurants = "restaurant", cafes = "cafe", pubs = "bar";
        Object[] transferData = new Object[2];
        GetNearbyPlaces getNearbyPlaces = new GetNearbyPlaces();
        switch(v.getId()){
            case R.id.search_address:
                EditText addressfield = getView().findViewById(R.id.location_search);
                String address = addressfield.getText().toString();

                List<Address> addressList = null;

                MarkerOptions userMarkerOptions = new MarkerOptions();

                if (!TextUtils.isEmpty(address)) {
                    Geocoder geocoder = new Geocoder(getActivity());
                    try {
                        addressList = geocoder.getFromLocationName(address, 6);

                        if (addressList != null) {
                            for (int i = 0; i < addressList.size(); i++) {
                                Address userAddress = addressList.get(i);
                                LatLng latLng = new LatLng(userAddress.getLatitude(), userAddress.getLongitude());

                                userMarkerOptions.position(latLng);
                                userMarkerOptions.title(address);
                                userMarkerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                                mMap.addMarker(userMarkerOptions);

                                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                mMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f));
                            }
                        } else {
                            Toast.makeText(getActivity(), "Location not found", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getActivity(), "Please write location name...", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.restaurants_nearby:
                mMap.clear();
                String url = getUrl(latitude, longitude, restaurants);
                transferData[0] = mMap;
                transferData[1] = url;
                getNearbyPlaces.execute(transferData);
                Toast.makeText(getActivity(), "Searching for nearby restaurants", Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity(), "Showing nearby restaurants", Toast.LENGTH_SHORT).show();
                break;
            case R.id.cafes_nearby:
                mMap.clear();
                url = getUrl(latitude, longitude, cafes);
                transferData[0] = mMap;
                transferData[1] = url;
                getNearbyPlaces.execute(transferData);
                Toast.makeText(getActivity(), "Searching for nearby cafes", Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity(), "Showing nearby cafes", Toast.LENGTH_SHORT).show();
                break;
            case R.id.pubs_nearby:
                mMap.clear();
                url = getUrl(latitude, longitude, pubs);
                transferData[0] = mMap;
                transferData[1] = url;
                getNearbyPlaces.execute(transferData);
                Toast.makeText(getActivity(), "Searching for nearby pubs", Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity(), "Showing nearby pubs", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {

        if (mMap == null) {
            mapFrag.getMapAsync(this);
        }
    }
    @Override
    public void onPause() {
        super.onPause();

        //stop location updates when Activity is no longer active
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, (LocationListener) this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        enableMyLocation();
    }

    private void enableMyLocation() {
        //Initialize Google Play Services
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);
            }
        } else {
            //Request Location Permission
            checkLocationPermission();
        }
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(getActivity())
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != MY_PERMISSIONS_REQUEST_LOCATION) {
            return;
        }

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Permission was denied. Display an error message
            // Display the missing permission error dialog when the fragments resume.
            permissionDenied = true;
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(getActivity(), "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(getActivity(), "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }
}
