package com.realmcontacts.contacts;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Settings;

import com.google.common.collect.Lists;
import com.realmcontacts.model.Contact;
import com.realmcontacts.model.Number;
import com.realmcontacts.retrofit.APIClient;
import com.realmcontacts.retrofit.APIRequestBody;
import com.realmcontacts.retrofit.ContactData;
import com.realmcontacts.retrofit.data.ContactsResponse;
import com.realmcontacts.retrofit.data.VContactsData;
import com.realmcontacts.utils.Logs;

import java.util.List;

import hugo.weaving.DebugLog;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import rx.functions.Action1;

/**
 * Created by Yuriy on 2016-03-09.
 */
public class ContactsSyncAdapter extends AbstractThreadedSyncAdapter {

    public ContactsSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority,
                              ContentProviderClient provider, SyncResult syncResult) {

        Logs.d("SyncContacts");
        long currentTime = System.currentTimeMillis();

        ContentResolver contentProvider = getContext().getContentResolver();
        Cursor cursor = contentProvider.query(ContactsQuery.CONTENT_URI, ContactsQuery.PROJECTION,
                ContactsQuery.SELECTION, null, ContactsQuery.SORT_ORDER);

        if (cursor != null) {
            Realm realm = Realm.getDefaultInstance();
            while (cursor.moveToNext()) {
                try {
                    long id = cursor.getLong(ContactsQuery.ID);
                    Contact.Builder builder = new Contact.Builder()
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
                        builder.setNumbers(numberList);
                        numberCursor.close();
                    }

                    Contact contact = builder.build();

                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(contact);
                    realm.commitTransaction();

                } catch (Exception e) {
                    e.printStackTrace();
                    realm.cancelTransaction();
                }
            }

            crossReference(realm, getContactDataList(realm));
            //removeUneligibleContacts(realm, currentTime);

            realm.close();
            cursor.close();
        }
    }

    @DebugLog
    private void crossReference(Realm realm, List<ContactData> contactsList) {
        if (contactsList.isEmpty()) return;
        APIClient.getInstance().getAPIService(composeBody(contactsList))
                .crossReferenceRx("1006311459790753945")
                .subscribe(new Action1<ContactsResponse>() {
                    @Override
                    public void call(ContactsResponse response) {
                        List<VContactsData> vContactsList = response.getResult().getContacts();
                        if (!vContactsList.isEmpty()) {
                            for (VContactsData vContact : vContactsList) {
                                Contact contact = realm.where(Contact.class)
                                        .equalTo("numbers.number", vContact.getId()).findFirst();

                                if (contact != null) {
                                    realm.beginTransaction();
                                    contact.setVsrId(vContact.getVid());
                                    contact.setVsrName(vContact.getName());
                                    realm.commitTransaction();
                                }
                            }
                        }
                    }
                });
    }

    @DebugLog
    private List<ContactData> getContactDataList(Realm realm) {
        List<ContactData> dataList = Lists.newLinkedList();
        RealmResults<Contact> results = realm.where(Contact.class).findAll();
        if (!results.isEmpty()) {
            for (Contact contact : results) {
                ContactData.Builder newContact = new ContactData.Builder()
                        .setName(contact.getName())
                        .setType("numbers");

                int size = contact.getNumbers().size();
                String[] numbers = new String[size];
                for (int i = 0; i < size; i++) {
                    numbers[i] = contact.getNumbers().get(i).getNumber();
                }
                newContact.setNumber(numbers);
                dataList.add(newContact.build());
            }
        }
        return dataList;
    }

    private APIRequestBody composeBody(List<ContactData> contactsList) {
        APIRequestBody body = new APIRequestBody();
        body.setDeviceId(Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID));
        body.setUserToken("1234");
        body.setContactsList(contactsList);
        return body;
    }


    @DebugLog
    private void removeUneligibleContacts(Realm realm, long currentTime) {
        RealmResults<Contact> results = realm.where(Contact.class).notEqualTo("updatedAt", currentTime).findAll();
        if (results.isEmpty()) return;
        try {
            realm.beginTransaction();
            for (int i = 0; i < results.size(); i++) {
                results.get(i).removeFromRealm();
            }
            results.clear();
            realm.commitTransaction();
        } catch (Exception e) {
            e.printStackTrace();
            realm.cancelTransaction();
        }
    }
}
