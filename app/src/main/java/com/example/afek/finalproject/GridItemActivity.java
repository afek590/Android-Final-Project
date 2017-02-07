package com.example.afek.finalproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GridItemActivity extends AppCompatActivity implements View.OnClickListener
{
    private int id;
    private Bitmap image;
    private double longitude, latitude;
    private ImageView imageView;
    private TextView textView;
    private Button delete;
    private Geocoder geocoder;
    private List<Address> addrList;
    private Address finalAddr;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_item);

        Intent i = getIntent();
        id = i.getIntExtra("id", -1);
        image = ImageUtils.getImage(i.getByteArrayExtra("data"));
        longitude = i.getDoubleExtra("longitude", -1);
        latitude = i.getDoubleExtra("latitude", -1);
        checkExtras();
        imageView = (ImageView) findViewById(R.id.imageView);
        textView = (TextView) findViewById(R.id.textView);
        delete = (Button) findViewById(R.id.delBtn);
        delete.setOnClickListener(this);
        textView.setOnClickListener(this);
        textView.setText(getAddress());
        imageView.setImageBitmap(image);
    }

    private void checkExtras()
    {
        if(id == -1 || image == null || longitude == -1 || latitude == -1)
        {
            setResult(RESULT_CANCELED);
            finish();
        }
    }

    @Override
    public void onClick(View v)
    {
        if(v.getId() == delete.getId())
        {
            MainActivity.deletePicture(id);
            startActivity(new Intent(this, MainActivity.class));
        }
        else if(v.getId() == textView.getId())
        {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("geo:0,0?q=" + (latitude + "," + longitude)));
            startActivity(intent);
        }
    }

    private String getAddress()
    {
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
                return "Location unavailable.";
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return "Location unavailable.";
        }
        return strAdd;
    }
}
