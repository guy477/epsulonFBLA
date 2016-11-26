package com.epsulon.fbla_proto_2.main;

import android.content.Intent;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.epsulon.fbla_proto_2.R;

import java.io.FileInputStream;

public class mainMenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    final String xmlFileName = "user.xml";
    private String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try {
            FileInputStream inp = openFileInput("username.txt");
            StringBuilder builder = new StringBuilder();
            int ch;
            while ((ch = inp.read()) != -1) {
                builder.append((char) ch);
            }
            username = builder.toString();
        } catch (Exception e) {
            username = "If you are reading this. We might have an issue... Hacker...";
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check if the user has any mail
                //make the mail icon do the spinny loading thing while it checks the server?

            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        TextView usernameTextView = (TextView) findViewById(R.id.mainUsername);
        usernameTextView.setText(username);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        //// TODO: make the names more logical to what i need to get done. 

        if (id == R.id.find_sales) {
            startActivity(new Intent(mainMenuActivity.this, garageLocationActivity.class));

            //FROM THIS ACTIVITY THE USER CAN CLICK ON DIFFERENT SALES AND FROM THE TODO SALE ACTIVITY; WE WILL ENABLE THE USER TO DONATE, COMMENT ETC.

        } else if (id == R.id.help_others) {
            //todo: make a new activity to start something

        } else if (id == R.id.sell_items) {
            //todo: make a new activity to view your current sell; if the user has one.
            //todo: there will be 2 activities in which the user can view. the one which will navigate the user to where they can make a new sale and the actual current sale.

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.Start_sale) {

        } else if (id == R.id.Account) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public DocumentsContract.Document getDoc() {
        //todo: get the document which contains the user id and/or email;
        return null;
    }

    public String getUserID() {
        //todo: get the user id from an xml file

        return "";
    }
}
