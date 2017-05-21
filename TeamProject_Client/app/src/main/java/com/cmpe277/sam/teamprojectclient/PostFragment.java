package com.cmpe277.sam.teamprojectclient;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by sam on 5/12/17.
 */

public class PostFragment extends Fragment implements AsyncResponse{

    Button btnUpload, btnPost, btnCancel;
    ImageView imgUploadView;
    static final int REQUEST_IMAGE_CHOOSE = 100;
    Bitmap imgUPload;
    EditText txtPost;
    UserInfo userInfo;

    public PostFragment() {
    }

    public static PostFragment newInstance(String text) {
        PostFragment postFragment = new PostFragment();
        return postFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post, container, false);
        userInfo = UserInfo.getInstance();

        btnUpload = (Button) view.findViewById(R.id.btnUpload);
        btnPost = (Button) view.findViewById(R.id.btnPost);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);
        txtPost = (EditText) view.findViewById(R.id.txtPost);

        imgUploadView = (ImageView) view.findViewById(R.id.imgUploadView);
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), REQUEST_IMAGE_CHOOSE);
            }
        });
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnWorker connWorker = new ConnWorker();
                connWorker.delegate = getAsyncResponse();
                connWorker.execute("post", "/post", "POST", userInfo.getEmail(), userInfo.getScreenName(), txtPost.getText().toString(), toBase64(imgUPload));
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtPost.setText("");
                final android.support.v4.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragmentContainer, new TimeLineFragment(), "jumpToTimeLine");
                ft.commit();
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_IMAGE_CHOOSE && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            Bitmap bitmap = null;

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                Matrix matrix = new Matrix();
                Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap , 0, 0, bitmap .getWidth(), bitmap .getHeight(), matrix, true);
                matrix.postRotate(180);
                imgUploadView.setImageBitmap(rotatedBitmap);
                imgUPload = rotatedBitmap;
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public String toBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    @Override
    public void getResponse(String str) {
        Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
        final android.support.v4.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentContainer, new TimeLineFragment(), "jumpToTimeLine");
        ft.commit();
    }

    @Override
    public void getJSONResponse(ArrayList array) {

    }


    public AsyncResponse getAsyncResponse(){
        return this;
    }
}
