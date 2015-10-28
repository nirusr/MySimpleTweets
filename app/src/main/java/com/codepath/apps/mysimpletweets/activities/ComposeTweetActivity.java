package com.codepath.apps.mysimpletweets.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.application.TwitterApplication;
import com.codepath.apps.mysimpletweets.client.TwitterClient;
import com.codepath.apps.mysimpletweets.models.Credential;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.concurrent.ExecutionException;

public class ComposeTweetActivity extends AppCompatActivity {
    private static String s;
    public TextView tvMyTweet ;
    public TwitterClient client;
    public static final int REQUEST_CODE = 100;
    public TextView tvMyUserName;
    public ImageView ivMyImage;
    public Tweet myTweet;

    public static final String TWEET = "TWEET";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Context context = getApplicationContext();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_tweet);
        tvMyTweet = (TextView) findViewById(R.id.etMyTweet);
        tvMyUserName = (TextView) findViewById(R.id.tvMyUserName);
        ivMyImage = (ImageView) findViewById(R.id.ivMyImage);



        final InputMethodManager imm =(InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        //
        tvMyTweet.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    imm.showSoftInput(tvMyTweet, InputMethodManager.SHOW_IMPLICIT);

                } else {
                    imm.hideSoftInputFromInputMethod(tvMyTweet.getWindowToken(), 0);
                }
                imm.toggleSoftInput(0,0);
            }
        });


        client = TwitterApplication.getRestClient();

        setMyProfile();


    }


    public void btnTweetClicked(View v) {

        String body = tvMyTweet.getText().toString();


        if ( body.length() > 0 ) {
            client.postTweet(body, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Toast.makeText(getApplicationContext(), "Posting Success", Toast.LENGTH_SHORT).show();
                    Log.d("Response=", response.toString());
                    myTweet = new Tweet();
                    myTweet = Tweet.fromJson(response);
                    Log.d("body=", myTweet.getBody().toString());
                    Intent intent = new Intent();
                    intent.putExtra(TWEET, myTweet);
                    ComposeTweetActivity.this.setResult(RESULT_OK, intent);
                    ComposeTweetActivity.this.finish();

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    //super.onFailure(statusCode, headers, throwable, errorResponse);
                    Log.d("DEBUG:Pos Fail=", errorResponse.toString());
                    Toast.makeText(getApplicationContext(), "Posting failed", Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(this, "Empty Tweet can't be posted", Toast.LENGTH_SHORT).show();
        }

    }

   public void btnCancelClicked (View v) {
        tvMyTweet.setText("");
        Intent intent = new Intent();
        Tweet tweet = new Tweet();
        setResult(RESULT_OK);
        this.finish();

    }

    public void setMyProfile() {
        client.getVerifyCredentials(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Credential credential = Credential.fromJsonObject(response);
                if (credential != null) {
                    tvMyUserName.setText(credential.getMyName());
                    Picasso.with(getApplicationContext()).load(credential.getMyImageUrl()).fit().centerCrop().into(ivMyImage);
                }

            }
        });

    }



}
