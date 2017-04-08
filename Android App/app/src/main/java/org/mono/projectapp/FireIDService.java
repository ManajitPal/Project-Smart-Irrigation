package org.mono.projectapp;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by ManajitPal on 02-04-2017.
 */

public class FireIDService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        String tkn = FirebaseInstanceId.getInstance().getToken(); // Getting tokenID for pushnotificaiton. Dont forget to check the log and paste it on the PHP file.
        Log.d("Not","Token ["+tkn+"]");

    }
}
