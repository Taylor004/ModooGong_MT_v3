package kr.co.bravecompany.api.android.stdapp.api.data;

import java.io.File;

/**
 * Created by BraveCompany on 2016. 11. 14..
 */

public class StudyQARequest {
    private int inquiryNo;
    private String lectureNo;
    private String title;
    private String content;
    private String cate;
    private String category;
    private String tchCd;
    private File file;
    private String fileFormName;
    private String mp3FormName;
    private String bookNo;
    private String bookPage;

    public int getInquiryNo() {
        return inquiryNo;
    }

    public void setInquiryNo(int inquiryNo) {
        this.inquiryNo = inquiryNo;
    }

    public String getLectureNo() {
        return lectureNo;
    }

    public void setLectureNo(String lectureNo) {
        this.lectureNo = lectureNo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCate() {
        return cate;
    }

    public void setCate(String cate) {
        this.cate = cate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTchCd() {
        return tchCd;
    }

    public void setTchCd(String tchCd) {
        this.tchCd = tchCd;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getFileFormName() {
        return fileFormName;
    }

    public void setFileFormName(String fileFormName) {
        this.fileFormName = fileFormName;
    }

    public String getMp3FormName() {
        return mp3FormName;
    }

    public void setMp3FormName(String mp3FormName) {
        this.mp3FormName = mp3FormName;
    }

    public String getBookNo() {
        return bookNo;
    }

    public void setBookNo(String bookNo) {
        this.bookNo = bookNo;
    }

    public String getBookPage() {
        return bookPage;
    }

    public void setBookPage(String bookPage) {
        this.bookPage = bookPage;
    }
}
