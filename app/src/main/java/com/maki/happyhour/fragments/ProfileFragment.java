package com.maki.happyhour.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.maki.happyhour.R;
import com.maki.happyhour.activities.LoginActivity;
import com.maki.happyhour.activities.MainActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment implements View.OnClickListener {
    View view;
    FirebaseAuth mAuth;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    TextView name,mail,phone,location;
    ImageView userImg;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_profile,container,false);
        Button sign_out=(Button) view.findViewById(R.id.sign_out);
        Button change_picture=(Button)view.findViewById(R.id.change_pic);
        sign_out.setOnClickListener(this);
        mAuth=FirebaseAuth.getInstance();

        if (user != null) {
            // Name, email address, and profile photo Url
            String Uname = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();
            String Uphone = user.getPhoneNumber();
            //String location = user.getLocation();
            name=(TextView)view.findViewById(R.id.name_surname);
            name.setText(Uname);
            mail=(TextView)view.findViewById(R.id.user_email);
            mail.setText(email);
            phone=(TextView)view.findViewById(R.id.phone_num);
            phone.setText(Uphone);
            location=(TextView)view.findViewById(R.id.last_loc);
            location.setText("Last Location");
            userImg=(ImageView)view.findViewById(R.id.user_img);

        }


        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.sign_out:
                mAuth.signOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.change_pic:
                //Intent openGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                break;
        }


    }
    }

