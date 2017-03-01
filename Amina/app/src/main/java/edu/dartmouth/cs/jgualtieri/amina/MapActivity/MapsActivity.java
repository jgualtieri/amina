package edu.dartmouth.cs.jgualtieri.amina.MapActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import edu.dartmouth.cs.jgualtieri.amina.MainActivity;
import edu.dartmouth.cs.jgualtieri.amina.R;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    // time limit to open
    private final int PROMPTTIMELIMIT = 24 * 60;

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // get shared preferences
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();

        // save timestamps
        Date timestampDate;
        Date currentDate;

        // get timestamp
        String date = preferences.getString("timestamp", null);

        // if saved timestamp exists
        if (date != null) {

            // create date format
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
            try {

                // parse saved timestamp
                timestampDate = dateFormat.parse(date);

                // parse current time
                String currentDateString = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
                currentDate = dateFormat.parse(currentDateString);

                // check the time difference in minutes
                long diff = currentDate.getTime() - timestampDate.getTime();
                long diffMinutes = diff / (60 * 1000) % 60;

                // if the time difference is greater than 24 hours
                if (diffMinutes > PROMPTTIMELIMIT){

                    // update timestamp
                    String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
                    editor.putString("timestamp", timeStamp);
                }

            } catch (ParseException pe){
                pe.printStackTrace();
            }
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng hanover = new LatLng(43.7, -72.2);

        // test code to show shit on mapscreen
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(hanover)
                .zoom(10)
                .bearing(0)
                .tilt(0)
                .build();

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(43.6, -72.1))
                .title("Danger"));

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(43.9, -72.4))
                .title("Safe")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Hanover"));
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }


    // override the back button so user can't go to MainActivity
    @Override
    public void onBackPressed() {
        // finish all apps
        finishAffinity();
    }
}
