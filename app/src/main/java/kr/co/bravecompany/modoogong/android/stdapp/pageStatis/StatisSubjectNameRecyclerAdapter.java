package kr.co.bravecompany.modoogong.android.stdapp.pageStatis;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import kr.co.bravecompany.api.android.stdapp.api.data.Packet;
import kr.co.bravecompany.modoogong.android.stdapp.R;

public class StatisSubjectNameRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public interface OnSubjectNameSelectedListener
    {
        void onSubjectNameSelected(String name);
    }

    public static class StatisSubjectNameRecyclerViewHolder extends RecyclerView.ViewHolder {

        Button name;

        StatisSubjectNameRecyclerViewHolder(View view){
          super(view);
          name = (Button) view.findViewById(R.id.statis_subject_name);
        }

    }

    ArrayList<Packet.SubjectData> subjectList = new ArrayList<>();
    Context mContext = null;
    StatisSubjectNameRecyclerAdapter.OnSubjectNameSelectedListener mListener;
    LayoutInflater mLayoutInflater = null;

    String mSelectedSubjectCD = "";

    StatisSubjectNameRecyclerAdapter(Context context, StatisSubjectNameRecyclerAdapter.OnSubjectNameSelectedListener listener)
    {
        this.mContext = context;

        this.mListener = listener;

        this.mLayoutInflater = LayoutInflater.from(mContext);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_statis_subjects_history_item,parent,false);


        return new StatisSubjectNameRecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {


        StatisSubjectNameRecyclerViewHolder itemView = (StatisSubjectNameRecyclerViewHolder) holder;
        // 여기 바인드뷰홀더에서 뷰들을 설정 해줄것 클릭 이벤트라던지....

        Packet.SubjectData data = subjectList.get(position);

        itemView.name.setText(data.SUBJ_NM);
        //itemView.name.setTypeface(Typeface.DEFAULT_BOLD);
        itemView.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Packet.SubjectData clickData = subjectList.get(position);
                mListener.onSubjectNameSelected(clickData.SUBJ_CD);
                //Selected(clickData.SUBJ_CD);
            }
        });

        boolean selected = data.SUBJ_CD.equalsIgnoreCase(mSelectedSubjectCD);
        itemView.name.setBackgroundResource(selected ? R.drawable.round_background_white : R.drawable.round_background_gray);
        itemView.name.setTextColor(selected ? Color.BLACK : Color.GRAY);
    }

    @Override
    public int getItemCount() {
        return subjectList.size();
    }

    public void addSubjectData0()
    {
        Packet.SubjectData data = new Packet.SubjectData();
        data.SUBJ_NM = "전체";
        data.SUBJ_CD = "0";
        subjectList.add(data);
    }

    public void addSubjectData(Packet.SubjectData data)
    {
         subjectList.add(data);
    }

    public void clearData()
    {
        subjectList.clear();
    }

    public void refresh()
    {
        notifyDataSetChanged();
    }

    public void Selected(String subjCD)
    {
        mSelectedSubjectCD = subjCD;
        notifyDataSetChanged();
    }

}
