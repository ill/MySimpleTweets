package com.codepath.apps.restclienttemplate.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.restclienttemplate.EndlessRecyclerViewScrollListener;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TweetAdapter;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by ilyaseletsky on 10/7/17.
 */

public class TweetsListFragment extends Fragment implements TweetAdapter.TweetAdapterListener {
    public interface TweetSelectedListener {
        public void onTweetSelected(Tweet tweet);
    }

    static final int PAGE_SIZE = 15;
    private EndlessRecyclerViewScrollListener scrollListener;

    TweetAdapter tweetAdapter;
    ArrayList<Tweet> tweets;
    RecyclerView rvTweets;
    LinearLayoutManager linearLayoutManager;

    long currentMaxId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragments_tweet_list, container, false);

        currentMaxId = -1;

        rvTweets = (RecyclerView) v.findViewById(R.id.rvTweet);

        tweets = new ArrayList<>();
        tweetAdapter = new TweetAdapter(tweets, this);

        linearLayoutManager = new LinearLayoutManager(getContext());

        rvTweets.setLayoutManager(linearLayoutManager);
        rvTweets.setAdapter(tweetAdapter);

        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                //loadNextDataFromApi(page);
                loadPageDelayed(PAGE_SIZE, currentMaxId, 500);
            }
        };
        // Adds the scroll listener to RecyclerView
        rvTweets.addOnScrollListener(scrollListener);

        loadPageDelayed(PAGE_SIZE, -1, 500);

        return v;
    }

    protected final void loadPageDelayed(final int count, final long maxId, long delayMillis) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadPage(count, maxId);
            }
        }, delayMillis);
    }

    protected void loadPage(int count, long maxId) {

    }

    public void handleNewCreatedTweet(Tweet tweet) {
        linearLayoutManager.scrollToPosition(insertNewTweet(tweet));
    }

    int insertNewTweet(Tweet tweet) {
        int index = 0;
        for(Tweet currTweet : tweets) {
            if (currTweet.uid > tweet.uid) {
                ++index;
            }
        }

        tweets.add(index, tweet);
        tweetAdapter.notifyItemInserted(index);

        return index;
    }

    public void addItems(JSONArray response) {
        for (int i = 0; i < response.length(); i++) {
            try {
                Tweet tweet = Tweet.fromJSON(response.getJSONObject(i));
                tweets.add(tweet);

                tryUpdateMaxId(tweet.uid);

                tweetAdapter.notifyItemInserted(tweets.size() - 1);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    protected void tryUpdateMaxId(long newMaxId) {
        if (currentMaxId < 0 || newMaxId < currentMaxId) {
            currentMaxId = newMaxId - 1;
        }
    }

    @Override
    public void onItemSelected(View view, int position) {
        Tweet tweet = tweets.get(position);

        TweetSelectedListener tweetSelectedListener = (TweetSelectedListener) getActivity();
        tweetSelectedListener.onTweetSelected(tweet);
    }
}
