package kr.co.bravecompany.modoogong.android.stdapp.Zremoved;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kr.co.bravecompany.modoogong.android.stdapp.R;

public class WeeklyLectureButtonListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<WeeklyLectureButtonListData> mWeeklyLectureButtonList;


   public static class MyViewHolder extends RecyclerView.ViewHolder
   {
       Button mButton;



       MyViewHolder(View view)
       {
           super(view);
           mButton = view.findViewById(R.id.weeklySubjectLectureBtn);
       }


   }

    public WeeklyLectureButtonListAdapter(ArrayList<WeeklyLectureButtonListData> weeklyLectureButtonList)
    {
        this.mWeeklyLectureButtonList = weeklyLectureButtonList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_weekly_subjects_pure_time_btn,
               parent,false);


        return new WeeklyLectureButtonListAdapter.MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

       final MyViewHolder myViewHolder = (MyViewHolder) holder;

       myViewHolder.mButton.setText(mWeeklyLectureButtonList.get(position).getmButtonText());


    }

    @Override
    public int getItemCount() {
        return mWeeklyLectureButtonList.size();
    }

}
