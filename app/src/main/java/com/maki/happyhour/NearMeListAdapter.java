package com.maki.happyhour;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class NearMeListAdapter extends BaseAdapter {
    private Context context;
    private List<NearMeListModel> NearMeList;

    public NearMeListAdapter(Context context, List<NearMeListModel> NearMe){
        this.context = context;
        this.NearMeList = NearMeList;
    }

    @Override
    public int getCount() {
        return NearMeList.size();
    }

    @Override
    public Object getItem(int position) {
        return NearMeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return NearMeList.indexOf(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(R.layout.activity_near_me_list, parent, false);
        NearMeListModel nearMe = NearMeList.get(position);

        ImageView image = (ImageView) convertView.findViewById(R.id.facilityIMG);
        TextView title = (TextView) convertView.findViewById(R.id.facilityTitle);
        TextView shortDesc = (TextView) convertView.findViewById(R.id.facilityDescription);

        image.setImageResource(nearMe.getImageUri());
        title.setText(nearMe.getTitle());
        shortDesc.setText(nearMe.getTitle());

        return convertView;
    }
}