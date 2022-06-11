package com.codelabs.pocketuni_admin.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.codelabs.pocketuni_admin.R;

public class SuccessMailActivity extends AppCompatActivity {

    private Button btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_mail);

        btnLogin = findViewById(R.id.btn_success_email_send);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SuccessMailActivity.this, MainActivity.class));
                Animatoo.animateSlideRight(SuccessMailActivity.this);
                finishAffinity();
            }
        });
    }
}