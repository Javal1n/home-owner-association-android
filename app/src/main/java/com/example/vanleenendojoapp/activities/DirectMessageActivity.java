package com.example.vanleenendojoapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import com.example.vanleenendojoapp.R;
import com.example.vanleenendojoapp.fragments.DirectMessageFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;

public class DirectMessageActivity extends AppCompatActivity {

    private String uID;
    private String communityId;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direct_message);

        db = FirebaseFirestore.getInstance();

        if (getIntent() != null) {
            Bundle bundle = getIntent().getExtras();

            // Grab values from the bundle passed by intent
            if (bundle != null) {
                uID = bundle.getString("EXTRA_USERID");
                communityId = bundle.getString("EXTRA_COMMUNITY_ID");
            }
        }

        getConversations();

        // Set up action bar
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_dm);
    }

    private void getConversations() {
        final ArrayList<String> userNames = new ArrayList<>();
        final String[] userName = new String[1];

        db.collection("Conversations").whereEqualTo("secondary_user_name", true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                // Grab value
                                userName[0] = (String) document.get("secondary_user_name");

                                // Add user name to list
                                userNames.add(userName[0]);
                            }
                        }

                        if (task.isComplete()) {
                            // Call list view fragment to display posts
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.dm_list_fragment_container, DirectMessageFragment.newInstance(userNames))
                                    .commit();

                            Log.i("TAG", "getPosts: " + userNames.size());
                            Log.i("TAG", "getPosts: " + "DM Fragment Called");
                        }

                    }
                });
    }
}