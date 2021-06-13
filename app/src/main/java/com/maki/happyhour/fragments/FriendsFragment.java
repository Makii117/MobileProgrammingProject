package com.maki.happyhour.fragments;

import android.content.Context;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
    private static final String SUBSCRIBE_TO = "userABC";
    final protected String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final protected String serverKey = "key=" + "AAAA5p2qHhU:APA91bHuPVoulSKI5IOQGGMRwD96kfmgR--FgeM1A61Sfjw8YJJjLVMprGgB_AACQw1S-R369m-Sfjhr2eiDBNL0UWMe9HMGKV69L26JLBkNvqdmBpfpapgh9ItaLBrZeHehui1tnjwj\t\n";
    final protected String contentType = "application/json";
    final String TAG = "NOTIFICATION TAG";
    String token;
    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;
    String TOPIC;

    FirebaseStorage storage;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.friendslist,container,false);
        Geocoder geocoder = new Geocoder(getActivity(), Locale.ENGLISH);
        mAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        friendList=mainView.findViewById(R.id.friends_list);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }else {
                            // Get new FCM registration token
                            String token = task.getResult();
                            Log.d(TAG, token);

                        }
                        // Log and toast
                    }
                });
        FirebaseMessaging.getInstance().subscribeToTopic(SUBSCRIBE_TO);
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
                Log.d("IMGVAL1", String.valueOf(userModel.getPicture()));
              //get image from firebase storage
                if(String.valueOf(userModel.getPicture()).equals("Placeholder")){
                    Glide.with(getActivity()).load(R.drawable.logohappyhour).into(userViewModel.profile_pic);
                }else{

                    Glide.with(getActivity()).load(userModel.getPicture()).into(userViewModel.profile_pic);

                }




                Log.d("GLIDE_URI",userModel.getPicture());


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


                        TOPIC = "/topics/userABC"; //topic must match with what the receiver subscribed to
                        NOTIFICATION_TITLE = String.valueOf(mAuth.getCurrentUser().getDisplayName());
                        NOTIFICATION_MESSAGE = "Pinged you";

                        JSONObject notification = new JSONObject();
                        JSONObject notificationBody = new JSONObject();


                        try {
                            notificationBody.put("title", NOTIFICATION_TITLE);
                            notificationBody.put("message", NOTIFICATION_MESSAGE);

                            notification.put("to", TOPIC);
                            notification.put("data", notificationBody);
                            Log.d("JSONBOI", String.valueOf(notificationBody));
                            Log.d("JSONBOI1", String.valueOf(notification));
                        } catch (JSONException e) {
                            Log.e(TAG, "onCreate: " + e.getMessage() );
                        }
                        sendNotification(notification);



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

    private void sendNotification(JSONObject notification) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "onResponse: " + response.toString());

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Request error", Toast.LENGTH_LONG).show();
                        Log.i(TAG, "onErrorResponse: Didn't work");
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        MySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
    }

    public static class MySingleton {
        private  static MySingleton instance;
        private RequestQueue requestQueue;
        private Context ctx;

        private MySingleton(Context context) {
            ctx = context;
            requestQueue = getRequestQueue();
        }

        public static synchronized MySingleton getInstance(Context context) {
            if (instance == null) {
                instance = new MySingleton(context);
            }
            return instance;
        }

        public RequestQueue getRequestQueue() {
            if (requestQueue == null) {
                // getApplicationContext() is key, it keeps you from leaking the
                // Activity or BroadcastReceiver if someone passes one in.
                requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
            }
            return requestQueue;
        }

        public <T> void addToRequestQueue(Request<T> req) {
            getRequestQueue().add(req);
        }
    }

}
