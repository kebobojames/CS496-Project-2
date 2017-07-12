package com.project2.faceoff;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class CelebImageActivity extends AppCompatActivity {
    public String mImage;
    public String url;
    public String imgs;
    public String nickname;
    public String result;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_celeb_image);

        final Button button = (Button) findViewById(R.id.button1);
        final EditText search = (EditText) findViewById(R.id.plain_text_input);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                search.setFocusable(false);
                ListView lv1 = (ListView) findViewById(R.id.listview1);
                lv1.setAdapter(null);
                String name = search.getText().toString();
                search.setText("");
                toServer(name);
            }
        });
        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    button.requestFocus();
                    button.performClick();
                    return true;
                }
                return false;
            }
        });
        search.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                search.setFocusableInTouchMode(true);

                return false;
            }
        });
    }

    protected void toServer(String name) {
        // URL 설정.
        String url = "http://13.124.152.145:4000/celebrities/";
        url = url + name;
        // AsyncTask를 통해 HttpURLConnection 수행.
        Log.v("URL", url);
        new NetworkTask().execute(url);
    }

    class NetworkTask extends AsyncTask<String, Void, ArrayList<Pair<String, String>>> {
        @Override
        protected ArrayList<Pair<String,String>> doInBackground(String... params) {
            try {
                ArrayList<Pair<String,String>> pairs = new ArrayList<>();

                String searchURL = params[0];
                URL url = new URL(searchURL);
                HttpURLConnection search = (HttpURLConnection) url.openConnection();
                Log.v("Bloop", Integer.toString(search.getResponseCode()));
                search.setConnectTimeout(10000);
                search.setReadTimeout(10000);
                if (search.getResponseCode() != HttpURLConnection.HTTP_OK) return null;
                BufferedReader br = new BufferedReader(new InputStreamReader((search.getInputStream())));
                StringBuilder sb = new StringBuilder();
                String output;
                while((output = br.readLine()) != null) {
                    sb.append(output);
                }
                search.disconnect();

                JSONArray j = new JSONArray(sb.toString());
                for(int i = 0; i < j.length(); i++) {
                    String name = j.getJSONObject(i).getString("name");
                    url = new URL("http://13.124.152.145:4000/images/search/" + name);
                    search = (HttpURLConnection) url.openConnection();
                    Log.v("Bloop", Integer.toString(search.getResponseCode()));
                    if (search.getResponseCode() != HttpURLConnection.HTTP_OK) return null;
                    BufferedReader br2 = new BufferedReader(new InputStreamReader((search.getInputStream())));
                    StringBuilder sb2 = new StringBuilder();
                    String output2;
                    while ((output2 = br2.readLine()) != null) {
                        sb2.append(output2);
                    }
                    JSONArray j2 = new JSONArray(sb2.toString());
                    Pair<String,String> photos = Pair.create(sb2.toString(), name+" images");
                    String path = j2.getJSONObject(0).getString("path");
                    path = "http://13.124.152.145:4000/" + path;
                    search.disconnect();
                    Pair<String, String> result = Pair.create(path, name);
                    pairs.add(result);
                    pairs.add(photos);
                }
                return pairs;
            }
            catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }
            catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            TextView hello = (TextView) findViewById(R.id.text);
            //hello.setText("Loading...");
        }
        @Override
        protected void onPostExecute(final ArrayList<Pair<String, String>> pairs) {
            TextView hello = (TextView) findViewById(R.id.text);
            hello.setText("");

            final ListView lv1 = (ListView) findViewById(R.id.listview1);
            ListViewAdapter lva = new ListViewAdapter();
            lv1.setAdapter(lva);
            for (int i = 0; i < pairs.size() ; i=i+2) {
                lva.addItem(pairs.get(i).first, pairs.get(i).second);
            }
            lv1.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView parent, View v, int position, long id) {
                    // get item
                    ListViewItem item = (ListViewItem) parent.getItemAtPosition(position) ;

                    final String nameStr = item.getName() ;
                    final String urlStr = item.getUrl();

                    //해보기
                    //do something with click
                    Intent intent = getIntent();
                    nickname = intent.getStringExtra("nickname");
                    String from = intent.getStringExtra("from");
                    imgs = pairs.get(position*2 +1).first;
                    if(from.equals("profile")) //프사를 선택한 경우
                    {
                        Intent i = new Intent(CelebImageActivity.this,ShowCelebrityActivity.class);
                        i.putExtra("images", pairs.get(position*2 +1).first);
                        i.putExtra("nickname",nickname);
                        i.putExtra("url1",intent.getStringExtra("url"));
                        startActivity(i);
                    }else{ //갤러리 사진을 선택한 경우


                        String path = intent.getStringExtra("path");
                        /*
                        Intent i = new Intent(CelebImageActivity.this,ShowCelebrityActivity.class);
                        i.putExtra("images", imgs);
                        i.putExtra("nickname",nickname);
                        i.putExtra("url1", mImage);
                        startActivity(i);*/
                        new NetworkTask1().execute(path);
                   //     gotosendfile();
                    }
                }
            });
        }
    }

    class NetworkTask1 extends AsyncTask<String, Void, String> {

        private String url;
        private ContentValues values = new ContentValues();

        @Override
        protected String doInBackground(String... params) {
            OkHttpSend a = new OkHttpSend();
            try {
                result = a.run(params[0]);
                JSONArray ja = new JSONArray(result);
                result = ja.getJSONObject(0).getString("path");
                return result;
            }
            catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        @Override
        protected void onPostExecute(String s) {

            Intent i = new Intent(CelebImageActivity.this, ShowCelebrityActivity.class);
            i.putExtra("images", imgs);
            i.putExtra("nickname", nickname);
            i.putExtra("url1", "http://13.124.152.145:4000/"+s);
            startActivity(i);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case 0: {
                Intent i = new Intent(CelebImageActivity.this,ShowCelebrityActivity.class);
                i.putExtra("images", imgs);
                i.putExtra("nickname",nickname);
                url = data.getStringExtra("url");
                i.putExtra("url1", url);
                startActivity(i);
                break;
            }
        }
    }
}
