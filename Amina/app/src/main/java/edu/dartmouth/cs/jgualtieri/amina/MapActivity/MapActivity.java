package edu.dartmouth.cs.jgualtieri.amina.MapActivity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import edu.dartmouth.cs.jgualtieri.amina.R;

public class MapActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        MapsFragment.OnFragmentInteractionListener{

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

        // close the drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    public void onFragmentInteraction(Uri uri){
    }
}
