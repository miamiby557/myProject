package com.lnet.tmsapp.second.version.core;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.lnet.tmsapp.R;
import com.lnet.tmsapp.application.ApplicationTrans;

/**
 * Created by Administrator on 2015/7/31.
 */
public class StartupActivity extends FragmentActivity{
    ApplicationTrans application;
    RequestQueue requestQueue;
    SharedPreferences mySharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startup);
        application = (ApplicationTrans)getApplication();
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        mySharedPreferences = getSharedPreferences(application.getFILENAME(),application.getMODE());
        new Handler().postDelayed(new Runnable() {
            public void run() {
                Intent mainIntent = new Intent(StartupActivity.this, LoginActivity.class);
                startActivity(mainIntent);
                finish();
            }
        }, 3000);
    }

    /*private void loadData() {
        //省份优先加载
        String url = mySharedPreferences.getString("serviceAddress","");
        if(url.length()!=0){
            final List<DataItem> provinces = new ArrayList<>();
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            HttpArrayHelper cityArrayHelper = new HttpArrayHelper(application,StartupActivity.this,queue,null) {
                @Override
                public void onResponse(JSONArray response) {
                    DataItem defaultItem = new DataItem("","选择省份");
                    provinces.add(defaultItem);
                    for(int i = 0;i<response.length();i++){
                        try {
                            JSONObject object = response.getJSONObject(i);
                            DataItem dataItem = new DataItem(object.getString("regionId"),object.getString("name"));
                            provinces.add(dataItem);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    application.setProvinces(provinces);
                }
            };
            String cityUrl =url+"/order/getSupperBaseRegion";
            cityArrayHelper.get(cityUrl);
        }
    }*/
}
