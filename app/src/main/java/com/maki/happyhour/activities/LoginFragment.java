package com.maki.happyhour.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.maki.happyhour.R;

import androidx.appcompat.app.AppCompatActivity;

import static java.lang.Thread.sleep;

public class LoginFragment extends AppCompatActivity {
    Button register,login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_fragment);
        register = findViewById(R.id.button);
        login = findViewById(R.id.button2);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                register.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent register = new Intent(LoginFragment.this,RegisterActivity.class);
                        startActivity(register);
                    }
                });
                login.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent login = new Intent(LoginFragment.this, LoginActivity.class);
                        startActivity(login);
                    }
                });
            }
        });
        thread.start();
    }
}


