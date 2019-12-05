package kr.co.bravecompany.modoogong.android.stdapp.pageLectureList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.Spinner;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kr.co.bravecompany.api.android.stdapp.api.data.Packet;
import kr.co.bravecompany.api.android.stdapp.api.requests.SubscribeLectureRequests;
import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.modoogong.android.stdapp.activity.LectureSubscribeActivity;
import kr.co.bravecompany.modoogong.android.stdapp.activity.MainActivity;
import kr.co.bravecompany.modoogong.android.stdapp.db.DataManager;
import kr.co.bravecompany.modoogong.android.stdapp.db.StudyDataManager;
import kr.co.bravecompany.modoogong.android.stdapp.db.model.Lecture;
import kr.co.bravecompany.modoogong.android.stdapp.pageStudy.StudyActivity;
import kr.co.bravecompany.api.android.stdapp.api.OnResultListener;
import kr.co.bravecompany.api.android.stdapp.api.config.APIConfig;
import kr.co.bravecompany.api.android.stdapp.api.requests.StudyRequests;
import kr.co.bravecompany.api.android.stdapp.api.data.LectureResult;
import kr.co.bravecompany.modoogong.android.stdapp.config.AnalysisTags;
import kr.co.bravecompany.modoogong.android.stdapp.config.RequestCode;
import kr.co.bravecompany.modoogong.android.stdapp.config.Tags;
import kr.co.bravecompany.modoogong.android.stdapp.download.OnDownloadBindListener;
import kr.co.bravecompany.modoogong.android.stdapp.fragment.DownBaseFragment;
import kr.co.bravecompany.modoogong.android.stdapp.utils.BraveUtils;
import kr.co.bravecompany.modoogong.android.stdapp.utils.log;
import kr.co.bravecompany.modoogong.android.stdapp.view.AnimatedExpandableListView;
import kr.co.bravecompany.modoogong.android.stdapp.viewholder.OnItemClickListener;
import kr.co.bravecompany.api.android.stdapp.api.data.LectureItemVO;
import kr.co.bravecompany.modoogong.android.stdapp.data.LectureData;
import okhttp3.Request;


/**
 * Created by BraveCompany on 2016. 10. 12..
 */

public class LectureFragment extends DownBaseFragment implements MainActivity.OnHomeActivityEventListener {

    private AnimatedExpandableListView elvLecture;
    private LectureMenuAdapter mLectureMenuAdapter;

    private View layoutDefaultING;
    private View layoutDefaultEND;

    private RecyclerView mListView;
    private LectureAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    ListViewSwipeController listViewSwipeController = null;

    private SwipeRefreshLayout refreshView;


    LectureFilterWidget.CourseFilterWidget mCourseFilter;
    LectureFilterWidget.TeacherFilterWidget mTeacherFilter;


    private int mListPage = 1;
    private int mTotalCount = 0;
    private boolean isLast = false;
    //private static int firstVisibleInListView;
    private FloatingActionButton mfloatingActionButton; // 내강의실에서 수강신청을 하기 위한 플로팅 버튼 선언.[2019.11.06 테일러]

    public static LectureFragment newInstance() {
        return new LectureFragment();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == getActivity().RESULT_OK) {
            if (requestCode == RequestCode.REQUEST_STUDY) {
                String refresh = data.getStringExtra(Tags.TAG_REFRESH);
                LectureItemVO lecture = (LectureItemVO)BraveUtils.toJsonString(refresh, LectureItemVO.class);

                if(lecture != null) {
                    refreshIngData(lecture);
                }
            }
            else if(requestCode == RequestCode.REQUEST_REG_LECTURE) {

                mCourseFilter.initSelection();
                mTeacherFilter.initSelection();

                initData();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_lecture, container, false);
        initLayout(rootView);
        initListener();
        setupLectureListView();

        //initData();

        return rootView;
    }

    @Override
    public void refreshFragment()
    {
        super.refreshFragment();
        ((MainActivity)getActivity()).setToolbarAuxButton(2);
    }

    @Override
    public void onPause() {
        super.onPause();
        if(elvLecture.isGroupExpanded(Tags.LECTURE_MENU_GROUP_INDEX.LECTURE_TYPE)){
            elvLecture.collapseGroup(Tags.LECTURE_MENU_GROUP_INDEX.LECTURE_TYPE);
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    protected void initLayout(ViewGroup rootView) {
        super.initLayout(rootView);

        //Note. 강좌추가 메뉴 표시
        ((MainActivity)getActivity()).setToolbarAuxButton(2);


        elvLecture = (AnimatedExpandableListView)rootView.findViewById(R.id.elvLecture);
        mLectureMenuAdapter = new LectureMenuAdapter();
        elvLecture.setAdapter(mLectureMenuAdapter);

        List<String> lecture_menus = Arrays.asList(getResources().getStringArray(R.array.lecture_menu_text));
        for(int i=0; i<lecture_menus.size(); i++){
            mLectureMenuAdapter.put(Tags.LECTURE_MENU_GROUP_INDEX.LECTURE_TYPE, lecture_menus.get(i));
        }
        mLectureMenuAdapter.setLectureCount(Tags.LECTURE_MENU_GROUP_INDEX.LECTURE_TYPE, 0);

        mAdapter = new LectureAdapter();
        mListView = (RecyclerView)rootView.findViewById(R.id.recyclerLecture);
        mListView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(getContext());
        mListView.setLayoutManager(mLayoutManager);

        refreshView = (SwipeRefreshLayout)rootView.findViewById(R.id.refreshView);
        int color = getResources().getColor(R.color.colorPrimary);
        refreshView.setColorSchemeColors(color, color, color, color);

        layoutDefaultING = rootView.findViewById(R.id.layoutDefaultING);
        layoutDefaultEND = rootView.findViewById(R.id.layoutDefaultEND);

        //firstVisibleInListView = mLayoutManager.findFirstVisibleItemPosition();
        mfloatingActionButton = (FloatingActionButton)rootView.findViewById(R.id.openLectureSubscribe);
        mfloatingActionButton.setOnClickListener(mFabClickListener);

        mTeacherFilter = new LectureFilterWidget.TeacherFilterWidget((Spinner) rootView.findViewById(R.id.filterTeacherList),
                new LectureFilterWidget.OnFilterChangedListener() {
                    @Override
                    public void OnFilterChanged() {
                        int selected = mLectureMenuAdapter.getSelectedChildIndex(Tags.LECTURE_MENU_GROUP_INDEX.LECTURE_TYPE);
                        if(selected != -1){
                            initData(selected);
                        }
                    }
                });

        mCourseFilter = new LectureFilterWidget.CourseFilterWidget((Spinner) rootView.findViewById(R.id.filterCourseList),
                new LectureFilterWidget.OnFilterChangedListener() {
                    @Override
                    public void OnFilterChanged() {


                        int selected = mLectureMenuAdapter.getSelectedChildIndex(Tags.LECTURE_MENU_GROUP_INDEX.LECTURE_TYPE);
                        if(selected != -1){
                            initData(selected);
                        }

                        mTeacherFilter.initTeacherListByCourse(mCourseFilter.selectedCD);
                    }
                });

    }

    @Override
    protected void initListener() {
        super.initListener();

        elvLecture.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                initData(childPosition);
                elvLecture.collapseGroupWithAnimation(groupPosition);

//                mCourseFilter.initSelection();
//                mTeacherFilter.initSelection();

                return true;
            }
        });


        elvLecture.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
            }
        });
        elvLecture.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                setElvLectureLayout(false);
            }
        });


        elvLecture.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, final int groupPosition, long id) {
                if(elvLecture.isGroupExpanded(groupPosition)){
                    elvLecture.collapseGroupWithAnimation(groupPosition);
                }else{
                    setElvLectureLayout(true);
                    elvLecture.post(new Runnable(){
                        @Override
                        public void run() {
                            elvLecture.expandGroupWithAnimation(groupPosition);
                        }
                    });
                }
                return true;
            }
        });

        mAdapter.setOnItemClickListener(

                new OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        LectureData lecture = mAdapter.getItem(position);
                        LectureItemVO item = lecture.getLectureItemVO();
                        String studyState = item.getStudyState();

                        mAdapter.resetNewmark(position);


                        if (studyState != null) {
                            if (BraveUtils.checkPrevLecture(studyState)) {
                                BraveUtils.showToast(getActivity(), getString(R.string.toast_lecture_prev));
                            } else if (BraveUtils.checkStopLecture(studyState)) {
                                BraveUtils.showToast(getActivity(), getString(R.string.toast_lecture_stop));
                            } else {
                                Intent intent = new Intent(getActivity(), StudyActivity.class);
                                intent.putExtra(Tags.TAG_LECTURE, BraveUtils.toJson(item));
                                startActivityForResult(intent, RequestCode.REQUEST_STUDY);
                            }
                        }
                    }
                });


        mListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                /*
                if (!refreshView.isRefreshing() && isLast && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    // more data...
                    if(mAdapter.getItemCount() < mTotalCount) {
                        int selectedCD = mLectureMenuAdapter.getSelectedChildIndex(Tags.LECTURE_MENU_GROUP_INDEX.LECTURE_TYPE);
                        loadData(++mListPage, selectedCD);
                    }else{
                        BraveUtils.showToast(getActivity(), getString(R.string.toast_common_last));
                    }
                }
                */

                //스크롤 상태가 멈춰 있다면
//                if(newState == RecyclerView.SCROLL_STATE_IDLE)
//                {
//                    mfloatingActionButton.show();
//                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // scroll up [2019.11.06 테일러]
                if(dy > 0)
                {
                    log.d("testscroll","upupup!!");
                    mfloatingActionButton.hide();
                }
                // scroll down // scroll up [2019.11.06 테일러]
                else
                {
                    log.d("testscroll","downdown!!");
                    mfloatingActionButton.show();
                }

                /*
                if (mAdapter.getItemCount() -1 <= mLayoutManager.findLastVisibleItemPosition()) {
                    isLast = true;
                } else {
                    isLast = false;
                }
                */

            }
        });

        refreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                int selected = mLectureMenuAdapter.getSelectedChildIndex(Tags.LECTURE_MENU_GROUP_INDEX.LECTURE_TYPE);
                if(selected != -1){
                    initData(selected);
                }

                Answers.getInstance().logCustom(new CustomEvent(AnalysisTags.LECTURE)
                        .putCustomAttribute(AnalysisTags.ACTION, "refresh"));
            }
        });




        if(mDownloadManager != null){
            mDownloadManager.setOnDownloadBindListener(new OnDownloadBindListener() {
                @Override
                public void onBindComplete() {
                    initData();
                }
            });
        }
    }


    private View.OnClickListener mFabClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            int id = v.getId();

            switch (id) {
                case R.id.openLectureSubscribe:
                    Intent intent = new Intent(getActivity().getApplicationContext(), LectureSubscribeActivity.class); //현재 Fragment에서 데이터를 LectureSubscribeActivity로 보낼 Intent
                    startActivity(intent);
                    break;

            }

        }
    };

    private void setupLectureListView()
    {
        listViewSwipeController = new ListViewSwipeController(getContext(), new ListViewSwipeControllerActions() {
            @Override
            public void onRightClicked(int position) {
                onConfirmDeleteItem(position);
            }

            @Override
            public void onLeftClicked(int position)
            {

            }
        });

        listViewSwipeController.SetRightButton(Color.RED, R.drawable.ic_list_delete, null);

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(listViewSwipeController);
        itemTouchhelper.attachToRecyclerView(mListView);

        mListView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                listViewSwipeController.onDraw(c);
            }
        });
    }

    @Override
    protected void initData() {
        initData(Tags.LECTURE_MENU_INDEX.LECTURE_ING);
    }

    @Override
    protected void setData(String data) {

    }

    private void initData(int index){
        mAdapter.clear();
        mListPage = 1;
        mTotalCount = 0;
        loadData(mListPage, index);
    }

    private void loadData(int page, int index){
        CustomEvent event = new CustomEvent(AnalysisTags.LECTURE);
        String action;
        final String state;
        if(index == Tags.LECTURE_MENU_INDEX.LECTURE_ING){
            state = APIConfig.LECTURE_STATE_TYPE.LECTURE_ING_ITEM;

            action = "lecture_ing";
        }else{
            state = APIConfig.LECTURE_STATE_TYPE.LECTURE_END_ITEM;

            action = "lecture_end";
        }
        event.putCustomAttribute(AnalysisTags.ACTION, action);
        Answers.getInstance().logCustom(event);

        if(!refreshView.isRefreshing()){
            startLoading();
        }

        int perPage = 1000;
        StudyRequests.getInstance().requestLectureList(page, state, perPage, new OnResultListener<LectureResult>(){

            @Override
            public void onSuccess(Request request, LectureResult result) {
                if(!refreshView.isRefreshing()){
                    stopLoading();
                }else{
                    refreshView.setRefreshing(false);
                }
                if(state.equals(APIConfig.LECTURE_STATE_TYPE.LECTURE_ING_ITEM)){
                    setIngData(result);
                }else{
                    setEndData(result);
                }
            }

            @Override
            public void onFail(Request request, Exception exception) {
                if(!refreshView.isRefreshing()){
                    stopLoading();
                }else{
                    refreshView.setRefreshing(false);
                }
                BraveUtils.showRequestOnFailToast(getActivity(), exception);

                if(exception != null && exception.getMessage() != null) {
                    Answers.getInstance().logCustom(new CustomEvent(AnalysisTags.REQUEST)
                            .putCustomAttribute(AnalysisTags.ERROR, AnalysisTags.LECTURE + ":: " + exception.getMessage()));
                }
            }
        });
    }

    private void refreshIngData(LectureItemVO lecture){
        mAdapter.refresh(lecture);
    }

    private void setIngData(LectureResult result) {
        if(result == null){
            return;
        }

        mLectureMenuAdapter.setSelectChild(Tags.LECTURE_MENU_GROUP_INDEX.LECTURE_TYPE,
                Tags.LECTURE_MENU_INDEX.LECTURE_ING);

        ArrayList<LectureData> items = new ArrayList<>();
        ArrayList<LectureItemVO> lectures = result.getStudies();


        if(lectures != null && lectures.size() != 0){
            showDefault(false, Tags.LECTURE_MENU_INDEX.LECTURE_ING);

            for(int i=0; i<lectures.size(); i++){

                LectureItemVO vo= lectures.get(i);

                boolean cPass= mCourseFilter.isVisible( String.valueOf(vo.getSubjectCode()));
                boolean tPass= mTeacherFilter.isVisible(String.valueOf(vo.getTeacherCode()));


                if(tPass && cPass) {
                    LectureData lecture = new LectureData();
                    lecture.setType(Tags.LECTURE_VIEW_TYPE.LECTURE_ING_ITEM);
                    lecture.setLectureItemVO(lectures.get(i));

                    items.add(lecture);
                }
            }

            mAdapter.clear();
            mAdapter.addAll(items);

        }else{
            if(mListPage == 1) {
                showDefault(true, Tags.LECTURE_MENU_INDEX.LECTURE_ING);
            }
        }

        //mTotalCount = result.getTotalCount();
        mTotalCount = mAdapter.getItemCount();
        mLectureMenuAdapter.setLectureCount(Tags.LECTURE_MENU_GROUP_INDEX.LECTURE_TYPE,
                mTotalCount);

        if(mDownloadManager != null){
            mDownloadManager.updateDownloadLecture(lectures);
            updateDownloadViews();
        }

        StudyDataManager.getInstance().storeLectureDataIngList(result);
    }

    private void setEndData(LectureResult result) {
        if(result == null){
            return;
        }
        mLectureMenuAdapter.setSelectChild(Tags.LECTURE_MENU_GROUP_INDEX.LECTURE_TYPE,
                Tags.LECTURE_MENU_INDEX.LECTURE_END);

        ArrayList<LectureData> items = new ArrayList<>();
        ArrayList<LectureItemVO> lectures = result.getStudies();
        if(lectures != null && lectures.size() != 0){
            showDefault(false, Tags.LECTURE_MENU_INDEX.LECTURE_END);

            for(int i=0; i<lectures.size(); i++){

                LectureItemVO vo = lectures.get(i);
                boolean cPass= mCourseFilter.isVisible( String.valueOf(vo.getSubjectCode()));
                boolean tPass= mTeacherFilter.isVisible(String.valueOf(vo.getTeacherCode()));

                if(tPass && cPass) {

                    LectureData lecture = new LectureData();
                    lecture.setType(Tags.LECTURE_VIEW_TYPE.LECTURE_END_ITEM);
                    lecture.setLectureItemVO(lectures.get(i));
                    items.add(lecture);
                }
            }

            mAdapter.clear();
            mAdapter.addAll(items);

        }
        else {
            if(mListPage == 1)
            {
                showDefault(true, Tags.LECTURE_MENU_INDEX.LECTURE_END);
            }
        }

        //mTotalCount = result.getTotalCount();
        mTotalCount = mAdapter.getItemCount();
        mLectureMenuAdapter.setLectureCount(Tags.LECTURE_MENU_GROUP_INDEX.LECTURE_TYPE,
                mTotalCount);


        StudyDataManager.getInstance().storeLectureDataEndList(result);
    }

    private void setElvLectureLayout(boolean goExpanding){
        FrameLayout.LayoutParams params = null;
        if(goExpanding){
            params = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        }else{
            params = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        }
        elvLecture.setLayoutParams(params);
    }

    private void showDefault(boolean show, int index){
        if(show){
            if(index == Tags.LECTURE_MENU_INDEX.LECTURE_ING) {
                layoutDefaultING.setVisibility(View.VISIBLE);
                layoutDefaultEND.setVisibility(View.GONE);
            }else{
                layoutDefaultING.setVisibility(View.GONE);
                layoutDefaultEND.setVisibility(View.VISIBLE);
            }
            mListView.setVisibility(View.GONE);
        }else{
            layoutDefaultING.setVisibility(View.GONE);
            layoutDefaultEND.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void updateDownloadViews(String studyKey, final int state, int percent, final int errorCode){

        if(studyKey == null){
            return;
        }
        if(isResumed) {
            log.d(String.format("LectureFragment updateDownloadViews - studyKey: %s, state: %d, percent: %d, errorCode: %d", studyKey, state, percent, errorCode));
        }
        super.updateDownloadViews(studyKey, state, percent, errorCode);
    }


    void onConfirmDeleteItem(final int position)
    {
        LectureData lectureData= mAdapter.getItem(position);
        final String lecName = lectureData.getLectureItemVO().getLectureName();
        String title = String.format("%s", lecName);
        String msg = "본 강좌의 다운로드된 강의도 삭제됩니다\n강좌 신청 메뉴에서 다시 신청할 수 있습니다.";

        new AlertDialog.Builder(this.getContext())
                .setTitle(title)
                .setIcon(R.drawable.ic_list_black_delete) // 흰색 바탕에 사용 휴지통 아이콘 변경
                .setMessage(msg)
                .setPositiveButton("삭제", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton)
                    {
                        requestDeleteLecture(mAdapter.getItem(position), position);
                    }})

                .setNegativeButton("취소", null).show();
    }

    void requestDeleteLecture(final LectureData lectureItemVO, final int position)
    {
        int lectureNo = lectureItemVO.getLectureItemVO().getStudyLectureNo();
        //  List<String> selected = new ArrayList<String>();

        SubscribeLectureRequests.getInstance().requestUnsubscribeLectureList(String.valueOf(lectureNo),
                new OnResultListener<Packet.ResUnsubscribeLecture> (){

            @Override
            public void onSuccess(Request request, Packet.ResUnsubscribeLecture result) {
                if (result.resultCode == 0) {

                    String msg = String.format("\"%s\" 강좌를 삭제 하였습니다.", lectureItemVO.getLectureItemVO().getLectureName());
                    BraveUtils.showToast(getContext(), msg);

                    mAdapter.remove(lectureItemVO,position);

                    int count= mAdapter.getItemCount();
                    mLectureMenuAdapter.setLectureCount(Tags.LECTURE_MENU_GROUP_INDEX.LECTURE_TYPE, count);

                    StudyDataManager.getInstance().removedLectureDataIngList(lectureItemVO.getLectureItemVO());

                    //! 다운로드 중이거나 다운로드 된 강의를 삭제한다.
                    int lectureNo = lectureItemVO.getLectureItemVO().getStudyLectureNo();
                    mDownloadManager.removeDownloadLecture(lectureNo);
                    updateFabVisibility();

                } else {
                    String msg = String.format("강좌 삭제중 오류가 발생하였습니다");
                    BraveUtils.showToast(getContext(), msg);
                }
            }

            @Override
            public void onFail(Request request, Exception exception) {
                String msg = String.format("강좌 삭제중 오류가 발생하였습니다");
                BraveUtils.showToast(getContext(), msg);
            }
        });
    }

    ///////////////
    // Home Event
    ///////////////////////////////
    @Override
    public boolean onHomeBackPressed()
    {
        return false;
    }

    @Override
    public void onHomeTitleBarPressed()
    {

    }

    @Override
    public void onHomeMenuLecRegClicked()
    {
       Intent intent = new Intent(getActivity(), LectureSubscribeActivity.class);
       getActivity().startActivityForResult(intent, RequestCode.REQUEST_REG_LECTURE);
    }
}
