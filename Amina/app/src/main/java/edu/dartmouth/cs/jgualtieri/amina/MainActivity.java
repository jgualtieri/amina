package edu.dartmouth.cs.jgualtieri.amina;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import edu.dartmouth.cs.jgualtieri.amina.AuthenticationActivity.AuthenticationActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, AuthenticationActivity.class);
        intent.putExtra("auth", "auth");
        startActivity(intent);
        //setContentView(R.layout.activity_main);

        // testing
    }
}
