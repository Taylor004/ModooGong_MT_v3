package kr.co.bravecompany.modoogong.android.stdapp.pageLectureList;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.modoogong.android.stdapp.data.LectureMenuData;

/**
 * Created by BraveCompany on 2016. 10. 17..
 */

public class LectureMenuView extends LinearLayout{

    TextView txtLectureType;
    ImageView imgCheck;
    LectureMenuData item;

    public LectureMenuView(Context context) {
        super(context);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.view_lecture_menu_item, this);
        txtLectureType = (TextView)findViewById(R.id.txtLectureType);
        imgCheck = (ImageView)findViewById(R.id.imgCheck);
    }

    public void setChildItem(LectureMenuData item) {
        if(item != null) {
            this.item = item;

            txtLectureType.setText(item.getChildName());
            txtLectureType.setSelected(item.isSelected());

            if (item.isSelected()) {
                imgCheck.setVisibility(View.VISIBLE);
            } else {
                imgCheck.setVisibility(View.GONE);
            }
        }
    }
}
