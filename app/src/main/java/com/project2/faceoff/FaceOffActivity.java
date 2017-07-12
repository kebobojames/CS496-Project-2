package com.project2.faceoff;

import android.content.ContentValues;
import android.content.Intent;
import android.net.NetworkRequest;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.google.gson.JsonParser;
import com.microsoft.projectoxford.face.FaceServiceClient;
import com.microsoft.projectoxford.face.FaceServiceRestClient;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.net.URLEncoder;

public class FaceOffActivity extends AppCompatActivity {
    public String url2 = "http://13.124.152.145:4000/uploads/%EC%86%A1%ED%98%9C%EA%B5%901.jpg";
    public String celebname;
    public String confidence;
    public String url1 = "http://13.124.152.145:4000/uploads/%EC%86%A1%EC%A4%91%EA%B8%B01.jpg";
    String nickname;
    private FaceServiceClient faceServiceClient =
            new FaceServiceRestClient("880258b6148441a5b2e6cec1a27d7125");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_off);
        //이미지 두개 url로 받을것 getintent로~
        Intent i = getIntent();
        url1 = i.getStringExtra("url1");
        nickname = i.getStringExtra("nickname");
        url2 = i.getStringExtra("url2");

        String [] front = url2.split("uploads/");
        celebname = front[1].split(".jpg")[0];
        try {
            url2 = front[0] +"uploads/" + URLEncoder.encode(front[1],"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        new NetworkTask().execute();
    }

    class NetworkTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            next();
        }

        @Override
        protected String doInBackground(Void... params) {
            //얼굴사진 하나를 facelist로 만들기
            CloseableHttpClient httpclient = HttpClients.createDefault();

                // 2. facelist에 url1 추가

                try {
                    URIBuilder builder4 = new URIBuilder("https://westcentralus.api.cognitive.microsoft.com/face/v1.0/detect");
                    URI uri4 = builder4.build();
                    HttpPost request4 = new HttpPost(uri4);
                    request4.setHeader("Content-Type", "application/json");
                    request4.setHeader("Ocp-Apim-Subscription-Key", "880258b6148441a5b2e6cec1a27d7125");

                    JSONObject Jo2 = new JSONObject();
                    try {
                        Jo2.put("url", url1);
                    } catch (JSONException e) {
                    }
                    // Request body
                    StringEntity reqEntity4 = new StringEntity(Jo2.toString());
                    request4.setEntity(reqEntity4);


                    HttpResponse response4 = httpclient.execute(request4);
                    HttpEntity entity4 = response4.getEntity();
                    String retsrcc = EntityUtils.toString(entity4);
                    JSONArray resultt = new JSONArray(retsrcc);
                    String faceId2 = resultt.getJSONObject(0).getString("faceId");
                    if (entity4 != null) {
                    }
                    try {

                        CloseableHttpClient httpclient2 = HttpClients.createDefault();
                        URIBuilder builder3 = new URIBuilder("https://westcentralus.api.cognitive.microsoft.com/face/v1.0/detect");

                        URI uri3 = builder3.build();
                        HttpPost request3 = new HttpPost(uri3);
                        request3.setHeader("Content-Type", "application/json");
                        request3.setHeader("Ocp-Apim-Subscription-Key", "880258b6148441a5b2e6cec1a27d7125");

                        JSONObject Jo3 = new JSONObject();
                        try {
                            Jo3.put("url", url2);
                        } catch (JSONException e) {
                        }

                        // Request body
                        StringEntity reqEntity3 = new StringEntity(Jo3.toString());
                        request3.setEntity(reqEntity3);

                        HttpResponse response3 = httpclient2.execute(request3);
                        HttpEntity entity3 = response3.getEntity();
                        String retsrc = EntityUtils.toString(entity3);
                        JSONArray result = new JSONArray(retsrc);
                        String faceId1 = result.getJSONObject(0).getString("faceId");
                        if (entity3 != null) {
                        }
                        //두개로 유사도 request보내기
                        CloseableHttpClient httpclient4 = HttpClients.createDefault();
                        try {
                            URIBuilder builder2 = new URIBuilder("https://westcentralus.api.cognitive.microsoft.com/face/v1.0/verify");


                            URI uri2 = builder2.build();
                            HttpPost request2 = new HttpPost(uri2);
                            request2.setHeader("Content-Type", "application/json");
                            request2.setHeader("Ocp-Apim-Subscription-Key", "880258b6148441a5b2e6cec1a27d7125");


                            JSONObject Jo4 = new JSONObject();
                            try {
                                Jo4.put("faceId1", faceId1);
                                Jo4.put("faceId2", faceId2);
                            } catch (JSONException e) {
                            }
                            // Request body
                            StringEntity reqEntity2 = new StringEntity(Jo4.toString());
                            request2.setEntity(reqEntity2);

                            HttpResponse response2 = httpclient4.execute(request2);
                            HttpEntity entity2 = response2.getEntity();

                            String retsrc2 = EntityUtils.toString(entity2);
                            JSONObject result2 = new JSONObject(retsrc2);
                            confidence = result2.getString("confidence");
                            if (entity2 != null) {
                            }
                            return confidence;
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            return null;
        }
    }

    void next(){
        // 다음 액티비티로 confidence보내주기~
        Intent intent = new Intent(this , ResultActivity.class);
        intent.putExtra("nickname",nickname);
        intent.putExtra("yourface",url1);
        intent.putExtra("celebface",url2);
        intent.putExtra("confidence",confidence);
        intent.putExtra("celebname",celebname);
        startActivity(intent);
        finish();
    }
}
