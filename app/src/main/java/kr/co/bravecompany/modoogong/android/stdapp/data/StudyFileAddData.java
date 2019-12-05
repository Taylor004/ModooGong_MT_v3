package kr.co.bravecompany.modoogong.android.stdapp.data;

/**
 * Created by BraveCompany on 2017. 10. 20..
 */

public class StudyFileAddData {
    private int no = 0;
    private String path;
    private String name;

    public StudyFileAddData(int no, String path, String name) {
        this.no = no;
        this.path = path;
        this.name = name;
    }

    public int getNo() {
        return no;
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }
}
