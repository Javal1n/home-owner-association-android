package com.example.vanleenendojoapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.vanleenendojoapp.R;
import com.example.vanleenendojoapp.data_models.Post;

import java.util.ArrayList;

public class CustomPostAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Post> mHomePosts;

    public CustomPostAdapter(Context context, ArrayList<Post> homePosts) {

        mContext = context;
        mHomePosts = homePosts;
    }

    @Override
    public int getCount() {
        if(mHomePosts != null){
            return mHomePosts.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        if(mHomePosts != null && i >= 0 && i < mHomePosts.size()){
            return mHomePosts.get(i);
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder vh;

        if(view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.home_post_list_item, viewGroup, false);
            vh = new ViewHolder(view);
            view.setTag(vh);
        }
        else {
            vh = (ViewHolder) view.getTag();
        }

        if(mHomePosts.get(i) != null){

            vh.subjectTv.setText(mHomePosts.get(i).getmSubject());
            vh.usernameTv.setText(mHomePosts.get(i).getmUsername());
            vh.textContentTv.setText(mHomePosts.get(i).getmMessage());
        }
        return view;
    }

    public static class ViewHolder {
        TextView subjectTv;
        TextView usernameTv;
        TextView textContentTv;

        public ViewHolder(View layout) {
            this.subjectTv = layout.findViewById(R.id.subject_tv);
            this.usernameTv = layout.findViewById(R.id.username_tv);
            this.textContentTv = layout.findViewById(R.id.text_content);
        }
    }
}
