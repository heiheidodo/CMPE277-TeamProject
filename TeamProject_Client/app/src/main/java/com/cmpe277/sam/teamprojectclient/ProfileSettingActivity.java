package com.cmpe277.sam.teamprojectclient;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Created by sam on 5/24/17.
 */
public class ProfileSettingActivity extends Activity implements AsyncResponse{

    ImageView myPortrait;
    TextView myName;
    TextView aboutMe;
    TextView myEmail;
    private View myLocation;
    TextView myLocationText;
    private View myProfession;
    TextView myProfessionText;
    private View myHobby;
    TextView myHobbyText;
    Switch visibiliy;
    Switch notification;


    public final static int SMALL_CAPTURE = 0;
    public final static int PICK_IMAGE = 2;
    private String selectedImagePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_setting);

        //NetworkAsyncTask connWorker = new NetworkAsyncTask();
        //connWorker.execute("getMe", "/user", "GET");

        UserInfo user = UserInfo.getInstance();

        myPortrait = (ImageView) findViewById(R.id.profile_portrait);
        myPortrait.setOnClickListener(new ChangePortrait());
        myPortrait.setImageBitmap(getImage());
        myName = (TextView)findViewById(R.id.profile_myName);
        myName.setText(user.getScreenName());
        myName.setOnClickListener(new ChangeMyName());
        aboutMe = (TextView)findViewById(R.id.profile_aboutMe);
        aboutMe.setText(user.getAboutMe());
        aboutMe.setOnClickListener(new ChangeAboutMe());
        myEmail = (TextView)findViewById(R.id.profile_email_text);
        myEmail.setText(user.getEmail());
        myEmail.setOnClickListener(new ChangeEmail());
        myLocation = findViewById(R.id.profile_location);
        myLocation.setOnClickListener(new ChangeLocation());
        myLocationText = (TextView) findViewById(R.id.profile_location_text);
        myLocationText.setText(user.getLocation());
        myProfession = findViewById(R.id.profile_profession);
        myProfession.setOnClickListener(new ChangeProfession());
        myProfessionText = (TextView)findViewById(R.id.profile_profession_text);
        myProfessionText.setText(user.getProfession());
        myHobby = findViewById(R.id.profile_hobby);
        myHobby.setOnClickListener(new ChangeHobby());
        myHobbyText = (TextView)findViewById(R.id.profile_hobby_text);
        myHobbyText.setText(user.getHobby());
        visibiliy = (Switch)findViewById(R.id.visibility);
        visibiliy.setOnCheckedChangeListener(new ChangeVisibility());
        if (user.getVisibility().equals("public")) {
            visibiliy.setChecked(true);
        } else {
            visibiliy.setChecked(false);
        }
        notification = (Switch)findViewById(R.id.profile_notification);
        notification.setOnCheckedChangeListener(new ChangeNotification());
        if (user.getNotification().equals("true")) {
            visibiliy.setChecked(true);
        } else {
            visibiliy.setChecked(false);
        }






    }

    public static Bitmap getImage() {
        UserInfo user = UserInfo.getInstance();
        String image = user.getPortrait();
        if (image == null || image.equals("default")) {
            //默认头像设置？
        }
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(image, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
            return bitmap;
        } catch (Exception e) {
            return null;
        }
    }


    class ChangePortrait implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            final AlertDialog.Builder dlg= new AlertDialog.Builder(ProfileSettingActivity.this);
            final AlertDialog alterDialog = dlg.create();
            dlg.setTitle("Portrait Set Way: ");
            dlg.setPositiveButton("From Camera", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(intent, SMALL_CAPTURE);
                    }
                }
            });
            dlg.setNegativeButton("Cancel", null);
            dlg.setNeutralButton("From Gallery", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, PICK_IMAGE);
                }
            });
            dlg.show();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("----------------------->data back");
        if (requestCode == SMALL_CAPTURE && resultCode == RESULT_OK) {
            System.out.println("------------------------>data trying to set");
            Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
            myPortrait.setImageBitmap(imageBitmap);

        }

        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            System.out.println("------------------------>data trying to set");

            if(null!=data && null!=data.getData()){
                System.out.println(data.getData());
                selectedImagePath = getAbsolutePath(data.getData());
                Bitmap portrait = decodeFile(selectedImagePath);
                myPortrait.setImageBitmap(portrait);

                ConnWorker connWorker = new ConnWorker();
                System.out.println("----------------> connWork initiated");
                connWorker.execute("updateMe", "/user", "PUT", null, null, null,null,null, toBase64(portrait),null);


            }
        }
    }

    public String getAbsolutePath(Uri uri) {
        String[] projection = { MediaStore.MediaColumns.DATA };
        @SuppressWarnings("deprecation")
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }

    public Bitmap decodeFile(String path) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, o);
            // The new size we want to scale to
            final int REQUIRED_SIZE = 70;
            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE)
                scale *= 2;
            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeFile(path, o2);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    public String toBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }


    class ChangeAboutMe implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            System.out.println("------------> Change about me");
            final AlertDialog.Builder dlg= new AlertDialog.Builder(ProfileSettingActivity.this);
            final AlertDialog alterDialog = dlg.create();
            dlg.setTitle("new brief introduction: ");
            final EditText input = new EditText(ProfileSettingActivity.this);
            dlg.setView(input);
            dlg.setPositiveButton("Submit", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String str = input.getText().toString();
                    aboutMe.setText(str);
                    ConnWorker connWorker = new ConnWorker();
                    System.out.println("----------------> connWork initiated");
                    connWorker.execute("updateMe", "/user", "PUT", str, null, null,null,null,null,null);

                }
            });
            dlg.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            dlg.show();
        }
    }

    class ChangeMyName implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            ConnWorker connWorker = new ConnWorker();
            connWorker.execute("getMe", "/user", "GET");
            /*
            System.out.println("------------> name");
            final AlertDialog.Builder dlg= new AlertDialog.Builder(ProfileSettingActivity.this);
            final AlertDialog alterDialog = dlg.create();
            dlg.setTitle("new name: ");
            final EditText input = new EditText(ProfileSettingActivity.this);
            dlg.setView(input);
            dlg.setPositiveButton("Submit", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String str = input.getText().toString();
                    myName.setText(str);
                    Toast.makeText(getApplicationContext(), str,
                            Toast.LENGTH_SHORT).show();
                }
            });
            dlg.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            dlg.show();
            */

        }
    }

    class ChangeEmail implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String url = "http://localhost:8080/project/rest/server/clientInfo_get";

        }
    }

    class ChangeLocation implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            System.out.println("------------> Change location");
            final AlertDialog.Builder dlg= new AlertDialog.Builder(ProfileSettingActivity.this);
            final AlertDialog alterDialog = dlg.create();
            dlg.setTitle("New Location: ");
            final EditText input = new EditText(ProfileSettingActivity.this);
            dlg.setView(input);
            dlg.setPositiveButton("Submit", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String str = input.getText().toString();
                    myLocationText.setText(str);
                    ConnWorker connWorker = new ConnWorker();
                    System.out.println("----------------> connWork initiated");
                    connWorker.execute("updateMe", "/user", "PUT", null, str, null,null,null, null,null);

                }
            });
            dlg.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            dlg.show();
        }
    }

    class ChangeProfession implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            System.out.println("------------> Change profession");
            final AlertDialog.Builder dlg= new AlertDialog.Builder(ProfileSettingActivity.this);
            final AlertDialog alterDialog = dlg.create();
            dlg.setTitle("New Profession: ");
            final EditText input = new EditText(ProfileSettingActivity.this);
            dlg.setView(input);
            dlg.setPositiveButton("Submit", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String str = input.getText().toString();
                    myProfessionText.setText(str);
                    ConnWorker connWorker = new ConnWorker();
                    System.out.println("----------------> connWork initiated");
                    connWorker.execute("updateMe", "/user", "PUT", null, null, str,null,null, null,null);

                }
            });
            dlg.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            dlg.show();


        }
    }
    class ChangeHobby implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            System.out.println("------------> Change Hobby");
            final AlertDialog.Builder dlg= new AlertDialog.Builder(ProfileSettingActivity.this);
            final AlertDialog alterDialog = dlg.create();
            dlg.setTitle("New Interests: ");
            final EditText input = new EditText(ProfileSettingActivity.this);
            dlg.setView(input);
            dlg.setPositiveButton("Submit", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String str = input.getText().toString();
                    myHobbyText.setText(str);
                    ConnWorker connWorker = new ConnWorker();
                    System.out.println("----------------> connWork initiated");
                    connWorker.execute("updateMe", "/user", "PUT", null, null, null,str,null, null,null);

                }
            });
            dlg.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            dlg.show();
        }
    }

    class ChangeVisibility implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                System.out.println("&&&&&&&&&&&&&&&&&&&&& set public!");
                ConnWorker connWorker = new ConnWorker();
                System.out.println("----------------> connWork initiated");
                connWorker.execute("updateMe", "/user", "PUT", null, null, null,null,"public", null,null);

            } else {
                System.out.println("&&&&&&&&&&&&&&&&&&&&& set private!");
                ConnWorker connWorker = new ConnWorker();
                System.out.println("----------------> connWork initiated");
                connWorker.execute("updateMe", "/user", "PUT", null, null, null,null,"private", null,null);
            }
        }
    }

    class ChangeNotification implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                System.out.println("&&&&&&&&&&&&&&&&&&&&& notification!");
                ConnWorker connWorker = new ConnWorker();
                System.out.println("----------------> connWork initiated");
                connWorker.execute("updateMe", "/user", "PUT", null, null, null, null, null, null, "true");

            } else {
                System.out.println("&&&&&&&&&&&&&&&&&&&&& no notification!");
                ConnWorker connWorker = new ConnWorker();
                System.out.println("----------------> connWork initiated");
                connWorker.execute("updateMe", "/user", "PUT", null, null, null, null, null, null, "false");
            }
        }
    }

    @Override
    public void getResponse(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void getJSONResponse(ArrayList array) {

    }
    public AsyncResponse getAsyncResponse(){
        return this;
    }

}
