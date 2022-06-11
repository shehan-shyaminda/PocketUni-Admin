package com.codelabs.pocketuni_admin.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.codelabs.pocketuni_admin.views.LoginActivity;
import com.codelabs.pocketuni_admin.R;
import com.codelabs.pocketuni_admin.services.SharedPreferencesManager;
import com.codelabs.pocketuni_admin.utils.CustomAlertDialog;
import com.codelabs.pocketuni_admin.utils.CustomProgressDialog;
import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment {

    private ImageView btnProfile, btnEditProfile;
    private EditText txtUserName, txtUserCPassword, txtUserNPassword;
    private Button btnLogout, btnUpdate;
    private TextView txtAdminEmail, txtAdminName;
    private ConstraintLayout layoutProfile, layoutEditProfile;
    private SharedPreferencesManager sharedPreferencesManager;
    private CustomProgressDialog customProgressDialog;
    private CustomAlertDialog customAlertDialog;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        btnProfile = view.findViewById(R.id.btn_profile);
        btnUpdate = view.findViewById(R.id.btn_update);
        btnEditProfile = view.findViewById(R.id.btn_editProfile);
        layoutProfile = view.findViewById(R.id.layout_profile);
        txtUserName = view.findViewById(R.id.txt_profileUpdate_name);
        txtUserCPassword = view.findViewById(R.id.txt_profileUpdate_CP);
        txtUserNPassword = view.findViewById(R.id.txt_profileUpdate_NP);
        layoutEditProfile = view.findViewById(R.id.layout_editProfile);
        txtAdminEmail = view.findViewById(R.id.txt_profileEmail);
        txtAdminName = view.findViewById(R.id.txt_profileName);
        btnLogout = view.findViewById(R.id.btn_logout);

        db = FirebaseFirestore.getInstance();
        sharedPreferencesManager = new SharedPreferencesManager(getContext());
        customProgressDialog = new CustomProgressDialog(getContext());
        customAlertDialog = new CustomAlertDialog();

        init();

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutProfile.setVisibility(View.GONE);
                layoutProfile.setEnabled(false);
                layoutEditProfile.setVisibility(View.VISIBLE);
                layoutEditProfile.setEnabled(true);
            }
        });

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutEditProfile.setVisibility(View.GONE);
                layoutEditProfile.setEnabled(false);
                layoutProfile.setVisibility(View.VISIBLE);
                layoutProfile.setEnabled(true);
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customProgressDialog.createProgress();
                if (txtUserName.getText().toString().trim().length() == 0 && txtUserCPassword.getText().toString().trim().length() == 0 && txtUserNPassword.getText().toString().trim().length() == 0) {
                    customProgressDialog.dismissProgress();
                    customAlertDialog.negativeAlert(getContext(), "Oops!", "Check your information & Please try again later!", "OK", CFAlertDialog.CFAlertStyle.ALERT);
                } else if (!(txtUserCPassword.getText().toString().trim().length() == 0) && txtUserNPassword.getText().toString().trim().length() == 0) {
                    customProgressDialog.dismissProgress();
                    customAlertDialog.negativeAlert(getContext(), "Oops!", "Check your New Password & Please try again!", "OK", CFAlertDialog.CFAlertStyle.ALERT);
                } else if (txtUserCPassword.getText().toString().trim().length() == 0 && !(txtUserNPassword.getText().toString().trim().length() == 0)) {
                    customProgressDialog.dismissProgress();
                    customAlertDialog.negativeAlert(getContext(), "Oops!", "Check your Current Password & Please try again!", "OK", CFAlertDialog.CFAlertStyle.ALERT);
                }
//                else if(txtUserName.getText().toString().trim().length() == 0 && (!(txtUserCPassword.getText().toString().trim().length() == 0) & txtUserNPassword.getText().toString().trim().length() == 0)){
//                    customProgressDialog.dismissProgress();
//                    customAlertDialog.negativeAlert(getContext(), "Oops!", "Check your New Password & Please try again!","OK", CFAlertDialog.CFAlertStyle.ALERT);
//                }
//                else if(txtUserName.getText().toString().trim().length() == 0 && (txtUserCPassword.getText().toString().trim().length() == 0 & !(txtUserNPassword.getText().toString().trim().length() == 0))){
//                    customProgressDialog.dismissProgress();
//                    customAlertDialog.negativeAlert(getContext(), "Oops!", "Check your Current Password & Please try again!","OK", CFAlertDialog.CFAlertStyle.ALERT);
//                }
//                else if (!(txtUserCPassword.getText().toString().trim().length() == 0) && !(txtUserNPassword.getText().toString().trim().length() == 0) && !txtUserCPassword.getText().toString().equals(txtUserNPassword.getText().toString())){
//                    customProgressDialog.dismissProgress();
//                    customAlertDialog.negativeAlert(getContext(), "Oops!", "Check your passwords & Please try again!","OK", CFAlertDialog.CFAlertStyle.ALERT);
//                }
                else {
                    db.collection("Admin").document(sharedPreferencesManager.getPreferences(SharedPreferencesManager.USER_ID))
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.getResult().exists()) {
                                        if (!(txtUserCPassword.getText().toString().trim().length() == 0) && !(txtUserName.getText().toString().trim().length() == 0)) {
                                            if (task.getResult().get("userPassword").equals(txtUserCPassword.getText().toString())) {
                                                db.collection("Admin").document(sharedPreferencesManager.getPreferences(SharedPreferencesManager.USER_ID))
                                                        .update(updateUserData(txtUserName.getText().toString(), txtUserNPassword.getText().toString()))
                                                        .addOnCompleteListener(new OnCompleteListener() {
                                                            @Override
                                                            public void onComplete(@NonNull Task task) {
                                                                customProgressDialog.dismissProgress();
                                                                customAlertDialog.positiveAlert(getContext(), "Awesome!", "Your details has been changed!", "OK", CFAlertDialog.CFAlertStyle.ALERT);
                                                                clearEditText();
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                customProgressDialog.dismissProgress();
                                                                customAlertDialog.negativeAlert(getContext(), "Oops!", "Something went wrong.\nPlease try again later!", "OK", CFAlertDialog.CFAlertStyle.ALERT);
                                                            }
                                                        });
                                            } else {
                                                customProgressDialog.dismissProgress();
                                                customAlertDialog.negativeAlert(getContext(), "Oops!", "Something went wrong.\nCheck your current password and try again!", "OK", CFAlertDialog.CFAlertStyle.ALERT);
                                            }
                                        } else if (!(txtUserCPassword.getText().toString().trim().length() == 0) && txtUserName.getText().toString().trim().length() == 0) {
                                            if (task.getResult().get("userPassword").equals(txtUserCPassword.getText().toString())) {
                                                db.collection("Admin").document(sharedPreferencesManager.getPreferences(SharedPreferencesManager.USER_ID))
                                                        .update(updateUserData(txtUserName.getText().toString(), txtUserNPassword.getText().toString()))
                                                        .addOnCompleteListener(new OnCompleteListener() {
                                                            @Override
                                                            public void onComplete(@NonNull Task task) {
                                                                customProgressDialog.dismissProgress();
                                                                customAlertDialog.positiveAlert(getContext(), "Awesome!", "Your details has been changed!", "OK", CFAlertDialog.CFAlertStyle.ALERT);
                                                                clearEditText();
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                customProgressDialog.dismissProgress();
                                                                customAlertDialog.negativeAlert(getContext(), "Oops!", "Something went wrong.\nPlease try again later!", "OK", CFAlertDialog.CFAlertStyle.ALERT);
                                                            }
                                                        });
                                            } else {
                                                customProgressDialog.dismissProgress();
                                                customAlertDialog.negativeAlert(getContext(), "Oops!", "Something went wrong.\nCheck your Current Password and try!", "OK", CFAlertDialog.CFAlertStyle.ALERT);
                                            }
                                        } else if (txtUserCPassword.getText().toString().trim().length() == 0 && !(txtUserName.getText().toString().trim().length() == 0)) {
                                            db.collection("Admin").document(sharedPreferencesManager.getPreferences(SharedPreferencesManager.USER_ID))
                                                    .update(updateUserData(txtUserName.getText().toString(), txtUserNPassword.getText().toString()))
                                                    .addOnCompleteListener(new OnCompleteListener() {
                                                        @Override
                                                        public void onComplete(@NonNull Task task) {
                                                            customProgressDialog.dismissProgress();
                                                            customAlertDialog.positiveAlert(getContext(), "Awesome!", "Your details has been changed!", "OK", CFAlertDialog.CFAlertStyle.ALERT);
                                                            clearEditText();
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            customProgressDialog.dismissProgress();
                                                            customAlertDialog.negativeAlert(getContext(), "Oops!", "Something went wrong.\nPlease try again later!", "OK", CFAlertDialog.CFAlertStyle.ALERT);
                                                        }
                                                    });
                                        }
                                    } else {
                                        customProgressDialog.dismissProgress();
                                        customAlertDialog.negativeAlert(getContext(), "Oops!", "Something went wrong.\nPlease try again later!", "OK", CFAlertDialog.CFAlertStyle.ALERT);
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    customProgressDialog.dismissProgress();
                                    customAlertDialog.negativeAlert(getContext(), "Oops!", "Something went wrong.\nPlease try again later!", "OK", CFAlertDialog.CFAlertStyle.ALERT);
                                }
                            });
                }
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customProgressDialog.createProgress();

                sharedPreferencesManager.clearPreferences(SharedPreferencesManager.USER_ID);
                sharedPreferencesManager.clearPreferences(SharedPreferencesManager.USER_DETAILS);
                sharedPreferencesManager.savePreferences(SharedPreferencesManager.USER_LOGGED_IN, false);

                customProgressDialog.dismissProgress();

                startActivity(new Intent(getContext(), LoginActivity.class));
                Animatoo.animateSlideRight(getContext());
                getActivity().finishAffinity();
            }
        });

        return view;
    }

    private Map updateUserData(String userName, String userPassword) {
        Map<String, Object> userData = new HashMap<>();
        if (!userName.isEmpty() && userPassword.isEmpty()) {
            userData.put("userName", userName);
        } else if (userName.isEmpty() && !userPassword.isEmpty()) {
            userData.put("userPassword", userPassword);
        } else {
            userData.put("userName", userName);
            userData.put("userPassword", userPassword);
        }

        return userData;
    }

    private void clearEditText() {
        txtUserName.getText().clear();
        txtUserCPassword.getText().clear();
        txtUserNPassword.getText().clear();
        clearErrors();
    }

    private void clearErrors() {
        txtUserName.setError(null);
        txtUserCPassword.setError(null);
        txtUserNPassword.setError(null);
    }

    private void init() {
        layoutEditProfile.setVisibility(View.GONE);
        layoutEditProfile.setEnabled(false);
        layoutProfile.setVisibility(View.VISIBLE);
        layoutProfile.setEnabled(true);

        txtAdminEmail.setText(sharedPreferencesManager.getUserDataPreferences(SharedPreferencesManager.USER_DETAILS).getUserEmail());
        if (!sharedPreferencesManager.getUserDataPreferences(SharedPreferencesManager.USER_DETAILS).getUserName().isEmpty()) {
            txtAdminName.setText(sharedPreferencesManager.getUserDataPreferences(SharedPreferencesManager.USER_DETAILS).getUserName());
        } else {
            txtAdminName.setText(sharedPreferencesManager.getUserDataPreferences(SharedPreferencesManager.USER_DETAILS).getUserEmail().split("@")[0].toUpperCase());
//            txtStudentName.setText(sharedPreferencesManager.getStudentDataPreferences(SharedPreferencesManager.USER_DETAILS).getStudentName());
        }
    }
}