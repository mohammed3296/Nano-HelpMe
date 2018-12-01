package com.example.mohammedabdullah3296.helpme;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mohammedabdullah3296.helpme.adapters.ArticleAdapter;
import com.example.mohammedabdullah3296.helpme.adapters.ListArticalClickListener;
import com.example.mohammedabdullah3296.helpme.loaders.ArticalLoader;
import com.example.mohammedabdullah3296.helpme.models.Article;

import java.util.List;

public class NewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Article>>, ListArticalClickListener {

    public static int counter = 1;
    private static String urlpart1;
    private static String urlpart2;
    private static String urlType;
    private static int RECIPE_LOADER_ID = 1;
    private static String holeURL;
    private RecyclerView mRecyclerView;
    private ArticleAdapter mRecipestAdapter;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        Bundle bundle = getIntent().getExtras();
        urlpart1 = bundle.getString("newsURL1");
        urlpart2 = bundle.getString("newsURL2");
        urlType = bundle.getString("type");
        holeURL = urlpart1 + counter + urlpart2;
        getSupportActionBar().setTitle(urlType);
        mRecyclerView = (RecyclerView) findViewById(R.id.main_recipes);

        /* This TextView is used to display errors and will be hidden if there are no errors */
        mErrorMessageDisplay = (TextView) findViewById(R.id.empty_view);

        /*
         * LinearLayoutManager can support HORIZONTAL or VERTICAL orientations. The reverse layout
         * parameter is useful mostly for HORIZONTAL layouts that should reverse for right to left
         * languages.
         */
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            LinearLayoutManager layoutManager
                    = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            mRecyclerView.setLayoutManager(layoutManager);
            //  Toast.makeText(this, "ORIENTATION_PORTRAIT", Toast.LENGTH_SHORT).show();
        }
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            GridLayoutManager mLayoutManager = new GridLayoutManager(this, 2);
            mRecyclerView.setLayoutManager(mLayoutManager);
            //   Toast.makeText(this, "ORIENTATION_LANDSCAPE", Toast.LENGTH_SHORT).show();
        }
        if (isTablet(this)) {
            GridLayoutManager mLayoutManager = new GridLayoutManager(this, 3);
            mRecyclerView.setLayoutManager(mLayoutManager);
            // Toast.makeText(this, "isTablet", Toast.LENGTH_SHORT).show();
        }

        /*
         * Use this setting to improve performance if you know that changes in content do not
         * change the child layout size in the RecyclerView
         */
        mRecyclerView.setHasFixedSize(true);

        // COMPLETED (11) Pass in 'this' as the ForecastAdapterOnClickHandler
        /*
         * The ForecastAdapter is responsible for linking our weather data with the Views that
         * will end up displaying our weather data.
         */
        mRecipestAdapter = new ArticleAdapter(this);

        /* Setting the adapter attaches it to the RecyclerView in our layout. */
        mRecyclerView.setAdapter(mRecipestAdapter);


        mLoadingIndicator = (ProgressBar) findViewById(R.id.loading_indicator);
        callRecipesLoderAsyncTask();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.page_up, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.page_up) {

            holeURL = urlpart1 + ++counter + urlpart2;
            RECIPE_LOADER_ID = (int) (2 + (Math.random() * (999999999 - 2)));
            callRecipesLoderAsyncTask();
            return true;
        } else if (id == R.id.page_prev) {
            if (counter > 1) {
                holeURL = urlpart1 + --counter + urlpart2;
                RECIPE_LOADER_ID = (int) (2 + (Math.random() * (999999999 - 2)));
                callRecipesLoderAsyncTask();
            } else {
                Toast.makeText(this, R.string.first_page, Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean isTablet(Context context) {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }

    @Override
    public Loader<List<Article>> onCreateLoader(int id, Bundle args) {
        return new ArticalLoader(this, holeURL);
    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> data) {
        mLoadingIndicator.setVisibility(View.GONE);
        mRecipestAdapter.data.clear();
        mRecyclerView.getAdapter().notifyDataSetChanged();
        if (data != null && !data.isEmpty()) {
            showRecipeDataView();
            mRecipestAdapter.setArticalData(data, this);
        } else {
            mErrorMessageDisplay.setText(R.string.no_articales);
            showErrorMessage();
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {
        mRecipestAdapter.data.clear();
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onListItemClick(Article clickedItemIndex) {
        // Convert the String URL into a URI object (to pass into the Intent constructor)
        Uri BookUri = Uri.parse(clickedItemIndex.getUrl());

        // Create a new intent to view the Book URI
        Intent websiteIntent = new Intent(Intent.ACTION_VIEW, BookUri);

        // Send the intent to launch a new activity
        startActivity(websiteIntent);
    }

    @SuppressLint("SetTextI18n")
    public void callRecipesLoderAsyncTask() {
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            LoaderManager loaderManager = getSupportLoaderManager();
            loaderManager.initLoader(RECIPE_LOADER_ID, null, this);
        } else {
            mLoadingIndicator.setVisibility(View.GONE);
            mErrorMessageDisplay.setText(R.string.no_internet_con);
        }

    }

    private void showRecipeDataView() {
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
}
