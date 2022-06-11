package com.codelabs.pocketuni_admin.views;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.codelabs.pocketuni_admin.R;
import com.codelabs.pocketuni_admin.models.Admin;
import com.codelabs.pocketuni_admin.services.SharedPreferencesManager;
import com.codelabs.pocketuni_admin.utils.CustomAlertDialog;
import com.codelabs.pocketuni_admin.utils.CustomProgressDialog;
import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.libizo.CustomEditText;

public class LoginActivity extends AppCompatActivity {

    private TextView btnForgotPassword, btnReqLogin;
    private Button btnLogin;
    private CustomEditText txtUsername, txtPassword;
    private CustomProgressDialog customProgressDialog;
    private FirebaseFirestore db;
    private SharedPreferencesManager sharedPreferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        customProgressDialog = new CustomProgressDialog(LoginActivity.this);
        db = FirebaseFirestore.getInstance();
        sharedPreferencesManager = new SharedPreferencesManager(LoginActivity.this);

        btnForgotPassword = findViewById(R.id.btn_loginForgotPassword);
        btnLogin = findViewById(R.id.btn_login);
        btnReqLogin = findViewById(R.id.btn_loginSignUp);
        txtUsername = findViewById(R.id.txt_loginEmail);
        txtPassword = findViewById(R.id.txt_loginPassword);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {  customProgressDialog.createTextProgress("Please wait");
                if (txtUsername.getText().toString().length() == 0 || txtPassword.getText().toString().length() == 0) {
                    customProgressDialog.dismissProgress();
                    new CustomAlertDialog().negativeAlert(LoginActivity.this, "Something Went Wrong!!", "Username & Password fields are required.", "OK", CFAlertDialog.CFAlertStyle.ALERT);
                    if (txtUsername.getText().toString().length() == 0) {
                        txtUsername.setError("Username Cannot be Empty!");
                    }
                    if (txtPassword.getText().toString().length() == 0) {
                        txtPassword.setError("Password Cannot be Empty!");
                    }
                } else {
                    db.collection("Admin").document(txtUsername.getText().toString().toUpperCase()).get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if (documentSnapshot.exists()){
                                        if (documentSnapshot.get("userPassword").equals(txtPassword.getText().toString())){
                                            customProgressDialog.dismissProgress();

                                            sharedPreferencesManager.savePreferences(SharedPreferencesManager.USER_LOGGED_IN, true);
                                            if (documentSnapshot.get("userType").equals("S")){
                                                sharedPreferencesManager.savePreferences(SharedPreferencesManager.PREF_IS_SUPER_ADMIN, true);
                                            }else{
                                                sharedPreferencesManager.savePreferences(SharedPreferencesManager.PREF_IS_SUPER_ADMIN, false);
                                            }
                                            sharedPreferencesManager.savePreferences(SharedPreferencesManager.USER_ID, txtUsername.getText().toString().toUpperCase());
                                            sharedPreferencesManager.saveUserDataPreferences(SharedPreferencesManager.USER_DETAILS, new Admin(documentSnapshot.get("userEmail").toString(),documentSnapshot.get("userName").toString(),documentSnapshot.get("userPassword").toString(),documentSnapshot.get("userType").toString()));

                                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                            finishAffinity();
                                        }else{
                                            customProgressDialog.dismissProgress();
                                            new CustomAlertDialog().negativeAlert(LoginActivity.this, "Something Went Wrong!!", "Please check your Login Credentials.","OK", CFAlertDialog.CFAlertStyle.ALERT);
                                        }
                                    }else{
                                        customProgressDialog.dismissProgress();
                                        new CustomAlertDialog().negativeAlert(LoginActivity.this, "Something Went Wrong!!", "Please check your Login Credentials.","OK", CFAlertDialog.CFAlertStyle.ALERT);
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    customProgressDialog.dismissProgress();
                                    Log.e(TAG, "onFailure: " + e.getLocalizedMessage() );
                                    new CustomAlertDialog().negativeAlert(LoginActivity.this, "Something Went Wrong!!", "Please check your Internet Connection.","OK", CFAlertDialog.CFAlertStyle.ALERT);
                                }
                            });
                }
            }
        });

        btnReqLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });

        btnForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            }
        });

        txtUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                txtUsername.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (txtUsername.getText().toString().length() == 0){
                    txtUsername.setError("Username Cannot be Empty!");
                }
            }
        });

        txtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                txtPassword.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (txtPassword.getText().toString().length() == 0){
                    txtPassword.setError("Password Cannot be Empty!");
                }
            }
        });
    }
}