package com.maki.happyhour.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.maki.happyhour.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FriendsFragment extends Fragment {
    private RecyclerView friendList;
    String online_user_id;
    private View mainView;
    //all users
    public FriendsFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        friendList = (RecyclerView) mainView.findViewById(R.id.friends_list);

        // online_user_id= database get user
        //  get current user from database

        friendList.setLayoutManager(new LinearLayoutManager(getContext()));

        return mainView;
    }



    @Override
    public void onStart() {
        super.onStart();

    }
}
