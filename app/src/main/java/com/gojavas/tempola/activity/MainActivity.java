package com.gojavas.tempola.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.gojavas.tempola.R;
import com.gojavas.tempola.application.TempolaApplication;
import com.gojavas.tempola.constants.Constants;
import com.gojavas.tempola.fragment.MapFragment;
import com.gojavas.tempola.fragment.MapFragmentNew;
import com.gojavas.tempola.utils.Utility;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by gjs331 on 6/30/2015.
 */


public class MainActivity extends ActionBarActivity implements CompoundButton.OnCheckedChangeListener {

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mainnew);


/**
 *setup a toolbar
 */
        setUpToolbar();
        /**
         * Set up a navigation Drawer in Toolbar
         */

        setUpNavDrawer();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        /**
         * setup a navigation view clicklistners
         */
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        /**
         * Profile Image Setup on Navigationview
         */
        ImageView imageViewRound = (ImageView) findViewById(R.id.imageView_round);
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.profile);
        imageViewRound.setImageBitmap(icon);

        SwitchCompat switchCompat = (SwitchCompat) findViewById(R.id
                .togglebutton);
        //switchCompat.setSwitchPadding(40);
        switchCompat.setOnCheckedChangeListener(this);

        Fragment fragment=new MapFragment();

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();


        }
    }


    private void setUpToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
    }

    private void setUpNavDrawer() {
        if (mToolbar != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mToolbar.setNavigationIcon(R.drawable.ic_menu);
            mToolbar.setTitleTextColor(Color.WHITE);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
            });
        }
    }


    private void setupDrawerContent(NavigationView navigationView) {

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.togglebutton:

                String str = Boolean.toString(isChecked);
                Utility.saveToSharedPrefs(this, "toggle_state", str);

                    UserState();

                break;
        }
    }


    private boolean UserState(){


        StringRequest jsonObjRequest = new StringRequest(Request.Method.POST,
                Constants.TOGGLE_STATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("response toggle state = ", response);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("volley", "Error: " + error.getMessage());
                error.printStackTrace();

            }
        }) {

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(Constants.USERID, Utility.getFromSharedPrefs(MainActivity.this, Constants.USERID));
                map.put(Constants.TOKEN, Utility.getFromSharedPrefs(MainActivity.this, Constants.TOKEN));
                return map;
            }

        };

        TempolaApplication.getInstance().addToRequestQueue(jsonObjRequest);


        return false;
    }


}