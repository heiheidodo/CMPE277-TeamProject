package com.cmpe277.sam.teamprojectclient;

import android.graphics.Bitmap;

/**
 * Created by sam on 5/20/17.
 */

public class PublicProfileModel {

    private String screenName;
    private String email;
    private Bitmap selfPic;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public Bitmap getSelfPic() {
        return selfPic;
    }

    public void setSelfPic(Bitmap selfPic) {
        this.selfPic = selfPic;
    }


}
