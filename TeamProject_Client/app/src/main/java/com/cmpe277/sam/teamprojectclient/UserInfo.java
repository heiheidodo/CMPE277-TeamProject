package com.cmpe277.sam.teamprojectclient;

/**
 * Created by sam on 5/18/17.
 */

public class UserInfo {

    String screenName;
    String email;
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
