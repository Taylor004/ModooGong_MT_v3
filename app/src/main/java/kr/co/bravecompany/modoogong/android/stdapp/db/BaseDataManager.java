package kr.co.bravecompany.modoogong.android.stdapp.db;

import android.util.Log;

public class BaseDataManager {

    public interface OnDataUpdatedListener<T>
    {
        void onDataUpdated(T data);
    }


    protected boolean IsResultOk(int resultCode)
    {
        if(resultCode < 0)
        {
            Log.d("DataManager", "ResultCode:"+resultCode);
            return false;
        }

        return true;
    }

}
