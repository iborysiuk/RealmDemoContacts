package com.realmcontacts.realm.model;

import io.realm.RealmObject;
import io.realm.annotations.Required;

/**
 * Created by Yuriy on 2016-04-06 RealmContacts.
 */
public class Number extends RealmObject {

    @Required
    private String number;
    private String type;

    public Number() {
    }

    public Number(String number, String type) {
        this.number = number;
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int hashCode() {
        return number.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof Number && number.hashCode() == ((Number) object).getNumber().hashCode();
    }

}
