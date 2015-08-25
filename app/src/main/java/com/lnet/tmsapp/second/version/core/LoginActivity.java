package com.lnet.tmsapp.second.version.core;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.lnet.tmsapp.R;
import com.lnet.tmsapp.application.ApplicationTrans;
import com.lnet.tmsapp.model.HttpArrayHelper;
import com.lnet.tmsapp.model.HttpHelper;
import com.lnet.tmsapp.util.DataItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    Button loginButton;
    Button testService;
    EditText textName;
    EditText textPwd;
    CheckBox checkBox;
    LinearLayout linearLayout;
    ProgressDialog progressDialog;
    LocationManager lm;
    RequestQueue requestQueue;
    SharedPreferences mySharedPreferences;
    SharedPreferences.Editor editor;
    ApplicationTrans application;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login2);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        application = (ApplicationTrans)getApplication();
        lm=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        mySharedPreferences = getSharedPreferences(application.getFILENAME(),application.getMODE());
        loginButton = (Button) findViewById(R.id.btn_login);
        testService = (Button)findViewById(R.id.service_config);
        textName = (EditText)findViewById(R.id.login_name);
        textPwd = (EditText)findViewById(R.id.login_pwd);
        checkBox = (CheckBox)findViewById(R.id.rememberMe);
        //加载用户信息
        String userName = mySharedPreferences.getString("userName","");
        textName.setText(userName);
        String password = mySharedPreferences.getString("password","");
        textPwd.setText(password);

        linearLayout = (LinearLayout)findViewById(R.id.linelayout);
        linearLayout.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                return imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        });

        //保存用户信息
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editor = mySharedPreferences.edit();
                    editor.putString("userName", textName.getText().toString().trim());
                    editor.putString("password", textPwd.getText().toString().trim());
                    editor.commit();
                }
            }
        });



        //测试服务器
        testService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, ServiceConfigActivity.class);
                startActivity(intent);

            }
        });


        loginButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                //判断GPS是否正常启动
                if(!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                    Toast.makeText(LoginActivity.this, "请开启GPS导航...", Toast.LENGTH_LONG).show();
                    //返回开启GPS导航设置界面
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(intent,0);
                    return;
                }
                //改变登陆按钮颜色
                loginButton.setBackgroundColor(R.color.grey);
                String service_address = mySharedPreferences.getString("serviceAddress","");
                if(service_address.length()==0){
                    Toast.makeText(LoginActivity.this, "请先配置服务器...", Toast.LENGTH_LONG).show();
                    return;
                }
                progressDialog = ProgressDialog.show(LoginActivity.this, "登陆提示", "...登陆中...");


                Map<String, String> map = new HashMap<>();
                map.put("username", textName.getText().toString());
                map.put("password", textPwd.getText().toString());
                String httpUrl =service_address+"/sysUser/login";
                HttpHelper httpHelper = new HttpHelper(application,LoginActivity.this,requestQueue,progressDialog) {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String userId = response.getString("userId");
                            String userName = response.getString("username");
                            long nextTime = response.getLong("nextTime");
                            application.setUserId(userId);
                            application.setLoginName(userName);
                            application.setNextTime(nextTime);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        loadProvinces();
                        Intent intent = new Intent();
                        intent.setClass(LoginActivity.this, com.lnet.tmsapp.second.version.core.MainActivity.class);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), "欢迎您，" + application.getLoginName(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onErrorResponse(JSONObject response) {

                    }
                };
                httpHelper.login(httpUrl,new JSONObject(map),loginButton);
            }
        });
    }

    private void loadProvinces(){
        final List<DataItem> provinces = new ArrayList<>();
        HttpArrayHelper provinceHelper = new HttpArrayHelper(application,LoginActivity.this,requestQueue,null) {
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
        String cityUrl =mySharedPreferences.getString("serviceAddress","")+"/order/getSupperBaseRegion";
        provinceHelper.get(cityUrl);
    }

}
