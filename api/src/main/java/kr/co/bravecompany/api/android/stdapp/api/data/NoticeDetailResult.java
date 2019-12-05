package kr.co.bravecompany.api.android.stdapp.api.data;

/**
 * Created by BraveCompany on 2016. 11. 17..
 */

public class NoticeDetailResult {
    private int noticeNo;
    private int type;
    private String title;
    private String content;
    private String writeDate;
    private String typeName;
    ////
    private String FilePath;
    private String FilePathName;

    public int getNoticeNo() {
        return noticeNo;
    }

    public int getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getWriteDate() {
        return writeDate;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getFilePath() {
        return FilePath;
    }

    public String getFilePathName() {
        return FilePathName;
    }
}
