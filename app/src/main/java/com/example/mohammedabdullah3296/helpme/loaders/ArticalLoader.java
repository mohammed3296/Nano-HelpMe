package com.example.mohammedabdullah3296.helpme.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.example.mohammedabdullah3296.helpme.models.Article;
import com.example.mohammedabdullah3296.helpme.utils.QueryUtils;

import java.util.List;

/**
 * Created by Mohammed Abdullah on 9/10/2017.
 */


public class ArticalLoader extends AsyncTaskLoader<List<Article>> {

    /**
     * Tag for log messages
     */
    private static final String LOG_TAG = ArticalLoader.class.getName();

    /**
     * Query URL
     */
    private String mUrl;

    /**
     * Constructs a new {@link ArticalLoader}.
     *
     * @param context of the activity
     * @param url     to load data from
     */
    public ArticalLoader(Context context, String url) {
        super(context);
        mUrl = url;
        Log.w(LOG_TAG, "ArticalLoader consrructor");
    }

    @Override
    protected void onStartLoading() {
        Log.w(LOG_TAG, "onStartLoading forceLoad");
        forceLoad();

    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<Article> loadInBackground() {
        Log.w(LOG_TAG, "loadInBackground loadInBackground");

        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of articles.
        List<Article> articles = QueryUtils.fetcharticlesData(mUrl);
        return articles;
    }
}