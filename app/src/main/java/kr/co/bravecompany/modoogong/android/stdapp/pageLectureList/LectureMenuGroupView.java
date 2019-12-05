package kr.co.bravecompany.modoogong.android.stdapp.pageLectureList;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.modoogong.android.stdapp.data.LectureMenuGroupData;

/**
 * Created by BraveCompany on 2016. 10. 17..
 */

public class LectureMenuGroupView extends LinearLayout {

    TextView txtLectureType;
    TextView txtLectureCount;
    LectureMenuGroupData item;

    public LectureMenuGroupView(Context context) {
        super(context);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.view_lecture_menu, this);
        txtLectureType = (TextView) findViewById(R.id.txtLectureType);
        txtLectureCount = (TextView) findViewById(R.id.txtLectureCount);
    }

    public void setGroupItem(LectureMenuGroupData item) {
        if(item != null) {
            this.item = item;

            txtLectureType.setText(item.getGroupName());
            txtLectureCount.setText(String.format(getResources().getString(R.string.lecture_count),
                    item.getLectureCount()));
        }
    }
}
