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
public class GridViewAdapter extends BaseAdapter
{
    private Context mContext;

    public GridViewAdapter(Context c)
    {
        mContext = c;
    }

    @Override
    public int getCount()
    {
        return MainActivity.imageItemList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return null;
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ImageView imageView;
        if(convertView == null)
        {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        }
        else
            imageView = (ImageView) convertView;
        imageView.setImageBitmap(MainActivity.imageItemList.get(position).getImage());
        return imageView;
    }
}
