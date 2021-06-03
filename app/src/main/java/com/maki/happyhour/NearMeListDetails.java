package com.maki.happyhour;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.maki.happyhour.activities.MainActivity;

import androidx.appcompat.app.AppCompatActivity;

public class NearMeListDetails extends AppCompatActivity {
    private ImageView imageView;
    private TextView title;
    private TextView description;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_me_details);

        imageView = findViewById(R.id.facilityIMG);
        title = findViewById(R.id.facilityName);
        description = findViewById(R.id.facilityDescription);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            setTitle(extras.getString(MainActivity.EXTRA_TITLE));
            imageView.setImageResource(extras.getInt(MainActivity.EXTRA_IMAGE));
            title.setText(extras.getString(MainActivity.EXTRA_TITLE));
            description.setText(extras.getString(MainActivity.EXTRA_DESCRIPTION));
        }
    }
}
