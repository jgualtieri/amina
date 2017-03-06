package edu.dartmouth.cs.jgualtieri.amina;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.justingualtieri.myapplication.backend.registration.Registration;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.dartmouth.cs.jgualtieri.amina.AuthenticationActivity.AuthenticationActivity;
import edu.dartmouth.cs.jgualtieri.amina.MapActivity.MapActivity;
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

        // back button base case
        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }

        // register with GCM
        GCMRegistration gcmRegistration = new GCMRegistration(this);
        gcmRegistration.execute();

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
        Intent intent = new Intent(this, MapActivity.class);
        startActivityForResult(intent, MAP);
    }

    class GCMRegistration extends AsyncTask<Void, Void, String> {
        private Registration registration = null;
        private GoogleCloudMessaging gcm;
        private Context context;

        // TODO: change to your own sender ID to Google Developers Console project number, as per instructions above
        private static final String SENDER_ID = "318663839137";

        public GCMRegistration(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(Void... params) {
            if (registration == null) {
                Registration.Builder builder = new Registration.Builder(AndroidHttp.newCompatibleTransport(),
                        new AndroidJsonFactory(), null)
                        // Need setRootUrl and setGoogleClientRequestInitializer only for local testing,
                        // otherwise they can be skipped
                        .setRootUrl("http://127.0.0.1:8080/_ah/api/")
                        .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                            @Override
                            public void initialize(AbstractGoogleClientRequest<?>
                                                           abstractGoogleClientRequest)
                                    throws IOException {
                                abstractGoogleClientRequest.setDisableGZipContent(true);
                            }
                        });
                // end of optional local run code
                registration = builder.build();
            }

            String msg = "";
            try {
                if (gcm == null) {
                    gcm = GoogleCloudMessaging.getInstance(context);
                }
                String regId = gcm.register(SENDER_ID);
                msg = "Successfully registered device with ID " + regId;

                // You should send the registration ID to your server over HTTP,
                // so it can use GCM/HTTP or CCS to send messages to your app.
                // The request to your server should be authenticated if your app
                // is using accounts.
                registration.register(regId).execute();

            } catch (IOException ex) {
                ex.printStackTrace();
                msg = "Error: " + ex.getMessage();
            }
            return msg;
        }

        @Override
        protected void onPostExecute(String msg) {
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
            Logger.getLogger("REGISTRATION").log(Level.INFO, msg);
        }
    }
}
