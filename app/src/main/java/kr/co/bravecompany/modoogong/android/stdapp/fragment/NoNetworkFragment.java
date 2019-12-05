package kr.co.bravecompany.modoogong.android.stdapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;

import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.modoogong.android.stdapp.activity.MainActivity;
import kr.co.bravecompany.modoogong.android.stdapp.config.AnalysisTags;
import kr.co.bravecompany.modoogong.android.stdapp.config.Tags;
import kr.co.bravecompany.modoogong.android.stdapp.utils.BraveUtils;
import kr.co.bravecompany.modoogong.android.stdapp.utils.SystemUtils;

/**
 * Created by BraveCompany on 2016. 10. 12..
 */

public class NoNetworkFragment extends BaseFragment {

    private boolean showGoDown = true;

    private TextView btnReconnect;
    private LinearLayout layoutGoDown;
    private TextView btnGoDown;

    public static NoNetworkFragment newInstance() {
        return new NoNetworkFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            showGoDown = args.getBoolean(Tags.TAG_GO_DOWN, false);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_no_network, container, false);
        initLayout(rootView);
        initListener();
        return rootView;
    }

    @Override
    protected void initLayout(ViewGroup rootView) {
        btnReconnect = (TextView)rootView.findViewById(R.id.btnReconnect);
        layoutGoDown = (LinearLayout)rootView.findViewById(R.id.layoutGoDown);
        if(showGoDown) {
            layoutGoDown.setVisibility(View.VISIBLE);
        }else{
            layoutGoDown.setVisibility(View.GONE);
        }
        btnGoDown = (TextView)rootView.findViewById(R.id.btnGoDown);
    }

    @Override
    protected void initListener() {

        btnReconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SystemUtils.isNetworkConnected(getContext())){
                    ((MainActivity)getActivity()).refreshMenuPage();
                }else{
                    BraveUtils.showToast(getActivity(), getString(R.string.toast_no_network));
                }

                Answers.getInstance().logCustom(new CustomEvent(AnalysisTags.NONETWORK)
                        .putCustomAttribute(AnalysisTags.ACTION, "reconnect_network"));
            }
        });

        btnGoDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).openMenuPage(MainActivity.PageId.MAIN_DOWNLOAD_LECTURE);

                Answers.getInstance().logCustom(new CustomEvent(AnalysisTags.NONETWORK)
                        .putCustomAttribute(AnalysisTags.ACTION, "go_local_lecture"));
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setData(String data)
    {

    }

}
