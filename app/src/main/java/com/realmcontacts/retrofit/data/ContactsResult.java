package com.realmcontacts.retrofit.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ContactsResult {

    @SerializedName(value = "ok")
    @Expose
    private boolean ok;

    @SerializedName(value = "data")
    @Expose
    private List<VContactsData> data;

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public List<VContactsData> getContacts() {
        return data;
    }

    public void setContacts(List<VContactsData> contacts) {
        this.data = contacts;
    }

}
