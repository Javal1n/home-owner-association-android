package com.example.vanleenendojoapp.data_models;

import java.io.Serializable;
import java.util.Date;

public class Post implements Serializable {

    private String mUsername;
    private String mSubject;
    private String mMessage;
    //private String mDatetime;

    public Post(){
        //Empty Constructor
    }

    public Post(String _subject, String _message, String _userName){

        mSubject = _subject;
        mMessage = _message;
        mUsername = _userName;
    }

    public String getmUsername() { return mUsername; }

    public String getmSubject() { return mSubject; }

    public String getmMessage() { return mMessage; }
}
