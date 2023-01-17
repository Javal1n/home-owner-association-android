package com.example.vanleenendojoapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.vanleenendojoapp.R;
import com.example.vanleenendojoapp.data_models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterFragment extends Fragment implements View.OnClickListener {

    EditText etUsername;
    EditText etEmail;
    EditText etPassword;
    EditText etPasswordConfirm;
    private LoginListener loginListener;
    FirebaseAuth fAuth;
    private User newUser;

    public interface LoginListener {
        void callLogin();
        void goToVerification(User _user);
    }

    public RegisterFragment() {
        //Required default constructor
    }
    public static RegisterFragment newInstance() {

        Bundle args = new Bundle();

        RegisterFragment fragment = new RegisterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof LoginListener) {
            loginListener = (LoginListener) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getView() != null) {
            etUsername = getView().findViewById(R.id.et_username);
            etEmail = getView().findViewById(R.id.et_email_signup);
            etPassword = getView().findViewById(R.id.et_password_signup);
            etPasswordConfirm = getView().findViewById(R.id.et_confirm_password);
            fAuth = FirebaseAuth.getInstance();

            getView().findViewById(R.id.btn_next).setOnClickListener(this);
            getView().findViewById(R.id.tv_login).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.tv_login:
                loginListener.callLogin();
                break;
            case R.id.btn_next:

                // Grab values from UI elements
                final String etUName = etUsername.getText().toString();
                final String etEmailSignUp = etEmail.getText().toString().trim().toLowerCase();
                final String etPasswordSignUp = etPassword.getText().toString().trim();
                final String etPasswordConfirmSignUp = etPasswordConfirm.getText().toString().trim();

                // Validate email
                if (!validateEmail(etEmailSignUp)) {
                    Toast.makeText(getContext(), "Please input correct email address structure", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Validate password
                if (!validatePassword(etPasswordSignUp)) {
                    Toast.makeText(getContext(), "Password must be 8 characters long (no spaces) and contain one uppercase, lowercase, special character, and one digit", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Make sure passwords match
                if (!etPasswordSignUp.equals(etPasswordConfirmSignUp)) {
                    Toast.makeText(getContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Make sure all fields are filled before going to Verification process
                if (!etUName.isEmpty() && !etEmailSignUp.isEmpty() && !etPasswordSignUp.isEmpty()) {

                    newUser = new User(etUName, etEmailSignUp, etPasswordSignUp);

                    // Call interface to send the user to the
                    loginListener.goToVerification(newUser);

                } else {
                    Toast.makeText(getContext(), "Please fill all fields before signing up", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    // Correct email address structure
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    // Password must be 8 characters long (no spaces) and contain one uppercase, lowercase, special character, and one digit
    public static final Pattern VALID_PASSWORD_REGEX =
            Pattern.compile("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}", Pattern.CASE_INSENSITIVE);

    public static boolean validatePassword(String passwordStr) {
        Matcher matcher = VALID_PASSWORD_REGEX.matcher(passwordStr);
        return matcher.find();
    }
}
