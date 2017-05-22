package com.cmpe277.sam.teamprojectclient;

import java.util.ArrayList;

/**
 * Created by sam on 5/18/17.
 */

public class UserInfo {

    String screenName;
    String email = "xiaoshan1213@gmail.com";

    public ArrayList<String> getPendingList() {
        return pendingList;
    }

    public void setPendingList(ArrayList<String> pendingList) {
        this.pendingList = pendingList;
    }

    ArrayList<String> pendingList;

    private static UserInfo instance;

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserInfo(){}

    public static UserInfo getInstance(){
        if(instance == null){
            instance = new UserInfo();

        }
        return instance;
    }
}
