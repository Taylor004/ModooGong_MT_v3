package kr.co.bravecompany.modoogong.android.stdapp.pageLectureList;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import kr.co.bravecompany.modoogong.android.stdapp.view.AnimatedExpandableListView;
import kr.co.bravecompany.modoogong.android.stdapp.data.LectureMenuGroupData;
import kr.co.bravecompany.modoogong.android.stdapp.data.LectureMenuData;

/**
 * Created by BraveCompany on 2016. 10. 17..
 */

public class LectureMenuAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter {
    List<LectureMenuGroupData> items = new ArrayList<LectureMenuGroupData>();

    public void put(int groupTag, String childName) {
        LectureMenuGroupData match = null;
        for (LectureMenuGroupData g : items) {
            if (g.getTag() == groupTag) {
                match = g;
                break;
            }
        }
        boolean isNew = (match == null);
        if (isNew) {
            match = new LectureMenuGroupData();
            match.setTag(groupTag);
            match.setGroupName(childName);
            items.add(match);
        }

        if (!TextUtils.isEmpty(childName)) {
            LectureMenuData child = new LectureMenuData();
            child.setChildName(childName);
            match.getChildren().add(child);
        }

        notifyDataSetChanged();
    }

    public void setLectureCount(int groupTag, int count){
        for (LectureMenuGroupData g : items) {
            if (g.getTag() == groupTag) {
                g.setLectureCount(count);
                break;
            }
        }
        notifyDataSetChanged();
    }

    public void setSelectChild(int groupPosition, int childPosition){
        List<LectureMenuData> children = items.get(groupPosition).getChildren();
        for(int i=0; i<children.size(); i++){
            children.get(i).setSelected(i == childPosition);
        }
        items.get(groupPosition).setGroupName(children.get(childPosition).getChildName());
        notifyDataSetChanged();
    }

    public int getSelectedChildIndex(int groupPosition){
        List<LectureMenuData> children = items.get(groupPosition).getChildren();
        for(int i=0; i<children.size(); i++){
            if(children.get(i).isSelected()){
                return i;
            }
        }
        return  -1;
    }

    @Override
    public int getGroupCount() {
        return items.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return items.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return items.get(groupPosition).getChildren().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return ((long)groupPosition)<<32|0xFFFFFFFF;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return ((long)groupPosition)<<32|childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        LectureMenuGroupView view;
        if (convertView == null) {
            view = new LectureMenuGroupView(parent.getContext());
        } else {
            view = (LectureMenuGroupView)convertView;
        }
        view.setGroupItem(items.get(groupPosition));
        return view;
    }

    @Override
    public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        LectureMenuView view;
        if (convertView == null) {
            view = new LectureMenuView(parent.getContext());
        } else {
            view = (LectureMenuView)convertView;
        }
        view.setChildItem(items.get(groupPosition).getChildren().get(childPosition));
        return view;
    }

    @Override
    public int getRealChildrenCount(int groupPosition) {
        return items.get(groupPosition).getChildren().size();
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
