package com.realmcontacts.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Yuriy on 2016-04-18 RealmContacts.
 */
public class APIClient {

    private static final String BASIC_URL = "https://api.vaster.com:8443/vps-api/api/";

    private APIClient() {
    }

    public static APIClient getInstance() {
        return RestfulClientHolder.instance;
    }

    public APIService getAPIService(Object inputObj) {

        OkHttpClient client = CustomSSL.configureClient(new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(2, TimeUnit.MINUTES)
                .readTimeout(2, TimeUnit.MINUTES)
                .addInterceptor(new LoggingInterceptor(inputObj))
                .retryOnConnectionFailure(true))
                .build();

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASIC_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        return retrofit.create(APIService.class);
    }

    private static class RestfulClientHolder {
        public static final APIClient instance = new APIClient();
    }

}
