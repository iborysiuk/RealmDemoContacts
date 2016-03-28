package com.realmcontacts.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by Yuriy on 2016-03-09.
 */
public class Contacts extends RealmObject {

    @PrimaryKey
    private String id;
    @Required
    private String name;
    private String number;
    private String thumbnail;

    public Contacts() {
    }

    private Contacts(String id, String name, String number, String thumbnail) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.thumbnail = thumbnail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public static class Builder {
        private String id;
        private String name;
        private String number;
        private String thumbnail;

        public Contacts.Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Contacts.Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Contacts.Builder setNumber(String number) {
            this.number = number;
            return this;
        }

        public Contacts.Builder setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
            return this;
        }

        public Contacts build() {
            return new Contacts(id, name, number, thumbnail);
        }
    }
}
