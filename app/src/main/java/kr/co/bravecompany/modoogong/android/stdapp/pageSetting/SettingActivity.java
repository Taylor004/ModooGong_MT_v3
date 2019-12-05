package kr.co.bravecompany.modoogong.android.stdapp.pageSetting;

import android.content.res.Configuration;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.modoogong.android.stdapp.activity.DownBaseActivity;
import kr.co.bravecompany.modoogong.android.stdapp.application.MyFirebaseMessagingService;
import kr.co.bravecompany.modoogong.android.stdapp.config.AnalysisTags;
import kr.co.bravecompany.modoogong.android.stdapp.config.Tags;
import kr.co.bravecompany.modoogong.android.stdapp.download.OnDownloadBindListener;
import kr.co.bravecompany.modoogong.android.stdapp.download.OnDownloadDataListener;
import kr.co.bravecompany.modoogong.android.stdapp.manager.PropertyManager;
import kr.co.bravecompany.modoogong.android.stdapp.utils.SystemUtils;
import kr.co.bravecompany.modoogong.android.stdapp.utils.BraveUtils;
import kr.co.bravecompany.modoogong.android.stdapp.data.SettingData;

public class SettingActivity extends DownBaseActivity {

    private Toolbar mToolbar;

    private RecyclerView mListView;
    private SettingAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private TextView txtVersion;
    private TextView btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSystemBar(true);

        setContentView(R.layout.activity_setting);
        initLayout();
        initListener();
    }

    @Override
    protected void initLayout() {

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);

        TextView title = (TextView)mToolbar.findViewById(R.id.toolbar_title);
        title.setText(getString(R.string.setting_toolbar_title));

        mAdapter = new SettingAdapter();
        mListView = (RecyclerView)findViewById(R.id.recyclerSetting);
        mListView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(getApplicationContext()){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mListView.setLayoutManager(mLayoutManager);

        txtVersion = (TextView)findViewById(R.id.txtVersion);
        btnLogout = (TextView)findViewById(R.id.btnLogout);

    }

    @Override
    protected void initListener() {

        mAdapter.setOnSwitchCheckedChangeListener(new SettingSwitchItemViewHolder.OnSwitchCheckedChangeListener() {
            @Override
            public void onCheckedChanged(View view, int position, boolean isChecked) {
                //value of shared preference change
                String value = String.valueOf(isChecked);
                String action = "setting";
                CustomEvent event = new CustomEvent(AnalysisTags.SETTING);
                switch (position){
                    case Tags.SETTING_SWITCH_INDEX.SETTING_NOTICE_DATA:
                        PropertyManager.getInstance().setNoticeData(isChecked);

                        action = "set_notice_data";
                        break;
                    case Tags.SETTING_SWITCH_INDEX.SETTING_PUSH:
                        PropertyManager.getInstance().setPush(isChecked);

                        MyFirebaseMessagingService.initPushSystem(getApplicationContext());

                        action = "set_push";
                        break;
                    case Tags.SETTING_PLAYER_INDEX.SETTING_AUTO_CONTINUE_PLAY:
                        PropertyManager.getInstance().setAutoContinuePlay(isChecked);

                        action = "set_auto_continue_play";
                        break;
                    case Tags.SETTING_PLAYER_INDEX.SETTING_SCREEN_ORIENTATION:
                        int orientation;
                        if(isChecked){
                            orientation = Configuration.ORIENTATION_LANDSCAPE;
                        }else{
                            orientation = Configuration.ORIENTATION_PORTRAIT;
                        }
                        PropertyManager.getInstance().setScreenOrientation(orientation);

                        action = "set_screen_orientation";
                        break;
                    case Tags.SETTING_PLAYER_INDEX.SETTING_SAVE_RATE:
                        PropertyManager.getInstance().setSaveRate(isChecked);

                        action = "set_save_rate";
                        break;
                    case Tags.SETTING_PLAYER_INDEX.SETTING_SAVE_BRIGHTNESS:
                        PropertyManager.getInstance().setSaveBrightness(isChecked);

                        action = "set_save_brightness";
                        break;
                    case Tags.SETTING_PLAYER_INDEX.SETTING_SW_CODEC:
                        PropertyManager.getInstance().setSWCodec(isChecked);

                        action = "set_sw_codec";
                        break;
                }
                event.putCustomAttribute(AnalysisTags.ACTION, action);
                event.putCustomAttribute(AnalysisTags.VALUE, value);
                Answers.getInstance().logCustom(event);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = getString(R.string.dialog_logout);
                BraveUtils.showAlertDialogOkCancel(SettingActivity.this, message,
                        new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                removeAllData();

                                Answers.getInstance().logCustom(new CustomEvent(AnalysisTags.SETTING)
                                        .putCustomAttribute(AnalysisTags.ACTION, "logout"));
                            }
                        }, new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                //nothing
                            }
                        });
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

    private void initData() {
        initSettingList();
        initVersion();

        btnLogout.setEnabled(BraveUtils.checkUserInfo());
    }

    private void initSettingList(){
        ArrayList<SettingData> settings = new ArrayList<SettingData>();
        SettingData divider = new SettingData();
        divider.setType(Tags.SETTING_TYPE.SETTING_DIVIDER);
        settings.add(divider);

        int prev = settings.size();
        List<String> setting_switch = Arrays.asList(getResources().getStringArray(R.array.setting_switch));
        for(int i=0; i<setting_switch.size(); i++){
            SettingData s = new SettingData();
            s.setType(Tags.SETTING_TYPE.SETTING_SWITCH);
            s.setTitle(setting_switch.get(i));
            int index = i + prev;
            switch (index){
                case Tags.SETTING_SWITCH_INDEX.SETTING_NOTICE_DATA:
                    s.setChecked(PropertyManager.getInstance().isNoticeData());
                    break;
                case Tags.SETTING_SWITCH_INDEX.SETTING_PUSH: {
                    s.setChecked(PropertyManager.getInstance().isPush());


                }
                    break;
            }
            settings.add(s);
        }
        SettingData divider1 = new SettingData();
        divider1.setType(Tags.SETTING_TYPE.SETTING_DIVIDER);
        settings.add(divider1);

        prev = settings.size();
        List<String> setting_player = Arrays.asList(getResources().getStringArray(R.array.setting_player));
        for(int i=0; i<setting_player.size(); i++){
            SettingData s = new SettingData();
            s.setType(Tags.SETTING_TYPE.SETTING_SWITCH);
            s.setTitle(setting_player.get(i));
            int index = i + prev;
            switch (index){
                case Tags.SETTING_PLAYER_INDEX.SETTING_AUTO_CONTINUE_PLAY:
                    s.setChecked(PropertyManager.getInstance().isAutoContinuePlay());
                    break;
                case Tags.SETTING_PLAYER_INDEX.SETTING_SCREEN_ORIENTATION:
                    s.setChecked(PropertyManager.getInstance().getScreenOrientation()
                            == Configuration.ORIENTATION_LANDSCAPE);
                    break;
                case Tags.SETTING_PLAYER_INDEX.SETTING_SAVE_RATE:
                    s.setChecked(PropertyManager.getInstance().isSaveRate());
                    break;
                case Tags.SETTING_PLAYER_INDEX.SETTING_SAVE_BRIGHTNESS:
                    s.setChecked(PropertyManager.getInstance().isSaveBrightness());
                    break;
                case Tags.SETTING_PLAYER_INDEX.SETTING_SW_CODEC:
                    s.setChecked(PropertyManager.getInstance().isSWCodec());
                    break;
            }
            settings.add(s);
        }
        SettingData divider2 = new SettingData();
        divider2.setType(Tags.SETTING_TYPE.SETTING_DIVIDER);
        settings.add(divider2);

        prev = settings.size();
        List<String> setting_text = Arrays.asList(getResources().getStringArray(R.array.setting_text));
        for(int i=0; i<setting_text.size(); i++){
            SettingData s = new SettingData();
            s.setType(Tags.SETTING_TYPE.SETTING_TEXT);
            s.setTitle(setting_text.get(i));
            if(mDownloadManager != null) {
                int index = i + prev;
                switch (index) {
                    case Tags.SETTING_TEXT_INDEX.SETTING_MEMORY_TOTAL:
                        s.setContent(BraveUtils.getTotalMemorySize(getApplicationContext()));
                        break;
                    case Tags.SETTING_TEXT_INDEX.SETTING_MEMORY_AVAILABLE:
                        s.setContent(BraveUtils.getAvailableMemorySize(getApplicationContext()));
                        break;
                    case Tags.SETTING_TEXT_INDEX.SETTING_MEMORY_STUDY:
                        s.setContent(mDownloadManager.getStorageDownloadSize());
                        break;
                    default:
                        s.setContent(getString(R.string.setting_memory_default));
                }
            }
            settings.add(s);
        }
        SettingData divider3 = new SettingData();
        divider3.setType(Tags.SETTING_TYPE.SETTING_DIVIDER);
        settings.add(divider3);

        mAdapter.addAll(settings);
    }

    private void initVersion(){
        String version = SystemUtils.getAppVersionName(getApplicationContext());
        if(version != null){
            txtVersion.setText(String.format(getString(R.string.setting_version), version));
        }
    }

    private void removeAllData(){
        if(mDownloadManager != null){
            startLoading();
            mDownloadManager.removeDownloadAll(new OnDownloadDataListener<String>() {
                @Override
                public void onDataProgress(int progress) {
                }

                @Override
                public void onDataComplete(String result) {
                    stopLoading();
                    PropertyManager.getInstance().removeAllPrefs();
                    BraveUtils.restartMain(SettingActivity.this);
                }

                @Override
                public void onDataError(int error) {
                    stopLoading();
                    BraveUtils.showLoadOnFailToast(SettingActivity.this);
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void updateDownloadViews(String studyKey, int state, int percent, int errorCode) {
    }
}
