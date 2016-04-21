package com.realmcontacts.utils;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.realmcontacts.RealmApp;

/**
 * Created by Yuriy on 2016-04-20 RealmContacts.
 */
public class Utils {

    public static Drawable getDrawable(int id) {
        return ContextCompat.getDrawable(RealmApp.getContext(), id);
    }

}
