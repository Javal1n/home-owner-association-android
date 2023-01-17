package com.example.vanleenendojoapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.vanleenendojoapp.R;
import com.example.vanleenendojoapp.activities.CreateGroupActivity;
import com.example.vanleenendojoapp.data_models.Post;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateGroupFragment extends Fragment {

    private FirebaseFirestore db;
    public static final String ARGS_USERID = "ARGS_USERID";
    public static final String ARGS_COMMUNITY_ID = "ARGS_COMMUNITY_ID";

    Button createGroupBttn;
    EditText groupNameET;
    String groupName;
    String userId;
    String communityId;

    private GroupsListener groupsListener;

    public CreateGroupFragment() {
        //Default Constructor
    }

    public interface GroupsListener {
        void callGroupFragment();
    }

    public static CreateGroupFragment newInstance(String userId, String communityId) {

        Bundle args = new Bundle();

        args.putString(ARGS_USERID, userId);
        args.putString(ARGS_COMMUNITY_ID, communityId);

        CreateGroupFragment fragment = new CreateGroupFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof GroupsListener) {
            groupsListener = (GroupsListener) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_group, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(getView() != null && getArguments() != null){

            db = FirebaseFirestore.getInstance();

            userId = getArguments().getString(ARGS_USERID);
            communityId = getArguments().getString(ARGS_COMMUNITY_ID);
            createGroupBttn = getView().findViewById(R.id.create_btn);
            groupNameET = getView().findViewById(R.id.group_name_et);

            createGroupBttn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    groupName = groupNameET.getText().toString();

                    if(getContext() != null && groupName.trim().length() != 0){
                        pushGroup(groupName);
                        groupsListener.callGroupFragment();
                    }
                    else{
                        Toast.makeText(getContext(), "Error empty text field", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            Log.i("TAG", "onActivityCreated: " + userId);
            Log.i("TAG", "onActivityCreated: " + communityId);
        }
    }

    private void pushGroup(final String theGroupName){
        Map<String, String> group_data = new HashMap<>();
        group_data.put("group_name", theGroupName);
        group_data.put("member", userId);
        group_data.put("community_id", communityId);

        db.collection("Groups").document()
                .set(group_data)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.i("TAG", "onComplete: " + "Group pushed successfully");
                        }
                        if (task.isComplete()) {
                            //homeListener.callHomeActivity(new_post);
                            Log.i("TAG", "onComplete: " + "Push post complete");
                        }
                    }
                });
    }
}
