package com.example.vanleenendojoapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.vanleenendojoapp.R;
import java.util.ArrayList;

public class CustomConvoAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<String> mPeople;

    public CustomConvoAdapter(Context context, ArrayList<String> people) {

        mContext = context;
        mPeople = people;
    }

    @Override
    public int getCount() {
        if(mPeople != null){
            return mPeople.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        if(mPeople != null && i >= 0 && i < mPeople.size()){
            return mPeople.get(i);
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        CustomConvoAdapter.ViewHolder vh;

        if(view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.conversation_list_item, viewGroup, false);
            vh = new CustomConvoAdapter.ViewHolder(view);
            view.setTag(vh);
        }
        else {
            vh = (CustomConvoAdapter.ViewHolder) view.getTag();
        }

        if(mPeople.get(i) != null){
            vh.usernameTv.setText(mPeople.get(i));
        }
        return view;
    }

    public static class ViewHolder {
        TextView usernameTv;

        public ViewHolder(View layout) {
            this.usernameTv = layout.findViewById(R.id.convo_username_tv);
        }
    }
}
