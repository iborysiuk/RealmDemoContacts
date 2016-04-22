package com.realmcontacts.retrofit;

import android.text.TextUtils;
import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.realmcontacts.RealmApp;
import com.realmcontacts.utils.Logs;
import com.realmcontacts.utils.NetworkUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import hugo.weaving.DebugLog;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Yuriy on 2016-04-21 RealmContacts.
 */
public class RestAPIClient extends RestAPICustomSSL {

    private static final String BASIC_URL = "https://api.vaster.com:8443/";
    private static final MediaType JSON_TYPE = MediaType.parse("application/json; charset=utf-8");
    private static OkHttpClient.Builder httpClient = configureSSL(new OkHttpClient.Builder());
    private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(BASIC_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create());

    public static <T> T createVPService(Class<T> serviceClass, Object body) {
        return createService(serviceClass, "vclient", "VastV1", body);
    }

    private static <T> T createService(Class<T> serviceClass, String username, String password, final Object body) {
        if (!NetworkUtils.isOnline(RealmApp.getContext())) throw new RuntimeException("No Internet connection");
            if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {

                String credentials = username + ":" + password;
                final String basic = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

                httpClient.addInterceptor(new Interceptor() {

                    @DebugLog
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request originalRequest = chain.request();

                        //transform && compose request body
                        String jsonBody = getJsonBody(body);
                        Logs.i("=====>RequestBody. json: " + jsonBody);

                        RequestBody requestBody = !TextUtils.isEmpty(jsonBody)
                                ? RequestBody.create(JSON_TYPE, jsonBody) : null;

                        Request.Builder builder = requestBody != null
                                ? originalRequest.newBuilder().post(requestBody) // post request
                                : originalRequest.newBuilder().get(); // get request

                        //compose final authorizedRequest
                        Request authorizedRequest = builder
                                .addHeader("Authorization", basic)
                                .build();
                        return chain.proceed(authorizedRequest);
                    }
                });
            }

        OkHttpClient client = httpClient
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(2, TimeUnit.MINUTES)
                .readTimeout(2, TimeUnit.MINUTES)
                .retryOnConnectionFailure(true)
                .build();

        Retrofit retrofit = builder
                .client(client)
                .build();

        return retrofit.create(serviceClass);
    }

    @DebugLog
    private static String getJsonBody(Object body) {
        if (body == null) return null;
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        return gson.toJson(body);
    }
}
