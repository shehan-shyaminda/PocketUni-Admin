package com.codelabs.pocketuni_admin.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

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

public class AddNewLecturerActivity extends AppCompatActivity {

    private Button btnAddLecturer;
    private Spinner spnLecturerTitle;
    private CustomEditText txtLecturerName;
    private ImageView btnNewLecBack;
    private CustomProgressDialog customProgressDialog;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_lecturer);

        btnNewLecBack = findViewById(R.id.btn_newLecBack);
        btnAddLecturer = findViewById(R.id.btn_AddNewLecturer);
        spnLecturerTitle = findViewById(R.id.spn_lecturerTitle);
        txtLecturerName = findViewById(R.id.txt_newLecturerName);

        customProgressDialog = new CustomProgressDialog(AddNewLecturerActivity.this);
        db = FirebaseFirestore.getInstance();

        btnNewLecBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddNewLecturerActivity.this, MainActivity.class));
                Animatoo.animateSlideRight(AddNewLecturerActivity.this);
                finishAffinity();
            }
        });

        btnAddLecturer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customProgressDialog.createProgress();
                if (txtLecturerName.getText().toString().length() == 0){
                    customProgressDialog.dismissProgress();
                    new CustomAlertDialog().negativeAlert(AddNewLecturerActivity.this, "Something Went Wrong!!", "Please check Lecturer Name & try again!","OK", CFAlertDialog.CFAlertStyle.ALERT);
                }else{
                    db.collection("Lecturers").add(lecturerData(spnLecturerTitle.getSelectedItem().toString(), txtLecturerName.getText().toString()))
                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                customProgressDialog.dismissProgress();
                                new CustomAlertDialog().positiveDismissAlert(AddNewLecturerActivity.this, "Congrats!!", "New lecturer has been added!", CFAlertDialog.CFAlertStyle.ALERT);
                                clearData();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                customProgressDialog.dismissProgress();
                                new CustomAlertDialog().negativeDismissAlert(AddNewLecturerActivity.this, "Something Went Wrong!!", "Please try again later!",CFAlertDialog.CFAlertStyle.ALERT);
                            }
                        });
                }
            }
        });
    }

    private Map lecturerData(String title, String lecturerName) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("lecturerName", title + " " + lecturerName);

        return userData;
    }

    private void clearData(){
        txtLecturerName.getText().clear();
        spnLecturerTitle.setSelection(0);
    }
}