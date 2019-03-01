package com.highlatencygames.laags.cinemo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private Context mContext;
    private List<Movie> mData;
    private Animation slideRightAnim, slideLeftAnim;

    // Takes a list of movies
    public RecyclerViewAdapter(Context mContext, List<Movie> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    // Inflate the view
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        slideRightAnim = AnimationUtils.loadAnimation(mContext, R.anim.frommiddle);
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardview_item_movie, parent, false);
        view.startAnimation(slideRightAnim);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // Set the movie title
        holder.tv_movie_title.setText(mData.get(position).getTitle());

        // Checking if thumbnail is coming from resources or filepath
        File imgFile = null;
        try {
             imgFile = new File(mData.get(position).getImgName());
        }catch(Exception e){}
        if (imgFile != null) {
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            holder.img_movie_thumbnail.setImageBitmap(myBitmap);
        } else {
            holder.img_movie_thumbnail.setImageResource(mData.get(position).getThumbnail());
        }


        // Set click listener
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, Movie_Activity.class);

                // Passing data to the movie page
                intent.putExtra("Id", mData.get(position).getId());
                intent.putExtra("Title", mData.get(position).getTitle());
                intent.putExtra("Description", mData.get(position).getDescription());
                intent.putExtra("Thumbnail", mData.get(position).getThumbnail());
                intent.putExtra("Rating", mData.get(position).getRating());
                intent.putExtra("VideoName", mData.get(position).getVideoName());
                intent.putExtra("ImgName", mData.get(position).getImgName());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_movie_title;
        ImageView img_movie_thumbnail;
        CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_movie_title = (TextView) itemView.findViewById(R.id.movie_title_id);
            img_movie_thumbnail = (ImageView) itemView.findViewById(R.id.movie_img_id);
            cardView = (CardView) itemView.findViewById(R.id.cardview_id);
        }
    }
}
