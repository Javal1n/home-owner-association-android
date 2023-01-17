package com.example.vanleenendojoapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.vanleenendojoapp.R;
import com.example.vanleenendojoapp.fragments.LoginFragment;
import com.example.vanleenendojoapp.fragments.RegisterFragment;

public class MainActivity extends AppCompatActivity implements LoginFragment.SignUpListener {

    public static final String EXTRA_USERID = "EXTRA_USERID";
    public static final String EXTRA_COMMUNITY_ID = "EXTRA_COMMUNITY_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.login_fragment_container, LoginFragment.newInstance())
                .commit();
    }

    @Override
    public void callSignUp() {
        Intent signUpIntent = new Intent(this, RegisterActivity.class);
        startActivity(signUpIntent);
    }

    @Override
    public void goToHome(String userID, String communityID) {
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_USERID, userID);
        bundle.putString(EXTRA_COMMUNITY_ID, communityID);
        Intent goToHomeIntent = new Intent(this, HomeActivity.class);
        goToHomeIntent.putExtras(bundle);
        startActivity(goToHomeIntent);
    }
}