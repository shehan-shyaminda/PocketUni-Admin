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

public class AddNewCourseActivity extends AppCompatActivity {

    private Button btnAddCourse;
    private ImageView btnNewCourseBack;
    private CustomEditText txtCourseName;
    private CustomProgressDialog customProgressDialog;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_course);

        btnAddCourse = findViewById(R.id.btnAddNewCourse);
        btnNewCourseBack = findViewById(R.id.btn_newCourseBack);
        txtCourseName = findViewById(R.id.txt_courseName);

        customProgressDialog = new CustomProgressDialog(AddNewCourseActivity.this);
        db = FirebaseFirestore.getInstance();


        btnNewCourseBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddNewCourseActivity.this, MainActivity.class));
                Animatoo.animateSlideRight(AddNewCourseActivity.this);
                finishAffinity();
            }
        });

        btnAddCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customProgressDialog.createProgress();
                if (txtCourseName.getText().toString().isEmpty()) {
                    customProgressDialog.dismissProgress();
                    new CustomAlertDialog().negativeAlert(AddNewCourseActivity.this, "Something Went Wrong!!", "Please check Course Name & try again!", "OK", CFAlertDialog.CFAlertStyle.ALERT);
                } else {
                    db.collection("Courses").add(courseData(txtCourseName.getText().toString()))
                            .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    new CustomAlertDialog().positiveDismissAlert(AddNewCourseActivity.this, "Congrats!!", "Course has been added!", CFAlertDialog.CFAlertStyle.ALERT);
                                    clearData();
                                    customProgressDialog.dismissProgress();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    customProgressDialog.dismissProgress();
                                    new CustomAlertDialog().negativeAlert(AddNewCourseActivity.this, "Something Went Wrong!!", "Please try again later!", "OK", CFAlertDialog.CFAlertStyle.ALERT);
                                }
                            });
                }
            }
        });
    }

    private Map courseData(String subjectName) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("courseName", subjectName);

        return userData;
    }

    private void clearData() {
        txtCourseName.getText().clear();
    }
}