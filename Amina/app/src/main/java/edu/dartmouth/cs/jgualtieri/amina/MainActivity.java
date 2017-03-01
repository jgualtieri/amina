package edu.dartmouth.cs.jgualtieri.amina;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import edu.dartmouth.cs.jgualtieri.amina.AuthenticationActivity.AuthenticationActivity;
import edu.dartmouth.cs.jgualtieri.amina.MapActivity.MapsActivity;
import edu.dartmouth.cs.jgualtieri.amina.WelcomeActivity.WelcomeActivity;

public class MainActivity extends AppCompatActivity {

    public static Activity activity;

    // access screen status
    private String WELCOMESTATUS = "welcomeStatus";
    private String PROMPTSTUATUS = "promptStatus";

    // intent number
    private final int WELCOME = 0;
    private final int LOGIN = 1;
    private final int MAP = 2;

    // save shared preferences
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get shared prefereces
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
        activity = this;

        // launch sign in authentication activity
        signIn();

        // check if first time opening for welcome screen
        if(!preferences.getBoolean(WELCOMESTATUS, false)){
            welcome();
        }

        setContentView(R.layout.activity_main);
    }

    // on previous activity finishing
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {

            // after login screen finishes
            case LOGIN:

                // launch map activity
                map();
                break;
        }
    }

    public void welcome(){
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivityForResult(intent, WELCOME);
    }

    public void signIn(){
        Intent intent = new Intent(this, AuthenticationActivity.class);
        startActivityForResult(intent, LOGIN);
    }

    public void map(){
        Intent intent = new Intent(this, MapsActivity.class);
        startActivityForResult(intent, MAP);
    }

}
