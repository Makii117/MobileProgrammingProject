package com.maki.happyhour;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<Catering_Facilities> {
    public ListAdapter(Context context, ArrayList<Catering_Facilities> cateringFacilitiesArrayList){
        super(context, R.layout.activity_list_view, cateringFacilitiesArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Catering_Facilities cateringFacilities = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_list_view, parent,false);
        }

        ImageView imageView = convertView.findViewById((R.id.facilityIMG));
        TextView title = convertView.findViewById(R.id.title);
        TextView description = convertView.findViewById(R.id.description);

        imageView.setImageResource(cateringFacilities.imageId);
        title.setText(cateringFacilities.facilityName);
        description.setText(cateringFacilities.facilityDescription);

        return super.getView(position, convertView, parent);
    }
}
