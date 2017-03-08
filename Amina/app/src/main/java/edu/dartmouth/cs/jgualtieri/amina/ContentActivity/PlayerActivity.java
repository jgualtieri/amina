package edu.dartmouth.cs.jgualtieri.amina.ContentActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

import edu.dartmouth.cs.jgualtieri.amina.R;

public class PlayerActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        final String vidPath = intent.getStringExtra("path");
        setContentView(R.layout.media_player);

        Button buttonPlayVideo = (Button)findViewById(R.id.playvideobuttom);

        getWindow().setFormat(PixelFormat.UNKNOWN);

        VideoView mVideoView = (VideoView)findViewById(R.id.videoview);

        buttonPlayVideo.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {

                VideoView mVideoView = (VideoView)findViewById(R.id.videoview);

                String uriPath = vidPath;

                Uri uri = Uri.parse(uriPath);
                mVideoView.setVideoURI(uri);
                mVideoView.requestFocus();
                mVideoView.start();

            }});
    }
}
