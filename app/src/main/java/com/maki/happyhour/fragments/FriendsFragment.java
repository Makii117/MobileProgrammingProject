package com.maki.happyhour.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.maki.happyhour.R;
import com.maki.happyhour.models.UserModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FriendsFragment extends Fragment {
    StorageReference storageRef;
    private FirebaseFirestore firebaseFirestore;
    private RecyclerView friendList;
    private FirestoreRecyclerAdapter adapter;
    private View mainView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.friendslist,container,false);
        storageRef = FirebaseStorage.getInstance().getReference();


        firebaseFirestore=FirebaseFirestore.getInstance();
        friendList=mainView.findViewById(R.id.friends_list);

        //Query
        Query query =firebaseFirestore.collection("users");

        //Recycler Options
        FirestoreRecyclerOptions<UserModel> options = new FirestoreRecyclerOptions.Builder<UserModel>()
                .setQuery(query,UserModel.class)
                .build();


        adapter = new FirestoreRecyclerAdapter<UserModel,RecyclerViewHolder>(options){
            @Override
            protected void onBindViewHolder(@NonNull RecyclerViewHolder userViewModel, int i, @NonNull UserModel userModel) {
              userViewModel.list_name.setText(userModel.getName());
              userViewModel.location_name.setText(userModel.getLocation());
              userViewModel.profile_pic.setImageURI(Uri.parse(userModel.getPicture()));
            }


            @NonNull
            @Override
            public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.user_item,parent,false);

                return new RecyclerViewHolder(view);
            }
        };





        friendList.setLayoutManager(new LinearLayoutManager(getContext()));
        friendList.setAdapter(adapter);
        return mainView;
    }


    private class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private TextView list_name;
        private TextView location_name;
        private ImageView profile_pic;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            list_name=itemView.findViewById(R.id.username);
            location_name=itemView.findViewById(R.id.last_location);
            profile_pic=itemView.findViewById(R.id.profile_image);
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
