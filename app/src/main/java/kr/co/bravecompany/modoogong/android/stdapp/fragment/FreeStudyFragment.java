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
import kr.co.bravecompany.modoogong.android.stdapp.activity.MainActivity;
import kr.co.bravecompany.modoogong.android.stdapp.adapter.FreeStudyAdapter;
import kr.co.bravecompany.api.android.stdapp.api.OnResultListener;
import kr.co.bravecompany.api.android.stdapp.api.utils.APIManager;
import kr.co.bravecompany.api.android.stdapp.api.requests.StudyRequests;
import kr.co.bravecompany.api.android.stdapp.api.data.FreeStudyResult;
import kr.co.bravecompany.modoogong.android.stdapp.config.AnalysisTags;
import kr.co.bravecompany.modoogong.android.stdapp.config.Tags;
import kr.co.bravecompany.modoogong.android.stdapp.manager.PropertyManager;
import kr.co.bravecompany.modoogong.android.stdapp.utils.BraveUtils;
import kr.co.bravecompany.modoogong.android.stdapp.utils.SystemUtils;
import kr.co.bravecompany.modoogong.android.stdapp.viewholder.FreeStudyHeaderViewHolder;
import kr.co.bravecompany.modoogong.android.stdapp.viewholder.OnItemClickListener;
import kr.co.bravecompany.api.android.stdapp.api.data.FreeStudyItemVO;
import kr.co.bravecompany.modoogong.android.stdapp.data.FreeStudyData;
import okhttp3.Request;

/**
 * Created by BraveCompany on 2016. 10. 24..
 */

public class FreeStudyFragment extends BaseFragment {

    private int cate = -1;
    private String scate = null;
    private String bestFVod = null;

    private LinearLayout layoutDefault;

    private RecyclerView mListView;
    private FreeStudyAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private int mListPage = 1;
    private int mTotalCount = 0;
    private boolean isLast = false;


    public static FreeStudyFragment newInstance() {
        return new FreeStudyFragment();
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
            cate = args.getInt(Tags.TAG_CATE, -1);
            scate = args.getString(Tags.TAG_SCATE);
            bestFVod = args.getString(Tags.TAG_BESTFVOD);
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
        mAdapter = new FreeStudyAdapter();
        mListView = (RecyclerView)rootView.findViewById(R.id.recyclerFreeStudy);
        mListView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(getContext());
        mListView.setLayoutManager(mLayoutManager);

        layoutDefault = (LinearLayout) rootView.findViewById(R.id.layoutDefault);
        TextView txtDefault = (TextView)layoutDefault.findViewById(R.id.txtDefault);
        txtDefault.setText(getString(R.string.no_free_study));
    }

    @Override
    protected void initListener() {
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //play
                FreeStudyItemVO item = mAdapter.getItem(position).getFreeStudyItemVO();
                if(item != null){
                    playStudyWithDialog(item);
                }
            }
        });

        mAdapter.setOnItemClickListener(new FreeStudyHeaderViewHolder.OnHeaderItemClickListener() {
            @Override
            public void onItemClick(View view, FreeStudyData freeStudy) {
                //play
                FreeStudyItemVO item = freeStudy.getFreeStudyItemVO();
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
                        loadData(++mListPage, cate, scate);
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


        if (bestFVod != null) {
            FreeStudyItemVO bestItem = (FreeStudyItemVO) BraveUtils.toJsonString(bestFVod, FreeStudyItemVO.class);
            if (bestItem != null) {
                FreeStudyData studyHeader = new FreeStudyData();
                studyHeader.setFreeStudyItemVO(bestItem);
                mAdapter.setFreeStudyHeader(studyHeader);
            }
        }

        if(cate != -1) {
            loadData(mListPage, cate, scate);
        }
    }

    private void loadData(int page, int cate, String scate){
        if(!mAdapter.isShowFooter()) {
            startLoading();
        }
        StudyRequests.getInstance().requestFreeStudyList(page, cate, scate, -1, new OnResultListener<FreeStudyResult>(){

            @Override
            public void onSuccess(Request request, FreeStudyResult result) {
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
                            .putCustomAttribute(AnalysisTags.ERROR, AnalysisTags.FREESTUDY + ":: " + exception.getMessage()));
                }
            }
        });
    }

    ///* Lee : 닥터 보카 무료강의 크래쉬 오류 수정을 위한 코드(오리지널 버전)
    private void setData(FreeStudyResult result){
        if(result == null){
            return;
        }
        ArrayList<FreeStudyData> items = new ArrayList<>();
        ArrayList<FreeStudyItemVO> studies = result.getFvods();

        if(studies != null && studies.size() != 0){
            showDefault(false);

            if(mListPage == 1){
                if(mAdapter.getFreeStudyHeader() == null){
                    FreeStudyData studyHeader = new FreeStudyData();
                    studyHeader.setFreeStudyItemVO(studies.get(0));
                    mAdapter.setFreeStudyHeader(studyHeader);
                }

                //이유 없이 삭제
                //mAdapter.setFreeStudyDivider(((MainActivity) getActivity()).getToolbarTitle());
            }

            //items
            for(int i=0; i<studies.size(); i++){
                FreeStudyData study = new FreeStudyData();
                study.setFreeStudyItemVO(studies.get(i));
                items.add(study);
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
                    loadData(++mListPage, cate, scate);
                }
            }
        });
    }


    //디버거 버전!
    /*
    private void setData(FreeStudyResult result){
        if(result == null){
            return;
        }
        ArrayList<FreeStudyData> items = new ArrayList<>();
        ArrayList<FreeStudyItemVO> studies = result.getFvods();

        //for test
        // studies =  new ArrayList(studies.subList(0,1));

        if(studies != null && studies.size() != 0){
            showDefault(false);


            if(mListPage == 1){
                if(mAdapter.getFreeStudyHeader() == null){
                    FreeStudyData studyHeader = new FreeStudyData();
                    studyHeader.setFreeStudyItemVO(studies.get(0));
                    mAdapter.setFreeStudyHeader(studyHeader);
                }
                mAdapter.setFreeStudyDivider(((MainActivity) getActivity()).getToolbarTitle());
            }


            //items
            for(int i=0; i<studies.size(); i++){
                FreeStudyData study = new FreeStudyData();
                study.setFreeStudyItemVO(studies.get(i));
                items.add(study);
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
                    loadData(++mListPage, cate, scate);
                }
            }
        });
    }
    */

    private void playStudyWithDialog(final FreeStudyItemVO freeStudyItem){
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

    private void playStudy(FreeStudyItemVO freeStudyItem){
        String title = BraveUtils.fromHTMLTitle(freeStudyItem.getTitle());

        String url = APIManager.getFreePlayUrl(getContext(), freeStudyItem.getVideoKey());

        BraveUtils.goPlayerWithUrl(FreeStudyFragment.this, title, url);
        //BraveUtils.goPlayerWithUrl(FreeStudyFragment.this, title, "http://hello");

        Answers.getInstance().logCustom(new CustomEvent(AnalysisTags.FREESTUDY)
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
