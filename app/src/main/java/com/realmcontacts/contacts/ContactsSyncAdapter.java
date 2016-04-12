package com.realmcontacts.contacts;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import com.realmcontacts.realm.model.Contacts;
import com.realmcontacts.realm.model.Number;

import hugo.weaving.DebugLog;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by Yuriy on 2016-03-09.
 */
public class ContactsSyncAdapter extends AbstractThreadedSyncAdapter {

    private static final String TAG = "ContactsSyncAdapter";

    public ContactsSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority,
                              ContentProviderClient provider, SyncResult syncResult) {

        Log.d(TAG, "SyncContacts");
        long currentTime = System.currentTimeMillis();

        ContentResolver contentProvider = getContext().getContentResolver();
        Cursor cursor = contentProvider.query(ContactsQuery.CONTENT_URI, ContactsQuery.PROJECTION,
                ContactsQuery.SELECTION, null, ContactsQuery.SORT_ORDER);

        if (cursor != null) {
            Realm realm = Realm.getDefaultInstance();
            while (cursor.moveToNext()) {
                try {
                    long id = cursor.getLong(ContactsQuery.ID);
                    Contacts.Builder builder = new Contacts.Builder()
                            .setId(id)
                            .setName(cursor.getString(ContactsQuery.DISPLAY_NAME))
                            .setThumbnail(cursor.getString(ContactsQuery.PHOTO_URI))
                            .setUpdatedAt(currentTime);

                    Cursor numberCursor = contentProvider.query(PhonesQuery.CONTENT_URI, PhonesQuery.PROJECTION,
                            PhonesQuery.SELECTION, new String[]{String.valueOf(id)}, null);

                    if (numberCursor != null) {
                        RealmList<Number> numberList = new RealmList<>();
                        while (numberCursor.moveToNext()) {
                            String number = numberCursor.getString(PhonesQuery.NUMBER).replaceAll("[^0-9]", "");
                            String type = numberCursor.getString(PhonesQuery.TYPE);

                            Number newNumber = new Number(number, type);
                            int index = numberList.indexOf(newNumber);
                            if (index > -1) numberList.set(index, newNumber);
                            else numberList.add(newNumber);
                        }
                        builder.setNumber(numberList);
                        numberCursor.close();
                    }

                    Contacts contact = builder.build();

                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(contact);
                    realm.commitTransaction();

                } catch (Exception e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            }

            removeUneligibleContacts(realm, currentTime);

            realm.close();
            cursor.close();
        }

    }
    @DebugLog
    private void removeUneligibleContacts(Realm realm, long currentTime) {
        RealmResults<Contacts> results = realm.where(Contacts.class).notEqualTo("updatedAt", currentTime).findAll();
        if (results.isEmpty()) return;
        realm.beginTransaction();
        for (int i = 0; i < results.size(); i++) {
            results.get(i).removeFromRealm();
        }
        results.clear();
        realm.commitTransaction();


    }
 }
