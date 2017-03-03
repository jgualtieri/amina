package edu.dartmouth.cs.jgualtieri.amina.MapActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.dartmouth.cs.jgualtieri.amina.MainActivity;
import edu.dartmouth.cs.jgualtieri.amina.R;

public class MapsFragment extends Fragment {

    // string to reference boolean of whether prompt has been shown or not
    private final String PROMPT = "prompt";

    private OnFragmentInteractionListener interactionListener;

    // save shared preferences
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

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
        final View view = inflater.inflate(R.layout.fragment_map, container, false);

        // clear the container of any existing views
        if (container != null) {
            container.removeAllViews();
        }

        // create floation action button
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
                        // ToDo get user input here
                    }
                });

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
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
