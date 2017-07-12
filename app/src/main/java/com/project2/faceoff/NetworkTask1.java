package com.project2.faceoff;

import android.os.AsyncTask;
import android.support.v4.util.Pair;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by q on 2017-07-12.
 */

class NetworkTask1 extends AsyncTask<String, Void, ArrayList<Pair<String, String>>> {
    @Override
    protected ArrayList<Pair<String,String>> doInBackground(String... params) {
        try{


            MultipartUtility multipart = new MultipartUtility("http://13.124.152.145:4000/", "UTF-8");

            //add your file here.
            /*This is to add file content*//*
    for (int i = 0; i < myFileArray.size(); i++) {
        multipart.addFilePart(myFileArray.getParamName(),
                new File(myFileArray.getFileName()));
    }*/

            File f = new File(params[0]);
            multipart.addFilePart("myimage", f);

            List<String> response = multipart.finish();
            Log.v("asdf", "SERVER REPLIED:");
            for (String line : response) {
                Log.v("asdf", "Upload Files Response:::" + line);
                // get your server response here.
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPreExecute() {

    }
    @Override
    protected void onPostExecute(final ArrayList<Pair<String, String>> pairs) {

    }
}