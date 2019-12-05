package kr.co.bravecompany.modoogong.android.stdapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kr.co.bravecompany.modoogong.android.stdapp.R;

/**
 * Created by BraveCompany on 2017. 7. 6..
 */

public class NoMenuFragment extends BaseFragment {

    public static NoMenuFragment newInstance() {
        return new NoMenuFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_no_menu, container, false);
        initLayout(rootView);
        initListener();
        return rootView;
    }

    @Override
    protected void initLayout(ViewGroup rootView) {

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setData(String data) {

    }

}
