package edu.calpoly.tzli.seek;

import java.util.Date;

/**
 * Created by terrence on 5/16/16.
 */
public class MetUpInformation {
    private String name;
    private Date date;
    private String location;

    public MetUpInformation(String name) {
        this.name = name;
    }

    public MetUpInformation(String name, Date date, String location) {
        this.name = name;
        this.date = date;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public Date getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "{" +
                "name:'" + name + '\'' +
                ", date:" + date +
                ", location:'" + location + '\'' +
                '}';
    }
}
