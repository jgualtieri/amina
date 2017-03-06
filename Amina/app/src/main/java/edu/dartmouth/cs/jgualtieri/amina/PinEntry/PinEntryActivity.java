package edu.dartmouth.cs.jgualtieri.amina.PinEntry;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.icu.util.Calendar;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Layout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

import edu.dartmouth.cs.jgualtieri.amina.Data.Hashtag;
import edu.dartmouth.cs.jgualtieri.amina.Data.Pin;
import edu.dartmouth.cs.jgualtieri.amina.Data.PinHashtagDBHelper;
import edu.dartmouth.cs.jgualtieri.amina.Data.SQLiteHelper;
import edu.dartmouth.cs.jgualtieri.amina.R;

public class PinEntryActivity extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "PinEntryActivity";

    // used to save to database
    private PinHashtagDBHelper dbHelper;
    private Pin pin = new Pin();

    // reference safety radio button group
    RadioGroup safetyGroup;
    int safetyStatus;

    // used to get last known location
    GoogleApiClient googleApiClient;
    Location lastLocation;
    double lastLocationX;
    double lastLocationY;

    MultiAutoCompleteTextView hashTagTextView;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_entry);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // get user permission to use location
        requestPermission();

        // get safety radiogroup
        safetyGroup = (RadioGroup) findViewById(R.id.safetyRadioGroupPin);

        // set the initially checked radiobutton
        safetyStatus = getIntent().getExtras().getInt("safetyStatus");
        setCheckedSafetyStatus(safetyStatus);

        //TODO: This list should be populated from Hashtag db
        final String[] hashtags = new String[] {
                "#water", "#electricity", "#food", "#shelter", "#safe"
        };

        // create array adapter for autocomplete hashtag editText field
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, hashtags);

        // Create an instance of GoogleAPIClient.
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        hashTagTextView = (MultiAutoCompleteTextView) findViewById(R.id.hashtagEditText);
        hashTagTextView.setAdapter(adapter);
        hashTagTextView.setThreshold(1);
        hashTagTextView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
    }

    // create save button in action bar
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.pin_entry_save_button, menu);
        return true;
    }

    //when user clicks the save button in action bar
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_pin_button:

                // Init the db helper
                dbHelper = new PinHashtagDBHelper(this);
                dbHelper.open();

                // Populate the necessary fields in the Pin object
                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
                pin.setUserId(settings.getString("id", ""));
                pin.setLocationX(lastLocationX);
                pin.setLocationY(lastLocationY);
                pin.setDateTime(Calendar.getInstance());
                pin.setSafetyStatus(safetyStatus);

                EditText commentEditText = (EditText) findViewById(R.id.commentsEditText);
                pin.setComment(commentEditText.getText().toString());

                long pinEntryId = dbHelper.addPinToDatabase(pin);
                if (pinEntryId > 0) {
                    Log.d(TAG, "Successfully saved pin with id = " + pinEntryId);
                } else {
                    Log.d(TAG, "Unable to successfully save pin.");
                }

                // Parse out the hashtags from textView
                String hashtagsString = hashTagTextView.getText() + "";
                if (!hashtagsString.isEmpty()) {
                    hashtagsString = hashtagsString.replaceAll("\\s+", "");
                    if (hashtagsString.charAt(hashtagsString.length() - 1) == ',') {
                        hashtagsString = hashtagsString.substring(0, hashtagsString.length() - 1);
                    }
                    String[] hashtags = hashtagsString.split(",");

                    // Store all of the hashtags
                    for (String hashtag : hashtags) {
                        Hashtag hashtagObject = new Hashtag();
                        String[] associatedPins = {String.valueOf(pinEntryId)};
                        hashtagObject.setValue(hashtag);
                        hashtagObject.setAssociatedPins(associatedPins);
                        long hashtagId = dbHelper.addHashtagToDatabase(hashtagObject, pinEntryId);
                        if (hashtagId > 0) {
                            Log.d(TAG, "Successfully saved or updated hashtag with value = " +
                                    hashtagObject.getValue());
                        } else {
                            Log.d(TAG, "Unable to save hashtag with value = " +
                                    hashtagObject.getValue());
                        }
                    }
                }

                Intent resultIntent = new Intent();
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
                return true;

            default:
                return true;
        }
    }

    // set the correct radio button as checked
    public void setCheckedSafetyStatus(int safetyRadioButton){
        switch (safetyRadioButton){
            case 1:
                safetyGroup.check(R.id.greenPinEntry);
                break;

            case 2:
                safetyGroup.check(R.id.yellowPinEntry);
                break;

            case 3:
                safetyGroup.check(R.id.redPinEntry);
                break;
        }
    }

    // Get the last known location
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (checkPermission()) {
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    googleApiClient);
            if (lastLocation != null) {
                lastLocationX = lastLocation.getLatitude();
                lastLocationY = lastLocation.getLongitude();
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void requestPermission() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean checkPermission() {
        return (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED);
    }

    protected void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }
}
