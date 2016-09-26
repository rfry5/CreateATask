package com.example.rachel.createatask;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import static android.R.attr.password;

/**
 * Created by Rachel on 7/12/16.
 */

public class ItemInfo {

    String itemname, sku, location, description, video, picture, com1, com2, com3, com4, com5, com6, spinnerType,spinnerSize;
    byte[] thumbnail;
    int iD, itemFlag;


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

    public void setCom1(String com1) {
        this.com1 = com1;
    }

    public String getCom1() {
        return this.com1;
    }

    public void setCom2(String com2) {
        this.com2 = com2;
    }

    public String getCom2() {
        return this.com2;
    }

    public void setCom3(String com3) {
        this.com3 = com3;
    }

    public String getCom3() {
        return this.com3;
    }

    public void setCom4(String com4) {
        this.com4 = com4;
    }

    public String getCom4() {
        return this.com4;
    }

    public void setCom5(String com5) {
        this.com5 = com5;
    }

    public String getCom5() {
        return this.com5;
    }

    public void setCom6(String com6) {
        this.com6 = com6;
    }

    public String getCom6() {
        return this.com6;
    }

    public void setSpinType(String spinnerType) {
        this.spinnerType = spinnerType;
    }

    public String getSpinType() {
        return this.spinnerType;
    }

    public void setSpinSize(String spinnerSize) {
        this.spinnerSize = spinnerSize;
    }

    public String getSpinSize() {
        return this.spinnerSize;
    }

    public void setItemFlag(int itemFlag) {
        this.itemFlag = itemFlag;
    }

    public int getItemFlag() {
        return this.itemFlag;
    }


}
