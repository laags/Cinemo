package com.highlatencygames.laags.cinemo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<Movie> lstMovie;
    public static SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide(); // Hide the action bar

        // List of Movies
        lstMovie = new ArrayList<>();

        // Spin the film roll logo
        final Animation spinAnim = AnimationUtils.loadAnimation(this, R.anim.wobble);
        ImageView filmRoll = (ImageView) findViewById(R.id.filmRoll);
        filmRoll.startAnimation(spinAnim);

        // Slide the border down
        final Animation slideDown = AnimationUtils.loadAnimation(this, R.anim.slidedown);
        ImageView border = (ImageView) findViewById(R.id.borderImg);
        border.startAnimation(slideDown);

        // Try to access DB
        try{
            String destPath = "/data/data/" + getPackageName() +"/database/MyDB";
            File f = new File(destPath);
            if(!f.exists()){
                CopyDB(getBaseContext().getAssets().open("mydb"),
                        new FileOutputStream(destPath));
            }

        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }

        DBAdapter db = new DBAdapter(this);

        // Using prefs to prevent the DB from adding sample data more than once
        prefs = getPreferences(MODE_PRIVATE);
        if(!prefs.contains("alreadyLoaded")) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("alreadyLoaded", true);
            editor.apply();
            db.open();
            long id = db.insertMovie("Venom", "Venom gets real mad and punches things", "venom", "1", "venom");
            id = db.insertMovie("Cars", "A lot of left turns in this movie", "cars", "3", "cars");
            id = db.insertMovie("Deadpool", "Grown man in pajamas fights crime", "deadpool", "4", "deadpool");
            id = db.insertMovie("Incredibles", "A family fights crime together then has supper", "incredibles", "5", "incredibles");
            id = db.insertMovie("Superman", "This is a REALLY bad movie", "superman", "1", "superman");
            db.close();
        }

        // Read in movies from DB and add them to the list
        db.open();
        Cursor c = db.getAllMovies();
        if(c.moveToFirst())
        {
            do{
                // This part is to allow us to read from raw AND sdcard, it checks to see if the stored video name
                // coming from a filepath or the raw folder, if it comes from raw we need to get the actual path first
                File testImg = new File(c.getString(3));
                if(testImg.exists()){
                    lstMovie.add(new Movie(c.getString(0), c.getString(1), c.getString(2), 1, Integer.parseInt(c.getString(4)), c.getString(5)));
                    lstMovie.get(lstMovie.size()-1).setImgName(c.getString(3));
                }else {
                    int movieImg = this.getResources().getIdentifier(c.getString(3), "drawable", this.getPackageName());
                    lstMovie.add(new Movie(c.getString(0), c.getString(1), c.getString(2), movieImg, Integer.parseInt(c.getString(4)), c.getString(5)));
                }
            }while(c.moveToNext());
        }
        db.close();

        // Button to add new movie, navigates to add movie screen
        final Button addNew = (Button) findViewById(R.id.addNewBtn);
        final Animation slideLeft = AnimationUtils.loadAnimation(this, R.anim.slideleft);
        addNew.startAnimation(slideLeft);
        addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(addNew.getContext(), AddMovie_Activity.class);
                addNew.getContext().startActivity(intent);
            }
        });

        // This is for calculating the number of columns we can display on the device
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int numColumns = (int) (dpWidth / (120));

        // Create the recyclerview and give it the list of movies
        RecyclerView myrv = (RecyclerView) findViewById(R.id.recyclerview_id);
        RecyclerViewAdapter myadapter = new RecyclerViewAdapter(this, lstMovie);
        myrv.setLayoutManager(new GridLayoutManager(this, numColumns));
        myrv.setAdapter(myadapter);

    }

    // Copy DB method
    public void CopyDB(InputStream inputStream, OutputStream outputStream)
            throws IOException{
        //copy 1k bytes at a time
        byte[] buffer = new byte[1024];
        int length;
        while((length = inputStream.read(buffer)) > 0)
        {
            outputStream.write(buffer,0,length);
        }
        inputStream.close();
        outputStream.close();

    }//end method CopyDB

}
