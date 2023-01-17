package com.example.vanleenendojoapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import com.example.vanleenendojoapp.R;
import com.example.vanleenendojoapp.adapters.CustomGroupsAdapter;
import com.example.vanleenendojoapp.adapters.CustomPostAdapter;
import com.example.vanleenendojoapp.data_models.Groups;
import com.example.vanleenendojoapp.data_models.Post;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class GroupsFragment extends ListFragment {

    private CreateGroupListener createGroupListener;
    public static final String ARGS_GROUPS = "ARGS_GROUPS";
    public static final String ARGS_USERID = "ARGS_USERID";
    public static final String ARGS_COMMUNITY_ID = "ARGS_COMMUNITY_ID";

    private FirebaseFirestore db;
    String userId;
    String communityId;
    ArrayList<Groups> groups;

    public GroupsFragment() {
        //Default Constructor
    }

    public interface CreateGroupListener {
        void callCreateGroupActivity();
    }

    public static GroupsFragment newInstance(String userId, String communityId, ArrayList<Groups> groupsList) {

        Bundle args = new Bundle();

        args.putString(ARGS_USERID, userId);
        args.putString(ARGS_COMMUNITY_ID, communityId);
        args.putSerializable(ARGS_GROUPS, groupsList);

        GroupsFragment fragment = new GroupsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static GroupsFragment newInstance() {
        
        Bundle args = new Bundle();
        
        GroupsFragment fragment = new GroupsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof CreateGroupListener) {
            createGroupListener = (CreateGroupListener) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_groups, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(getView() != null && getArguments() != null){

            //db = FirebaseFirestore.getInstance();

            groups = (ArrayList<Groups>) getArguments().getSerializable(ARGS_GROUPS);
//            ArrayList<String> newArrayList = new ArrayList<String>();
//            newArrayList.add("jnfojnvfjnv");
//            Groups newGroup = new Groups("Richard", newArrayList);
//            ArrayList<Groups> groupArray = new ArrayList<>();
//            groupArray.add(newGroup);
            Log.i("TAG", "onActivityCreated: " + groups.size());
            userId = getArguments().getString(ARGS_USERID);
            communityId = getArguments().getString(ARGS_COMMUNITY_ID);

            if (groups != null && getContext() != null) {
                CustomGroupsAdapter customGroupsAdapter = new CustomGroupsAdapter(getContext(), groups);

                ListView listView = getView().findViewById(android.R.id.list);

                listView.setAdapter(customGroupsAdapter);

                Log.i("TAG", "Inside groups fragment onActivityCreated:  GOT HERE");
            }

            Log.i("TAG", "onActivityCreated: " + userId);
            Log.i("TAG", "onActivityCreated: " + communityId);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.menu_item_create){
            createGroupListener.callCreateGroupActivity();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu_groups, menu);
    }
}
