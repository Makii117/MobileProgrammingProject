package com.maki.happyhour.fragments;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.messaging.*;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.messaging.FirebaseMessaging;

import com.google.firebase.storage.FirebaseStorage;
import com.maki.happyhour.R;
import com.maki.happyhour.models.UserModel;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

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
    FirebaseAuth mAuth;

    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAA5p2qHhU:APA91bHuPVoulSKI5IOQGGMRwD96kfmgR--FgeM1A61Sfjw8YJJjLVMprGgB_AACQw1S-R369m-Sfjhr2eiDBNL0UWMe9HMGKV69L26JLBkNvqdmBpfpapgh9ItaLBrZeHehui1tnjwj";
    final private String contentType = "application/json";
    final String TAG = "NOTIFICATION TAG";
    String token;
    FirebaseStorage storage;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.friendslist,container,false);
        Geocoder geocoder = new Geocoder(getActivity(), Locale.ENGLISH);
        mAuth=FirebaseAuth.getInstance();
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

              //get image from firebase storage
                if(String.valueOf(userModel.getPicture()).equals("Placeholder")){
                    Glide.with(getActivity()).load(R.drawable.logohappyhour).into(userViewModel.profile_pic);
                }else{

                    Glide.with(getActivity()).load(userModel.getPicture()).into(userViewModel.profile_pic);

                }







                //get address
              Geocoder geocoder = new Geocoder(getActivity());
                    try {
                        List<Address> addresses = geocoder.getFromLocation(userModel.getLat(), userModel.getLon(), 1);
                        Log.d("ADRESSES", String.valueOf(addresses));
                        if (addresses.size() > 0) {
                            Address fetchedAddress = addresses.get(0);

                            Log.d("ADDR_1", String.valueOf(fetchedAddress));

                            StringBuilder strAddress = new StringBuilder();
                            strAddress.append(fetchedAddress.getAddressLine(0));

                            userViewModel.location_name.setText(strAddress);

                        } else {
                            Toast.makeText(getActivity(), "Searching", Toast.LENGTH_SHORT).show();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), "Error, couldn't get location", Toast.LENGTH_SHORT).show();
                    }







                    //ping users
                userViewModel.relativeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseMessaging.getInstance().getToken()
                                .addOnCompleteListener(new OnCompleteListener<String>() {
                                    @Override
                                    public void onComplete(@NonNull Task<String> task) {
                                        if (!task.isSuccessful()) {
                                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                                            return;
                                        }
                                        // Get new FCM registration token
                                        token = task.getResult();
                                    }
                                });

                        FirebaseMessaging.getInstance().subscribeToTopic(userModel.getId())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (!task.isSuccessful()) {
                                            Log.d("subtotop", "ERR");
                                        }

                                    }
                                });
                        String topic = userModel.getId();

                        // See documentation on defining a message payload.
                        RemoteMessage message = new RemoteMessage.Builder(userModel.getId()).addData("Name",mAuth.getCurrentUser().getDisplayName()).addData("message","pinged you").build();




                        // Send a message to the devices subscribed to the provided topic.
                        String response;
                        FirebaseMessaging.getInstance().send(message);
                        // Response is a message ID string.

                        Toast.makeText(getActivity(), "Pinged user", Toast.LENGTH_SHORT).show();






                    }
                });


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
        protected RelativeLayout relativeLayout;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            list_name=itemView.findViewById(R.id.username);
            location_name=itemView.findViewById(R.id.last_location);
            profile_pic=itemView.findViewById(R.id.profile_image);
            relativeLayout=itemView.findViewById(R.id.user_item);
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
