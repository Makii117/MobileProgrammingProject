package com.maki.happyhour.fragments;

import android.app.Activity;
import android.content.Intent;
import android.icu.util.ULocale;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.maki.happyhour.R;
import com.maki.happyhour.activities.LoginActivity;
import com.maki.happyhour.activities.MainActivity;
import com.maki.happyhour.models.UserLocation;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static android.content.ContentValues.TAG;

public class ProfileFragment extends Fragment implements View.OnClickListener {
    StorageReference storageRef;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
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
        change_picture.setOnClickListener(this);
        mAuth=FirebaseAuth.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference();

        StorageReference profileRef = storageRef.child("users/"+mAuth.getCurrentUser().getUid()+"/profile.jpg");
profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
    @Override
    public void onSuccess(Uri uri) {
         Picasso.get().load(uri).into(userImg);
    }
});
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
            location=(TextView)view.findViewById(R.id.last_loc);
            userImg=(ImageView)view.findViewById(R.id.user_img);

        }

        Geocoder geocoder = new Geocoder(getActivity());
        DocumentReference docRef=db.collection("users").document(mAuth.getCurrentUser().getUid());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                UserLocation loc=documentSnapshot.toObject(UserLocation.class);
                try {
                    List<Address> addresses = geocoder.getFromLocation(UserLocation.getLat(), UserLocation.getLon(), 1);

                    if (addresses.size() > 0) {
                        Address fetchedAddress = addresses.get(0);

                        StringBuilder strAddress = new StringBuilder();
                        strAddress.append(fetchedAddress.getAddressLine(0));


                        location.setText(strAddress);


                    } else {
                        Toast.makeText(getActivity(), "Searching", Toast.LENGTH_SHORT).show();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Error, couldn't get location", Toast.LENGTH_SHORT).show();
                }
            }
        });



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

                Intent openGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(openGallery,1000);


            break;
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1000){
            if(resultCode== Activity.RESULT_OK){
                Uri imgUri=data.getData();

               // userImg.setImageURI(imgUri);

                uploadToFirebase(imgUri);
            }
        }

    }

    private void uploadToFirebase(Uri imgUri) {
        final StorageReference fileRef= storageRef.child("users/"+mAuth.getCurrentUser().getUid()+"/profile.jpg");
        fileRef.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
             fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                 @Override
                 public void onSuccess(Uri uri) {
                     Picasso.get().load(uri).into(userImg);
                 }
             });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}


