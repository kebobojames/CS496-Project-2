package com.project2.faceoff;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.kakao.kakaolink.KakaoLink;
import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder;

public class Sendmessage extends AppCompatActivity {

    final String text = "Hello it's me";
    final String Image = "연예인 사진 url 넣기";
    final String stringUrl = "다운로드 링크 넣기";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendmessage);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == RESULT_OK){
            Cursor cursor = getContentResolver().query(data.getData(),
                    new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER}, null, null, null);
            cursor.moveToFirst();
            sendMessageTo(cursor.getString(1),text);
        }
    }
    public void sendSMS(View v) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(intent,0);
    }
    public void sendKatalk(View v) {
        try{
            final KakaoLink kakaoLink = KakaoLink.getKakaoLink(this);
            final KakaoTalkLinkMessageBuilder kakaoBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();
            kakaoBuilder.addText(text);
           // String url = Image;
           // kakaoBuilder.addImage(url,160,160);
            kakaoBuilder.addAppButton("앱 실행 혹은 다운로드");
            kakaoLink.sendMessage(kakaoBuilder,this);

        }catch (Exception e){
        }
        finish();

    }
    public void sendMessageTo(String phoneNum, String message) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNum, null, message, null, null);
        finish();
    }
}
