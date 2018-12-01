package com.example.mohammedabdullah3296.helpme;

import android.view.View;

/**
 * Created by Mohammed Abdullah on 11/27/2017.
 */

public interface ListItemClickListener {
    void callButtonOnClick(View v, int position);

    void emailButtonOnClick(View v, int position);

    void mapButtonOnClick(View v, int position);

    void shareButtonOnClick(View v, int position);

    void childImageOnClick(View v, int position);
}