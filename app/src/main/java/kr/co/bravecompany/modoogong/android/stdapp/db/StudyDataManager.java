package kr.co.bravecompany.modoogong.android.stdapp.db;

import java.util.ArrayList;
import java.util.List;

import kr.co.bravecompany.api.android.stdapp.api.data.LectureItemVO;
import kr.co.bravecompany.api.android.stdapp.api.data.LectureResult;

public class StudyDataManager extends BaseDataManager {

    final String Tag = "StudyDataManager";

    private static StudyDataManager instance;

    public static StudyDataManager getInstance() {

        if (instance == null)
            instance = new StudyDataManager();

        return instance;
    }


    public boolean IsNewLecture(String lecCode)
    {
        return  false;
    }


    public ArrayList<LectureItemVO> mEndLectureList = new ArrayList<>();
    public ArrayList<LectureItemVO> mIngLectureList = new ArrayList<>();

    //OnGlobalDataListener mGlobalDataListener;

    public void storeLectureDataEndList(LectureResult result) {

        //studies가 null인 상태를 발견하여 studies == null상태일때 메소드를 그냥 리턴.
        if (result == null || result.getStudies() ==null) {
            return;
        }

        ArrayList<LectureItemVO> lectures = result.getStudies();
        mEndLectureList.clear();
        mEndLectureList.addAll(result.getStudies());
    }

    public void storeLectureDataIngList(LectureResult result) {

        if (result == null || result.getStudies() ==null) {
            return;
        }

        ArrayList<LectureItemVO> lectures = result.getStudies();
        mIngLectureList.clear();
        mIngLectureList.addAll(result.getStudies());
    }

    public void removedLectureDataIngList(LectureItemVO item)
    {
        mIngLectureList.remove(item);
    }

    public void storeSubjectDataList()
    {

    }

    public void storeTeacherDataList()
    {

    }

    public LectureItemVO querySubscribedLectureInfo(String lectureCode)
    {
        int iLecCode = Integer.parseInt(lectureCode);

        for (LectureItemVO lectureItemVO : mIngLectureList) {
            if(lectureItemVO.getLectureCode() ==    iLecCode )
            {
                return lectureItemVO;
            }
        }

        return null;
    }

    List<String> lectureCodeList = new ArrayList<>();
    public void storeTempLectureSubscribeList(List<String> codes)
    {
        lectureCodeList.clear();
        lectureCodeList.addAll(codes);
    }

    public boolean isNewSubscribeLecture(String cd)
    {
        return lectureCodeList.contains(cd);
    }

    public void clearTempLectureSubscribeList()
    {
        lectureCodeList.clear();
    }
}
