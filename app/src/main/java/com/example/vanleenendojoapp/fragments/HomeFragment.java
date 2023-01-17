package com.example.vanleenendojoapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;
import com.example.vanleenendojoapp.R;
import com.example.vanleenendojoapp.adapters.CustomPostAdapter;
import com.example.vanleenendojoapp.data_models.Post;

import java.util.ArrayList;

public class HomeFragment extends ListFragment {

    private PostListener postListener;
    public static final String ARGS_POSTS = "ARGS_POSTS";
    private ArrayList<Post> posts;

    public interface PostListener {
        void callPostActivity();
        void callDMActivity();
    }

    public HomeFragment() {
        // Required default constructor
    }

    public static HomeFragment newInstance(ArrayList<Post> postList) {

        Bundle args = new Bundle();

        args.putSerializable(ARGS_POSTS, postList);
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {

        super.onAttach(context);

        if (context instanceof PostListener) {
            postListener = (PostListener) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getView() != null && getArguments() != null) {
            posts = (ArrayList<Post>) getArguments().getSerializable(ARGS_POSTS);

            if (posts != null && getContext() != null) {
                CustomPostAdapter customPostAdapter = new CustomPostAdapter(getContext(), posts);

                ListView listView = getView().findViewById(android.R.id.list);

                listView.setAdapter(customPostAdapter);
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        // Code here
        switch (item.getItemId()) {
            case R.id.menu_item_post:
                postListener.callPostActivity();
                break;
            case R.id.menu_item_dm:
                postListener.callDMActivity();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu_home, menu);
    }
}
