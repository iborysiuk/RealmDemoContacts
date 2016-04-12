package com.realmcontacts.contacts;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Yuriy on 2016-03-09.
 */
public class ContactsSyncAdapterService extends Service {
    private static ContactsSyncAdapter sSyncAdapter;

    @Override
    public void onCreate() {
        super.onCreate();
        if (sSyncAdapter == null) sSyncAdapter = new ContactsSyncAdapter(this, true);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return sSyncAdapter.getSyncAdapterBinder();
    }

}
