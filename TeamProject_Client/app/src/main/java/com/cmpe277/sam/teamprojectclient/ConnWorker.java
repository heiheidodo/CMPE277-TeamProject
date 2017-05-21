package com.cmpe277.sam.teamprojectclient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by sam on 5/12/17.
 */

public class ConnWorker extends AsyncTask<String, Integer, String> {

    HttpURLConnection conn;
    public static String basicUrl = "http://54.219.190.254:3000";
    public AsyncResponse delegate = null;
    DataOutputStream wr;
    InputStreamReader isr;
    private ArrayList returnArr;
//    private ArrayList<PublicProfileModel> publicProfileArr;

    @Override
    protected String doInBackground(String... param) {
        try{

            if(param[0] == "signup"){
                URL url = new URL(basicUrl+param[1]);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/json"); // dont use utf-8 dont know why
                conn.setRequestProperty("Accept", "application/json"); // accept json response from server
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
                if(param[5]!=null)
                    req.put("screenName", param[5]);

                wr = new DataOutputStream(conn.getOutputStream());
                wr.writeBytes(req.toString());
                System.out.println(req.toString());
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
                System.out.println(conn.getResponseCode());
                if((boolean)responseJson.get("dup") == true) return "duplicate user";
                else return "sign up success";


            }else if(param[0] == "login"){

                URL url = new URL(basicUrl+param[1]);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
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
                if(param[5]!=null)
                    req.put("screenName", param[5]);
                if(param[6]!=null)
                    req.put("code", param[6]);

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

            }else if(param[0] == "post"){
                URL url = new URL(basicUrl+param[1]);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
                conn.setReadTimeout(10000000);
                conn.setConnectTimeout(15000000);
                conn.setRequestMethod(param[2]);
                conn.setDoInput(true);
                conn.setDoOutput(true);

                JSONObject req = new JSONObject();
                if(param[3]!=null)
                    req.put("email", param[3]);
                if(param[4]!=null)
                    req.put("screenName", param[4]);
                if(param[5]!=null)
                    req.put("text", param[5]);
                if(param[6]!=null)
                    req.put("pic", param[6]);

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
                if((boolean)responseJson.get("posted") == true) return "post success";
                else return "post fail";

            }else if(param[0] == "timeline"){

                URL url = new URL(basicUrl+param[1]);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
                conn.setReadTimeout(10000000);
                conn.setConnectTimeout(15000000);
                conn.setRequestMethod(param[2]);
                conn.setDoInput(true);
//                conn.setDoOutput(true);

                InputStream responseStream = new BufferedInputStream(conn.getInputStream());
                BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(responseStream));
                String line = "";
                StringBuilder stringBuilder = new StringBuilder();
                while ((line = responseStreamReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                responseStreamReader.close();
                String response = stringBuilder.toString();
                returnArr = new ArrayList<>();
                JSONArray responseJson = new JSONArray(response);
//                System.out.println(responseJson);
                for(int i = 0; i < responseJson.length(); i++){
                    TimeLineModel itemModel = new TimeLineModel();
                    JSONObject timelineItem = responseJson.getJSONObject(i);
                    itemModel.setScreenName(timelineItem.getString("screenName"));
                    itemModel.setText(timelineItem.getString("text"));
                    if(timelineItem.getString("pic") != ""){
                        byte[] decodedString = Base64.decode(timelineItem.getString("pic"), Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        itemModel.setPic(decodedByte);
                    }
                    returnArr.add(itemModel);
                }
                if(responseJson.length() != 0) return "post success";
                else return "post fail";
            }else if(param[0] == "publicUsers"){

                URL url = new URL(basicUrl+param[1]);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
                conn.setReadTimeout(10000000);
                conn.setConnectTimeout(15000000);
                conn.setRequestMethod(param[2]);
                conn.setDoInput(true);
//                conn.setDoOutput(true);

                InputStream responseStream = new BufferedInputStream(conn.getInputStream());
                BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(responseStream));
                String line = "";
                StringBuilder stringBuilder = new StringBuilder();
                while ((line = responseStreamReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                responseStreamReader.close();
                String response = stringBuilder.toString();
                returnArr = new ArrayList<>();
                JSONArray responseJson = new JSONArray(response);
//                System.out.println(responseJson);
                for(int i = 0; i < responseJson.length(); i++){
                    PublicProfileModel itemModel = new PublicProfileModel();
                    JSONObject timelineItem = responseJson.getJSONObject(i);
                    itemModel.setScreenName(timelineItem.getString("screenName"));
//                    if(timelineItem.getString("pic") != ""){
//                        byte[] decodedString = Base64.decode(timelineItem.getString("pic"), Base64.DEFAULT);
//                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//                        itemModel.setPic(decodedByte);
//                    }
                    returnArr.add(itemModel);
                }
                if(responseJson.length() != 0) return "post success";
                else return "post fail";
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
        delegate.getJSONResponse(returnArr);
    }

}
