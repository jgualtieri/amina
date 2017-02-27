package edu.dartmouth.cs.jgualtieri.amina.WelcomeActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Date;

import edu.dartmouth.cs.jgualtieri.amina.R;

/**
 * Created by Azhar Hussain on 2/26/17.
 *
 * Welcome Activity will be displayed once, the first time a user activates the
 * application - this will inform the user of the purpose of the app and provide
 * a segway into authentication
 */

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Update the shared preferences recording that the Welcome Activity has been
        // launched - that way the activity will not be launched again the next time
        // a user opens the app
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        if (settings.getBoolean("welcomeStatus", false)){
            Intent resultIntent = new Intent();
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        }

    }

    // Called when the user presses the continue button
    public void onClick(View view){
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = settings.edit();

        switch (view.getId()){

            case (R.id.welcomeContinue):
                editor.putBoolean("welcomeStatus", true);

                String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
                Log.d("prompt", "timestamp set by welcome: " + timeStamp);
                editor.putString("timestamp", timeStamp);
                editor.commit();

                Intent resultIntent = new Intent();
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
                break;

                //if (date != null && );
        }
    }

}
