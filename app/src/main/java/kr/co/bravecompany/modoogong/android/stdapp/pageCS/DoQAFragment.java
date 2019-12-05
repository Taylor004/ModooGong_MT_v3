package kr.co.bravecompany.modoogong.android.stdapp.pageCS;

import android.Manifest;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.gun0912.tedpermission.PermissionListener;
import com.kollus.sdk.media.util.Utils;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.modoogong.android.stdapp.adapter.QAMenuAdapter;
import kr.co.bravecompany.api.android.stdapp.api.OnResultListener;
import kr.co.bravecompany.api.android.stdapp.api.utils.APIManager;
import kr.co.bravecompany.api.android.stdapp.api.requests.BoardRequests;
import kr.co.bravecompany.api.android.stdapp.api.requests.QARequests;
import kr.co.bravecompany.api.android.stdapp.api.data.BoardCateResult;
import kr.co.bravecompany.api.android.stdapp.api.data.BoardCateVO;
import kr.co.bravecompany.api.android.stdapp.api.data.BoardDelFileRequest;
import kr.co.bravecompany.api.android.stdapp.api.data.OneToOneQARequest;
import kr.co.bravecompany.api.android.stdapp.api.data.PostResult;
import kr.co.bravecompany.modoogong.android.stdapp.config.AnalysisTags;
import kr.co.bravecompany.modoogong.android.stdapp.config.LocalAddress;
import kr.co.bravecompany.modoogong.android.stdapp.config.RequestCode;
import kr.co.bravecompany.modoogong.android.stdapp.config.Tags;
import kr.co.bravecompany.modoogong.android.stdapp.fragment.BaseFragment;
import kr.co.bravecompany.modoogong.android.stdapp.fragment.OneToOneQAFragment;
import kr.co.bravecompany.modoogong.android.stdapp.manager.MediaPlayerManager;
import kr.co.bravecompany.modoogong.android.stdapp.utils.BraveUtils;
import kr.co.bravecompany.modoogong.android.stdapp.utils.MediaScanner;
import kr.co.bravecompany.modoogong.android.stdapp.utils.ProgressRunnable;
import kr.co.bravecompany.modoogong.android.stdapp.view.CustomSeekBar;
import kr.co.bravecompany.modoogong.android.stdapp.view.OnProgressFinishListener;
import kr.co.bravecompany.modoogong.android.stdapp.viewholder.OnItemClickListener;
import kr.co.bravecompany.api.android.stdapp.api.data.OneToOneQADetailVO;
import kr.co.bravecompany.modoogong.android.stdapp.data.QAMenuData;
import okhttp3.Request;

/**
 * Created by BraveCompany on 2016. 11. 3..
 */

public class DoQAFragment extends BaseFragment {

    private int index = Tags.QA_TYPE.QA_ONE_TO_ONE;
    protected FrameLayout contentCall;
    private TextView btnCall;
    private boolean isShowContentCall = true;

    private List<String> mQATypeList;
    private ArrayList<BoardCateVO> mQAType = new ArrayList<>();

    protected EditText editQATitle;
    protected EditText editQAContent;

    protected ImageView btnImage;

    //preview
    private FrameLayout contentUpload;
    private ImageView btnClose;
    private LinearLayout layoutUploadImg;
    private ImageView imgThum;
    private TextView txtImgName;
    private LinearLayout layoutUploadVoice;
    private ImageView btnPlay;
    private CustomSeekBar progressPlay;
    private TextView txtPlay;
    private LinearLayout layoutUploadFile;
    private TextView txtFileName;

    //player
    private MediaPlayerManager mPlayerManager;

    protected RecyclerView mListView;
    protected QAMenuAdapter mAdapter;
    protected LinearLayoutManager mLayoutManager;

    protected String mPath;
    protected String mFileName;

    private OneToOneQADetailVO mEditQADetail;
    private OneToOneQADetailVO mPostEditQADetail;

    protected Uri mCameraUri = null;

    private boolean isResumed = false;
    
    public static DoQAFragment newInstance() {
        return new DoQAFragment();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            updateResult(requestCode, data);
        }
    }

    protected void updateResult(int requestCode, Intent data){
        switch (requestCode) {
            case RequestCode.REQUEST_RC_GALLERY:
                Uri uri = data.getData();
                String[] projection = {MediaStore.Images.Media.DATA};
                Cursor c = getContext().getContentResolver().query(uri, projection, null, null, null);
                if (c.moveToNext()) {
                    String path = c.getString(c.getColumnIndex(MediaStore.Images.Media.DATA));
                    path = BraveUtils.makeImageSample(getContext(), path);
                    updatePreview(Tags.QA_UPLOAD_TYPE.QA_UPLOAD_IMAGE, path, null);
                }
                break;
            case RequestCode.REQUEST_RC_CAMERA:

                /* Lee
                if(mCameraUri != null) {
                    String path = mCameraUri.getPath();

                    if (path != null) {
                        MediaScanner.scanMedia(getContext(), path);
                        path = BraveUtils.makeImageSample(getContext(), path);
                        updatePreview(Tags.QA_UPLOAD_TYPE.QA_UPLOAD_IMAGE, path, null);
                    }
                }
                */
                //https://black-jin0427.tistory.com/120
                if(mCameraPhotoFile !=null)
                {
                    String path = mCameraPhotoFile.getAbsolutePath();

                    if (path != null) {
                        MediaScanner.scanMedia(getContext(), path);
                        path = BraveUtils.makeImageSample(getContext(), path);
                        updatePreview(Tags.QA_UPLOAD_TYPE.QA_UPLOAD_IMAGE, path, null);
                    }
                }
                break;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        updateOrientationConfigChanged(newConfig.orientation);
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
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_do_qa, container, false);

        setHasOptionsMenu(true);

        initLayout(rootView);
        initListener();
        initData();
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_cs_do_qa, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_do_qa) {
            postQA();
        }
        else
            return super.onOptionsItemSelected(item);

        return true;
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

        contentCall = (FrameLayout)rootView.findViewById(R.id.contentCall);
        btnCall = (TextView)rootView.findViewById(R.id.btnCall);
        contentCall.setVisibility(View.VISIBLE);

        editQATitle = (EditText) rootView.findViewById(R.id.editQATitle);
        editQAContent = (EditText) rootView.findViewById(R.id.editQAContent);

        btnImage = (ImageView) rootView.findViewById(R.id.btnImage);

        //preview
        contentUpload = (FrameLayout) rootView.findViewById(R.id.contentUpload);
        btnClose = (ImageView) rootView.findViewById(R.id.btnClose);
        //image
        layoutUploadImg = (LinearLayout) rootView.findViewById(R.id.layoutUploadImg);
        imgThum = (ImageView) rootView.findViewById(R.id.imgThum);
        imgThum.setScaleType(ImageView.ScaleType.CENTER_CROP);
        txtImgName = (TextView)rootView.findViewById(R.id.txtImgName);
        //voice
        layoutUploadVoice = (LinearLayout)rootView.findViewById(R.id.layoutUploadVoice);
        btnPlay = (ImageView)rootView.findViewById(R.id.btnPlay);
        progressPlay = (CustomSeekBar) rootView.findViewById(R.id.progressPlay);
        txtPlay = (TextView)rootView.findViewById(R.id.txtPlay);
        txtPlay.setText(BraveUtils.formatTime(0));
        //file
        layoutUploadFile = (LinearLayout)rootView.findViewById(R.id.layoutUploadFile);
        txtFileName = (TextView)rootView.findViewById(R.id.txtFileName);

        mPlayerManager = new MediaPlayerManager();

        mListView = (RecyclerView)rootView.findViewById(R.id.recyclerQA);

        initMenuAdapter();
    }

    protected void initMenuAdapter(){
        mAdapter = new QAMenuAdapter();
        mListView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(getContext());
        mListView.setLayoutManager(mLayoutManager);
    }

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

                int selected = ((OneToOneQAFragment)getParentFragment()).getSelectedPage();
                if(isResumed && selected == Tags.QA_PAGE_INDEX.QA_PAGE_Q_DO && mAdapter.getItemCount() > 1){
                    BraveUtils.showToast(getActivity(), getString(R.string.toast_qa_guide_landscape));
                }

                isShowContentCall = false;
                contentCall.setVisibility(View.GONE);
                break;
            case Configuration.ORIENTATION_PORTRAIT:
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                mListView.setLayoutParams(params);
                mListView.setVerticalScrollBarEnabled(false);

                isShowContentCall = true;
                contentCall.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    protected void initListener() {

        /* Luke, 임시!
        ((MainActivity)getActivity()).setOnOkBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postQA();
            }
        });
        */

        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                showDialog(position);
            }
        });

        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkAddAvailable()) {
                    BraveUtils.checkPermission(getActivity(), mStoragePermissionListener,
                            getString(R.string.check_permission_external_storage),
                            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                } else {
                    Toast.makeText(getActivity(), getString(R.string.toast_qa_add),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        layoutUploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go viewer
                if (mPath != null && mPath.length() != 0) {
                    BraveUtils.goPhotoView(getActivity(), mPath);

                    Answers.getInstance().logCustom(new CustomEvent(AnalysisTags.DOQA)
                            .putCustomAttribute(AnalysisTags.ACTION, "go_photo_view"));
                }
            }
        });

        layoutUploadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPath != null && mPath.length() != 0) {
                    BraveUtils.checkPermission(getActivity(), mDownloadPermissionListener,
                            getString(R.string.check_permission_external_storage),
                            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean play = !btnPlay.isActivated();
                onPlay(play);

                Answers.getInstance().logCustom(new CustomEvent(AnalysisTags.DOQA)
                        .putCustomAttribute(AnalysisTags.ACTION, "do_play_voice_record")
                        .putCustomAttribute(AnalysisTags.VALUE, String.valueOf(play)));
            }
        });

        progressPlay.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(layoutUploadVoice.getVisibility() == View.VISIBLE) {
                    if (fromUser) {
                        if (mPlayerManager.isPlaying()) {
                            updateSeekTo(progress);
                        } else {
                            txtPlay.setText(BraveUtils.formatTime(progress));
                        }
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        progressPlay.setOnProgressFinish(new OnProgressFinishListener() {
            @Override
            public void onProgressFinish(View progressBar) {
                resetPlay();
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickClosePreview();
            }
        });

        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //BraveUtils.goCall(getActivity(), getString(R.string.ono_to_ono_qa_call_uri));
                BraveUtils.checkPermission(getContext(), mCallPermissionListener,
                        getString(R.string.check_permission_call_phone),
                        Manifest.permission.CALL_PHONE);
            }
        });

        KeyboardVisibilityEvent.setEventListener(getActivity(), new KeyboardVisibilityEventListener() {
            @Override
            public void onVisibilityChanged(boolean isOpen) {
                setVisibilityViews(isOpen);
            }
        });
    }

    private void clickClosePreview(){
        contentUpload.setVisibility(View.GONE);
        layoutUploadImg.setVisibility(View.GONE);
        layoutUploadVoice.setVisibility(View.GONE);
        layoutUploadFile.setVisibility(View.GONE);
        setVisibilityBtnClose(true);
        setVisibilityBtnUpload(true);
        resetResource();
        mPath = null;
        mFileName = null;
        mCameraUri = null;
    }

    protected void setVisibilityViews(boolean isOpen){
        if (isOpen) {
            BraveUtils.setVisibilityTopView(mListView, View.GONE);
            if(isShowContentCall) {
                BraveUtils.setVisibilityBottomView(contentCall, View.GONE);
            }
        } else {
            BraveUtils.setVisibilityTopView(mListView, View.VISIBLE);
            if(isShowContentCall) {
                BraveUtils.setVisibilityBottomView(contentCall, View.VISIBLE);
            }
        }
    }

    public void resetViews(){
        mAdapter.resetQAMenuIndexValue();
        editQATitle.setText("");
        editQAContent.setText("");
        clickClosePreview();
    }

    @Override
    protected void setData(String data) {
        if(data != null){
            mEditQADetail = (OneToOneQADetailVO) BraveUtils.toJsonString(data, OneToOneQADetailVO.class);
        }
    }

    private void setEditData(OneToOneQADetailVO editData){
        if(editData != null) {
            for(int i=0; i<mQAType.size(); i++) {
                BoardCateVO boardCateVO = mQAType.get(i);
                if(boardCateVO.getCD().equals(editData.getC_code())){
                    mAdapter.updateQAMenuIndexValue(Tags.ONE_TO_ONE_QA_MENU_INDEX.QA_MENU_TYPE, i, boardCateVO.getNM());
                    break;
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
    protected void initData() {
        mEditQADetail = null;
        initMenuData();
    }

    private void initMenuData(){
        mPostEditQADetail = null;
        mAdapter.clear();

        loadMenuData();

        ArrayList<QAMenuData> menus = new ArrayList<QAMenuData>();
        List<String> qa_menu = Arrays.asList(getResources().getStringArray(R.array.one_to_one_qa_menu));
        for(String menu : qa_menu){
            QAMenuData m = new QAMenuData();
            m.setTitle(menu);
            menus.add(m);
        }

        mAdapter.addAll(menus);
        resetViews();

        onConfigurationChanged(getResources().getConfiguration());
    }

    protected void loadMenuData(){
        startLoading();
        BoardRequests.getInstance().requestOneToOneQABoardCateList(new OnResultListener<BoardCateResult>(){

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

    protected void setMenuData(BoardCateResult result){
        if(result == null){
            return;
        }

        if(result.getBoardCates() != null) {
            ArrayList<BoardCateVO> boardCates = result.getBoardCates().getBOARD_CATE().getBoardCatesOneToOneQA();
            if (boardCates != null && boardCates.size() != 0) {
                mQATypeList = new ArrayList<>();
                for (int i = 0; i < boardCates.size(); i++) {
                    mQATypeList.add(boardCates.get(i).getNM());
                }
                mQAType = boardCates;
                mAdapter.setQAType(boardCates);
            }
        }

        if(mEditQADetail != null) {
            setEditData(mEditQADetail);
            mPostEditQADetail = mEditQADetail;
            mEditQADetail = null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////

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

    protected void updateQA(){
        String typeName = null;
        OneToOneQARequest request = new OneToOneQARequest();
        int type = mAdapter.getQAMenuIndex(Tags.ONE_TO_ONE_QA_MENU_INDEX.QA_MENU_TYPE);

        request.setCategory(mQAType.get(type).getCD());
        typeName = mQAType.get(type).getNM();
        request.setTitle(BraveUtils.toHTMLEditTitle(editQATitle.getText().toString()));
        //request.setContent(editQAContent.getText().toString());
        request.setContent(BraveUtils.toHTMLEditContent(editQAContent.getText().toString()));

        if(mPath != null) {
            if(!(mPostEditQADetail != null && mPostEditQADetail.getFilePath() != null &&
                    mPath.equals(APIManager.getFileUrl(getContext(), mPostEditQADetail.getFilePath())))){
                request.setFile(new File(mPath));
            }
        }

        if(mPostEditQADetail != null){
            request.setQnaNo(mPostEditQADetail.getQnaNo());

            boolean hasFile = (request.getFile() != null);
            CustomEvent event = new CustomEvent(AnalysisTags.DOQAONETOONE)
                    .putCustomAttribute(AnalysisTags.ACTION, "post_edit_qa")
                    .putCustomAttribute(AnalysisTags.FILE, String.valueOf(hasFile));
            if(typeName != null){
                event.putCustomAttribute(AnalysisTags.VALUE, typeName);
            }
            Answers.getInstance().logCustom(event);
        }else{
            request.setQnaNo(-1);

            boolean hasFile = (request.getFile() != null);
            CustomEvent event = new CustomEvent(AnalysisTags.DOQAONETOONE)
                    .putCustomAttribute(AnalysisTags.ACTION, "post_new_qa")
                    .putCustomAttribute(AnalysisTags.FILE, String.valueOf(hasFile));
            if(typeName != null){
                event.putCustomAttribute(AnalysisTags.VALUE, typeName);
            }
            Answers.getInstance().logCustom(event);
        }

        startLoading();
        QARequests.getInstance().updateOneToOneQA(request, new OnResultListener<PostResult>() {
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

    protected void deleteQAFile(){
        if(mPostEditQADetail != null) {
            BoardDelFileRequest request = new BoardDelFileRequest();
            request.setListNo(mPostEditQADetail.getQnaNo());

            Answers.getInstance().logCustom(new CustomEvent(AnalysisTags.DOQAONETOONE)
                    .putCustomAttribute(AnalysisTags.ACTION, "post_delete_qa_attach"));

            startLoading();
            BoardRequests.getInstance().deleteOneToOneQAFile(request, new OnResultListener<PostResult>() {
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

    protected void goQAList(){
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                stopLoading();
               // ((OneToOneQAFragment) getParentFragment()).movePage(Tags.QA_PAGE_INDEX.QA_PAGE_Q_LIST, null);

                ((OneToOneCSActivity) getActivity()).movePage(Tags.QA_PAGE_INDEX.QA_PAGE_Q_LIST, null);
            }
        }, 1000);
    }

    protected boolean validateUpdateRequest(){
        int check = mAdapter.checkQAMenuIndexEmpty();

        switch (check){
            case Tags.ONE_TO_ONE_QA_MENU_INDEX.QA_MENU_TYPE:
                BraveUtils.showToast(getActivity(), getString(R.string.toast_qa_no_type));
                return false;
            default:
                break;
        }

        if(TextUtils.isEmpty(editQATitle.getText()) || TextUtils.isEmpty(editQAContent.getText())) {
            BraveUtils.showToast(getActivity(), getString(R.string.toast_qa_no_title_content));
            return false;
        }

        return true;
    }

    protected boolean validateEditRequest(){
        if(mPostEditQADetail != null){
            int type = mAdapter.getQAMenuIndex(Tags.ONE_TO_ONE_QA_MENU_INDEX.QA_MENU_TYPE);
            boolean isEditCategory = !mPostEditQADetail.getC_code().equals(mQAType.get(type).getCD());
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

            if(!isEditCategory && !isEditTitle && !isEditContent && !isAddFile && !isEditFile){
                return false;
            }

            return true;
        }

        return false;
    }

    protected boolean validateDeleteRequest(){
        if(mPostEditQADetail != null){
            return (mPostEditQADetail.getFilePath() != null && mPath == null);
        }

        return false;
    }

    ////////////////////////////////////////////////////////////////////////////////////

    protected void showDialog(int position) {
        if(mAdapter.checkQAMenuPrevIndexIsEmpty(position)) {
            switch (position) {
                case Tags.ONE_TO_ONE_QA_MENU_INDEX.QA_MENU_TYPE:
                    BraveUtils.showSelectAlertDialog(getActivity(), mQATypeList,
                            mAdapter.getQAMenuIndex(position), mSelectTypeDialogListener);
                    break;
            }
        }else{
            Toast.makeText(getActivity(), getString(R.string.toast_qa_prev_empty),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private MaterialDialog.ListCallbackSingleChoice mSelectTypeDialogListener = new MaterialDialog.ListCallbackSingleChoice() {
        @Override
        public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
            mAdapter.updateQAMenuIndexValue(Tags.ONE_TO_ONE_QA_MENU_INDEX.QA_MENU_TYPE, which, String.valueOf(text));
            return true;
        }
    };

    private void goGetImage(){
        List<String> items = Arrays.asList(getResources().getStringArray(R.array.qa_image_type));
        BraveUtils.showListAlertDialog(getActivity(), items, mSelectImageTypeDialogListner);
    }

    private MaterialDialog.ListCallback mSelectImageTypeDialogListner = new MaterialDialog.ListCallback() {

        @Override
        public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
            if (which == 0) {
                if(BraveUtils.checkCamera(getActivity())) {
                    getImageFromCamera();
                }
            } else {
                getImageFromGallery();
            }
        }
    };

    /* Lee 카메라 접근 오류 수정
    private void getImageFromCamera() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String child = LocalAddress.NAME + "_" + System.currentTimeMillis() + ".jpg";

        mCameraUri = Uri.fromFile(new File(BraveUtils.getDCIMFilePath(LocalAddress.FOLDER_CAMERA, child)));

        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mCameraUri);
        startActivityForResult(intent, RequestCode.REQUEST_RC_CAMERA);

        Answers.getInstance().logCustom(new CustomEvent(AnalysisTags.DOQA)
                .putCustomAttribute(AnalysisTags.ACTION, "go_image_from_camera"));
    }
    */

    File mCameraPhotoFile;

    //출처: https://kyome.tistory.com/9 [KYOME ]
    private void getImageFromCamera() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String child = LocalAddress.NAME + "_" + System.currentTimeMillis() + ".jpg";

        mCameraPhotoFile = new File(BraveUtils.getDCIMFilePath(LocalAddress.FOLDER_CAMERA, child));

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N)
        {

            String provider = getContext().getPackageName() + ".fileprovider";
            mCameraUri = FileProvider.getUriForFile(getContext(), provider, mCameraPhotoFile);
        }
        else
        {
            mCameraUri = Uri.fromFile(mCameraPhotoFile);
        }

        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mCameraUri);
        startActivityForResult(intent, RequestCode.REQUEST_RC_CAMERA);

        Answers.getInstance().logCustom(new CustomEvent(AnalysisTags.DOQA)
                .putCustomAttribute(AnalysisTags.ACTION, "go_image_from_camera"));
    }

    private void getImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, RequestCode.REQUEST_RC_GALLERY);

        Answers.getInstance().logCustom(new CustomEvent(AnalysisTags.DOQA)
                .putCustomAttribute(AnalysisTags.ACTION, "go_image_from_gallery"));
    }


    protected boolean checkAddAvailable() {
        return (!btnImage.isActivated());
    }

    // =============================================================================
    // Preview
    // =============================================================================

    private void setVisibilityBtnClose(boolean show){
        if(show){
            btnClose.setVisibility(View.VISIBLE);
        }else{
            btnClose.setVisibility(View.GONE);
        }
    }

    protected void setVisibilityBtnUpload(boolean show){
        if(show){
            btnImage.setVisibility(View.VISIBLE);
            btnImage.setActivated(false);
        }else{
            btnImage.setVisibility(View.GONE);
        }
    }

    protected void updatePreview(int type, String path, String name) {
        if(path != null) {
            if(name == null){
                name = BraveUtils.getFileName(path);
            }
            switch (type) {
                case Tags.QA_UPLOAD_TYPE.QA_UPLOAD_IMAGE:
                    setupImage(path, name);
                    break;
                case Tags.QA_UPLOAD_TYPE.QA_UPLOAD_VOICE:
                    setupPlayer(path, name);
                    break;
                case Tags.QA_UPLOAD_TYPE.QA_UPLOAD_FILE:
                    setupFile(path, name);
                    break;
            }
        }
    }

    //// Image //////////////////////////////////////////////////////////////////////

    private void setupImage(String path, String name){
        contentUpload.setVisibility(View.VISIBLE);
        layoutUploadImg.setVisibility(View.VISIBLE);

        int size = (int)getResources().getDimension(R.dimen.common_icon_size);
        BraveUtils.setImage(imgThum, path, size, size);
        txtImgName.setText(name);

        mPath = path;
        mFileName = name;
        btnImage.setActivated(true);
    }

    private void setupFile(String path, String name){
        contentUpload.setVisibility(View.VISIBLE);
        layoutUploadFile.setVisibility(View.VISIBLE);

        txtFileName.setText(name);

        mPath = path;
        mFileName = name;
        setVisibilityBtnUpload(false);
    }

    //// Voice //////////////////////////////////////////////////////////////////////

    private void setupPlayer(String path, String name){
        startLoading();
        if(mPlayerManager.setupPlaying(path)) {
            mPath = path;
            mFileName = name;
            mPlayerManager.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    stopLoading();
                    updatePlayerView();
                }
            });
        }else{
            stopLoading();
            Toast.makeText(getActivity(), getString(R.string.toast_player_setup_failed),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void updatePlayerView(){
        contentUpload.setVisibility(View.VISIBLE);
        layoutUploadVoice.setVisibility(View.VISIBLE);

        btnPlay.setActivated(false);
        progressPlay.setProgress(0);
        progressPlay.setMax(mPlayerManager.getDuration());
        txtPlay.setText(BraveUtils.formatTime(mPlayerManager.getDuration()));

        Handler handler = new Handler();
        mPlayerManager.setupHandler(handler, new ProgressRunnable(
                handler, progressPlay, txtPlay, mPlayerManager.getDuration()));

        mPlayerManager.setupPlayButton(btnPlay);
        updateUploadView();
    }

    protected void updateUploadView(){
        setVisibilityBtnUpload(false);
    }

    private void onPlay(boolean start) {
        if (start) {
            mPlayerManager.startPlaying(progressPlay.getProgress());
        } else {
            mPlayerManager.pausePlaying();
        }
    }

    private void updateSeekTo(int progress){
        mPlayerManager.setSeekTo(progress);
    }

    private void resetPlay(){
        onPlay(false);
        progressPlay.setProgressWithAnimation(0, 10);
        txtPlay.setText(BraveUtils.formatTime(mPlayerManager.getDuration()));
    }

    private void resetResource(){
        mPlayerManager.stopPlaying();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mPlayerManager != null)
            mPlayerManager.releasePlayer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    // =============================================================================
    // Check Permission
    // =============================================================================

    private PermissionListener mStoragePermissionListener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            if(BraveUtils.checkExternalStorageMounted(getActivity())){
                goGetImage();
            }
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            //do nothing
        }
    };

    private PermissionListener mCallPermissionListener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            if(BraveUtils.checkTelephony(getActivity())) {
                BraveUtils.goCall(getActivity(), getString(R.string.one_to_one_qa_call_uri));

                Answers.getInstance().logCustom(new CustomEvent(AnalysisTags.DOQA)
                        .putCustomAttribute(AnalysisTags.ACTION, "go_call"));
            }
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            //do nothing
        }
    };

    private PermissionListener mDownloadPermissionListener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            if(BraveUtils.checkExternalStorageMounted(getActivity())){
                if (mPath != null && mPath.length() != 0) {
                    BraveUtils.doAttachDownload(getContext(), mPath, mFileName);

                    Answers.getInstance().logCustom(new CustomEvent(AnalysisTags.DOQA)
                            .putCustomAttribute(AnalysisTags.ACTION, "download_attach"));
                }
            }
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            //do nothing
        }
    };
}
