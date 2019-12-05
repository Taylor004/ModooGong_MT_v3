package kr.co.bravecompany.modoogong.android.stdapp.application;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import kr.co.bravecompany.api.android.stdapp.api.OnResultListener;
import kr.co.bravecompany.api.android.stdapp.api.data.Packet;
import kr.co.bravecompany.api.android.stdapp.api.requests.ProfileRequests;
import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.modoogong.android.stdapp.activity.SplashActivity;
import kr.co.bravecompany.modoogong.android.stdapp.manager.PropertyManager;
import kr.co.bravecompany.modoogong.android.stdapp.utils.BraveUtils;
import kr.co.bravecompany.modoogong.android.stdapp.utils.SystemUtils;
import okhttp3.Request;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public static class PushActionData
    {
        public String title;
        public String body;
        public String action;
        public String param;
    }

    public final static String NOTIFICATION_CHANNEL_ID = "ModooStudyAppChannel";
    public final static String Tag = "Push";
    public final static String ACTION_NAME_STORE = "store";
    public final static String ACTION_NAME_OPEN = "open";

    public final static long VibrationPattern[] = {0, 1000, 500, 1000};

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


        Map<String, String> data = remoteMessage.getData();

        String body ="Not Set";
        String title = null;
        PendingIntent pendingIntent=null;
        boolean useWhiteIcon = false;

        if(remoteMessage.getNotification()!=null)
        {
            body = remoteMessage.getNotification().getBody();
            title = remoteMessage.getNotification().getTitle();

            Log.e(Tag, String.format("message title:%s, body:%s",title, body));

            pendingIntent = createStartActivityIntent(title, body , "", "");
        }

        if(data !=null && data.size() > 0 )
        {
            JSONObject object = new JSONObject(data);

            Log.e(Tag, String.format("message data:%s", object.toString()));

            body = data.get("body");
            title = data.get("title");
            String action = data.get("action");
            String param = data.get("param");

            if(ACTION_NAME_OPEN.equals(action))
                pendingIntent = createOpenIntent(param);
            else
                pendingIntent = createStartActivityIntent(title, body , action, param);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);

        notificationBuilder.setAutoCancel(true)
                //.setSmallIcon(R.drawable.ic_remove_circle_white_24dp)
                //.setColor(ContextCompat.getColor(this, R.color.color_black))
                .setContentText(body)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true);

        if(title !=null && title.length() > 0)
            notificationBuilder.setContentTitle(title);

        if(pendingIntent !=null)
            notificationBuilder.setContentIntent(pendingIntent);

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) //롤리팝 버전 보다 낮다면
        {
            useWhiteIcon = false;

            if(!useWhiteIcon)
            {
                notificationBuilder.setSmallIcon(R.drawable.icon_noti_white2); // 노티 아이콘만 셋팅.

            }
        }

        else //롤리팝 버전 이상 이라면
            {

                useWhiteIcon = true;

                if(useWhiteIcon)
                {
                    notificationBuilder.setSmallIcon(R.drawable.icon_noti_white2); // 노티 아이콘(흰색) 셋팅.
                    notificationBuilder.setColor(ContextCompat.getColor(this, R.color.black)); // 노티 이미지 및 타이틀 색상.

                }
            }

        createNotificationManager().notify(1000, notificationBuilder.build());
    }


    PendingIntent createStartActivityIntent(String title, String body, String action, String param)
    {
        Intent intent;
        intent = new Intent(MyFirebaseMessagingService.this, SplashActivity.class);
        intent.putExtra("action", action);
        intent.putExtra("param", param);
        intent.putExtra("body", body);
        intent.putExtra("title", title);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        return pendingIntent;
    }

    PendingIntent createOpenIntent( String param)
    {
        Intent intent;
        intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(param));

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        return pendingIntent;
    }

    NotificationManager createNotificationManager()
    {

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                    "ModooStudyApp Notifications",
                    NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.setDescription("");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(VibrationPattern);
            notificationChannel.enableVibration(true);

            mNotificationManager.createNotificationChannel(notificationChannel);
        }

        // to display notification in DND Mode
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = mNotificationManager.getNotificationChannel(NOTIFICATION_CHANNEL_ID);
            channel.canBypassDnd();
        }

        return mNotificationManager;
    }

    @Override
    public void onNewToken(String token)
    {
        Log.e(Tag, "onNewToken:"+ token);
        //sendRegistrationToServer(token);
    }


    public static void initPushSystem(final Context applicationContext)
    {
        boolean isPush = PropertyManager.getInstance().isPush();

        if(isPush)
            registerPushTokenToServer(applicationContext);
        else
            unregisterPushToken();
    }

    static void unregisterPushToken()
    {
        FirebaseMessaging.getInstance().setAutoInitEnabled(false);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    FirebaseInstanceId.getInstance().deleteInstanceId();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    static void registerPushTokenToServer(final Context applicationContext)
    {
        if(BraveUtils.checkUserInfo()) {

            FirebaseMessaging.getInstance().setAutoInitEnabled(true);

            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {

                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            if (!task.isSuccessful()) {
                                Log.w(Tag, "getInstanceId failed", task.getException());
                                return;
                            }

                            // Get new Instance ID token
                            final String token = task.getResult().getToken();
                            final String mobileKey = SystemUtils.getAndroidID(applicationContext);


                            ProfileRequests.getInstance().requestSetPushKey(token, mobileKey, new OnResultListener<Packet.ResponseResult>() {
                                @Override
                                public void onSuccess(Request request, Packet.ResponseResult result) {
                                    Log.d(Tag, "push key store completed:"+token);
                                }

                                @Override
                                public void onFail(Request request, Exception exception) {
                                    Log.d(Tag, "push key store failed:" + exception.getMessage());
                                }
                            });

                        }
                    });
        }
    }

    private static PushActionData msPushActionData =null;

    public static void InitPushStartData(Intent startIntent)
    {
        msPushActionData =null;

        Bundle bundle= startIntent.getExtras();
        if(bundle !=null)
        {
            String action = bundle.getString("action");
            String param = bundle.getString("param");
            String notiMsg = bundle.getString("body");
            String title = bundle.getString("title");

            msPushActionData = new PushActionData();
            msPushActionData.action = action;
            msPushActionData.param = param;
            msPushActionData.body = notiMsg;
            msPushActionData.title = title;

            Log.d(Tag, String.format("handle app intent: action:%s param:%s title:%s body:%s", action, param, title, notiMsg ));
        }
    }

    public static PushActionData PopPushStartData()
    {
        PushActionData pop = msPushActionData;
        msPushActionData =null;

        return pop;
    }

}
