package com.epsulon.fbla_proto_2.sources;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by tprat on 11/22/2016.
 */


public class donationObject extends AppCompatActivity {
    private itemObject donation;

    public donationObject(String inp){
        this.donation = new itemObject(inp);
    }

    private String getName(){
        return donation.getName();
    }

    private String getDesc(){
        return donation.getDesc();
    }

    private String getDate(){
        return donation.getDate();
    }

    private String getUser(){
        return donation.getUser();
    }

    private int getRating(){
        return donation.getRating();
    }




    private void setName(String name){
        donation.setName(name);
    }

    private void setDesc(String desc){
        donation.setDesc(desc);
    }

    private void setDate(String date){
        donation.setDate(date);
    }

    private void setUser(String user){
        donation.setUser(user);
    }

    private void setRating(int rating){
        donation.setRating(rating);
    }
}
