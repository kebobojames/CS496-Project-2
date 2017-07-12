package com.project2.faceoff;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ShowRank extends AppCompatActivity {
    ArrayList<String> numbers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_rank);
        final Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
        String _ID = ContactsContract.Contacts._ID;
        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;
        Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
        String phoneNumber;
        ContentResolver contentResolver = getContentResolver();
        //String sortOrder = DISPLAY_NAME + " COLLATE LOCALIZED ASC";
        Cursor cursor = contentResolver.query(CONTENT_URI, null, null, null, /*sortOrder*/null);
        // Iterate every contact in the phone
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String contact_id = cursor.getString(cursor.getColumnIndex(_ID));
                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(HAS_PHONE_NUMBER)));
                if (hasPhoneNumber > 0) {
                    //This is to read multiple phone numbers associated with the same contact
                    Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[]{contact_id}, null);
                    while (phoneCursor.moveToNext()) {
                        phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                        numbers.add(phoneNumber.replaceAll("\\D",""));
                    }
                    phoneCursor.close();
                }
            }
        }
        cursor.close();
        TextView tv = (TextView) findViewById(R.id.textView6);
        tv.setText("Loading..");
        new NetworkTask().execute("http://13.124.152.145:4000/scores");

    }

    public static Comparator<Pair<String, Pair<String, String>>> getComparator() {
        return new Comparator<Pair<String, Pair<String, String>>>() {
            @Override
            public int compare(Pair<String, Pair<String, String>> p1, Pair < String, Pair < String, String >> p2)
            {
                return -1 * Integer.valueOf(Integer.parseInt(p1.second.second)).compareTo(Integer.parseInt(p2.second.second));
            }
        };
    }

    class NetworkTask extends AsyncTask<String, Void, ArrayList<ListViewItem2>> {

        @Override
        protected ArrayList<ListViewItem2> doInBackground(String... params) {
            try {

                ArrayList<Pair<String, Pair<String, String>>> scores = new ArrayList<>();


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

                for(int i = 0; i<j.length(); i++) {
                    String number = j.getJSONObject(i).getString("number");
                    if (numbers.contains(number)) {
                        String celebrity = j.getJSONObject(i).getString("celebrityName");
                        String score = j.getJSONObject(i).getString("score");
                        Pair<String, String> info = Pair.create(celebrity, score);
                        Pair<String, Pair<String, String>> person = Pair.create(number, info);
                        scores.add(person);
                    }
                }

                //Sorting
                Collections.sort(scores, getComparator());

                ArrayList<ListViewItem2> items = new ArrayList<>();

                for (int i = 0; i< scores.size(); i++) {
                    if(i==10) {
                        break;
                    }
                    String searchURL1 = "http://13.124.152.145:4000/users/search/" + scores.get(i).first;
                    URL url1 = new URL(searchURL1);
                    HttpURLConnection search1 = (HttpURLConnection) url1.openConnection();
                    search1.setConnectTimeout(10000);
                    search1.setReadTimeout(10000);
                    if (search1.getResponseCode() != HttpURLConnection.HTTP_OK) return null;
                    BufferedReader br1 = new BufferedReader(new InputStreamReader((search1.getInputStream())));
                    StringBuilder sb1 = new StringBuilder();
                    String output1;
                    while((output1 = br1.readLine()) != null) {
                        sb1.append(output1);
                    }
                    search1.disconnect();
                    JSONObject person = new JSONObject(sb1.toString());
                    ListViewItem2 item = new ListViewItem2();
                    Log.v("ASDf",person.getString("profileImageURL"));
                    item.setUrl(person.getString("profileImageURL"));
                    item.setName(person.getString("nickname"));
                    item.setCelebrity(scores.get(i).second.first);
                    Log.v("asdf",item.getCelebrity());
                    item.setScore(scores.get(i).second.second);
                    items.add(item);
                }
                return items;

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
            super.onPreExecute();
        }
        @Override
        protected void onPostExecute(ArrayList<ListViewItem2> items) {
            ListView lv1 = (ListView) findViewById(R.id.listview3);
            TextView tv = (TextView) findViewById(R.id.textView6);
            tv.setText("Friend Leaderboard");
            ListViewAdapter2 lva = new ListViewAdapter2();
            lv1.setAdapter(lva);
            if (items != null) {
                for (ListViewItem2 i : items) {
                    String url = i.getUrl();
                    String name = i.getName();
                    String celeb = i.getCelebrity();
                    String score = i.getScore();
                    lva.addItem(url, name, celeb, score);
                }
            }
            else {
                tv.setText("Zero items...");
            }
        }
    }
}
