package com.codelabs.pocketuni_admin.views;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.codelabs.pocketuni_admin.R;
import com.codelabs.pocketuni_admin.utils.CustomAlertDialog;
import com.codelabs.pocketuni_admin.utils.CustomProgressDialog;
import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.libizo.CustomEditText;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ResetStudentActivity extends AppCompatActivity {

    private CustomEditText studentEmail;
    private Button btnUpdateStudent;
    private CustomProgressDialog customProgressDialog;
    private FirebaseFirestore db;
    private SecureRandom random;
    private StringBuilder sb;

    final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_student_passwords);

        btnUpdateStudent = findViewById(R.id.btnResetStudent);
        studentEmail = findViewById(R.id.txt_resetStudentEmail);
        customProgressDialog = new CustomProgressDialog(ResetStudentActivity.this);
        db = FirebaseFirestore.getInstance();

        btnUpdateStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customProgressDialog.createProgress();
                if(studentEmail.getText().toString().isEmpty()){
                    customProgressDialog.dismissProgress();
                    new CustomAlertDialog().negativeAlert(ResetStudentActivity.this, "Oops!", "Something went wrong.\nPlease check email address & try again!","OK", CFAlertDialog.CFAlertStyle.ALERT);
                }else{
                    db.collection("Student").document(studentEmail.getText().toString().trim().toUpperCase()).get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if(documentSnapshot.exists()){
                                        random = new SecureRandom();
                                        sb = new StringBuilder();

                                        for (int j = 0; j < 6; j++)
                                        {
                                            int randomIndex = random.nextInt(chars.length());
                                            sb.append(chars.charAt(randomIndex));
                                        }
                                        db.collection("Student").document(studentEmail.getText().toString().trim().toUpperCase()).set(studentData(sb.toString()))
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        try {
                                                            Properties properties = System.getProperties();

                                                            properties.put("mail.smtp.host", "smtp.gmail.com");
                                                            properties.put("mail.smtp.port", "465");
                                                            properties.put("mail.smtp.ssl.enable", "true");
                                                            properties.put("mail.smtp.auth", "true");

                                                            javax.mail.Session session = Session.getInstance(properties, new Authenticator() {
                                                                @Override
                                                                protected PasswordAuthentication getPasswordAuthentication() {
                                                                    return new PasswordAuthentication("pocketuni.nibm@gmail.com","nrokksgsbjvjbavm");
                                                                }
                                                            });

                                                            MimeMessage mimeMessage = new MimeMessage(session);
                                                            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(studentEmail.getText().toString().trim()));
                                                            mimeMessage.setSubject("Subject: Regarding Account Password Reset");
                                                            mimeMessage.setText("Hello Student, \n\nYou have registered PocketUni. \n\nYour Email : " + studentEmail.getText().toString().trim() + "\nYour Password : " +  sb.toString() +  " \n\nCheers!\n@Pocket_Uni");

                                                            Thread thread = new Thread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    try {
                                                                        Transport.send(mimeMessage);
                                                                        startActivity(new Intent(ResetStudentActivity.this, SuccessMailActivity.class));
                                                                        Animatoo.animateSlideLeft(ResetStudentActivity.this);
                                                                        customProgressDialog.dismissProgress();
                                                                    } catch (MessagingException e) {
                                                                        new CustomAlertDialog().negativeAlert(ResetStudentActivity.this, "Oops!", "Something went wrong.\nPlease try again later!","OK", CFAlertDialog.CFAlertStyle.ALERT);
                                                                        e.printStackTrace();
                                                                        customProgressDialog.dismissProgress();
                                                                    }
                                                                }
                                                            });
                                                            thread.start();

                                                        } catch (AddressException e) {
                                                            customProgressDialog.dismissProgress();
                                                            new CustomAlertDialog().negativeAlert(ResetStudentActivity.this, "Oops!", "Check Your Email Address & Please try again later!","OK", CFAlertDialog.CFAlertStyle.ALERT);
                                                            e.printStackTrace();
                                                        } catch (MessagingException e) {
                                                            customProgressDialog.dismissProgress();
                                                            new CustomAlertDialog().negativeAlert(ResetStudentActivity.this, "Oops!", "Something went wrong.\nPlease try again later!","OK", CFAlertDialog.CFAlertStyle.ALERT);
                                                            e.printStackTrace();
                                                        }catch (Exception e){
                                                            customProgressDialog.dismissProgress();
                                                            new CustomAlertDialog().negativeAlert(ResetStudentActivity.this, "Oops!", "Something went wrong.\nPlease try again later!","OK", CFAlertDialog.CFAlertStyle.ALERT);
                                                            e.printStackTrace();
                                                        }
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
                                    }else{
                                        customProgressDialog.dismissProgress();
                                        new CustomAlertDialog().negativeAlert(ResetStudentActivity.this, "Oops!", "Invalid Email Address.\nPlease try again!","OK", CFAlertDialog.CFAlertStyle.ALERT);
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
    }

    private Map studentData(String password) {
        Map<String, Object> studData = new HashMap<>();
        studData.put("studentPassword", password);

        return studData;
    }
}