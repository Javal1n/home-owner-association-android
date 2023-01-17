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
import com.example.vanleenendojoapp.data_models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class VerificationFragment extends Fragment implements View.OnClickListener {

    public static final String ARGS_USER = "ARGS_USER";

    EditText etCode;
    private User sentOverUser;
    FirebaseAuth fAuth;

    private FirebaseFirestore db;

    private SignUpListener signUpListener;

    public VerificationFragment() {
        // Required default constructor
    }

    public interface SignUpListener {
        void callSignUp();
        void callHome(String uID, String code);
    }

    public static VerificationFragment newInstance(User user) {

        Bundle args = new Bundle();

        args.putSerializable(ARGS_USER, user);
        VerificationFragment fragment = new VerificationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_verification, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof SignUpListener) {
            signUpListener = (SignUpListener) context;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getArguments() != null && getContext() != null) {

            // Get instance of Firestore database
            db = FirebaseFirestore.getInstance();

            // Initialize Firebase Authentication
            fAuth = FirebaseAuth.getInstance();

            // Get the sent over user data from registration screen
            sentOverUser = (User) getArguments().getSerializable(ARGS_USER);

            if (getView() != null) {
                etCode = getView().findViewById(R.id.et_address);

                getView().findViewById(R.id.btn_verify).setOnClickListener(this);
                getView().findViewById(R.id.btn_sign_up).setOnClickListener(this);
                getView().findViewById(R.id.tv_go_back).setOnClickListener(this);
            }
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_verify:
                // If code field is empty, notify user and do nothing
                if (etCode.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Please fill in all fields before verifying", Toast.LENGTH_SHORT).show();
                } else {

                    String community_code = etCode.getText().toString();
                    verifyCode(community_code);
                }
                break;
            case R.id.btn_sign_up:

                // Start registering user to Firebase
                fAuth.createUserWithEmailAndPassword(sentOverUser.getmEmail(), sentOverUser.getmPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Let user know that their account was successfully created
                            Toast.makeText(getContext(), "Account successfully created", Toast.LENGTH_SHORT).show();

                            String user_id = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();
                            String community_code = etCode.getText().toString();

                            // Push the new user to the database
                            pushNewUserFirestore(user_id, community_code, sentOverUser);

                        } else {
                            Toast.makeText(getContext(), "Error with creating account", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case R.id.tv_go_back:
                signUpListener.callSignUp();
                break;
        }
    }

    //Method to write a new user to the database
    private void pushNewUserFirestore(final String userId, final String communityCode, User currentUser){
        Map<String, String> user_data = new HashMap<>();
        user_data.put("email", (String) currentUser.getmEmail());
        user_data.put("full_name", (String) currentUser.getmUsername());
        user_data.put("password", (String) currentUser.getmPassword());
        user_data.put("community_id", communityCode);
        user_data.put("user_id", userId);

        db.collection("Resident_User").document()
                .set(user_data)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.i("TAG", "onComplete: " + "User pushed successfully");
                        }
                        if (task.isComplete()) {
                            signUpListener.callHome(userId, communityCode);
                            Log.i("TAG", "onComplete: " + "Go to home called");
                        }
                    }
                });
    }

    // Method verifies community code with database
    private void verifyCode(String communityCode) {

        final boolean[] codeExists = {false};
        db.collection("Communities").whereEqualTo("community_id", communityCode)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                codeExists[0] = true;
                                Log.i("TAG", "onComplete: " + "Code found");
                            }
                            if (!codeExists[0]) {
                                Toast.makeText(getContext(), "Code does not exist", Toast.LENGTH_SHORT).show();
                            }
                        }

                        if (task.isComplete() && codeExists[0]) {
                            Toast.makeText(getContext(), "Verification successful", Toast.LENGTH_SHORT).show();

                            Button btnSignUp = getView().findViewById(R.id.btn_sign_up);
                            Button btnVerify = getView().findViewById(R.id.btn_verify);

                            // Disable and configure Verify Button
                            btnVerify.setEnabled(false);
                            btnVerify.setBackgroundColor(getResources().getColor(R.color.colorDarkGrey));
                            btnVerify.setAlpha((float) 0.80);

                            // Enable and configure Sign Up Button
                            btnSignUp.setEnabled(true);
                            btnSignUp.setBackgroundColor(getResources().getColor(R.color.colorOrange));
                            btnSignUp.setAlpha((float) 0.80);
                        }
                    }
                });
    }
}
