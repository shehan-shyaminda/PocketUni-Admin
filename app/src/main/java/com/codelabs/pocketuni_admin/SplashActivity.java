package com.codelabs.pocketuni_admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.codelabs.pocketuni_admin.services.SharedPreferencesManager;

public class SplashActivity extends AppCompatActivity {

    private SharedPreferencesManager sharedPreferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sharedPreferencesManager = new SharedPreferencesManager(SplashActivity.this);

        Thread background = new Thread() {
            public void run() {
                try {
                    sleep(2000);

                    if(sharedPreferencesManager.getBooleanPreferences(SharedPreferencesManager.USER_LOGGED_IN) == true) {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    }else{
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    }
                    finishAffinity();
                } catch (Exception e) {
                }
            }
        };
        background.start();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
//                finishAffinity();
//            }
//        }, 3000);
    }
}