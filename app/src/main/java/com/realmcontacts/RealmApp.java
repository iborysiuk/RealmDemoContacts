package com.realmcontacts;

import android.app.Application;
import android.content.Context;

import com.realmcontacts.account.AuthenticatorHelper;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Yuriy on 2016-03-27 RealmContacts.
 */
public class RealmApp extends Application {

    private static final String TAG = "RealmApp";
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        initRealm();
        AuthenticatorHelper.getInstance().authenticateAccount(this);
    }

    @Override
    public Context getApplicationContext() {
        return this;
    }

    public static Context getContext() {
        return context;
    }

    private void initRealm() {
        RealmConfiguration configuration = new RealmConfiguration.Builder(this)
                .name("realm.contacts")
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(configuration);
    }

}
