package com.example.mohammedabdullah3296.helpme.utils;

import android.text.TextUtils;
import android.util.Log;

import com.example.mohammedabdullah3296.helpme.models.Article;
import com.example.mohammedabdullah3296.helpme.models.Feed;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public final class QueryUtils {
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {
    }

    public static List<Feed> fetchFeeds() {
        final List<Feed> feeds = new ArrayList<>();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        database.child("posters").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                        Feed feed = noteDataSnapshot.getValue(Feed.class);
                        feeds.add(feed);
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return feeds;
    }

    public static List<Article> fetcharticlesData(String requestUrl) {
        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try {
            Log.e(LOG_TAG, url.toString());
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        List<Article> articles = extractUsersFromJson(jsonResponse);
        return articles;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();

            Log.e(LOG_TAG, "urlConnection = " + urlConnection.toString());
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the user JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }


    private static List<Article> extractUsersFromJson(String articalsJSON) {
        if (TextUtils.isEmpty(articalsJSON)) {
            return null;
        }

        List<Article> articles = new ArrayList<>();
        try {

            JSONObject baseJsonResponse = new JSONObject(articalsJSON);
            JSONObject jj = baseJsonResponse.getJSONObject("response");
            JSONArray articlesArray = jj.getJSONArray("results");
            // String message = baseJsonResponse.getString("message");
            if (articlesArray.length() != 0) {
                for (int i = 0; i < articlesArray.length(); i++) {
                    JSONObject jsonObject = articlesArray.getJSONObject(i);
                    JSONObject fields = jsonObject.getJSONObject("fields");
                    String sourceName = "No sourceName Found";
                    if (jsonObject.has("webUrl")) {
                        sourceName = jsonObject.getString("webUrl");
                    }
                    String title = "No title Found";
                    if (jsonObject.has("webTitle")) {
                        title = jsonObject.getString("webTitle");
                    }
                    String pillarName = "No pillar name";
                    if (jsonObject.has("pillarName")) {
                        pillarName = jsonObject.getString("pillarName");
                    }
                    String url = "No url ";
                    if (jsonObject.has("webUrl")) {
                        url = jsonObject.getString("webUrl");
                    }

                    String urlToImage = "No urlToImage ";
                    if (fields.has("thumbnail")) {
                        urlToImage = fields.getString("thumbnail");
                    }
                    String publishedAt = "No publishedAt ";
                    if (jsonObject.has("webPublicationDate")) {
                        publishedAt = jsonObject.getString("webPublicationDate");
                    }
                    articles.add(new Article(sourceName, title, pillarName, url, urlToImage, publishedAt));
                }
            } else {
                articles = null;
            }

        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the user JSON results", e);
        }
        return articles;
    }

}