package com.epsulon.fbla_proto_2.sources;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by tprat on 11/22/2016.
 */

public class itemObject extends AppCompatActivity {
    private String name;
    private String desc;
    private String date;
    private String user;
    private int rating;

    //Item Name|Description|Date Added|Owner/user|Rating
    public itemObject(String inp){
        String[] arr = inp.split("|");

        name = arr[0];
        desc = arr[1];
        date = arr[2];
        user = arr[3];
        rating = Integer.valueOf(arr[3]);
    }

    public String getName(){
        return name;
    }

    public String getDesc(){
        return desc;
    }

    public String getDate(){
        return date;
    }

    public String getUser(){
        return user;
    }

    public int getRating(){
        return rating;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setDesc(String desc){
        this.desc = desc;
    }

    public void setDate(String date){
        this.date = date;
    }

    public void setUser(String user){
        this.user = user;
    }

    public void setRating(int rating){
        this.rating = rating;
    }

    @Override
    public boolean equals(Object obj){
        if(obj == null)
            return false;
        if(!(obj instanceof itemObject))
            return false;

        return true;
    }
}
