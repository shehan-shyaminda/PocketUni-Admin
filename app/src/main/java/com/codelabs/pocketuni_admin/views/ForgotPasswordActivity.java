package com.codelabs.pocketuni_admin.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.codelabs.pocketuni_admin.R;
import com.codelabs.pocketuni_admin.utils.CustomAlertDialog;
import com.codelabs.pocketuni_admin.utils.CustomProgressDialog;
import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.libizo.CustomEditText;

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

public class ForgotPasswordActivity extends AppCompatActivity {

    private ImageView btnBack;
    private Button btnSend;
    private CustomEditText txtEmail;
    private TextView btnLogin;
    private CustomProgressDialog customProgressDialog;
    private CustomAlertDialog customAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        btnBack = findViewById(R.id.ic_forgot_password_back);
        btnSend = findViewById(R.id.btn_forgot_password_send);
        btnLogin = findViewById(R.id.btn_forgot_password_login);
        txtEmail = findViewById(R.id.txt_ForgotPassword_Email);

        customProgressDialog = new CustomProgressDialog(ForgotPasswordActivity.this);
        customAlertDialog = new CustomAlertDialog();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
                Animatoo.animateSlideRight(ForgotPasswordActivity.this);
                finishAffinity();
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customProgressDialog.createProgress();
                if(txtEmail.getText().toString().length() == 0){
                    customProgressDialog.dismissProgress();
                    customAlertDialog.negativeAlert(ForgotPasswordActivity.this, "Oops!", "Please check your entered email address & try again!","OK", CFAlertDialog.CFAlertStyle.ALERT);
                }
                else{
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
                        mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress("dinukashehan1999@gmail.com"));
                        mimeMessage.setSubject("Subject: Regarding Account Password Reset");
                        mimeMessage.setText("Hello Programmer, \n\nProgrammer World has sent you this 2nd email. " + txtEmail.getText().toString().trim() +" \n\nCheers!\nProgrammer World");

                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Transport.send(mimeMessage);
                                    customProgressDialog.dismissProgress();
                                    startActivity(new Intent(ForgotPasswordActivity.this, MailConfirmationActivity.class));
                                    Animatoo.animateSlideLeft(ForgotPasswordActivity.this);
                                } catch (MessagingException e) {
                                    customProgressDialog.dismissProgress();
                                    customAlertDialog.negativeAlert(ForgotPasswordActivity.this, "Oops!", "Something went wrong.\nPlease try again later!","OK", CFAlertDialog.CFAlertStyle.ALERT);
                                    e.printStackTrace();
                                }
                            }
                        });
                        thread.start();

                    } catch (AddressException e) {
                        customProgressDialog.dismissProgress();
                        customAlertDialog.negativeAlert(ForgotPasswordActivity.this, "Oops!", "Check Your Email Address & Please try again later!","OK", CFAlertDialog.CFAlertStyle.ALERT);
                        e.printStackTrace();
                    } catch (MessagingException e) {
                        customProgressDialog.dismissProgress();
                        customAlertDialog.negativeAlert(ForgotPasswordActivity.this, "Oops!", "Something went wrong.\nPlease try again later!","OK", CFAlertDialog.CFAlertStyle.ALERT);
                        e.printStackTrace();
                    }catch (Exception e){
                        customProgressDialog.dismissProgress();
                        customAlertDialog.negativeAlert(ForgotPasswordActivity.this, "Oops!", "Something went wrong.\nPlease try again later!","OK", CFAlertDialog.CFAlertStyle.ALERT);
                        e.printStackTrace();
                    }
                }
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
                Animatoo.animateSlideRight(ForgotPasswordActivity.this);
                finishAffinity();
            }
        });
    }
}