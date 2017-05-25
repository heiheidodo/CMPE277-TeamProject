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
    public static String basicUrl = "http://54.193.110.205:3000";
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
                    itemModel.setEmail(timelineItem.getString("email"));
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
                    if(responseJson.get(i) != null) {
                        JSONObject timelineItem = responseJson.getJSONObject(i);
                        itemModel.setScreenName(timelineItem.getString("screenName"));
                        itemModel.setEmail(timelineItem.getString("email"));
                    }
//                    if(timelineItem.getString("pic") != ""){
//                        byte[] decodedString = Base64.decode(timelineItem.getString("pic"), Base64.DEFAULT);
//                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//                        itemModel.setPic(decodedByte);
//                    }
                    returnArr.add(itemModel);
                }
                if(responseJson.length() != 0) return "post success";
                else return "post fail";
            }else if(param[0] == "addFriendRequest"){

                URL url = new URL(basicUrl+param[1]);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
                conn.setReadTimeout(10000000);
                conn.setConnectTimeout(15000000);
                conn.setRequestMethod(param[2]);
                conn.setDoInput(true);
                conn.setDoOutput(true);

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
                    itemModel.setEmail(timelineItem.getString("email"));
//                    if(timelineItem.getString("pic") != ""){
//                        byte[] decodedString = Base64.decode(timelineItem.getString("pic"), Base64.DEFAULT);
//                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//                        itemModel.setPic(decodedByte);
//                    }
                    returnArr.add(itemModel);
                }
                if(responseJson.length() != 0) return "post success";
                else return "post fail";

            }else if(param[0] == "getPublicPosts"){

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
                JSONObject responseJson = new JSONObject(response);
                System.out.println(responseJson);
                JSONObject userJson =  responseJson.getJSONObject("user");
                JSONArray postsArr =  userJson.getJSONArray("posts");
                Boolean canAdd = responseJson.getBoolean("canRequest");
                Boolean canFollow = responseJson.getBoolean("canFollow");
//                JSONArray pendingArr =  userJson.getJSONArray("pending");
//                System.out.println("pending arr from server: "+pendingArr);
//                for(int i = 0; i<pendingArr.length(); i++){
//                    JSONObject pendingItem = pendingArr.getJSONObject(i);
//                    UserInfo.getInstance().setPendingList(new ArrayList<String>());
//                    UserInfo.getInstance().getPendingList().add(pendingItem.getString("email"));
//                }
                for(int i = 0; i < postsArr.length(); i++){
                    TimeLineModel itemModel = new TimeLineModel();
                    JSONObject timelineItem = postsArr.getJSONObject(i);
                    itemModel.setScreenName(timelineItem.getString("screenName"));
                    itemModel.setEmail(timelineItem.getString("email"));
                    itemModel.setText(timelineItem.getString("text"));
                    if(timelineItem.getString("pic") != ""){
                        byte[] decodedString = Base64.decode(timelineItem.getString("pic"), Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        itemModel.setPic(decodedByte);
                    }
                    returnArr.add(itemModel);
                }
                if(responseJson.length() != 0) return canAdd.toString()+canFollow.toString();
                else return "fail to get the posts and add status";

            }else if(param[0] == "getPending"){

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

                for(int i = 0; i<responseJson.length(); i++){
                    JSONObject pendingItem = responseJson.getJSONObject(i);
                    returnArr.add(pendingItem.get("email"));
                }

                if(responseJson.length() != 0) return "get pending list success";
                else return "get pending list fail";

            }else if(param[0] == "getInMailList"){

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
//                System.out.println("message arr from server: "+responseJson);
                for(int i = 0; i < responseJson.length(); i++){
                    MessageModel itemModel = new MessageModel();
                    JSONObject msgItem = responseJson.getJSONObject(i);
                    itemModel.setFromScreenName(msgItem.getString("fromScreenName"));
                    itemModel.setToScreenName(msgItem.getString("toScreenName"));
                    itemModel.setMessage(msgItem.getString("message"));
                    itemModel.setTime(msgItem.getString("time"));
                    itemModel.setId(msgItem.getString("_id"));
                    returnArr.add(itemModel);
                }
                if(responseJson.length() != 0) return "get mail list success";
                else return "mail list get fail";

            }else if(param[0] == "sendMessage"){

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
                    req.put("fromEmail", param[3]);
                if(param[4]!=null)
                    req.put("fromScreenName", param[4]);
                if(param[5]!=null)
                    req.put("toEmail", param[5]);
                if(param[6]!=null)
                    req.put("toScreenName", param[6]);
                if(param[7]!=null)
                    req.put("subject", param[7]);
                if(param[8]!=null)
                    req.put("message", param[8]);

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
                if((boolean)responseJson.get("posted") == true) return "send success";
                else return "send fail";

            } else if (param[0] == "updateMe") {
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
                if (param[3] != null) {
                    req.put("aboutMe", param[3]);
                }
                if (param[4] != null) {
                    req.put("location", param[4]);
                }
                if (param[5] != null) {
                    req.put("profession", param[5]);
                }
                if (param[6] != null) {
                    req.put("hobby", param[6]);
                }
                if (param[7] != null) {
                    req.put("visibility", param[7]);
                }
                if (param[8] != null) {
                    req.put("portrait", param[8]);
                }
                if (param[9] != null) {
                    req.put("notification", param[9]);
                }
                req.put("email", UserInfo.getInstance().getEmail());
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
                System.out.println("***********************************************");
                System.out.println(response);
                //JSONObject responseJson = new JSONObject(response);

            }else if(param[0].equals("getMe")) {
                URL url = new URL(basicUrl + param[1]+"/"+UserInfo.getInstance().getEmail());
                conn = (HttpURLConnection)url.openConnection();
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
                conn.setReadTimeout(10000000);
                conn.setConnectTimeout(15000000);
                conn.setRequestMethod(param[2]);
                conn.setDoInput(true);

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
                JSONObject responseJson = new JSONObject(response);
                //JSONArray responseJson = new (response);
                System.out.println(responseJson);

                System.out.println("^^^^^^^^^^^^^^^^^");
                String aboutMe = "null";
                if(responseJson.has("aboutMe")) {
                    aboutMe = (String)responseJson.get("aboutMe");
                }
                String location = "null";
                if (responseJson.has("location")) {
                    location = (String)responseJson.get("location");
                }
                String hobby = "null";
                if (responseJson.has("hobby")) {
                    hobby = (String)responseJson.get("hobby");
                }
                String visibility = "public";
                if (responseJson.has("visibility")) {
                    visibility = (String)responseJson.get("visibility");
                }
                String name = (String)responseJson.get("screenName");
                String email = (String)responseJson.get("email");
                String profession = "null";
                if (responseJson.has("visibility")) {
                    profession = (String)responseJson.get("profession");
                }
                String portrait = (String)responseJson.get("portrait");
                String notification = "true";
                if (responseJson.has("notification")) {
                    notification = (String)responseJson.get("notification");
                }
                System.out.println("----------------->notificaion: " + notification);

                UserInfo user = UserInfo.getInstance();
                System.out.println("AboutMe: "+ aboutMe);
                user.setAboutMe(aboutMe);
                System.out.println("location: " + location);
                user.setLocation(location);
                System.out.println("hobby: " + hobby);
                user.setHobby(hobby);
                System.out.println("visibility: " + visibility);
                user.setVisibility(visibility);
                System.out.println("name: " + name);
                user.setScreenName(name);
                System.out.println("email: " + email);
                user.setEmail(email);
                System.out.println("profession: " + profession);
                user.setProfession(profession);
                //System.out.println(portrait);
                user.setPortrait(portrait);
                user.setNotification(notification);

                //SetInfo.myName.setText(responseJson.getString("screenName"));
                //itemModel.setScreenName(timelineItem.getString("screenName"));
                //itemModel.setText(timelineItem.getString("text"));
                //if(timelineItem.getString("pic") != ""){
                //    byte[] decodedString = Base64.decode(timelineItem.getString("pic"), Base64.DEFAULT);
                //    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                //    itemModel.setPic(decodedByte);

                //returnArr.add(itemModel);

            }else if(param[0] == "getMyPosts"){

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
                JSONObject responseJson = new JSONObject(response);
                JSONArray posts = responseJson.getJSONArray("posts");
//                System.out.println(responseJson);
                for(int i = 0; i < posts.length(); i++){
                    TimeLineModel itemModel = new TimeLineModel();
                    itemModel.setScreenName(posts.getJSONObject(i).getString("screenName"));
                    itemModel.setEmail(posts.getJSONObject(i).getString("email"));
                    itemModel.setText(posts.getJSONObject(i).getString("text"));
                    if(posts.getJSONObject(i).getString("pic") != ""){
                        byte[] decodedString = Base64.decode(posts.getJSONObject(i).getString("pic"), Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        itemModel.setPic(decodedByte);
                    }
                    returnArr.add(itemModel);
                }
                if(responseJson.length() != 0) return "post success";
                else return "post fail";
            }else if(param[0] == "deleteMail"){

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
                if(response.length() != 0) return "delete success";
                else return "delete fail";
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
