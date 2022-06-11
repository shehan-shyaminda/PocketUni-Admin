package com.codelabs.pocketuni_admin.views;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.codelabs.pocketuni_admin.R;
import com.codelabs.pocketuni_admin.utils.CustomAlertDialog;
import com.codelabs.pocketuni_admin.utils.CustomProgressDialog;
import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.common.collect.Lists;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.libizo.CustomEditText;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class AddNewBatchActivity extends AppCompatActivity {

    private Button btnAddBatch;
    private Spinner spnBatchType, spnBatchCourse;
    private CustomEditText txt_batch, txt_noStudents, txt_batchCode;
    private ImageView btnNewBatchBack;
    private CustomProgressDialog customProgressDialog;
    private FirebaseFirestore db;
    private Pattern checkBatch, checkInt;
    private SecureRandom random;
    private StringBuilder sb;
    final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_batch);

        btnNewBatchBack = findViewById(R.id.btn_newBatchBack);
        btnAddBatch = findViewById(R.id.btnAddNewBatch);
        spnBatchType = findViewById(R.id.txtBatchType);
        spnBatchCourse = findViewById(R.id.txtCourse);
        txt_batch = findViewById(R.id.txtBatchYearIntake);
        txt_batchCode = findViewById(R.id.txtCourseShortName);
        txt_noStudents = findViewById(R.id.txtNumberOfStudents);

        customProgressDialog = new CustomProgressDialog(AddNewBatchActivity.this);
        db = FirebaseFirestore.getInstance();
        checkBatch = Pattern.compile("[0-9]{2}\\.[0-9]", Pattern.CASE_INSENSITIVE);
        checkInt = Pattern.compile("[0-9]{1,4}", Pattern.CASE_INSENSITIVE);

        init();

        btnNewBatchBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddNewBatchActivity.this, MainActivity.class));
                Animatoo.animateSlideRight(AddNewBatchActivity.this);
                finishAffinity();
            }
        });

        btnAddBatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customProgressDialog.createProgress();
                if (txt_batch.getText().toString().length() == 0 || spnBatchCourse.getSelectedItem().toString() == "Loading..." || txt_batchCode.getText().toString().length() == 0 || txt_noStudents.getText().toString().length() == 0) {
                    customProgressDialog.dismissProgress();
                    new CustomAlertDialog().negativeAlert(AddNewBatchActivity.this, "Something Went Wrong!!", "Please check details & try again!","OK", CFAlertDialog.CFAlertStyle.ALERT);
                }
                else if(!checkBatch.matcher(txt_batch.getText().toString()).matches()){
                    customProgressDialog.dismissProgress();
                    new CustomAlertDialog().negativeAlert(AddNewBatchActivity.this, "Something Went Wrong!!", "Invalid Batch Intake Year. Please try again!","OK", CFAlertDialog.CFAlertStyle.ALERT);
                }
                else if(!checkInt.matcher(txt_noStudents.getText().toString()).matches()){
                    customProgressDialog.dismissProgress();
                    new CustomAlertDialog().negativeAlert(AddNewBatchActivity.this, "Something Went Wrong!!", "Invalid student count. Please try again!","OK", CFAlertDialog.CFAlertStyle.ALERT);
                }
                else{
                    db.collection("Batches").document(txt_batch.getText().toString()).set(createBatch())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                db.collection("Batches").document(txt_batch.getText().toString()).collection("CO")
                                    .add(batchData(txt_batch.getText().toString(), spnBatchCourse.getSelectedItem().toString(), spnBatchType.getSelectedItem().toString(),
                                            txt_noStudents.getText().toString()))
                                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentReference> task) {
                                            String type = "F";
                                            if (spnBatchType.getSelectedItem().toString().equals("Full-Time")){
                                                type = "F";
                                            }else {
                                                type = "P";
                                            }
                                            List<List<String>> listOfLists = Lists.newArrayList();
//                                String[][] students =  new String[Integer.parseInt(txt_noStudents.getText().toString())][];

                                            for (int i=1; i<=Integer.parseInt(txt_noStudents.getText().toString()); i++){
                                                random = new SecureRandom();
                                                sb = new StringBuilder();
                                                String id = Integer.toString(i);

                                                if (i<=9){
                                                    id = "00"+i;
                                                }else if (i<=99){
                                                    id = "0"+i;
                                                }
                                                String email = txt_batchCode.getText().toString() + txt_batch.getText().toString().replace(".","") + type + "-" + id + "@STUDENT.NIBM.LK";
                                                for (int j = 0; j < 6; j++)
                                                {
                                                    int randomIndex = random.nextInt(chars.length());
                                                    sb.append(chars.charAt(randomIndex));
                                                }
                                                String batch = "F";
                                                if(spnBatchType.getSelectedItem().toString().equals("Full-Time")){
                                                    batch = "F";
                                                }else{
                                                    batch = "P";
                                                }

                                                db.collection("Student")
                                                        .document(email.toUpperCase()).set(studentData(txt_batch.getText().toString(), spnBatchCourse.getSelectedItem().toString(),
                                                                batch, email.toLowerCase(),"", sb.toString()));

                                                listOfLists.add(Lists.newArrayList(email.toLowerCase(), sb.toString()));
                                            }
                                            try {
                                                File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
                                                String studentLogins = txt_batchCode.getText().toString().toUpperCase() + txt_batch.getText().toString().replace(".","") + type + ".txt";
                                                File file = new File(root, studentLogins);
                                                if(!root.exists()){
                                                    root.mkdir();
                                                }
                                                FileWriter writer = new FileWriter(file);
                                                for (int i=0; i<Integer.parseInt(txt_noStudents.getText().toString()); i++) {
                                                    writer.append(listOfLists.get(i).get(0) + "  " + listOfLists.get(i).get(1) + "\n");
                                                }
                                                writer.flush();
                                                writer.close();

                                                Toast.makeText(AddNewBatchActivity.this, "Student login credentials saved on storage/documents", Toast.LENGTH_SHORT).show();
                                                Log.e(TAG, "onComplete: " + file.getPath() );
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                            customProgressDialog.dismissProgress();
                                            new CustomAlertDialog().positiveDismissAlert(AddNewBatchActivity.this, "Congrats!!", "Batch has created successfully!", CFAlertDialog.CFAlertStyle.NOTIFICATION);
                                            clearData();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            customProgressDialog.dismissProgress();
                                            new CustomAlertDialog().negativeAlert(AddNewBatchActivity.this, "Something Went Wrong!!", "Please try again Later!","OK", CFAlertDialog.CFAlertStyle.ALERT);
                                        }
                                    });
                            }
                        });
                }
            }
        });
    }

    private void init(){
        customProgressDialog.createProgress();
        db.collection("Courses").get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        ArrayList<String> coursesArray = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if (document != null) {
                                String course = document.get("courseName").toString();
                                coursesArray.add(course);
                            }
                        }
                        Collections.sort(coursesArray);
                        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(AddNewBatchActivity.this, android.R.layout.simple_spinner_dropdown_item, coursesArray);
                        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spnBatchCourse.setAdapter(spinnerArrayAdapter);
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
                    new CustomAlertDialog().negativeDismissAlert(AddNewBatchActivity.this, "Something Went Wrong!!", "Please try again later!",CFAlertDialog.CFAlertStyle.ALERT);
                }
            });
    }

    private Map batchData(String batch, String course, String batchType, String studentCount) {
        Map<String, Object> batchData = new HashMap<>();
        batchData.put("batch", batch);
        batchData.put("courseName", course);
        batchData.put("batchType", batchType);
        batchData.put("studentCount", studentCount);

        return batchData;
    }

    private Map studentData(String batch, String course, String batchType, String email, String name, String password) {
        Map<String, Object> batchData = new HashMap<>();
        batchData.put("studentBatch", batch);
        batchData.put("studentBatchType", course);
        batchData.put("studentCourse", batchType);
        batchData.put("studentEmail", email);
        batchData.put("studentName", name);
        batchData.put("studentPassword", password);

        return batchData;
    }

    private Map createBatch() {
        Map<String, Object> batchData = new HashMap<>();
        batchData.put("status", true);

        return batchData;
    }

    private void clearData(){
        txt_batch.getText().clear();
        txt_batchCode.getText().clear();
        txt_noStudents.getText().clear();
        spnBatchType.setSelection(0);
        spnBatchCourse.setSelection(0);
    }
}