package com.realmcontacts.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.MenuRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.realmcontacts.R;

import butterknife.ButterKnife;

/**
 * Created by Yuriy on 2016-04-14 RealmContacts.
 */
public abstract class BaseFragment extends Fragment {

    public static final String ACTION_BACK_PRESSED = "ACTION_BACK_PRESSED";
    public Toolbar toolbar;
    protected LocalBroadcastManager broadcastManager;
    protected IntentFilter filter = new IntentFilter(ACTION_BACK_PRESSED);

    protected BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
   //         onReceiveBackPressed();
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isActivityNotNull())
            broadcastManager = LocalBroadcastManager.getInstance(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayout(), container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        setToolbar(view);
    }

    @Override
    public void onResume() {
        if (isBroadcastManagerNotNull())
            broadcastManager.registerReceiver(receiver, filter);
        super.onResume();
    }

    @Override
    public void onPause() {
        if (isBroadcastManagerNotNull())
            broadcastManager.unregisterReceiver(receiver);
        super.onPause();
    }

    private void setToolbar(View view) {
        if (!hasCustomToolbar()) return;
        toolbar = ButterKnife.findById(view, getToolbarId());
        toolbar.setTitle(getTitle());
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        toolbar.inflateMenu(getMenu());
    }

    protected
    @StringRes
    int getTitle() {
        return R.string.app_name;
    }

    protected
    @IdRes
    int getToolbarId() {
        return R.id.toolbar;
    }

    protected
    @MenuRes
    int getMenu() {
        return R.menu.empty_menu;
    }

    public boolean hasCustomToolbar() {
        return false;
    }

    protected abstract
    @LayoutRes
    int getLayout();

    private boolean isBroadcastManagerNotNull() {
        return broadcastManager != null;
    }

    protected boolean isActivityNotNull() {
        return getActivity() != null;
    }

 //   protected abstract void onReceiveBackPressed();
}