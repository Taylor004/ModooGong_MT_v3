package kr.co.bravecompany.api.android.stdapp.api.data;

/**
 * Created by BraveCompany on 2016. 11. 11..
 */

public class NoticeItemVO {
    private String gb;
    private int noticeNo;
    private int type;
    private String title;
    private String writeDate;
    private String typeName;

    public String getGb() {
        return gb;
    }

    public int getNoticeNo() {
        return noticeNo;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public String getWriteDate() {
        return writeDate;
    }

    public String getTypeName() {
        return typeName;
    }
}
