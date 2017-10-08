package com.codepath.apps.restclienttemplate.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by ilyaseletsky on 10/7/17.
 */

public class TweetsPagerAdapter extends FragmentPagerAdapter {
    HomeTimelineFragment homeTimelineFragment;
    private String tabTitles[] = new String[] {"Home", "Mentions"};
    Context context;

    public TweetsPagerAdapter(FragmentManager fm,
                              Context context,
                              HomeTimelineFragment homeTimelineFragment) {
        super(fm);
        //Kindof a quick way to just have a reference to it
        this.homeTimelineFragment = homeTimelineFragment;
        this.context = context;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return homeTimelineFragment;
        }
        else if (position == 1) {
            return new MentionsTimelineFragment();
        }
        else {
            return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
