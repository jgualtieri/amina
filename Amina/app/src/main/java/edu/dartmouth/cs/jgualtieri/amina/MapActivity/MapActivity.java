package edu.dartmouth.cs.jgualtieri.amina.MapActivity;

import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import edu.dartmouth.cs.jgualtieri.amina.R;

public class MapActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    // save shared preferences
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View hView =  navigationView.getHeaderView(0);
        TextView nav_user = (TextView)hView.findViewById(R.id.nameDisplayTextView);
        nav_user.setText(preferences.getString("name", ""));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // create drawer layout
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // set first fragment as selected
        itemSelected(R.id.stressMeter);
        navigationView.getMenu().getItem(0).setChecked(true);
    }

    @SuppressWarnings("StatementWithEmptyBody")
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

        // if first item is selected, open stress meter fragment
        if (id == R.id.stressMeter) {

            // replace container with ImageFragment
            fragmentManager.beginTransaction().replace(R.id.content_main,
                    new ImageRequestFragment()).commit();
        }

        // if second item selected, open results fragment
        else if (id == R.id.results) {

            // replace container with ResultsFragment
            fragmentManager.beginTransaction().replace(R.id.content_main,
                    new ResultsFragment()).commit();
        }

        // close the drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }
}
