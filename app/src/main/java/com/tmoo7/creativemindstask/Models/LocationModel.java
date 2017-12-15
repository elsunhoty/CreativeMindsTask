package com.tmoo7.creativemindstask.Models;

/**
 * Created by othello on 12/15/2017.
 */

public class LocationModel {
    private  String name,formattedPhone,categories,status,photourl;

    public LocationModel() {
    }

    public LocationModel(String name, String formattedPhone, String categories, String status, String photourl) {
        this.name = name;
        this.formattedPhone = formattedPhone;
        this.categories = categories;
        this.status = status;
        this.photourl = photourl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFormattedPhone() {
        return formattedPhone;
    }

    public void setFormattedPhone(String formattedPhone) {
        this.formattedPhone = formattedPhone;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhotourl() {
        return photourl;
    }

    public void setPhotourl(String photourl) {
        this.photourl = photourl;
    }

    @Override
    public String toString() {
        return "LocationModel{" +
                "name='" + name + '\'' +
                ", formattedPhone='" + formattedPhone + '\'' +
                ", categories='" + categories + '\'' +
                ", status='" + status + '\'' +
                ", photourl='" + photourl + '\'' +
                '}';
    }
}
