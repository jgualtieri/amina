package edu.dartmouth.cs.jgualtieri.amina;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import edu.dartmouth.cs.jgualtieri.amina.AuthenticationActivity.AuthenticationActivity;
import edu.dartmouth.cs.jgualtieri.amina.WelcomeActivity.WelcomeActivity;

public class MainActivity extends AppCompatActivity {

    private String SIGNEDINSTATUS = "signedIn";
    private String WELCOMESTATUS = "welcomeStatus";

    private final int WELCOME = 0;
    private final int LOGIN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (!preferences.getBoolean(WELCOMESTATUS, false)){
            Intent intent = new Intent(this, WelcomeActivity.class);
            startActivityForResult(intent, WELCOME);
        }


        setContentView(R.layout.activity_main);

        // testing
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case WELCOME:
                //you just got back from activity B - deal with resultCode
                //use data.getExtra(...) to retrieve the returned data
                signIn();
                break;
            case LOGIN:
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                break;
        }
    }

    public void signIn(){
        Intent intent = new Intent(this, AuthenticationActivity.class);
        startActivity(intent);
    }
}
