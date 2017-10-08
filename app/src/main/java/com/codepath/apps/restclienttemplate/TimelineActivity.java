package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.restclienttemplate.fragments.TweetsListFragment;
import com.codepath.apps.restclienttemplate.fragments.TweetsPagerAdapter;
import com.codepath.apps.restclienttemplate.models.Tweet;

import static com.codepath.apps.restclienttemplate.ProfileActivity.USER_KEY;

public class TimelineActivity extends AppCompatActivity implements TweetsListFragment.TweetSelectedListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        ViewPager vpPager = (ViewPager) findViewById(R.id.viewpager);
        vpPager.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager(), this));
        TabLayout tabLayout = (TabLayout)findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(vpPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timeline, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btnTweet:
                newTweet();
                break;

            case R.id.miProfile:
                showProfile();
                break;

            default:
                break;
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == CreateTweetActivity.NEW_TWEET_REQUEST_CODE) {
            // Extract name value from result extras
            Tweet tweet = (Tweet) data.getExtras().getSerializable(CreateTweetActivity.NEW_TWEET_DATA_KEY);

            //TODO: Tell Home Timeline
            //tweetsListFragment.handleNewCreatedTweet(tweet);
        }
    }

    void newTweet() {
        Intent intent = new Intent(getApplicationContext(), CreateTweetActivity.class);

        startActivityForResult(intent, CreateTweetActivity.NEW_TWEET_REQUEST_CODE);
    }

    void showProfile() {
        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);

        startActivity(intent);
    }

    @Override
    public void onTweetSelected(Tweet tweet) {
        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
        intent.putExtra(USER_KEY, tweet.user);

        startActivity(intent);
    }
}
