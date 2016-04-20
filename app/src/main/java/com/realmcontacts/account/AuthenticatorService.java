package com.realmcontacts.account;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Yuriy on 2016-03-27.
 */
public class AuthenticatorService extends Service {

    private static AuthenticatorImpl sAuthenticator = null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return intent.getAction().equals(AccountManager.ACTION_AUTHENTICATOR_INTENT)
                ? getAuthenticator().getIBinder()
                : null;

    }

    private synchronized AuthenticatorImpl getAuthenticator() {
        if (sAuthenticator == null)
            sAuthenticator = new AuthenticatorImpl(this);
        return sAuthenticator;
    }

    private static class AuthenticatorImpl extends AbstractAccountAuthenticator {

        public AuthenticatorImpl(Context context) {
            super(context);
        }

        @Override
        public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
            return null;
        }

        @Override
        public Bundle addAccount(AccountAuthenticatorResponse response, String accountType, String authTokenType, String[] requiredFeatures, Bundle options) throws NetworkErrorException {
            return null;
        }

        @Override
        public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account account, Bundle options) throws NetworkErrorException {
            return null;
        }

        @Override
        public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {

//            ContentResolver.setIsSyncable(account, ContactsContract.AUTHORITY, 1);
//            if (!ContentResolver.getSyncAutomatically(account, ContactsContract.AUTHORITY))
//                ContentResolver.addPeriodicSync(account, ContactsContract.AUTHORITY, Bundle.EMPTY, 90);
//            ContentResolver.setSyncAutomatically(account, ContactsContract.AUTHORITY, true);
//            Log.d(TAG, "SyncAutomatically");

            return null;
        }

        @Override
        public String getAuthTokenLabel(String authTokenType) {
            return null;
        }

        @Override
        public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
            return null;
        }

        @Override
        public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account, String[] features) throws NetworkErrorException {
            return null;
        }
    }
}
