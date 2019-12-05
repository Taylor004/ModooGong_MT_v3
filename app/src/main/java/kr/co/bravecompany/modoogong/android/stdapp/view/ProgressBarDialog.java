package kr.co.bravecompany.modoogong.android.stdapp.view;

import android.app.Dialog;
import android.content.Context;

import kr.co.bravecompany.modoogong.android.stdapp.R;

/**
 * Created by BraveCompany on 2016. 10. 12..
 */

public class ProgressBarDialog extends Dialog {
    private static ProgressBarDialog mProgress;

    public static ProgressBarDialog getInstance(Context context){
        //if(mProgress == null){
            mProgress = new ProgressBarDialog(context);
        //}
        return mProgress;
    }

    private ProgressBarDialog(Context context) {
        super(context, R.style.ProgressBarDialog);

        setCanceledOnTouchOutside(false);
        setCancelable(false);

        initLayout();
    }

    private void initLayout() {
        setContentView(R.layout.dialog_progress_bar);
    }
}
