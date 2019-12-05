package kr.co.bravecompany.modoogong.android.stdapp.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.crashlytics.android.answers.LoginEvent;

import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.api.android.stdapp.api.OnResultListener;
import kr.co.bravecompany.api.android.stdapp.api.config.ErrorType;
import kr.co.bravecompany.api.android.stdapp.api.requests.AuthRequests;
import kr.co.bravecompany.api.android.stdapp.api.data.LoginRequest;
import kr.co.bravecompany.api.android.stdapp.api.data.LoginResult;
import kr.co.bravecompany.modoogong.android.stdapp.application.ModooGong;
import kr.co.bravecompany.modoogong.android.stdapp.config.AnalysisTags;
import kr.co.bravecompany.modoogong.android.stdapp.config.Tags;
import kr.co.bravecompany.modoogong.android.stdapp.manager.PropertyManager;
import kr.co.bravecompany.modoogong.android.stdapp.utils.BraveUtils;
import kr.co.bravecompany.modoogong.android.stdapp.utils.SystemUtils;
import okhttp3.Request;

public class LoginActivity extends BaseActivity {
    private FrameLayout btnClose;
    private TextView btnLogin;

    private View layoutJoinLogin;
    private View layoutNoJoinLogin;

    private TextView txtFindID;
    private TextView txtFindPW;
    private TextView btnJoin;

    private EditText editID;
    private EditText editPW;

    private boolean isSplash = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        isSplash = getIntent().getBooleanExtra(Tags.TAG_SPLASH, false);

        initLayout();
        initListener();

        if(isSplash && !SystemUtils.isNetworkConnected(getApplicationContext())
                && PropertyManager.getInstance().isShowGoDownGuide()){
            startActivity(new Intent(LoginActivity.this, GoDownGuideActivity.class));
        }
    }

    private void initLayout(){

        btnClose = (FrameLayout)findViewById(R.id.btnClose);
        btnLogin = (TextView)findViewById(R.id.btnLogin);

        layoutJoinLogin = findViewById(R.id.layoutJoinLogin);
        layoutNoJoinLogin = findViewById(R.id.layoutNoJoinLogin);
        if(!ModooGong.isShowJoinLogin){
            layoutJoinLogin.setVisibility(View.INVISIBLE);
            layoutNoJoinLogin.setVisibility(View.VISIBLE);
        }

        txtFindID = (TextView)findViewById(R.id.txtFindID);
        txtFindID.setText(Html.fromHtml("<u>" + getString(R.string.login_find_id) + "</u>"));
        txtFindPW = (TextView)findViewById(R.id.txtFindPW);
        txtFindPW.setText(Html.fromHtml("<u>" + getString(R.string.login_find_pw) + "</u>"));
        btnJoin = (TextView)findViewById(R.id.btnJoin);

        editID = (EditText)findViewById(R.id.editID);
        editPW = (EditText)findViewById(R.id.editPW);
    }

    private void initListener(){

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                braveLogin();
            }
        });

        txtFindID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url;
                if(ModooGong.isModoo){
                    url = BraveUtils.getLinkModooLoginFindIDUrl(getApplicationContext());
                }else if(ModooGong.hasMobileWeb){
                    url = BraveUtils.getLinkLoginFindIDUrl(getApplicationContext());
                }else{
                    url = BraveUtils.getLinkWebLoginFindInfoUrl(getApplicationContext());
                }
                BraveUtils.goWeb(LoginActivity.this, url);

                Answers.getInstance().logCustom(new CustomEvent(AnalysisTags.LOGIN)
                        .putCustomAttribute(AnalysisTags.ACTION, "go_find_id"));
            }
        });

        txtFindPW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url;
                if(ModooGong.isModoo){
                    url = BraveUtils.getLinkModooLoginFindPWUrl(getApplicationContext());
                }else if(ModooGong.hasMobileWeb) {
                    url = BraveUtils.getLinkLoginFindPWUrl(getApplicationContext());
                }else{
                    url = BraveUtils.getLinkWebLoginFindInfoUrl(getApplicationContext());
                }
                BraveUtils.goWeb(LoginActivity.this, url);

                Answers.getInstance().logCustom(new CustomEvent(AnalysisTags.LOGIN)
                        .putCustomAttribute(AnalysisTags.ACTION, "go_find_pw"));
            }
        });

        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url;
                if(ModooGong.isModoo) {
                    url = BraveUtils.getLinkModooJoinUrl(getApplicationContext());
                }else if(ModooGong.hasMobileWeb) {
                    url = BraveUtils.getLinkJoinUrl(getApplicationContext());
                }else{
                    url = BraveUtils.getLinkWebJoinUrl(getApplicationContext());
                }
                BraveUtils.goWeb(LoginActivity.this, url);

                Answers.getInstance().logCustom(new CustomEvent(AnalysisTags.LOGIN)
                        .putCustomAttribute(AnalysisTags.ACTION, "go_join"));
            }
        });
    }

    private void braveLogin(){

        if(validateRequest()) {
            LoginRequest request = new LoginRequest();
            request.setId(editID.getText().toString().trim());
            request.setPassword(editPW.getText().toString().trim());
            request.setOs(SystemUtils.getOS());
            request.setOs_ver(Build.VERSION.RELEASE);
            request.setMaddr1(SystemUtils.getMacAddress(getApplicationContext()));
            request.setMaddr2(SystemUtils.getIpAddress(getApplicationContext()));
            request.setMobile_key(SystemUtils.getAndroidID(getApplicationContext()));
            request.setSerial(Build.SERIAL);
            request.setMthd(SystemUtils.getMthd());
            request.setAuto(Tags.LOGIN_TYPE.LOGIN_USER);
            request.setUserAgent(SystemUtils.getDeviceInfo(getApplicationContext()));

            startLoading();
            AuthRequests.getInstance().requestlogin(request, new OnResultListener<LoginResult>() {
                @Override
                public void onSuccess(Request request, LoginResult result) {
                    stopLoading();
                    checkUser(result);
                }

                @Override
                public void onFail(Request request, Exception exception) {
                    stopLoading();
                    BraveUtils.showRequestOnFailToast(LoginActivity.this, exception);

                    if(exception != null && exception.getMessage() != null) {
                        Answers.getInstance().logCustom(new CustomEvent(AnalysisTags.REQUEST)
                                .putCustomAttribute(AnalysisTags.ERROR, AnalysisTags.LOGIN + ":: " + exception.getMessage()));
                    }
                }
            });
        }
    }

    private boolean validateRequest(){
        if(TextUtils.isEmpty(editID.getText()) || TextUtils.isEmpty(editPW.getText())) {
            BraveUtils.showToast(this, getString(R.string.toast_login_check));
            return false;
        }
        return true;
    }

    private void checkUser(LoginResult result){
        if(result == null){
            return;
        }
        LoginEvent event = new LoginEvent().putMethod("login_user");
        String userId = result.getUserId();
        if(userId != null){
            event.putCustomAttribute(AnalysisTags.VALUE, userId);
        }

        String state = result.getRstState();
        if(state != null){
            if(ErrorType.LOGIN_USER_STATE.S2.equals(state)){
                boolean isNewId = false;
                String id = PropertyManager.getInstance().getUserID();
                if(!id.equals("") && !id.equals(result.getUserId())){
                    isNewId = true;
                }
                PropertyManager.getInstance().setUserKey(result.getUserKey());
                PropertyManager.getInstance().setUserID(result.getUserId());
                PropertyManager.getInstance().setUserPW(editPW.getText().toString().trim());
                PropertyManager.getInstance().setUserName(result.getUserName());
                goMainActivity(isNewId);

                event.putSuccess(true);
            }else if(ErrorType.LOGIN_USER_STATE.ERROR_ID.equals(state)
                    || ErrorType.LOGIN_USER_STATE.ERROR_PW.equals(state)){
                BraveUtils.showToast(this, getString(R.string.toast_login_error_ei_ep));

                event.putCustomAttribute(AnalysisTags.ERROR, "id_or_pw_error");
                event.putSuccess(false);
            }else if(ErrorType.LOGIN_USER_STATE.ERROR_MOBILE.equals(state)){
                if(ModooGong.hasMypage){
                    BraveUtils.showToast(this, getString(R.string.toast_login_error_im_mypage));
                }else{
                    BraveUtils.showToast(this, getString(R.string.toast_login_error_im));
                }

                event.putCustomAttribute(AnalysisTags.ERROR, "mobile_error");
                event.putSuccess(false);
            }else{
                BraveUtils.showToast(this, getString(R.string.toast_login_error_default));

                event.putCustomAttribute(AnalysisTags.ERROR, "default_error");
                event.putSuccess(false);
            }
        }else{
            BraveUtils.showToast(this, getString(R.string.toast_login_error));

            event.putSuccess(false);
        }
        Answers.getInstance().logLogin(event);
    }

    private void goMainActivity(boolean isNewId) {
        if(isSplash){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra(Tags.TAG_NEW_ID, isNewId);
            startActivity(intent);
            //BraveUtils.goMain(LoginActivity.this);
        }else{
            Intent intent = new Intent();
            intent.putExtra(Tags.TAG_NEW_ID, isNewId);
            setResult(RESULT_OK, intent);
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        SystemUtils.hideSoftKeyboard(LoginActivity.this);

        if(isSplash){
            BraveUtils.goMain(LoginActivity.this);
            finish();
        }else{
            setResult(RESULT_CANCELED);
            super.onBackPressed();
        }
    }
}
