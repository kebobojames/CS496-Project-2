package com.project2.faceoff;

import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.kakao.kakaolink.KakaoLink;
import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder;

import java.io.IOException;

public class ResultActivity extends AppCompatActivity {
    String nickname;
    String yourfaceurl;
    String celebfaceurl ;
    String score;
    String myPN;
    String namee;

    String url = "http://13.124.152.145:4000/scores/";
    ContentValues values = new ContentValues();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Intent intent = getIntent();
        nickname = intent.getStringExtra("nickname");
        yourfaceurl = intent.getStringExtra("yourface");
        celebfaceurl = intent.getStringExtra("celebface");
        score = intent.getStringExtra("confidence");
        score = Integer.toString((int)(Float.parseFloat(score) * 100));
        namee =  intent.getStringExtra("celebname");
        namee = namee.substring(0,namee.length()-1);

        WebView wv2 = (WebView) findViewById(R.id.celebfacee);
        wv2.getSettings().setLoadWithOverviewMode(true);
        wv2.getSettings().setUseWideViewPort(true);
        wv2.loadUrl(celebfaceurl);

        WebView wv = (WebView) findViewById(R.id.yourfacee);
        WebSettings ws = wv.getSettings();
        wv.getSettings().setLoadWithOverviewMode(true);
        wv.getSettings().setUseWideViewPort(true);
        //wv.setInitialScale(90);
        //ws.setDefaultFontSize(8);
        wv.loadUrl(yourfaceurl);
        TextView textView = (TextView) findViewById(R.id.textView5);
        textView.setText(nickname+"님의 점수는 " + score +"점 입니다!");
        GetMyPN();
        new sendscore().execute();
    }
    public void retry(View v){
        finish();
    }
    public void ranking(View v){
        Intent intent = new Intent(this, ShowRank.class);
        startActivity(intent);
    }
    public void GetMyPN() {
        TelephonyManager telManager = (TelephonyManager) getApplicationContext().getSystemService(getApplicationContext().TELEPHONY_SERVICE);
        myPN = telManager.getLine1Number();
    }
    class sendscore extends AsyncTask<Void , Void, String>  {

        @Override
        protected String doInBackground(Void... params) {
            values.put("number",myPN);
            values.put("score",score);
            values.put("celebrityName",namee);
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
    public void sendKatalk(View v) {
        try{
            final KakaoLink kakaoLink = KakaoLink.getKakaoLink(this);
            final KakaoTalkLinkMessageBuilder kakaoBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();
            String text = nickname + "의 점수를 확인하세요!";
            kakaoBuilder.addText(text);
            String url = celebfaceurl;
            kakaoBuilder.addImage(url,160,160);
            kakaoBuilder.addAppButton("앱 실행 혹은 다운로드");
            kakaoLink.sendMessage(kakaoBuilder,this);

        }catch (Exception e){
        }
    }
}
