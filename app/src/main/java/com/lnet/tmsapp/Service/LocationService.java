package com.lnet.tmsapp.Service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.IBinder;

import com.lnet.tmsapp.application.ApplicationTrans;
import com.lnet.tmsapp.schedules.LocationSendWork;

import java.util.Timer;

/**
 * Created by admin on 2015/7/18.
 */
public class LocationService extends Service{

    LocationManager locationManager;
    SharedPreferences mySharedPreferences;
    String FILENAME = "info";
    int MODE = MODE_PRIVATE;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        mySharedPreferences = getSharedPreferences(FILENAME,MODE);
        Timer timer = new Timer();
        ApplicationTrans application = ((ApplicationTrans)getApplication());
        timer.schedule(new LocationSendWork(locationManager,application,mySharedPreferences,this),0,15*60*1000);//15分钟调用一次

    }
}
