package kr.co.bravecompany.modoogong.android.stdapp.utils;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;

import java.io.File;

/**
 * Created by BraveCompany on 2016. 11. 15..
 */

public class MediaScanner implements MediaScannerConnection.MediaScannerConnectionClient{

    private MediaScannerConnection mMs;
    private File mFile;

    public static MediaScanner scanMedia(Context context, String path){
        if(path == null){
            return null;
        }
        return new MediaScanner(context, new File(path));
    }

    private MediaScanner(Context context, File file) {
        this.mFile = file;
        mMs = new MediaScannerConnection(context, this);
        mMs.connect();
    }

    @Override
    public void onMediaScannerConnected() {
        mMs.scanFile(mFile.getAbsolutePath(), null);
    }

    @Override
    public void onScanCompleted(String path, Uri uri) {
        mMs.disconnect();
    }
}
