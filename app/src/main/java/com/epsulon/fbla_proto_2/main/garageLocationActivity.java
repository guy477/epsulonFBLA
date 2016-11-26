package com.epsulon.fbla_proto_2.main;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.epsulon.fbla_proto_2.R;
import com.epsulon.fbla_proto_2.activities.otherSaleActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.FileOutputStream;

public class garageLocationActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LatLng[] sales;
    private Marker[] markers;
    private static final String[] CoordDummy = new String[]{
            //lat:long:Sale Name
            "29.707400:-95.807889:Seven Lakes High School",
            "29.734940:-95.371233:Houston"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        grabSales();
        setContentView(R.layout.activity_garage_location);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */


    public void grabSales() {

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        loadGarageSaleLocations(googleMap);

        //sets the bounds of the map to cover the whole of the united states
        //LatLngBounds bounds = new LatLngBounds(new LatLng(48.500965, -124.736313), new LatLng(44.963645, -67.420640));

        //zooms the map to the center of the united states
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(39.463108, -97.468394)));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(2));
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                for (Marker x : markers) {
                    if (marker.equals(x)) {
                        //start another activity with the choosen sale data
                        //todo create a means by which the user can store data about their current state.
                        //todo create a method somewhere where you grab the sale information from the 'server'
                        startActivity(new Intent(garageLocationActivity.this, otherSaleActivity.class));

                        //todo: write to the file the name of the location in which the user selected. This will be how we grab the data from memory in the future class.

                        try{
                            FileOutputStream s = openFileOutput("SaleNameOther.txt", Context.MODE_PRIVATE);
                            s.write((x.getTitle()+".txt").getBytes());
                        }catch (Exception e){

                        }
                    }
                }

            }
        });
        /*mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener(){
            public boolean onMarkerClick(final Marker marker) {
            for(Marker x : markers){
                if (marker.equals(x)) {
                    startActivity(new Intent(garageLocationActivity.this, otherSaleActivity.class));
                }
            }
            return true;
        }});*/
    }

    public void loadGarageSaleLocations(GoogleMap googleMap) {
        mMap = googleMap;

        //Load the coordnates from the server and create an array to match the size of all the coordnates

        sales = new LatLng[2];
        markers = new Marker[2];
        int count = 0;
        for (String x : CoordDummy) {
            String[] y = x.split(":");
            sales[count] = new LatLng(Double.valueOf(y[0]), Double.valueOf(y[1]));
            markers[count] = mMap.addMarker(new MarkerOptions().position(sales[count]).title(y[2]));
            count++;

        }


    }

    public void gathersales() {
        //todo gather all sales on memory/from a server into an array of sale objects

    }

    public void writeSale() {
        //todo write to a file that
    }
}