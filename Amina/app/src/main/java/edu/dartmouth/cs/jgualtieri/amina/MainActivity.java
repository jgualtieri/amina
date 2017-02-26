package edu.dartmouth.cs.jgualtieri.amina;

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
import edu.dartmouth.cs.jgualtieri.amina.PromptActivity.PromptActivity;
import edu.dartmouth.cs.jgualtieri.amina.WelcomeActivity.WelcomeActivity;

public class MainActivity extends AppCompatActivity {

    private String SIGNEDINSTATUS = "signedIn";
    private String WELCOMESTATUS = "welcomeStatus";
    private String PROMPTSTUATUS = "promptStatus";

    private final int WELCOME = 0;
    private final int LOGIN = 1;
    private final int PROMPT = 2;

    private final int PROMPTTIMELIMIT = 24 * 60;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();

        if (!preferences.getBoolean(WELCOMESTATUS, false)){
            welcome();
        }

        if (preferences.getBoolean(WELCOMESTATUS, false)){
            signIn();
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
                Log.d("prompt", "login reached");
                Date timestampDate;
                Date currentDate;

                String date = preferences.getString("timestamp", null);
                Log.d("prompt", "timestamp: " + date);

                if (!preferences.getBoolean(PROMPTSTUATUS, false)){
                    // launchPrompt first time
                    editor.putBoolean(PROMPTSTUATUS, true);
                    editor.commit();
                }

                if (date != null) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
                    try {
                        timestampDate = dateFormat.parse(date);

                        String currentDateString = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
                        currentDate = dateFormat.parse(currentDateString);
                        Log.d("prompt", timestampDate.toString());
                        Log.d("prompt", "current date: " + currentDate.toString());

                        long diff = currentDate.getTime() - timestampDate.getTime();
                        long diffMinutes = diff / (60 * 1000) % 60;


                        Log.d("prompt", "difference in hours  " + diffMinutes);
                        if (diffMinutes > PROMPTTIMELIMIT){
                            String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
                            editor.putString("timestamp", timeStamp);
                            //prompt();
                        }

                    } catch (ParseException pe){
                        pe.printStackTrace();
                    }
                }

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

    public void prompt(){
        Intent intent = new Intent(this, PromptActivity.class);
        startActivityForResult(intent, PROMPT);
    }

}
