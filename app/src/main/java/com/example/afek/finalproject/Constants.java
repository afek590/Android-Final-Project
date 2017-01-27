package com.example.afek.finalproject;

import android.graphics.Bitmap;
import android.provider.BaseColumns;

/**
 * Created by Afek on 26/01/2017.
 */

public final class Constants
{
    private Constants()
    {
        throw new AssertionError("Can't create constans class");
    }

    public static abstract class Gallery implements BaseColumns
    {
        public static final String TABLE_NAME = "imageTable";
        public static final String KEY_DATA = "image_data";
        public static final String KEY_LONG = "image_longitude";
        public static final String KEY_LATI = "image_latitude";
    }
}
