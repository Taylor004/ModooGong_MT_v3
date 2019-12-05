package kr.co.bravecompany.api.android.stdapp.api.data;

/**
 * Created by BraveCompany on 2016. 11. 11..
 */

public class StudyQADetailVO implements QADetailVO {
    private int inquiryNo;
    private String title;
    private String writeDate;
    private boolean hasReply;
    private String content;
    private String filePathName;
    private String filePath;
    private String mp3Path;
    private String categoryName;

    //edit
    private String category;
    private String cate;
    private String lectureNo;
    private String lectureName;
    private String tchCd;

    //Reply
    private String replyTitle;
    private String replyContent;
    private String replyFilePathName;
    private String replyFilePath;
    private String replyMp3Path;

    //isEditer
    private int isEditer = 1;

    //book
    private String bookNo;
    private String bookPage;
    private String bookName;


    public int getInquiryNo() {
        return inquiryNo;
    }

    public String getTitle() {
        return title;
    }

    public String getWriteDate() {
        return writeDate;
    }

    public boolean isHasReply() {
        return hasReply;
    }

    public String getContent() {
        return content;
    }

    public String getFilePathName() {
        return filePathName;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getMp3Path() {
        return mp3Path;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getCategory() {
        return category;
    }

    public String getCate() {
        return cate;
    }

    public String getLectureNo() {
        return lectureNo;
    }

    public String getLectureName() {
        return lectureName;
    }

    public String getTchCd() {
        return tchCd;
    }

    public String getReplyTitle() {
        return replyTitle;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public String getReplyFilePathName() {
        return replyFilePathName;
    }

    public String getReplyFilePath() {
        return replyFilePath;
    }

    public String getReplyMp3Path() {
        return replyMp3Path;
    }

    public int isEditer() {
        return isEditer;
    }

    public String getBookNo() {
        return bookNo;
    }

    public String getBookPage() {
        return bookPage;
    }

    public String getBookName() {
        return bookName;
    }
}
