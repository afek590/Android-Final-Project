package com.example.afek.finalproject;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Afek on 25/01/2017.
 */
public class GridViewAdapter extends BaseAdapter implements Filterable
{
    private Context mContext;
    private ArrayList<ImageItem> filterList;
    private CustomFilter filter;

    public GridViewAdapter(Context c)
    {
        mContext = c;
        this.filterList = MainActivity.imageItemList;
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

    public void update()
    {
        this.filterList = MainActivity.imageItemList;
        getFilter().filter("");
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

    @Override
    public Filter getFilter()
    {
        if(filter == null)
        {
            filter = new CustomFilter();
        }
        return filter;
    }

    class CustomFilter extends Filter
    {
        @Override
        protected FilterResults performFiltering(CharSequence constraint)
        {
            FilterResults results = new FilterResults();
            if(constraint != null && constraint.length() > 0)
            {
                constraint = constraint.toString().toLowerCase();
                ArrayList<ImageItem> filters = new ArrayList<ImageItem>();
                for(int i=0; i<filterList.size(); i++)
                {
                    if(filterList.get(i).getAddress().toLowerCase().contains(constraint))
                    {
                        filters.add(filterList.get(i));
                    }
                }
                results.count = filters.size();
                results.values = filters;
            }
            else
            {
                results.count = filterList.size();
                results.values = filterList;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results)
        {
            MainActivity.imageItemList = (ArrayList<ImageItem>) results.values;
            notifyDataSetChanged();
        }
    }
}
