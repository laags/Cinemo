package com.highlatencygames.laags.cinemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.angads25.filepicker.controller.DialogSelectionListener;
import com.github.angads25.filepicker.model.DialogConfigs;
import com.github.angads25.filepicker.model.DialogProperties;
import com.github.angads25.filepicker.view.FilePickerDialog;

import java.io.File;

public class AddMovie_Activity extends AppCompatActivity {
    FilePickerDialog dialog;
    TextView imgTxt, videoTxt;
    EditText titleTxt, descTxt;
    ImageView border;
    Animation slideDown;
    Boolean imgSelected = false, videoSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_movie_);
        getSupportActionBar().hide(); // Hide the action bar

        // Get Views
        titleTxt = (EditText) findViewById(R.id.titleTxt);
        descTxt = (EditText) findViewById(R.id.descTxt);
        imgTxt = (TextView) findViewById(R.id.imgTxt);
        videoTxt = (TextView) findViewById(R.id.videoTxt);

        // Set Text Fields to empty
        titleTxt.setText("");
        descTxt.setText("");
        imgTxt.setText("");
        videoTxt.setText("");

        // FilePicker implementation via com.github.angads25:filepicker:1.1.1
        DialogProperties properties = new DialogProperties();
        properties.selection_mode = DialogConfigs.SINGLE_MODE;
        properties.selection_type = DialogConfigs.FILE_SELECT;
        properties.root = new File(DialogConfigs.DEFAULT_DIR);
        properties.error_dir = new File(DialogConfigs.DEFAULT_DIR);
        properties.offset = new File(DialogConfigs.DEFAULT_DIR);
        properties.extensions = null;
        dialog = new FilePickerDialog(AddMovie_Activity.this,properties);
        dialog.setTitle("Select a File");
        dialog.setDialogSelectionListener(new DialogSelectionListener() {
            @Override
            public void onSelectedFilePaths(String[] files) {
                if(videoSelected)
                    videoTxt.setText(files[0]);
                else if(imgSelected)
                    imgTxt.setText(files[0]);
            }
        });

        // Button to select Video
        Button selectVideoBtn = (Button) findViewById(R.id.selectVideoBtn);
        final Animation slideLeft = AnimationUtils.loadAnimation(this, R.anim.slideleft);
        selectVideoBtn.startAnimation(slideLeft);
        selectVideoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoSelected = true;
                imgSelected = false;
                dialog.show();
            }
        });

        // Button to select Image
        Button selectImgBtn = (Button) findViewById(R.id.selectImgBtn);
        final Animation slideRight = AnimationUtils.loadAnimation(this, R.anim.slideright);
        selectImgBtn.startAnimation(slideRight);
        selectImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoSelected = false;
                imgSelected = true;
                dialog.show();
            }
        });

        // Button to add Movie
        final Button addMovieBtn = (Button) findViewById(R.id.addMovieBtn);
        final Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slideup);
        addMovieBtn.startAnimation(slideUp);
        addMovieBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if all fields are filled first for validation
                if(titleTxt.getText().toString() != "" && descTxt.getText().toString() != "" && imgTxt.getText().toString() != ""
                        && videoTxt.getText().toString() != "") {
                    // Add the new movie to DB
                    DBAdapter db = new DBAdapter(addMovieBtn.getContext());
                    db.open();
                    db.insertMovie(titleTxt.getText().toString(), descTxt.getText().toString(), imgTxt.getText().toString(), "0", videoTxt.getText().toString());
                    db.close();
                    Intent intent = new Intent(addMovieBtn.getContext(), MainActivity.class);
                    addMovieBtn.getContext().startActivity(intent);
                }else {
                    Toast.makeText(AddMovie_Activity.this, "Please Enter All Fields!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Animation for making the border slide down
        slideDown = AnimationUtils.loadAnimation(this, R.anim.slidedown);
        border = (ImageView) findViewById(R.id.addBorder);
        border.startAnimation(slideDown);
    }

    @Override
    public void onStart(){
        super.onStart();
        border.startAnimation(slideDown);
    }
}


