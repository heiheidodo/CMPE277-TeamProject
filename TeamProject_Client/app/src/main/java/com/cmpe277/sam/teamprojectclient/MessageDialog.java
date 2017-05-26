package com.cmpe277.sam.teamprojectclient;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

/**
 * Created by sam on 5/12/17.
 */

public class MessageDialog extends Dialog implements AsyncResponse{

    private Activity activity;
    Button btnSend;
    String toEmail, toScreenName;
    EditText etToScreenName, etSubject, etContent;


    public MessageDialog(Activity activity, String toEmail, String toScreenName) {
        super(activity);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.activity = activity;
        this.toEmail = toEmail;
        this.toScreenName = toScreenName;
    }

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
        etSubject = (EditText) findViewById(R.id.etSubject);
        etContent = (EditText) findViewById(R.id.etContent);
        btnSend = (Button) findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnWorker connWorker = new ConnWorker();
                connWorker.delegate = getAsyncResponse();
                connWorker.execute("sendMessage", "/inMail", "POST", UserInfo.getInstance().getEmail(), UserInfo.getInstance().getScreenName(), toEmail, toScreenName, etSubject.getText().toString(), etContent.getText().toString());
                dismiss();
            }
        });
    }


    @Override
    public void getResponse(String str) {

    }

    @Override
    public void getJSONResponse(ArrayList array) {

    }

    public AsyncResponse getAsyncResponse(){
        return this;
    }
}
