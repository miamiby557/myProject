package com.lnet.tmsapp.schedules;

import android.app.Service;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.lnet.tmsapp.application.ApplicationTrans;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;

/**
 * Created by admin on 2015/7/18.
 */
public class LocationSendWork extends TimerTask{

    LocationManager locationManager;
    String userId;
    String address;
    ApplicationTrans application;
    Service service;
    RequestQueue requestQueue;
    SharedPreferences preferences;

    public LocationSendWork() {
    }

    public LocationSendWork(LocationManager locationManager,ApplicationTrans application,SharedPreferences mySharedPreferences,Service service) {
        this.locationManager = locationManager;
        this.userId = application.getUserId();
        this.address = application.getSERVICE_ADDRESS();
        this.service = service;
        this.application = application;
        this.preferences = mySharedPreferences;
    }

    @Override
    public void run() {
        //发送位置
        Location location = getLocation();
        if (location != null) {
            double  latitude = location.getLatitude();//纬度
            double longitude= location.getLongitude();//经度
            String locationName = getAddressName(latitude,longitude);
            //解析
            String city = null;
            if(locationName!=null){
                try {
                    JSONObject json = new JSONObject(locationName);
                    JSONObject result = json.getJSONObject("result");
                    JSONObject addressComponent=result.getJSONObject("addressComponent");
                    city = addressComponent.getString("city");
                    application.setCurrentCity(city);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            //向服务器发送位置
            if(city!=null){
                /*String currentCity = application.getCurrentCity();
                if(!city.equalsIgnoreCase(currentCity)){
                    sendLocation(city);
                }*/
                sendLocation(city);
            }
        }
    }

    private void sendLocation(final String city) {
        String ADDRESS =preferences.getString("serviceAddress", "")+"/location/receiveLocation";
        requestQueue = Volley.newRequestQueue(service.getApplicationContext());
        Map<String, String> map = new HashMap<String, String>();
        map.put("userId",userId);
        map.put("city", city);
        JSONObject jsonObject = new JSONObject(map);
        JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.POST,ADDRESS, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject object = response.getJSONObject("content");
                            String cityId = object.getString("regionId");
                            //保存位置
                            application.setCurrentCityId(cityId);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
        })
        {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json; charset=UTF-8");
                return headers;
            }
        };
        requestQueue.add(jsonRequest);
    }

    private String getAddressName(double latitude, double longitude) {
        String result = null;
        URL url = null;
        HttpURLConnection connection = null;
        InputStreamReader in = null;
        String ADDRESS = "http://api.map.baidu.com/geocoder?output=json&location="+latitude+","+longitude+"&key=APP_KEY";
        try {
            url = new URL(ADDRESS);
            connection = (HttpURLConnection) url.openConnection();
            in = new InputStreamReader(connection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(in);
            StringBuffer strBuffer = new StringBuffer();
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                strBuffer.append(line);
            }
            result = strBuffer.toString();
            return result;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(connection!=null){
                connection.disconnect();
            }
            if(in!=null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private Location getLocation(){
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE); // 高精度
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW); // 低功耗
        String provider = locationManager.getBestProvider(criteria, true); // 获取GPS信息
        Location location = locationManager.getLastKnownLocation(provider);
        return location;
    }
}
