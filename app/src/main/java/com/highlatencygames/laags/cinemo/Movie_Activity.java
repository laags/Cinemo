package com.highlatencygames.laags.cinemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.io.File;

public class Movie_Activity extends AppCompatActivity {

    private TextView tvtitle, tvdescription;
    private ImageView img, playbtn;
    private RatingBar ratingBar;
    private Button rateBtn, deleteBtn;
    private Long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_);
        getSupportActionBar().hide(); // Hide the action bar

        // Receive data from intent
        Intent intent = getIntent();
        final String title = intent.getExtras().getString("Title");
        id = Long.parseLong(intent.getExtras().getString("Id"));
        final String description = intent.getExtras().getString("Description");
        final String sImg = intent.getExtras().getString("ImgName");
        final int rating = intent.getExtras().getInt("Rating");
        int image = intent.getExtras().getInt("Thumbnail");
        final String videoName = intent.getExtras().getString("VideoName");

        // Get the views
        tvtitle = (TextView) findViewById(R.id.txttitle);
        tvdescription = (TextView) findViewById(R.id.txtdescription);
        img = (ImageView) findViewById(R.id.moviethumbnail);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        // Play Button for video, launches the video playback in another activity
        playbtn = (ImageView) findViewById(R.id.playthumbnail);
        playbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(playbtn.getContext(), Video_Activity.class);
                intent.putExtra("VideoName", videoName);
                playbtn.getContext().startActivity(intent);
            }
        });

        // Button for saving the rating
        rateBtn = (Button) findViewById(R.id.ratebtn);
        final Animation slideLeft = AnimationUtils.loadAnimation(this, R.anim.slideleft);
        rateBtn.startAnimation(slideLeft);
        rateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBAdapter db = new DBAdapter(ratingBar.getContext());
                db.open();
                db.updateRating(id, ratingBar.getRating());
                db.close();
                Intent intent = new Intent(rateBtn.getContext(), MainActivity.class);
                rateBtn.getContext().startActivity(intent);
            }
        });

        // Button for deleting the movie
        deleteBtn = (Button) findViewById(R.id.deleteBtn);
        final Animation slideRight = AnimationUtils.loadAnimation(this, R.anim.slideright);
        deleteBtn.startAnimation(slideRight);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBAdapter db = new DBAdapter(deleteBtn.getContext());
                db.open();
                db.deleteMovie(id);
                db.close();
                Intent intent = new Intent(deleteBtn.getContext(), MainActivity.class);
                deleteBtn.getContext().startActivity(intent);
            }
        });

        // Set the title/desc
        tvtitle.setText(title);
        tvdescription.setText(description);

        // This switch is for setting the ratings value
        switch(rating) {
            case 0:
                ratingBar.setRating(0);
                break;
            case 1:
                ratingBar.setRating(1);
                break;
            case 2:
                ratingBar.setRating(2);
                break;
            case 3:
                ratingBar.setRating(3);
                break;
            case 4:
                ratingBar.setRating(4);
                break;
            case 5:
                ratingBar.setRating(5);
                break;
        }

        // Checking if image is provided via filepath or resources and setting it accordingly
        File imgFile = null;
        try {
            imgFile = new File(sImg);
        }catch(Exception e){}
        if(imgFile != null){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            img.setImageBitmap(myBitmap);
        }else{
            img.setImageResource(image);
        }

    }
}
