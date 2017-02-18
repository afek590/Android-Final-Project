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
    private String address;
    private ImageView imageView;
    private TextView textView;
    private Button delete;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_item);

        Intent i = getIntent();
        id = i.getIntExtra("id", -1);
        image = ImageUtils.getImage(i.getByteArrayExtra("data"));
        longitude = i.getDoubleExtra("longitude", 200);
        latitude = i.getDoubleExtra("latitude", 200);
        address = i.getStringExtra("address");
        checkExtras();
        imageView = (ImageView) findViewById(R.id.imageView);
        textView = (TextView) findViewById(R.id.textView);
        delete = (Button) findViewById(R.id.delBtn);
        delete.setOnClickListener(this);
        textView.setOnClickListener(this);
        textView.setText(address);
        imageView.setImageBitmap(image);
    }

    private void checkExtras() // If some data is unavailable or have error we finish the activity.
    {
        if(id == -1 || image == null || longitude == 200 || latitude == 200)
        {
            setResult(RESULT_CANCELED);
            finish();
        }
    }

    @Override
    public void onClick(View v)
    {
        if(v.getId() == delete.getId()) // Delete the picture from the database.
        {
            MainActivity.deletePicture(id);
            startActivity(new Intent(this, MainActivity.class));
        }
        else if(v.getId() == textView.getId()) // See the picture's location on google maps.
        {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("geo:0,0?q=" + (latitude + "," + longitude)));
            startActivity(intent);
        }
    }


}
