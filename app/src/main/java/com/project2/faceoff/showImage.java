package com.project2.faceoff;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.io.IOException;

public class showImage extends AppCompatActivity {

    private ImageView iv_UserPhoto;
    private Uri mImageCaptureUri;
    private String nickname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);
        Intent intentt = getIntent();

        mImageCaptureUri = Uri.parse(intentt.getStringExtra("mImageCaptureUri"));
        iv_UserPhoto = (ImageView) this.findViewById(R.id.userImage);
        try {
            Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), mImageCaptureUri);
            iv_UserPhoto.setImageBitmap(bm);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void gogo(View v){
        Intent intent = new Intent(this, CelebImageActivity.class);
        intent.putExtra("mImageCaptureUri",mImageCaptureUri.toString());
        Intent intentt = getIntent();
        nickname = intentt.getStringExtra("nickname");
        intent.putExtra("nickname",nickname);
        startActivity(intent);
    }
}
