package com.realmcontacts.retrofit.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Yuriy on 2015-10-04.
 * The name of the current project is VasterAndroidApp.
 */
public class VContactsData {

    @SerializedName(value = "id")
    @Expose
    private String id;

    @SerializedName(value = "vid")
    @Expose
    private String vid;

    @SerializedName(value = "name")
    @Expose
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
