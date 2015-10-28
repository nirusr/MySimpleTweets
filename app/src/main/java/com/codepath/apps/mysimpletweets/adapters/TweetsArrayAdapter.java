package com.codepath.apps.mysimpletweets.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sgovind on 10/23/15.
 */
public class TweetsArrayAdapter extends RecyclerView.Adapter<TweetsArrayAdapter.ViewHolder> {


    public static class ViewHolder extends RecyclerView.ViewHolder {
        //Define screen fields
        TextView tvUsername ;
        TextView tvBody;
        ImageView ivProfileImage;
        TextView tvTimestamp;


        public ViewHolder(View itemView) {
            super(itemView);
            //set the fields in the view holder
            tvUsername = (TextView) itemView.findViewById(R.id.tvUsername);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvTimestamp = (TextView) itemView.findViewById(R.id.tvTimestamp);
        }
    }

    private ArrayList<Tweet> mTweets;

    public TweetsArrayAdapter(ArrayList<Tweet> tweets) {
        this.mTweets = tweets;
        //Log.d("#of tweets:", Integer.toString(getItemCount()));
    }


    @Override
    public TweetsArrayAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemTweetView  = inflater.inflate(R.layout.item_tweet, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemTweetView);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(TweetsArrayAdapter.ViewHolder holder, int position) {
        Tweet tweet = mTweets.get(position);
        holder.tvUsername.setText(tweet.getUser().getName());
        holder.tvBody.setText((tweet.getBody()));
        holder.tvTimestamp.setText(tweet.getCreatedAt());
        Picasso.with(holder.ivProfileImage.getContext()).
                load(tweet.getUser().getProfileImageUrl()).fit().centerCrop().into(holder.ivProfileImage);


    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    public void clear() {
        mTweets.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Tweet> list) {
        mTweets.addAll(list);
        notifyDataSetChanged();

    }

    public void insertAt(int position, Tweet tweet) {
        mTweets.add(position, tweet);
        notifyDataSetChanged();
    }
}
