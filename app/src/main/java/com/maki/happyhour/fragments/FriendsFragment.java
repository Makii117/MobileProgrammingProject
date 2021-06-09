package com.maki.happyhour.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.auth.User;
import com.maki.happyhour.R;
import com.maki.happyhour.models.UserModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FriendsFragment extends Fragment {

    private FirebaseFirestore firebaseFirestore;
    private RecyclerView friendList;
    private FirestoreRecyclerAdapter adapter;
    private View mainView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        friendList = (RecyclerView) mainView.findViewById(R.id.friends_list);
        firebaseFirestore=FirebaseFirestore.getInstance();
        friendList=mainView.findViewById(R.id.friends_list);

        //Query
        Query query =firebaseFirestore.collection("users");

        //Recycler Options
        FirestoreRecyclerOptions<UserModel> options = new FirestoreRecyclerOptions.Builder<UserModel>()
                .setQuery(query,UserModel.class)
                .build();


        adapter = new FirestoreRecyclerAdapter<UserModel,UserViewModel>(options){
            @Override
            protected void onBindViewHolder(@NonNull UserViewModel userViewModel, int i, @NonNull UserModel userModel) {
              userViewModel.list_name.setText(userModel.getName());
              userViewModel.location_name.setText(userModel.getLocation());
            }

            @NonNull
            @Override
            public UserViewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.user_item,parent,false);

                return new UserViewModel(view);
            }
        };





        friendList.setLayoutManager(new LinearLayoutManager(getContext()));
        friendList.setAdapter(adapter);
        return mainView;
    }


    private class UserViewModel extends RecyclerView.ViewHolder {
        private TextView list_name;
        private TextView location_name;


        public UserViewModel(@NonNull View itemView) {
            super(itemView);

            list_name=itemView.findViewById(R.id.username);
            location_name=itemView.findViewById(R.id.last_location);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
