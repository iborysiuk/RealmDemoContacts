package com.realmcontacts.utils;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.realmcontacts.R;

/**
 * Created by accou on 8/24/2015
 */
public class Navigator {
    @NonNull
    protected final FragmentManager mFragmentManager;

    @IdRes
    protected final int mDefaultContainer;


    public Navigator(@NonNull final FragmentManager fragmentManager, @IdRes final int defaultContainer) {
        mFragmentManager = fragmentManager;
        mDefaultContainer = defaultContainer;
    }

    public FragmentManager getFragmentManager() {
        return mFragmentManager;
    }

    public Fragment getActiveFragment() {
        if (mFragmentManager.getBackStackEntryCount() == 0) {
            return null;
        }
        String tag = mFragmentManager
                .getBackStackEntryAt(mFragmentManager.getBackStackEntryCount() - 1).getName();
        return mFragmentManager.findFragmentByTag(tag);
    }

    public void goTo(final Fragment fragment) {
        mFragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(mDefaultContainer, fragment, getName(fragment))
                .commit();
        mFragmentManager.executePendingTransactions();
    }

    protected String getName(final Fragment fragment) {
        return fragment.getClass().getSimpleName();
    }

    public void setRootFragment(final Fragment startFragment) {
        if (getSize() > 0) {
            this.clearHistory();
        }
        this.replaceFragment(startFragment);
    }

    private void replaceFragment(final Fragment fragment) {
        mFragmentManager.beginTransaction()
                .replace(mDefaultContainer, fragment, getName(fragment))
                .commit();
        mFragmentManager.executePendingTransactions();
    }

    public void removeFragment(final Fragment fragment) {
        mFragmentManager.beginTransaction()
                .remove(fragment)
                .commit();
        mFragmentManager.popBackStack();
    }

    public void show(String tag) {
        Fragment fragment = mFragmentManager.findFragmentByTag(tag);
        mFragmentManager.beginTransaction()
                .show(fragment)
                .commit();
    }

    public void goOneBack() {
        mFragmentManager.popBackStackImmediate();
    }

    public void hideAndShow(Fragment hideFragment, String showTag){
        Fragment fragment = mFragmentManager.findFragmentByTag(showTag);
        mFragmentManager.beginTransaction()
                .hide(hideFragment)
                .show(fragment)
                .commit();
    }


    public void removeAndShow(Fragment removeFragment, String showTag){
        Fragment fragment = mFragmentManager.findFragmentByTag(showTag);
        mFragmentManager.beginTransaction()
                .remove(removeFragment)
                .show(fragment)
                .commit();
    }


    public void hideAndShow(Fragment hideFragment, Fragment fragment){
        mFragmentManager.beginTransaction()
                .add(R.id.container, fragment, getName(fragment))
                .hide(hideFragment)
                .show(fragment)
                .commit();
    }


    public void removeAndShow(Fragment hideFragment, Fragment fragment){
        mFragmentManager.beginTransaction()
                .add(R.id.container, fragment, getName(fragment))
                .remove(hideFragment)
                .show(fragment)
                .commit();
    }

    public void hideAddBackStackAndShow(Fragment hideFragment, Fragment showFragment){
        mFragmentManager.beginTransaction()
                .addToBackStack(getName(hideFragment))
                .add(mDefaultContainer, hideFragment,getName(hideFragment))
                .hide(hideFragment)
                .show(showFragment)
                .commit();

    }


    public void goBack() {mFragmentManager.popBackStack();}


    public int getSize() {
        return mFragmentManager.getBackStackEntryCount();
    }


    public boolean isEmpty() {
        return getSize() == 0;
    }


    public void gotToTheRootFragmentBack() {
        for (int i = 0; i <= mFragmentManager.getBackStackEntryCount(); ++i) {
            goOneBack();
        }
    }

    public Fragment getFragmentByTag(String fragmentName) {
        return getFragmentManager().findFragmentByTag(fragmentName);
    }

    public void clearHistory() {
        //noinspection StatementWithEmptyBody - it works as wanted
        while (mFragmentManager.popBackStackImmediate()) ;
    }
}
