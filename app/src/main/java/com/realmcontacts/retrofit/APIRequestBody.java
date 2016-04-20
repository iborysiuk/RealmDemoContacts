package com.realmcontacts.retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Yuriy on 2016-04-18 RealmContacts.
 */
public class APIRequestBody {

    @SerializedName(value = "deviceId")
    @Expose(serialize = true, deserialize = false)
    private String deviceId;

    @SerializedName(value = "token")
    @Expose(serialize = true, deserialize = false)
    private String userToken;

    @SerializedName(value = "contacts")
    @Expose(serialize = true, deserialize = false)
    private List<ContactData> contactsList;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public List<ContactData> getContactsList() {
        return contactsList;
    }

    public void setContactsList(List<ContactData> contactsList) {
        this.contactsList = contactsList;
    }
}
