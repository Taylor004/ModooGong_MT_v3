package kr.co.bravecompany.modoogong.android.stdapp.utils;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.modoogong.android.stdapp.activity.MainActivity;

public class UIUtils {

    public static void startAnimation(ProgressBar progressBar, int start, int end, int time)
    {
        int width = progressBar.getWidth();
        progressBar.setMax(100);
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.setInterpolator(new LinearInterpolator());
        animator.setStartDelay(0);
        animator.setDuration(time);

        ProgressAnimator aniUpdater = new ProgressAnimator();
        aniUpdater.progressBar = progressBar;

        animator.addUpdateListener(aniUpdater);

        animator.start();
    }

    public static class ProgressAnimator implements  ValueAnimator.AnimatorUpdateListener
    {
        public  ProgressBar progressBar;

        @Override
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            int value = (int) valueAnimator.getAnimatedValue();
            progressBar.setProgress(value);
        }
    }

    public static void InputText(final Activity context, String title,String desc, String hintText, final InputResultListener listener)
    {
        // Set up the input
        final EditText input =new EditText(context);
        input.setHint(hintText);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(desc);
        builder.setView(input);


        // Set up the buttons
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
                dialog.dismiss();
                String result =  input.getText().toString();

                //                MainActivity mainActivity = new MainActivity(); //MainActivity에 있는 mTodayMessageButton 과 mViewTodayMessageDivider를 전역으로 불러오기 위해서 생성.
//
//                if(result.length() == 0) //입력된 텍스트가 하나도 없다면
//                {
//                    //result = "오늘의 화이팅 한마디";
//                    //mainActivity.txtTodayMessage.setTextColor(Color.parseColor("#c0c0c0"));
//                    mainActivity.mViewTodayMessageDivider.setVisibility(View.VISIBLE);
//                    mainActivity.mTodayMessageButton.setVisibility(View.VISIBLE); // 오늘의 한미디 편집 아이콘 상시 노출로 수정[2019.10.17 테일러]
//                }
//                else //입력된 텍스트가 하나라도 있다면
//                {
//                    mainActivity.txtTodayMessage.setTextColor(Color.parseColor("#ffffff")); // 텍스트의 색상을 하얀색으로 변경
//                    mainActivity.mViewTodayMessageDivider.setVisibility(View.INVISIBLE);
//                    mainActivity.mTodayMessageButton.setVisibility(View.VISIBLE);
//                }

                listener.onInputText(result);

                //input.clearFocus();
                //SystemUtils.hideSoftKeyboard(context);

            }
        });

        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(input.getWindowToken(), 0);

                dialog.cancel();

                //input.clearFocus();
                //SystemUtils.hideSoftKeyboard(context);
            }
        });

        builder.show();
    }

    public static void ResizeGridViewDynamicHeight(GridView gridView)
    {
        ListAdapter gridViewAdapter = gridView.getAdapter();// 데이터가 들어있는 adapter를 받아서 ListApater에 저장.

        int totalHeight = 0; // gridView의 레이아웃 최대 높이
        int emptyItemHeight = 1; // gridView에 아이템이 없을 때 레이아웃 높이
        int item = gridViewAdapter.getCount(); // 어댑터의 있던 아이템 리스트의 사이즈(총 갯수)
        int rows = 0; // GridView 열 단위를 변경시키기 위한 변수
        int maxCols = 3; // GridView의 MaxColumn 수   (*gridView.getNumColumns(); --> 그리드 뷰의 컬럼값을 받아오지 못하는 이슈 발생으로 임의로 3으로 변경.)
        float x = 1.0f;

        if(gridViewAdapter == null) // 어댑터를 받지 못했다면 예외 처리
        {
            rows = (int) x;
            emptyItemHeight *= rows;

            ViewGroup.LayoutParams params = gridView.getLayoutParams();
            params.height = emptyItemHeight;
            gridView.setLayoutParams(params);
            return;
        }

        View listItem = gridViewAdapter.getView(0,null,gridView);
        listItem.measure(0,0); //measure()를 통해서 아이템의 넓이, 높이의 초기 지정.
        totalHeight = listItem.getMeasuredHeight(); // 하나의 아이템 높이 값 저장.



        if(item > maxCols) // 총 아이템 사이즈가 3 보다 크면(아이템이 3개 이상 있다면)
        {
            x = item % maxCols; // GridView의 rows를 늘리기 위해
            if(x == 0)
            {
                rows = item / maxCols;
                totalHeight *= rows;

            }
            else
            {
                rows = (int) ((item/ maxCols) + 1); // rows값 증가
                totalHeight *= rows; // GridView 레이아웃 최대 높이를 지정

            }
        }

        ViewGroup.LayoutParams params = gridView.getLayoutParams(); // GridView의 레이아웃 속성들을 받아옴.
        params.height = totalHeight; // GridView의 최대 레이아웃 높이를 변경.
        gridView.setLayoutParams(params); // 높이를 변경한 것을 다시 GridView에 설정.
    }


    public static void ResizeListViewHeightBasedOnChildren(ListView listView) {

        ListAdapter adapter = listView.getAdapter();

        if (adapter == null) {
            return;
        }
        ViewGroup vg = listView;
        int totalHeight = 0;

        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, vg);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams par = listView.getLayoutParams();
        par.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(par);
        listView.requestLayout();
    }

    public interface InputResultListener
    {
        void onInputText(String result);
    }

}
