package com.example.vanleenendojoapp.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import com.example.vanleenendojoapp.R;
import com.example.vanleenendojoapp.fragments.PostFragment;

public class PostActivity extends AppCompatActivity implements PostFragment.HomeListener {

    // Variables
    String userID = "";
    String communityID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        Bundle bundle = getIntent().getExtras();

        // Grab values from the bundle passed by intent
        if (bundle != null) {
            userID = bundle.getString("EXTRA_USERID");
            communityID = bundle.getString("EXTRA_COMMUNITY_ID");
        }

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_post);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.post_fragment_container, PostFragment.newInstance(userID, communityID))
                .commit();
    }


    @Override
    public void callHomeActivity() {
        Intent goToHomeIntent = new Intent(this, HomeActivity.class);
        startActivity(goToHomeIntent);
    }
}