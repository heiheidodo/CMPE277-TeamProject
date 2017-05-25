package com.cmpe277.sam.teamprojectclient;

import java.util.ArrayList;

/**
 * Created by sam on 5/18/17.
 */

public class UserInfo {
    String screenName;
    String email;
    String aboutMe;
    String location;
    String profession;
    String hobby;
    String visibility = "public";
    String portrait;
    String notification = "true";
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

    public String getAboutMe() {return this.aboutMe;}
    public void setAboutMe(String aboutMe) {this.aboutMe = aboutMe;}

    public String getLocation() {return this.location;}
    public void setLocation(String location) {this.location = location;}

    public String getHobby() {return this.hobby;}
    public void setHobby(String hobby) {this.hobby = hobby;}

    public String getVisibility() {return this.visibility;}
    public void setVisibility(String visibility) {this.visibility = visibility;}

    public String getProfession() {return this.profession;}
    public void setProfession(String profession) {this.profession = profession;}

    public String getPortrait() {return this.portrait;}
    public void setPortrait(String portrait) {this.portrait = portrait;}

    public String getNotification() {return this.notification;}
    public void setNotification(String notification) {this.notification = notification;}

    //public UserInfo(){}

    public static UserInfo getInstance(){
        if(instance == null){
            instance = new UserInfo();
        }
        return instance;
    }
}

