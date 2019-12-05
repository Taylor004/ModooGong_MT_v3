package kr.co.bravecompany.modoogong.android.stdapp.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.ViewGroup;

import kr.co.bravecompany.modoogong.android.stdapp.activity.BaseActivity;

/**
 * Created by BraveCompany on 2016. 10. 14..
 */

public abstract class BaseFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void startLoading(){
        BaseActivity activity = (BaseActivity)getActivity();
        if(activity != null){
            activity.startLoading();
        }
    }

    public void stopLoading(){
        BaseActivity activity = (BaseActivity)getActivity();
        if(activity != null){
            activity.stopLoading();
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Abstract Method In Activity
    ///////////////////////////////////////////////////////////////////////////

    protected abstract void initLayout(ViewGroup rootView);

    protected abstract void initListener();

    protected abstract void initData();

    protected abstract void setData(String data);

    //Adhoc, Jongok... 프래그먼트 리프레시 이벤트를 발생시키기 위해...
    public void refreshFragment() {}
}
