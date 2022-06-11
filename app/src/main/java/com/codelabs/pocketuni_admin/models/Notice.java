package com.codelabs.pocketuni_admin.models;

public class Notice {
    String noticeBatch, noticeBatchType, noticeCourse, noticeDate, noticeDesc, noticeTitle;

    public Notice(String noticeBatch, String noticeBatchType, String noticeCourse, String noticeDate, String noticeDesc, String noticeTitle) {
        this.noticeBatch = noticeBatch;
        this.noticeBatchType = noticeBatchType;
        this.noticeCourse = noticeCourse;
        this.noticeDate = noticeDate;
        this.noticeDesc = noticeDesc;
        this.noticeTitle = noticeTitle;
    }

    public String getNoticeBatch() {
        return noticeBatch;
    }

    public void setNoticeBatch(String noticeBatch) {
        this.noticeBatch = noticeBatch;
    }

    public String getNoticeBatchType() {
        return noticeBatchType;
    }

    public void setNoticeBatchType(String noticeBatchType) {
        this.noticeBatchType = noticeBatchType;
    }

    public String getNoticeCourse() {
        return noticeCourse;
    }

    public void setNoticeCourse(String noticeCourse) {
        this.noticeCourse = noticeCourse;
    }

    public String getNoticeDate() {
        return noticeDate;
    }

    public void setNoticeDate(String noticeDate) {
        this.noticeDate = noticeDate;
    }

    public String getNoticeDesc() {
        return noticeDesc;
    }

    public void setNoticeDesc(String noticeDesc) {
        this.noticeDesc = noticeDesc;
    }

    public String getNoticeTitle() {
        return noticeTitle;
    }

    public void setNoticeTitle(String noticeTitle) {
        this.noticeTitle = noticeTitle;
    }
}
