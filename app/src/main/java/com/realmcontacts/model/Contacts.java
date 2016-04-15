package com.realmcontacts.model;

import io.realm.RealmList;
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
    private RealmList<Number> number;
    private String thumbnail;
    private Long updatedAt;

    public Contacts() {
    }

    private Contacts(String id, String name, RealmList<Number> number, String thumbnail, Long updatedAt) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.thumbnail = thumbnail;
        this.updatedAt = updatedAt;
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

    public RealmList<Number> getNumber() {
        return number;
    }

    public void setNumber(RealmList<Number> number) {
        this.number = number;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public static class Builder {
        private String id;
        private String name;
        private RealmList<Number> number;
        private String thumbnail;
        private Long updatedAt;

        public Contacts.Builder setId(long id) {
            this.id = String.valueOf(id);
            return this;
        }

        public Contacts.Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Contacts.Builder setNumber(RealmList<Number> number) {
            this.number = number;
            return this;
        }

        public Contacts.Builder setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
            return this;
        }

        public Contacts.Builder setUpdatedAt(Long updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Contacts build() {
            return new Contacts(id, name, number, thumbnail, updatedAt);
        }
    }
}
