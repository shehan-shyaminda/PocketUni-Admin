package com.codelabs.pocketuni_admin.adapters;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.codelabs.pocketuni_admin.R;
import com.codelabs.pocketuni_admin.models.Announcement;

import java.util.ArrayList;

public class AnnouncementAdapter extends ArrayAdapter<Announcement> {
    ArrayList<Announcement> announcementsList;
    Activity mActivity;

    public AnnouncementAdapter(Activity activity, ArrayList<Announcement> announcementsList) {
        super(activity, R.layout.row_notice, announcementsList);
        this.mActivity = activity;
        this.announcementsList = announcementsList;
    }

    @Override
    public int getCount() {
        return announcementsList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = mActivity.getLayoutInflater();
        View row = inflater.inflate(R.layout.row_notice, null, true);

        TextView txtTitle = row.findViewById(R.id.txt_NoticeTitle);
        TextView txtBody = row.findViewById(R.id.txt_NoticeBody);
        TextView txtDate = row.findViewById(R.id.txt_NoticeDate);

        try {
            txtTitle.setText(announcementsList.get(position).getNotificationTitle());
            txtBody.setText(announcementsList.get(position).getNotificationDesc());
            txtDate.setText("~ " + announcementsList.get(position).getNotificationDate().replace("/","-") + " ~");
        } catch (Exception ex) {
            Log.e(TAG, "getView Error: " + ex.getLocalizedMessage());
        }

        return row;
    }
}