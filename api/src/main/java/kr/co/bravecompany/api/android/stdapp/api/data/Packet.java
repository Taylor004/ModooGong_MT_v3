package kr.co.bravecompany.api.android.stdapp.api.data;

import androidx.annotation.Nullable;

import java.util.List;

public class Packet {

    public static class ResponseResult
    {
        public int resultCode;
    }

    //! 푸쉬키 등록
    public static class ReqSetPushKey
    {
        public String userKey;
        public String platformType;
        public String pushKey;
    }

    //! 오늘의 메시지 등록
    public static class ReqSetUserMsg
    {
        public String userKey;
        public String msg;
    }


    //! 패스 리스트 요청
    public static class ReqGetPassList
    {
        public String userKey;
    }


    public static class ResGetPassList extends ResponseResult
    {
        public List<PassData> passList;
    }

    public static class PassData
    {
        public String STD_GRD_SEQ;  //수강번호
        public String STL_SEQ;      //결제번호
        public String SAL_CD;       //판매상품코드
        public String GRD_SDT;      //수강시작일
        public String GRD_EDT;      //수강종료일
        public String SAL_NM;       //판매상품명
        public String ING_DAYS;     //남은수강일수
        public String PASS_DATE;    //총수강일수
        public String STATE;        //수강상태
        public String TCH_CD;       //강사코드
        public String EDTTM;        //마지막수강일
    }

    //! 패스에 포함된 강좌 리스트 요청
    public static class ReqGetLectureList
    {
        public String  userKey;
        public String  salCd;  // 패스판매코드
        public String  stlSeq; // 결제번호
    }

    public static class ResGetLectureList extends ResponseResult
    {
        public List<LectureData> passChrList;
    }

    public static class LectureData {

        public String SAL_NM;//강좌명
        public String SAL_SINTRO;//강좌 간단소개 :,
        public String DP_INFO;  // 노출정보
        public String SAL_GB;   //판매직급
        public String SAL_THUM; //썸네일
        public String STATE;    //판매상태 '":<String>,,
        public String ORD;//노출순서 : ":<String>,,
        public String PROD_CD;//강좌코드 : "":<String>,,
        public String STEP_SEQ;//단계 : "":<String>,,
        public String TCH_CD;//강사 : "":<String>,,
        public String SUBJ_CD;//과목 : ":<String>,,
        public String LCT_CNT;//강의수 : "":<String>,
        public String TCH_NM;
        public String SUBJ_NM;

        public String SAL_CD; //판매 상품 코드

        public boolean selected;//AdHoc!
        public boolean subscribed; //AdHoc!
        public String grdSeq;
        public String stlSeq;
    }

    //! 패스에 포함된 강좌 수강 등록
    public static class ReqSubscribeLecture
    {
        public String userKey;
    }

    public static class ResSubscribeLecture extends ResponseResult
    {
        public List<String> LectureCode;
    }

    //! 패스에 포함된 강좌 수강 해제
    public static class ReqUnsubscribeLecture
    {
        public String userKey;
    }

    public static class ResUnsubscribeLecture extends ResponseResult
    {

    }

    public  static class SubjectData
    {
        public String SUBJ_CD;
        public String SUBJ_NM;
    }

    public static class ResGetSubjectList extends ResponseResult
    {
        public List<SubjectData> subjList;
    }

    public static class TeacherData
    {
        public String TCH_CD;
        public String TCH_NM;
        public String SUBJ_CD;
    }

    public static class ResGetTeacherList extends ResponseResult
    {
        public List<TeacherData> tchList;
    }
}
