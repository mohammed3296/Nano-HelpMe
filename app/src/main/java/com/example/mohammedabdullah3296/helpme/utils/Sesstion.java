package com.example.mohammedabdullah3296.helpme.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.mohammedabdullah3296.helpme.LoginActivity;
import com.example.mohammedabdullah3296.helpme.models.User;

/**
 * Created by Mohammed Abdullah on 11/13/2017.
 */

public class Sesstion {
    private static final String SHARED_PREF_NAME = "HelpMeSesstion";
    private static final String KEY_ID = "keyid";
    private static final String KEY_EMAIL = "keyemail";
    private static final String KEY_PHONE = "keyphone";
    private static final String KEY_FIRSTNAME = "keyfirstname";
    private static final String KEY_SECONDNAME = "keysecondname";
    private static final String KEY_PROFILEIMAGE = "KEY_PROFILEIMAGE";


    private static Sesstion mInstance;
    private static Context mCtx;

    private Sesstion(Context context) {
        mCtx = context;
    }

    public static synchronized Sesstion getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new Sesstion(context);
        }
        return mInstance;
    }

    public void userLogin(User user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_ID, user.getID());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putString(KEY_PHONE, user.getPhone());
        editor.putString(KEY_FIRSTNAME, user.getFirstName());
        editor.putString(KEY_SECONDNAME, user.getSecondName());
        editor.putString(KEY_PROFILEIMAGE, user.getProfileImage());
        editor.apply();
    }

    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_EMAIL, null) != null;
    }

    public User getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getString(KEY_ID, null),
                sharedPreferences.getString(KEY_FIRSTNAME, null),
                sharedPreferences.getString(KEY_SECONDNAME, null),
                sharedPreferences.getString(KEY_EMAIL, null),
                sharedPreferences.getString(KEY_PHONE, null),
                sharedPreferences.getString(KEY_PROFILEIMAGE, null)
        );
    }

    public void editProfileUser(String profileImage) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_PROFILEIMAGE, profileImage);
        editor.apply();
    }

    public void editPhoneUser(String userPhone) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_PHONE, userPhone);
        editor.apply();
    }

    public void logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        mCtx.startActivity(new Intent(mCtx, LoginActivity.class));
    }
}
