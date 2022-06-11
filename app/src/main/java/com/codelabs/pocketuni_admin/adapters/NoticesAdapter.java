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
import com.codelabs.pocketuni_admin.models.Notice;

import java.util.ArrayList;

public class NoticesAdapter extends ArrayAdapter<Notice> {
    ArrayList<Notice> noticesList;
    Activity mActivity;

    public NoticesAdapter(Activity activity, ArrayList<Notice> noticesList) {
        super(activity, R.layout.row_notice, noticesList);
        this.mActivity = activity;
        this.noticesList = noticesList;
    }

    @Override
    public int getCount() {
        return noticesList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = mActivity.getLayoutInflater();
        View row = inflater.inflate(R.layout.row_notice, null, true);

        TextView txtTitle = row.findViewById(R.id.txt_NoticeTitle);
        TextView txtBody = row.findViewById(R.id.txt_NoticeBody);
        TextView txtDate = row.findViewById(R.id.txt_NoticeDate);

        try {
            txtTitle.setText(noticesList.get(position).getNoticeTitle());
            txtBody.setText(noticesList.get(position).getNoticeDesc());
            txtDate.setText("~ " + noticesList.get(position).getNoticeDate().replace("/","-") + " ~");
        } catch (Exception ex) {
            Log.e(TAG, "getView Error: " + ex.getLocalizedMessage());
        }

        return row;
    }
}
