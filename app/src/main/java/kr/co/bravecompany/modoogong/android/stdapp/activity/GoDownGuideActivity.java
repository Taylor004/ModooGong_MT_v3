package kr.co.bravecompany.modoogong.android.stdapp.activity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.modoogong.android.stdapp.manager.PropertyManager;

public class GoDownGuideActivity extends BaseActivity {

    TextView btnClose;
    TextView btnNoMore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.66f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.activity_go_down_guide);
        initLayout();
        initListener();
    }

    private void initLayout(){

        btnClose = (TextView)findViewById(R.id.btnClose);
        btnNoMore = (TextView)findViewById(R.id.btnNoMore);
    }

    private void initListener(){
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnNoMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PropertyManager.getInstance().setShowGoDownGuide(false);
                finish();
            }
        });
    }
}
