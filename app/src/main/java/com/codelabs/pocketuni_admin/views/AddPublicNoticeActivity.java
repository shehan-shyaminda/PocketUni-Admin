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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddPublicNoticeActivity extends AppCompatActivity {

    private CustomEditText noticeTitle, noticeBody;
    private ImageView buttonBack;
    private Button addNotice;
    private CustomProgressDialog customProgressDialog;
    private FirebaseFirestore db;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_public_notice);

        noticeTitle = findViewById(R.id.notice_addTitlePublic);
        noticeBody = findViewById(R.id.notice_addTextPublic);
        buttonBack = findViewById(R.id.btn_backAddPublicNotice);
        addNotice = findViewById(R.id.btnAddPNotice);

        customProgressDialog = new CustomProgressDialog(AddPublicNoticeActivity.this);
        db = FirebaseFirestore.getInstance();
        calendar = Calendar.getInstance();

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddPublicNoticeActivity.this, MainActivity.class));
                Animatoo.animateSlideRight(AddPublicNoticeActivity.this);
                finishAffinity();
            }
        });

        addNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customProgressDialog.createProgress();
                String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(calendar.getTime());
                if (noticeTitle.getText().toString().isEmpty() || noticeBody.getText().toString().isEmpty()){
                    customProgressDialog.dismissProgress();
                    new CustomAlertDialog().negativeAlert(AddPublicNoticeActivity.this, "Something Went Wrong!!", "Please check your information & try again!", "OK", CFAlertDialog.CFAlertStyle.ALERT);
                }else{
                    db.collection("Notifications").add(noticeData(noticeTitle.getText().toString(), noticeBody.getText().toString(), date))
                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                new CustomAlertDialog().positiveDismissAlert(AddPublicNoticeActivity.this, "Congrats!!", "Public Notice have been published!", CFAlertDialog.CFAlertStyle.ALERT);
                                clearData();
                                customProgressDialog.dismissProgress();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                customProgressDialog.dismissProgress();
                                new CustomAlertDialog().negativeAlert(AddPublicNoticeActivity.this, "Something Went Wrong!!", "Please try again later!", "OK", CFAlertDialog.CFAlertStyle.ALERT);
                            }
                        });
                }
            }
        });
    }

    private Map noticeData(String title, String body, String date) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("notificationDate", date);
        userData.put("notificationDesc", body);
        userData.put("notificationTitle", title);

        return userData;
    }

    private void clearData() {
        noticeTitle.getText().clear();
        noticeBody.getText().clear();
    }
}