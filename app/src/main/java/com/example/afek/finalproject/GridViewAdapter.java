package com.example.afek.finalproject;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by Afek on 25/01/2017.
 */
public class GridViewAdapter extends ArrayAdapter
{
    private Context context;
    private int layoutResourceId;

    public GridViewAdapter(Context context, int resource)
    {
        super(context, resource);
        this.context = context;
        this.layoutResourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        return super.getView(position, convertView, parent);
    }
}
