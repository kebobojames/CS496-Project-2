package com.project2.faceoff;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import java.io.IOException;

public class getGalleryImage extends Activity {
    private ImageView iv_UserPhoto;
    private Uri mImageCaptureUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_gallery_image);
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, 0);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case 0: {
                mImageCaptureUri = data.getData();

                Log.d("imageuri", mImageCaptureUri.getPath().toString());
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(mImageCaptureUri, "image/*");
                intent.putExtra("outputX", 300);
                intent.putExtra("outputY", 300);
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("scale", true);
                intent.putExtra("output", mImageCaptureUri);
                intent.putExtra("return-data", true);
                startActivityForResult(intent, 1);
                break;
            }
            case 1: {
                Intent intent = new Intent(this, showImage.class);
                Intent intentt = getIntent();
                String nickname = intentt.getStringExtra("nickname");
                intent.putExtra("nickname",nickname);
                String[] proj = { MediaStore.Images.Media.DATA };
                Cursor cursor = getContentResolver().query(mImageCaptureUri, proj, null, null, null);
                int column_index = cursor
                        .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                String path = cursor.getString(column_index);
                cursor.close();
                Log.v("PLEASEEEEEEEEEEEEEEEE", path);
                intent.putExtra("path", path);
                intent.putExtra("mImageCaptureUri", mImageCaptureUri.toString());
                startActivity(intent);
                //finish();
            }
        }
    }
    public void fin(View v){
        finish();
    }
}
