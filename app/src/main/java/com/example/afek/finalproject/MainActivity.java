package com.example.afek.finalproject;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    private final int GPS_PERMISSION = 200;
    private final int CAMERA_REQUEST = 1888;
    private final long MIN_TIME_FOR_UPDATE = 500;
    private final long MIN_DIS_FOR_UPDATE = 0;
    private final String NO_LOCATION = "Location unavailable.";
    private ImageButton editBtn, searchBtn, cameraBtn;
    private GridView gridView;
    private GridViewAdapter gridAdapter;
    private Location currentLocation;
    private DbHelper dbHelper;
    private static SQLiteDatabase db;
    private Cursor cursor;
    public static ArrayList<ImageItem> imageItemList;
    private String searchStr;
    private boolean editEnable;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Basic Initializes
        imageItemList = new ArrayList<ImageItem>();
        editBtn = (ImageButton) findViewById(R.id.edit_button);
        searchBtn = (ImageButton) findViewById(R.id.search_button);
        cameraBtn = (ImageButton) findViewById(R.id.camera_button);
        editBtn.setOnClickListener(this);
        searchBtn.setOnClickListener(this);
        cameraBtn.setOnClickListener(this);
        gridView = (GridView) findViewById(R.id.gridView);
        gridAdapter = new GridViewAdapter(this);
        gridView.setAdapter(gridAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) // On click listener of the pictures in the grid
            {
                if(editEnable) // If edit is enabled, click on a picture will mark it.
                {
                    imageItemList.get(position).setChecked();
                    gridAdapter.notifyDataSetChanged();
                }
                else // If edit is disabled, click on a picture will transfer to the GridItemActivity
                {
                    Intent i = new Intent(getApplicationContext(), GridItemActivity.class);
                    i.putExtra("id", imageItemList.get(position).getId());
                    i.putExtra("data", ImageUtils.getBytes(imageItemList.get(position).getImage()));
                    i.putExtra("longitude", imageItemList.get(position).getLongitude());
                    i.putExtra("latitude", imageItemList.get(position).getLatitude());
                    i.putExtra("address", imageItemList.get(position).getAddress());
                    startActivity(i);
                }
            }
        });
        searchStr = "";
        editEnable = false;
        permissionCheck();
        dbHelper = new DbHelper(this);
        db = dbHelper.getWritableDatabase();
        updateImageArray();
    }

    private void permissionCheck() // Check the gps permissions.
    {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if(permissionCheck == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, GPS_PERMISSION);
        setLocationMethod();
    }

    @Override
    public void onClick(View v)
    {
        if (v.getId() == editBtn.getId()) // Click on the edit button
        {
            if(editEnable) // If edit already enabled, we unmark all the pictures in the grid and the other button will switch back to 'search'.
            {
                searchBtn.setImageResource(android.R.drawable.ic_menu_search);
                resetCheckedImages();
                gridAdapter.notifyDataSetChanged();
            }
            else // If edit is disabled, the other button will switch to 'delete' and marking pictures is enabled.
            {
                searchBtn.setImageResource(android.R.drawable.ic_menu_delete);
            }
            editEnable = !editEnable;
        }
        else if (v.getId() == searchBtn.getId()) // Click on the search\delete button
        {
            if(editEnable) // If edit is enabled - the button is 'delete' button and on click we delete all the marked pictures.
            {
                for(int i=0; i<imageItemList.size(); i++)
                {
                    if(imageItemList.get(i).isChecked())
                        deletePicture(imageItemList.get(i).getId());
                }
                searchBtn.setImageResource(android.R.drawable.ic_menu_search);
                editEnable = false;
                updateImageArray();
            }
            else // If edit is disabled - the button is 'search' button and on click we open a alert dialog to search pictures according to location.
            {
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("Search");
                alert.setMessage("Enter a location");
                final EditText input = new EditText(this);
                input.setText(searchStr.toString());
                alert.setView(input);

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        searchStr = input.getText().toString();
                        gridAdapter.getFilter().filter(searchStr);
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        searchStr = "";
                        gridAdapter.getFilter().filter(searchStr);
                    }
                });

                alert.show();
            }
        }
        else if (v.getId() == cameraBtn.getId()) // Camera button opens the camera to take a new picture.
        {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }
    }

    private void setLocationMethod() // Define the location listener and its settings
    {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                currentLocation = location;
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}

            @Override
            public void onProviderEnabled(String provider) {}

            @Override
            public void onProviderDisabled(String provider) {}
        };

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return;
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_FOR_UPDATE, MIN_DIS_FOR_UPDATE, locationListener);
        currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) // Returning from camera activity and saving the pictures and its information to the database.
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK)
        {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            double latitude = 200;
            double longitude = 200;
            if(currentLocation != null)
            {
                latitude = currentLocation.getLatitude();
                longitude = currentLocation.getLongitude();
            }
            ContentValues values = new  ContentValues();
            values.put(Constants.Gallery.KEY_DATA, ImageUtils.getBytes(photo));
            values.put(Constants.Gallery.KEY_LATI, latitude);
            values.put(Constants.Gallery.KEY_LONG, longitude);
            values.put(Constants.Gallery.KEY_ADDR, getAddress(latitude, longitude));
            db.insert(Constants.Gallery.TABLE_NAME, null, values);
            updateImageArray();
        }
    }

    private void updateImageArray() // Updating the image list according to the database.
    {
        imageItemList.clear();
        cursor = db.query(Constants.Gallery.TABLE_NAME, null, null, null, null, null, null);
        while(cursor.moveToNext())
        {
            ImageItem imageItem = new ImageItem();
            imageItem.setId(cursor.getInt(0));
            imageItem.setImage(ImageUtils.getImage(cursor.getBlob(1)));
            imageItem.setLongitude(cursor.getDouble(2));
            imageItem.setLatitude(cursor.getDouble(3));
            imageItem.setAddress(cursor.getString(4));
            imageItemList.add(imageItem);
        }
        searchStr = "";
        gridAdapter.update();
    }

    public static void deletePicture(int id) // Delete a picture from the database.
    {
        db.delete(Constants.Gallery.TABLE_NAME, Constants.Gallery._ID + "=?", new String[] { String.valueOf(id) });
    }

    private String getAddress(double latitude, double longitude) // Getting the string of the address according to latitude and longitude.
    {
        if(latitude == 200 || longitude == 200)
            return NO_LOCATION;
        Geocoder geocoder;
        List<Address> addrList;
        Address finalAddr;
        geocoder = new Geocoder(this, Locale.getDefault());
        String strAdd = "";
        try
        {
            addrList = geocoder.getFromLocation(latitude, longitude, 1);
            if(addrList != null && addrList.size() > 0)
            {
                finalAddr = addrList.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");
                for (int i = 0; i < finalAddr.getMaxAddressLineIndex(); i++)
                    strReturnedAddress.append(finalAddr.getAddressLine(i)).append("\n");
                strAdd = strReturnedAddress.toString();
            }
            else
                return NO_LOCATION;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return NO_LOCATION;
        }
        return strAdd;
    }

    private void resetCheckedImages() // Unmark all the pictures in the grid.
    {
        for(int i=0; i<imageItemList.size(); i++)
            imageItemList.get(i).setValueCheck(false);
        gridAdapter.notifyDataSetChanged();
    }
}
