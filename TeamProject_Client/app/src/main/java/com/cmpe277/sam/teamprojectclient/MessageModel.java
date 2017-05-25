package com.cmpe277.sam.teamprojectclient;

/**
 * Created by sam on 5/24/17.
 */

public class MessageModel {

    String fromScreenName;
    String toScreenName;
    String message;
    String time;
    String id;


    public String getFromScreenName() {
        return fromScreenName;
    }

    public void setFromScreenName(String fromScreenName) {
        this.fromScreenName = fromScreenName;
    }

    public String getToScreenName() {
        return toScreenName;
    }

    public void setToScreenName(String toScreenName) {
        this.toScreenName = toScreenName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setId(String id){this.id = id;}

    public String getId(){return id;}


}
