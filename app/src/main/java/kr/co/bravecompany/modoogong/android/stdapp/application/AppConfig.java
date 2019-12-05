package kr.co.bravecompany.modoogong.android.stdapp.application;

import androidx.lifecycle.ViewModelStoreOwner;

import kr.co.bravecompany.modoogong.android.stdapp.config.Tags;

public class AppConfig {


    static void SetupApplicationConfig(String packageName)
    {
        switch (packageName)
        {
            //1.용감한스피킹
            case "kr.co.bravecompany.bravespk.android.stdapp":
            {
                ModooGong.kollusKey = "a69b852441e9c5de8821ced1cc26c2eb2c0d0862";
                ModooGong.kollusPort =  7332;
                ModooGong.host = "bravespk.com";

                ModooGong.splashDescription = null;
                ModooGong.isShowCateName = true;
                ModooGong.isShowQAMenuSubject = true;
                ModooGong.isShowQAMenuType = true;
                ModooGong.isShowQAMenuTeacher = false;
                ModooGong.isRequiredQAMenuLecture = true;
                ModooGong.isShowQAMenuBook = false;
                ModooGong.isRequiredQAMenuBook = false;
                ModooGong.isShowQAVoiceRecoder = true;
                ModooGong.hasMypage = false;
                ModooGong.hasExplainStudy = false;
                ModooGong.hasExplainStudyBottom = false;
                ModooGong.hasExplainStudyNoTab = false;
                ModooGong.hasMobileWeb = true;
                ModooGong.isShowJoinLogin = true;
                ModooGong.hasStudyQA = true;
                ModooGong.isModoo = false;

                // default: -1000
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_SUBJECT = 0;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_TYPE = 1;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_TEACHER = -1000;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_LECTURE = 2;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_BOOK = -1000;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_PAGE = -1000;

                // default: "-1000"
                Tags.QA_TYPE_CD.QA_TYPE_LECTURE = "2"; //학습상담
                Tags.QA_TYPE_CD.QA_TYPE_STUDY = "1"; //수업(강의)내용
                Tags.QA_TYPE_CD.QA_TYPE_ETC = "3"; //기타
                Tags.QA_TYPE_CD.QA_TYPE_FEEDBACK = "4"; //답변첨삭

                if(ModooGong.hasStudyQA){
                    Tags.MENU_INDEX.MENU_STUDY_QA = 2;
                }
            }
            break;
            //2.덩허접영어
            case "kr.co.bravecompany.dhjeng.android.stdapp":
            {
                ModooGong.kollusKey = "18ac6d0c7ae24458a4c6686fd1e4e1499e61a84b";
                ModooGong.kollusPort =  7328;

                ModooGong.host = "dhjeng.com";
                ModooGong.splashDescription = null;
                ModooGong.isShowCateName = true;
                ModooGong.isShowQAMenuSubject = false;
                ModooGong.isShowQAMenuType = false;
                ModooGong.isShowQAMenuTeacher = false;
                ModooGong.isRequiredQAMenuLecture = true;
                ModooGong.isShowQAMenuBook = false;
                ModooGong.isRequiredQAMenuBook = false;
                ModooGong.isShowQAVoiceRecoder = false;
                ModooGong.hasMypage = true;
                ModooGong.hasExplainStudy = false;
                ModooGong.hasExplainStudyBottom = false;
                ModooGong.hasExplainStudyNoTab = false;
                ModooGong.hasMobileWeb = true;
                ModooGong.isShowJoinLogin = true;
                ModooGong.hasStudyQA = true;
                ModooGong.isModoo = false;
                ModooGong.isShowMenuTextColorGray = false;
                ModooGong.hasStudyFile = true;


                // default: -1000
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_SUBJECT = -1000;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_TYPE = -1000;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_TEACHER = -1000;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_LECTURE = 0;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_BOOK = -1000;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_PAGE = -1000;

                // default: "-1000"
                Tags.QA_TYPE_CD.QA_TYPE_LECTURE = "-1000"; //학습상담
                Tags.QA_TYPE_CD.QA_TYPE_STUDY = "-1000"; //수업(강의)내용
                Tags.QA_TYPE_CD.QA_TYPE_ETC = "-1000"; //기타
                Tags.QA_TYPE_CD.QA_TYPE_FEEDBACK = "-1000"; //답변첨삭

                if(ModooGong.hasStudyQA){
                    Tags.MENU_INDEX.MENU_STUDY_QA = 2;
                }
                if(ModooGong.hasStudyFile){
                    Tags.MENU_INDEX.MENU_STUDY_FILE = 3;
                }
            }
            break;
            //3. 덩허접 수능
            case "kr.co.bravecompany.dhjengsn.android.stdapp":
            {
                ModooGong.kollusKey = "8725d1da26bb323e5b4bfcb06ea7e115ece104b7";
                ModooGong.kollusPort =  7330;

                ModooGong.host = "dhjengsn.com";
                ModooGong.splashDescription = null;
                ModooGong.isShowCateName = false;
                ModooGong.isShowQAMenuSubject = false;
                ModooGong.isShowQAMenuType = false;
                ModooGong.isShowQAMenuTeacher = false;
                ModooGong.isRequiredQAMenuLecture = true;
                ModooGong.isShowQAMenuBook = false;
                ModooGong.isRequiredQAMenuBook = false;
                ModooGong.isShowQAVoiceRecoder = false;
                ModooGong.hasMypage = false;
                ModooGong.hasExplainStudy = false;
                ModooGong.hasExplainStudyBottom = false;
                ModooGong.hasExplainStudyNoTab = false;
                ModooGong.hasMobileWeb = true;
                ModooGong.isShowJoinLogin = true;
                ModooGong.hasStudyQA = true;
                ModooGong.isModoo = true;

                // default: -1000
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_SUBJECT = -1000;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_TYPE = -1000;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_TEACHER = -1000;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_LECTURE = 0;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_BOOK = -1000;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_PAGE = -1000;

                // default: "-1000"
                Tags.QA_TYPE_CD.QA_TYPE_LECTURE = "-1000"; //학습상담
                Tags.QA_TYPE_CD.QA_TYPE_STUDY = "-1000"; //수업(강의)내용
                Tags.QA_TYPE_CD.QA_TYPE_ETC = "-1000"; //기타
                Tags.QA_TYPE_CD.QA_TYPE_FEEDBACK = "-1000"; //답변첨삭

                if(ModooGong.hasStudyQA){
                    Tags.MENU_INDEX.MENU_STUDY_QA = 2;
                }
            }
            break;
            //4. 닥터보카
            case "kr.co.bravecompany.drvoca.android.stdapp":
            {

                ModooGong.kollusKey = "e253f7906de2661ef79051ed985f7c4ff7952478";
                ModooGong.kollusPort =  7331;

                ModooGong.host = "drvoca.com";
                ModooGong.splashDescription = null;
                ModooGong.isShowCateName = true;
                ModooGong.isShowQAMenuSubject = false;
                ModooGong.isShowQAMenuType = false;
                ModooGong.isShowQAMenuTeacher = false;
                ModooGong.isRequiredQAMenuLecture = true;
                ModooGong.isShowQAMenuBook = false;
                ModooGong.isRequiredQAMenuBook = false;
                ModooGong.isShowQAVoiceRecoder = false;
                ModooGong.hasMypage = false;
                ModooGong.hasExplainStudy = false;
                ModooGong.hasExplainStudyBottom = false;
                ModooGong.hasExplainStudyNoTab = false;
                ModooGong.hasMobileWeb = true;
                ModooGong.isShowJoinLogin = true;
                ModooGong.hasStudyQA = true;
                ModooGong.isModoo = false;

                // default: -1000
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_SUBJECT = -1000;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_TYPE = -1000;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_TEACHER = -1000;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_LECTURE = 0;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_BOOK = -1000;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_PAGE = -1000;

                // default: "-1000"
                Tags.QA_TYPE_CD.QA_TYPE_LECTURE = "-1000"; //학습상담
                Tags.QA_TYPE_CD.QA_TYPE_STUDY = "-1000"; //수업(강의)내용
                Tags.QA_TYPE_CD.QA_TYPE_ETC = "-1000"; //기타
                Tags.QA_TYPE_CD.QA_TYPE_FEEDBACK = "-1000"; //답변첨삭

                if(ModooGong.hasStudyQA){
                    Tags.MENU_INDEX.MENU_STUDY_QA = 2;
                }
            }
            break;
            //5. 유기적 수학
            case "kr.co.bravecompany.ygmath.android.stdapp":
            {
                ModooGong.kollusKey = "289f6f0a87ed9bd8ffe6a64f86afcc218481bb64";
                ModooGong.kollusPort =  7333;

                ModooGong.host = "ygmath.com";
                ModooGong.splashDescription = null;
                ModooGong.isShowCateName = false;
                ModooGong.isShowQAMenuSubject = false;
                ModooGong.isShowQAMenuType = true;
                ModooGong.isShowQAMenuTeacher = false;
                ModooGong.isRequiredQAMenuLecture = true;
                ModooGong.isShowQAMenuBook = false;
                ModooGong.isRequiredQAMenuBook = false;
                ModooGong.isShowQAVoiceRecoder = false;
                ModooGong.hasMypage = false;
                ModooGong.hasExplainStudy = false;
                ModooGong.hasExplainStudyBottom = false;
                ModooGong.hasExplainStudyNoTab = false;
                ModooGong.hasMobileWeb = true;
                ModooGong.isShowJoinLogin = true;
                ModooGong.hasStudyQA = true;
                ModooGong.isModoo = true;

                // default: -1000
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_SUBJECT = -1000;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_TYPE = 0;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_TEACHER = -1000;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_LECTURE = 1;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_BOOK = -1000;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_PAGE = -1000;

                // default: "-1000"
                Tags.QA_TYPE_CD.QA_TYPE_LECTURE = "1"; //학습상담
                Tags.QA_TYPE_CD.QA_TYPE_STUDY = "2"; //수업(강의)내용
                Tags.QA_TYPE_CD.QA_TYPE_ETC = "3"; //기타
                Tags.QA_TYPE_CD.QA_TYPE_FEEDBACK = "-1000"; //답변첨삭

                if(ModooGong.hasStudyQA){
                    Tags.MENU_INDEX.MENU_STUDY_QA = 2;
                }
            }
            break;
            //6. 덩허접기초
            case "kr.co.bravecompany.dhjrestart.android.stdapp":
            {
                ModooGong.kollusKey = "6fea4f251ec8d5960c091147b2721d01afd92666";
                ModooGong.kollusPort =  7329;

                ModooGong.host = "dhjbasic.com";
                ModooGong.splashDescription = null;
                ModooGong.isShowCateName = true;
                ModooGong.isShowQAMenuSubject = false;
                ModooGong.isShowQAMenuType = true;
                ModooGong.isShowQAMenuTeacher = false;
                ModooGong.isRequiredQAMenuLecture = true;
                ModooGong.isShowQAMenuBook = false;
                ModooGong.isRequiredQAMenuBook = false;
                ModooGong.isShowQAVoiceRecoder = false;
                ModooGong.hasMypage = true;
                ModooGong.hasExplainStudy = false;
                ModooGong.hasExplainStudyBottom = false;
                ModooGong.hasExplainStudyNoTab = false;
                ModooGong.hasMobileWeb = true;
                ModooGong.isShowJoinLogin = true;
                ModooGong.hasStudyQA = true;
                ModooGong.isModoo = false;

                // default: -1000
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_SUBJECT = -1000;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_TYPE = 0;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_TEACHER = -1000;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_LECTURE = 1;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_BOOK = -1000;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_PAGE = -1000;

                // default: "-1000"
                Tags.QA_TYPE_CD.QA_TYPE_LECTURE = "1"; //학습상담
                Tags.QA_TYPE_CD.QA_TYPE_STUDY = "2"; //수업(강의)내용
                Tags.QA_TYPE_CD.QA_TYPE_ETC = "3"; //기타
                Tags.QA_TYPE_CD.QA_TYPE_FEEDBACK = "-1000"; //답변첨삭

                if(ModooGong.hasStudyQA){
                    Tags.MENU_INDEX.MENU_STUDY_QA = 2;
                }
            }
            break;
            //7. 코스모스생명과학
            case "net.bravecompany.cosmosbio.android.stdapp":
            {
                ModooGong.kollusKey = "fbba10f08d491ee57fa06138a9bcc463e6e71269";
                ModooGong.kollusPort =  7481;

                ModooGong.host = "cosmosbio.kr";
                ModooGong.splashDescription = null;
                ModooGong.isShowCateName = true;
                ModooGong.isShowQAMenuSubject = false;
                ModooGong.isShowQAMenuType = false;
                ModooGong.isShowQAMenuTeacher = false;
                ModooGong.isRequiredQAMenuLecture = true;
                ModooGong.isShowQAMenuBook = false;
                ModooGong.isRequiredQAMenuBook = false;
                ModooGong.isShowQAVoiceRecoder = false;
                ModooGong.hasMypage = true;
                ModooGong.hasExplainStudy = false;
                ModooGong.hasExplainStudyBottom = false;
                ModooGong.hasExplainStudyNoTab = false;
                ModooGong.hasMobileWeb = true;
                ModooGong.isShowJoinLogin = true;
                ModooGong.hasStudyQA = true;
                ModooGong.isModoo = true;


                // default: -1000
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_SUBJECT = -1000;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_TYPE = -1000;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_TEACHER = -1000;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_LECTURE = 0;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_BOOK = -1000;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_PAGE = -1000;

                // default: "-1000"
                Tags.QA_TYPE_CD.QA_TYPE_LECTURE = "-1000"; //학습상담
                Tags.QA_TYPE_CD.QA_TYPE_STUDY = "-1000"; //수업(강의)내용
                Tags.QA_TYPE_CD.QA_TYPE_ETC = "-1000"; //기타
                Tags.QA_TYPE_CD.QA_TYPE_FEEDBACK = "-1000"; //답변첨삭

                if(ModooGong.hasStudyQA){
                    Tags.MENU_INDEX.MENU_STUDY_QA = 2;
                }

            }
            break;
            //8. 아만다지구과학
            case "net.bravecompany.amandaearth.android.stdapp":
            {
                ModooGong.kollusKey = "501187a174698ed2e6f9833e0a2700ec0412d0d8";
                ModooGong.kollusPort =  7483;


                ModooGong.host = "amandaearth.com";
                ModooGong.splashDescription = null;
                ModooGong.isShowCateName = true;
                ModooGong.isShowQAMenuSubject = false;
                ModooGong.isShowQAMenuType = false;
                ModooGong.isShowQAMenuTeacher = false;
                ModooGong.isRequiredQAMenuLecture = true;
                ModooGong.isShowQAMenuBook = false;
                ModooGong.isRequiredQAMenuBook = false;
                ModooGong.isShowQAVoiceRecoder = false;
                ModooGong.hasMypage = true;
                ModooGong.hasExplainStudy = false;
                ModooGong.hasExplainStudyBottom = false;
                ModooGong.hasExplainStudyNoTab = false;
                ModooGong.hasMobileWeb = true;
                ModooGong.isShowJoinLogin = true;
                ModooGong.hasStudyQA = true;
                ModooGong.isModoo = true;

                // default: -1000
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_SUBJECT = -1000;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_TYPE = -1000;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_TEACHER = -1000;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_LECTURE = 0;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_BOOK = -1000;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_PAGE = -1000;

                // default: "-1000"
                Tags.QA_TYPE_CD.QA_TYPE_LECTURE = "-1000"; //학습상담
                Tags.QA_TYPE_CD.QA_TYPE_STUDY = "-1000"; //수업(강의)내용
                Tags.QA_TYPE_CD.QA_TYPE_ETC = "-1000"; //기타
                Tags.QA_TYPE_CD.QA_TYPE_FEEDBACK = "-1000"; //답변첨삭

                if(ModooGong.hasStudyQA){
                    Tags.MENU_INDEX.MENU_STUDY_QA = 2;
                }
            }
            break;
            //9. 강한수능국어
            case "net.bravecompany.ganghankor.android.stdapp":
            {
                ModooGong.kollusKey = "b4abc239648916c545e985fc43a21f1ad5cb0032";
                ModooGong.kollusPort =  7484;


                ModooGong.host = "ganghankor.com";
                ModooGong.splashDescription = null;
                ModooGong.isShowCateName = false;
                ModooGong.isShowQAMenuSubject = false;
                ModooGong.isShowQAMenuType = false;
                ModooGong.isShowQAMenuTeacher = false;
                ModooGong.isRequiredQAMenuLecture = true;
                ModooGong.isShowQAMenuBook = false;
                ModooGong.isRequiredQAMenuBook = false;
                ModooGong.isShowQAVoiceRecoder = false;
                ModooGong.hasMypage = false;
                ModooGong.hasExplainStudy = true;
                ModooGong.hasExplainStudyBottom = false;
                ModooGong.hasExplainStudyNoTab = false;
                ModooGong.hasMobileWeb = true;
                ModooGong.isShowJoinLogin = true;
                ModooGong.hasStudyQA = true;
                ModooGong.isModoo = true;

                // default: -1000
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_SUBJECT = -1000;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_TYPE = -1000;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_TEACHER = -1000;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_LECTURE = 0;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_BOOK = -1000;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_PAGE = -1000;

                // default: "-1000"
                Tags.QA_TYPE_CD.QA_TYPE_LECTURE = "-1000"; //학습상담
                Tags.QA_TYPE_CD.QA_TYPE_STUDY = "-1000"; //수업(강의)내용
                Tags.QA_TYPE_CD.QA_TYPE_ETC = "-1000"; //기타
                Tags.QA_TYPE_CD.QA_TYPE_FEEDBACK = "-1000"; //답변첨삭

                if(ModooGong.hasStudyQA){
                    Tags.MENU_INDEX.MENU_STUDY_QA = 2;
                }

            }
            break;
            //10. 가로세로한국사스쿨
            case "net.bravecompany.gasehisexam.android.stdapp":
            {
                ModooGong.kollusKey = "e83d77c7a2bdca19d4a988675491046170442db0";
                ModooGong.kollusPort =  7486;


                ModooGong.host = "gasehisexam.com";
                ModooGong.splashDescription = null;
                ModooGong.isShowCateName = true;
                ModooGong.isShowQAMenuSubject = true;
                ModooGong.isShowQAMenuType = false;
                ModooGong.isShowQAMenuTeacher = false;
                ModooGong.isRequiredQAMenuLecture = true;
                ModooGong.isShowQAMenuBook = false;
                ModooGong.isRequiredQAMenuBook = false;
                ModooGong.isShowQAVoiceRecoder = false;
                ModooGong.hasMypage = true;
                ModooGong.hasExplainStudy = false;
                ModooGong.hasExplainStudyBottom = false;
                ModooGong.hasExplainStudyNoTab = false;
                ModooGong.hasMobileWeb = true;
                ModooGong.isShowJoinLogin = true;
                ModooGong.hasStudyQA = true;
                ModooGong.isModoo = false;

                // default: -1000
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_SUBJECT = 0;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_TYPE = -1000;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_TEACHER = -1000;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_LECTURE = 1;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_BOOK = -1000;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_PAGE = -1000;

                // default: "-1000"
                Tags.QA_TYPE_CD.QA_TYPE_LECTURE = "-1000"; //학습상담
                Tags.QA_TYPE_CD.QA_TYPE_STUDY = "-1000"; //수업(강의)내용
                Tags.QA_TYPE_CD.QA_TYPE_ETC = "-1000"; //기타
                Tags.QA_TYPE_CD.QA_TYPE_FEEDBACK = "-1000"; //답변첨삭

                if(ModooGong.hasStudyQA){
                    Tags.MENU_INDEX.MENU_STUDY_QA = 2;
                }
            }
            break;
            //11. 모두의자격증
            case "net.bravecompany.modoolicense.android.stdapp":
            {
                ModooGong.kollusKey = "26feac5463bb31c262a296a8993e16e9e4d6384f";
                ModooGong.kollusPort =  7487;

                ModooGong.host = "modoolicense.com";
                ModooGong.splashDescription = null;
                ModooGong.isShowCateName = false;
                ModooGong.isShowQAMenuSubject = false;
                ModooGong.isShowQAMenuType = false;
                ModooGong.isShowQAMenuTeacher = false;
                ModooGong.isRequiredQAMenuLecture = true;
                ModooGong.isShowQAMenuBook = false;
                ModooGong.isRequiredQAMenuBook = false;
                ModooGong.isShowQAVoiceRecoder = false;
                ModooGong.hasMypage = false;
                ModooGong.hasExplainStudy = false;
                ModooGong.hasExplainStudyBottom = false;
                ModooGong.hasExplainStudyNoTab = false;
                ModooGong.hasMobileWeb = true;
                ModooGong.isShowJoinLogin = true;
                ModooGong.hasStudyQA = true;
                ModooGong.isModoo = false;


                // default: -1000
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_SUBJECT = -1000;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_TYPE = -1000;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_TEACHER = -1000;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_LECTURE = 0;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_BOOK = -1000;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_PAGE = -1000;

                // default: "-1000"
                Tags.QA_TYPE_CD.QA_TYPE_LECTURE = "-1000"; //학습상담
                Tags.QA_TYPE_CD.QA_TYPE_STUDY = "-1000"; //수업(강의)내용
                Tags.QA_TYPE_CD.QA_TYPE_ETC = "-1000"; //기타
                Tags.QA_TYPE_CD.QA_TYPE_FEEDBACK = "-1000"; //답변첨삭

                if(ModooGong.hasStudyQA){
                    Tags.MENU_INDEX.MENU_STUDY_QA = 2;
                }

            }
            break;
            //12. 도끼한국사
            case "net.bravecompany.dok2his.android.stdapp":
            {
                ModooGong.kollusKey = "adff1f8309e61297a0cc262d6a42698ffa867ad3";
                ModooGong.kollusPort =  7490;


                ModooGong.host = "dok2his.com";
                ModooGong.splashDescription = null;
                ModooGong.isShowCateName = true;
                ModooGong.isShowQAMenuSubject = false;
                ModooGong.isShowQAMenuType = false;
                ModooGong.isShowQAMenuTeacher = false;
                ModooGong.isRequiredQAMenuLecture = true;
                ModooGong.isShowQAMenuBook = false;
                ModooGong.isRequiredQAMenuBook = false;
                ModooGong.isShowQAVoiceRecoder = false;
                ModooGong.hasMypage = true;
                ModooGong.hasExplainStudy = false;
                ModooGong.hasExplainStudyBottom = false;
                ModooGong.hasExplainStudyNoTab = false;
                ModooGong.hasMobileWeb = true;
                ModooGong.isShowJoinLogin = true;
                ModooGong.hasStudyQA = true;
                ModooGong.isModoo = false;

                // default: -1000
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_SUBJECT = -1000;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_TYPE = -1000;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_TEACHER = -1000;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_LECTURE = 0;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_BOOK = -1000;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_PAGE = -1000;

                // default: "-1000"
                Tags.QA_TYPE_CD.QA_TYPE_LECTURE = "-1000"; //학습상담
                Tags.QA_TYPE_CD.QA_TYPE_STUDY = "-1000"; //수업(강의)내용
                Tags.QA_TYPE_CD.QA_TYPE_ETC = "-1000"; //기타
                Tags.QA_TYPE_CD.QA_TYPE_FEEDBACK = "-1000"; //답변첨삭

                if(ModooGong.hasStudyQA){
                    Tags.MENU_INDEX.MENU_STUDY_QA = 2;
                }
            }
            break;
            //13. 리라클영어
            case "net.bravecompany.riracleeng.android.stdapp":
            {
                ModooGong.kollusKey = "d354a7d13d3fd9b755813ed13eb2698d4b4617b9";
                ModooGong.kollusPort =  7491;


                ModooGong.host = "riracleeng.kr";
                ModooGong.splashDescription = null;
                ModooGong.isShowCateName = true;
                ModooGong.isShowQAMenuSubject = false;
                ModooGong.isShowQAMenuType = false;
                ModooGong.isShowQAMenuTeacher = false;
                ModooGong.isRequiredQAMenuLecture = true;
                ModooGong.isShowQAMenuBook = false;
                ModooGong.isRequiredQAMenuBook = false;
                ModooGong.isShowQAVoiceRecoder = false;
                ModooGong.hasMypage = true;
                ModooGong.hasExplainStudy = false;
                ModooGong.hasExplainStudyBottom = false;
                ModooGong.hasExplainStudyNoTab = false;
                ModooGong.hasMobileWeb = true;
                ModooGong.isShowJoinLogin = true;
                ModooGong.hasStudyQA = true;
                ModooGong.isModoo = false;


                // default: -1000
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_SUBJECT = -1000;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_TYPE = -1000;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_TEACHER = -1000;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_LECTURE = 0;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_BOOK = -1000;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_PAGE = -1000;

                // default: "-1000"
                Tags.QA_TYPE_CD.QA_TYPE_LECTURE = "-1000"; //학습상담
                Tags.QA_TYPE_CD.QA_TYPE_STUDY = "-1000"; //수업(강의)내용
                Tags.QA_TYPE_CD.QA_TYPE_ETC = "-1000"; //기타
                Tags.QA_TYPE_CD.QA_TYPE_FEEDBACK = "-1000"; //답변첨삭

                if(ModooGong.hasStudyQA){
                    Tags.MENU_INDEX.MENU_STUDY_QA = 2;
                }
            }
            break;
            //14.모두공
            case "net.bravecompany.modoogong.android.stdapp":
            {
                ModooGong.kollusKey = "dd7706d1077fa828ae0ebec7e5cca8fcf9b2434d";
                ModooGong.kollusPort =  7492 ;
                ModooGong.host = "modoogong.com";
                ModooGong.storeURL = "https://m.modoogong.com";

                ModooGong.splashDescription = null;
                ModooGong.isShowCateName = true;
                ModooGong.isShowQAMenuSubject = false;
                ModooGong.isShowQAMenuType = false;
                ModooGong.isShowQAMenuTeacher = false;
                ModooGong.isRequiredQAMenuLecture = true;
                ModooGong.isShowQAMenuBook = false;
                ModooGong.isRequiredQAMenuBook = false;
                ModooGong.isShowQAVoiceRecoder = false;
                ModooGong.hasMypage = false;
                ModooGong.hasExplainStudy = false;
                ModooGong.hasExplainStudyBottom = false;
                ModooGong.hasExplainStudyNoTab = false;
                ModooGong.hasMobileWeb = false;
                ModooGong.isShowJoinLogin = true;
                ModooGong.hasStudyQA = false;
                ModooGong.isModoo = true;
                ModooGong.isShowMenuTextColorGray = true;
                ModooGong.hasStudyFile = false;

                ModooGong.bookStoreSeq = "32";

                // default: -1000
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_SUBJECT = -1000;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_TYPE = -1000;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_TEACHER = -1000;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_LECTURE = -1000;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_BOOK = -1000;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_PAGE = -1000;

                // default: "-1000"
                Tags.QA_TYPE_CD.QA_TYPE_LECTURE = "-1000"; //학습상담
                Tags.QA_TYPE_CD.QA_TYPE_STUDY = "-1000"; //수업(강의)내용
                Tags.QA_TYPE_CD.QA_TYPE_ETC = "-1000"; //기타
                Tags.QA_TYPE_CD.QA_TYPE_FEEDBACK = "-1000"; //답변첨삭

                if(ModooGong.hasStudyQA){
                    Tags.MENU_INDEX.MENU_STUDY_QA = 2;
                }
                if(ModooGong.hasStudyFile){
                    Tags.MENU_INDEX.MENU_STUDY_FILE = 3;
                }

            }
            break;
            //15. 모두의경찰
            case "net.bravecompany.modoocop.android.stdapp":
            {

                ModooGong.kollusKey = "32100a5516ccea973ededdf1501a30a48baba250";
                ModooGong.kollusPort =  7494;
                ModooGong.storeURL = "https://m.modoocop.com";

                ModooGong.host = "modoocop.com";
                ModooGong.splashDescription = null;
                ModooGong.isShowCateName = false;
                ModooGong.isShowQAMenuSubject = false;
                ModooGong.isShowQAMenuType = false;
                ModooGong.isShowQAMenuTeacher = false;
                ModooGong.isRequiredQAMenuLecture = true;
                ModooGong.isShowQAMenuBook = false;
                ModooGong.isRequiredQAMenuBook = false;
                ModooGong.isShowQAVoiceRecoder = false;
                ModooGong.hasMypage = false;
                ModooGong.hasExplainStudy = false;
                ModooGong.hasExplainStudyBottom = false;
                ModooGong.hasExplainStudyNoTab = false;
                ModooGong.hasMobileWeb = false;
                ModooGong.isShowJoinLogin = true;
                ModooGong.hasStudyQA = false;
                ModooGong.isModoo = true;
                ModooGong.isShowMenuTextColorGray = true;
                ModooGong.hasStudyFile = false;

                ModooGong.bookStoreSeq = "35";
                // default: -1000
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_SUBJECT = -1000;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_TYPE = -1000;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_TEACHER = -1000;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_LECTURE = -1000;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_BOOK = -1000;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_PAGE = -1000;

                // default: "-1000"
                Tags.QA_TYPE_CD.QA_TYPE_LECTURE = "-1000"; //학습상담
                Tags.QA_TYPE_CD.QA_TYPE_STUDY = "-1000"; //수업(강의)내용
                Tags.QA_TYPE_CD.QA_TYPE_ETC = "-1000"; //기타
                Tags.QA_TYPE_CD.QA_TYPE_FEEDBACK = "-1000"; //답변첨삭

                if(ModooGong.hasStudyQA){
                    Tags.MENU_INDEX.MENU_STUDY_QA = 2;
                }
                if(ModooGong.hasStudyFile){
                    Tags.MENU_INDEX.MENU_STUDY_FILE = 3;
                }

            }
            break;
            //16. 모두의소방
            case "net.bravecompany.modoofire.android.stdapp":
            {
                ModooGong.kollusKey = "de9233d08c95ffd1f20cb2f037a486af1b09d08a";
                ModooGong.kollusPort =  7496;
                ModooGong.storeURL = "https://m.modoofire.com";

                ModooGong.host = "modoofire.com";
                ModooGong.splashDescription = null;
                ModooGong.isShowCateName = true;
                ModooGong.isShowQAMenuSubject = false;
                ModooGong.isShowQAMenuType = false;
                ModooGong.isShowQAMenuTeacher = false;
                ModooGong.isRequiredQAMenuLecture = true;
                ModooGong.isShowQAMenuBook = false;
                ModooGong.isRequiredQAMenuBook = false;
                ModooGong.isShowQAVoiceRecoder = false;
                ModooGong.hasMypage = false;
                ModooGong.hasExplainStudy = false;
                ModooGong.hasExplainStudyBottom = false;
                ModooGong.hasExplainStudyNoTab = false;
                ModooGong.hasMobileWeb = false;
                ModooGong.isShowJoinLogin = true;
                ModooGong.hasStudyQA = false;
                ModooGong.isModoo = true;
                ModooGong.isShowMenuTextColorGray = true; // 테일러 : 19.10.08(화) 모두소 메뉴 슬라이드 텍스트가 안보이는 현상 원인이 였던 부분.
                ModooGong.hasStudyFile = false;

                ModooGong.bookStoreSeq = "36";

                // default: -1000
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_SUBJECT = -1000;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_TYPE = -1000;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_TEACHER = -1000;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_LECTURE = -1000;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_BOOK = -1000;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_PAGE = -1000;

                // default: "-1000"
                Tags.QA_TYPE_CD.QA_TYPE_LECTURE = "-1000"; //학습상담
                Tags.QA_TYPE_CD.QA_TYPE_STUDY = "-1000"; //수업(강의)내용
                Tags.QA_TYPE_CD.QA_TYPE_ETC = "-1000"; //기타
                Tags.QA_TYPE_CD.QA_TYPE_FEEDBACK = "-1000"; //답변첨삭

                if(ModooGong.hasStudyQA){
                    Tags.MENU_INDEX.MENU_STUDY_QA = 2;
                }
                if(ModooGong.hasStudyFile){
                    Tags.MENU_INDEX.MENU_STUDY_FILE = 3;
                }
            }
            break;
            //17. 클라우드화학
            case "net.bravecompany.cloudchem.android.stdapp":
            {

                ModooGong.kollusKey = "1c2c9923815d0497b6dd72eed6b52d2a2f650a26";
                ModooGong.kollusPort =  7497;
                ModooGong.host = "cloudchem.kr";


                ModooGong.splashDescription = null;
                ModooGong.isShowCateName = true;
                ModooGong.isShowQAMenuSubject = false;
                ModooGong.isShowQAMenuType = false;
                ModooGong.isShowQAMenuTeacher = false;
                ModooGong.isRequiredQAMenuLecture = true;
                ModooGong.isShowQAMenuBook = false;
                ModooGong.isRequiredQAMenuBook = false;
                ModooGong.isShowQAVoiceRecoder = false;
                ModooGong.hasMypage = true;
                ModooGong.hasExplainStudy = false;
                ModooGong.hasExplainStudyBottom = false;
                ModooGong.hasExplainStudyNoTab = false;
                ModooGong.hasMobileWeb = false;
                ModooGong.isShowJoinLogin = true;
                ModooGong.hasStudyQA = true;
                ModooGong.isModoo = true;
                ModooGong.isShowMenuTextColorGray = false;
                ModooGong.hasStudyFile = false;


                // default: -1000
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_SUBJECT = -1000;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_TYPE = -1000;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_TEACHER = -1000;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_LECTURE = 0;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_BOOK = -1000;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_PAGE = -1000;

                // default: "-1000"
                Tags.QA_TYPE_CD.QA_TYPE_LECTURE = "-1000"; //학습상담
                Tags.QA_TYPE_CD.QA_TYPE_STUDY = "-1000"; //수업(강의)내용
                Tags.QA_TYPE_CD.QA_TYPE_ETC = "-1000"; //기타
                Tags.QA_TYPE_CD.QA_TYPE_FEEDBACK = "-1000"; //답변첨삭

                if(ModooGong.hasStudyQA){
                    Tags.MENU_INDEX.MENU_STUDY_QA = 2;
                }
                if(ModooGong.hasStudyFile){
                    Tags.MENU_INDEX.MENU_STUDY_FILE = 3;
                }
            }
            break;
            //18. 이파마스
            case "co.kr.bravecompany.iipa.android.stdapp":
            {
                ModooGong.kollusKey = "604606851d25bec60016c97b1219a421718046df";
                ModooGong.kollusPort =  7498;


                ModooGong.host = "iipamaster.com";
                ModooGong.splashDescription = null;
                ModooGong.isShowCateName = true;
                ModooGong.isShowQAMenuSubject = false;
                ModooGong.isShowQAMenuType = false;
                ModooGong.isShowQAMenuTeacher = false;
                ModooGong.isRequiredQAMenuLecture = true;
                ModooGong.isShowQAMenuBook = false;
                ModooGong.isRequiredQAMenuBook = false;
                ModooGong.isShowQAVoiceRecoder = false;
                ModooGong.hasMypage = true;
                ModooGong.hasExplainStudy = false;
                ModooGong.hasExplainStudyBottom = false;
                ModooGong.hasExplainStudyNoTab = false;
                ModooGong.hasMobileWeb = true;
                ModooGong.isShowJoinLogin = true;
                ModooGong.hasStudyQA = false;
                ModooGong.isModoo = false;

                // default: -1000
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_SUBJECT = -1000;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_TYPE = -1000;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_TEACHER = -1000;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_LECTURE = 0;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_BOOK = -1000;
                Tags.STUDY_QA_MENU_INDEX.QA_MENU_PAGE = -1000;

                // default: "-1000"
                Tags.QA_TYPE_CD.QA_TYPE_LECTURE = "-1000"; //학습상담
                Tags.QA_TYPE_CD.QA_TYPE_STUDY = "-1000"; //수업(강의)내용
                Tags.QA_TYPE_CD.QA_TYPE_ETC = "-1000"; //기타
                Tags.QA_TYPE_CD.QA_TYPE_FEEDBACK = "-1000"; //답변첨삭

                if(ModooGong.hasStudyQA){
                    Tags.MENU_INDEX.MENU_STUDY_QA = 2;
                }

            }
            break;

        }
    }
}
