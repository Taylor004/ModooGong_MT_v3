package kr.co.bravecompany.modoogong.android.stdapp.utils;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.kollus.sdk.media.util.Utils;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.List;

import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.modoogong.android.stdapp.activity.DownloadActivity;
import kr.co.bravecompany.modoogong.android.stdapp.activity.LoginActivity;
import kr.co.bravecompany.modoogong.android.stdapp.activity.MainActivity;
import kr.co.bravecompany.modoogong.android.stdapp.activity.PhotoViewActivity;
import kr.co.bravecompany.modoogong.android.stdapp.activity.PlayActivity;
import kr.co.bravecompany.api.android.stdapp.api.data.LectureItemVO;
import kr.co.bravecompany.modoogong.android.stdapp.activity.WebViewActivity;
import kr.co.bravecompany.modoogong.android.stdapp.application.ModooGong;
import kr.co.bravecompany.modoogong.android.stdapp.config.LocalAddress;
import kr.co.bravecompany.modoogong.android.stdapp.config.RequestCode;
import kr.co.bravecompany.modoogong.android.stdapp.config.Tags;
import kr.co.bravecompany.modoogong.android.stdapp.manager.PropertyManager;
import kr.co.bravecompany.player.android.stdapp.utils.PlayerUtils;

/**
 * Created by BraveCompany on 2016. 10. 12..
 */

public class BraveUtils {

    // =============================================================================
    // Convert
    // =============================================================================

    /**
     * Convert dp to px
     *
     * @param context context
     * @param px      pixel
     * @return dp
     */
    public static int convertPxToDp(Context context, float px) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return (int) dp;
    }

    /**
     * Make a vo to json string
     *
     * @param json  json string
     * @param clazz convert class
     * @return vo object
     */
    public static Object toJsonString(String json, Class clazz) {
        Gson gson = new Gson();
        TypeAdapter adapter = gson.getAdapter(clazz);
        try {
            return adapter.fromJson(json);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Make a vo to json object
     *
     * @param object json object
     * @param clazz  convert class
     * @return vo object
     */
    public static Object toJsonObject(Object object, Class clazz) {
        Gson gson = new Gson();
        String json = gson.toJson(object);
        JsonParser parser = new JsonParser();
        JsonObject jsonObject = parser.parse(json).getAsJsonObject();
        return gson.fromJson(jsonObject, clazz);
    }

    /**
     * Make to json
     *
     * @param obj vo object
     * @return Json String
     */
    public static String toJson(Object obj) {
        return new Gson().toJson(obj);
    }

    // =============================================================================
    // Image
    // =============================================================================

    public static void setImage(ImageView view, int res){
        if(res == -1){
            Glide.with(view.getContext()).load(Uri.EMPTY).into(view);
        }else{
            Glide.with(view.getContext()).load(res)
                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).into(view);
        }
    }

    public static void setImage(ImageView view, int res, int width, int height){
        if(res == -1){
            Glide.with(view.getContext()).load(Uri.EMPTY).into(view);
        }else{
            Glide.with(view.getContext()).load(res).override(width, height).into(view);
        }
    }

    public static void setImage(ImageView view, String path){
        if(path == null){
            Glide.with(view.getContext()).load(Uri.EMPTY).into(view);
        }else{
            if(path.length() == 0){
                Glide.with(view.getContext()).load(Uri.EMPTY).into(view);
            }else{
                Glide.with(view.getContext()).load(path).placeholder(R.color.colorPrimaryDark)
                        .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).thumbnail(0.1f).into(view);
            }
        }
    }

    public static void setImage(ImageView view, String path, int width, int height){
        if(path == null){
            Glide.with(view.getContext()).load(Uri.EMPTY).override(width, height).into(view);
        }else{
            if(path.length() == 0){
                Glide.with(view.getContext()).load(Uri.EMPTY).override(width, height).into(view);
            }else{
                Glide.with(view.getContext()).load(path).placeholder(R.color.colorPrimaryDark)
                        .override(width, height).thumbnail(0.1f).into(view);
            }
        }
    }

    public static void setImage(ImageView view, Bitmap bitmap){
        if(bitmap == null){
            Glide.with(view.getContext()).load(Uri.EMPTY).into(view);
        }else{
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            if(byteArray == null){
                Glide.with(view.getContext()).load(Uri.EMPTY).into(view);
            }else {
                Glide.with(view.getContext()).load(byteArray).placeholder(R.color.colorPrimaryDark)
                        .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).thumbnail(0.1f).into(view);
            }
        }
    }

    public static void setImage(ImageView view, Bitmap bitmap, int width, int height){
        if(bitmap == null){
            Glide.with(view.getContext()).load(Uri.EMPTY).into(view);
        }else{
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            if(byteArray == null){
                Glide.with(view.getContext()).load(Uri.EMPTY).override(width, height).into(view);
            }else{
                Glide.with(view.getContext()).load(byteArray).placeholder(R.color.colorPrimaryDark)
                        .override(width, height).thumbnail(0.1f).into(view);
            }
        }
    }

    public static String makeImageSample(Context context, String path){

        try {
            //Samsung Galaxy S4 장비 기준
            int IMAGE_MAX_SIZE = 1920;

            BitmapFactory.Options bfo = new BitmapFactory.Options();
            bfo.inJustDecodeBounds = true;

            BitmapFactory.decodeFile(path, bfo);

            bfo.inSampleSize = 1;
            if(bfo.outHeight * bfo.outWidth >= IMAGE_MAX_SIZE * IMAGE_MAX_SIZE) {
                bfo.inSampleSize = (int)Math.pow(2, (int)Math.round(Math.log(IMAGE_MAX_SIZE
                        / (double) Math.max(bfo.outHeight, bfo.outWidth)) / Math.log(0.5)));
            }
            bfo.inJustDecodeBounds = false;

            int degree = getExifOrientation(path);
            Bitmap bitmap = getRotatedBitmap(BitmapFactory.decodeFile(path, bfo), degree);

            if(bfo.inSampleSize != 1){
                return saveBitmapToCompressJPEG(context, bitmap);
            }else{
                return path;
            }
        } catch(OutOfMemoryError ex) {
            log.e(Log.getStackTraceString(ex));
        }
        return path;
    }


    public static String makeImageSampleForAvatar(Context context, String path){

        try {
            //Samsung Galaxy S4 장비 기준
            int IMAGE_MAX_SIZE = 512;

            BitmapFactory.Options bfo = new BitmapFactory.Options();
            bfo.inJustDecodeBounds = true;

            BitmapFactory.decodeFile(path, bfo);

            bfo.inSampleSize = 1;
            if(bfo.outHeight * bfo.outWidth >= IMAGE_MAX_SIZE * IMAGE_MAX_SIZE) {
                bfo.inSampleSize = (int)Math.pow(2, (int)Math.round(Math.log(IMAGE_MAX_SIZE
                        / (double) Math.max(bfo.outHeight, bfo.outWidth)) / Math.log(0.5)));
            }
            bfo.inJustDecodeBounds = false;

            int degree = getExifOrientation(path);
            Bitmap bitmap = getRotatedBitmap(BitmapFactory.decodeFile(path, bfo), degree);

            //Bitmap bitmap = BitmapFactory.decodeFile(path, bfo);

            if(bfo.inSampleSize != 1){
                return saveBitmapToCompressJPEG(context, bitmap);
            }else{
                return path;
            }
        } catch(OutOfMemoryError ex) {
            log.e(Log.getStackTraceString(ex));
        }
        return path;
    }

    public static String makeImageSample(Context context, Bitmap bitmap){

        try {
            return saveBitmapToCompressJPEG(context, bitmap);

        } catch(OutOfMemoryError ex) {
            log.e(Log.getStackTraceString(ex));
        }
        return null;
    }

    private static int getExifOrientation(String path) {
        int degree = 0;
        ExifInterface exif = null;

        try {
            exif = new ExifInterface(path);
        }
        catch (IOException e) {
            log.e(log.getStackTraceString(e));
        }

        if (exif != null) {
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
            if (orientation != -1) {
                // We only recognize a subset of orientation tag values.
                switch(orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                }
            }
        }
        return degree;
    }

    private static Bitmap getRotatedBitmap(Bitmap bitmap, int degrees) {
        if ( degrees != 0 && bitmap != null ) {
            Matrix m = new Matrix();
            m.setRotate(degrees, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2 );
            try {
                Bitmap b2 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
                if (bitmap != b2) {
                    bitmap.recycle();
                    bitmap = b2;
                }
            } catch (OutOfMemoryError ex) {
                log.e(Log.getStackTraceString(ex));
            }
        }

        return bitmap;
    }

    private static String saveBitmapToCompressJPEG(Context context, Bitmap bitmap){
        String child = LocalAddress.NAME + "_" + System.currentTimeMillis() + ".jpg";
        String path = getPicturesFilePath(child);
        try{
            FileOutputStream out = new FileOutputStream(path);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, out);
            out.close();

            MediaScanner.scanMedia(context, path);
        }catch(FileNotFoundException exception){
            log.e("FileNotFoundException " + exception.getMessage());
        }catch(IOException exception){
            log.e("IOException " + exception.getMessage());
        }
        return path;
    }

    // =============================================================================
    // File Path
    // =============================================================================

    public static String getDCIMFilePath(String folder, String child){
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();
        path = path + folder;
        File file = new File(path);
        if(!file.exists()){
            file.mkdirs();
        }
        path = path + child;
        log.d("getDCIMFilePath - " + path);
        return path;
    }

    public static String getDCIMFilePath(String child){
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();
        path = path + LocalAddress.FOLDER;
        File file = new File(path);
        if(!file.exists()){
            file.mkdirs();
        }
        path = path + child;
        log.d("getDCIMFilePath - " + path);
        return path;
    }

    public static String getPicturesFilePath(String child){
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
        path = path + LocalAddress.FOLDER;
        File file = new File(path);
        if(!file.exists()){
            file.mkdirs();
        }
        path = path + child;
        log.d("getPicturesFilePath - " + path);
        return path;
    }

    // =============================================================================
    // Alert Dialog
    // =============================================================================

    public static void showAlertDialog(Activity activity, String title, String msg,
                                               String positive, String negative,
                                               MaterialDialog.SingleButtonCallback listenerOk,
                                               MaterialDialog.SingleButtonCallback listenerCancel){
        if(activity.isFinishing()) {
            return;
        }
        new MaterialDialog.Builder(activity)
                .title(title)
                .content(msg)
                .positiveText(positive)
                .onPositive(listenerOk)
                .negativeText(negative)
                .onNegative(listenerCancel)
                .cancelable(false)
                .show();
    }

    public static void showAlertDialogOkCancel(Activity activity, String msg,
                                               MaterialDialog.SingleButtonCallback listenerOk,
                                               MaterialDialog.SingleButtonCallback listenerCancel){
        if(activity.isFinishing()) {
            return;
        }
        new MaterialDialog.Builder(activity)
                .content(msg)
                .positiveText(R.string.common_ok)
                .onPositive(listenerOk)
                .negativeText(R.string.common_cancel)
                .onNegative(listenerCancel)
                .cancelable(false)
                .show();
    }

    public static void showAlertDialogOk(Activity activity, String msg,
                                         MaterialDialog.SingleButtonCallback listenerOk){
        showAlertDialogOk(activity, msg, activity.getString(R.string.common_ok), listenerOk);
    }

    public static void showAlertDialogOk(Activity activity, String msg, String positive,
                                               MaterialDialog.SingleButtonCallback listenerOk){
        if(activity.isFinishing()) {
            return;
        }
        new MaterialDialog.Builder(activity)
                .content(msg)
                .positiveText(positive)
                .onPositive(listenerOk)
                .cancelable(false)
                .show();
    }

    public static void showSelectAlertDialog(Activity activity, List<String> items, int selectedIndex,
                                             MaterialDialog.ListCallbackSingleChoice listener){
        if(activity.isFinishing()) {
            return;
        }
        if(items != null && items.size() != 0){
            new MaterialDialog.Builder(activity)
                    .items(items)
                    .itemsCallbackSingleChoice(selectedIndex, listener)
                    .show();
        }else {
            BraveUtils.showToast(activity, activity.getString(R.string.toast_common_dialog_no_list));
        }
    }

    public static void showListAlertDialog(Activity activity, List<String> items,
                                           MaterialDialog.ListCallback listener){
        if(activity.isFinishing()) {
            return;
        }
        if(items != null && items.size() != 0){
            new MaterialDialog.Builder(activity)
                    .items(items)
                    .itemsCallback(listener)
                    .show();
        }else {
            BraveUtils.showToast(activity, activity.getString(R.string.toast_common_dialog_no_list));
        }
    }

    public static void showInputAlertDialog(Activity activity, int inputType, String hint, String prefill,
                                           MaterialDialog.InputCallback listener){
        if(activity.isFinishing()) {
            return;
        }
        new MaterialDialog.Builder(activity)
                .inputType(inputType)
                .input(hint, prefill, listener)
                .show();
    }

    // =============================================================================
    // Format
    // =============================================================================

    public static String formatTime(int milliseconds){
        milliseconds = milliseconds / 1000;
        int secondPart = milliseconds % 60;
        int minutePart = milliseconds / 60;
        return (minutePart >= 10 ? minutePart : "0" + minutePart) + ":" + (secondPart >= 10 ? secondPart : "0" + secondPart);
    }

    public static boolean checkImageFormat(String path){
        if(path == null){
            return false;
        }
        return (path.endsWith(".jpg")||path.endsWith(".jpeg")||
                path.endsWith(".png")||path.endsWith(".bmp")||path.endsWith(".gif"));
    }

    public static boolean checkAudioFormat(String path){
        if(path == null){
            return false;
        }
        return (path.endsWith(".3gp")||path.endsWith(".mp4")||
                path.endsWith(".m4a")||path.endsWith(".aac")||path.endsWith(".ts")||
                path.endsWith(".flac")||
                path.endsWith(".mp3")||
                path.endsWith(".mid")||path.endsWith(".xmf")||path.endsWith(".mxmf")||
                path.endsWith(".rtttl")||path.endsWith(".rtx")||path.endsWith(".ota")||path.endsWith(".imy")||
                path.endsWith(".ogg")||path.endsWith(".mkv")||path.endsWith(".wav"));
    }

    public static boolean checkURLFormat(String path) {
        try {
            URI uri = new URI(path);
            return uri.getScheme().equals("http") || uri.getScheme().equals("https");
        }
        catch (Exception e) {
            return false;
        }
    }

    public static String formatOrder(Context context, int order, int subOrder){
        String result;
        if(order != 0) {
            String s_order = String.valueOf(order);
            if (subOrder != 0) {
                s_order = s_order + "-" + subOrder;
            }
            result = String.format(context.getString(R.string.study_order), s_order);
        }else{
            result = context.getString(R.string.study_order_0);
        }
        return result;
    }

    public static int getLectureingDays(String endDay){
        if(endDay == null){
            return -1;
        }
        DateTime now = DateTime.now();
        DateTime end = DateTimeFormat.forPattern("yyyy-MM-dd").parseDateTime(endDay).plusDays(1);
        int days = -1;
        long diff = end.getMillis() - now.getMillis();
        if(diff >= 0){
            days = Days.daysBetween(now, end).getDays();
        }
        return days;
        //long days = end.minus(now.getMillis()).getMillis() / (24 * 60 * 60 * 1000);
        //return (int)days;
    }

    public static boolean isNewBoard(String writeDate){
        if(writeDate == null){
            return false;
        }
        DateTime now = DateTime.now();
        DateTime write = DateTimeFormat.forPattern("yy-MM-dd").parseDateTime(writeDate);
        int days = -1;
        long diff = now.getMillis() - write.getMillis();
        if(diff >= 0){
            days = Days.daysBetween(write, now).getDays();
        }
        return (days < 7);
    }

    public static boolean checkStopLecture(String studyState){
        if(studyState == null || studyState.length() == 0){
            return false;
        }
        return studyState.startsWith("5");
    }

    public static boolean checkPrevLecture(String studyState){
        if(studyState == null || studyState.length() == 0){
            return false;
        }
        return studyState.equals("XX");
    }

    public static boolean checkNotOpenLecture(String startDate){
        if(startDate == null || startDate.length() == 0 || startDate.equals("0000-00-00")){
            return false;
        }
        DateTime now = DateTime.now();
        DateTime start = DateTimeFormat.forPattern("yyyy-MM-dd").parseDateTime(startDate);
        long diff = now.getMillis() - start.getMillis();
        return (diff < 0);
    }

    // =============================================================================
    // Start Activity
    // =============================================================================

    public static void goPhotoView(Activity activity, String path){
        Intent intent = new Intent(activity, PhotoViewActivity.class);
        intent.putExtra(Tags.TAG_IMAGE, path);
        activity.startActivity(intent);
    }

    public static void goCall(Activity activity, String uri){
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(uri));
        activity.startActivity(intent);
    }

    public static void goLogin(Activity activity){
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivityForResult(intent, RequestCode.REQUEST_LOGIN);
    }

    public static void goMain(Activity activity){
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
    }

    public static void restartMain(Activity activity){
        Intent intent = new Intent(activity, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
        activity.finish();
    }

    public static void goWeb(Activity activity, String url){
        activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        //Intent intent = new Intent(activity, WebViewActivity.class);
        //intent.putExtra(Tags.TAG_URL, url);
        //activity.startActivity(intent);
    }

    public static void goPlayerWithUrl(Activity activity, String title, String url){
        Intent intent = new Intent(activity, PlayActivity.class);
        intent.putExtra(Tags.TAG_PLAY, title);
        if(url != null) {
            intent.setData(Uri.parse(url));
        }
        activity.startActivityForResult(intent, RequestCode.REQUEST_PLAY);
    }

    public static void goPlayerWithUrl(Fragment fragment, String title, String url){
        Intent intent = new Intent(fragment.getActivity(), PlayActivity.class);
        intent.putExtra(Tags.TAG_PLAY, title);
        if(url != null) {
            intent.setData(Uri.parse(url));
        }

        fragment.startActivityForResult(intent, RequestCode.REQUEST_PLAY);
    }

    public static void goPlayerWithMediaContentKey(Activity activity, String title, String mediaContentKey){
        Intent intent = new Intent(activity, PlayActivity.class);
        intent.putExtra(Tags.TAG_PLAY, title);
        intent.putExtra(Tags.TAG_MEDIA_CONTENT_KEY, mediaContentKey);
        activity.startActivityForResult(intent, RequestCode.REQUEST_PLAY);
    }

    public static void goDownload(Activity activity){
        Intent intent = new Intent(activity, DownloadActivity.class);
        activity.startActivityForResult(intent, RequestCode.REQUEST_DOWN);
    }

    public static void goDownload(Fragment fragment){
        Intent intent = new Intent(fragment.getActivity(), DownloadActivity.class);
        fragment.startActivityForResult(intent, RequestCode.REQUEST_DOWN);
    }

    public static void goWebView(Activity activity, String url, String title)
    {
        Intent intent = new Intent(activity, WebViewActivity.class);
        intent.putExtra(Tags.TAG_URL, url);
        if(title !=null)
            intent.putExtra(Tags.TAGE_TITLE, title);

        activity.startActivity(intent);
    }

    // =============================================================================
    // Views
    // =============================================================================

    public static void setVisibilityTopView(final View view, int visibility){
        if(view.getVisibility() != visibility) {
            Animation visible = AnimationUtils.loadAnimation(view.getContext(), R.anim.slide_in_top);
            Animation gone = AnimationUtils.loadAnimation(view.getContext(), R.anim.slide_out_top);
            if (visibility == View.VISIBLE) {
                view.setAnimation(visible);
            } else {
                view.setAnimation(gone);
            }
            view.setVisibility(visibility);
        }
    }

    public static void setVisibilityBottomView(final View view, int visibility){
        if(view.getVisibility() != visibility) {
            Animation visible = AnimationUtils.loadAnimation(view.getContext(), R.anim.slide_in_bottom);
            Animation gone = AnimationUtils.loadAnimation(view.getContext(), R.anim.slide_out_bottom);
            if (visibility == View.VISIBLE) {
                view.setAnimation(visible);
            } else {
                view.setAnimation(gone);
            }
            view.setVisibility(visibility);
        }
    }

    public static void setVisibilityBottomViewLong(final View view, int visibility){
        if(view.getVisibility() != visibility) {
            Animation visible = AnimationUtils.loadAnimation(view.getContext(), R.anim.slide_in_bottom_long);
            Animation gone = AnimationUtils.loadAnimation(view.getContext(), R.anim.slide_out_bottom_long);
            if (visibility == View.VISIBLE) {
                view.setAnimation(visible);
            } else {
                view.setAnimation(gone);
            }
            view.setVisibility(visibility);
        }
    }

    public static void updateNoticeTypeView(View txtTypeNone, View txtTypeFirst, View txtTypeEvent, int type) {
        switch (type) {
            case Tags.NOTICE_TYPE_CD.NOTICE_NONE:
                txtTypeNone.setVisibility(View.VISIBLE);
                txtTypeFirst.setVisibility(View.GONE);
                txtTypeEvent.setVisibility(View.GONE);
                break;
            case Tags.NOTICE_TYPE_CD.NOTICE_FIRST:
                txtTypeNone.setVisibility(View.GONE);
                txtTypeFirst.setVisibility(View.VISIBLE);
                txtTypeEvent.setVisibility(View.GONE);
                break;
            case Tags.NOTICE_TYPE_CD.NOTICE_EVENT:
                txtTypeNone.setVisibility(View.GONE);
                txtTypeFirst.setVisibility(View.GONE);
                txtTypeEvent.setVisibility(View.VISIBLE);
                break;
        }
    }

    public static boolean isRecyclerScrollable(RecyclerView recyclerView) {
        return recyclerView.computeHorizontalScrollRange() > recyclerView.getWidth() ||
                    recyclerView.computeVerticalScrollRange() > recyclerView.getHeight();
    }

    public static void updateStudyHeaderView(TextView txtTeacherName, TextView txtSaleType,
                                             TextView txtLectureName, TextView txtLectureDetail,
                                             LectureItemVO lectureItem){
        if(lectureItem == null){
            return;
        }

        txtTeacherName.setText(lectureItem.getTeacherName());
        String cateName = lectureItem.getCateName();
        if(!ModooGong.isShowCateName){
            cateName = null;
        }
        if(cateName != null && cateName.length() != 0){
            txtSaleType.setText(cateName);
            txtSaleType.setVisibility(View.VISIBLE);
        }else{
            txtSaleType.setVisibility(View.GONE);
        }
        txtLectureName.setText(BraveUtils.fromHTMLTitle(lectureItem.getLectureName()));
        txtLectureDetail.setText(String.format(txtLectureDetail.getContext().getString(R.string.lecture_item_detail),
                lectureItem.getStudyStartDay(), lectureItem.getStudyEndDay(),
                lectureItem.getLectureingDays(),
                lectureItem.getStudyLessonCount(), lectureItem.getLectureCnt()));
    }

    // =============================================================================
    // Toast
    // =============================================================================

    public static void showToast(Activity activity, String msg){
        Toast toast = Toast.makeText(activity, msg, Toast.LENGTH_SHORT);
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        if( v != null) v.setGravity(Gravity.CENTER);
        toast.show();
    }

    public static void showToast(Context context, String msg){
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        if( v != null) v.setGravity(Gravity.CENTER);
        toast.show();
    }

    public static void showRequestOnFailToast(Activity activity, Exception exception){
        if (exception instanceof JsonSyntaxException) {
            showToast(activity, activity.getString(R.string.toast_common_json_fail));
        } else {
            showToast(activity, activity.getString(R.string.toast_common_network_fail));
        }
    }

    public static void showLoadOnFailToast(Activity activity){
        showToast(activity, activity.getString(R.string.toast_common_data_fail));
    }

    // =============================================================================
    // HTML
    // =============================================================================

    public static String fromHTMLEditContent(String content){
        if(content == null) {
            return null;
        }
        content = Html.fromHtml(content).toString();
        content = content.replaceAll("(</p>)|(</div>)|(</li>)|(<br.*?>)", "\n");
        content = content.replaceAll("\\<.*?\\>", "");
        content = content.replaceAll("&nbsp;", " ");
        content = content.replaceAll("&amp;", "&");
        content = content.replaceAll("&lt;", "<");
        content = content.replaceAll("&gt;", ">");
        return content;
    }

    public static String toHTMLEditContent(String content){
        if(content == null) {
            return null;
        }
        content = content.replaceAll("&", "&amp;");
        content = content.replaceAll("<", "&lt;");
        content = content.replaceAll(">", "&gt;");
        content = content.replaceAll("\n", "<br>");
        content = content.replace("\\", "\\\\");
        content = content.replaceAll("\'", "\\\\'");
        return content;
    }

    public static String toHTMLEditTitle(String content){
        if(content == null) {
            return null;
        }
        content = content.replaceAll("\n", "<br>");
        content = content.replace("\\", "\\\\");
        content = content.replaceAll("\'", "\\\\'");
        return content;
    }

    public static String fromHTMLContent(String content){
        if(content == null) {
            return null;
        }
        content = Html.fromHtml(content).toString();
        content = content.replaceAll("(<font.*?>)|(<(/)font>)", "");
        content = content.replaceAll("background-color:.*?;", "");
        content = content.replaceAll("color:.*?;", "");
        content = content.replaceAll("font-size:.*?;", "");
        content = content.replaceAll("(</p><p.*?>)|(</div><div.*?>)", "<br>");
        content = content.replaceAll("&nbsp;", " ");
        return content;
    }

    public static String fromHTMLTitle(String content){
        if(content == null) {
            return null;
        }
        content = Html.fromHtml(content).toString();
        content = content.replaceAll("(<font.*?>)|(<(/)font>)", "");
        content = content.replaceAll("&#8226;", "•");
        content = content.replaceAll("<br>", " ");
        content = content.replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", "");
        return content;
    }

    public static String fromHTML(String content){
        if(content == null) {
            return null;
        }
        return Html.fromHtml(content).toString();
    }

    public static String toHTML(Spanned content){
        if(content == null) {
            return null;
        }
        return Html.toHtml(content).toString();
    }

    public static String makeHTML(String content, int padding, float lineHeight){
        if(content == null) {
            return null;
        }
        content = Html.fromHtml(content).toString();
        String head = "<head><meta name=\"viewport\" content=\"width=device-width," +
                "initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=0\">" +
                "<style type=\"text/css\">img{max-width: 100%; width:auto; height:auto;} " +
                "body{background-color: transparent; " +
                "line-height: " + String.valueOf(lineHeight) + "; " +
                "margin: 0; padding: " + String.valueOf(padding) + "px;} " +
                "span{line-height: " + String.valueOf(lineHeight) + ";} " +
                "div{line-height: " + String.valueOf(lineHeight) + ";} " +
                "p{line-height: " + String.valueOf(lineHeight) + "; " + "margin: 0 0 0 0;} " +
                "a{text-decoration: none;} " +
                "</style></head>";
        String result = "<html>" + head + "<body>" + content + "</body></html>";
        return result;
    }

    public static String makeHTMLTextGray(Context context, String content){
        if(content == null) {
            return null;
        }
        content = Html.fromHtml(content).toString();
        //content = content.replaceAll("(<font.*?>)|(<(/)font>)", "");
        //content = content.replaceAll("background-color:.*?;", "");
        //content = content.replaceAll("color:.*?;", "");
        //content = content.replaceAll("font-size:.*?;", "");
        return makeHTMLTextGray(context, content, 0, 1.5f);
    }

    public static String makeHTMLTextGray(Context context, String content, int padding, float lineHeight){
        if(content == null) {
            return null;
        }
        int color = context.getResources().getColor(R.color.colorPrimary);
        String linkColor = "#" + Integer.toHexString(color & 0x00FFFFFF);
        int color_gray = context.getResources().getColor(R.color.color_gray);
        String fontColor = "#" + Integer.toHexString(color_gray & 0x00FFFFFF);

        log.d(content);
        String head = "<head><meta name=\"viewport\" content=\"width=device-width," +
                "initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=0\">" +
                "<style type=\"text/css\">img{max-width: 100%; width:auto; height:auto;} " +
                "body{color: " + fontColor + "; background-color: transparent; " +
                "line-height: " + String.valueOf(lineHeight) + "; " +
                "margin: 0; padding: " + String.valueOf(padding) + "px;} " +
                "span{line-height: " + String.valueOf(lineHeight) + ";} " +
                "div{line-height: " + String.valueOf(lineHeight) + ";} " +
                "p{line-height: " + String.valueOf(lineHeight) + "; " + "margin: 0 0 0 0;} " +
                "a{color:" + linkColor + "; text-decoration: none;} " +
                "</style></head>";
        String result = "<html>" + head + "<body>" + content + "</body></html>";
        return result;
    }

    // =============================================================================
    // Kollus Player
    // =============================================================================

    public static String formattingTime(int time) {
        int h = time / 3600;
        int m = (time - h * 3600) / 60;
        int s = time - (h * 3600 + m * 60);
        String strTime;
        if (h == 0) {
            strTime = String.format("%02d:%02d", m, s);
        } else {
            strTime = String.format("%d:%02d:%02d", h, m, s);
        }
        return strTime;
    }

    public static String getTotalMemorySize(Context context) {
        return PlayerUtils.getStringSize(Utils.getTotalMemorySize(Utils.getStoragePath(context)));
    }

    public static String getAvailableMemorySize(Context context) {
        return PlayerUtils.getStringSize(Utils.getAvailableMemorySize(Utils.getStoragePath(context)));
    }

    // =============================================================================
    // Property
    // =============================================================================

    public static boolean checkUserInfo(){
        PropertyManager instance = PropertyManager.getInstance();
        if(instance.getUserKey() != null && !instance.getUserID().equals("") && !instance.getUserPW().equals("")){
            return true;
        }
        return false;
    }

    // =============================================================================
    // Download Attach File
    // =============================================================================

    public static void doAttachDownload(Context context, String url, String fileName){
        if(url == null){
            return;
        }
        if(fileName == null){
            fileName = getFileName(url);
        }
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle(fileName);
        request.setDescription(context.getString(R.string.notice_downloading));
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).mkdirs();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        ((DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE)).enqueue(request);

        BraveUtils.showToast(context, context.getString(R.string.notice_downloading));
    }

    public static String getFileName(String path){
        if(path == null){
            return "";
        }
        int index = path.lastIndexOf("/");
        String fileName = "";
        if(index != -1){
            fileName = path.substring(index + 1);
        }
        return fileName;
    }

    public static String getExtension(String path){
        if(path == null){
            return "";
        }
        int index = path.lastIndexOf(".");
        String fileExtension = "";
        if(index != -1){
            fileExtension = path.substring(index);
        }
        return fileExtension;
    }

    // =============================================================================
    // Check Permission
    // =============================================================================

    public static void checkPermission(Context context, PermissionListener listener, String msg,
                                       String permission){
        String message = String.format(context.getString(R.string.check_permission_denied_msg),
                msg, context.getString(R.string.app_name));
        new TedPermission(context)
                .setPermissionListener(listener)
                .setDeniedMessage(message)
                .setPermissions(permission)
                .check();
    }

    public static void checkPermission(Context context, PermissionListener listener, String msg,
                                       String permission1, String permission2){
        String message = String.format(context.getString(R.string.check_permission_denied_msg),
                msg, context.getString(R.string.app_name));
        new TedPermission(context)
                .setPermissionListener(listener)
                .setDeniedMessage(message)
                .setPermissions(permission1, permission2)
                .check();
    }

    public static boolean checkExternalStorageMounted(Activity activity){
        String status = Environment.getExternalStorageState();
        boolean result = status.equalsIgnoreCase(Environment.MEDIA_MOUNTED);
        if (!result) {
            BraveUtils.showToast(activity, activity.getString(R.string.toast_check_external_storage));
        }
        return result;
    }

    public static boolean checkCamera(Activity activity){
        boolean result = activity.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
        if(!result){
            BraveUtils.showToast(activity, activity.getString(R.string.toast_qa_check_camera));
        }
        return result;
    }

    public static boolean checkMicrophone(Activity activity){
        boolean result = activity.getPackageManager().hasSystemFeature(PackageManager.FEATURE_MICROPHONE);
        if(!result){
            BraveUtils.showToast(activity, activity.getString(R.string.toast_qa_check_mic));
        }
        return result;
    }

    public static boolean checkTelephony(Activity activity){
        boolean result = activity.getPackageManager().hasSystemFeature(PackageManager.FEATURE_TELEPHONY);
        if(!result){
            BraveUtils.showToast(activity, activity.getString(R.string.toast_qa_check_telephony));
        }
        return result;
    }

    // =============================================================================
    // Mobile Web URL
    // =============================================================================

    public static String getBaseWebUrl(Context context) {
        return String.format(context.getString(R.string.base_web_url), ModooGong.host);
    }

    public static String getLinkWebLoginFindInfoUrl(Context context) {
        return getBaseWebUrl(context) + context.getString(R.string.web_login_find_info_url);
    }

    public static String getLinkWebJoinUrl(Context context) {
        return getBaseWebUrl(context) + context.getString(R.string.web_join_url);
    }

    public static String getBaseMobileWebUrl(Context context) {
        return String.format(context.getString(R.string.base_mobile_web_url), ModooGong.host);
    }

    public static String getLinkLoginFindIDUrl(Context context) {
        return getBaseMobileWebUrl(context) + context.getString(R.string.login_find_id_url);
    }

    public static String getLinkLoginFindPWUrl(Context context) {
        return getBaseMobileWebUrl(context) + context.getString(R.string.login_find_pw_url);
    }

    public static String getLinkJoinUrl(Context context) {
        return getBaseMobileWebUrl(context) + context.getString(R.string.join_url);
    }

    public static String getBaseModooUrl(Context context) {
        return String.format(context.getString(R.string.base_modoo_url), ModooGong.host);
    }

    public static String getLinkModooLoginFindIDUrl(Context context) {
        return context.getString(R.string.modoo_login_find_id_url) + getBaseModooUrl(context);
    }

    public static String getLinkModooLoginFindPWUrl(Context context) {
        return context.getString(R.string.modoo_login_find_pw_url) + getBaseModooUrl(context);
    }

    public static String getLinkModooJoinUrl(Context context) {
        return context.getString(R.string.modoo_join_url) + getBaseModooUrl(context);
    }

    // =============================================================================
    // App
    // =============================================================================

    public static boolean checkAppClearData(Context context){
        String lastVer = PropertyManager.getInstance().getAppVer();
        String nowVer = SystemUtils.getAppVersionName(context);
        if(lastVer != null){
            if(nowVer != null && !lastVer.equals(nowVer)){
                //check app version
                /*
                String sub = nowVer.substring(2);
                if(sub.equals("x.x")){
                    return true;
                }
                */
            }else{
                if(nowVer == null){
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    public static boolean checkAppUpdate(Context context, String storeVer){
        String nowVer = SystemUtils.getAppVersionName(context);
        if(storeVer != null && nowVer != null){
            String[] stores = storeVer.split("\\.");
            String[] nows = nowVer.split("\\.");

            if(stores.length ==3 && nows.length ==3) {
                if (Integer.valueOf(stores[0]) > Integer.valueOf(nows[0])) {
                    return true;
                } else if (Integer.valueOf(stores[1]) > Integer.valueOf(nows[1])) {
                    return true;
                } else if (Integer.valueOf(stores[2]) > Integer.valueOf(nows[2])) {
                    return true;
                }
            }
        }
        return false;
    }
}
