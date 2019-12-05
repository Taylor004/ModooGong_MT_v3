package kr.co.bravecompany.modoogong.android.stdapp.adapter;

import kr.co.bravecompany.modoogong.android.stdapp.application.ModooGong;
import kr.co.bravecompany.modoogong.android.stdapp.config.Tags;
import kr.co.bravecompany.modoogong.android.stdapp.data.QAMenuData;

/**
 * Created by BraveCompany on 2016. 11. 4..
 */

public class StudyQAMenuAdapter extends QAMenuAdapter{

    @Override
    public void updateQAMenuIndexValue(int position, int index, String value){
        QAMenuData menu = items.get(position);
        menu.setIndex(index);
        menu.setValue(value);

        for(int i=position+1; i<items.size(); i++){
            QAMenuData m = items.get(i);
            if(menu.getTag() == m.getTag()) {
                m.setIndex(-1);
                m.setValue("");
            }
        }

        if(ModooGong.isShowQAMenuSubject){
            if(mQAType != null && mQAType.size() != 0) {
                int type = items.get(Tags.STUDY_QA_MENU_INDEX.QA_MENU_TYPE).getIndex();
                QAMenuData m = items.get(Tags.STUDY_QA_MENU_INDEX.QA_MENU_LECTURE);
                if(type == -1){
                    m.setEnable(true);
                }else {
                    if(type > -1 && type < mQAType.size()) {
                        String CD = mQAType.get(type).getCD();
                        m.setEnable(CD.equals(Tags.QA_TYPE_CD.QA_TYPE_STUDY) ||
                                        CD.equals(Tags.QA_TYPE_CD.QA_TYPE_FEEDBACK));
                    }
                }
            }
        }else if(ModooGong.isShowQAMenuType){
            if(mQAType != null && mQAType.size() != 0) {
                int type = items.get(Tags.STUDY_QA_MENU_INDEX.QA_MENU_TYPE).getIndex();
                QAMenuData m = items.get(Tags.STUDY_QA_MENU_INDEX.QA_MENU_LECTURE);
                if(type == -1){
                    m.setEnable(true);
                }else {
                    if(type > -1 && type < mQAType.size()) {
                        String CD = mQAType.get(type).getCD();
                        m.setEnable(CD.equals(Tags.QA_TYPE_CD.QA_TYPE_STUDY));
                    }
                }
            }
        }else{
            //
        }

        notifyDataSetChanged();
    }

}
