package com.project2.faceoff;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;

public class ShowCelebrityActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_celebrity);
        Intent i = getIntent();
        String images = i.getStringExtra("images");
        final String url1 = i.getStringExtra("url1");
        final String nickname = i.getStringExtra("nickname");

        try {
            JSONArray j2 = new JSONArray(images);
            final GridView gv1 = (GridView) findViewById(R.id.gridview);
            GridViewAdapter gva = new GridViewAdapter();
            gv1.setAdapter(gva);
            for (int j=0; j<j2.length(); j++) {
                String path = j2.getJSONObject(j).getString("path");
                path = "http://13.124.152.145:4000/" + path;
                gva.addItem(path);
                gv1.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView parent, View v, int position, long id) {
                        // get item
                        GridViewItem item = (GridViewItem) parent.getItemAtPosition(position) ;

                        final String urlStr = item.getUrl();

                        //해보기
                        //do something with click
                        Intent i = new Intent(ShowCelebrityActivity.this, FaceOffActivity.class);
                        i.putExtra("url2", urlStr);
                        i.putExtra("nickname",nickname);
                        i.putExtra("url1",url1);
                        startActivity(i);
                        finish();
                    }
                });
            }
        }
        catch(JSONException e) {
            e.printStackTrace();
        }
    }
}
