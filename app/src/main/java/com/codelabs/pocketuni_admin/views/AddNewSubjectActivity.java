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

public class AddNewSubjectActivity extends AppCompatActivity {

    private Button btnAddSub;
    private ImageView btnNewSubBack;
    private CustomEditText txtSubName;
    private CustomProgressDialog customProgressDialog;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_subject);

        btnAddSub = findViewById(R.id.btnAddNewSub);
        btnNewSubBack = findViewById(R.id.btn_newSubBack);
        txtSubName = findViewById(R.id.txt_subName);

        customProgressDialog = new CustomProgressDialog(AddNewSubjectActivity.this);
        db = FirebaseFirestore.getInstance();

        btnNewSubBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddNewSubjectActivity.this, MainActivity.class));
                Animatoo.animateSlideRight(AddNewSubjectActivity.this);
                finishAffinity();
            }
        });

        btnAddSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customProgressDialog.createProgress();
                if(txtSubName.getText().toString().isEmpty()){
                    customProgressDialog.dismissProgress();
                    new CustomAlertDialog().negativeAlert(AddNewSubjectActivity.this, "Something Went Wrong!!", "Please check Subject Name & try again!", "OK", CFAlertDialog.CFAlertStyle.ALERT);
                }else{
                    db.collection("Subject").add(subjectData(txtSubName.getText().toString()))
                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                new CustomAlertDialog().positiveDismissAlert(AddNewSubjectActivity.this, "Congrats!!", "Subject has been added!", CFAlertDialog.CFAlertStyle.ALERT);
                                clearData();
                                customProgressDialog.dismissProgress();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                customProgressDialog.dismissProgress();
                                new CustomAlertDialog().negativeAlert(AddNewSubjectActivity.this, "Something Went Wrong!!", "Please try again later!", "OK", CFAlertDialog.CFAlertStyle.ALERT);
                            }
                        });
                }
            }
        });
    }

    private Map subjectData(String subjectName) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("subjectName", subjectName);

        return userData;
    }

    private void clearData(){
        txtSubName.getText().clear();
    }
}