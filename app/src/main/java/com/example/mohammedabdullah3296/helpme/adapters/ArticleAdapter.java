package com.example.mohammedabdullah3296.helpme.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mohammedabdullah3296.helpme.R;
import com.example.mohammedabdullah3296.helpme.models.Article;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

/**
 * Created by Mohammed El_amary on 2/25/2018.
 */

public class ArticleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    final private ListArticalClickListener lOnClickListener;
    public List<Article> data = Collections.emptyList();
    Article current;
    int currentPos = 0;
    private Context mContext;
    private LayoutInflater inflater;

    public ArticleAdapter(ListArticalClickListener listener) {
        lOnClickListener = listener;
    }

    public void setArticalData(List<Article> articlesIn, Context context) {
        data = articlesIn;
        mContext = context;
        notifyDataSetChanged();
    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.list_item_article;

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, parent, false);
        ArticleAdapter.MyHolder viewHolder = new ArticleAdapter.MyHolder(view);

        return viewHolder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        // Get current position of item in recyclerview to bind data and assign values from list
        ArticleAdapter.MyHolder myHolder = (ArticleAdapter.MyHolder) holder;
        current = data.get(position);
        String imageUrl = current.getUrlToImage().toString();

        myHolder.articalTitle.setText(current.getTitle().toString());
        if (imageUrl != "") {
            Uri builtUri = Uri.parse(imageUrl).buildUpon().build();
            Picasso.with(mContext).load(builtUri).placeholder(R.drawable.arrow_bg2).into(((MyHolder) holder).articalImage);
        }


    }

    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView articalTitle;
        ImageView articalImage;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);

            articalTitle = (TextView) itemView.findViewById(R.id.article_title);
            articalImage = (ImageView) itemView.findViewById(R.id.thumbnail);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            lOnClickListener.onListItemClick(data.get(clickedPosition));
        }
    }
}
