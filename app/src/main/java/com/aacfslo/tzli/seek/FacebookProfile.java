package com.aacfslo.tzli.seek;

import java.io.IOException;
import java.io.Serializable;

/**
 * Created by terrence on 5/13/16.
 */
public class FacebookProfile implements Serializable{
    public String name;
    public String id;
    public String picture;
    public int metUp;

    public FacebookProfile() {
    }

    public FacebookProfile(String name, String id) throws IOException {
        this.name = name;
        this.id = id;
        this.picture = getFacebookProfilePicture(id);
        this.metUp = getMetUp();
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getImageUrl() {
        return picture;
    }

    public String getFacebookProfilePicture(String userID) throws IOException {
        return "https://graph.facebook.com/" + userID + "/picture?type=large";
    }

    //TODO: pull from firebase to get meetup amount using ID
    public int getMetUp() {
        return metUp;
    }

    @Override
    public String toString() {
        return name;
    }
}
