package com.realmcontacts.contacts;

import android.net.Uri;
import android.provider.ContactsContract;

/**
 * Created by Yuriy on 2016-03-09.
 */
public interface PhonesQuery {

    Uri CONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

    String SELECTION = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?" + " AND "
            + ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE;

    String[] PROJECTION = {
            ContactsContract.Data.CONTACT_ID,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Phone.TYPE,
    };

    int NUMBER = 1;
    int TYPE = 2;
}
