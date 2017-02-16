package com.example.afek.finalproject;

import android.graphics.Bitmap;

/**
 * Created by Afek on 25/01/2017.
 */
public class ImageItem
{
    private int id;
    private Bitmap image;
    private double longitude, latitude;
    private String address;
    private boolean isChecked = false;

    public void setImage(Bitmap image)
    {
        this.image = image;
    }

    public void setLongitude(double longitude)
    {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude)
    {
        this.latitude = latitude;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public Bitmap getImage()
    {
        return image;
    }

    public double getLongitude()
    {
        return longitude;
    }

    public double getLatitude()
    {
        return latitude;
    }

    public int getId()
    {
        return id;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {

        return address;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked() {
        isChecked = !isChecked;
    }

    public void setValueCheck(boolean check)
    {
        isChecked = check;
    }
}
