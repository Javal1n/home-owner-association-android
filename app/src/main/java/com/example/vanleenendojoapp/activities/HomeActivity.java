package com.example.vanleenendojoapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.vanleenendojoapp.R;
import com.example.vanleenendojoapp.data_models.Groups;
import com.example.vanleenendojoapp.data_models.Post;
import com.example.vanleenendojoapp.fragments.CreateGroupFragment;
import com.example.vanleenendojoapp.fragments.GroupsFragment;
import com.example.vanleenendojoapp.fragments.HomeFragment;
import com.example.vanleenendojoapp.fragments.ProfileFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity implements HomeFragment.PostListener, GroupsFragment.CreateGroupListener, View.OnClickListener {

    // Variables
    String userID = "";
    String communityID = "";
    private FirebaseFirestore db;

    public static final String EXTRA_USERID = "EXTRA_USERID";
    public static final String EXTRA_COMMUNITY_ID = "EXTRA_COMMUNITY_ID";


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        db = FirebaseFirestore.getInstance();

        Bundle bundle = getIntent().getExtras();

        // Grab values from the bundle passed by intent
        if (bundle != null) {
            userID = bundle.getString("EXTRA_USERID");
            communityID = bundle.getString("EXTRA_COMMUNITY_ID");

            if(Objects.equals(bundle.getString("EXTRA_FRAGMENT_ID"), "groups")){
                getGroups(communityID);
                Log.i("TAG", "onCreate: WE GOT INSIDE THE IF STATEMENT");
            }

            Log.i("TAG", "onCreate: " + userID);
            Log.i("TAG", "onCreate: " + communityID);
        }

        getPosts(communityID);

        // Set on click listener for bottom nav buttons
        findViewById(R.id.btn_profile).setOnClickListener(this);
        findViewById(R.id.btn_groups).setOnClickListener(this);
        findViewById(R.id.btn_home).setOnClickListener(this);
    }

    // Go to post creation screen
    @Override
    public void callPostActivity() {
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_USERID, userID);
        bundle.putString(EXTRA_COMMUNITY_ID, communityID);
        Intent goToPostIntent = new Intent(this, PostActivity.class);
        goToPostIntent.putExtras(bundle);
        startActivity(goToPostIntent);
    }

    @Override
    public void callCreateGroupActivity() {
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_USERID, userID);
        bundle.putString(EXTRA_COMMUNITY_ID, communityID);

        Intent goToCreateGroupIntent = new Intent(this, CreateGroupActivity.class);
        goToCreateGroupIntent.putExtras(bundle);
        startActivity(goToCreateGroupIntent);
    }

    public void callDMActivity() {
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_USERID, userID);
        Intent goToDmIntent = new Intent(this, DirectMessageActivity.class);
        goToDmIntent.putExtras(bundle);
        startActivity(goToDmIntent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_profile:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.list_fragment_container, ProfileFragment.newInstance(userID, communityID))
                        .commit();
                break;

            case R.id.btn_home:
                getPosts(communityID);
                break;

            case R.id.btn_groups:
                getGroups(communityID);
                break;
        }
    }

    private void getGroups(String communityId){

        final ArrayList<Groups> groups = new ArrayList<>();
        final ArrayList<String> members = new ArrayList<>();
        final String[] groupName = new String[1];


        db.collection("Groups").whereEqualTo("community_id", communityId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                //List<String> stringList = (List<String>) document.get("members");
                                //ArrayList<String> members = new ArrayList<>(stringList.size());
                                //members.addAll(stringList);
                                groupName[0] = (String) document.get("group_name");
                                members.add((String) document.get("member"));
                                Log.i("TAG", "getMembers: " + members.toString());
                                groups.add(new Groups(groupName[0], members));
                            }
                        }

                        if (task.isComplete()) {
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.list_fragment_container, GroupsFragment.newInstance(userID, communityID, groups))
                                    .commit();

                            Log.i("TAG", "GetGroupsTaskIsComplete: " + "Group Fragment Called");

                        }

                    }
                });
    }

    // Get posts from Firestore database
    private void getPosts(String communityId) {
        final ArrayList<Post> posts = new ArrayList<>();
        final String[] userName = new String[1];
        final String[] subject = new String[1];
        final String[] textContent = new String[1];

        db.collection("Posts").whereEqualTo("community_id", communityId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                // Grab values
                                userName[0] = (String) document.get("user_name");
                                subject[0] = (String) document.get("subject");
                                textContent[0] = (String) document.get("text_content");

                                // Create new posts and add to list
                                posts.add(new Post(subject[0], textContent[0], userName[0]));
                            }
                        }

                        if (task.isComplete()) {
                            // Call list view fragment to display posts
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.list_fragment_container, HomeFragment.newInstance(posts))
                                    .commit();

                            Log.i("TAG", "POST SIZE: " + posts.size());
                            Log.i("TAG", "HomeTaskIsComplete: " + "Home Fragment Called");
                        }

                    }
                });
    }
}
