package com.example.mohammedabdullah3296.helpme.utils;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.text.format.DateUtils;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.mohammedabdullah3296.helpme.NewsAppWidget;
import com.example.mohammedabdullah3296.helpme.R;
import com.example.mohammedabdullah3296.helpme.models.Article;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class WidgetCollectionService extends RemoteViewsService {
    private List<Article> mDataSet;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {
            @Override
            public void onCreate() {

            }

            @Override
            public void onDataSetChanged() {
                final long identityToken = Binder.clearCallingIdentity();
                // CharSequence widgetText = NewsAppWidgetConfigureActivity.loadTitlePref(getApplicationContext(), 2);
                ArticleAsyncTask appWidgetId = new ArticleAsyncTask();
                appWidgetId.execute(NewsAppWidget.url.toString());
                Binder.restoreCallingIdentity(identityToken);
            }

            @Override
            public void onDestroy() {
                mDataSet = null;
            }

            @Override
            public int getCount() {
                return mDataSet == null ? 0 : mDataSet.size();
            }

            @Override
            public RemoteViews getViewAt(int i) {
                RemoteViews remoteViews = new RemoteViews(WidgetCollectionService.this.getPackageName(),
                        R.layout.widget_item_list);

                String title = mDataSet.get(i).getTitle().equalsIgnoreCase("null") ? "" :
                        mDataSet.get(i).getTitle();

                String author = "";
                if (mDataSet.get(i) != null && mDataSet.get(i).getDescription() != null) {
                    author = mDataSet.get(i).getDescription().equalsIgnoreCase("null") ? "" :
                            mDataSet.get(i).getDescription();
                    author = author.isEmpty() ? "" : ", Type: " + author;
                }
                String publishAt = "";
                if (mDataSet.get(i) != null && mDataSet.get(i).getPublishedAt() != null &&
                        !mDataSet.get(i).getPublishedAt().equalsIgnoreCase("null"))
                    publishAt = mDataSet.get(i).getPublishedAt();

                remoteViews.setTextViewText(R.id.article_title, title);
                remoteViews.setTextViewText(R.id.article_subtitle, manipulateDateFormat(publishAt) + author);
                return remoteViews;
            }

            public String manipulateDateFormat(String post_date) {

                SimpleDateFormat existingUTCFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                //SimpleDateFormat requiredFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


                Date date = null;
                try {
                    date = existingUTCFormat.parse(post_date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (date != null) {
                    // Converting timestamp into x ago format
                    CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                            Long.parseLong(String.valueOf(date.getTime())),
                            System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
                    return timeAgo + "";
                } else {
                    return post_date;
                }
            }

            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.widget_item_list);
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int i) {
                return 0;
            }

            @Override
            public boolean hasStableIds() {
                return false;
            }
        };
    }

    private class ArticleAsyncTask extends AsyncTask<String, Void, List<Article>> {
        @Override
        protected List<Article> doInBackground(String... urls) {
            // Don't perform the request if there are no URLs, or the first URL is null.
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }
            List<Article> result = QueryUtils.fetcharticlesData(urls[0]);
            return result;
        }

        @Override
        protected void onPostExecute(List<Article> data) {

            if (data != null && !data.isEmpty()) {
                mDataSet = data;
            }
        }
    }
}
