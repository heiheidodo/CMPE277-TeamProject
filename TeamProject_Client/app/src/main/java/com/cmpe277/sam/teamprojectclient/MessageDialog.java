package com.cmpe277.sam.teamprojectclient;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Window;

/**
 * Created by sam on 5/12/17.
 */

public class MessageDialog extends Dialog {

    private Activity activity;

    public MessageDialog(Activity activity) {
        super(activity);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_message);
        Window window = this.getWindow();
        this.setCancelable(true);

    }


}
