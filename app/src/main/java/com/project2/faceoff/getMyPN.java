package com.project2.faceoff;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.widget.Toast;

/**
 * Created by q on 2017-07-08.
 */

public class getMyPN extends AppCompatActivity{

    public String myPN = null;
    /** override onRequestPermissionResult for permission checking final value
     * if requestCode is MY_PERMISSION_REQUEST_READ_CONTACTS check the grantResults it is granted or not
     * if granted showAddress function call else show text No Permission */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.READ_PHONE_STATE}, 100);
            }
        }
        else{
            GetMyPN();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 100: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    GetMyPN(); // permission was granted, yay! Do the contacts-related task you need to do.
                } else {      // permission denied, boo! Disable the functionality that depends on this permission.
                    Toast.makeText(this, "No Permissions ", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
    public void GetMyPN(){
        TelephonyManager telManager = (TelephonyManager) getApplicationContext().getSystemService(getApplicationContext().TELEPHONY_SERVICE);
        myPN = telManager.getLine1Number();
        Intent intent = new Intent(this, kakaoSignupActivity.class);
        intent.putExtra("PN",myPN);
    }

}
