package com.realmcontacts.account;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.Context;

import com.google.common.base.Optional;
import com.realmcontacts.RealmApp;

import java.io.IOException;

/**
 * Created by Yuriy on 2016-03-27 RealmContacts.
 */
public class AuthenticatorHelper {

    public static final String ACCOUNT_TYPE = "com.realmcontacts.account";
    private static final String ACCOUNT_NAME = "RealmContacts";

    private AuthenticatorHelper() {
    }

    public Optional<Account> getOrCreateAccount(Context context) {
        AccountManager accountManager = AccountManager.get(context);
        Account[] accounts = accountManager.getAccountsByType(ACCOUNT_TYPE);
        return accounts.length == 0 ? Optional.absent() : Optional.of(accounts[0]);
    }

    public void authenticateAccount(Context context) {
        Optional<Account> account = getOrCreateAccount(context);
        if (!account.isPresent())
            AccountManager.get(RealmApp.getContext())
                    .addAccountExplicitly(new Account(ACCOUNT_NAME, ACCOUNT_TYPE), null, null);
        else {
            new Thread(() -> {
                try {
                    AccountManager.get(RealmApp.getContext()).blockingGetAuthToken(account.get(), ACCOUNT_TYPE, true);
                } catch (OperationCanceledException | IOException | AuthenticatorException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    public static AuthenticatorHelper getInstance() {
        return AccountAuthenticatorHelperHolder.instance;
    }

    private static class AccountAuthenticatorHelperHolder {
        public static final AuthenticatorHelper instance = new AuthenticatorHelper();
    }
}
