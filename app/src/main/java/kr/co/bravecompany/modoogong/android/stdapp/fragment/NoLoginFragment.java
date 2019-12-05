package kr.co.bravecompany.modoogong.android.stdapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;

import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.modoogong.android.stdapp.config.AnalysisTags;
import kr.co.bravecompany.modoogong.android.stdapp.utils.BraveUtils;

/**
 * Created by BraveCompany on 2016. 10. 12..
 */

public class NoLoginFragment extends BaseFragment {

    private TextView btnLogin;

    public static NoLoginFragment newInstance() {
        return new NoLoginFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_no_login, container, false);
        initLayout(rootView);
        initListener();
        return rootView;
    }

    @Override
    protected void initLayout(ViewGroup rootView) {
        btnLogin = (TextView)rootView.findViewById(R.id.btnLogin);
    }

    @Override
    protected void initListener() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BraveUtils.goLogin(getActivity());

                Answers.getInstance().logCustom(new CustomEvent(AnalysisTags.NOLOGIN)
                        .putCustomAttribute(AnalysisTags.ACTION, "go_login"));
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setData(String data) {

    }

}
