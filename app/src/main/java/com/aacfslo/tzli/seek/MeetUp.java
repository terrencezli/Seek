package com.aacfslo.tzli.seek;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by terrence on 5/16/16.
 */
public class MeetUp {
    private String name;
    private String id;
    private String date;
    private Boolean met;
    private String uniqueId;

    public MeetUp() {
    }

    public MeetUp(String name, String id) {
        this.name = name;
        this.id = id;
        this.met = false;
    }

    public MeetUp(String name, String id, String date) {
        this.name = name;
        this.id = id;
        this.date = date;
        this.met = false;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public Boolean getMet() {
        return met;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDate(String date) {
        this.date = date.substring(0,10);
    }

    public void setMet(Boolean met) {
        this.met = met;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    @JsonIgnoreProperties({
            "picture"
    })
    public String retrievePicture() {
        return "https://graph.facebook.com/" + id + "/picture?type=large";
    }

    @Override
    public String toString() {
        return "{" +
                "name:'" + name + '\'' +
                ", id:'" + id + '\'' +
                ", date:'" + date + '\'' +
                ", met:'" + met + '\'' +
                ", uniqueId:'" + uniqueId + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MeetUp meetUp = (MeetUp) o;

        if (name != null ? !name.equals(meetUp.name) : meetUp.name != null) return false;
        if (id != null ? !id.equals(meetUp.id) : meetUp.id != null) return false;
        if (date != null ? !date.equals(meetUp.date) : meetUp.date != null) return false;
        return met != null ? met.equals(meetUp.met) : meetUp.met == null;

    }
}
