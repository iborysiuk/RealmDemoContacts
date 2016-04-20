package com.realmcontacts.retrofit.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ContactsResponse {

    @SerializedName(value = "status")
    @Expose
    private int status;

    @SerializedName(value = "message")
    @Expose
    private String message;

    @SerializedName(value = "result")
    @Expose
    private ContactsResult result;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ContactsResult getResult() {
        return result;
    }

    public void setResult(ContactsResult result) {
        this.result = result;
    }

    public boolean isSuccessStatus(ContactsResponse response) {
        return response.getStatus() == 1 && response.getMessage().equals("Success.");
    }

    public boolean isResultOk(ContactsResponse response) {
        return response.getResult().isOk();
    }
}
