package com.realmcontacts.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.realmcontacts.fragments.ContactFragment;
import com.realmcontacts.fragments.PhoneFragment;

/**
 * Created by Yuriy on 2016-04-14 RealmContacts.
 */
public class PageAdapter extends SmartFragmentStatePageAdapter {

    public static final int NUM_ITEMS = 2;

    public PageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return ContactFragment.newInstance();
            case 1:
                return PhoneFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Contacts";
            case 1:
                return "Phone";
            default:
                return "";
        }
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

}
