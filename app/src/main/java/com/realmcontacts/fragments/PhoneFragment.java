package com.realmcontacts.fragments;

import android.content.ContentResolver;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.common.base.Optional;
import com.realmcontacts.R;
import com.realmcontacts.adapters.PhonesRecyclerAdapter;
import com.realmcontacts.model.Contact;
import com.realmcontacts.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Yuriy on 2016-04-14 RealmContacts.
 */
public class PhoneFragment extends BaseFragment {

    private static final String TAG = "PhoneFragment";

    @Bind(R.id.phone_realm_recycler_view)
    RealmRecyclerView realmRecyclerView;

    private Optional<Realm> realm;
    private PhonesRecyclerAdapter phonesAdapter;

    @Override
    protected int getLayout() {
        return R.layout.fragment_tab_phone;
    }

    public PhoneFragment() {
    }

    public static PhoneFragment newInstance() {
        return new PhoneFragment();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupListView();
    }

    private void setupListView() {
        try {
            if (isActivityNotNull()) {
                realm = Optional.of(Realm.getDefaultInstance());
                if (realm.isPresent()) {
                    RealmResults<Contact> realmResults = realm.get()
                            .where(Contact.class)
                            .findAllSorted("name", Sort.ASCENDING);

                    phonesAdapter = new PhonesRecyclerAdapter(getActivity(), realmResults, true, true, true, "name");
                    realmRecyclerView.setAdapter(phonesAdapter);
                    realmRecyclerView.setOnRefreshListener(this::manualSyncContacts);
                    phonesAdapter.setUpdatedListener((boolean isUpdated) -> {
                        realmRecyclerView.setRefreshing(!isUpdated);
                    });
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (phonesAdapter != null) phonesAdapter.close();
        if (realm.isPresent()) realm.get().close();
        realm = Optional.absent();
        if (isActivityNotNull()) Picasso.with(getActivity()).cancelTag(this);
    }

    private void manualSyncContacts() {
        if (isActivityNotNull())
            if (NetworkUtils.isOnline(getActivity())) {
                Bundle bundle = new Bundle();
                bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
                bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
                ContentResolver.requestSync(null, ContactsContract.AUTHORITY, bundle);
            } else {
                Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_SHORT).show();
                realmRecyclerView.setRefreshing(false);
            }
    }

}
