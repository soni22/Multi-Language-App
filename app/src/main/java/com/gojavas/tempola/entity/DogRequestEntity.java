package com.gojavas.tempola.entity;

/**
 * Created by gjs331 on 7/15/2015.
 */
public class DogRequestEntity {

    String requestid;
    int time_left_to_respond;
    String name;
    String curr_address;
    String picture;
    String phone;
    String address;
    double latitude;
    double longitude;
    String rating;
    int num_rating;

    public String getRequestid() {
        return requestid;
    }

    public void setRequestid(String requestid) {
        this.requestid = requestid;
    }

    public int getTime_left_to_respond() {
        return time_left_to_respond;
    }

    public void setTime_left_to_respond(int time_left_to_respond) {
        this.time_left_to_respond = time_left_to_respond;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurr_address() {
        return curr_address;
    }

    public void setCurr_address(String curr_address) {
        this.curr_address = curr_address;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public int getNum_rating() {
        return num_rating;
    }

    public void setNum_rating(int num_rating) {
        this.num_rating = num_rating;
    }
}
