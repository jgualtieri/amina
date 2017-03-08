package edu.dartmouth.cs.jgualtieri.amina.Data;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;

import java.util.ArrayList;

import edu.dartmouth.cs.jgualtieri.amina.MapActivity.MapsFragment;

/**
 * Created by azharhussain on 2/19/17.
 * Class provided by CS65 to receive message from server and delete corresponding entry
 */

public class GcmIntentService extends IntentService {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        // get shared prefereces
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();

        if (extras != null && !extras.isEmpty()) { // has effect of unparcelling Bundle
            // Since we're not using two way messaging, this is all we really to check for
            if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {

                String rawMessage = extras.getString("message");
                String[] messageSplit = rawMessage.split("/");

                PinHashtagDBHelper dbHelper = new PinHashtagDBHelper(getApplicationContext());
                dbHelper.open();

                if (messageSplit[0].equals("Pin")){
                    if (!messageSplit[2].equals(preferences.getString("id", ""))){

                        Pin pin = new Pin();
                        pin.setHashId(messageSplit[1]);
                        pin.setUserId(messageSplit[2]);
                        pin.setLocationX(Double.parseDouble(messageSplit[3]));
                        pin.setLocationY(Double.parseDouble(messageSplit[4]));
                        pin.setDateTime(Calendar.getInstance());
                        pin.setSafetyStatus(Integer.parseInt(messageSplit[5]));
                        pin.setComment(messageSplit[6]);


                        // How to retrieve your Java object back from the string
                        Gson gson = new Gson();
                        ArrayList<String> pinHashtagArray = gson.fromJson(messageSplit[7], ArrayList.class);
                        pin.setHashtags(pinHashtagArray);
                        dbHelper.addCloudPinToDatabase(pin);

                        dbHelper.updateMap();
                        showToast("remote pin added: " + messageSplit[1]);
                    }
                } else {
                    if (!messageSplit[2].equals(preferences.getString("id", ""))) {
                        Hashtag hashtagObject = new Hashtag();
                        String[] associatedPins = messageSplit[2].split(",");
                        hashtagObject.setValue(messageSplit[1]);
                        hashtagObject.setAssociatedPins(associatedPins);
                        Log.d("search", "broadcast triggered add");
                        dbHelper.addCloudHashtagToDatabase(hashtagObject);
                        showToast("remote Hashtag added: " + messageSplit[1]);
                    }
                }
            }
        }
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    // function to show toast notifying user of delete operation
    protected void showToast(final String message) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}

