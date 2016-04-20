package com.realmcontacts.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.realmcontacts.R;
import com.realmcontacts.adapters.PageAdapter;
import com.realmcontacts.model.Contact;

import butterknife.Bind;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Yuriy on 2016-04-14 RealmContacts.
 */
public class MainFragment extends BaseFragment {

    private static final String TAG = "MainFragment";

    @Bind(R.id.contact_tab_layout)
    TabLayout mTabLayout;
    @Bind(R.id.contact_view_pager)
    ViewPager mViewPager;

    public MainFragment() {
    }

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_main;
    }

    @Override
    protected int getToolbarId() {
        return R.id.contact_toolbar;
    }

    @Override
    public boolean hasCustomToolbar() {
        return true;
    }

    @Override
    protected int getMenu() {
        return R.menu.main_menu;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViewPager();
        setupMenu();
    }

    private void setupMenu() {
        if (toolbar != null) {
            toolbar.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.delete) deleteContacts();
                return false;
            });
        }
    }

    private void setupViewPager() {
        PageAdapter pageAdapter = new PageAdapter(getChildFragmentManager());
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(pageAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void deleteContacts() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Contact> results = realm.where(Contact.class).findAll();
        if (!results.isEmpty()) {
            try {
                realm.beginTransaction();
                for (int i = 0; i < results.size(); i++) {
                    results.get(i).removeFromRealm();
                }
                results.clear();
                realm.commitTransaction();
            } catch (Exception e) {
                realm.cancelTransaction();
                Log.e(TAG, e.getMessage(), e);
            }
        }
        realm.close();
    }


}
