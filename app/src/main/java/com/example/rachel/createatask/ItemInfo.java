package com.example.rachel.createatask;

import static android.R.attr.password;

/**
 * Created by Rachel on 7/12/16.
 */

public class ItemInfo {

    String itemname, sku, location, description, video, picture;

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


}
