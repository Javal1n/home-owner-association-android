package com.example.vanleenendojoapp.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.vanleenendojoapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ProfileFragment extends Fragment {


    private FirebaseFirestore db;

    public static final String ARGS_USERID = "ARGS_USERID";
    public static final String ARGS_COMMUNITY_ID = "ARGS_COMMUNITY_ID";

    ImageButton profileImage;
    String userId;
    String communityId;
    TextView fullName;
    TextView communityName;
    TextView verified;


    public ProfileFragment() {
        // Required default constructor
    }

    public static ProfileFragment newInstance(String userId, String communityId) {

        Bundle args = new Bundle();

        args.putString(ARGS_USERID, userId);
        args.putString(ARGS_COMMUNITY_ID, communityId);

        ProfileFragment fragment = new ProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {

        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(getView() != null && getArguments() != null){

            profileImage = getView().findViewById(R.id.profile_image);
            fullName = getView().findViewById(R.id.full_nameTXT);
            communityName = getView().findViewById(R.id.community_nameTXT);
            verified = getView().findViewById(R.id.verifiedTXT);

            userId = getArguments().getString(ARGS_USERID);
            communityId = getArguments().getString(ARGS_COMMUNITY_ID);

            db = FirebaseFirestore.getInstance();

            Log.i("TAG", "onActivityCreated: " + userId);
            Log.i("TAG", "onActivityCreated: " + communityId);

            getUserInfo(userId);
            getCommunityInfo(communityId);

            profileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(getContext() != null){
                        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pickPhoto , 1);
                    }
                }
            });

            verified.setText("True");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            Uri selectedImage = data.getData();
            profileImage.setImageURI(selectedImage);
        }
    }

    private void getUserInfo(final String userID) {
        final String[] userFullName = new String[1];

        db.collection("Resident_User").whereEqualTo("user_id", userID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                userFullName[0] = (String) document.get("full_name");
                                Log.i("TAG", "onComplete: " + userFullName[0]);
                            }
                        }
                        if (task.isComplete()) {
                            if(userFullName[0] != null){
                                fullName.setText(userFullName[0]);
                            }
                        }
                    }
                });
    }

    private void getCommunityInfo(final String communityId) {
        final String[] userCommunityName = new String[1];

        db.collection("Communities").whereEqualTo("community_id", communityId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                userCommunityName[0] = (String) document.get("community_name");
                                Log.i("TAG", "onComplete: " + userCommunityName[0]);
                            }
                        }
                        if (task.isComplete()) {
                            if(userCommunityName[0] != null){
                                communityName.setText(userCommunityName[0]);
                            }
                        }
                    }
                });
    }
}
