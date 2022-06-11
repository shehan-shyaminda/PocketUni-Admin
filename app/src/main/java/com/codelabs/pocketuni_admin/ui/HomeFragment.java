package com.codelabs.pocketuni_admin.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codelabs.pocketuni_admin.views.AddNewBatchActivity;
import com.codelabs.pocketuni_admin.views.AddNewCourseActivity;
import com.codelabs.pocketuni_admin.views.AddNewEventActivity;
import com.codelabs.pocketuni_admin.views.AddNewHallActivity;
import com.codelabs.pocketuni_admin.views.AddNewLecturerActivity;
import com.codelabs.pocketuni_admin.views.AddNewSubjectActivity;
import com.codelabs.pocketuni_admin.R;
import com.codelabs.pocketuni_admin.services.SharedPreferencesManager;

public class HomeFragment extends Fragment {

    private ConstraintLayout btnNewEvents, btnNewBatch, btnNewSubject, btnNewLecturer, btnNewHall, btnNewCourse;
    private TextView txtWelcomeName;
    private SharedPreferencesManager sharedPreferencesManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        btnNewEvents = view.findViewById(R.id.btn_homeCalenderEvents);
        btnNewBatch = view.findViewById(R.id.btn_homeNewBatch);
        btnNewSubject = view.findViewById(R.id.btn_homeNewSubject);
        btnNewLecturer = view.findViewById(R.id.btn_homeNewLecturer);
        btnNewHall = view.findViewById(R.id.btn_homeNewHall);
        btnNewCourse = view.findViewById(R.id.btn_homeNewCourse);
        txtWelcomeName = view.findViewById(R.id.txt_welcome_name);

        sharedPreferencesManager = new SharedPreferencesManager(getContext());

        init();

        btnNewEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AddNewEventActivity.class));
            }
        });

        btnNewBatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AddNewBatchActivity.class));
            }
        });

        btnNewSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AddNewSubjectActivity.class));
            }
        });

        btnNewLecturer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AddNewLecturerActivity.class));
            }
        });

        btnNewHall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AddNewHallActivity.class));
            }
        });

        btnNewCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AddNewCourseActivity.class));
            }
        });

        return view;
    }

    private void init() {
        if (sharedPreferencesManager.getUserDataPreferences(SharedPreferencesManager.USER_DETAILS).getUserName().isEmpty()){
            txtWelcomeName.setText(sharedPreferencesManager.getUserDataPreferences(SharedPreferencesManager.USER_DETAILS).getUserEmail().split("@")[0].toUpperCase());
        }else{
            txtWelcomeName.setText(sharedPreferencesManager.getUserDataPreferences(SharedPreferencesManager.USER_DETAILS).getUserName());
        }
    }
}