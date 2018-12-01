package com.example.mohammedabdullah3296.helpme.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mohammedabdullah3296.helpme.ChildImageActivity;
import com.example.mohammedabdullah3296.helpme.CreatePostActivity;
import com.example.mohammedabdullah3296.helpme.ListItemClickListener;
import com.example.mohammedabdullah3296.helpme.R;
import com.example.mohammedabdullah3296.helpme.adapters.FeedAdapter;
import com.example.mohammedabdullah3296.helpme.loaders.FeedLoader;
import com.example.mohammedabdullah3296.helpme.models.Feed;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mohammed Abdullah on 11/11/2017.
 */

public class FeedsFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Feed>>, SwipeRefreshLayout.OnRefreshListener, ListItemClickListener {
    private static final int RECIPE_LOADER_ID = 1;
    Button button;
    SwipeRefreshLayout swipeLayout;
    private AdView mAdView;
    private RecyclerView mRecyclerView;
    private FeedAdapter mFeedstAdapter;
    private TextView mErrorMessageDisplay;

    private ProgressBar mLoadingIndicator;
    private List<Feed> feedsArray;
    public FeedsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.feeds_layout, container, false);
        //  button = rootView.findViewById(R.id.angry_btn);
        MobileAds.initialize(getContext(), getString(R.string.mobile_ads));
        mAdView = rootView.findViewById(R.id.ad_view);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        // Start loading the ad in the background.
        mAdView.loadAd(adRequest);
        mLoadingIndicator = (ProgressBar) rootView.findViewById(R.id.loading_indicator);
        swipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeColors(android.R.color.holo_green_dark,
                android.R.color.holo_red_dark,
                android.R.color.holo_blue_dark,
                android.R.color.holo_orange_dark);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getContext(), CreatePostActivity.class));
//            }
//        });
        mRecyclerView = rootView.findViewById(R.id.main_feeds);

        /* This TextView is used to display errors and will be hidden if there are no errors */
        mErrorMessageDisplay = (TextView) rootView.findViewById(R.id.empty_view);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        mFeedstAdapter = new FeedAdapter(this);
        mRecyclerView.setAdapter(mFeedstAdapter);
        feedsArray = new ArrayList<>();
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), CreatePostActivity.class));
            }
        });
        callFeedsLoderAsyncTask();

        return rootView;
    }

    @Override
    public Loader<List<Feed>> onCreateLoader(int id, Bundle args) {
        return new FeedLoader(getContext());
    }

    @Override
    public void onLoadFinished(Loader<List<Feed>> loader, List<Feed> data) {
        mLoadingIndicator.setVisibility(View.GONE);
        mFeedstAdapter.data.clear();
        mRecyclerView.getAdapter().notifyDataSetChanged();
        if (data != null && !data.isEmpty()) {
            showFeedDataView();
            mFeedstAdapter.setRecipeData(data, getContext());
            feedsArray.clear();
            feedsArray.addAll(data);
        } else {
            mErrorMessageDisplay.setText(R.string.no_feeds);
            showErrorMessage();
        }
        swipeLayout.setRefreshing(false);
    }

    @Override
    public void onLoaderReset(Loader<List<Feed>> loader) {
        mFeedstAdapter.data.clear();
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }


    public void callFeedsLoderAsyncTask() {
        ConnectivityManager cm =
                (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(RECIPE_LOADER_ID, null, this);
        } else {
            mLoadingIndicator.setVisibility(View.GONE);
            swipeLayout.setRefreshing(false);
            mErrorMessageDisplay.setText(R.string.no_internet_conn);

        }

    }

    private void showFeedDataView() {
        /* First, make sure the error is invisible */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the weather data is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        /* First, hide the currently visible data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRefresh() {
        ConnectivityManager cm =
                (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.

            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.restartLoader(RECIPE_LOADER_ID, null, this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            mLoadingIndicator.setVisibility(View.GONE);
            swipeLayout.setRefreshing(false);
            // Update empty state with no connection error message
            mErrorMessageDisplay.setText(R.string.no_internet_conn);

        }

    }

    @Override
    public void callButtonOnClick(View v, int selectedFeed) {
        // Toast.makeText(getContext(), "callButtonOnClick" + feedsArray.get(selectedFeed).getUserphone(), Toast.LENGTH_SHORT).show();
        Uri number = Uri.parse("tel:" + feedsArray.get(selectedFeed).getUserphone());
        Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
        startActivity(callIntent);
    }

    @Override
    public void emailButtonOnClick(View v, int selectedFeed) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{feedsArray.get(selectedFeed).getUsermail()}); // recipients
        intent.putExtra(Intent.EXTRA_SUBJECT, "Comment");
        intent.putExtra(Intent.EXTRA_TEXT, "");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(getString(R.string.pathemail)));
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    public void mapButtonOnClick(View v, int selectedFeed) {
        //   Toast.makeText(getContext(), "mapButtonOnClick" + feedsArray.get(selectedFeed).getLatitude(), Toast.LENGTH_SHORT).show();
        Uri uri = Uri.parse("geo:0,0?q=" + feedsArray.get(selectedFeed).getLatitude() + ","
                + feedsArray.get(selectedFeed).getLongitude() + "(" + feedsArray.get(selectedFeed).getPlacename() + ")");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    @Override
    public void shareButtonOnClick(View v, int selectedFeed) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,
                "Hi\n" +
                        getString(R.string.there_hgn) + feedsArray.get(selectedFeed).getChildname() +
                        getString(R.string.dfjghbdfjhsik) + feedsArray.get(selectedFeed).getChildage() + getString(R.string.fdgsdgrtdgth) +
                        getString(R.string.gfgh45fh45gf) + feedsArray.get(selectedFeed).getChildsex() + getString(R.string.fxgbfd25dfg) +
                        getString(R.string.fdjgdf54) + feedsArray.get(selectedFeed).getEyes() + getString(R.string.sdfgddfgs) +
                        getString(R.string.fjgdk5646) + feedsArray.get(selectedFeed).getHeight() + getString(R.string.fgfd252dg) +
                        getString(R.string.defg4) + feedsArray.get(selectedFeed).getWeight() + getString(R.string.fmngvmdfs65) +
                        getString(R.string.fdjgdf547) + feedsArray.get(selectedFeed).getHair() + "\n" +
                        getString(R.string.dfjsgdbf45) + feedsArray.get(selectedFeed).getPlacename() + "\n" +
                        getString(R.string.fjxbgvjkdfs65) + feedsArray.get(selectedFeed).getDescription() + "\n" +
                        getString(R.string.dfgjbskdmn656) + feedsArray.get(selectedFeed).getImage() + "\n" +
                        getString(R.string.dfbgdf25fbg) + feedsArray.get(selectedFeed).getUserphone());
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    @Override
    public void childImageOnClick(View v, int position) {
        Intent in = new Intent(getContext(), ChildImageActivity.class);
        in.putExtra("CHILDIMAGE", feedsArray.get(position).getImage());
        startActivity(in);
    }

    /**
     * Called when leaving the activity
     */
    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    /**
     * Called when returning to the activity
     */
    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    /**
     * Called before the activity is destroyed
     */
    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }
}
