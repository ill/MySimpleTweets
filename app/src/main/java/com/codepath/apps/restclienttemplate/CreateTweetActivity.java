package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class CreateTweetActivity extends AppCompatActivity {

    public static final int NEW_TWEET_REQUEST_CODE = 1337;
    public static final String NEW_TWEET_DATA_KEY = "newTweet";

    TextView etTweet;
    TwitterClient client;
    Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_tweet);

        client = TwitterApp.getRestClient();
        etTweet = (TextView) findViewById(R.id.etTweet);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
    }

    public String getTextToSend() {
        String tweetText = etTweet.getText().toString();

        tweetText.trim();

        return tweetText;
    }

    void onSuccessTweet(Tweet tweet) {
        Intent data = new Intent();
        data.putExtra(NEW_TWEET_DATA_KEY, tweet);

        setResult(RESULT_OK, data);
        finish();
    }

    void onFailureReset() {
        btnSubmit.setEnabled(true);
    }

    public void submitNewTweet(View view) {
        String tweetText = getTextToSend();

        if (tweetText.length() > 0) {
            btnSubmit.setEnabled(false);

            client.postTweet(tweetText, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        Tweet tweet = Tweet.fromJSON(response);
                        onSuccessTweet(tweet);
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Log.d("CreateTweet", responseString);
                    throwable.printStackTrace();
                    onFailureReset();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.d("CreateTweet", errorResponse.toString());
                    throwable.printStackTrace();
                    onFailureReset();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    Log.d("CreateTweet", errorResponse.toString());
                    throwable.printStackTrace();
                    onFailureReset();
                }
            });
        }
    }
}
