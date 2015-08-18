package com.lnet.tmsapp.second.version.core;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.lnet.tmsapp.R;
import com.lnet.tmsapp.Service.LocationService;
import com.lnet.tmsapp.application.ApplicationTrans;
import com.lnet.tmsapp.model.HttpHelper;

import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends ActionBarActivity implements FragmentDrawer.FragmentDrawerListener {

    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    RequestQueue requestQueue;
    SharedPreferences mySharedPreferences;
    ApplicationTrans application;

    Button logout;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Intent i= new Intent(Intent.ACTION_MAIN);
            i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            i.addCategory(Intent.CATEGORY_HOME);
            startActivity(i);
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        application = (ApplicationTrans)getApplication();
        mySharedPreferences = getSharedPreferences(application.getFILENAME(),application.getMODE());

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        logout = (Button)findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        // display the first navigation drawer view on app launch
        displayView(0);

        Intent intent = new Intent(getApplicationContext(), LocationService.class);
        getApplicationContext().startService(intent);

    }


    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                fragment = new HomeFragment();
                title = getString(R.string.title_home);
                break;
            case 1:
                fragment = new TransportOrderListFragment();
                title = getString(R.string.title_transport_order_list);
                break;
            case 2:
                fragment = new CarrierOrderListFragment();
                title = getString(R.string.title_carrier_order);
                break;
            case 3:
                fragment = new PaichedanListFragment();
                title = getString(R.string.title_paichedan_list);
                break;
            case 4:
                fragment = new FeeDeclareFragment();
                title = "费用申报";
                break;
            case 5:
                fragment = new PersonInfoFragment();
                title = getString(R.string.title_person_info);
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }
    }

    private void logout(){
        new AlertDialog.Builder(MainActivity.this).setTitle("退出登录").setMessage("确认退出？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        logoutService();
                    }})
                .setNegativeButton("取消",null)
                .show();
    }

    private void logoutService(){
        HttpHelper httpHelper = new HttpHelper(application,null,requestQueue,null) {
            @Override
            public void onResponse(JSONObject response) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
            @Override
            public void onErrorResponse(JSONObject response) {

            }
        };
        String httpUrl = mySharedPreferences.getString("serviceAddress","") + "/sysUser/logout";
        httpHelper.loginout(httpUrl, new JSONObject(new HashMap()));
    }
}