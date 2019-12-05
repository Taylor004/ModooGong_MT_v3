package kr.co.bravecompany.api.android.stdapp.api.requests;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kr.co.bravecompany.api.android.stdapp.R;
import kr.co.bravecompany.api.android.stdapp.api.NetworkManager;
import kr.co.bravecompany.api.android.stdapp.api.OnResultListener;
import kr.co.bravecompany.api.android.stdapp.api.config.APIConfig;
import kr.co.bravecompany.api.android.stdapp.api.data.Packet;
import kr.co.bravecompany.api.android.stdapp.api.data.ShopPacket;
import kr.co.bravecompany.api.android.stdapp.api.data.StatisPacket.*;
import kr.co.bravecompany.api.android.stdapp.manager.APIPropertyManager;
import okhttp3.MultipartBody;
import okhttp3.Request;

public class ShopRequests extends NetworkManager {

    private static ShopRequests instance;

    public static ShopRequests getInstance() {
        if (mContext == null) {
            throw new IllegalStateException("Call `NetworkManager.init(Context..)` before calling this method.");
        }
        if (instance == null) {
            instance = new ShopRequests();
        }
        return instance;
    }

    public class CountRequest
    {
        public String userKey;
        public List<String> type;
    }

    /*
    public Request requestCouponInfo(OnResultListener<ShopPacket.MyCountInfo> listener)
    {
        CountRequest req = new CountRequest();
        req.userKey =  APIPropertyManager.getInstance(mContext).getUserKey();
        req.type = new ArrayList<>();
        req.type.add("Cpn");

        String url = api.getMyCountInfoUrl(mContext);
        return request(url, req, ShopPacket.MyCountInfo.class, listener);
    }


    public Request requestNotificationInfo(OnResultListener<ShopPacket.MyCountInfo> listener)
    {
        CountRequest req = new CountRequest();
        req.userKey =  APIPropertyManager.getInstance(mContext).getUserKey();
        req.type = new ArrayList<>();
        req.type.add("Push");

        String url = api.getMyCountInfoUrl(mContext);
        return request(url, req, ShopPacket.MyCountInfo.class, listener);
    }


    public Request requestCartInfo(OnResultListener<ShopPacket.MyCountInfo> listener)
    {
        CountRequest req = new CountRequest();
        req.userKey =  APIPropertyManager.getInstance(mContext).getUserKey();
        req.type = new ArrayList<>();
        req.type.add("Bskt");

        String url = api.getMyCountInfoUrl(mContext);
        return request(url, req, ShopPacket.MyCountInfo.class, listener);
    }
    */

    public Request requestCountInfo(String type, OnResultListener<ShopPacket.MyCountInfo> listener)
    {
        CountRequest req = new CountRequest();
        req.userKey =  APIPropertyManager.getInstance(mContext).getUserKey();
        req.type = new ArrayList<>();
        req.type.add(type);

        String url = api.getMyCountInfoUrl(mContext);
        return request(url, req, ShopPacket.MyCountInfo.class, listener);
    }

}