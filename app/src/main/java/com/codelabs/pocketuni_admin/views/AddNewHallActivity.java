package com.codelabs.pocketuni_admin.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.codelabs.pocketuni_admin.R;
import com.codelabs.pocketuni_admin.utils.CustomAlertDialog;
import com.codelabs.pocketuni_admin.utils.CustomProgressDialog;
import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.libizo.CustomEditText;

import java.util.HashMap;
import java.util.Map;

public class AddNewHallActivity extends AppCompatActivity {

    private Button btnAddHall;
    private ImageView btnNewHallBack;
    private CustomEditText txtHallName;
    private CustomProgressDialog customProgressDialog;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_hall);

        btnAddHall = findViewById(R.id.btnAddNewHall);
        btnNewHallBack = findViewById(R.id.btn_newHallBack);
        txtHallName = findViewById(R.id.txt_hallName);

        customProgressDialog = new CustomProgressDialog(AddNewHallActivity.this);
        db = FirebaseFirestore.getInstance();

        btnNewHallBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddNewHallActivity.this, MainActivity.class));
                Animatoo.animateSlideRight(AddNewHallActivity.this);
                finishAffinity();
            }
        });

        btnAddHall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customProgressDialog.createProgress();
                if(txtHallName.getText().toString().isEmpty()){
                    customProgressDialog.dismissProgress();
                    new CustomAlertDialog().negativeAlert(AddNewHallActivity.this, "Something Went Wrong!!", "Please check Hall Name & try again!", "OK", CFAlertDialog.CFAlertStyle.ALERT);
                }else{
                    db.collection("Hall").add(hallData(txtHallName.getText().toString()))
                            .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    new CustomAlertDialog().positiveDismissAlert(AddNewHallActivity.this, "Congrats!!", "Hall has been added!", CFAlertDialog.CFAlertStyle.ALERT);
                                    clearData();
                                    customProgressDialog.dismissProgress();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    customProgressDialog.dismissProgress();
                                    new CustomAlertDialog().negativeAlert(AddNewHallActivity.this, "Something Went Wrong!!", "Please try again later!", "OK", CFAlertDialog.CFAlertStyle.ALERT);
                                }
                            });
                }
            }
        });
    }

    private Map hallData(String subjectName) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("name", subjectName);

        return userData;
    }

    private void clearData(){
        txtHallName.getText().clear();
    }
}