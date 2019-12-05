package kr.co.bravecompany.modoogong.android.stdapp.pageLectureList;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import kr.co.bravecompany.api.android.stdapp.api.data.Packet;
import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.modoogong.android.stdapp.db.BaseDataManager;
import kr.co.bravecompany.modoogong.android.stdapp.db.MetaDataManager;

public  class LectureFilterWidget {

    public interface  OnFilterChangedListener
    {
        void OnFilterChanged();
    }

    public static class CourseFilterWidget {

        public List<Packet.SubjectData> mSubjectDataList = new ArrayList<>();

        public List<String> courseList;
        public String selectedCD;
        public String selectedNM;

        public Spinner mListSpinner;
        private ArrayAdapter<String> mListAdapter;

        OnFilterChangedListener mListener;

        public CourseFilterWidget(Spinner spinner,OnFilterChangedListener listener ) {

            mListener = listener;

           // mListAdapter =  new ArrayAdapter<String>(spinner.getContext(), android.R.layout.simple_spinner_dropdown_item);
            mListAdapter =  new ArrayAdapter<String>(spinner.getContext(), R.layout.filter_list_item);
            mListSpinner = spinner;
            mListSpinner.setAdapter(mListAdapter);


            mListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    onSelected(position);
                    mListener.OnFilterChanged();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            courseList = new ArrayList<>();
            courseList.clear();
            courseList.add("전체 과목");

            mListAdapter.addAll(courseList);
            mListAdapter.notifyDataSetChanged();

            MetaDataManager.getInstance().loadSubjectData(new BaseDataManager.OnDataUpdatedListener<List<Packet.SubjectData>>() {
                @Override
                public void onDataUpdated(List<Packet.SubjectData> dataType) {
                    initSubjectList(dataType);
                }
            });
        }

        public String querySubjectName(String subjectCode) {
            if (mSubjectDataList != null) {
                for (Packet.SubjectData data : mSubjectDataList) {
                    if (data.SUBJ_CD.equalsIgnoreCase(subjectCode))
                        return data.SUBJ_NM;
                }
            }

            return "";
        }

        public void initSubjectList(List<Packet.SubjectData> subjectDataList) {
            mSubjectDataList = subjectDataList;
            courseList = new ArrayList<>();
            courseList.clear();
            courseList.add("전체 과목");

            for (Packet.SubjectData subjData : mSubjectDataList) {
                courseList.add(subjData.SUBJ_NM);
            }

            mListAdapter.clear();
            mListAdapter.addAll(courseList);
            mListAdapter.notifyDataSetChanged();

            onSelected(0);
        }

        public void initSelection()
        {
            onSelected(0);
            mListSpinner.setSelection(0);
        }

        public void onSelected(int idx) {
            if (idx == 0) {
                selectedNM = null;
                selectedCD = null;
            } else {
                selectedNM = mSubjectDataList.get(idx - 1).SUBJ_NM;
                selectedCD = mSubjectDataList.get(idx - 1).SUBJ_CD;
            }
        }

        public boolean isVisible(String subjCD) {
            if (selectedCD == null || selectedCD.length() == 0)
                return true;

            return subjCD.equalsIgnoreCase(selectedCD);
        }
    }

    /************************************************************************
     *
     ************************************************************************/

    public static class TeacherFilterWidget
    {
        public List<String> teacherItemList;
        public List<Packet.TeacherData> teacherItemDataList;

        public String selectedCD;
        public String selectedNM;

        public Spinner mListSpinner;
        private ArrayAdapter<String> mListAdapter;

        OnFilterChangedListener mListener;

        public List<Packet.TeacherData> mTeacherDataList = new ArrayList<>();

        public TeacherFilterWidget(Spinner spinner,OnFilterChangedListener listener ) {

            mListener = listener;

           // mListAdapter =  new ArrayAdapter<String>(spinner.getContext(), android.R.layout.simple_spinner_dropdown_item);
            mListAdapter =  new ArrayAdapter<String>(spinner.getContext(), R.layout.filter_list_item);
            mListSpinner = spinner;
            mListSpinner.setAdapter(mListAdapter);


            mListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    onSelected(position);
                    mListener.OnFilterChanged();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            teacherItemList = new ArrayList<>();
            teacherItemDataList = new ArrayList<>();

            teacherItemList.clear();
            teacherItemList.add("전체 강사");
           // teacherItemDataList.add(null);

            mListAdapter.addAll(teacherItemList);
            mListAdapter.notifyDataSetChanged();

            MetaDataManager.getInstance().loadTeacherData(new BaseDataManager.OnDataUpdatedListener<List<Packet.TeacherData>>() {
                @Override
                public void onDataUpdated(List<Packet.TeacherData> dataType) {
                    initTeacherList(dataType);
                }
            });

        }

        Boolean containTeachCD(List<Packet.TeacherData> teacherDataList, String cd )
        {
            for (Packet.TeacherData d :  teacherDataList)
            {
                if(d.TCH_CD.equalsIgnoreCase(cd))
                    return true;
            }

            return false;
        }

        public void initTeacherList(List<Packet.TeacherData> teacherDataList)
        {
            mTeacherDataList =teacherDataList;

            teacherItemList = new ArrayList<>();
            teacherItemDataList = new ArrayList<>();

            teacherItemList.clear();
            teacherItemList.add("전체 강사");
            //teacherItemDataList.add(null);


            for (Packet.TeacherData tchData: teacherDataList){

                if(!containTeachCD(teacherItemDataList, tchData.TCH_CD)) {
                    teacherItemList.add(tchData.TCH_NM);
                    teacherItemDataList.add(tchData);
                }
            }

            mListAdapter.clear();
            mListAdapter.addAll(teacherItemList);
            mListAdapter.notifyDataSetChanged();

            mListSpinner.setSelection(0);
            onSelected(0);
        }

        public void initTeacherListByCourse(String subjectCD)
        {
            teacherItemList = new ArrayList<>();
            teacherItemDataList = new ArrayList<>();

            teacherItemList.clear();
            teacherItemList.add("전체 강사");
         //   teacherItemDataList.add(null);
            if(subjectCD ==null)
            {
                for (Packet.TeacherData tchData: mTeacherDataList){
                    if(!containTeachCD(teacherItemDataList, tchData.TCH_CD)) {
                        teacherItemList.add(tchData.TCH_NM);
                        teacherItemDataList.add(tchData);
                    }
                }
            }
            else {
                for (Packet.TeacherData tchData : mTeacherDataList) {

                    if (tchData.SUBJ_CD.equalsIgnoreCase(subjectCD)) {
                        if(!containTeachCD(teacherItemDataList, tchData.TCH_CD)) {
                            teacherItemList.add(tchData.TCH_NM);
                            teacherItemDataList.add(tchData);
                        }
                    }
                }
            }

            mListAdapter.clear();
            mListAdapter.addAll(teacherItemList);
            mListAdapter.notifyDataSetChanged();

            mListSpinner.setSelection(0);
            onSelected(0);
        }

        public void initSelection()
        {
            onSelected(0);
            mListSpinner.setSelection(0);
        }

        public String queryTeacherName(String tchCode)
        {
            if(mTeacherDataList !=null) {
                for (Packet.TeacherData data : mTeacherDataList) {
                    if (data.TCH_CD.equalsIgnoreCase(tchCode))
                        return data.TCH_NM;
                }
            }

            return "";
        }

        public void onSelected(int idx)
        {
            if(idx == 0) {
                selectedCD = null;
                selectedNM = null;
            }
            else {
                Packet.TeacherData data= teacherItemDataList.get(idx-1);
                selectedCD = data.TCH_CD;
                selectedNM = data.TCH_NM;
                //selectedCD =  mTeacherDataList.get(idx-1).TCH_CD;
               // selectedNM =  mTeacherDataList.get(idx-1).TCH_NM;
            }
        }

        public boolean isVisible(String teacherCD)
        {
            if(selectedCD ==null || selectedCD.length() ==0)
                return true;

            return  teacherCD.equalsIgnoreCase(selectedCD);
        }
    }
}