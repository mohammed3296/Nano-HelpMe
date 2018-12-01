package com.example.mohammedabdullah3296.helpme.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.example.mohammedabdullah3296.helpme.models.Feed;
import com.example.mohammedabdullah3296.helpme.utils.QueryUtils;

import java.util.List;

/**
 * Created by Mohammed Abdullah on 9/10/2017.
 */


public class FeedLoader extends AsyncTaskLoader<List<Feed>> {

    /**
     * Tag for log messages
     */
    private static final String LOG_TAG = FeedLoader.class.getName();

    //private String mUrl;

    /**
     * Constructs a new {@link  FeedLoader}.
     *
     * @param context of the activity
     */
    public FeedLoader(Context context) {
        super(context);
        //    mUrl = url;
        //  Log.w(LOG_TAG, " FeedLoader consrructor");
    }

    @Override
    protected void onStartLoading() {
        // Log.w(LOG_TAG, "onStartLoading forceLoad");
        forceLoad();

    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<Feed> loadInBackground() {
        //   Log.w(LOG_TAG, "loadInBackground loadInBackground");

//        if (mUrl == null) {
//            return null;
//        }

        // Perform the network request, parse the response, and extract a list of  Feeds.
        List<Feed> Feeds = QueryUtils.fetchFeeds();
        return Feeds;
    }
}