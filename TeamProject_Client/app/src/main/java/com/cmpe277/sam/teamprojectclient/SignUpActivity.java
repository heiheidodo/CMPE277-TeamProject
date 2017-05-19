package com.cmpe277.sam.teamprojectclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity implements AsyncResponse{

    TextView txtEmail, txtPwd, txtVerification, txtName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        txtName = (TextView) findViewById(R.id.txtName);
        txtEmail = (TextView) findViewById(R.id.txtEmail);
        txtPwd = (TextView) findViewById(R.id.txtPwd);
        txtVerification = (TextView) findViewById(R.id.txtVerification);

        Button btnSignup = (Button) findViewById(R.id.btnSignup);
        Button btnLogin = (Button) findViewById(R.id.btnLogin);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String  name = txtName.getText().toString();
                String email = txtEmail.getText().toString();
                String pwd = txtPwd.getText().toString();

                if(!isValidEmailAddress(email)){
                    Toast.makeText(SignUpActivity.this, "email invalid", Toast.LENGTH_SHORT).show();
                    return ;
                }
                ConnWorker connWorker = new ConnWorker();
                connWorker.delegate = getAsyncResponse();
                connWorker.execute("signup", "/user", "POST", email, pwd, name);

            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                test
//                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                String email = txtEmail.getText().toString();
                String pwd = txtPwd.getText().toString();
                String code = txtVerification.getText().toString();
                String  name = txtName.getText().toString();
                UserInfo userInfo = UserInfo.getInstance();
                userInfo.setScreenName(name);
                userInfo.setEmail(email);
                if(!isValidEmailAddress(email)){
                    Toast.makeText(SignUpActivity.this, "email invalid", Toast.LENGTH_SHORT).show();
                    return ;
                }
                if(pwd.equals("")){
                    Toast.makeText(SignUpActivity.this, "requirement empty", Toast.LENGTH_SHORT).show();
                    return ;
                }
                ConnWorker connWorker = new ConnWorker();
                connWorker.delegate = getAsyncResponse();

                if(code.equals("")){
                    connWorker.execute("login", "/user/signIn", "POST", email, pwd, null, null);
                }else{
                    connWorker.execute("login", "/user/verify", "PUT", email, pwd, name, code);
                }

            }
        });
    }

    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    @Override
    public void getResponse(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
        if(str == "verify success")
            startActivity(new Intent(SignUpActivity.this, MainActivity.class));
    }

    public AsyncResponse getAsyncResponse(){
        return this;
    }
}
