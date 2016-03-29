package com.realmcontacts.contacts;

import android.content.ContentResolver;
import android.database.Cursor;
import android.util.Log;

import com.realmcontacts.RealmApp;
import com.realmcontacts.realm.model.Contacts;

import io.realm.Realm;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.schedulers.Schedulers;

/**
 * Created by Yuriy on 2016-03-28 RealmContacts.
 */
public class ContactsHelper {

    private static final String TAG = "ContactsHelper";
    private ContentResolver mContentProvider;

    public ContactsHelper() {
        mContentProvider = RealmApp.getContext().getContentResolver();
    }

    public Subscription syncContacts() {
        return Observable.create(new Observable.OnSubscribe<Contacts>() {
            @Override
            public void call(Subscriber<? super Contacts> subscriber) {

                if (!subscriber.isUnsubscribed()) {

                    Cursor cursor = mContentProvider.query(ContactsQuery.CONTENT_URI, ContactsQuery.PROJECTION,
                            ContactsQuery.SELECTION, null, ContactsQuery.SORT_ORDER);

                    if (cursor != null) {
                        if (!cursor.moveToFirst()) {
                            cursor.close();
                            subscriber.onCompleted();
                        }

                        do {
                            try {
                                Contacts contact = new Contacts.Builder()
                                        .setId(cursor.getLong(ContactsQuery.ID))
                                        .setName(cursor.getString(ContactsQuery.DISPLAY_NAME))
                                        .setThumbnail(cursor.getString(ContactsQuery.PHOTO_URI))
                                        .build();

                                Realm realm = Realm.getDefaultInstance();
                                realm.beginTransaction();
                                realm.copyToRealmOrUpdate(contact);
                                realm.commitTransaction();

                            } catch (Exception e) {
                                Log.e(TAG, e.getMessage(), e);
                            }
                        } while (cursor.moveToNext());

                        cursor.close();
                        subscriber.onCompleted();
                    }
                }
            }
        })
                .observeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe();
    }


    public static ContactsHelper getInstance() {
        return ContactsHelperHolder.instance;
    }

    private static class ContactsHelperHolder {
        public static final ContactsHelper instance = new ContactsHelper();
    }


}
