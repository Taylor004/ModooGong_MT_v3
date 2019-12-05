package kr.co.bravecompany.modoogong.android.stdapp.data;

/**
 * Created by BraveCompany on 2016. 10. 17..
 */

public class LectureMenuData {
    private String childName;
    private boolean isSelected = false;

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
