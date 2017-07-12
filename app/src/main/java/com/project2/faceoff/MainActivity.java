package com.project2.faceoff;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.kakao.auth.Session;
import com.kakao.usermgmt.response.model.UserProfile;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {
    String nickname;
    String profileImageURL;
    String thumbnailURL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();

        nickname = intent.getStringExtra("nickname");
        profileImageURL = intent.getStringExtra("profileImageURL");
        if(profileImageURL != null){
            WebView wv = (WebView) findViewById(R.id.wv);
            WebSettings ws = wv.getSettings();
            wv.setInitialScale(90);
            ws.setDefaultFontSize(8);
            wv.getSettings().setUseWideViewPort(true);
            wv.loadData("<img src='"+ profileImageURL +"'>", "text/html","utf-8");
        }
        TextView textView = (TextView) findViewById(R.id.textView3);
        textView.setText("안녕하세요 "+nickname+"님! \n사용하실 사진을 골라주세요!");
    }


    public void withProfileImage(View v){
        Intent intent = new Intent(this, CelebImageActivity.class);
        intent.putExtra("nickname",nickname);
        intent.putExtra("url",profileImageURL);
        intent.putExtra("from","profile");
        startActivity(intent);
    }
    public void withGalleryImage(View v){
        Intent intent = new Intent(this, getGalleryImage.class);
        intent.putExtra("nickname",nickname);
        startActivity(intent);
    }
}
