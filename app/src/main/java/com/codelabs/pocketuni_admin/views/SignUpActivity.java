package com.codelabs.pocketuni_admin.views;

import androidx.annotation.Nullable;
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

public class SignUpActivity extends AppCompatActivity {

    private ImageView btnBack;
    private Button btnSend;
    private TextView btnLogin;
    private CustomEditText txtSignupSubject, txtSignupBody;
    private CustomProgressDialog customProgressDialog;
    private CustomAlertDialog customAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        btnBack = findViewById(R.id.ic_compose_email_back);
        btnSend = findViewById(R.id.btn_compose_email_send);
        btnLogin = findViewById(R.id.btn_compose_email_login);
        txtSignupSubject = findViewById(R.id.txt_SignupSubject);
        txtSignupBody = findViewById(R.id.txt_SignupBody);

        customProgressDialog = new CustomProgressDialog(SignUpActivity.this);
        customAlertDialog = new CustomAlertDialog();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                Animatoo.animateSlideRight(SignUpActivity.this);
                finishAffinity();
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customProgressDialog.createProgress();
                if (txtSignupSubject.getText().toString().length() == 0 || txtSignupBody.getText().toString().length() == 0) {
                    customProgressDialog.dismissProgress();
                    customAlertDialog.negativeAlert(SignUpActivity.this, "Oops!", "Please check your details & try again!", "OK", CFAlertDialog.CFAlertStyle.ALERT);
                } else {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("message/rfc822");
                    i.putExtra(Intent.EXTRA_EMAIL, new String[]{"dinukashehan1999@gmail.com"});
                    i.putExtra(Intent.EXTRA_SUBJECT, txtSignupSubject.getText().toString());
                    i.putExtra(Intent.EXTRA_TEXT, txtSignupBody.getText().toString());

                    try {
                        customProgressDialog.dismissProgress();
                        startActivityForResult(Intent.createChooser(i, "Choose a Mail Client :"), 404);
                    } catch (android.content.ActivityNotFoundException ex) {
                        customProgressDialog.dismissProgress();
                        customAlertDialog.negativeAlert(SignUpActivity.this, "Oops!", "Something went wrong.\nPlease try again later!", "OK", CFAlertDialog.CFAlertStyle.ALERT);
                    }
                }
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                Animatoo.animateSlideRight(SignUpActivity.this);
                finishAffinity();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 404) {
            startActivity(new Intent(SignUpActivity.this, MailConfirmationActivity.class));
            Animatoo.animateSlideLeft(SignUpActivity.this);
        }
    }
}