package com.realmcontacts;

import android.accounts.Account;
import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.SyncInfo;
import android.content.SyncStatusObserver;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.google.common.base.Optional;
import com.realmcontacts.account.AuthenticatorHelper;
import com.realmcontacts.realm.model.Contacts;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import hugo.weaving.DebugLog;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private Optional<Realm> mRealm;
    private Object mSyncHandle;
    private SyncStatusObserver mSyncStatusObserver;
    private ContactsRecyclerAdapter mContactAdapter;

    @Bind(R.id.contacts_realm_recycler_view)
    RealmRecyclerView mRealmRecyclerView;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.toolbar_progress_bar)
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        mSyncStatusObserver = which -> runOnUiThread(() -> {
            Optional<Account> account = AuthenticatorHelper.getInstance().getOrCreateAccount(MainActivity.this);
            if (account.isPresent()) {
                boolean isSync = isSyncActive(account.get(), ContactsContract.AUTHORITY);
                mProgressBar.setVisibility(isSync ? View.VISIBLE : View.INVISIBLE);
            }
        });


        try {
            mRealm = Optional.of(Realm.getDefaultInstance());
            if (mRealm.isPresent()) {
                RealmResults<Contacts> realmResults = mRealm.get()
                        .where(Contacts.class)
                        .findAllSorted("name", Sort.ASCENDING);

                mContactAdapter = new ContactsRecyclerAdapter(this, realmResults, true, true, true,"name");
                mRealmRecyclerView.setAdapter(mContactAdapter);
            }

        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    @DebugLog
    private boolean isSyncActive(Account account, String authority) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            return isSyncActiveHoneycomb(account, authority);
        else {
            List<SyncInfo> currentSync = ContentResolver.getCurrentSyncs();
            return !currentSync.isEmpty() && currentSync.get(0).account.equals(account)
                    && currentSync.get(0).authority.equals(authority);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private static boolean isSyncActiveHoneycomb(Account account,
                                                 String authority) {
        for (SyncInfo syncInfo : ContentResolver.getCurrentSyncs()) {
            if (syncInfo.account.equals(account) &&
                    syncInfo.authority.equals(authority)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSyncStatusObserver.onStatusChanged(0);

        // Watch for synchronization status changes
        final int mask = ContentResolver.SYNC_OBSERVER_TYPE_PENDING |
                ContentResolver.SYNC_OBSERVER_TYPE_ACTIVE;
        mSyncHandle = ContentResolver.addStatusChangeListener(mask, mSyncStatusObserver);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mSyncHandle != null) {
            ContentResolver.removeStatusChangeListener(mSyncHandle);
            mSyncHandle = null;
        }
    }

    @Override
    protected void onDestroy() {
        if (mContactAdapter != null) mContactAdapter.close();
        if (mRealm.isPresent()) mRealm.get().close();
        mRealm = Optional.absent();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.refresh, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:

                Bundle bundle = new Bundle();
                bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
                bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
                ContentResolver.requestSync(null, ContactsContract.AUTHORITY, bundle);

                return true;
            case R.id.delete:
                clearRealm();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void clearRealm() {
        RealmResults<Contacts> results = mRealm.get().where(Contacts.class).findAll();
        mRealm.get().beginTransaction();
        for (int i = 0; i < results.size(); i++) {
            results.get(i).removeFromRealm();
        }
        results.clear();
        mRealm.get().commitTransaction();
    }

}
