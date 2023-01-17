package com.example.vanleenendojoapp.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.vanleenendojoapp.R;
import com.example.vanleenendojoapp.data_models.User;
import com.example.vanleenendojoapp.fragments.VerificationFragment;

public class VerificationActivity extends AppCompatActivity implements VerificationFragment.SignUpListener {

    public static final String EXTRA_USER = "EXTRA_USER";
    public User user;
    public static final String TAG = "TAG";
    public static final String EXTRA_USERID = "EXTRA_USERID";
    public static final String EXTRA_COMMUNITY_ID = "EXTRA_COMMUNITY_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        if (getIntent() != null) {
            if (getIntent().hasExtra(EXTRA_USER)) {
                user = (User) getIntent().getSerializableExtra(EXTRA_USER);

                if (user != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.verification_fragment_container, VerificationFragment.newInstance(user))
                            .commit();
                }
            }
        }
    }

    // Fragment Callbacks
    @Override
    public void callSignUp() {
        Intent signUpIntent = new Intent(this, RegisterActivity.class);
        startActivity(signUpIntent);
    }

    @Override
    public void callHome(String userID, String communityID) {

        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_USERID, userID);
        bundle.putString(EXTRA_COMMUNITY_ID, communityID);
        Intent goToHomeIntent = new Intent(this, HomeActivity.class);
        goToHomeIntent.putExtras(bundle);
        startActivity(goToHomeIntent);
    }
}
