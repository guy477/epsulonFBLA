package com.epsulon.fbla_proto_2.sources;

import android.support.v7.app.AppCompatActivity;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by tprat on 11/22/2016.
 */

public class saleObject extends AppCompatActivity {
    //todo complete to where it contains all the information that a sale will have.
    //coordinates, donations, comments, duration, items for sale, etc.
    //comments are going to have to be independent objects
    //donations are going to have to be independent objects
    //items are going to have to be independent objects

    private ArrayList<itemObject> items = new ArrayList<itemObject>();
    private ArrayList<donationObject> donations = new ArrayList<donationObject>();
    private ArrayList<commentObject> comments = new ArrayList<commentObject>();

    public saleObject(){
        //read through 3 files. one makes new items, one makes new donated objects, and the last one adds the comments
        //todo read from a file which contains all the information
        String name;
        try{
            FileInputStream inp = openFileInput("SaleNameOther.txt");
            StringBuilder builder = new StringBuilder();
            int ch;
            while ((ch = inp.read()) != -1) {
                builder.append((char) ch);
            }
            name = builder.toString();

        }catch (Exception e){
            //wont happen
            name = "";
        }


        try{
            //todo: grab file
            FileInputStream inp = openFileInput(name);
            StringBuilder builder = new StringBuilder();
            int ch;
            while ((ch = inp.read()) != -1) {
                builder.append((char) ch);
            }
            String temp = builder.toString();

            //todo: read the file. Key is on your desktop (inputKey)

            Scanner sc = new Scanner(temp);
            while(sc.hasNextLine()){
                //0 = item
                //1 = donation
                //2 = comments
                for(int h=0;h<3;h++){
                    int num = Integer.valueOf(sc.nextLine());
                    for(int i=0;i<num;i++){
                        switch (h){
                            case 0: items.add(new itemObject(sc.nextLine())); break;

                            case 1: donations.add(new donationObject(sc.nextLine())); break;

                            case 2: comments.add(new commentObject(sc.nextLine())); break;
                        }
                    }
                }
            }
        }catch(Exception e){
            //todo: make a new file
            try {
                FileOutputStream out = openFileOutput(name, MODE_PRIVATE);
                out.write("0\n0\n0".getBytes());
            }catch(Exception f){
                //wont happen
            }
        }
    }

    public itemObject getItemAtLoc(int loc){
        if(loc>0&&loc<items.size()){
            return items.get(loc);
        }
        else return null;
    }

    public donationObject getDonationAtLoc(int loc){
        if(loc>0&&loc<donations.size()){
            return donations.get(loc);
        }
        else return null;
    }

    public commentObject getCommentAtLoc(int loc){
        if(loc>0&&loc<comments.size()){
            return comments.get(loc);
        }
        else return null;
    }

    public ArrayList<itemObject> getItems(){
        return items;
    }

    public ArrayList<donationObject> getDonations(){
        return donations;
    }

    public ArrayList<commentObject> getComments(){
        return comments;
    }

    public void addItem(String inp){
        items.add(new itemObject(inp));
    }

    public void addDonation(String inp){
        donations.add(new donationObject(inp));
    }

    public void addComment(String inp){
        comments.add(new commentObject(inp));
    }

    public boolean removeItem(itemObject obj){
        return items.remove(obj);
    }



    public void updateMemory(){
        //todo: rewrite the file to memory. keeps the data up to date.
    }
}
