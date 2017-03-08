package edu.dartmouth.cs.jgualtieri.amina.MapActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import edu.dartmouth.cs.jgualtieri.amina.Data.Pin;
import edu.dartmouth.cs.jgualtieri.amina.Data.PinHashtagDBHelper;
import edu.dartmouth.cs.jgualtieri.amina.MainActivity;
import edu.dartmouth.cs.jgualtieri.amina.PinEntry.PinEntryActivity;
import edu.dartmouth.cs.jgualtieri.amina.R;

import static edu.dartmouth.cs.jgualtieri.amina.MapActivity.MapActivity.context;

public class MapsFragment extends Fragment implements Button.OnClickListener, OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    // string to reference boolean of whether prompt has been shown or not
    private final String PROMPT = "prompt";

    // prompt variables
    private final int PROMPTTIMELIMIT = 24 * 60;
    private final int PROMPTINT = 4;

    private OnFragmentInteractionListener interactionListener;

    // save shared preferences
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    // access dimmable shape
    FrameLayout layout_MainMenu;

    // access View
    private View view;
    private FloatingActionButton fab;

    // safety radio group
    private RadioGroup safetyOptions;
    private Button dismissButtonOne;
    private Button dismissButtonTwo;
    private TextView safetyTitle;
    private TextView safetySubtitle;

    // google maps
    public static GoogleMap map;
    private MapView mapView;

    // used to get last known location
    GoogleApiClient googleApiClient;
    Location lastLocation;
    double lastLocationX;
    double lastLocationY;

    public MapsFragment() {
    }

    public static MapsFragment newInstance() {
        MapsFragment fragment = new MapsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_map, container, false);

        // clear the container of any existing views
        if (container != null) {
            container.removeAllViews();
        }

        // create floating action button
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // if user clicks on fab, create new pin
                createPin();
            }
        });

        setupMap(savedInstanceState);


        // Create an instance of GoogleAPIClient.
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(MainActivity.activity)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        googleApiClient.connect();

        // get shared preferences
        preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.activity);
        editor = preferences.edit();

        // if first time opening
        if (!preferences.getBoolean(PROMPT, false)){

            // get user permission to use location
//            requestPermission();

            // display prompt to user
//            makeDialog();
//            editor.putBoolean(PROMPT, true);
//            editor.commit();
        }

        // if its been 24 hours
        if (timeHasElapsed()) {
            createPin();
        }

        // create dimmable shape
        layout_MainMenu = (FrameLayout) view.findViewById(R.id.mainmenu);
        layout_MainMenu.getForeground().setAlpha( 0);

        // get Safety radio button group
        safetyOptions = (RadioGroup) view.findViewById(R.id.safetyRadioGroup);
        safetyTitle = (TextView) view.findViewById(R.id.safetyRadioGroupTitle);
        safetySubtitle = (TextView) view.findViewById(R.id.safetyRadioGroupSubtitle);

        // if use clicks on on one of the safety options
        safetyOptions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                // launch pin entry activity, pass in safety status
                switch(checkedId) {
                    case R.id.greenMap:
                        safetyOptions.check(R.id.greenMap);
                        launchPinEntry(1);
                        safetyOptions.clearCheck();
                        break;
                    case R.id.yellowMap:
                        safetyOptions.check(R.id.yellowMap);
                        launchPinEntry(2);
                        safetyOptions.clearCheck();
                        break;
                    case R.id.redMap:
                        safetyOptions.check(R.id.redMap);
                        launchPinEntry(3);
                        safetyOptions.clearCheck();
                        break;
                }
            }
        });

        // get hidden dismiss buttons
        dismissButtonOne = (Button) view.findViewById(R.id.dismissButtonOne);
        dismissButtonOne.setOnClickListener(this);
        dismissButtonTwo = (Button) view.findViewById(R.id.dismissButtonTwo);
        dismissButtonTwo.setOnClickListener(this);

        // hide safety color options picker
        hideSafetyOptions();

        // Inflate the layout for this fragment
        return view;
    }

    // make dialog explaining to users what Amina is
    public void makeDialog(){
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(MapActivity.context);
        View mView = layoutInflaterAndroid.inflate(R.layout.user_input_dialog_box, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(MapActivity.context);
        alertDialogBuilderUserInput.setView(mView);

        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Got it", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        createPin();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {

                    }
                });

        // create dialog
        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
    }

    // check if 24 hours has elapsed
    public Boolean timeHasElapsed(){
        Date timestampDate;
        Date currentDate;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
        try {
            String date = preferences.getString("timestamp", null);
            timestampDate = dateFormat.parse(date);

            String currentDateString = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
            currentDate = dateFormat.parse(currentDateString);

            long diff = currentDate.getTime() - timestampDate.getTime();
            long diffMinutes = diff / (60 * 1000) % 60;


            if (diffMinutes > PROMPTTIMELIMIT) {
                return true;
            }
        } catch (ParseException pe){
            pe.printStackTrace();
        }

        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean checkPermission() {
        return (MainActivity.activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED);
    }

    public void setupMap(Bundle savedInstanceState){
        Log.d("dubug", "setupMap called");
        MapsInitializer.initialize(getActivity());
        mapView = (MapView) view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public View getInfoContents(Marker marker) {

                Context context = getContext(); //or getActivity(), YourActivity.this, etc.

                LinearLayout info = new LinearLayout(context);
                info.setOrientation(LinearLayout.VERTICAL);

                // Get the title (either Safe, Caution, or Danger)
                TextView title = new TextView(context);
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());
                info.addView(title);

                String[] text = marker.getSnippet().split("[|]");
                Log.d("Value of text", Arrays.toString(text));

                if (text.length >= 1) {
                    if (!text[0].equals("[]")) {
                        Log.d("1: ", text[0]);
                        TextView hashtag = new TextView(context);
                        hashtag.setTextColor(Color.RED);
                        hashtag.setText(text[0]);
                        info.addView(hashtag);
                    }
                }
                if (text.length == 2) {
                    Log.d("2: ", text[1]);
                    TextView comment = new TextView(context);
                    comment.setTextColor(Color.GRAY);
                    String commentText = "Comment: \n" + text[1];
                    comment.setText(commentText);
                    info.addView(comment);
                }

                return info;
            }
        });
    }

    // create a new pin
    public void createPin(){
        showSafetyOptions();
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        editor.putString("timestamp", timeStamp);
        editor.commit();
    }

    // dim map
    public void setDim(){
        layout_MainMenu.getForeground().setAlpha(180);
    }

    // brighten map
    public void unDim(){
        layout_MainMenu.getForeground().setAlpha(0);
    }

    // launch pin entry activity
    public void launchPinEntry(int checkedBox){
        Intent intent = new Intent(MainActivity.activity, PinEntryActivity.class);
        intent.putExtra("safetyStatus", checkedBox);
        Log.d("safetyStatus", checkedBox+"");
        startActivityForResult(intent, PROMPTINT);
    }

    public void showFab(){
        fab.setVisibility(View.VISIBLE);
    }

    public void hideFab(){
        fab.setVisibility(View.INVISIBLE);
    }

    // show safety radio group
    public void showSafetyOptions(){
        setDim();
        hideFab();
        safetyOptions.setVisibility(View.VISIBLE);
        dismissButtonOne.setVisibility(View.VISIBLE);
        dismissButtonTwo.setVisibility(View.VISIBLE);
        safetyTitle.setVisibility(View.VISIBLE);
        safetySubtitle.setVisibility(View.VISIBLE);
    }

    // hide safety radio group
    public void hideSafetyOptions(){
        unDim();
        showFab();
        safetyOptions.setVisibility(View.INVISIBLE);
        dismissButtonOne.setVisibility(View.INVISIBLE);
        dismissButtonOne.setBackgroundColor(Color.TRANSPARENT);
        dismissButtonTwo.setVisibility(View.INVISIBLE);
        dismissButtonTwo.setBackgroundColor(Color.TRANSPARENT);
        safetyTitle.setVisibility(View.INVISIBLE);
        safetySubtitle.setVisibility(View.INVISIBLE);
    }

    // if user clicks on hidden dismiss buttons
    public void onClick(View view){
        hideSafetyOptions();
    }

    // on previous activity finishing
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {

            // after pin entry finishes
            case PROMPTINT:
                hideSafetyOptions();
                break;
        }
    }

    public void onButtonPressed(Uri uri) {
        if (interactionListener != null) {
            interactionListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            interactionListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        interactionListener = null;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onConnected(@Nullable Bundle bundle) {

        Log.d("testing", "connected");

        if (!checkPermission()){

            makeDialog();
            editor.putBoolean(PROMPT, true);
            editor.commit();

            lastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    googleApiClient);
            if (lastLocation != null) {
                lastLocationX = lastLocation.getLatitude();
                lastLocationY = lastLocation.getLongitude();

                // position the camera correctly and animate the move
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(lastLocationX, lastLocationY))
                        .zoom(1)
                        .bearing(0)
                        .tilt(0)
                        .build();

                CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);

                map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                map.animateCamera(zoom);
            }
        }

        if (checkPermission()) {
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    googleApiClient);
            if (lastLocation != null) {
                lastLocationX = lastLocation.getLatitude();
                lastLocationY = lastLocation.getLongitude();

                // position the camera correctly and animate the move
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(lastLocationX, lastLocationY))
                        .zoom(18)
                        .bearing(0)
                        .tilt(0)
                        .build();

                map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState); mapView.onSaveInstanceState(outState);
    }
    @Override
    public void onLowMemory()
    {
        super.onLowMemory();
        mapView.onLowMemory();
    }
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        new AsyncDb().execute();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    // populate the mapView with all the pins
    public static class AsyncDb extends AsyncTask<String, Integer, List<Pin>> {

        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Pin> doInBackground(String... params) {

            PinHashtagDBHelper data = new PinHashtagDBHelper(context);
            data.open();

            // return an array list of all entries
            List<Pin> pins = data.getAllEntries();

            data.close();
            return pins;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected void onPostExecute(List<Pin> result) {
            super.onPostExecute(result);

            map.clear();

            for (Pin pin : result) {

                Log.d("pinhashtag", pin.getHashtags().toString());

                float icon = BitmapDescriptorFactory.HUE_BLUE;
                String title = "";

                switch (pin.getSafetyStatus()) {
                    case (1):
                        icon = BitmapDescriptorFactory.HUE_GREEN;
                        title = "Safe";
                        break;
                    case (2):
                        icon = BitmapDescriptorFactory.HUE_YELLOW;
                        title = "Caution";
                        break;
                    case (3):
                        icon = BitmapDescriptorFactory.HUE_RED;
                        title = "Danger";
                        break;
                }

                String hashtags = "#water, #electricity, #fire #damage\n";
                String comments = "Watch out for potholes!";

                hashtags = pin.getHashtags().toString();
                comments = pin.getComment();
                String send = hashtags + "|" + comments;
                //Log.d("snippet", send);

                map.addMarker(new MarkerOptions()
                        .position(new LatLng(pin.getLocationX(), pin.getLocationY()))
                        .title(title)
                        .snippet(send)
                        .icon(BitmapDescriptorFactory.defaultMarker(icon)));

            }
        }
    }
}

