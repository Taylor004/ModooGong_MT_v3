package kr.co.bravecompany.modoogong.android.stdapp.db;

import android.content.Context;

import java.util.ArrayList;
import java.util.Date;

import io.realm.DynamicRealm;
import io.realm.FieldAttribute;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.RealmObjectSchema;
import io.realm.RealmResults;
import io.realm.RealmSchema;
import io.realm.Sort;
import kr.co.bravecompany.modoogong.android.stdapp.config.Tags;
import kr.co.bravecompany.modoogong.android.stdapp.db.model.Lecture;
import kr.co.bravecompany.modoogong.android.stdapp.db.model.LectureAux;
import kr.co.bravecompany.modoogong.android.stdapp.db.model.Study;
import kr.co.bravecompany.api.android.stdapp.api.data.LectureItemVO;
import kr.co.bravecompany.modoogong.android.stdapp.data.StudyData;

/**
 * Created by BraveCompany on 2016. 12. 5..
 */

public class DataManager {
    private static DataManager instance;

    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    public interface OnDataChangeListener {
        void onDataChangeProgress(int progress);
        void onDataChangeComplete();
        void onDataChangeError();
    }

    public Lecture createLectureModel(LectureItemVO lectureItem){
        Lecture lecture = getLectureModel(lectureItem.getStudyLectureNo());
        if(lecture == null){
            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            lecture = Lecture.create(lectureItem);
            realm.copyToRealmOrUpdate(lecture);
            realm.commitTransaction();
        }
        return lecture;
    }

    public Study createStudyModel(StudyData studyData, int studyLectureNo){
        Study study = getStudyModel(studyData.getStudyKey());
        if(study == null){
            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            study = Study.create(studyData, studyLectureNo);
            realm.copyToRealmOrUpdate(study);
            realm.commitTransaction();
        }
        return study;
    }

    public void createLectureStudyModels(LectureItemVO lectureItem, ArrayList<StudyData> studies, OnDataChangeListener listener){
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        int studyLectureNo = lectureItem.getStudyLectureNo();
        Lecture lecture = getLectureModel(studyLectureNo);
        if(lecture == null){
            lecture = Lecture.create(lectureItem);
            realm.insertOrUpdate(lecture);
        }
        for(StudyData studyData : studies){
            Study study = getStudyModel(studyData.getStudyKey());
            if(study == null) {
                study = Study.create(studyData, studyLectureNo);
                realm.insertOrUpdate(study);
            }
        }
        realm.commitTransaction();
        if(listener != null) {
            listener.onDataChangeComplete();
        }
    }

    public Lecture getLectureModel(int studyLectureNo){
        Realm realm = Realm.getDefaultInstance();
        Lecture lecture = realm.where(Lecture.class).equalTo("studyLectureNo", studyLectureNo).findFirst();
        return lecture;
    }

    public Study getStudyModel(String studyKey){
        Realm realm = Realm.getDefaultInstance();
        Study study = realm.where(Study.class).equalTo("studyKey", studyKey).findFirst();
        return study;
    }

    public Study getStudyModelContent(String mediaContentKey){
        Realm realm = Realm.getDefaultInstance();
        Study study = realm.where(Study.class).equalTo("mediaContentKey", mediaContentKey).findFirst();
        return study;
    }

    public Study getStudyModelVodKey(String studyVodKey){
        Realm realm = Realm.getDefaultInstance();
        Study study = realm.where(Study.class).equalTo("studyVodKey", studyVodKey).findFirst();
        return study;
    }

    public String getStudyKey(String mediaContentKey){
        Realm realm = Realm.getDefaultInstance();
        Study study = realm.where(Study.class).equalTo("mediaContentKey", mediaContentKey).findFirst();
        String key = null;
        if(study != null){
            key = study.getStudyKey();
        }
        return key;
    }

    public void updateDownState(String studyKey, int state, int errorCode){
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        Study study = getStudyModel(studyKey);
        if(study != null) {
            study.setState(state);
            study.setErrorCode(errorCode);
        }
        realm.commitTransaction();
    }

    public void updateDownStateContent(String mediaContentKey, int state, int errorCode){
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        Study study = getStudyModelContent(mediaContentKey);
        if(study != null) {
            study.setState(state);
            study.setErrorCode(errorCode);
        }
        realm.commitTransaction();
    }

    public void updateDownStateContents(ArrayList<String> mediaContentKeys, int state, int errorCode, OnDataChangeListener listener){
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        for(String mediaContentKey : mediaContentKeys){
            Study study = getStudyModelContent(mediaContentKey);
            if(study != null) {
                study.setState(state);
                study.setErrorCode(errorCode);
            }
        }
        realm.commitTransaction();
        if(listener != null) {
            listener.onDataChangeComplete();
        }
    }

    public void updateMediaContentKey(String studyKey, String mediaContentKey){
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        Study study = getStudyModel(studyKey);
        if(study != null) {
            study.setMediaContentKey(mediaContentKey);
        }
        realm.commitTransaction();
    }

    public void updateVodInfo(String studyKey, String vodInfo){
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        Study study = getStudyModel(studyKey);
        if(study != null) {
            study.setVodInfo(vodInfo);
        }
        realm.commitTransaction();
    }

    public void updateStudyEndDay(int studyLectureNo, String studyEndDay){
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        Lecture lecture = getLectureModel(studyLectureNo);
        if(lecture != null) {
            lecture.setStudyEndDay(studyEndDay);
        }
        realm.commitTransaction();
    }

    public void updateStudyState(int studyLectureNo, String studyState){
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        Lecture lecture = getLectureModel(studyLectureNo);
        if(lecture != null) {
            lecture.setStudyState(studyState);
        }
        realm.commitTransaction();
    }

    public ArrayList<Study> getDownloadingStudyList(){
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Study> studies = realm.where(Study.class).notEqualTo("state",
                Tags.STUDY_DOWN_STATE_TYPE.STUDY_DOWN_COMPLETE).findAll();
        ArrayList<Study> results = new ArrayList<Study>();
        if(studies!=null){
            results.addAll(studies);
        }
        return results;
    }

    public ArrayList<Study> getDownloadAllPauseStudyList(){
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Study> studies = realm.where(Study.class).equalTo("state",
                Tags.STUDY_DOWN_STATE_TYPE.STUDY_DOWN_PAUSE)
                .or()
                .equalTo("state", Tags.STUDY_DOWN_STATE_TYPE.STUDY_DOWN_API_ERROR)
                .or()
                .equalTo("state", Tags.STUDY_DOWN_STATE_TYPE.STUDY_DOWN_ERROR)
                .findAll();
        ArrayList<Study> results = new ArrayList<Study>();
        if(studies!=null){
            results.addAll(studies);
        }
        return results;
    }

    public ArrayList<Study> getDownloadCompleteStudyList(int studyLectureNo){
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Study> studies = realm.where(Study.class).equalTo("studyLectureNo", studyLectureNo)
                .equalTo("state", Tags.STUDY_DOWN_STATE_TYPE.STUDY_DOWN_COMPLETE).findAll();
        studies = studies.sort("studyOrder", Sort.ASCENDING, "studySubOrder", Sort.ASCENDING);
        ArrayList<Study> results = new ArrayList<Study>();
        if(studies!=null){
            results.addAll(studies);
        }
        return results;
    }

    public ArrayList<Study> getStudyList(int studyLectureNo){
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Study> studies = realm.where(Study.class).equalTo("studyLectureNo", studyLectureNo).findAll();
        ArrayList<Study> results = new ArrayList<Study>();
        if(studies!=null){
            results.addAll(studies);
        }
        return results;
    }

    public ArrayList<Lecture> getLectureList(){
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Lecture> lectures = realm.where(Lecture.class).findAll();
        lectures = lectures.sort("studyLectureNo", Sort.ASCENDING);
        ArrayList<Lecture> results = new ArrayList<Lecture>();
        if(lectures!=null){
            results.addAll(lectures);
        }
        return results;
    }

    public void removeStudyContent(String mediaContentKey){
        Realm realm = Realm.getDefaultInstance();
        Study study = realm.where(Study.class).equalTo("mediaContentKey", mediaContentKey).findFirst();
        if(study != null) {
            realm.beginTransaction();
            study.deleteFromRealm();
            realm.commitTransaction();
        }
    }

    public void removeStudy(String studyKey){
        Realm realm = Realm.getDefaultInstance();
        Study study = realm.where(Study.class).equalTo("studyKey", studyKey).findFirst();
        if(study != null) {
            realm.beginTransaction();
            study.deleteFromRealm();
            realm.commitTransaction();
        }
    }

    public void removeStudys(ArrayList<String> studyKeys, ArrayList<String> mediaContentKeys, OnDataChangeListener listener){
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        for(String studyKey : studyKeys){
            Study study = getStudyModel(studyKey);
            if(study != null) {
                study.deleteFromRealm();
            }
        }
        for(String mediaContentKey : mediaContentKeys){
            Study study = getStudyModelContent(mediaContentKey);
            if(study != null) {
                study.deleteFromRealm();
            }
        }
        realm.commitTransaction();
        if(listener != null) {
            listener.onDataChangeComplete();
        }
    }

    public void removeLecture(int studyLectureNo){
        Realm realm = Realm.getDefaultInstance();
        Lecture lecture = realm.where(Lecture.class).equalTo("studyLectureNo", studyLectureNo).findFirst();
        if(lecture != null) {
            realm.beginTransaction();
            lecture.deleteFromRealm();
            realm.commitTransaction();
        }
    }

    public void removeAllLectureStudyModels(OnDataChangeListener listener){
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();

        RealmResults<Study> studyList = realm.where(Study.class).findAll();
        studyList.deleteAllFromRealm();

        RealmResults<Lecture> lectureList = realm.where(Lecture.class).findAll();
        lectureList.deleteAllFromRealm();

        realm.commitTransaction();
        if(listener != null) {
            listener.onDataChangeComplete();
        }
    }

    public void refreshRealm(){
        Realm.getDefaultInstance().refresh();
    }


    ///////////
    private static final int LOCAL_DB_VERSION = 5;

    public void initDatabase(Context context)
    {
        Realm.init(context);

        RealmConfiguration config = new RealmConfiguration.Builder()
                //.deleteRealmIfMigrationNeeded()
                .schemaVersion(LOCAL_DB_VERSION)
                .migration(new RealmMigration() {
                    @Override
                    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {

                        RealmSchema schema = realm.getSchema();

                        if(oldVersion < 4) {

                            RealmObjectSchema lectureSchema = schema.get("Lecture");
                            lectureSchema.addField("favMark", boolean.class);
                            lectureSchema.addField("newMark", boolean.class);

                            oldVersion++;
                        }

                        if(oldVersion < 5) {

                            RealmObjectSchema lectureSchema = schema.get("Lecture");
                            lectureSchema.addField("updatedTime", long.class);

                            oldVersion++;
                        }
                    }
                })
                .build();

        Realm.setDefaultConfiguration(config);
    }

    public Lecture getLectureOrInsert(LectureItemVO lectureItem)
    {
        int studyLectureNo = lectureItem.getStudyLectureNo();
        Lecture lecture = getLectureModel(studyLectureNo);

        if(lecture == null){
            Realm realm = Realm.getDefaultInstance();

            realm.beginTransaction();
                lecture = Lecture.create(lectureItem);
                realm.insertOrUpdate(lecture);
            realm.commitTransaction();
        }

        return lecture;
    }

    public void updateLectureNewFlag(LectureItemVO lectureItem, boolean value)
    {
        int studyLectureNo = lectureItem.getStudyLectureNo();
        Lecture lecture = getLectureModel(studyLectureNo);
        if(lecture !=null)
        {
            long time= new Date().getTime();

            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            lecture.newMark = value;
            lecture.updatedTime = time;
            realm.commitTransaction();
        }
    }


    public void updateLectureTimeFlag(LectureItemVO lectureItem)
    {
        int studyLectureNo = lectureItem.getStudyLectureNo();
        Lecture lecture = getLectureModel(studyLectureNo);
        if(lecture !=null)
        {
            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            lecture.updatedTime = new Date().getTime();
            realm.commitTransaction();
        }
    }

    public void updateLectureFavFlag(LectureItemVO lectureItem, boolean value)
    {
        int studyLectureNo = lectureItem.getStudyLectureNo();
        Lecture lecture = getLectureModel(studyLectureNo);
        if(lecture !=null)
        {
            long time= new Date().getTime();
            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            lecture.favMark = value;
            lecture.updatedTime =time;
            realm.insertOrUpdate(lecture);
            realm.commitTransaction();
        }
    }

    public void updateLectureFavFlag(Lecture lecture, boolean value)
    {
        if(lecture !=null)
        {
            long time= new Date().getTime();
            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            lecture.favMark = value;
            lecture.updatedTime =time;
            realm.insertOrUpdate(lecture);
            realm.commitTransaction();
        }
    }

}
