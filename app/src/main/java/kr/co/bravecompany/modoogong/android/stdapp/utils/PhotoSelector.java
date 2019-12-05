package kr.co.bravecompany.modoogong.android.stdapp.utils;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.core.content.FileProvider;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.gun0912.tedpermission.PermissionListener;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.modoogong.android.stdapp.config.AnalysisTags;
import kr.co.bravecompany.modoogong.android.stdapp.config.LocalAddress;
import kr.co.bravecompany.modoogong.android.stdapp.config.RequestCode;

import static android.app.Activity.RESULT_OK;

/*
// Crop Sample
https://stackoverflow.com/questions/25490928/androidselect-image-from-gallery-then-crop-that-and-show-in-an-imageview
*/

public class PhotoSelector {

    public interface OnPhotoSelectedListener
    {
        void onPhotoSelected(String path, Bitmap selectedBitmap);
    }



    protected Context mContext;
    protected Activity mActivity;
    protected OnPhotoSelectedListener mPhotoSelectedListener;

    protected File mCameraPhotoFile;
    protected Uri mCameraUri = null;
    protected String mPhotoFilePath;

    public void requestPhoto(Activity activity, OnPhotoSelectedListener onPhotoSelectedListener )
    {
        mContext = activity.getApplicationContext();
        mActivity = activity;
        mPhotoSelectedListener = onPhotoSelectedListener;

        BraveUtils.checkPermission(activity, mStoragePermissionListener,
                activity.getString(R.string.check_permission_external_storage),
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    private PermissionListener mStoragePermissionListener = new PermissionListener()
    {
        @Override
        public void onPermissionGranted() {
            if(BraveUtils.checkExternalStorageMounted(mActivity)){
                goGetImage();
            }
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            //do nothing
        }
    };


    private void goGetImage(){
        List<String> items = Arrays.asList(mActivity.getResources().getStringArray(R.array.qa_image_type));
        BraveUtils.showListAlertDialog(mActivity, items, mSelectImageTypeDialogListner);
    }

    private MaterialDialog.ListCallback mSelectImageTypeDialogListner = new MaterialDialog.ListCallback() {

        @Override
        public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
            if (which == 0) {
                if(BraveUtils.checkCamera(mActivity)) {
                    getImageFromCamera();
                }
            } else {
                getImageFromGallery();
            }
        }
    };


    public boolean onActivityResult(int requestCode,  int resultCode, Intent data){

        switch (requestCode) {
            case RequestCode.REQUEST_RC_GALLERY: {

                if(resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    String[] projection = {MediaStore.Images.Media.DATA};
                    Cursor c = mContext.getContentResolver().query(uri,projection,null,null,null);

                    if(c == null)// 다른 어플리케이션에서 접근 하였을 경우, [네이버 클라우드에서 이미지 불러올 경우] [2019.10.20 테일러]
                    {
                        String path = uri.getPath();
                        mPhotoFilePath = path;

                        path = BraveUtils.makeImageSampleForAvatar(mContext,path);
                        mPhotoSelectedListener.onPhotoSelected(path,null);
                    }
                    else if (c.moveToNext()) {
                        String path = c.getString(c.getColumnIndex(MediaStore.Images.Media.DATA));

                        // Uri uri = makeFilePathToUri(path);
                        mPhotoFilePath = path;

                        //if (!performCrop(uri)) {
                            path = BraveUtils.makeImageSampleForAvatar(mContext, path);
                            mPhotoSelectedListener.onPhotoSelected(path, null);
                        //}
                    }
                }
            }
            return true;
            case RequestCode.REQUEST_RC_CAMERA: {
                if(resultCode == RESULT_OK) {

                    //https://black-jin0427.tistory.com/120
                    if (mCameraPhotoFile != null) {
                        String path = mCameraPhotoFile.getAbsolutePath();

                        path = BraveUtils.makeImageSampleForAvatar(mContext, path);
                        addPicToGallery(path);
                        //MediaScanner.scanMedia(mContext, path);

                        Uri uri = makeFilePathToUri(path);

                        // Uri uri = Uri.parse(path);

                        //Test! exposed beyond app  에러로 크래쉬 발생!
                        //File file=new File(path);
                        //uri = Uri.fromFile(file);
                        //

                        //if (path != null) {
                        mPhotoFilePath = path;

                       // if (!performCrop(mCameraUri)) {
                           // MediaScanner.scanMedia(mContext, path);
                            //path = BraveUtils.makeImageSample(mContext, path);
                            mPhotoSelectedListener.onPhotoSelected(path, null);
                       // }

                    }
                }
            }
            return true;
            case RequestCode.REQUEST_RC_CROP:
            {
                if(resultCode == RESULT_OK) {

                    Bundle extras = data.getExtras();
                    Bitmap selectedBitmap = extras.getParcelable("data");

                    if (selectedBitmap != null) {
                        String path = BraveUtils.makeImageSample(mContext, selectedBitmap);
                        mPhotoSelectedListener.onPhotoSelected(path, selectedBitmap);
                    }
                }
                else
                {
                    mPhotoSelectedListener.onPhotoSelected(mPhotoFilePath, null);
                }
            }
            return true;
        }

        return false;
    }


    //출처: https://kyome.tistory.com/9 [KYOME ]
    private void getImageFromCamera() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String child = LocalAddress.NAME + "_" + System.currentTimeMillis() + ".jpg";

        mCameraPhotoFile = new File(BraveUtils.getDCIMFilePath(LocalAddress.FOLDER_CAMERA, child));

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N)
        {
            String provider = mContext.getPackageName() + ".fileprovider";
            mCameraUri = FileProvider.getUriForFile(mContext, provider, mCameraPhotoFile);
        }
        else
        {
            mCameraUri = Uri.fromFile(mCameraPhotoFile);
        }

        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mCameraUri);

        mActivity.startActivityForResult(intent, RequestCode.REQUEST_RC_CAMERA);

        Answers.getInstance().logCustom(new CustomEvent(AnalysisTags.DOQA)
                .putCustomAttribute(AnalysisTags.ACTION, "go_image_from_camera"));
    }

    Uri makeFilePathToUri(String filepath)
    {
        File file=new File(filepath);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            String provider = mContext.getPackageName() + ".fileprovider";
            return FileProvider.getUriForFile(mContext, provider, file);
        } else {
            return Uri.fromFile(mCameraPhotoFile);
        }
    }

    void addPicToGallery(String imagePath)
    {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(imagePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        mActivity.sendBroadcast(mediaScanIntent);
    }

    private void getImageFromGallery()
    {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        mActivity.startActivityForResult(intent, RequestCode.REQUEST_RC_GALLERY);

        Answers.getInstance().logCustom(new CustomEvent(AnalysisTags.DOQA)
                .putCustomAttribute(AnalysisTags.ACTION, "go_image_from_gallery"));
    }

    //참고 사이트
    // https://g-y-e-o-m.tistory.com/48

    private boolean performCrop(Uri contentUri) {
        try {
            //Start Crop Activity

            Intent cropIntent = new Intent("com.android.camera.action.CROP");

            // indicate image type and Uri
           // File f = new File(picUri);
           // Uri contentUri = Uri.fromFile(f);

            cropIntent.setDataAndType(contentUri, "image/*");
            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 512);
            cropIntent.putExtra("outputY", 512);

            // retrieve data on return
            cropIntent.putExtra("return-data", true);

            cropIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            cropIntent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            //Lee
            /*
            cropIntent.putExtra("output", contentUri);

            List<ResolveInfo> list = mActivity.getPackageManager().queryIntentActivities(cropIntent, 0);
            mActivity.grantUriPermission(list.get(0).activityInfo.packageName, contentUri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            Intent i = new Intent(cropIntent);
            ResolveInfo res = list.get(0);
            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            i.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            mActivity.grantUriPermission(res.activityInfo.packageName, contentUri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            */

            // start the activity - we handle returning in onActivityResult
            mActivity.startActivityForResult(cropIntent, RequestCode.REQUEST_RC_CROP);

            return true;
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            // display an error message
            String errorMessage = "your device doesn't support the crop action!";
            BraveUtils.showToast(mActivity, errorMessage);

            return false;
        }
    }

}
