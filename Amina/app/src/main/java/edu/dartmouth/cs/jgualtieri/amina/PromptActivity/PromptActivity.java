package edu.dartmouth.cs.jgualtieri.amina.PromptActivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import edu.dartmouth.cs.jgualtieri.amina.Data.Pin;
import edu.dartmouth.cs.jgualtieri.amina.R;

/**
 * Created by Justin Gualtieri on 2/26/17.
 *
 * Prompt Activity will be used to prompt the user for information about their
 * current location - this information will then be used by the Amina app
 * to crowd-source whether certain locations are safe, have access to food and
 * water, etc. - the Prompt Activity will be activated every 24 hours.
 */

public class PromptActivity extends AppCompatActivity {
    static Pin pin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prompt);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // If the user chooses to answer questions, launch the questions fragment
        Button yesButton = (Button) findViewById(R.id.prompt_yes);
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // If the user chooses not to answer questions, go to the main menu
        Button noButton = (Button) findViewById(R.id.prompt_no);
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

}
