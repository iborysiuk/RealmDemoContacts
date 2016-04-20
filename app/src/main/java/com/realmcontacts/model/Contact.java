package com.realmcontacts.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by Yuriy on 2016-03-09.
 */
public class Contact extends RealmObject {

    @PrimaryKey
    private String id;

    @Required
    private String name;
    private RealmList<Number> numbers;
    private String thumbnail;
    private String vsrId;
    private String vsrName;
    private Long updatedAt;

    public Contact() {
    }

    private Contact(String id, String name, RealmList<Number> numbers, String thumbnail,
                    String vsrId, String vsrName, Long updatedAt) {
        this.id = id;
        this.name = name;
        this.numbers = numbers;
        this.thumbnail = thumbnail;
        this.vsrId = vsrId;
        this.vsrName = vsrName;
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

    public RealmList<Number> getNumbers() {
        return numbers;
    }

    public void setNumbers(RealmList<Number> numbers) {
        this.numbers = numbers;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getVsrId() {
        return vsrId;
    }

    public void setVsrId(String vsrId) {
        this.vsrId = vsrId;
    }

    public String getVsrName() {
        return vsrName;
    }

    public void setVsrName(String vsrName) {
        this.vsrName = vsrName;
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
        private RealmList<Number> numbers;
        private String thumbnail;
        private String vsrId;
        private String vsrName;
        private Long updatedAt;

        public Contact.Builder setId(long id) {
            this.id = String.valueOf(id);
            return this;
        }

        public Contact.Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Contact.Builder setNumbers(RealmList<Number> numbers) {
            this.numbers = numbers;
            return this;
        }

        public Contact.Builder setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
            return this;
        }

        public Contact.Builder setVasterId(String vsrId) {
            this.vsrId = vsrId;
            return this;
        }

        public Contact.Builder setVasterName(String vsrName) {
            this.vsrName = vsrName;
            return this;
        }

        public Contact.Builder setUpdatedAt(Long updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }


        public Contact build() {
            return new Contact(id, name, numbers, thumbnail, vsrId, vsrName, updatedAt);
        }
    }
}
