package com.cmpe277.sam.teamprojectclient;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by sam on 5/12/17.
 */

public class ConnWorker extends AsyncTask<String, Integer, String> {

    HttpURLConnection conn;
    public static String basicUrl = "xxx";
    public AsyncResponse delegate = null;
    DataOutputStream wr;
    InputStreamReader isr;

    @Override
    protected String doInBackground(String... param) {
        try{

            if(param[0] == "signup"){
                URL url = new URL(basicUrl+param[1]);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF8");
                conn.setReadTimeout(10000000);
                conn.setConnectTimeout(15000000);
                conn.setRequestMethod(param[2]);
                conn.setDoInput(true);
                conn.setDoOutput(true);

                JSONObject req = new JSONObject();
                if(param[2]!=null)
                    req.put("email", param[3]);
                if(param[3]!=null)
                    req.put("password", param[4]);

                wr = new DataOutputStream(conn.getOutputStream());
                wr.writeBytes(req.toString());
                wr.flush();
                wr.close();

                InputStream responseStream = new BufferedInputStream(conn.getInputStream());
                BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(responseStream));
                String line = "";
                StringBuilder stringBuilder = new StringBuilder();
                while ((line = responseStreamReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                responseStreamReader.close();
                String response = stringBuilder.toString();
                JSONObject responseJson = new JSONObject(response);
                if((boolean)responseJson.get("dup") == true) return "duplicate user";
                else return "sign up success";

            }else if(param[0] == "login"){

                URL url = new URL(basicUrl+param[1]);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF8");
                conn.setReadTimeout(10000000);
                conn.setConnectTimeout(15000000);
                conn.setRequestMethod(param[2]);
                conn.setDoInput(true);
                conn.setDoOutput(true);

                JSONObject req = new JSONObject();
                if(param[3]!=null)
                    req.put("email", param[3]);
                if(param[4]!=null)
                    req.put("password", param[4]);

                wr = new DataOutputStream(conn.getOutputStream());
                wr.writeBytes(req.toString());
                wr.flush();
                wr.close();

                InputStream responseStream = new BufferedInputStream(conn.getInputStream());
                BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(responseStream));
                String line = "";
                StringBuilder stringBuilder = new StringBuilder();
                while ((line = responseStreamReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                responseStreamReader.close();
                String response = stringBuilder.toString();
                JSONObject responseJson = new JSONObject(response);
                if((boolean)responseJson.get("verified") == true) return "verify success";
                else return "verify fail";

            }


        }catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String str) {
        super.onPostExecute(str);
        delegate.getResponse(str);
    }
}
