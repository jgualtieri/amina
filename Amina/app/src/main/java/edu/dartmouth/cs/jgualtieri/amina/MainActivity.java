package edu.dartmouth.cs.jgualtieri.amina;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import edu.dartmouth.cs.jgualtieri.amina.AuthenticationActivity.AuthenticationActivity;

public class MainActivity extends AppCompatActivity {

    private String SIGNEDINSTATUS = "signedIn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (!preferences.getBoolean(SIGNEDINSTATUS, false)){
            Intent intent = new Intent(this, AuthenticationActivity.class);
            intent.putExtra("auth", "auth");
            startActivity(intent);
        }

        setContentView(R.layout.activity_main);

        // testing
    }
}
