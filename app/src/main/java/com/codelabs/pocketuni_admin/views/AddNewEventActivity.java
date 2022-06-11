package com.codelabs.pocketuni_admin.views;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddNewEventActivity extends AppCompatActivity {

    private Button btnAddEvent;
    private Spinner spnEventBatch, spnEventCourse, spnEventBatchType, spnEventHall, spnEventLecturer, spnEventSubject, spnEventType;
    private ImageView btnNewEventBack;
    private TextView txtClock, txtCalender;
    private ConstraintLayout btnClock, btnCalender;
    private CustomProgressDialog customProgressDialog;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_event);

        btnNewEventBack = findViewById(R.id.btn_newEventBack);
        btnAddEvent = findViewById(R.id.btnAddNewEvent);
        btnClock = findViewById(R.id.btn_clock);
        btnCalender = findViewById(R.id.btn_calender);
        txtCalender = findViewById(R.id.txtEventCalender);
        txtClock = findViewById(R.id.txtEventClock);
        spnEventBatch = findViewById(R.id.spinnerBatch);
        spnEventCourse = findViewById(R.id.spinnerCourse);
        spnEventBatchType = findViewById(R.id.spinnerBatchType);
        spnEventHall = findViewById(R.id.spinnerEventHall);
        spnEventLecturer = findViewById(R.id.spinnerLecturer);
        spnEventSubject = findViewById(R.id.spinnerEventSubject);
        spnEventType = findViewById(R.id.spinnerEventType);

        customProgressDialog = new CustomProgressDialog(AddNewEventActivity.this);
        db = FirebaseFirestore.getInstance();

        init();

        btnCalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddNewEventActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        Log.e(TAG, "Date: " + i2 + "/" + (i1 + 1) + "/" + i);
                        txtCalender.setText(i2 + "/" + (i1 + 1) + "/" + i);
                    }
                }, year, month, day);
                datePickerDialog.getDatePicker().setMinDate(new Date().getTime());
                datePickerDialog.show();
            }
        });

        btnClock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                new TimePickerDialog(AddNewEventActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        Log.e(TAG, "onTimeSet: " + selectedHour + ":" + selectedMinute );
                        String hour = Integer.toString(selectedHour);
                        String minute = Integer.toString(selectedMinute);
                        if (selectedHour <= 9){
                            hour = "0" + selectedHour;
                        }
                        if(selectedMinute <=9){
                            minute = "0" + minute;
                        }
                        txtClock.setText(hour + ":" + minute);
                    }
                }, hour, minute, true).show();
            }
        });

        btnNewEventBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddNewEventActivity.this, MainActivity.class));
                Animatoo.animateSlideRight(AddNewEventActivity.this);
                finishAffinity();
            }
        });

        btnAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customProgressDialog.createProgress();
                if(txtCalender.getText().toString().isEmpty() || txtClock.getText().toString().isEmpty() || spnEventBatch.getSelectedItem() == null ||
                        spnEventCourse.getSelectedItem() == null || spnEventBatchType.getSelectedItem() == null || spnEventHall.getSelectedItem() == null ||
                        spnEventSubject.getSelectedItem() == null || spnEventLecturer.getSelectedItem() == null || spnEventType.getSelectedItem() == null){
                    customProgressDialog.dismissProgress();
                    new CustomAlertDialog().negativeAlert(AddNewEventActivity.this, "Something Went Wrong!!", "Please check details & try again!","OK", CFAlertDialog.CFAlertStyle.ALERT);
                }
                else{
                    String batchType = "F";
                    if (spnEventBatchType.getSelectedItem().toString().equals("Full-Time")){batchType = "F";}else{batchType = "P";}

                    Log.e(TAG, "onClick: " +  spnEventBatch.getSelectedItem().toString());
                    Log.e(TAG, "onClick: " +  spnEventCourse.getSelectedItem().toString());
                    Log.e(TAG, "onClick: " +  batchType);

                    db.collection("Batches").document(spnEventBatch.getSelectedItem().toString()).collection("CO")
                        .whereEqualTo("batch", spnEventBatch.getSelectedItem().toString())
                        .whereEqualTo("courseName", spnEventCourse.getSelectedItem().toString())
                        .whereEqualTo("batchType", batchType)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.getResult().isEmpty()){
                                    customProgressDialog.dismissProgress();
                                    new CustomAlertDialog().negativeAlert(AddNewEventActivity.this, "Something Went Wrong!!", "Selected information doesn't match. Please try again!","OK", CFAlertDialog.CFAlertStyle.ALERT);
                                }else{
                                    db.collection("Events").add(setEventData(txtCalender.getText().toString(), txtClock.getText().toString(),
                                                    spnEventBatch.getSelectedItem().toString(), spnEventCourse.getSelectedItem().toString(),spnEventBatchType.getSelectedItem().toString(),
                                                    spnEventHall.getSelectedItem().toString(), spnEventSubject.getSelectedItem().toString(), spnEventLecturer.getSelectedItem().toString(),
                                                    spnEventType.getSelectedItem().toString()))
                                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                                new CustomAlertDialog().positiveDismissAlert(AddNewEventActivity.this, "Congrats!!", "Event has created successfully!", CFAlertDialog.CFAlertStyle.ALERT);
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
            }
        });

        spnEventBatch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                customProgressDialog.createProgress();
                Log.e(TAG, "onItemSelected: " + spnEventBatch.getItemAtPosition(i).toString() );
                db.collection("Batches").document(spnEventBatch.getItemAtPosition(i).toString()).collection("CO").get()
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
                                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(AddNewEventActivity.this, android.R.layout.simple_spinner_dropdown_item, coursesArray);
                                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    spnEventCourse.setAdapter(spinnerArrayAdapter);
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
                        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(AddNewEventActivity.this, android.R.layout.simple_spinner_dropdown_item, batchArray);
                        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spnEventBatch.setAdapter(spinnerArrayAdapter);
                    } else {
                        Log.d(TAG, task.getException().getMessage());
                    }
                }
            });

        db.collection("Hall").get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        ArrayList<String> hallArray = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()){
                            if (document != null) {
                                String hall = document.getString("name");
                                hallArray.add(hall);
                            }
                        }
                        Collections.sort(hallArray);
                        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(AddNewEventActivity.this, android.R.layout.simple_spinner_dropdown_item, hallArray);
                        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spnEventHall.setAdapter(spinnerArrayAdapter);
                    } else {
                        Log.d(TAG, task.getException().getMessage());
                    }
                }
            });

        db.collection("Lecturers").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<String> lecturerArray = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()){
                                if (document != null) {
                                    String lecturer = document.getString("lecturerName");
                                    lecturerArray.add(lecturer);
                                }
                            }
                            Collections.sort(lecturerArray);
                            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(AddNewEventActivity.this, android.R.layout.simple_spinner_dropdown_item, lecturerArray);
                            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spnEventLecturer.setAdapter(spinnerArrayAdapter);
                        } else {
                            Log.d(TAG, task.getException().getMessage());
                        }
                    }
                });

        db.collection("Subject").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<String> subjectArray = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()){
                                if (document != null) {
                                    String subject = document.getString("subjectName");
                                    subjectArray.add(subject);
                                }
                            }
                            Collections.sort(subjectArray);
                            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(AddNewEventActivity.this, android.R.layout.simple_spinner_dropdown_item, subjectArray);
                            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spnEventSubject.setAdapter(spinnerArrayAdapter);
                        } else {
                            Log.d(TAG, task.getException().getMessage());
                        }
                    }
                });
    }

    private Map setEventData(String txtCalender, String txtClock, String spnEventBatch, String spnEventCourse, String spnEventBatchType, String spnEventHall,
                          String spnEventSubject, String spnEventLecturer, String spnEventType) {

        if (spnEventBatchType.equals("Full-Time")){
            spnEventBatchType = "F";
        }else{
            spnEventBatchType = "P";
        }

        Map<String, Object> eventData = new HashMap<>();
        eventData.put("eventBatch", spnEventBatch);
        eventData.put("eventBatchType", spnEventBatchType);
        eventData.put("eventCourse", spnEventCourse);
        eventData.put("eventDate", txtCalender);
        eventData.put("eventHallName", spnEventHall);
        eventData.put("eventLecturer", spnEventLecturer);
        eventData.put("eventSubject", spnEventSubject);
        eventData.put("eventTime", txtClock);
        eventData.put("eventType", spnEventType.toUpperCase());

        return eventData;
    }
    
    private void clearData(){
        txtClock.setText("");
        txtCalender.setText("");
        spnEventBatch.setSelection(0);
        spnEventBatchType.setSelection(0);
        spnEventHall.setSelection(0);
        spnEventSubject.setSelection(0);
        spnEventLecturer.setSelection(0);
        spnEventType.setSelection(0);
    }
}
