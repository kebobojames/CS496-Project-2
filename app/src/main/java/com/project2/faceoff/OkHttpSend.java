package com.project2.faceoff;

import android.net.http.Headers;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;

/**
 * Created by q on 2017-07-12.
 */

public class OkHttpSend {
        MediaType MEDIA_TYPE_JPG = MediaType.parse("image/jpg");
        OkHttpClient client = new OkHttpClient();

        public String run(String path) throws Exception {
            RequestBody requestBody = new MultipartBuilder()
                    .type(MultipartBuilder.FORM)
                    .addFormDataPart("myimage", "user.jpg", RequestBody.create(MEDIA_TYPE_JPG, new File(path)))
                    .build();

            Request request = new Request.Builder()
                    .url("http://13.124.152.145:4000/")
                    .post(requestBody)
                    .build();

            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            return response.body().string();
        }
}

