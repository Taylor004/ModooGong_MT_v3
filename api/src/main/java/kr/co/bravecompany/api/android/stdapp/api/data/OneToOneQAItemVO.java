package kr.co.bravecompany.api.android.stdapp.api.data;

/**
 * Created by BraveCompany on 2016. 11. 11..
 */

public class OneToOneQAItemVO implements QAItemVO {
    private int qnaNo;
    private String title;
    private String writeDate;
    private boolean hasReply;
    private String type;
    private String typeName;

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

    public String getType() {
        return type;
    }

    public String getTypeName() {
        return typeName;
    }
}
