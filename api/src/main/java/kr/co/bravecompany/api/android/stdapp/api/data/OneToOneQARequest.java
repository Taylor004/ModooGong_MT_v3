package kr.co.bravecompany.api.android.stdapp.api.data;

import java.io.File;

/**
 * Created by BraveCompany on 2016. 11. 16..
 */

public class OneToOneQARequest {
    private int qnaNo;
    private String title;
    private String content;
    private String category;
    private File file;
    private String fileFormName;

    public int getQnaNo() {
        return qnaNo;
    }

    public void setQnaNo(int qnaNo) {
        this.qnaNo = qnaNo;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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
}
