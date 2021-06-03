package com.maki.happyhour;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.maki.happyhour.databinding.NearmeFragmentBinding;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class NearMeActivity extends AppCompatActivity {

    NearmeFragmentBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = NearmeFragmentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int[] imageId = {R.drawable.a, R.drawable.b, R.drawable.c, R.drawable.d};

        String[] facilityName = {"Apple", "McDonalds", "Brajlovic", "KFC"};
        String[] facilityDescription = {"description #1", "description #2", "description #3", "description #4"};

        ArrayList<Catering_Facilities> cateringFacilitiesArrayList = new ArrayList<>();

        for(int i = 0; i < imageId.length; i++){
            Catering_Facilities cateringFacilities = new Catering_Facilities(facilityName[i], facilityDescription[i], imageId[i]);
            cateringFacilitiesArrayList.add(cateringFacilities);
        }
        ListAdapter listAdapter = new ListAdapter(NearMeActivity.this, cateringFacilitiesArrayList);

    }
}
