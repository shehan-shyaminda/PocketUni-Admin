package com.codelabs.pocketuni_admin.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.codelabs.pocketuni_admin.R;
import com.codelabs.pocketuni_admin.utils.CustomProgressDialog;
import com.google.firebase.firestore.FirebaseFirestore;

public class HelpSupportActivity extends AppCompatActivity {

    private ImageView btnBack;
    private CustomProgressDialog customProgressDialog;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        btnBack = findViewById(R.id.btn_HelpUsBack);
        customProgressDialog = new CustomProgressDialog(HelpSupportActivity.this);
        db = FirebaseFirestore.getInstance();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HelpSupportActivity.this, MainActivity.class));
                Animatoo.animateSlideRight(HelpSupportActivity.this);
                finishAffinity();
            }
        });
    }
}