package com.example.mohammedabdullah3296.helpme.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mohammedabdullah3296.helpme.ListItemClickListener;
import com.example.mohammedabdullah3296.helpme.R;
import com.example.mohammedabdullah3296.helpme.models.Feed;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

/**
 * Created by Mohammed El_amary on 1/31/2018.
 */

public class FeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    final private ListItemClickListener lOnClickListener;
    public List<Feed> data = Collections.emptyList();
    Feed current;
    int currentPos = 0;
    private Context mContext;
    private LayoutInflater inflater;

    public FeedAdapter(ListItemClickListener listener) {
        lOnClickListener = listener;
    }

    public void setRecipeData(List<Feed> feedsIn, Context context) {
        data = feedsIn;
        mContext = context;
        notifyDataSetChanged();
    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.feed_item;

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, parent, false);
        MyHolder viewHolder = new MyHolder(view);

        return viewHolder;
    }

    // Bind data
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        // Get current position of item in recyclerview to bind data and assign values from list
        MyHolder myHolder = (MyHolder) holder;
        final int position1 = position;
        current = data.get(position);
        String imageUrl = current.getImage().toString();

        myHolder.userName.setText(current.getUsername().toString());
        // Converting timestamp into x ago format
        CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                Long.parseLong(current.getDatetime()),
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
        myHolder.timestamp.setText(timeAgo);

        myHolder.childName.setText(current.getChildname());
        myHolder.ChildAge.setText(current.getChildage());
        myHolder.childHeight.setText(current.getHeight() + "m");
        myHolder.childWeight.setText(current.getWeight() + "kg");
        myHolder.childGender.setText(current.getChildsex());
        myHolder.childHair.setText(current.getHair());
        myHolder.childEyes.setText(current.getEyes());
        myHolder.childAddress.setText(current.getPlacename());
        myHolder.childDescription.setText(current.getDescription());
        // myHolder.userImage.setText(current.getName().toString());
        if (imageUrl != "") {
            Uri builtUri = Uri.parse(imageUrl).buildUpon().build();
            Picasso.with(mContext).load(builtUri).placeholder(R.drawable.arrow_bg2).into(((MyHolder) holder).childImage);
        }

        myHolder.callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lOnClickListener.callButtonOnClick(v, position1);

            }
        });
        myHolder.emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lOnClickListener.emailButtonOnClick(v, position1);
            }
        });
        myHolder.mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lOnClickListener.mapButtonOnClick(v, position1);
            }
        });
        myHolder.shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lOnClickListener.shareButtonOnClick(v, position1);
            }
        });

        myHolder.childImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lOnClickListener.childImageOnClick(v, position1);
            }
        });

    }

    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        TextView userName, timestamp, childName, ChildAge, childHeight;
        TextView childWeight, childGender, childHair, childEyes, childAddress, childDescription;
        de.hdodenhof.circleimageview.CircleImageView userImage;
        ImageView childImage;
        Button callButton, emailButton, mapButton, shareButton;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);

            userName = (TextView) itemView.findViewById(R.id.user__name);
            timestamp = (TextView) itemView.findViewById(R.id.timestamp);
            childName = (TextView) itemView.findViewById(R.id.child__name);
            ChildAge = (TextView) itemView.findViewById(R.id.child__age);
            childHeight = (TextView) itemView.findViewById(R.id.child__height);
            childWeight = (TextView) itemView.findViewById(R.id.child__weight);
            childGender = (TextView) itemView.findViewById(R.id.child__gender);
            childHair = (TextView) itemView.findViewById(R.id.child__hair);
            childEyes = (TextView) itemView.findViewById(R.id.child__eyes);
            childAddress = (TextView) itemView.findViewById(R.id.child__address);
            childDescription = (TextView) itemView.findViewById(R.id.child__description);
            userImage = (de.hdodenhof.circleimageview.CircleImageView) itemView.findViewById(R.id.profilePic);
            childImage = (ImageView) itemView.findViewById(R.id.feedImage1);
            callButton = (Button) itemView.findViewById(R.id.call_button);
            emailButton = (Button) itemView.findViewById(R.id.email_button);
            mapButton = (Button) itemView.findViewById(R.id.map_button);
            shareButton = (Button) itemView.findViewById(R.id.share_button);
        }
    }
}