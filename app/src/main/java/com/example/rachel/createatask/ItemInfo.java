package com.example.rachel.createatask;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import static android.R.attr.password;

/**
 * Created by Rachel on 7/12/16.
 */

public class ItemInfo {

    String itemname, sku, location, description, video, picture;
    byte[] thumbnail;
    int iD;


    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getItemname() {
        return this.itemname;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getSku() {
        return this.sku;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return this.location;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public String getVideo() { return this.video; }

    public void setVideo(String vid) { this.video = vid; }

    public String getPicture() { return this.picture; }

    public void setPicture(String pic) { this.picture = pic; }

    public void setThumbnail(byte[] pic) {
        this.thumbnail = pic;
    }

    public byte[] getThumbnail() {
        return thumbnail;
    }

    public void setID(int iD) {
        this.iD = iD;
    }

    public int getID() {
        return iD;
    }

}
