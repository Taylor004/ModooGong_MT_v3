package kr.co.bravecompany.modoogong.android.stdapp.db;

import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import kr.co.bravecompany.api.android.stdapp.api.OnResultListener;
import kr.co.bravecompany.api.android.stdapp.api.data.Packet;
import kr.co.bravecompany.api.android.stdapp.api.data.ShopPacket;
import kr.co.bravecompany.api.android.stdapp.api.requests.ShopRequests;
import kr.co.bravecompany.api.android.stdapp.api.requests.SubscribeLectureRequests;
import kr.co.bravecompany.modoogong.android.stdapp.activity.LectureSubscribeActivity;
import kr.co.bravecompany.modoogong.android.stdapp.utils.BraveUtils;
import okhttp3.Request;

public class ShopDataManager extends BaseDataManager {

    final String Tag = "StatisDataManager";

    private static ShopDataManager instance;

    public static ShopDataManager getInstance() {

        if (instance == null)
            instance = new ShopDataManager();
        return instance;
    }

    public void GetCouponInfo(final OnDataUpdatedListener<ShopPacket.MyCountInfo> listener)
    {
//        ShopPacket.MyCouponInfo info = new ShopPacket.MyCouponInfo();
//        info.count = 100;
//        listener.onDataUpdated(info);

        ShopRequests.getInstance().requestCountInfo("Cpn", new OnResultListener<ShopPacket.MyCountInfo>() {
            @Override
            public void onSuccess(Request request, ShopPacket.MyCountInfo result) {
                if(IsResultOk(result.resultCode)) {
                    listener.onDataUpdated(result);
                }
            }

            @Override
            public void onFail(Request request, Exception exception) {

            }
        });

    }

    public void GetNotificationInfo(final OnDataUpdatedListener<ShopPacket.MyCountInfo> listener)
    {
//        ShopPacket.NotificationInfo info = new ShopPacket.NotificationInfo();
//        info.count = 5;
//        listener.onDataUpdated(info);

        ShopRequests.getInstance().requestCountInfo("Push", new OnResultListener<ShopPacket.MyCountInfo>() {
            @Override
            public void onSuccess(Request request, ShopPacket.MyCountInfo result) {
                if(IsResultOk(result.resultCode)) {
                    listener.onDataUpdated(result);
                }
            }

            @Override
            public void onFail(Request request, Exception exception) {

            }
        });

    }

    public void GetMyCartInfo(final OnDataUpdatedListener<ShopPacket.MyCountInfo> listener)
    {
//        ShopPacket.MyCartInfo info = new ShopPacket.MyCartInfo();
//        info.count = 5;
//        listener.onDataUpdated(info);

        ShopRequests.getInstance().requestCountInfo("Bskt", new OnResultListener<ShopPacket.MyCountInfo>() {
            @Override
            public void onSuccess(Request request, ShopPacket.MyCountInfo result) {
                if(IsResultOk(result.resultCode)) {
                    listener.onDataUpdated(result);
                }
            }

            @Override
            public void onFail(Request request, Exception exception) {

            }
        });
    }


    public void GetMyPassList(final OnDataUpdatedListener<List<Packet.PassData>> listener)
    {

        SubscribeLectureRequests.getInstance().requestGetPassList(new OnResultListener<Packet.ResGetPassList>() {
            @Override
            public void onSuccess(Request request, Packet.ResGetPassList result)
            {
                if(IsResultOk(result.resultCode))
                {
                    listener.onDataUpdated(result.passList);
                }
            }

            @Override
            public void onFail(Request request, Exception exception) {

            }
        });
    }

    public void DoTest()
    {
        GetMyCartInfo(new OnDataUpdatedListener<ShopPacket.MyCountInfo>() {
            @Override
            public void onDataUpdated(ShopPacket.MyCountInfo data) {
                if(data !=null)
                {
                    Log.d("PacketTest", String.format("GetMyCartInfo %d", data.getCount()));
                }
            }
        });

        GetNotificationInfo(new OnDataUpdatedListener<ShopPacket.MyCountInfo>() {
            @Override
            public void onDataUpdated(ShopPacket.MyCountInfo data) {
                if(data !=null)
                {
                    Log.d("PacketTest", String.format("GetNotificationInfo %d", data.getCount()));
                }
            }
        });


        GetCouponInfo(new OnDataUpdatedListener<ShopPacket.MyCountInfo>() {
            @Override
            public void onDataUpdated(ShopPacket.MyCountInfo data) {
                if(data !=null)
                {
                    Log.d("PacketTest", String.format("GetCouponInfo %d", data.getCount()));
                }
            }
        });
    }

}
