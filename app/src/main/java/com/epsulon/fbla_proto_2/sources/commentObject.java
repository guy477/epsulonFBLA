package com.epsulon.fbla_proto_2.sources;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by tprat on 11/22/2016.
 */

public class commentObject extends AppCompatActivity {
    private String comment;
    private String date;
    private String user;

    //Comment|Date|User
    public commentObject(String inp){
        String[] arr = inp.split("|");
        comment = arr[0];
        date = arr[1];
        user = arr[2];
    }

    public String getComment(){
        return comment;
    }

    public String getDate(){
        return date;
    }

    public String getUser(){
        return user;
    }




    public void setComment(String comment){
        this.comment = comment;
    }

    public void setDate(String date){
        this.date = date;
    }

    public void setUser(String user){
        this.user = user;
    }
}
