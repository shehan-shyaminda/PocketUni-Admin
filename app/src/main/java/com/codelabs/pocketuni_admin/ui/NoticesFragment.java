package com.codelabs.pocketuni_admin.ui;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.codelabs.pocketuni_admin.R;
import com.codelabs.pocketuni_admin.adapters.NoticesAdapter;
import com.codelabs.pocketuni_admin.models.Notice;
import com.codelabs.pocketuni_admin.services.SharedPreferencesManager;
import com.codelabs.pocketuni_admin.utils.CustomProgressDialog;
import com.codelabs.pocketuni_admin.views.AddBatchNoticeActivity;
import com.codelabs.pocketuni_admin.views.AddNewEventActivity;
import com.codelabs.pocketuni_admin.views.AddPublicNoticeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;

public class NoticesFragment extends Fragment {

    private ListView listView;
    private FirebaseFirestore db;
    private ArrayList<Notice> noticeList;
    private SharedPreferencesManager sharedPreferencesManager;
    private Calendar calendar;
    private CustomProgressDialog customProgressDialog;
    private TextView txtEmptyNotices;
    private Button btnFindNotices;
    private Spinner spnEventBatch, spnEventCourse, spnEventBatchType;
    private ConstraintLayout btnAddBatchNotice;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_notices, container, false);

        listView = view.findViewById(R.id.lst_batchNotices);
        txtEmptyNotices = view.findViewById(R.id.txt_emptyBNotices);
        spnEventBatch = view.findViewById(R.id.notice_spinnerBatch);
        spnEventCourse = view.findViewById(R.id.notice_spinnerCourse);
        spnEventBatchType = view.findViewById(R.id.notice_spinnerBatchType);
        btnFindNotices = view.findViewById(R.id.btnFindNotice);
        btnAddBatchNotice = view.findViewById(R.id.btnAddBatchNotice);

        db = FirebaseFirestore.getInstance();
        sharedPreferencesManager = new SharedPreferencesManager(getContext());
        calendar = Calendar.getInstance();
        customProgressDialog = new CustomProgressDialog(getContext());

        init();

        btnFindNotices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customProgressDialog.createProgress();
                listView.setAdapter(null);
                noticeList = new ArrayList<>();
                String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(calendar.getTime());
                String batchType = "F";
                if (spnEventBatchType.getSelectedItem().toString().equals("Full-Time")){
                    batchType = "F";
                }else{
                    batchType = "P";
                }

                db.collection("Notices")
                        .whereEqualTo("noticeDate", date)
                        .whereEqualTo("noticeCourse", spnEventCourse.getSelectedItem().toString())
                        .whereEqualTo("noticeBatch", spnEventBatch.getSelectedItem().toString())
                        .whereEqualTo("noticeBatchType", batchType)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.getResult().isEmpty()){
                                    Log.e(TAG, "onSuccess: Empty Collection");
                                    listView.setEnabled(false);
                                    listView.setVisibility(View.GONE);
                                    txtEmptyNotices.setEnabled(true);
                                    txtEmptyNotices.setVisibility(View.VISIBLE);
                                }
                                else{
                                    DocumentSnapshot snapsList;
                                    for(int i = 0; i < task.getResult().getDocuments().size(); i++){
                                        snapsList = task.getResult().getDocuments().get(i);
                                        noticeList.add(new Notice(snapsList.get("noticeBatch").toString(), snapsList.get("noticeBatchType").toString(), snapsList.get("noticeCourse").toString(),
                                        snapsList.get("noticeDate").toString(), snapsList.get("noticeDesc").toString(), snapsList.get("noticeTitle").toString()));
                                    }

                                    txtEmptyNotices.setEnabled(false);
                                    txtEmptyNotices.setVisibility(View.GONE);
                                    listView.setEnabled(true);
                                    listView.setVisibility(View.VISIBLE);

                                    if (getActivity()!=null){
                                        NoticesAdapter listAdapter = new NoticesAdapter(getActivity(), noticeList);
                                        listView.setAdapter(listAdapter);
                                    }
                                }
                                customProgressDialog.dismissProgress();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                    customProgressDialog.dismissProgress();
                                    Log.e(TAG, "onFailure: " + e);
                            }
                        });
                }
        });

        btnAddBatchNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), AddBatchNoticeActivity.class));
            }
        });

        return view;
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
                        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, batchArray);
                        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spnEventBatch.setAdapter(spinnerArrayAdapter);
                    } else {
                        customProgressDialog.dismissProgress();
                        Log.d(TAG, task.getException().getMessage());
                    }
                }
            });

        db.collection("Courses").get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        ArrayList<String> courseArray = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()){
                            if (document != null) {
                                String course = document.get("courseName").toString();
                                courseArray.add(course);
                            }
                        }
                        Collections.sort(courseArray);
                        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, courseArray);
                        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spnEventCourse.setAdapter(spinnerArrayAdapter);
                        customProgressDialog.dismissProgress();
                    } else {
                        customProgressDialog.dismissProgress();
                        Log.d(TAG, task.getException().getMessage());
                    }
                }
            });

    }
}