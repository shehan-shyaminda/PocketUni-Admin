package com.codelabs.pocketuni_admin.views;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.libizo.CustomEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddBatchNoticeActivity extends AppCompatActivity {

    private Spinner spnBatch, spnBatchType, spnCourse;
    private Button addNotice;
    private CustomEditText noticeTitle, noticeBody;
    private ImageView btnBack;
    private CustomProgressDialog customProgressDialog;
    private FirebaseFirestore db;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_batch_notice);

        spnBatch = findViewById(R.id.notice_addSpinnerBatch);
        spnBatchType = findViewById(R.id.notice_addSpinnerBatchType);
        spnCourse = findViewById(R.id.notice_addSpinnerCourse);
        addNotice = findViewById(R.id.btnAddBNotice);
        noticeTitle = findViewById(R.id.notice_addTitleBatch);
        noticeBody = findViewById(R.id.notice_addTextBatch);
        btnBack = findViewById(R.id.btn_backAddBatchNotice);

        customProgressDialog = new CustomProgressDialog(AddBatchNoticeActivity.this);
        db = FirebaseFirestore.getInstance();
        calendar = Calendar.getInstance();

        init();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddBatchNoticeActivity.this, MainActivity.class));
                Animatoo.animateSlideRight(AddBatchNoticeActivity.this);
                finishAffinity();
            }
        });

        spnBatch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e(TAG, "onItemSelected: " + spnBatch.getItemAtPosition(i).toString() );
                customProgressDialog.createProgress();
                db.collection("Batches").document(spnBatch.getItemAtPosition(i).toString()).collection("CO").get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    ArrayList<String> coursesArray = new ArrayList<>();
                                    for (QueryDocumentSnapshot document : task.getResult()){
                                        if (document != null) {
                                            String courses = document.getString("courseName");
                                            Log.e(TAG, "onComplete: " + courses );
                                            coursesArray.add(courses);
                                        }
                                    }
                                    Collections.sort(coursesArray);
                                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(AddBatchNoticeActivity.this, android.R.layout.simple_spinner_dropdown_item, coursesArray);
                                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    spnCourse.setAdapter(spinnerArrayAdapter);
                                    customProgressDialog.dismissProgress();
                                } else {
                                    customProgressDialog.dismissProgress();
                                    Log.d(TAG, task.getException().getMessage());
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                customProgressDialog.dismissProgress();
                                Log.d(TAG, e.getMessage());
                            }
                        });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        addNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customProgressDialog.createProgress();
                String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(calendar.getTime());
                String batchType = "F";
                if (spnBatchType.getSelectedItem().toString().equals("Full-Time")){batchType = "F";}else{batchType = "P";}

                Log.e(TAG, "onClick: " +  spnBatch.getSelectedItem().toString());
                Log.e(TAG, "onClick: " +  spnCourse.getSelectedItem().toString());
                Log.e(TAG, "onClick: " +  batchType);

                db.collection("Batches").document(spnBatch.getSelectedItem().toString()).collection("CO")
                    .whereEqualTo("batch", spnBatch.getSelectedItem().toString())
                    .whereEqualTo("courseName", spnCourse.getSelectedItem().toString())
                    .whereEqualTo("batchType", batchType)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.getResult().isEmpty()){
                                customProgressDialog.dismissProgress();
                                new CustomAlertDialog().negativeAlert(AddBatchNoticeActivity.this, "Something Went Wrong!!", "Selected information doesn't match. Please try again!","OK", CFAlertDialog.CFAlertStyle.ALERT);
                            }else{
                                db.collection("Notices").add(noticeData(spnBatch.getSelectedItem().toString(), spnBatchType.getSelectedItem().toString(),
                                                spnCourse.getSelectedItem().toString(), noticeTitle.getText().toString(), noticeBody.getText().toString(), date))
                                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentReference> task) {
                                            new CustomAlertDialog().positiveDismissAlert(AddBatchNoticeActivity.this, "Congrats!!", "Batch Notice have been published!", CFAlertDialog.CFAlertStyle.ALERT);
                                            clearData();
                                            customProgressDialog.dismissProgress();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            customProgressDialog.dismissProgress();
                                            Log.e(TAG, "onFailure: " + e.getLocalizedMessage() );
                                        }
                                    });
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            customProgressDialog.dismissProgress();
                            Log.e(TAG, "onFailure: " + e.getLocalizedMessage() );
                        }
                    });
            }
        });
    }

    private void init(){
        customProgressDialog.createProgress();

        db.collection("Batches").get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        ArrayList<String> batchArray = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()){
                            if (document != null) {
                                String batch = document.getId();
                                batchArray.add(batch);
                            }
                        }
                        Collections.sort(batchArray);
                        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(AddBatchNoticeActivity.this, android.R.layout.simple_spinner_dropdown_item, batchArray);
                        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spnBatch.setAdapter(spinnerArrayAdapter);
                    } else {
                        customProgressDialog.dismissProgress();
                        Log.d(TAG, task.getException().getMessage());
                    }
                }
            });
    }

    private Map noticeData(String batch, String batchType, String course, String title, String body, String date) {

        if (batchType.equals("Full-Time")){
            batchType = "F";
        }else{
            batchType = "P";
        }

        Map<String, Object> userData = new HashMap<>();
        userData.put("noticeBatch", batch);
        userData.put("noticeBatchType", batchType);
        userData.put("noticeCourse", course);
        userData.put("noticeDate", date);
        userData.put("noticeDesc", body);
        userData.put("noticeTitle", title);

        return userData;
    }

    private void clearData() {
        spnBatch.setSelection(0);
        spnBatchType.setSelection(0);
        noticeTitle.getText().clear();
        noticeBody.getText().clear();
    }
}