package com.realmcontacts;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.common.base.Optional;
import com.realmcontacts.contacts.ContactsHelper;
import com.realmcontacts.realm.model.Contacts;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Optional<Realm> mRealm;

    @Bind(R.id.contacts_realm_recycler_view)
    RealmRecyclerView mRealmRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        try {
            mRealm = Optional.of(Realm.getDefaultInstance());
            if (mRealm.isPresent()) {
                RealmResults<Contacts> realmResults = mRealm.get()
                        .where(Contacts.class)
                        .findAllSorted("name", Sort.ASCENDING);

                ContactsRecyclerAdapter adapter = new ContactsRecyclerAdapter(
                        this, realmResults, true, true, "name");
                mRealmRecyclerView.setAdapter(adapter);
            }

        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    @Override
    protected void onDestroy() {
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
                ContactsHelper.getInstance().syncContacts();
                return true;
            case R.id.delete:
                clearRealm();
                //         ContactsManager.getInstance().removeAllContacts();
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
