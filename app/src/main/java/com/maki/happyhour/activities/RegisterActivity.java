package com.maki.happyhour.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.maki.happyhour.R;

public class RegisterActivity extends AppCompatActivity {
    Button btnRegister;
    private FirebaseAuth mAuth;
    private ProgressDialog mLoadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_fragment);

        inputUsername = findViewById(R.id.input_user);
        inputEmail=findViewById(R.id.input_email);
        inputPassword = findViewById(R.id.input_pass);
        inputConfirmPassword=findViewById(R.id.input_confirm_pass);

        mAuth=FirebaseAuth.getInstance();
        mLoadingBar=new ProgressDialog(RegisterActivity.this);

        btnRegister=findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v){
               checkCredentials();
           }

        });

        private void checkCredentials(){
            String username=inputUsername.getText().toString();
            String email = inputEmail.getText().toString();
            String password = inputPassword.getText().toString();
            String confirmPass= inputConfirmPassword.getText().toString();
        }

        if(username.isEmpty()){
            showError(inputUsername,"Invalid username");
        }else if(email.isEmpty() || !email.contains("@")){
            showError(inputEmail,"Invalid email");
        }else if(password.isEmpty() || !email.contains("@")){
            showError(inputPassword,"Invalid email");
        }else if(confirmPass.isEmpty() || !email.contains("@")){
            showError(inputConfirmPassword,"Invalid email");
        }else{
            mLoadingBar.setTitle("Register Successful");
            mLoadingBar.setMessage("Checking credentials, please wait");
            mLoadingBar.setCanceledOnTouchOutside(false);
            mLoadingBar.show();


            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(RegisterActivity.this, "Registered Successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    }else{
                        Toast.makeText(RegisterActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });


        }






    }
}