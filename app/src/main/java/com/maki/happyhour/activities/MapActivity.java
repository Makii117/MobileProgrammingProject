package com.maki.happyhour.activities;

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

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final int LOCATION_REQUEST_CODE = 123;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_fragment);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

    }
    public void onClick(View view){
        switch(view.getId()){
            case R.id.sarajevo_map:
                LatLng sarajevo = new LatLng(44, 18);
                mMap.clear();
                mMap.animateCamera(CameraUpdateFactory.newLatLng(sarajevo));
                break;
            case R.id.location_search:
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
                                Toast.makeText(MapActivity.this, "Unable to get location", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                );
            }else{
                Toast.makeText(MapActivity.this, "Location is Disabled", Toast.LENGTH_LONG).show();
            }
        }else{
            getPermissions();
        }
    }

    private boolean isLocationEnabled(){
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)||locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sarajevo = new LatLng(44, 18);
        mMap.addMarker(new MarkerOptions().position(sarajevo).title("Marker in Sarajevo"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sarajevo));



    }

    public boolean checkPermission(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            return true;
        }
        return false;
    }

    private void getPermissions(){
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==LOCATION_REQUEST_CODE){
          if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
              //get location here
          }else{
              Toast.makeText(this, "permission not Granted", Toast.LENGTH_LONG).show();
          }
        }
    }
}