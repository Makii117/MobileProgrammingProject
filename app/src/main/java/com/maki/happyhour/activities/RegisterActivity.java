package com.maki.happyhour.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.maki.happyhour.R;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class RegisterActivity extends AppCompatActivity {
    TextView btn;
    private EditText inputUsername,inputEmail,inputPassword,inputConfirmPassword,inputName;
    Button btnRegister;
    private FirebaseAuth mAuth;
    private ProgressDialog mLoadingBar;
    private FirebaseFirestore mDB;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btn = findViewById(R.id.have_acc);


        inputEmail = findViewById(R.id.input_email);
        inputPassword = findViewById(R.id.input_pass);
        inputConfirmPassword = findViewById(R.id.input_confirm_pass);
        inputName=findViewById(R.id.input_name);

        mAuth = FirebaseAuth.getInstance();
        mDB = FirebaseFirestore.getInstance();

        mLoadingBar = new ProgressDialog(RegisterActivity.this);

        btnRegister = findViewById(R.id.btn_register);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));

            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCredentials();
            }

        });
    }
        private void checkCredentials() {
            String name = inputName.getText().toString();
            String email = inputEmail.getText().toString();
            String password = inputPassword.getText().toString();
            String confirmPass = inputConfirmPassword.getText().toString();


            if (name.isEmpty()) {
                showError(inputName, "Cant be empty");
            } else if (email.isEmpty() || !email.contains("@")) {
                showError(inputEmail, "Invalid email");
            } else if (password.isEmpty() || password.length()<7) {
                showError(inputPassword, "Invalid password");
            } else if (confirmPass.isEmpty() || !confirmPass.equals(password)) {
                showError(inputConfirmPassword, "Passwords must match");
            } else {
                mLoadingBar.setTitle("Register Successful");
                mLoadingBar.setMessage("Checking credentials, please wait");
                mLoadingBar.setCanceledOnTouchOutside(false);
                mLoadingBar.show();


                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Registered Successful", Toast.LENGTH_SHORT).show();
                            mLoadingBar.dismiss();

                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name)
                                    .setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
                                    .build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                userID = mAuth.getCurrentUser().getUid();
                                                DocumentReference documentReference = mDB.collection("users").document(userID);
                                                Map<String, Object> user = new HashMap<>();
                                                user.put("Name",name);
                                                user.put("Location","N/A");
                                                user.put("Picture","Placeholder");

                                                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d(TAG,"Successfully registered");
                                                    }
                                                });


                                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(RegisterActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        }
    private void showError(EditText input,String s) {
        input.setError(s);
        input.requestFocus();
    }



    }
