package com.project2.faceoff;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.kakao.auth.ErrorCode;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.helper.log.Logger;

import java.io.IOException;

public class kakaoSignupActivity extends AppCompatActivity {
    String myPN = "01010101010";

    String nickName;
    String profileImageURL ;
    String thumbnailURL ;
    String email;
    /**
     * Main으로 넘길지 가입 페이지를 그릴지 판단하기 위해 me를 호출한다.
     *
     */

    public void GetMyPN() {
        TelephonyManager telManager = (TelephonyManager) getApplicationContext().getSystemService(getApplicationContext().TELEPHONY_SERVICE);
        myPN = telManager.getLine1Number();
    }
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GetMyPN();
  //      Intent intent = getIntent();
  //      myPN = intent.getExtras().getString("PN");
        requestMe();
    }

    /**
     * 사용자의 상태를 알아 보기 위해 me API 호출을 한다.
     */
    protected void requestMe() { //유저의 정보를 받아오는 함수
        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                String message = "failed to get user info. msg=" + errorResult;
                Logger.d(message);

                ErrorCode result = ErrorCode.valueOf(errorResult.getErrorCode());
                if (result == ErrorCode.CLIENT_ERROR_CODE) {
                    finish();
                } else {
                    redirectLoginActivity();
                }
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                redirectLoginActivity();
            }

            @Override
            public void onNotSignedUp() {
            } // 카카오톡 회원이 아닐 시 showSignup(); 호출해야함

            @Override
            public void onSuccess(UserProfile userProfile) {  //성공 시 userProfile 형태로 반환
                Logger.d("UserProfile : " + userProfile);
                Log.d("UserProfile : ", userProfile.toString());
                nickName = userProfile.getNickname();
                profileImageURL = userProfile.getProfileImagePath();
                thumbnailURL = userProfile.getThumbnailImagePath();
                email = userProfile.getEmail();
                ContentValues contentvalues = new ContentValues();/*
                contentvalues.put("nickName", nickName);
                contentvalues.put("profileImageURL", profileImageURL);
                contentvalues.put("thumbnailURL", thumbnailURL);
                contentvalues.put("email", email);*/
                toServer(nickName,profileImageURL,thumbnailURL,email);
                redirectMainActivity(); // 로그인 성공시 MainActivity로
            }
        });
    }

    private void redirectMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("nickname",nickName);
        intent.putExtra("profileImageURL",profileImageURL);
        intent.putExtra("thumbnailURL",thumbnailURL);
        startActivity(intent);
        finish();
    }
    protected void redirectLoginActivity() {
        final Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }
    protected void toServer(String nickName, String profileImageURL, String thumbnailURL, String email) {
        // URL 설정.
        String url = "http://13.124.152.145:4000/users/";
        // AsyncTask를 통해 HttpURLConnection 수행.
        new NetworkTask().execute(url, nickName,profileImageURL,thumbnailURL,email);
    }
    class NetworkTask extends AsyncTask<String, Void, String> {

        private String url;
        private ContentValues values = new ContentValues();

        @Override
        protected String doInBackground(String... params) {
            url = params[0];
            for (int i=1; i<params.length; i++) {
                if (i == 1 && params[i] != null) values.put("nickname", params[1]);
                if (i == 2 && params[i] != null) values.put("profileImageURL", params[2]);
                if (i == 3 && params[i] != null) values.put("thumbnailURL", params[3]);
                if (i == 4 && params[i] != null) values.put("email", params[4]);
            }
            values.put("number",myPN);
            String result; // 요청 결과를 저장할 변수.
            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
            try {
                result = requestHttpURLConnection.request(url, values); // 해당 URL로 부터 결과물을 얻어온다.
                return result;
            }catch (IOException e) { // for openConnection().
                e.printStackTrace();
                return null;
            }
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
}