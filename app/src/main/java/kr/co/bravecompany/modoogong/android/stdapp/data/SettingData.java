package kr.co.bravecompany.modoogong.android.stdapp.data;

/**
 * Created by BraveCompany on 2016. 10. 26..
 */

public class SettingData {
    private int type;
    private String title;
    private String content;
    private boolean isChecked = true;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
