package com.realmcontacts.contacts;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;

import com.google.common.base.Optional;

import rx.Subscription;

/**
 * Created by Yuriy on 2016-03-09.
 */
public class ContactsSyncAdapter extends AbstractThreadedSyncAdapter {

    private Optional<Subscription> mContactsSub;

    public ContactsSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority,
                              ContentProviderClient provider, SyncResult syncResult) {

        mContactsSub = Optional.of(ContactsHelper.getInstance().syncContacts());
    }

    @Override
    public void onSyncCanceled() {
        if (mContactsSub.isPresent()) {
            mContactsSub.get().isUnsubscribed();
            mContactsSub = Optional.absent();
        }

        super.onSyncCanceled();
    }
}
