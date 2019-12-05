package kr.co.bravecompany.modoogong.android.stdapp.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.gun0912.tedpermission.PermissionListener;
import com.kollus.sdk.media.util.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.modoogong.android.stdapp.activity.VoiceRecordActivity;
import kr.co.bravecompany.modoogong.android.stdapp.adapter.StudyQAMenuAdapter;
import kr.co.bravecompany.api.android.stdapp.api.OnResultListener;
import kr.co.bravecompany.api.android.stdapp.api.utils.APIManager;
import kr.co.bravecompany.api.android.stdapp.api.requests.BoardRequests;
import kr.co.bravecompany.api.android.stdapp.api.requests.QARequests;
import kr.co.bravecompany.api.android.stdapp.api.data.BkCodeVO;
import kr.co.bravecompany.api.android.stdapp.api.data.BoardCateResult;
import kr.co.bravecompany.api.android.stdapp.api.data.BoardCateVO;
import kr.co.bravecompany.api.android.stdapp.api.data.BoardDelFileRequest;
import kr.co.bravecompany.api.android.stdapp.api.data.ChrCodeVO;
import kr.co.bravecompany.api.android.stdapp.api.data.PostResult;
import kr.co.bravecompany.api.android.stdapp.api.data.SalCateVO;
import kr.co.bravecompany.api.android.stdapp.api.data.StudyQARequest;
import kr.co.bravecompany.api.android.stdapp.api.data.TchCodeVO;
import kr.co.bravecompany.modoogong.android.stdapp.application.ModooGong;
import kr.co.bravecompany.modoogong.android.stdapp.config.AnalysisTags;
import kr.co.bravecompany.modoogong.android.stdapp.config.RequestCode;
import kr.co.bravecompany.modoogong.android.stdapp.config.Tags;
import kr.co.bravecompany.modoogong.android.stdapp.pageCS.DoQAFragment;
import kr.co.bravecompany.modoogong.android.stdapp.utils.BraveUtils;
import kr.co.bravecompany.modoogong.android.stdapp.utils.MediaScanner;
import kr.co.bravecompany.modoogong.android.stdapp.data.QAMenuData;
import kr.co.bravecompany.api.android.stdapp.api.data.StudyQADetailVO;
import okhttp3.Request;

/**
 * Created by BraveCompany on 2016. 10. 26..
 */

public class StudyDoQAFragment extends DoQAFragment {

    private List<String> mQASubjectList;
    private List<String> mQATypeList;
    private List<String> mQALectureList;
    private List<String> mQABookList;
    private List<String> mQATeacherList;
    private ArrayList<SalCateVO> mQASubject = new ArrayList<>();
    private ArrayList<BoardCateVO> mQAType = new ArrayList<>();
    private ArrayList<ChrCodeVO> mQALecture = new ArrayList<>();
    private ArrayList<ChrCodeVO> mQAShowLecture = new ArrayList<>();
    private ArrayList<BkCodeVO> mQABook = new ArrayList<>();
    private ArrayList<TchCodeVO> mQATeacher = new ArrayList<>();

    private ImageView btnVoice;
    private View divider;

    private StudyQADetailVO mEditQADetail;
    private StudyQADetailVO mPostEditQADetail;

    private boolean isResumed = false;

    public static StudyDoQAFragment newInstance() {
        return new StudyDoQAFragment();
    }

    @Override
    protected void updateResult(int requestCode, Intent data){
        super.updateResult(requestCode, data);
        switch (requestCode){
            case RequestCode.REQUEST_RECORD:
                String path = data.getStringExtra(Tags.TAG_VOICE);
                if(path != null){
                    MediaScanner.scanMedia(getContext(), path);
                    updatePreview(Tags.QA_UPLOAD_TYPE.QA_UPLOAD_VOICE, path, null);
                }
                break;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_do_qa, container, false);
        initLayout(rootView);
        initListener();
        initData();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        isResumed = true;
        if(mEditQADetail != null){
            initMenuData();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        isResumed = false;
    }


    @Override
    protected void initLayout(ViewGroup rootView) {
        super.initLayout(rootView);

        contentCall.setVisibility(View.GONE);

        btnVoice = (ImageView)rootView.findViewById(R.id.btnVoice);
        divider = rootView.findViewById(R.id.divider);

        //preview
        if(ModooGong.isShowQAVoiceRecoder) {
            btnVoice.setVisibility(View.VISIBLE);
            divider.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void initMenuAdapter(){
        mAdapter = new StudyQAMenuAdapter();
        mListView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(getContext());
        mListView.setLayoutManager(mLayoutManager);
    }

    @Override
    protected void updateOrientationConfigChanged(int orientation){
        if(Utils.isTablet(getContext())){
            return;
        }
        ViewGroup.LayoutParams params = mListView.getLayoutParams();
        int height = getResources().getDimensionPixelSize(R.dimen.common_row_height_s);
        switch (orientation){
            case Configuration.ORIENTATION_LANDSCAPE:
                params.height = height;
                mListView.setLayoutParams(params);

                int selected = ((StudyQAFragment)getParentFragment()).getSelectedPage();
                if(isResumed && selected == Tags.QA_PAGE_INDEX.QA_PAGE_Q_DO && mAdapter.getItemCount() > 1){
                    BraveUtils.showToast(getActivity(), getString(R.string.toast_qa_guide_landscape));
                }
                break;
            case Configuration.ORIENTATION_PORTRAIT:
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                mListView.setLayoutParams(params);
                break;
        }
    }

    @Override
    protected void initListener() {
        super.initListener();

        btnVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //record
                if (checkAddAvailable()) {
                    BraveUtils.checkPermission(getActivity(), mStoragePermissionListener,
                            getString(R.string.check_permission_external_storage),
                            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }else{
                    Toast.makeText(getActivity(), getString(R.string.toast_qa_add),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void setVisibilityViews(boolean isOpen){
        if (isOpen) {
            BraveUtils.setVisibilityTopView(mListView, View.GONE);
        } else {
            BraveUtils.setVisibilityTopView(mListView, View.VISIBLE);
        }
    }

    @Override
    protected void setData(String data) {
        if(data != null){
            mEditQADetail = (StudyQADetailVO) BraveUtils.toJsonString(data, StudyQADetailVO.class);
        }
    }

    private void setEditData(StudyQADetailVO editData){
        if(editData != null) {
            if(ModooGong.isShowQAMenuSubject) {
                for (int i = 0; i < mQASubject.size(); i++) {
                    SalCateVO salCateVO = mQASubject.get(i);
                    if (salCateVO.getCD().equals(editData.getCate())) {
                        mAdapter.updateQAMenuIndexValue(Tags.STUDY_QA_MENU_INDEX.QA_MENU_SUBJECT, i, salCateVO.getNM());
                        initShowBySubject();
                        break;
                    }
                }
            }
            if(ModooGong.isShowQAMenuType) {
                for (int i = 0; i < mQAType.size(); i++) {
                    BoardCateVO boardCateVO = mQAType.get(i);
                    if (boardCateVO.getCD().equals(editData.getCategory())) {
                        mAdapter.updateQAMenuIndexValue(Tags.STUDY_QA_MENU_INDEX.QA_MENU_TYPE, i, boardCateVO.getNM());
                        break;
                    }
                }
            }
            if(ModooGong.isShowQAMenuTeacher) {
                for (int i = 0; i < mQATeacher.size(); i++) {
                    TchCodeVO tchCodeVO = mQATeacher.get(i);
                    if (tchCodeVO.getCD().equals(editData.getTchCd())) {
                        mAdapter.updateQAMenuIndexValue(Tags.STUDY_QA_MENU_INDEX.QA_MENU_TEACHER, i, tchCodeVO.getNM());
                        break;
                    }
                }
            }
            if(ModooGong.isShowQAMenuBook){
                for (int i = 0; i < mQABook.size(); i++) {
                    BkCodeVO bkCodeVO = mQABook.get(i);
                    if (bkCodeVO.getCD().equals(editData.getBookNo())) {
                        mAdapter.updateQAMenuIndexValue(Tags.STUDY_QA_MENU_INDEX.QA_MENU_BOOK, i, bkCodeVO.getNM());
                        break;
                    }
                }
                if(editData.getBookPage() != null){
                    mAdapter.updateQAMenuIndexValue(Tags.STUDY_QA_MENU_INDEX.QA_MENU_PAGE, 0, editData.getBookPage());
                }
            }
            String lectureNo = editData.getLectureNo();
            if(lectureNo != null) {
                for (int i = 0; i < mQAShowLecture.size(); i++) {
                    ChrCodeVO chrCodeVO = mQAShowLecture.get(i);
                    if (chrCodeVO.getCD().equals(lectureNo)) {
                        mAdapter.updateQAMenuIndexValue(Tags.STUDY_QA_MENU_INDEX.QA_MENU_LECTURE, i,
                                BraveUtils.fromHTMLTitle(chrCodeVO.getNM()));
                        break;
                    }
                }
            }
            //editQATitle.setText(editData.getTitle());
            //editQAContent.setText(editData.getContent());
            editQATitle.setText(BraveUtils.fromHTMLTitle(editData.getTitle()));
            editQAContent.setText(BraveUtils.fromHTMLEditContent(editData.getContent()));

            String filePath = APIManager.getFileUrl(getContext(), editData.getFilePath());
            if(filePath != null) {
                String fileName = editData.getFilePathName();
                if(fileName == null){
                    fileName = BraveUtils.getFileName(filePath);
                }
                if (BraveUtils.checkImageFormat(filePath)) {
                    updatePreview(Tags.QA_UPLOAD_TYPE.QA_UPLOAD_IMAGE, filePath, fileName);
                } else if (BraveUtils.checkAudioFormat(filePath)) {
                    updatePreview(Tags.QA_UPLOAD_TYPE.QA_UPLOAD_VOICE, filePath, fileName);
                }else{
                    updatePreview(Tags.QA_UPLOAD_TYPE.QA_UPLOAD_FILE, filePath, fileName);
                }
                //setVisibilityBtnClose(false);
            }
        }
    }

    @Override
    protected void initData(){
        mEditQADetail = null;
        initMenuData();
    }

    private void initMenuData(){
        mPostEditQADetail = null;
        mAdapter.clear();

        loadMenuData();

        ArrayList<QAMenuData> menus = new ArrayList<QAMenuData>();
        ArrayList<String> qa_menu = new ArrayList<String>();
        qa_menu.addAll(Arrays.asList(getResources().getStringArray(R.array.study_qa_menu)));
        for(String menu : qa_menu){
            QAMenuData m = new QAMenuData();
            m.setTag(Tags.STUDY_QA_MENU_GROUP_INDEX.LECTURE_TYPE);
            m.setTitle(menu);
            m.setRequire(ModooGong.isRequiredQAMenuLecture);
            menus.add(m);
        }
        if(ModooGong.isShowQAMenuTeacher){
            String menu = getString(R.string.study_qa_menu_teacher);
            QAMenuData m = new QAMenuData();
            m.setTag(Tags.STUDY_QA_MENU_GROUP_INDEX.LECTURE_TYPE);
            m.setTitle(menu);
            menus.add(0, m);
        }
        if(ModooGong.isShowQAMenuType){
            String menu = getString(R.string.study_qa_menu_type);
            QAMenuData m = new QAMenuData();
            m.setTag(Tags.STUDY_QA_MENU_GROUP_INDEX.LECTURE_TYPE);
            m.setTitle(menu);
            menus.add(0, m);
        }
        if(ModooGong.isShowQAMenuSubject){
            String menu = getString(R.string.study_qa_menu_subject);
            QAMenuData m = new QAMenuData();
            m.setTag(Tags.STUDY_QA_MENU_GROUP_INDEX.LECTURE_TYPE);
            m.setTitle(menu);
            menus.add(0, m);
        }
        if(ModooGong.isShowQAMenuBook){
            ArrayList<String> qa_menu_book = new ArrayList<String>();
            qa_menu_book.addAll(Arrays.asList(getResources().getStringArray(R.array.study_qa_menu_book)));

            for(String menu : qa_menu_book){
                QAMenuData m = new QAMenuData();
                m.setTag(Tags.STUDY_QA_MENU_GROUP_INDEX.BOOK_TYPE);
                m.setTitle(menu);
                m.setRequire(ModooGong.isRequiredQAMenuBook);
                menus.add(m);
            }
        }

        mAdapter.addAll(menus);
        resetViews();

        onConfigurationChanged(getResources().getConfiguration());
    }

    @Override
    protected void loadMenuData(){
        startLoading();
        BoardRequests.getInstance().requestStudyQABoardCateList(new OnResultListener<BoardCateResult>(){

            @Override
            public void onSuccess(Request request, BoardCateResult result) {
                stopLoading();
                setMenuData(result);
            }

            @Override
            public void onFail(Request request, Exception exception) {
                stopLoading();
                BraveUtils.showToast(getActivity(), getString(R.string.toast_common_network_fail));
            }
        });
    }

    @Override
    protected void setMenuData(BoardCateResult result){
        if(result == null){
            return;
        }

        if(result.getCates() != null) {
            ArrayList<SalCateVO> salCates = result.getCates().getSAL_CATE();
            if (salCates != null && salCates.size() != 0) {
                mQASubjectList = new ArrayList<>();
                for (int i = 0; i < salCates.size(); i++) {
                    mQASubjectList.add(salCates.get(i).getNM());
                }
                mQASubject = salCates;
            }
        }

        if(result.getBoardCates() != null) {
            ArrayList<BoardCateVO> boardCates = result.getBoardCates().getBOARD_CATE().getBoardCatesStudyQA();
            if (boardCates != null && boardCates.size() != 0) {
                mQATypeList = new ArrayList<>();
                for (int i = 0; i < boardCates.size(); i++) {
                    mQATypeList.add(boardCates.get(i).getNM());
                }
                mQAType = boardCates;
                mAdapter.setQAType(boardCates);
            }
        }

        if(result.getTchs() != null) {
            ArrayList<TchCodeVO> tchCodes = result.getTchs().getTCH_CD();
            if (tchCodes != null && tchCodes.size() != 0) {
                mQATeacherList = new ArrayList<>();
                for (int i = 0; i < tchCodes.size(); i++) {
                    mQATeacherList.add(tchCodes.get(i).getNM());
                }
                mQATeacher = tchCodes;
            }
        }

        if(result.getChrs() != null) {
            ArrayList<ChrCodeVO> chrCodes = result.getChrs().getCHR_CD();
            if (chrCodes != null && chrCodes.size() != 0) {
                mQALectureList = new ArrayList<>();
                mQAShowLecture.clear();
                for (int i = 0; i < chrCodes.size(); i++) {
                    mQALectureList.add(BraveUtils.fromHTMLTitle(chrCodes.get(i).getNM()));
                    mQAShowLecture.add(chrCodes.get(i));
                }
                mQALecture = chrCodes;

                if(!ModooGong.isRequiredQAMenuLecture){
                    mQALectureList.add(0, getString(R.string.study_qa_menu_select_default));
                    mQAShowLecture.add(0, new ChrCodeVO("", "", "", ""));
                    mQALecture.add(0, new ChrCodeVO("", "", "", ""));
                }
            }
        }

        if(result.getBks() != null) {
            ArrayList<BkCodeVO> bkCodes = result.getBks().getBK_CD();
            if (bkCodes != null && bkCodes.size() != 0) {
                mQABookList = new ArrayList<>();
                for (int i = 0; i < bkCodes.size(); i++) {
                    mQABookList.add(BraveUtils.fromHTMLTitle(bkCodes.get(i).getNM()));
                }
                mQABook = bkCodes;

                if(!ModooGong.isRequiredQAMenuBook){
                    mQABookList.add(0, getString(R.string.study_qa_menu_select_default));
                    mQABook.add(0, new BkCodeVO("", "", ""));
                }
            }
        }

        if(mEditQADetail != null) {
            setEditData(mEditQADetail);
            mPostEditQADetail = mEditQADetail;
            mEditQADetail = null;
        }
    }

    private void initShowBySubject(){
        if(ModooGong.isShowQAMenuSubject) {
            int subject = mAdapter.getQAMenuIndex(Tags.STUDY_QA_MENU_INDEX.QA_MENU_SUBJECT);
            if(subject != -1) {
                if (mQASubject != null && mQASubject.size() != 0 &&
                        mQALecture != null && mQALecture.size() != 0) {
                    String CD = mQASubject.get(subject).getCD();
                    mQALectureList.clear();
                    mQAShowLecture.clear();
                    for (ChrCodeVO chr : mQALecture) {
                        if (chr.getSalCate().equals(CD)) {
                            mQALectureList.add(chr.getNM());
                            mQAShowLecture.add(chr);
                        }
                    }
                }
            }
        }
    }

    private void initShowByTeacher(){
        if(ModooGong.isShowQAMenuTeacher) {
            int teacher = mAdapter.getQAMenuIndex(Tags.STUDY_QA_MENU_INDEX.QA_MENU_TEACHER);
            if(teacher != -1) {
                if (mQATeacher != null && mQATeacher.size() != 0 &&
                        mQALecture != null && mQALecture.size() != 0) {
                    String CD = mQATeacher.get(teacher).getCD();
                    mQALectureList.clear();
                    mQAShowLecture.clear();
                    for (ChrCodeVO chr : mQALecture) {
                        if (chr.getTCH_CD().equals(CD)) {
                            mQALectureList.add(chr.getNM());
                            mQAShowLecture.add(chr);
                        }
                    }
                }
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void postQA(){
        boolean isUpdated = validateUpdateRequest();
        boolean isEdited = validateEditRequest();
        boolean isDeleted = validateDeleteRequest();

        if(mPostEditQADetail == null){
            if(!isUpdated){
                return;
            }
            updateQA();
        }else{
            if(!isUpdated){
                return;
            }
            if(!isEdited && !isDeleted){
                BraveUtils.showToast(getActivity(), getString(R.string.toast_qa_no_edit));
                return;
            }

            if(!isEdited && isDeleted){
                deleteQAFile();
            }else{
                updateQA();
            }
        }
    }

    @Override
    protected void updateQA(){
        String subjectName = null;
        String typeName = null;
        String teacherName = null;
        String bookName = null;
        String lectureName = null;
        StudyQARequest request = new StudyQARequest();
        if(ModooGong.isShowQAMenuSubject) {
            int subject = mAdapter.getQAMenuIndex(Tags.STUDY_QA_MENU_INDEX.QA_MENU_SUBJECT);
            String code = mQASubject.get(subject).getCD();
            request.setCate(code);
            subjectName = mQASubject.get(subject).getNM();
            if(ModooGong.host.equals("bravespk.com")){
                if(code.equals("1")){
                    request.setTchCd("1000");
                }else if(code.equals("2")){
                    request.setTchCd("1001");
                }
            }
        }
        if(ModooGong.isShowQAMenuType) {
            int type = mAdapter.getQAMenuIndex(Tags.STUDY_QA_MENU_INDEX.QA_MENU_TYPE);
            request.setCategory(mQAType.get(type).getCD());
            typeName = mQAType.get(type).getNM();
        }
        if(ModooGong.isShowQAMenuTeacher) {
            int teacher = mAdapter.getQAMenuIndex(Tags.STUDY_QA_MENU_INDEX.QA_MENU_TEACHER);
            request.setTchCd(mQATeacher.get(teacher).getCD());
            teacherName = mQATeacher.get(teacher).getNM();
        }
        if(ModooGong.isShowQAMenuBook){
            int book = mAdapter.getQAMenuIndex(Tags.STUDY_QA_MENU_INDEX.QA_MENU_BOOK);
            if(book != -1) {
                request.setBookNo(mQABook.get(book).getCD());
                bookName = mQABook.get(book).getNM();
            }else{
                request.setBookNo("");
            }

            int page = mAdapter.getQAMenuIndex(Tags.STUDY_QA_MENU_INDEX.QA_MENU_BOOK);
            String value = mAdapter.getQAMenuValue(Tags.STUDY_QA_MENU_INDEX.QA_MENU_PAGE);
            if(page != -1) {
                request.setBookPage(value);
            }else{
                request.setBookPage("");
            }
        }

        int lecture = mAdapter.getQAMenuIndex(Tags.STUDY_QA_MENU_INDEX.QA_MENU_LECTURE);
        if(lecture != -1) {
            request.setLectureNo(mQAShowLecture.get(lecture).getCD());
            lectureName = mQAShowLecture.get(lecture).getNM();
            if(request.getTchCd() == null) {
                request.setTchCd(mQAShowLecture.get(lecture).getTCH_CD());
            }
        }else{
            request.setLectureNo("");
        }

        request.setTitle(BraveUtils.toHTMLEditTitle(editQATitle.getText().toString()));
        //request.setContent(editQAContent.getText().toString());
        request.setContent(BraveUtils.toHTMLEditContent(editQAContent.getText().toString()));


        if(mPath != null) {
            if(!(mPostEditQADetail != null && mPostEditQADetail.getFilePath() != null &&
                    mPath.equals(APIManager.getFileUrl(getContext(), mPostEditQADetail.getFilePath())))){
                request.setFile(new File(mPath));
            }
        }

        CustomEvent event;
        if(mPostEditQADetail != null){
            request.setInquiryNo(mPostEditQADetail.getInquiryNo());

            boolean hasFile = (request.getFile() != null);
            event = new CustomEvent(AnalysisTags.DOQASTUDY)
                    .putCustomAttribute(AnalysisTags.ACTION, "post_edit_qa")
                    .putCustomAttribute(AnalysisTags.FILE, String.valueOf(hasFile));
        }else{
            request.setInquiryNo(-1);

            boolean hasFile = (request.getFile() != null);
            event = new CustomEvent(AnalysisTags.DOQASTUDY)
                    .putCustomAttribute(AnalysisTags.ACTION, "post_new_qa")
                    .putCustomAttribute(AnalysisTags.FILE, String.valueOf(hasFile));
        }

        if(subjectName != null){
            event.putCustomAttribute(AnalysisTags.VALUE, subjectName);
        }
        if(typeName != null){
            event.putCustomAttribute(AnalysisTags.VALUE, typeName);
        }
        if(teacherName != null){
            event.putCustomAttribute(AnalysisTags.VALUE, teacherName);
        }
        if(lectureName != null){
            event.putCustomAttribute(AnalysisTags.VALUE, lectureName);
        }
        if(bookName != null){
            event.putCustomAttribute(AnalysisTags.VALUE, bookName);
        }
        Answers.getInstance().logCustom(event);

        startLoading();
        QARequests.getInstance().updateStudyQA(request, new OnResultListener<PostResult>() {
            @Override
            public void onSuccess(Request request, PostResult result) {
                stopLoading();
                checkUpdateResult(result);
            }

            @Override
            public void onFail(Request request, Exception exception) {
                stopLoading();
                BraveUtils.showToast(getActivity(), getString(R.string.toast_common_network_fail));
            }
        });
    }

    @Override
    protected void checkUpdateResult(PostResult result){
        if(result == null){
            return;
        }
        if(result.getResult() != null && result.getResult().equals("1")){
            if(mPostEditQADetail == null){
                goQAList();
            }else{
                if(validateDeleteRequest()) {
                    deleteQAFile();
                }else {
                    goQAList();
                }
            }
        }else{
            BraveUtils.showToast(getActivity(), getString(R.string.toast_qa_upload_failed));
        }
    }

    @Override
    protected void deleteQAFile(){
        if(mPostEditQADetail != null) {
            BoardDelFileRequest request = new BoardDelFileRequest();
            request.setListNo(mPostEditQADetail.getInquiryNo());

            Answers.getInstance().logCustom(new CustomEvent(AnalysisTags.DOQASTUDY)
                    .putCustomAttribute(AnalysisTags.ACTION, "post_delete_qa_attach"));

            startLoading();
            BoardRequests.getInstance().deleteStudyQAFile(request, new OnResultListener<PostResult>() {
                @Override
                public void onSuccess(Request request, PostResult result) {
                    stopLoading();
                    checkDeleteResult(result);
                }

                @Override
                public void onFail(Request request, Exception exception) {
                    stopLoading();
                    BraveUtils.showToast(getActivity(), getString(R.string.toast_common_network_fail));
                }
            });
        }
    }

    @Override
    protected void checkDeleteResult(PostResult result){
        if(result == null){
            return;
        }
        if(result.getResult() != null && result.getResult().equals("1")){
            goQAList();
        }else{
            BraveUtils.showToast(getActivity(), getString(R.string.toast_qa_upload_failed));
        }
    }

    @Override
    protected void goQAList(){
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                stopLoading();
                ((StudyQAFragment) getParentFragment()).movePage(Tags.QA_PAGE_INDEX.QA_PAGE_Q_LIST, null);
            }
        }, 1000);
    }

    @Override
    protected boolean validateUpdateRequest(){
        int check = mAdapter.checkQAMenuIndexEmpty();

        if(check == Tags.STUDY_QA_MENU_INDEX.QA_MENU_SUBJECT){
            BraveUtils.showToast(getActivity(), getString(R.string.toast_qa_no_subject));
            return false;
        }
        if(check == Tags.STUDY_QA_MENU_INDEX.QA_MENU_TYPE){
            BraveUtils.showToast(getActivity(), getString(R.string.toast_qa_no_type));
            return false;
        }
        if(check == Tags.STUDY_QA_MENU_INDEX.QA_MENU_TEACHER){
            BraveUtils.showToast(getActivity(), getString(R.string.toast_qa_no_teacher));
            return false;
        }
        if(check == Tags.STUDY_QA_MENU_INDEX.QA_MENU_LECTURE){
            BraveUtils.showToast(getActivity(), getString(R.string.toast_qa_no_lecture));
            return false;
        }
        if(check == Tags.STUDY_QA_MENU_INDEX.QA_MENU_BOOK ||
                check == Tags.STUDY_QA_MENU_INDEX.QA_MENU_PAGE){
            BraveUtils.showToast(getActivity(), getString(R.string.toast_qa_no_book));
            return false;
        }
        if(TextUtils.isEmpty(editQATitle.getText()) || TextUtils.isEmpty(editQAContent.getText())) {
            BraveUtils.showToast(getActivity(), getString(R.string.toast_qa_no_title_content));
            return false;
        }

        return true;
    }

    @Override
    protected boolean validateEditRequest(){
        if(mPostEditQADetail != null){
            boolean isEditCate = false;
            if(ModooGong.isShowQAMenuSubject) {
                int subject = mAdapter.getQAMenuIndex(Tags.STUDY_QA_MENU_INDEX.QA_MENU_SUBJECT);
                if(mPostEditQADetail.getCate() != null && subject != -1) {
                    isEditCate = !mPostEditQADetail.getCate().equals(mQASubject.get(subject).getCD());
                }
            }

            boolean isEditCategory = false;
            if(ModooGong.isShowQAMenuType) {
                int type = mAdapter.getQAMenuIndex(Tags.STUDY_QA_MENU_INDEX.QA_MENU_TYPE);
                if(mPostEditQADetail.getCategory() != null && type != -1) {
                    isEditCategory = !mPostEditQADetail.getCategory().equals(mQAType.get(type).getCD());
                }
            }

            boolean isEditTeacher = false;
            if(ModooGong.isShowQAMenuTeacher) {
                int teacher = mAdapter.getQAMenuIndex(Tags.STUDY_QA_MENU_INDEX.QA_MENU_TEACHER);
                if(mPostEditQADetail.getTchCd() != null && teacher != -1) {
                    isEditTeacher = !mPostEditQADetail.getTchCd().equals(mQATeacher.get(teacher).getCD());
                }
            }

            boolean isEditBook = false;
            boolean isEditBookPage = false;
            if(ModooGong.isShowQAMenuBook) {
                int book = mAdapter.getQAMenuIndex(Tags.STUDY_QA_MENU_INDEX.QA_MENU_BOOK);
                if(mPostEditQADetail.getBookNo() != null && book != -1){
                    isEditBook = !mPostEditQADetail.getBookNo().equals(mQABook.get(book).getCD());
                }else if(mPostEditQADetail.getBookNo() != null) {
                    isEditBook = !(mPostEditQADetail.getBookNo().equals("0") && book == -1);
                }else{
                    isEditBook = !(mPostEditQADetail.getBookNo() == null && book == -1);
                }

                int page = mAdapter.getQAMenuIndex(Tags.STUDY_QA_MENU_INDEX.QA_MENU_PAGE);
                String value = mAdapter.getQAMenuValue(Tags.STUDY_QA_MENU_INDEX.QA_MENU_PAGE);
                if(mPostEditQADetail.getBookPage() != null && page != -1){
                    isEditBookPage = !mPostEditQADetail.getBookPage().equals(value);
                }else{
                    isEditBookPage = !(mPostEditQADetail.getBookPage() == null && page == -1);
                }
            }

            boolean isEditLectureNo = false;
            int lecture = mAdapter.getQAMenuIndex(Tags.STUDY_QA_MENU_INDEX.QA_MENU_LECTURE);
            if(mPostEditQADetail.getLectureNo() != null && lecture != -1){
                isEditLectureNo = !mPostEditQADetail.getLectureNo().equals(mQAShowLecture.get(lecture).getCD());
            }else{
                isEditLectureNo = !(mPostEditQADetail.getLectureNo() == null && lecture == -1);
            }

            boolean isEditTitle = !BraveUtils.fromHTMLTitle(mPostEditQADetail.getTitle())
                    .equals(editQATitle.getText().toString());
            boolean isEditContent = !BraveUtils.fromHTMLEditContent(mPostEditQADetail.getContent())
                    .equals(editQAContent.getText().toString());
            boolean isAddFile = (mPostEditQADetail.getFilePath() == null && mPath != null);
            boolean isEditFile = false;
            if(mPath != null) {
                if(mPostEditQADetail.getFilePath() != null &&
                        !mPath.equals(APIManager.getFileUrl(getContext(), mPostEditQADetail.getFilePath()))){
                    isEditFile = true;
                }
            }

            if(!isEditCate && !isEditCategory && !isEditTeacher && !isEditBook && !isEditBookPage &&
                    !isEditLectureNo && !isEditTitle && !isEditContent && !isAddFile && !isEditFile){
                return false;
            }

            return true;
        }

        return false;
    }

    @Override
    protected boolean validateDeleteRequest(){
        if(mPostEditQADetail != null){
            return (mPostEditQADetail.getFilePath() != null && mPath == null);
        }

        return false;
    }

    ////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void showDialog(int position){
        if(mAdapter.checkQAMenuPrevIndexIsEmpty(position)) {

            if(position == Tags.STUDY_QA_MENU_INDEX.QA_MENU_SUBJECT){
                BraveUtils.showSelectAlertDialog(getActivity(), mQASubjectList,
                        mAdapter.getQAMenuIndex(position), mSelectSubjectDialogListener);
            }else if(position == Tags.STUDY_QA_MENU_INDEX.QA_MENU_TYPE) {
                BraveUtils.showSelectAlertDialog(getActivity(), mQATypeList,
                        mAdapter.getQAMenuIndex(position), mSelectTypeDialogListener);
            }else if(position == Tags.STUDY_QA_MENU_INDEX.QA_MENU_TEACHER) {
                BraveUtils.showSelectAlertDialog(getActivity(), mQATeacherList,
                        mAdapter.getQAMenuIndex(position), mSelectTeacherDialogListener);
            }else if(position == Tags.STUDY_QA_MENU_INDEX.QA_MENU_LECTURE) {
                if (mQALectureList != null && mQALectureList.size() != 0) {
                    BraveUtils.showSelectAlertDialog(getActivity(), mQALectureList,
                            mAdapter.getQAMenuIndex(position), mSelectLectureDialogListener);
                } else {
                    if (ModooGong.isRequiredQAMenuLecture) {
                        BraveUtils.showToast(getActivity(), getString(R.string.toast_qa_dialog_no_lecture_list_required));
                    } else {
                        BraveUtils.showToast(getActivity(), getString(R.string.toast_qa_dialog_no_lecture_list));
                    }
                }
            }else if(position == Tags.STUDY_QA_MENU_INDEX.QA_MENU_BOOK){
                if(mQABookList != null && mQABookList.size() != 0) {
                    BraveUtils.showSelectAlertDialog(getActivity(), mQABookList,
                            mAdapter.getQAMenuIndex(position), mSelectBookDialogListener);
                }else {
                    BraveUtils.showToast(getActivity(), getString(R.string.toast_qa_dialog_no_book_list));
                }
            }else if(position == Tags.STUDY_QA_MENU_INDEX.QA_MENU_PAGE){
                BraveUtils.showInputAlertDialog(getActivity(), InputType.TYPE_CLASS_TEXT,
                        getString(R.string.study_qa_menu_book_page_hint), mAdapter.getQAMenuValue(position), mInputPageDialogListener);
            }
        }else{

            Toast.makeText(getActivity(), getString(R.string.toast_qa_prev_empty),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private MaterialDialog.ListCallbackSingleChoice mSelectSubjectDialogListener = new MaterialDialog.ListCallbackSingleChoice() {
        @Override
        public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
            mAdapter.updateQAMenuIndexValue(Tags.STUDY_QA_MENU_INDEX.QA_MENU_SUBJECT, which, String.valueOf(text));
            initShowBySubject();
            return true;
        }
    };

    private MaterialDialog.ListCallbackSingleChoice mSelectTypeDialogListener = new MaterialDialog.ListCallbackSingleChoice() {
        @Override
        public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
            mAdapter.updateQAMenuIndexValue(Tags.STUDY_QA_MENU_INDEX.QA_MENU_TYPE, which, String.valueOf(text));
            return true;
        }
    };

    private MaterialDialog.ListCallbackSingleChoice mSelectTeacherDialogListener = new MaterialDialog.ListCallbackSingleChoice() {
        @Override
        public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
            mAdapter.updateQAMenuIndexValue(Tags.STUDY_QA_MENU_INDEX.QA_MENU_TEACHER, which, String.valueOf(text));
            initShowByTeacher();
            return true;
        }
    };

    private MaterialDialog.ListCallbackSingleChoice mSelectLectureDialogListener = new MaterialDialog.ListCallbackSingleChoice() {
        @Override
        public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
            String lecture = String.valueOf(text);
            if(lecture.equals(getString(R.string.study_qa_menu_select_default))){
                mAdapter.resetQAMenuIndexValue(Tags.STUDY_QA_MENU_INDEX.QA_MENU_LECTURE);
            }else {
                mAdapter.updateQAMenuIndexValue(Tags.STUDY_QA_MENU_INDEX.QA_MENU_LECTURE, which, String.valueOf(text));
            }
            return true;
        }
    };

    private MaterialDialog.ListCallbackSingleChoice mSelectBookDialogListener = new MaterialDialog.ListCallbackSingleChoice() {
        @Override
        public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
            String book = String.valueOf(text);
            if(book.equals(getString(R.string.study_qa_menu_select_default))){
                mAdapter.resetQAMenuIndexValue(Tags.STUDY_QA_MENU_INDEX.QA_MENU_BOOK);
            }else{
                mAdapter.updateQAMenuIndexValue(Tags.STUDY_QA_MENU_INDEX.QA_MENU_BOOK, which, book);
            }
            return true;
        }
    };

    private MaterialDialog.InputCallback mInputPageDialogListener = new MaterialDialog.InputCallback() {
        @Override
        public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
            String page = String.valueOf(input);
            if(page.equals("")){
                mAdapter.resetQAMenuIndexValue(Tags.STUDY_QA_MENU_INDEX.QA_MENU_PAGE);
            }else {
                mAdapter.updateQAMenuIndexValue(Tags.STUDY_QA_MENU_INDEX.QA_MENU_PAGE, 0, page);
            }
        }
    };

    private void goVoiceRecord(){
        Intent intent = new Intent(getActivity(), VoiceRecordActivity.class);
        startActivityForResult(intent, RequestCode.REQUEST_RECORD);
    }

    @Override
    protected boolean checkAddAvailable(){
        return (!btnImage.isActivated() && !btnVoice.isActivated());
    }

    // =============================================================================
    // Preview
    // =============================================================================

    @Override
    protected void setVisibilityBtnUpload(boolean show){
        if(show){
            btnImage.setVisibility(View.VISIBLE);
            btnImage.setActivated(false);
            if(ModooGong.isShowQAVoiceRecoder) {
                btnVoice.setVisibility(View.VISIBLE);
                btnVoice.setActivated(false);
                divider.setVisibility(View.VISIBLE);
            }
        }else{
            btnImage.setVisibility(View.GONE);
            if(ModooGong.isShowQAVoiceRecoder) {
                btnVoice.setVisibility(View.GONE);
                divider.setVisibility(View.GONE);
            }
        }
    }

    //// Voice //////////////////////////////////////////////////////////////////////

    @Override
    protected void updateUploadView(){
        if(ModooGong.isShowQAVoiceRecoder) {
            btnVoice.setActivated(true);
        }else{
            setVisibilityBtnUpload(false);
        }
    }

    // =============================================================================
    // Check Permission
    // =============================================================================

    private PermissionListener mStoragePermissionListener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            if(BraveUtils.checkExternalStorageMounted(getActivity())){
                BraveUtils.checkPermission(getContext(), mRecordPermissionListener,
                        getString(R.string.check_permission_record_audio),
                        Manifest.permission.RECORD_AUDIO);
            }
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            //do nothing
        }
    };

    private PermissionListener mRecordPermissionListener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            if(BraveUtils.checkMicrophone(getActivity())) {
                goVoiceRecord();

                Answers.getInstance().logCustom(new CustomEvent(AnalysisTags.DOQASTUDY)
                        .putCustomAttribute(AnalysisTags.ACTION, "go_voice_record"));
            }
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            //do nothing
        }
    };
}
