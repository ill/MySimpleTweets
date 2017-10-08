package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.fragments.TweetsListFragment;
import com.codepath.apps.restclienttemplate.fragments.UserTimelineFragment;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.codepath.apps.restclienttemplate.fragments.UserTimelineFragment.SCREEN_NAME_KEY;

public class ProfileActivity extends AppCompatActivity implements TweetsListFragment.TweetSelectedListener {
    public static final String USER_KEY = "user";

    TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        User user = (User) getIntent().getSerializableExtra(USER_KEY);

        String screenName = user != null
                ? user.screenName
                : getIntent().getStringExtra(SCREEN_NAME_KEY);

        UserTimelineFragment userTimelineFragment = UserTimelineFragment.newInstance(screenName);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        ft.replace(R.id.flContainer, userTimelineFragment);
        ft.commit();

        client = TwitterApp.getRestClient();

        if (user != null) {
            populateUserHeader(user);
        }
        else {
            client.getUserInfo(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        User user = User.fromJSON(response);

                        getSupportActionBar().setTitle(user.screenName);
                        populateUserHeader(user);
                    }
                    catch (JSONException e) {
                        e. printStackTrace();
                    }
                }
            });
        }
    }

    public void populateUserHeader(User user) {
        TextView tvName = (TextView)findViewById(R.id.tvName);
        TextView tvTagline = (TextView)findViewById(R.id.tvTagline);
        TextView tvFollowers = (TextView)findViewById(R.id.tvFollowers);
        TextView tvFollowing = (TextView)findViewById(R.id.tvFollowing);

        ImageView ivProfileImage = (ImageView)findViewById(R.id.ivProfileImage);
        tvName.setText(user.name);
        tvTagline.setText(user.tagline);
        tvFollowers.setText(user.followersCount + " Followers");
        tvFollowers.setText(user.followingCount + " Following");

        Glide.with(this).load(user.profileImageUrl).into(ivProfileImage);
    }

    @Override
    public void onTweetSelected(Tweet tweet) {
        //Normally I wouldn't open the profile for the person who's profile you're looking at, but just to be safe and meet the requirements, do it...

        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
        intent.putExtra(USER_KEY, tweet.user);

        startActivity(intent);
    }
}
