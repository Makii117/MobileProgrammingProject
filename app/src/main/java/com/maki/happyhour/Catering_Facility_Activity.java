package com.maki.happyhour;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.maki.happyhour.databinding.ActivityCateringFacilityBinding;

public class Catering_Facility_Activity extends AppCompatActivity {

    ActivityCateringFacilityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCateringFacilityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = this.getIntent();

        if(intent != null){
            String name = intent.getStringExtra("facilityName");
            String description = intent.getStringExtra("facilityDescription");
            int imageId = intent.getIntExtra("imageId", R.drawable.a);

            binding.facilityName.setText(name);
            binding.facilityDescription.setText(description);
            binding.facilityIMG.setImageResource(imageId);

        }
    }
}