package com.example.vanleenendojoapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.vanleenendojoapp.R;
import com.example.vanleenendojoapp.data_models.Groups;
import com.example.vanleenendojoapp.data_models.Post;

import java.util.ArrayList;

public class CustomGroupsAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Groups> mHomeGroups;

    public CustomGroupsAdapter(Context context, ArrayList<Groups> homeGroups) {

        mContext = context;
        mHomeGroups = homeGroups;
    }

    @Override
    public int getCount() {
        if(mHomeGroups != null){
            return mHomeGroups.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        if(mHomeGroups != null && i >= 0 && i < mHomeGroups.size()){
            return mHomeGroups.get(i);
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        CustomGroupsAdapter.ViewHolder vh;

        if(view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.group_list_item, viewGroup, false);
            vh = new CustomGroupsAdapter.ViewHolder(view);
            view.setTag(vh);
        }
        else {
            vh = (CustomGroupsAdapter.ViewHolder) view.getTag();
        }

        if(mHomeGroups.get(i) != null){

            vh.groupNameTv.setText(mHomeGroups.get(i).getmGroupName());
            vh.membersTv.setText(mHomeGroups.get(i).getmMembers().get(0));
        }
        return view;
    }

    public static class ViewHolder {
        TextView groupNameTv;
        TextView membersTv;

        public ViewHolder(View layout) {
            this.groupNameTv = layout.findViewById(R.id.groupName_tv);
            this.membersTv = layout.findViewById(R.id.members_tv);
        }
    }
}
