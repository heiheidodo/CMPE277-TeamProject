package com.cmpe277.sam.teamprojectclient;

import android.graphics.Bitmap;

/**
 * Created by sam on 5/19/17.
 */

public class TimeLineModel {

    private String screenName;
    private String email;
    private String text;
    private Bitmap pic;

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }
    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Bitmap getPic() {
        return pic;
    }

    public void setPic(Bitmap pic) {
        this.pic = pic;
    }


}
