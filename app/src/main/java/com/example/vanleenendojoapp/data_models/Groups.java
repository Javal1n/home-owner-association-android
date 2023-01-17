package com.example.vanleenendojoapp.data_models;

import java.io.Serializable;
import java.util.ArrayList;

public class Groups implements Serializable {

    private String mGroupName;
    private ArrayList<String> mMembers;

    public Groups() {
        //Empty Constructor
    }

    public Groups(String _groupName, ArrayList<String> _members){

        mGroupName = _groupName;
        mMembers = _members;
    }

    public String getmGroupName() {
        return mGroupName;
    }

    public ArrayList<String> getmMembers() {
        return mMembers;
    }
}
