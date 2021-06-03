package com.maki.happyhour.activities;

import android.content.Intent;
import android.os.Bundle;

import com.maki.happyhour.R;

import androidx.appcompat.app.AppCompatActivity;

import static java.lang.Thread.sleep;

public class LoginFragment extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_fragment);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sleep(5000);
                    Intent i = new Intent(LoginFragment.this, MainActivity.class);
                    startActivity(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}


