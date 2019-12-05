package kr.co.bravecompany.modoogong.android.stdapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;

import java.util.ArrayList;

import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.modoogong.android.stdapp.adapter.FreeExplainStudyAdapter;
import kr.co.bravecompany.api.android.stdapp.api.OnResultListener;
import kr.co.bravecompany.api.android.stdapp.api.requests.StudyRequests;
import kr.co.bravecompany.api.android.stdapp.api.utils.APIManager;
import kr.co.bravecompany.api.android.stdapp.api.data.FreeExplainStudyItemVO;
import kr.co.bravecompany.api.android.stdapp.api.data.FreeExplainStudyResult;
import kr.co.bravecompany.modoogong.android.stdapp.config.AnalysisTags;
import kr.co.bravecompany.modoogong.android.stdapp.config.Tags;
import kr.co.bravecompany.modoogong.android.stdapp.manager.PropertyManager;
import kr.co.bravecompany.modoogong.android.stdapp.utils.BraveUtils;
import kr.co.bravecompany.modoogong.android.stdapp.utils.SystemUtils;
import kr.co.bravecompany.modoogong.android.stdapp.viewholder.OnItemClickListener;
import kr.co.bravecompany.modoogong.android.stdapp.data.FreeExplainStudyData;
import okhttp3.Request;

/**
 * Created by BraveCompany on 2016. 12. 16..
 */

public class FreeExplainStudyFragment extends BaseFragment {

    private int examClass = -1;

    private LinearLayout layoutDefault;

    private RecyclerView mListView;
    private FreeExplainStudyAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private int mListPage = 1;
    private int mTotalCount = 0;
    private boolean isLast = false;


    public static FreeExplainStudyFragment newInstance() {
        return new FreeExplainStudyFragment();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            examClass = args.getInt(Tags.TAG_EXAMCLASS, -1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_free_study, container, false);
        initLayout(rootView);
        initListener();
        initData();
        return rootView;
    }

    @Override
    protected void initLayout(ViewGroup rootView) {
        mAdapter = new FreeExplainStudyAdapter();
        mListView = (RecyclerView)rootView.findViewById(R.id.recyclerFreeStudy);
        mListView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(getContext());
        mListView.setLayoutManager(mLayoutManager);

        layoutDefault = (LinearLayout) rootView.findViewById(R.id.layoutDefault);
        TextView txtDefault = (TextView)layoutDefault.findViewById(R.id.txtDefault);
        txtDefault.setText(getString(R.string.no_free_explain_study));
    }

    @Override
    protected void initListener() {
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //play
                FreeExplainStudyItemVO item = mAdapter.getItem(position).getFreeExplainStudyItemVO();
                if(item != null){
                    playStudyWithDialog(item);
                }
            }
        });

        mListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (isLast && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    // more data...
                    if(mAdapter.isShowFooter()) {
                        loadData(++mListPage, examClass);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mAdapter.getItemCount() -1 <= mLayoutManager.findLastVisibleItemPosition()) {
                    isLast = true;
                } else {
                    isLast = false;
                }
            }
        });
    }

    @Override
    protected void setData(String data) {

    }

    @Override
    protected void initData(){
        mAdapter.setShowFooter(false);
        mAdapter.clear();
        mListPage = 1;
        mTotalCount = 0;
        loadData(mListPage, examClass);
    }

    private void loadData(int page, int examClass){
        if(!mAdapter.isShowFooter()) {
            startLoading();
        }
        StudyRequests.getInstance().requestFreeExplainStudyList(page, examClass, -1, new OnResultListener<FreeExplainStudyResult>(){

            @Override
            public void onSuccess(Request request, FreeExplainStudyResult result) {
                if(!mAdapter.isShowFooter()) {
                    stopLoading();
                }
                setData(result);
            }

            @Override
            public void onFail(Request request, Exception exception) {
                if(!mAdapter.isShowFooter()) {
                    stopLoading();
                }
                BraveUtils.showRequestOnFailToast(getActivity(), exception);

                if(exception != null && exception.getMessage() != null) {
                    Answers.getInstance().logCustom(new CustomEvent(AnalysisTags.REQUEST)
                            .putCustomAttribute(AnalysisTags.ERROR, AnalysisTags.EXPLAINSTUDY + ":: " + exception.getMessage()));
                }
            }
        });
    }

    private void setData(FreeExplainStudyResult result){
        if(result == null){
            return;
        }
        ArrayList<FreeExplainStudyData> items = new ArrayList<>();
        ArrayList<FreeExplainStudyItemVO> explains = result.getExplains();
        if(explains != null && explains.size() != 0){
            showDefault(false);

            for(int i=0; i<explains.size(); i++){
                FreeExplainStudyData explain = new FreeExplainStudyData();
                explain.setFreeExplainStudyItemVO(explains.get(i));
                items.add(explain);
            }
            mAdapter.addAll(items);

            if(mListPage == 1 && mLayoutManager.findFirstCompletelyVisibleItemPosition() != 0){
                mListView.scrollToPosition(0);
            }
        }else{
            if(mListPage == 1) {
                showDefault(true);
            }
        }

        mTotalCount = result.getTotalCount();
        mAdapter.setShowFooter(mAdapter.getRealItemCount() < mTotalCount);

        mListView.post(new Runnable() {
            @Override
            public void run() {
                if(!BraveUtils.isRecyclerScrollable(mListView) && mAdapter.isShowFooter()){
                    loadData(++mListPage, examClass);
                }
            }
        });
    }

    private void playStudyWithDialog(final FreeExplainStudyItemVO freeStudyItem){
        if(freeStudyItem != null) {
            if (SystemUtils.getConnectivityStatus(getContext()) == Tags.NETWORK_TYPE.NETWORK_MOBILE
                    && PropertyManager.getInstance().isNoticeData()) {
                String message = getString(R.string.dialog_no_wifi_play);
                BraveUtils.showAlertDialogOkCancel(getActivity(), message,
                        new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                playStudy(freeStudyItem);
                            }
                        }, null);
            } else {
                playStudy(freeStudyItem);
            }
        }
    }

    private void playStudy(FreeExplainStudyItemVO freeStudyItem){
        String name = BraveUtils.fromHTMLTitle(freeStudyItem.getExamName());
        String url = APIManager.getFreePlayUrl(getContext(), freeStudyItem.getVodKey());
        BraveUtils.goPlayerWithUrl(FreeExplainStudyFragment.this, name, url);

        Answers.getInstance().logCustom(new CustomEvent(AnalysisTags.EXPLAINSTUDY)
                .putCustomAttribute(AnalysisTags.ACTION, "play_study"));
    }

    private void showDefault(boolean show){
        if(show){
            layoutDefault.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
        }else{
            layoutDefault.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
        }
    }
}
