package com.project2.faceoff;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;

public class getGalleryImage extends Activity {
    private ImageView iv_UserPhoto;
    private Uri mImageCaptureUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_gallery_image);
        iv_UserPhoto = (ImageView) this.findViewById(R.id.userImage);
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, 0);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode!=RESULT_OK){
            return;
        }
        switch (requestCode)
        {
            case 0 : {
                mImageCaptureUri = data.getData();
                Log.d("imageuri",mImageCaptureUri.getPath().toString());
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(mImageCaptureUri,"image/*");
                intent.putExtra("outputX",300);
                intent.putExtra("outputY",300);
                intent.putExtra("aspectX",1);
                intent.putExtra("aspectY",1);
                intent.putExtra("scale",true);
                intent.putExtra("output",mImageCaptureUri+"1");
                intent.putExtra("return-data",true);
                startActivityForResult(intent,1);
                break;
            }
            case 1 :{/*
                final Bundle extras = data.getExtras();
                Bitmap photo = extras.getParcelable("data");
                iv_UserPhoto.setImageBitmap(photo);*/
                try {
                    Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(),Uri.parse(mImageCaptureUri.toString()+"1"));
                    iv_UserPhoto.setImageBitmap(bm);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

    }
}
