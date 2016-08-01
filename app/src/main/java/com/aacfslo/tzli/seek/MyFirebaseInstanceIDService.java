package com.aacfslo.tzli.seek;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by terrence on 6/7/16.
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private String TAG = "MyFirebaseInstanceIDService";
    public static String FIREBASE_URL = "https://boiling-heat-1137.firebaseIO.com/";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // TODO: Implement this method to send any registration to your app's servers.
        sendRegistrationToServer(refreshedToken);

    }

    private void sendRegistrationToServer(String refreshedToken) {

    }
}






