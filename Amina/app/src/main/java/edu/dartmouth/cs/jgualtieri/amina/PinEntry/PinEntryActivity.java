package edu.dartmouth.cs.jgualtieri.amina.PinEntry;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import edu.dartmouth.cs.jgualtieri.amina.R;

public class PinEntryActivity extends AppCompatActivity {

    // reference safety radiobutton group
    RadioGroup safetyGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_entry);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // get safety radiogroup
        safetyGroup = (RadioGroup) findViewById(R.id.safetyRadioGroupPin);

        // set the initially checked radiobutton
        setCheckedSafetyStatus(getIntent().getExtras().getInt("safetyStatus"));

        //TODO: This list should be populated from Hashtag db
        String[] hashtags = new String[] {
                "#water", "#electricity", "#food", "#shelter", "#safe"
        };

        // create array adapter for autocomplete hashtag editText field
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, hashtags);



        final MultiAutoCompleteTextView textView = (MultiAutoCompleteTextView) findViewById(R.id.hashtagEditText);
        textView.setAdapter(adapter);
        textView.setThreshold(1);
        textView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        
    }

    // create save button in action bar
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.pin_entry_save_button, menu);
        return true;
    }

    //when user clicks the save button in action bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_pin_button:

                //TODO: save pin to db
                Log.d("save", "pin saved clicked");
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
}
