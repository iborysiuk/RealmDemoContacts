package com.realmcontacts.account;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;

import com.google.common.base.Optional;
import com.realmcontacts.RealmApp;

/**
 * Created by Yuriy on 2016-03-27 RealmContacts.
 */
public class AuthenticatorHelper {

    public static final String ACCOUNT_TYPE = "com.realmcontacts.account";
    private static final String ACCOUNT_NAME = "RealmContacts";

    private AuthenticatorHelper() {
    }

    private Optional<Account> getOrCreateAccount(Context context) {
        AccountManager accountManager = AccountManager.get(context);
        Account[] accounts = accountManager.getAccountsByType(ACCOUNT_TYPE);
        return accounts.length == 0 ? Optional.absent() : Optional.of(accounts[0]);
    }

    public void authenticateAccount(Context context) {
        Optional<Account> account = getOrCreateAccount(context);
        if (!account.isPresent())
            AccountManager.get(RealmApp.getContext())
                    .addAccountExplicitly(new Account(ACCOUNT_NAME, ACCOUNT_TYPE), null, null);
    }

    public void manualContactsSync() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(null, ContactsContract.AUTHORITY, bundle);
    }

    public static AuthenticatorHelper getInstance() {
        return AccountAuthenticatorHelperHolder.instance;
    }

    private static class AccountAuthenticatorHelperHolder {
        public static final AuthenticatorHelper instance = new AuthenticatorHelper();
    }
}
