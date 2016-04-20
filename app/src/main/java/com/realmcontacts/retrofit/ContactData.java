package com.realmcontacts.retrofit;

import com.google.common.collect.Lists;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Yuriy on 2016-04-18 RealmContacts.
 */
public class ContactData {

    @SerializedName(value = "name")
    @Expose(serialize = true, deserialize = false)
    private String name;

    @SerializedName(value = "ids")
    @Expose(serialize = true, deserialize = false)
    private List<String> numbersList;

    @SerializedName(value = "type")
    @Expose(serialize = true, deserialize = false)
    private String type;

    public ContactData() {
    }

    private ContactData(String name, List<String> numbersList, String type) {
        this.name = name;
        this.numbersList = numbersList;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getNumbersList() {
        return numbersList;
    }

    public void setNumbersList(List<String> numbersList) {
        this.numbersList = numbersList;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static class Builder {
        private String name;
        private List<String> numbersList;
        private String type;

        public ContactData.Builder setName(String name) {
            this.name = name;
            return this;
        }

        public ContactData.Builder setNumber(String[] numbers) {
            this.numbersList = Lists.newArrayList(numbers);
            return this;
        }

        public ContactData.Builder setType(String type) {
            this.type = type;
            return this;
        }

        public ContactData build() {
            return new ContactData(name, numbersList, type);
        }
    }

}
