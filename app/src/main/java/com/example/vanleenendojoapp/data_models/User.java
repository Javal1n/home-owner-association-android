package com.example.vanleenendojoapp.data_models;

import java.io.Serializable;

public class User implements Serializable {

    private String mUsername;
    private String mFullName;
    private String mEmail;
    private String mPassword;

    public User(){
        //Empty Constructor
    }

    public User(String _userName, String _email, String _password) {
        mUsername = _userName;
        mEmail = _email;
        mPassword = _password;
    }

    public User(String _userName, String _fullName, String _email, String _password) {
        mUsername = _userName;
        mFullName = _fullName;
        mEmail = _email;
        mPassword = _password;
    }

    public String getmUsername() {return mUsername;}
    public String getmFullName() {return mFullName;}
    public String getmEmail() {return mEmail;}
    public String getmPassword() {return mPassword;}
}
