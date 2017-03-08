package edu.dartmouth.cs.jgualtieri.amina.MapActivity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import edu.dartmouth.cs.jgualtieri.amina.ContentActivity.ContentFragment;
import edu.dartmouth.cs.jgualtieri.amina.Data.Pin;
import edu.dartmouth.cs.jgualtieri.amina.Data.PinHashtagDBHelper;
import edu.dartmouth.cs.jgualtieri.amina.ContentActivity.ContentsFragment;
import edu.dartmouth.cs.jgualtieri.amina.MainActivity;
import edu.dartmouth.cs.jgualtieri.amina.R;

public class MapActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        MapsFragment.OnFragmentInteractionListener, SearchView.OnQueryTextListener, ContentFragment.OnFragmentInteractionListener, ContentsFragment.OnFragmentInteractionListener {

    // shared preferences
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    // save reference to main activity context
    public static Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // assign values
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
        context = this;

        // create navigationView
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // set user's name in Sliding Navigation View
        View hView =  navigationView.getHeaderView(0);
        TextView nav_user = (TextView)hView.findViewById(R.id.nameDisplayTextView);
        nav_user.setText(preferences.getString("name", ""));

        // create toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // create drawer layout
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // set first fragment as selected
        itemSelected(R.id.worldView);
        navigationView.getMenu().getItem(0).setChecked(true);
    }

    // create save button in action bar
    public boolean onCreateOptionsMenu(Menu menu) {
        //super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setQueryHint("#Hashtag...");
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        // call function to handle switching
        itemSelected(id);
        return true;
    }

    public void itemSelected(int itemId){

        // get the itemID of the item clicked
        int id = itemId;

        FragmentManager fragmentManager = getFragmentManager();

        // if first item is selected, open maps fragment
        if (id == R.id.worldView) {

            // replace container with MapsFragment
            fragmentManager.beginTransaction().replace(R.id.content_map,
                    new MapsFragment()).commit();
        }

        // if second item selected, open results fragment
        else if (id == R.id.connections) {

            // replace container with ResultsFragment
//            fragmentManager.beginTransaction().replace(R.id.content_map,
//                    new ResultsFragment()).commit();
        }

        else if (id == R.id.info) {

            // replace container with MapsFragment

            //Intent intent = new Intent(this, ContentActivity.class);
            //startActivity(intent);
            fragmentManager.beginTransaction().replace(R.id.content_map,
                    new ContentsFragment()).commit();

        }

        // close the drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    public void onFragmentInteraction(Uri uri){
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("EXIT", true);
        startActivity(intent);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        // Get access to the database
        PinHashtagDBHelper data = new PinHashtagDBHelper(context);
        data.open();

        MapsFragment.map.clear();

        // Query the database for the pins associated with a given hashtag
        List<Pin> pins = data.queryDatabaseForPins(query);
        for (Pin pin : pins) {

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

            String hashtags = pin.getHashtags().toString();
            String comments = pin.getComment();
            String send = hashtags + "|" + comments;

            MapsFragment.map.addMarker(new MarkerOptions()
                    .position(new LatLng(pin.getLocationX(), pin.getLocationY()))
                    .title(title)
                    .snippet(send)
                    .icon(BitmapDescriptorFactory.defaultMarker(icon)));
            Log.d("Query result", pin.getComment());
        }

        data.close();

        // Toast.makeText(context, "Query: " + query, Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        PinHashtagDBHelper data = new PinHashtagDBHelper(context);
        data.open();

        // return an array list of all entries
        List<Pin> pins = data.getAllEntries();
        for (Pin pin : pins) {

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

            String hashtags = pin.getHashtags().toString();
            String comments = pin.getComment();
            String send = hashtags + "|" + comments;
            //Log.d("snippet", send);

            MapsFragment.map.addMarker(new MarkerOptions()
                    .position(new LatLng(pin.getLocationX(), pin.getLocationY()))
                    .title(title)
                    .snippet(send)
                    .icon(BitmapDescriptorFactory.defaultMarker(icon)));

        }

        return false;
    }
}
