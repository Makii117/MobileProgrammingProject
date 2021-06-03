package com.maki.happyhour;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import static com.maki.happyhour.activities.MainActivity.EXTRA_DESCRIPTION;
import static com.maki.happyhour.activities.MainActivity.EXTRA_IMAGE;
import static com.maki.happyhour.activities.MainActivity.EXTRA_TITLE;


public class NearMeList extends FragmentActivity {
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_me_list);
        listView = findViewById(R.id.listview);
        NearMeListAdapter nearMeListAdapter = new NearMeListAdapter(this, getNearMe());
        listView.setAdapter(nearMeListAdapter);
        listView.setOnItemClickListener(onItemClickListener);
    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            NearMeListModel nearMeListModel = (NearMeListModel) parent.getItemAtPosition(position);
            Intent intent = new Intent(NearMeList.this,NearMeListDetails.class);
            intent.putExtra(EXTRA_IMAGE,nearMeListModel.getImageUri());
            intent.putExtra(EXTRA_TITLE, nearMeListModel.getTitle());
            intent.putExtra(EXTRA_DESCRIPTION, nearMeListModel.getShortDescription());
            startActivity(intent);
        }
    };


    private List<NearMeListModel> getNearMe(){
        List<NearMeListModel> nearMeList = new ArrayList<>();
        nearMeList.add(new NearMeListModel(R.drawable.a, "Title #1", "short descrition1"));
        nearMeList.add(new NearMeListModel(R.drawable.b, "Title #2", "short descrition2"));
        nearMeList.add(new NearMeListModel(R.drawable.c, "Title #3", "short descrition3"));
        return nearMeList;
    }

}