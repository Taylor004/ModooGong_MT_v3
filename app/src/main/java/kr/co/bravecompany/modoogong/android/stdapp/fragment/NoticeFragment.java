package kr.co.bravecompany.modoogong.android.stdapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.modoogong.android.stdapp.activity.MainActivity;
import kr.co.bravecompany.modoogong.android.stdapp.activity.NoticeDetailActivity;
import kr.co.bravecompany.modoogong.android.stdapp.adapter.NoticeAdapter;
import kr.co.bravecompany.api.android.stdapp.api.OnResultListener;
import kr.co.bravecompany.api.android.stdapp.api.requests.NoticeRequests;
import kr.co.bravecompany.api.android.stdapp.api.data.NoticeResult;
import kr.co.bravecompany.modoogong.android.stdapp.config.Tags;
import kr.co.bravecompany.modoogong.android.stdapp.utils.BraveUtils;
import kr.co.bravecompany.modoogong.android.stdapp.viewholder.OnItemClickListener;
import kr.co.bravecompany.api.android.stdapp.api.data.NoticeItemVO;
import kr.co.bravecompany.modoogong.android.stdapp.data.NoticeData;
import okhttp3.Request;

/**
 * Created by BraveCompany on 2016. 10. 14..
 */

public class NoticeFragment extends BaseFragment {

    private LinearLayout layoutDefault;

    private RecyclerView mListView;
    private NoticeAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private int mListPage = 1;
    private int mTotalCount = 0;
    private boolean isLast = false;

    public static NoticeFragment newInstance() {
        return new NoticeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_notice, container, false);
        initLayout(rootView);
        initListener();
        initData();
        return rootView;
    }

    @Override
    protected void initLayout(ViewGroup rootView) {
        mAdapter = new NoticeAdapter();
        mListView = (RecyclerView)rootView.findViewById(R.id.recyclerNotice);
        mListView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(getContext());
        mListView.setLayoutManager(mLayoutManager);

        layoutDefault = (LinearLayout) rootView.findViewById(R.id.layoutDefault);
        TextView txtDefault = (TextView)layoutDefault.findViewById(R.id.txtDefault);
        txtDefault.setText(getString(R.string.no_notice));

        ((MainActivity) getActivity()).setToolbarAuxButton(0);
    }

    @Override
    protected void initListener() {
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                NoticeItemVO noticeItemVO = mAdapter.getItem(position).getNoticeItemVO();
                if(noticeItemVO != null) {
                    Intent intent = new Intent(getActivity(), NoticeDetailActivity.class);
                    intent.putExtra(Tags.TAG_NOTICE_NO, noticeItemVO.getNoticeNo());
                    startActivity(intent);
                }
            }
        });

        mListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                /*
                if (isLast && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    // more data...
                    if(mAdapter.getItemCount() < mTotalCount) {
                        loadData(++mListPage);
                    }else{
                        BraveUtils.showToast(getActivity(), getString(R.string.toast_common_last));
                    }
                }
                */
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                /*
                if (mAdapter.getItemCount() -1 <= mLayoutManager.findLastVisibleItemPosition()) {
                    isLast = true;
                } else {
                    isLast = false;
                }
                */
            }
        });
    }

    @Override
    protected void setData(String data) {

    }

    @Override
    protected void initData(){
        mAdapter.clear();
        mListPage = 1;
        mTotalCount = 0;
        loadData(mListPage);
    }

    private void loadData(int page){
        startLoading();
        NoticeRequests.getInstance().requestNoticeList(page, 1000, new OnResultListener<NoticeResult>(){

            @Override
            public void onSuccess(Request request, NoticeResult result) {
                stopLoading();
                setData(result);
            }

            @Override
            public void onFail(Request request, Exception exception) {
                stopLoading();
                BraveUtils.showToast(getActivity(), getString(R.string.toast_common_network_fail));
            }
        });
    }

    private void setData(NoticeResult result) {
        if(result == null){
            return;
        }
        ArrayList<NoticeData> items = new ArrayList<>();
        ArrayList<NoticeItemVO> notices = result.getNotices();
        if(notices != null && notices.size() != 0){
            showDefault(false);

            for(int i=0; i<notices.size(); i++){
                NoticeData notice = new NoticeData();
                NoticeItemVO n = notices.get(i);
                if(n.getGb() != null && n.getGb().equals("top")){
                    if(mListPage > 1){
                        continue;
                    }else{
                        n.setType(Tags.NOTICE_TYPE_CD.NOTICE_FIRST);
                    }
                }
                /*
                if(mListPage > 1 && n.getGb().equals("top")){
                    continue;
                }
                */
                notice.setNew(BraveUtils.isNewBoard(n.getWriteDate()));
                notice.setNoticeItemVO(n);
                items.add(notice);
            }
            mAdapter.addAll(items);
        }else{
            if(mListPage == 1) {
                showDefault(true);
            }
        }

        mTotalCount = result.getTotalCount();
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
