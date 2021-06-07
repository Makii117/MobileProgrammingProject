package com.maki.happyhour.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.maki.happyhour.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.maki.happyhour.R;
import com.maki.happyhour.activities.MapActivity;

public class MapFragment extends Fragment implements View.OnClickListener {
    private static final int LOCATION_REQUEST_CODE = 123;
    private GoogleMap mMap;
    MapView mMapView;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    float zoomLevel = 15.0f;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.map_fragment, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.map);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;

                LatLng sarajevo = new LatLng(43.8563, 18.4131);

                mMap.addMarker(new MarkerOptions().position(sarajevo).title("Marker in Sarajevo"));

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sarajevo,zoomLevel));

                    googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                        @Override
                        public void onMapClick(LatLng latLng) {
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(latLng);
                            markerOptions.title(latLng.latitude+":"+latLng.longitude);
                            googleMap.clear();

                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                    latLng,zoomLevel
                            ));
                            googleMap.addMarker(markerOptions);
                        }
                    });
                }
            });






    return rootView;
    }
    public void onClick(View view){
        switch(view.getId()){
            case R.id.sarajevo_map:
                LatLng sarajevo = new LatLng(44, 18);
                mMap.clear();
                mMap.animateCamera(CameraUpdateFactory.newLatLng(sarajevo));
                break;
            case R.id.current_location:
                //get current location
                getLocation();
                break;
        }
    }

    private void getLocation(){
        if(checkPermission()){
            if(isLocationEnabled()){
                mFusedLocationProviderClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>(){
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();

                                if(location != null){
                                    LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                                    mMap.clear();

                                    mMap.addMarker(new MarkerOptions().position(currentLocation).title("Marker on current location"));
                                    mMap.animateCamera((CameraUpdateFactory.newLatLngZoom(currentLocation, 15.0f)));
                                }else{
                                    Toast.makeText(getActivity(), "Unable to get location", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                );
            }else{
                Toast.makeText(getActivity(), "Location is Disabled", Toast.LENGTH_LONG).show();
            }
        }else{
            getPermissions();
        }
    }

    private boolean isLocationEnabled(){
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)||locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public boolean checkPermission(){
        if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            return true;
        }
        return false;
    }

    private void getPermissions(){
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==LOCATION_REQUEST_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //get location here
            }else{
                Toast.makeText(getActivity(), "permission not Granted", Toast.LENGTH_LONG).show();
            }
        }
    }

}
