package com.example.mohammedabdullah3296.helpme.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.mohammedabdullah3296.helpme.R;
import com.example.mohammedabdullah3296.helpme.fragments.FeedsFragment;

public class CategoryAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public CategoryAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {

        return new FeedsFragment();

    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getString(R.string.category_feeds);
    }
}
