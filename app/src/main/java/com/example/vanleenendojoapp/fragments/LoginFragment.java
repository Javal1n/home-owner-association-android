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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.Objects;

public class LoginFragment extends Fragment implements View.OnClickListener {

    EditText etEmail;
    EditText etPassword;
    Button btnLogIn;
    private FirebaseAuth fAuth;
    private FirebaseFirestore db;

    private SignUpListener signUpListener;

    public interface SignUpListener {
        void callSignUp();
        void goToHome(String uID, String communityID);
    }

    public LoginFragment() {
        //Required default constructor
    }

    public static LoginFragment newInstance() {

        Bundle args = new Bundle();

        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof SignUpListener) {
            signUpListener = (SignUpListener) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getView() != null) {
            getView().findViewById(R.id.tv_signup).setOnClickListener(this);
            etEmail = getView().findViewById(R.id.et_email_login);
            etPassword = getView().findViewById(R.id.et_password);
            fAuth = FirebaseAuth.getInstance();
            db = FirebaseFirestore.getInstance();
            getView().findViewById(R.id.btn_login).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tv_signup:
                signUpListener.callSignUp();
                break;
            case R.id.btn_login:
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (email.isEmpty()){
                    Toast.makeText(getContext(), "Email is required", Toast.LENGTH_SHORT).show();
                }
                if(password.isEmpty()){
                    Toast.makeText(getContext(), "Password is required", Toast.LENGTH_SHORT).show();
                }
                if(password.length() < 6){
                    Toast.makeText(getContext(), "Password has to be 6 characters or longer", Toast.LENGTH_SHORT).show();
                }

                if(!email.isEmpty() && !password.isEmpty()){
                    fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){
                                Toast.makeText(getContext(), "Log in was successful", Toast.LENGTH_SHORT).show();
                                String userId = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();
                                getCommunityId(userId);
                            } else {
                                Toast.makeText(getContext(), "Incorrect password or email", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    break;
                }
        }
    }

    private void getCommunityId(final String userID) {
        final String[] communityCode = new String[1];
        db.collection("Resident_User").whereEqualTo("user_id", userID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                communityCode[0] = (String) document.get("community_code");
                                Log.i("TAG", "onComplete: " + communityCode[0]);
                                Log.i("TAG", "onComplete: " + userID);
                            }
                        }

                        if (task.isComplete()) {
                            signUpListener.goToHome(userID, communityCode[0]);
                        }
                    }
                });
    }
}
