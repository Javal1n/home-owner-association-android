package com.example.vanleenendojoapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.vanleenendojoapp.R;
import com.example.vanleenendojoapp.fragments.CreateGroupFragment;
import com.example.vanleenendojoapp.fragments.PostFragment;

public class CreateGroupActivity extends AppCompatActivity implements CreateGroupFragment.GroupsListener {

    // Variables
    String userID = "";
    String communityID = "";

    public static final String EXTRA_FRAGMENT_ID = "EXTRA_FRAGMENT_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            userID = bundle.getString("EXTRA_USERID");
            communityID = bundle.getString("EXTRA_COMMUNITY_ID");
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.group_fragment_container, CreateGroupFragment.newInstance(userID, communityID))
                .commit();
    }

    @Override
    public void callGroupFragment() {
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_FRAGMENT_ID, "groups");
        Intent goToHomeIntent = new Intent(this, HomeActivity.class);
        goToHomeIntent.putExtras(bundle);
        startActivity(goToHomeIntent);
    }
}