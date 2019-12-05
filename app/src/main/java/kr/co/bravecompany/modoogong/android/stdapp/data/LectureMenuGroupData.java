package kr.co.bravecompany.modoogong.android.stdapp.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BraveCompany on 2016. 10. 17..
 */

public class LectureMenuGroupData {
    private int tag;
    private String groupName;
    private List<LectureMenuData> children = new ArrayList<LectureMenuData>();
    private int lectureCount = 0;

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<LectureMenuData> getChildren() {
        return children;
    }

    public void setChildren(List<LectureMenuData> children) {
        this.children = children;
    }

    public int getLectureCount() {
        return lectureCount;
    }

    public void setLectureCount(int lectureCount) {
        this.lectureCount = lectureCount;
    }
}
