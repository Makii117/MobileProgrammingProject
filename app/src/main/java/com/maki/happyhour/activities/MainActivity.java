package com.maki.happyhour.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.maki.happyhour.R;
import com.maki.happyhour.fragments.FriendsFragment;
import com.maki.happyhour.fragments.MapFragment;
import com.maki.happyhour.fragments.NearMeFragment;
import com.maki.happyhour.fragments.ProfileFragment;
import com.maki.happyhour.fragments.SettingsFragment;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DrawerLayout drawer;
    MapFragment mapFragment;
    FirebaseAuth mAuth;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FusedLocationProviderClient fusedLocationClient;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mAuth=FirebaseAuth.getInstance();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState==null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MapFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_map);
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_map:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MapFragment()).commit();
                break;
            case R.id.nav_nearMe:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new NearMeFragment()).commit();
                break;
                case R.id.nav_friends:
                  getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FriendsFragment()).commit();
                break;
            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
                break;
            case R.id.nav_settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).commit();
                break;
            case R.id.nav_share:
                //update user location

                fusedLocationClient.getLastLocation()
                       .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                           @Override
                           public void onSuccess(Location location) {

                               DocumentReference userLoc = db.collection("users").document(mAuth.getCurrentUser().getUid());
                                Map<String,Object> loc = new HashMap<>();
                               loc.put("lat",location.getLatitude());
                               loc.put("lon",location.getLongitude());

                               userLoc.update(loc).addOnSuccessListener(new OnSuccessListener<Void>() {
                                   @Override
                                   public void onSuccess(Void aVoid) {
                                       Toast.makeText(MainActivity.this, "Shared Location", Toast.LENGTH_SHORT).show();

                                   }
                               }).addOnFailureListener(new OnFailureListener() {
                                   @Override
                                   public void onFailure(@NonNull Exception e) {
                                       Toast.makeText(MainActivity.this, "Error, couldn't share location", Toast.LENGTH_SHORT).show();
                                   }
                               });

                                if (location != null) {
                                    Toast.makeText(MainActivity.this, "Error, location null", Toast.LENGTH_SHORT).show();
                                }
                          }
                      });

                break;
        }
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onBackPressed(){
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == MapFragment.MY_PERMISSIONS_REQUEST_LOCATION){
            mapFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

}