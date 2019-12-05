package kr.co.bravecompany.api.android.stdapp.api.requests;

import android.content.Context;
import android.util.Log;

import org.junit.Test;

import kr.co.bravecompany.api.android.stdapp.api.NetworkManager;
import kr.co.bravecompany.api.android.stdapp.api.OnResultListener;
import kr.co.bravecompany.api.android.stdapp.api.data.StatisPacket;
import okhttp3.Request;

import static org.junit.Assert.*;

public class StatisRequestsTest {

    @Test
    public void getInstance() {
    }
//
//    @Test
//    public void requestExamInfo() {
//       // Context appContext = InstrumentationRegistry.getTargetContext();
//        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
//
//        NetworkManager.init(appContext, true, "");
//
//        StatisRequests.getInstance().requestExamInfo(new OnResultListener<StatisPacket.ExamInfoResult>() {
//            @Override
//            public void onSuccess(Request request, StatisPacket.ExamInfoResult result) {
//                Log.d("Test", String.format("Exam List:%d",result.examInfos.size()));
//            }
//
//            @Override
//            public void onFail(Request request, Exception exception) {
//
//            }
//        });
//    }

    @Test
    public void requestOnAirInfo() {
    }

    @Test
    public void requestTodayRank() {
    }

    @Test
    public void requestDailyTime() {
    }

    @Test
    public void requestDailyTimeHistory() {
    }

    @Test
    public void requestWeeklyTime() {
    }

    @Test
    public void requestHistoricalSubjectTime() {
    }

    @Test
    public void requestTotalSubjectView() {
    }
}