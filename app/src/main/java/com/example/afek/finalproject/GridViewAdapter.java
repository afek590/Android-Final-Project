package com.example.afek.finalproject;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by Afek on 25/01/2017.
 */
public class GridViewAdapter extends ArrayAdapter<ImageItem>
{

    public GridViewAdapter(Context context, int resource)
    {
        super(context, resource);
    }
}
