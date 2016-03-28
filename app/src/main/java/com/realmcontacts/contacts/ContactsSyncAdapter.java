package com.realmcontacts.contacts;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;

/**
 * Created by Yuriy on 2016-03-09.
 */
public class ContactsSyncAdapter extends AbstractThreadedSyncAdapter {

    public ContactsSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority,
                              ContentProviderClient provider, SyncResult syncResult) {

//        ContactsHelper helper = new ContactsHelper();
//        helper.syncContacts();
    }
}
