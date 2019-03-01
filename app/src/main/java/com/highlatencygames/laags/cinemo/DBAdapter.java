package com.highlatencygames.laags.cinemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {
    public static final String KEY_ROWID = "_id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_DESC ="description";
    public static final String KEY_IMG ="image";
    public static final String KEY_RATING = "rating";
    public static final String KEY_VIDEO = "video";
    public static final String TAG = "DBAdapter";

    private static final String DATABASE_NAME = "MyDB";
    private static final String DATABASE_TABLE = "movies";
    private static final int DATABASE_VERSION = 2;

    private static final String DATABASE_CREATE =
            "create table movies(_id integer primary key autoincrement,"
                    + "title text not null,description text not null,image text not null,rating text not null, video text not null);";

    private final Context context;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBAdapter(Context ctx)
    {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context)
        {
            super(context,DATABASE_NAME,null,DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db)
        {
            try{
                db.execSQL(DATABASE_CREATE);
            }catch(SQLException e){
                e.printStackTrace();
            }
        }//end method onCreate

        public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion)
        {
            Log.w(TAG,"Upgrade database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS movies");
            onCreate(db);
        }//end method onUpgrade
    }

    //open the database
    public DBAdapter open() throws SQLException
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    //close the database
    public void close()
    {
        DBHelper.close();
    }

    //insert a movie into the database
    public long insertMovie(String title,String description, String image, String rating, String video)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TITLE, title);
        initialValues.put(KEY_DESC, description);
        initialValues.put(KEY_IMG, image);
        initialValues.put(KEY_RATING, rating);
        initialValues.put(KEY_VIDEO, video);
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    //delete a particular movie
    public boolean deleteMovie(long rowId)
    {
        return db.delete(DATABASE_TABLE,KEY_ROWID + "=" + rowId,null) >0;
    }

    //retrieve all the movies
    public Cursor getAllMovies()
    {
        return db.query(DATABASE_TABLE,new String[]{KEY_ROWID, KEY_TITLE,
                KEY_DESC, KEY_IMG, KEY_RATING, KEY_VIDEO},null,null,null,null,null);
    }

    //retrieve a single movie
    public Cursor getMovie(long rowId) throws SQLException
    {
        Cursor mCursor = db.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
                KEY_TITLE, KEY_DESC, KEY_IMG, KEY_RATING, KEY_VIDEO},KEY_ROWID + "=" + rowId,null,null,null,null,null);
        if(mCursor != null)
        {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    //updates a movie
    public boolean updateMovie(long rowId,String title,String desc,String image,String rating,String video)
    {
        ContentValues cval = new ContentValues();
        cval.put(KEY_TITLE, title);
        cval.put(KEY_DESC, desc);
        cval.put(KEY_IMG, image);
        cval.put(KEY_RATING, rating);
        cval.put(KEY_VIDEO, video);
        return db.update(DATABASE_TABLE, cval, KEY_ROWID + "=" + rowId,null) >0;
    }

    // Update the rating of a movie
    public boolean updateRating(long rowId, float rating)throws SQLException
    {
        // Gets the rating as a float from the RatingBar then converts it to a string for DB
        int i = Math.round(rating);
        String rate = Integer.toString(i);
        Cursor c = getMovie(rowId);
        c.moveToFirst();
        ContentValues cval = new ContentValues();
        cval.put(KEY_TITLE, c.getString(1));
        cval.put(KEY_DESC, c.getString(2));
        cval.put(KEY_IMG, c.getString(3));
        cval.put(KEY_RATING, rate);
        cval.put(KEY_VIDEO, c.getString(5));
        return db.update(DATABASE_TABLE, cval, KEY_ROWID + "=" + rowId,null) >0;
    }

}//end class DBAdapter