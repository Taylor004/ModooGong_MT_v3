package kr.co.bravecompany.modoogong.android.stdapp.pageCS;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.gun0912.tedpermission.PermissionListener;

import java.util.ArrayList;

import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.modoogong.android.stdapp.activity.QADetailActivity;
import kr.co.bravecompany.modoogong.android.stdapp.adapter.QAAdapter;
import kr.co.bravecompany.api.android.stdapp.api.OnResultListener;
import kr.co.bravecompany.api.android.stdapp.api.requests.QARequests;
import kr.co.bravecompany.api.android.stdapp.api.data.OneToOneQAResult;
import kr.co.bravecompany.api.android.stdapp.api.data.StudyQAResult;
import kr.co.bravecompany.modoogong.android.stdapp.config.AnalysisTags;
import kr.co.bravecompany.modoogong.android.stdapp.config.RequestCode;
import kr.co.bravecompany.modoogong.android.stdapp.config.Tags;
import kr.co.bravecompany.modoogong.android.stdapp.fragment.BaseFragment;
import kr.co.bravecompany.modoogong.android.stdapp.fragment.OneToOneQAFragment;
import kr.co.bravecompany.modoogong.android.stdapp.fragment.StudyQAFragment;
import kr.co.bravecompany.modoogong.android.stdapp.utils.BraveUtils;
import kr.co.bravecompany.modoogong.android.stdapp.viewholder.OnItemClickListener;
import kr.co.bravecompany.api.android.stdapp.api.data.OneToOneQAItemVO;
import kr.co.bravecompany.api.android.stdapp.api.data.QAItemVO;
import kr.co.bravecompany.modoogong.android.stdapp.data.QAData;
import kr.co.bravecompany.api.android.stdapp.api.data.StudyQAItemVO;
import okhttp3.Request;

/**
 * Created by BraveCompany on 2016. 10. 26..
 */

public class QAListFragment extends BaseFragment {

    private int index = Tags.QA_TYPE.QA_ONE_TO_ONE;
    private FrameLayout contentCall;
    private TextView btnCall;

    private LinearLayout layoutDefault;

    private RecyclerView mListView;
    private int mCurrentVisibleItem;
    private QAAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private SwipeRefreshLayout refreshView;

    private int mListPage = 1;
    private int mTotalCount = 0;
    private boolean isLast = false;


    public static QAListFragment newInstance() {
        return new QAListFragment();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == getActivity().RESULT_OK){
            if(requestCode == RequestCode.REQUEST_QA_DETAIL){
                //add data
                if(index == Tags.QA_TYPE.QA_ONE_TO_ONE) {
                    ((OneToOneQAFragment) getParentFragment()).movePage(Tags.QA_PAGE_INDEX.QA_PAGE_Q_DO,
                            data.getStringExtra(Tags.TAG_QA_DATA));
                }else{
                    ((StudyQAFragment) getParentFragment()).movePage(Tags.QA_PAGE_INDEX.QA_PAGE_Q_DO,
                            data.getStringExtra(Tags.TAG_QA_DATA));
                }
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            index = args.getInt(Tags.TAG_QA, Tags.QA_TYPE.QA_ONE_TO_ONE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_qa_list, container, false);
        initLayout(rootView);
        initListener();
        initData();
        return rootView;
    }

    @Override
    protected void initLayout(ViewGroup rootView) {

        contentCall = (FrameLayout)rootView.findViewById(R.id.contentCall);
        btnCall = (TextView)rootView.findViewById(R.id.btnCall);
        if(index == Tags.QA_TYPE.QA_ONE_TO_ONE){
            contentCall.setVisibility(View.VISIBLE);
        }else{
            contentCall.setVisibility(View.GONE);
        }

        mAdapter = new QAAdapter();
        mListView = (RecyclerView)rootView.findViewById(R.id.recyclerQAList);
        mListView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(getContext());
        mListView.setLayoutManager(mLayoutManager);

        refreshView = (SwipeRefreshLayout)rootView.findViewById(R.id.refreshView);
        int color = getResources().getColor(R.color.colorPrimary);
        refreshView.setColorSchemeColors(color, color, color, color);

        layoutDefault = (LinearLayout) rootView.findViewById(R.id.layoutDefault);
        TextView txtDefault = (TextView)layoutDefault.findViewById(R.id.txtDefault);
        String msg = null;
        if(index == Tags.QA_TYPE.QA_ONE_TO_ONE){
            msg = getString(R.string.no_one_to_one_qa);
        }else{
            msg = getString(R.string.no_study_qa);
        }
        txtDefault.setText(msg);
    }

    @Override
    protected void initListener() {
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                QAItemVO item = mAdapter.getItem(position).getQaItemVO();
                int qaNo;
                if(index == Tags.QA_TYPE.QA_ONE_TO_ONE){
                    qaNo = ((OneToOneQAItemVO)item).getQnaNo();
                }else{
                    qaNo = ((StudyQAItemVO)item).getInquiryNo();
                }
                Intent intent = new Intent(getActivity(), QADetailActivity.class);
                intent.putExtra(Tags.TAG_QA_TYPE, index);
                intent.putExtra(Tags.TAG_QA_NO, qaNo);
                startActivityForResult(intent, RequestCode.REQUEST_QA_DETAIL);
            }
        });

        mListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!refreshView.isRefreshing() && isLast && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    // more data...
                    if(mAdapter.isShowFooter()){
                        loadData(++mListPage);
                    }
                }
                /*
                if(index == Tags.QA_TYPE.QA_ONE_TO_ONE && recyclerView.getChildAt(0) != null) {
                    int firstComplete = mLayoutManager.findFirstCompletelyVisibleItemPosition();
                    if(firstComplete == 0 && newState == RecyclerView.SCROLL_STATE_SETTLING){
                        BraveUtils.setVisibilityBottomView(contentCall, View.VISIBLE);
                    }else if(newState == RecyclerView.SCROLL_STATE_DRAGGING){
                        BraveUtils.setVisibilityBottomView(contentCall, View.GONE);
                    }
                }
                */
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

        refreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });

        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //BraveUtils.goCall(getActivity(), getString(R.string.ono_to_ono_qa_call_uri));
                BraveUtils.checkPermission(getActivity(), mCallPermissionListener,
                        getString(R.string.check_permission_call_phone),
                        Manifest.permission.CALL_PHONE);
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
        loadData(mListPage);
    }

    private void loadData(int page){
        if(index == Tags.QA_TYPE.QA_ONE_TO_ONE){
            if(!refreshView.isRefreshing() && !mAdapter.isShowFooter()) {
                startLoading();
            }
            QARequests.getInstance().requestOneToOneQAList(page, -1, new OnResultListener<OneToOneQAResult>(){

                @Override
                public void onSuccess(Request request, OneToOneQAResult result) {
                    if(!refreshView.isRefreshing() && !mAdapter.isShowFooter()) {
                        stopLoading();
                    }else{
                        if(refreshView.isRefreshing()) {
                            refreshView.setRefreshing(false);
                        }
                    }
                    setData(result);
                }

                @Override
                public void onFail(Request request, Exception exception) {
                    if(!refreshView.isRefreshing() && !mAdapter.isShowFooter()) {
                        stopLoading();
                    }else{
                        if(refreshView.isRefreshing()) {
                            refreshView.setRefreshing(false);
                        }
                    }
                    BraveUtils.showToast(getActivity(), getString(R.string.toast_common_network_fail));
                }
            });
        }else{
            if(!refreshView.isRefreshing() && !mAdapter.isShowFooter()) {
                startLoading();
            }
            QARequests.getInstance().requestStudyQAList(page, -1, new OnResultListener<StudyQAResult>(){

                @Override
                public void onSuccess(Request request, StudyQAResult result) {
                    if(!refreshView.isRefreshing() && !mAdapter.isShowFooter()) {
                        stopLoading();
                    }else{
                        if(refreshView.isRefreshing()) {
                            refreshView.setRefreshing(false);
                        }
                    }
                    setData(result);
                }

                @Override
                public void onFail(Request request, Exception exception) {
                    if(!refreshView.isRefreshing() && !mAdapter.isShowFooter()) {
                        stopLoading();
                    }else{
                        if(refreshView.isRefreshing()) {
                            refreshView.setRefreshing(false);
                        }
                    }
                    BraveUtils.showToast(getActivity(), getString(R.string.toast_common_network_fail));
                }
            });
        }
    }

    private void setData(OneToOneQAResult result) {
        if(result == null){
            return;
        }
        ArrayList<QAData> items = new ArrayList<>();
        ArrayList<OneToOneQAItemVO> qas = result.getQnas();
        if(qas != null && qas.size() != 0){
            showDefault(false);

            for(int i=0; i<qas.size(); i++){
                QAData qa = new QAData();
                qa.setQaItemVO(qas.get(i));
                items.add(qa);
            }
            mAdapter.addAll(items);
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
                    loadData(++mListPage);
                }
            }
        });
    }

    private void setData(StudyQAResult result) {
        if(result == null){
            return;
        }
        ArrayList<QAData> items = new ArrayList<>();
        ArrayList<StudyQAItemVO> qas = result.getInquiries();
        if(qas != null && qas.size() != 0){
            showDefault(false);

            for(int i=0; i<qas.size(); i++){
                QAData qa = new QAData();
                qa.setQaItemVO(qas.get(i));
                items.add(qa);
            }
            mAdapter.addAll(items);
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
                    loadData(++mListPage);
                }
            }
        });
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

    // =============================================================================
    // Check Permission
    // =============================================================================

    private PermissionListener mCallPermissionListener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            if(BraveUtils.checkTelephony(getActivity())) {
                BraveUtils.goCall(getActivity(), getString(R.string.one_to_one_qa_call_uri));

                Answers.getInstance().logCustom(new CustomEvent(AnalysisTags.QALIST)
                        .putCustomAttribute(AnalysisTags.ACTION, "go_call"));
            }
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            //do nothing
        }
    };
}