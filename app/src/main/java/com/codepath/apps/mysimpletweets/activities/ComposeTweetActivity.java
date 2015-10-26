package com.codepath.apps.mysimpletweets.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.application.TwitterApplication;
import com.codepath.apps.mysimpletweets.client.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

public class ComposeTweetActivity extends AppCompatActivity {
    public TextView tvMyTweet ;
    public TwitterClient client;
    public static final int REQUEST_CODE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Context context = getApplicationContext();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_tweet);
        tvMyTweet = (TextView) findViewById(R.id.etMyTweet);

        final InputMethodManager imm =(InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);

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

    }

    private void postTweet() {

        String body = tvMyTweet.getText().toString();

        if ( body.length() > 0 ) {
            client.postTweet(body, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    //super.onSuccess(statusCode, headers, response);
                    Log.d("DEBUG:POST Success=", response.toString());
                    Toast.makeText(getApplicationContext(), "Successfully Posted", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    //super.onFailure(statusCode, headers, throwable, errorResponse);
                    Log.d("DEBUG:Pos Fail=", errorResponse.toString());
                }
            });
            tvMyTweet.setText("");
        }

    }

    public void btnTweetClicked(View v) {
        postTweet();
    }

    public void btnCancelClicked (View v) {
        tvMyTweet.setText("");
        Intent intent = new Intent();
        setResult(RESULT_OK);
        finish();

    }

}
