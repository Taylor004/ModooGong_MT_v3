package kr.co.bravecompany.modoogong.android.stdapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import kr.co.bravecompany.api.android.stdapp.api.OnResultListener;
import kr.co.bravecompany.api.android.stdapp.api.data.StudyFileItemVO;
import kr.co.bravecompany.api.android.stdapp.api.data.StudyFileResult;
import kr.co.bravecompany.api.android.stdapp.api.requests.StudyFileRequests;
import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.modoogong.android.stdapp.activity.StudyFileDetailActivity;
import kr.co.bravecompany.modoogong.android.stdapp.pageStudy.StudyFileAdapter;
import kr.co.bravecompany.modoogong.android.stdapp.config.Tags;
import kr.co.bravecompany.modoogong.android.stdapp.data.StudyFileData;
import kr.co.bravecompany.modoogong.android.stdapp.utils.BraveUtils;
import kr.co.bravecompany.modoogong.android.stdapp.viewholder.OnItemClickListener;
import okhttp3.Request;

/**
 * Created by BraveCompany on 2017. 10. 19..
 */

public class StudyFileFragment extends BaseFragment {

    private RecyclerView mListView;
    private StudyFileAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private View layoutDefault;

    //header
    private TextView txtStudyFileCount;

    private int mListPage = 1;
    private boolean isLast = false;


    public static StudyFileFragment newInstance() {
        return new StudyFileFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_study_file, container, false);
        initLayout(rootView);
        initListener();
        initData();
        return rootView;
    }

    @Override
    protected void initLayout(ViewGroup rootView) {
        mAdapter = new StudyFileAdapter();
        mListView = (RecyclerView)rootView.findViewById(R.id.recyclerStudyFile);
        mListView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(getContext());
        mListView.setLayoutManager(mLayoutManager);

        layoutDefault = rootView.findViewById(R.id.layoutDefault);

        //header
        txtStudyFileCount = (TextView)rootView.findViewById(R.id.txtStudyFileCount);
    }

    @Override
    protected void initListener() {
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                StudyFileItemVO studyFileItemVO = mAdapter.getItem(position).getStudyFileItemVO();
                if(studyFileItemVO != null) {
                    Intent intent = new Intent(getActivity(), StudyFileDetailActivity.class);
                    intent.putExtra(Tags.TAG_STUDY_FILE_NO, studyFileItemVO.getNo());
                    startActivity(intent);
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
                        loadData(++mListPage);
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
    protected void initData() {
        mAdapter.setShowFooter(false);
        mAdapter.clear();
        mListPage = 1;
        loadData(mListPage);
    }

    private void loadData(int page){
        if(!mAdapter.isShowFooter()) {
            startLoading();
        }
        StudyFileRequests.getInstance().requestStudyFileList(page, new OnResultListener<StudyFileResult>() {
            @Override
            public void onSuccess(Request request, StudyFileResult result) {
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
            }
        });
    }

    private void setData(StudyFileResult result) {
        if (result == null) {
            return;
        }
        int start = result.getTotal_rows() - (mListPage - 1) * 10;
        ArrayList<StudyFileData> headers = new ArrayList<>();
        ArrayList<StudyFileData> items = new ArrayList<>();
        ArrayList<StudyFileItemVO> studyFiles = result.getBoard_list();
        if(studyFiles != null && studyFiles.size() != 0){
            for(StudyFileItemVO item : studyFiles){
                StudyFileData studyFile = new StudyFileData(item);
                if(item.getNoti_yn() != null && item.getNoti_yn().equals("y")){
                    studyFile.setType(Tags.STUDY_FILE_VIEW_TYPE.STUDY_FILE_HEADER);
                    headers.add(studyFile);
                }else{
                    items.add(studyFile);
                }
            }

            for(int i=0; i<items.size(); i++){
                StudyFileData studyFile = items.get(i);
                studyFile.setNumber(start - i);
            }
            items.addAll(0, headers);
            mAdapter.addAll(items);

            if(mListPage == 1 && mLayoutManager.findFirstCompletelyVisibleItemPosition() != 0){
                mListView.scrollToPosition(0);
            }
        }else{
            if(mListPage == 1){
                showDefault(true);
            }
        }

        txtStudyFileCount.setText(String.format(getString(R.string.study_file_menu_count), result.getTotal_rows()));

        String isLast = result.getIs_last_pg();
        mAdapter.setShowFooter(!(isLast != null && isLast.equals("Y")));

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
}
