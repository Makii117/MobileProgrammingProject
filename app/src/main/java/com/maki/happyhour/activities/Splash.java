package com.maki.happyhour.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.maki.happyhour.R;

import static java.lang.Thread.sleep;

public class Splash extends AppCompatActivity {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sleep(3000);
                    if(user==null){
                        Intent i = new Intent(Splash.this, LoginFragment.class);
                        startActivity(i);
                    }else {
                        Intent i1 = new Intent(Splash.this, MainActivity.class);
                        startActivity(i1);
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    thread.start();
    }
}