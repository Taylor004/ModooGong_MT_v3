package kr.co.bravecompany.modoogong.android.stdapp.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import kr.co.bravecompany.api.android.stdapp.api.OnResultListener;
import kr.co.bravecompany.api.android.stdapp.api.data.LectureItemVO;
import kr.co.bravecompany.api.android.stdapp.api.data.Packet;
import kr.co.bravecompany.api.android.stdapp.api.requests.SubscribeLectureRequests;
import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.modoogong.android.stdapp.config.Tags;
import kr.co.bravecompany.modoogong.android.stdapp.db.MetaDataManager;
import kr.co.bravecompany.modoogong.android.stdapp.db.StudyDataManager;
import kr.co.bravecompany.modoogong.android.stdapp.pageLectureList.LectureFilterWidget;
import kr.co.bravecompany.modoogong.android.stdapp.utils.BraveUtils;
import kr.co.bravecompany.modoogong.android.stdapp.viewholder.OnItemClickListener;
import okhttp3.Request;

//import net.bravecompany.modoogong.android.stdapp.R;

public class LectureSubscribeActivity extends DownBaseActivity {


    final String TAG = "SUBSCRIBE";

    public class HeaderWidget
    {
        Toolbar toolbar;
        public ImageView closeButton;
        public ImageView selectionResetButton;
    }

    public class FilterWidget
    {
        public Spinner passListSpinner;
        public ArrayList<String> passNameList = new ArrayList<>();
        public ArrayAdapter<String> passListAdapter;

        public LectureFilterWidget.CourseFilterWidget mCourseFilter;
        public LectureFilterWidget.TeacherFilterWidget mTeacherFilter;
    }


    public class LectureListWidget
    {
        public RecyclerView listView;
        public LectureListAdapter listAdapter;

        public LinearLayout emptyListView;
        public LinearLayout emptyPassView;

        public TextView doSubscribeButton;
    }

    HeaderWidget mHeaderWidget = new HeaderWidget();
    FilterWidget mFilterWidget = new FilterWidget();
    LectureListWidget mLecListWidget = new LectureListWidget();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSystemBar(true);
        //setDialogWindow();

        setContentView(R.layout.activity_course_reg);
        //Intent intent = new Intent(this.getIntent()); // LectureRecordFragment에서 보낸 데이터 값을 받아올 intent
        initLayout();
        initListener();

        initData();
    }

    @Override
    protected void initLayout()
    {
        mHeaderWidget.toolbar = findViewById(R.id.toolbar);
        mHeaderWidget.toolbar.setTitle("");
        setSupportActionBar(mHeaderWidget.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

     //   getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);

        mHeaderWidget.closeButton = (ImageView) findViewById(R.id.btnClose);
        mHeaderWidget.selectionResetButton = (ImageView) findViewById(R.id.btnSelectionReset);

        mLecListWidget.doSubscribeButton = (TextView) findViewById(R.id.doSubscribe);

        mLecListWidget.listView = (RecyclerView) findViewById(R.id.lectureList);
        mLecListWidget.listView.setLayoutManager(new LinearLayoutManager(this));
        mLecListWidget.listAdapter = new LectureListAdapter();
        mLecListWidget.listView.setAdapter(mLecListWidget.listAdapter);

        mLecListWidget.emptyListView = (LinearLayout) findViewById(R.id.emptyListViewLayout);
        mLecListWidget.emptyPassView = (LinearLayout) findViewById(R.id.emptyPassViewLayout);


        mFilterWidget.passListSpinner = (Spinner) findViewById(R.id.filterPassList);
        mFilterWidget.passListAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.filter_list_item);
        mFilterWidget.passListSpinner.setAdapter(mFilterWidget.passListAdapter);

    }


    void closeActivity()
    {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);

        super.onBackPressed();
    }


    @Override
    protected void initListener() {


        mHeaderWidget.closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                closeActivity();
            }
        });

        mHeaderWidget.selectionResetButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onSelectionReset();
            }
        });

        mFilterWidget.passListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                RefreshPassList(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        mFilterWidget.mTeacherFilter = new LectureFilterWidget.TeacherFilterWidget((Spinner) findViewById(R.id.filterTeacherList),
                new LectureFilterWidget.OnFilterChangedListener() {
                    @Override
                    public void OnFilterChanged() {
                        refreshLectureList(false);
                    }
                });

        mFilterWidget.mCourseFilter = new LectureFilterWidget.CourseFilterWidget((Spinner) findViewById(R.id.filterCourseList),
                new LectureFilterWidget.OnFilterChangedListener() {
                    @Override
                    public void OnFilterChanged() {

                        refreshLectureList(false);

                        mFilterWidget.mTeacherFilter.initTeacherListByCourse(mFilterWidget.mCourseFilter.selectedCD);
                    }
                });

        mLecListWidget.listAdapter.setOnItemSelectListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mLecListWidget.listAdapter.toogle(position);
                refreshSubscribeCheckUpdated();
            }
        });

        mLecListWidget.doSubscribeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSubscribeSelectedLectureAll();
            }
        });
    }


    /*******************************************************************************
     *
     * Data Management
     *
     *******************************************************************************/

    public class LectureItemData
    {
        public Packet.LectureData lecture;
       // public boolean selected;
      //  public boolean subscribed;
    }

    public class PassItemData
    {
        public String passKey;
        public String payKey;

        public String passName;
        public List<Packet.LectureData> lectureList;
        public boolean selected;
        public boolean fetchCompleted;

        public Packet.PassData passData;
    }


    List<PassItemData> mPassItemDataList;
    PassItemData  mSelectedPassItemData;


    void initData()
    {
        mPassItemDataList = new ArrayList<PassItemData>();
        setListHintMsg(mFilterWidget.passListAdapter, "패스를 선택해주세요");

        RefreshPassList(-1);
        fetchPassList();
       // fetchTeacherNSubjectList();
    }

    void setListHintMsg(ArrayAdapter<String> adapter, String hintMsg)
    {
        ArrayList<String> passNameList = new ArrayList<>();
        passNameList.clear();
        passNameList.add(hintMsg);
        adapter.clear();
        adapter.addAll(passNameList);
    }


    /***************************************************
     *
     * 패스 리스트
     *
     ***************************************************/

    void RefreshPassList(int selIdx)
    {
        if(selIdx == -1) {
            setListHintMsg(mFilterWidget.passListAdapter, "패스 정보가 없습니다");
        }
        else if(selIdx ==0 && mPassItemDataList.size()==0)
        {
            setListHintMsg(mFilterWidget.passListAdapter, "패스 정보가 없습니다");
        }
        else
        {
            mFilterWidget.passListSpinner.setSelection(selIdx);

            int dataSel = selIdx;
            PassItemData passItemData = mPassItemDataList.get(dataSel);
            mSelectedPassItemData = passItemData;

            if (!passItemData.fetchCompleted)
            {
                fetchLectureList(passItemData.passKey, passItemData.payKey);
            }
            else
            {
                refreshLectureList(true);
                refreshSubscribeCheckUpdated();
            }
        }
    }

    void fetchPassList()
    {
        startLoading();

        SubscribeLectureRequests.getInstance().requestGetPassList(new OnResultListener<Packet.ResGetPassList>() {
            @Override
            public void onSuccess(Request request, Packet.ResGetPassList result)
            {
                stopLoading();

                mPassItemDataList.clear();

                ArrayList<String> passNameList = new ArrayList<>();
                passNameList.clear();

                if(result.resultCode >= 0)
                {
                     // 사용자가 패스권 없이 수강 패스 강좌 신청 버튼을 눌렀을때, 앱 강제 종료 되는 현상 수정 [2019.10.24 테일러]
                     if(result.passList != null && result.passList.size() > 0 ) {
                        for (Packet.PassData passData : result.passList) {
                            PassItemData passItemData = new PassItemData();

                            passItemData.passKey = passData.SAL_CD;
                            passItemData.payKey = passData.STL_SEQ;
                            passItemData.passName = passData.SAL_NM;
                            passItemData.passData = passData;

                            mPassItemDataList.add(passItemData);

                            passNameList.add(passItemData.passName);
                        }

                        mFilterWidget.passListAdapter.clear();
                        mFilterWidget.passListAdapter.addAll(passNameList);

                        RefreshPassList(0);
                    }
                     else
                         {
                             mLecListWidget.emptyPassView.setVisibility(View.VISIBLE);  // 사용자가 패스권 없이 수강 패스 강좌 신청 버튼을 눌렀을때, 앱 강제 종료 되는 현상 수정 [2019.10.24 테일러]
                         }
                }
                else
                {
                    String msg = String.format("구매한 패스 정보를 불러오지 못했습니다.");
                    BraveUtils.showToast(getApplicationContext(), msg);
                }
            }

            @Override
            public void onFail(Request request, Exception exception) {
                stopLoading();

               // setListHintMsg(mHeaderWidget.passListAdapter,"패스 정보를 불러오지 못했습니다" );

                String msg = String.format("구매한 패스를 정보를 불러오지 못했습니다.");
                BraveUtils.showToast(getApplicationContext(), msg);

                RefreshPassList(-1);
            }
        });

    }


    /***************************************************
     *
     * 강좌 리스트
     *
     ***************************************************/

    void fetchLectureList(final String passKey, final String payKey)
    {
        startLoading();

        SubscribeLectureRequests.getInstance().requestGetLectureList(passKey, payKey, new OnResultListener<Packet.ResGetLectureList>() {
            @Override
            public void onSuccess(Request request, Packet.ResGetLectureList result) {

                stopLoading();

                if(result.resultCode >=0) {

                    for (PassItemData itemdata : mPassItemDataList) {
                        if (itemdata.passKey == passKey) {
                            itemdata.lectureList = result.passChrList;
                            itemdata.fetchCompleted = true;
                            mSelectedPassItemData = itemdata;


                            for (Packet.LectureData lectureData : itemdata.lectureList) {
                                lectureData.stlSeq = itemdata.passData.STL_SEQ;
                                lectureData.grdSeq = itemdata.passData.STD_GRD_SEQ;
                            }


                            refreshLectureList(true);
                            refreshSubscribeCheckUpdated();
                            break;
                        }
                    }
                }
                else
                {

                }
            }

            @Override
            public void onFail(Request request, Exception exception) {

                stopLoading();
                BraveUtils.showToast(getApplicationContext(), exception.getMessage());
            }
        });
    }

    void refreshLectureList(boolean reset)
    {
        if(mSelectedPassItemData ==null || mSelectedPassItemData.lectureList ==null)
        {
            return;
        }

        List<Packet.LectureData> lectureDataList = mSelectedPassItemData.lectureList;


        mLecListWidget.listAdapter.clear();

        ArrayList<Packet.LectureData> filterList = new ArrayList<>();

        for(Packet.LectureData data : lectureDataList)
        {
            if(mFilterWidget.mCourseFilter.isVisible(data.SUBJ_CD) &&  mFilterWidget.mTeacherFilter.isVisible(data.TCH_CD))
                filterList.add(data);

        }

        mLecListWidget.listAdapter.addAll(filterList);

        if(filterList.size() >0 )
        {
            mLecListWidget.listView.setVisibility(View.VISIBLE);
            mLecListWidget.emptyListView.setVisibility(View.GONE);
        }
        else
        {
            mLecListWidget.listView.setVisibility(View.GONE);
            mLecListWidget.emptyListView.setVisibility(View.VISIBLE);
        }
    }


    /*******************************************************************************
     *
     * 강과 신청/ 해제
     *
     *******************************************************************************/

    void refreshSubscribeButtonMsg(int selCount)
    {
        String msg;

        if(selCount > 0 )
            msg  = String.format("%d 강좌 수강 신청하기", selCount);
        else
            msg = String.format("수강을 원하는 강좌를 선택해 주십시오.");

        mLecListWidget.doSubscribeButton.setText(msg);
    }

    void refreshSubscribeCheckUpdated()
    {
        if(mSelectedPassItemData ==null)
            return;

        if(mSelectedPassItemData.lectureList ==null)
            return;

        int selCount = 0;

        if (mSelectedPassItemData.fetchCompleted && mSelectedPassItemData.lectureList != null) {
            for (Packet.LectureData lectureData : mSelectedPassItemData.lectureList) {
                selCount += lectureData.selected && !lectureData.subscribed ? 1 : 0;
            }
        }

        refreshSubscribeButtonMsg(selCount);
    }

    void onSelectionReset()
    {
        String msg = String.format("선택 항목을 초기화 합니다");

        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_main_pen)
                .setMessage(msg)
                .setPositiveButton(R.string.common_ok, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton)
                    {
                        doSelectionReset();
                    }})

                .setNegativeButton(R.string.common_cancel, null).show();
    }

    void doSelectionReset()
    {
        for(PassItemData passItemData : mPassItemDataList)
        {
            if (passItemData.fetchCompleted && passItemData.lectureList != null) {
                for (Packet.LectureData lectureData : passItemData.lectureList) {
                    lectureData.selected = false;
                }
            }
        }

        mLecListWidget.listAdapter.notifyDataSetChanged();
        refreshSubscribeCheckUpdated();
    }

    List<String> selectedLectureCDs = new ArrayList<>();

    void onSubscribeSelectedLectureAll()
    {
        if(mSelectedPassItemData ==null)
            return;

        if(mSelectedPassItemData.lectureList ==null)
            return;

        int selCount = 0;


        selectedLectureCDs.clear();
        if (mSelectedPassItemData.fetchCompleted && mSelectedPassItemData.lectureList != null) {
            for (Packet.LectureData lectureData : mSelectedPassItemData.lectureList) {

                if(lectureData.selected && !lectureData.subscribed) {
                    selCount += 1;
                    selectedLectureCDs.add(lectureData.PROD_CD);
                }
            }
        }

        if (selCount == 0) {
            String msg = String.format("수강을 원하는 강좌를 선택해주십시오.");
            BraveUtils.showToast(getApplicationContext(), msg);
            return;
        }

        startLoading();

        SubscribeLectureRequests.getInstance().requestSubscribeLectureList(
                mSelectedPassItemData.passKey,
                mSelectedPassItemData.payKey,
                mSelectedPassItemData.passData.STD_GRD_SEQ,
                selectedLectureCDs,
                new OnResultListener<Packet.ResSubscribeLecture>() {

                        @Override
                        public void onSuccess(Request request, Packet.ResSubscribeLecture result) {
                            stopLoading();

                            if (result.resultCode == 0) {

                                String msg = String.format("수강 신청이 완료되었습니다");
                                BraveUtils.showToast(getApplicationContext(), msg);

                                StudyDataManager.getInstance().storeTempLectureSubscribeList(selectedLectureCDs);
                                closeActivity();

                            } else {
                                String msg = String.format("수강 신청중 오류가 발생하였습니다");
                                BraveUtils.showToast(getApplicationContext(), msg);
                            }
                        }

                        @Override
                        public void onFail(Request request, Exception exception) {

                            stopLoading();

                            String msg = String.format("수강 신청중 오류가 발생하였습니다");
                            BraveUtils.showToast(getApplicationContext(), msg);
                        }
                    });
    }


    /*******************************************************************************
     *
     * LectureListAdapter
     *
     *******************************************************************************/

    public class LectureListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<LectureItemData> items = new ArrayList<LectureItemData>();

        private OnItemClickListener mItemSelectedListener;

        public void clear()
        {
            items.clear();
            notifyDataSetChanged();
        }

        public void addAll(List<Packet.LectureData> dataList) {

            for(Packet.LectureData data : dataList )
            {
                LectureItemData selData = new LectureItemData();
                selData.lecture = data;
                LectureItemVO lectureItemVO= StudyDataManager.getInstance().querySubscribedLectureInfo( data.PROD_CD);

                selData.lecture.subscribed = lectureItemVO !=null ? true : false ;

                items.add(selData);
            }

            notifyDataSetChanged();
        }

        public LectureItemData getItem(int postion) {
            return items.get(postion);
        }


        public int getSelectedCnt() {
            int result = 0;
            for (LectureItemData item : items) {
                if (item.lecture.selected && !item.lecture.subscribed) {
                    result = result + 1;
                }
            }
            return result;
        }
        public void toogle(int position)
        {
            getItem(position).lecture.selected = !getItem(position).lecture.selected;
            notifyDataSetChanged();
        }

        public ArrayList<Packet.LectureData> getSelectedItems() {
            ArrayList<Packet.LectureData> result = new ArrayList<>();
            for (LectureItemData item : items) {
                if (item.lecture.selected && !item.lecture.subscribed) {
                    result.add(item.lecture);
                }
            }
            return result;
        }


        public void setOnItemSelectListener(OnItemClickListener listener) {
            mItemSelectedListener = listener;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.view_lecture_reg_item, parent, false);
            RecyclerView.ViewHolder holder = new LectureItemViewHolder(view);
            view.setTag(holder);
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            LectureItemViewHolder h = (LectureItemViewHolder) holder;
            h.setLectureItem(items.get(position));
            h.setOnItemSelectListener(mItemSelectedListener);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }


    /*******************************************************************************
     *
     * LectureItemViewHolder
     *
     *******************************************************************************/

    public class LectureItemViewHolder extends RecyclerView.ViewHolder {
        private Context mContext;
        private LectureItemData mLectureData;

        private FrameLayout layoutItem;


        private TextView txtTeacherName;
        private TextView txtSaleType;
        private TextView txtLectureName;
       // private TextView txtLectureDetail;

        private OnItemClickListener mOnItemSelectedListener;

        private CheckBox checkSelected;
        private ImageView subscribedMark;

        private ImageView goLinkLectureDetail;


        public LectureItemViewHolder(View itemView) {
            super(itemView);
            mContext = itemView.getContext();

            layoutItem = (FrameLayout) itemView.findViewById(R.id.subscribeLayoutItem);

            txtLectureName = (TextView) itemView.findViewById(R.id.txtLectureName);
            txtTeacherName = (TextView)  itemView.findViewById(R.id.txtTeacherName);
            txtSaleType = (TextView)  itemView.findViewById(R.id.txtSaleType);
           // txtLectureDetail = (TextView)  itemView.findViewById(R.id.txtLectureDetail);

            checkSelected = (CheckBox) itemView.findViewById(R.id.checkSelected);
            subscribedMark = (ImageView) itemView.findViewById(R.id.subscribedMark);

            goLinkLectureDetail = (ImageView) itemView.findViewById(R.id.goLinkLectureDetail);

            layoutItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemSelectedListener.onItemClick(v, getLayoutPosition());
                }
            });

            checkSelected.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemSelectedListener.onItemClick(v, getLayoutPosition());
                }
            });

            goLinkLectureDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SubscribeLectureRequests.getInstance().goLinkLectureDetail(LectureSubscribeActivity.this,  mLectureData.lecture.SAL_CD);
                }
            });

            /*
            checkSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mOnItemSelectedListener.onItemClick(buttonView, getLayoutPosition());
                }
            });
            */
        }

        public void setOnItemSelectListener(OnItemClickListener listener)
        {
            mOnItemSelectedListener = listener;
        }

        public void setLectureItem(LectureItemData item)
        {
            mLectureData = item;

            String lectureName = mLectureData.lecture.SAL_NM;
            lectureName = BraveUtils.fromHTMLTitle(lectureName);

            txtLectureName.setText(lectureName);//  mLectureData.lecture.SAL_NM);
            txtTeacherName.setText(mLectureData.lecture.TCH_NM);// mFilterWidget.mTeacherFilter.queryTeacherName(mLectureData.lecture.TCH_CD));
            txtSaleType.setText(mLectureData.lecture.SUBJ_NM);// mFilterWidget.mCourseFilter.querySubjectName(mLectureData.lecture.SUBJ_CD));
           // txtLectureDetail.setText(mLectureData.lecture.DP_INFO);

            if(mLectureData.lecture.subscribed)
            {
                checkSelected.setVisibility(View.GONE);
                subscribedMark.setVisibility(View.VISIBLE);
            }
            else {
                checkSelected.setVisibility(View.VISIBLE);
                subscribedMark.setVisibility(View.GONE);

                checkSelected.setChecked(mLectureData.lecture.selected);
            }
        }
    }
}
