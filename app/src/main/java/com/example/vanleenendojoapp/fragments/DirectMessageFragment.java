package com.example.vanleenendojoapp.fragments;

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
import com.example.vanleenendojoapp.adapters.CustomConvoAdapter;
import java.util.ArrayList;

public class DirectMessageFragment extends ListFragment {

    private ArrayList<String> userNames;

    public static final String ARGS_USER_NAMES = "ARGS_USER_NAMES";

    public DirectMessageFragment() {
        // Required default constructor
    }

    public static DirectMessageFragment newInstance(ArrayList<String> user_names) {

        Bundle args = new Bundle();
        args.putStringArrayList(ARGS_USER_NAMES, user_names);

        DirectMessageFragment fragment = new DirectMessageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_direct_message, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getView() != null && getArguments() != null) {
            userNames = (ArrayList<String>) getArguments().getStringArrayList(ARGS_USER_NAMES);

            if (userNames != null && getContext() != null) {
                CustomConvoAdapter customConvoAdapter = new CustomConvoAdapter(getContext(), userNames);

                ListView listView = getView().findViewById(android.R.id.list);

                listView.setAdapter(customConvoAdapter);
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
            case R.id.menu_item_dm:

                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu_dm, menu);
    }
}
