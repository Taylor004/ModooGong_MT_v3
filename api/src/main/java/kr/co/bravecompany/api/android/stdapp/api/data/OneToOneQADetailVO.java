package kr.co.bravecompany.api.android.stdapp.api.data;

/**
 * Created by BraveCompany on 2016. 11. 11..
 */

public class OneToOneQADetailVO implements QADetailVO {
    private int qnaNo;
    private String title;
    private String writeDate;
    private boolean hasReply;
    private String content;
    private String filePathName;
    private String filePath;
    private String typeName;

    //edit
    private String c_code;

    //Reply
    private String replyTitle;
    private String replyContent;
    private String replyFilePathName;
    private String replyFilePath;

    //isEditer
    private int isEditer = 1;

    public int getQnaNo() {
        return qnaNo;
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

    public String getTypeName() {
        return typeName;
    }

    public String getC_code() {
        return c_code;
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

    public int isEditer() {
        return isEditer;
    }
}
