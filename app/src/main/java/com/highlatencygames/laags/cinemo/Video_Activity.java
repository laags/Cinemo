package com.highlatencygames.laags.cinemo;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import java.io.File;
import java.lang.reflect.Field;

public class Video_Activity extends AppCompatActivity {

    VideoView video;
    MediaController mController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_);
        getSupportActionBar().hide(); // Hide the action bar

        // Receive data from intent
        Intent intent = getIntent();
        String vidName = intent.getExtras().getString("VideoName");
        video = (VideoView) findViewById(R.id.videoViewId);

        // Create MediaController
        mController = new MediaController(this);

        // Check if video is from resources or filepath and set it accordingly
        File file = new File(vidName);
        if(file.exists()) {
            video.setVideoPath(vidName);
        }
        else {
            Uri videoPath = Uri.parse("android.resource://" + getPackageName() +"/" + getId(vidName, R.raw.class));
            video.setVideoURI(videoPath);
        }
        video.requestFocus();
        mController.setAnchorView(video);
        video.setMediaController(mController);
        video.start();

    }

    // Get the ID of video from raw
    public static int getId(String resourceName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(resourceName);
            return idField.getInt(idField);
        } catch (Exception e) {
            throw new RuntimeException("No resource ID found for: "
                    + resourceName + " / " + c, e);
        }
    }
}
