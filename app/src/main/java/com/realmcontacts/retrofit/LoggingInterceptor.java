package com.realmcontacts.retrofit;

import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.realmcontacts.RealmApp;
import com.realmcontacts.utils.Logs;
import com.realmcontacts.utils.NetworkUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Yuriy on 2016-04-18 RealmContacts.
 */
public class LoggingInterceptor implements Interceptor {

    private Object value;

    public LoggingInterceptor(Object value) {
        this.value = value;
    }

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");



    @Override
    public Response intercept(Chain chain) throws IOException {
        if (!NetworkUtils.isOnline(RealmApp.getContext())) throw new RuntimeException("No Internet connection");

        Request originalRequest = chain.request();

        String json = getRequestData(value);
        RequestBody requestBody = null;
        Logs.i("=====>RequestBody. json: " + json);

        Request authorizedRequest;
        if (json != null) requestBody = RequestBody.create(JSON, json);
        if (requestBody != null) {
            authorizedRequest = originalRequest.newBuilder()
                    .addHeader("Authorization", base64EncodedCredentials())
                    .post(requestBody)
                    .build();
        } else {
            authorizedRequest = originalRequest.newBuilder()
                    .addHeader("Authorization", base64EncodedCredentials())
                    .get()
                    .build();
        }

        Logs.i("=====>Request. url: " + authorizedRequest.url());
        return chain.proceed(authorizedRequest);
    }

    private String base64EncodedCredentials() {
        return "Basic " + Base64.encodeToString(("vclient:VastV1").getBytes(), Base64.NO_WRAP);
    }

    private String getRequestData(Object value) {
        if (value == null) return null;
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        return gson.toJson(value);
    }
}
