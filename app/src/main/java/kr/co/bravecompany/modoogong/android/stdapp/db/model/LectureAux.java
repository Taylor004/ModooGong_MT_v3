package kr.co.bravecompany.modoogong.android.stdapp.db.model;

import androidx.annotation.Nullable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import kr.co.bravecompany.api.android.stdapp.api.data.LectureItemVO;

public class LectureAux  { //extends RealmObject {
    @PrimaryKey
    private int lectureCode;
    public boolean newMark;
    public boolean favMark;

    public static LectureAux create(LectureItemVO lectureItem){
        LectureAux lecture = new LectureAux();
        lecture.lectureCode= lectureItem.getLectureCode();
        return lecture;
    }
}
