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

    }

    // Called when the user presses the continue button
    public void onClick(View view){
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = settings.edit();

        switch (view.getId()){

            // if continue clicked
            case (R.id.welcomeContinue):

                // set welcome status to true so it doesn't open again
                editor.putBoolean("welcomeStatus", true);

                // set initial timestamp into sharedPreferences
                String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
                editor.putString("timestamp", timeStamp);
                editor.commit();

                // set intent result
                Intent resultIntent = new Intent();
                setResult(Activity.RESULT_OK, resultIntent);

                // finish the activity
                finish();
                break;
        }
    }

}
