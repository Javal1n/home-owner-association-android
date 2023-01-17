package com.example.vanleenendojoapp.fragments;

import android.content.Context;
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
import com.example.vanleenendojoapp.data_models.Post;
import com.example.vanleenendojoapp.data_models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PostFragment extends Fragment implements View.OnClickListener {

    EditText subjectET;
    EditText messageET;
    String subjectString;
    String messageString;
    private FirebaseFirestore db;
    String userID;
    String communityID;
    String user_name;

    public static final String ARGS_USERID = "ARGS_USERID";
    public static final String ARGS_COMMUNITY_ID = "ARGS_COMMUNITY_ID";

    public PostFragment() {
        // Required default constructor
    }

    private HomeListener homeListener;

    public interface HomeListener {
        void callHomeActivity();
    }

    public static PostFragment newInstance(String userId, String communityId) {

        Bundle args = new Bundle();
        args.putString(ARGS_USERID, userId);
        args.putString(ARGS_COMMUNITY_ID, communityId);

        PostFragment fragment = new PostFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {

        super.onAttach(context);

        if (context instanceof HomeFragment.PostListener) {
            homeListener = (HomeListener) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_post, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(getView() != null && getArguments() != null) {
            userID = getArguments().getString(ARGS_USERID);
            communityID = getArguments().getString(ARGS_COMMUNITY_ID);

            Button postBtn = (Button) getView().findViewById(R.id.post_btn);
            postBtn.setOnClickListener(this);

            Log.i("TAG", "onActivityCreated: " + userID);
            Log.i("TAG", "onActivityCreated: " + communityID);

            db = FirebaseFirestore.getInstance();

            getUserName(userID);

            subjectET = getView().findViewById(R.id.subject_et);
            messageET = getView().findViewById(R.id.message_et);
        }
    }

    @Override
    public void onClick(View view) {


        if(view.getId() == R.id.post_btn) {

            subjectString = subjectET.getText().toString();
            messageString = messageET.getText().toString();

            if (subjectString.isEmpty() || messageString.isEmpty()) {
                Toast.makeText(getContext(), "Please fill all fields first", Toast.LENGTH_SHORT).show();
            } else {
                Post newPost = new Post(subjectString, messageString, user_name);
                Log.i("TAG", "onClick: " + newPost.getmSubject());
                Log.i("TAG", "onClick: " + newPost.getmMessage());
                Log.i("TAG", "onClick: " + newPost.getmUsername());

                pushPost(newPost);
            }
        }
    }

    public String getDateTimeStamp(){

        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");

        return df.format(date.getTime());
    }

    //Method to write a new post to the database
    private void pushPost(final Post new_post){
        Map<String, String> post_data = new HashMap<>();
        post_data.put("subject", (String) new_post.getmSubject());
        post_data.put("text_content", (String) new_post.getmMessage());
        post_data.put("user_name", (String) new_post.getmUsername());
        post_data.put("community_id", communityID);
        post_data.put("user_id", userID);

        db.collection("Posts").document()
                .set(post_data)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.i("TAG", "onComplete: " + "Post pushed successfully");
                        }
                        if (task.isComplete()) {
                            homeListener.callHomeActivity();
                            Log.i("TAG", "onComplete: " + "Push post complete");

                        }
                    }
                });
    }

    private void getUserName(final String user_id) {
        if (getView() != null) {
            final Button postBtn = getView().findViewById(R.id.post_btn);
            final String[] userName = new String[1];
            db.collection("Resident_User").whereEqualTo("user_id", user_id)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    userName[0] = (String) document.get("full_name");
                                }
                            }

                            if (task.isComplete()) {
                                user_name = userName[0];

                                Log.i("TAG", "onComplete: " + user_name);
                            }
                        }
                    });
        }
    }
}
