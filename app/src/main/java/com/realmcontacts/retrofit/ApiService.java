package com.realmcontacts.retrofit;

import com.realmcontacts.retrofit.data.ContactsResponse;

import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Yuriy on 2016-04-18 RealmContacts.
 */
public interface APIService {

    @POST("users/{id}/contacts")
    Observable<ContactsResponse> crossReferenceRx(@Path("id") String userId);
}
