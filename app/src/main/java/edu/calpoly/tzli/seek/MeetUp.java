package edu.calpoly.tzli.seek;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by terrence on 5/16/16.
 */
public class MeetUp {
    private String name;
    private String id;
    private String date;
    private String location;

    public MeetUp() {
    }

    public MeetUp(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public MeetUp(String name, String id, String date, String location) {
        this.name = name;
        this.id = id;
        this.date = date;
        this.location = location;
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

    public String getLocation() {
        return location;
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

    public void setLocation(String location) {
        this.location = location;
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
                ", location:'" + location + '\'' +
                '}';
    }
}
