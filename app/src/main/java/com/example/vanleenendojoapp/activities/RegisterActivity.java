package com.example.vanleenendojoapp.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vanleenendojoapp.R;
import com.example.vanleenendojoapp.data_models.User;
import com.example.vanleenendojoapp.fragments.RegisterFragment;

public class RegisterActivity extends AppCompatActivity implements RegisterFragment.LoginListener {

    public static final String EXTRA_USER = "EXTRA_USER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.register_fragment_container, RegisterFragment.newInstance())
                .commit();
    }

    @Override
    public void callLogin() {
        Intent loginIntent = new Intent(this, MainActivity.class);
        startActivity(loginIntent);
    }

    @Override
    public void goToVerification(User _user) {
        Intent goToVerificationIntent = new Intent(this, VerificationActivity.class);
        goToVerificationIntent.putExtra(EXTRA_USER, _user);
        startActivity(goToVerificationIntent);
    }
}
