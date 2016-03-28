package com.realmcontacts.contacts;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.provider.ContactsContract;

/**
 * Created by Yuriy on 2016-03-09.
 */
public interface ContactsQuery {

    Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;

    @SuppressLint("InlinedApi")
    String SELECTION = ContactsContract.Contacts.DISPLAY_NAME + "<>''" + " AND " +
            ContactsContract.Contacts.HAS_PHONE_NUMBER + "=1";

    @SuppressLint("InlinedApi")
    String SORT_ORDER = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";

    @SuppressLint("InlinedApi")
    String[] PROJECTION = {
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.LOOKUP_KEY,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.Contacts.PHOTO_URI,
            ContactsContract.Contacts.HAS_PHONE_NUMBER
    };

    int ID = 0;
    int LOOKUP_KEY = 1;
    int DISPLAY_NAME = 2;
    int PHOTO_URI = 3;
    int HAS_PHONE_NUMBER = 4;
}
