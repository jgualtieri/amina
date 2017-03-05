package edu.dartmouth.cs.jgualtieri.amina.MapActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import edu.dartmouth.cs.jgualtieri.amina.MainActivity;
import edu.dartmouth.cs.jgualtieri.amina.PinEntry.PinEntryActivity;
import edu.dartmouth.cs.jgualtieri.amina.R;

public class MapsFragment extends Fragment implements Button.OnClickListener{

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

    // safety radio group
    private RadioGroup safetyOptions;
    private Button dismissButtonOne;
    private Button dismissButtonTwo;
    private TextView safetyTitle;
    private TextView safetySubtitle;

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
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // if user clicks on fab, create new pin
                createPin();
            }
        });



        // get shared preferences
        preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.activity);
        editor = preferences.edit();

        // if first time opening
        if (!preferences.getBoolean(PROMPT, false)){

            // display prompt to user
            makeDialog();
            editor.putBoolean(PROMPT, true);
            editor.commit();
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

    // show safety radio group
    public void showSafetyOptions(){
        setDim();
        safetyOptions.setVisibility(View.VISIBLE);
        dismissButtonOne.setVisibility(View.VISIBLE);
        dismissButtonTwo.setVisibility(View.VISIBLE);
        safetyTitle.setVisibility(View.VISIBLE);
        safetySubtitle.setVisibility(View.VISIBLE);
    }

    // hide safety radio group
    public void hideSafetyOptions(){
        unDim();
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

//        Snackbar.make(view, "hide button clicked", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
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

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
