package com.lnet.tmsapp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.lnet.tmsapp.application.ApplicationTrans;
import com.lnet.tmsapp.model.HttpHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Administrator on 2015/7/30.
 */
public class ServiceConfigActivity extends Activity{
    EditText serviceAddress;
    EditText port;
    Button testButton;
    Button save;
    ProgressDialog testServiceDialog;

    RequestQueue requestQueue;
    SharedPreferences mySharedPreferences;
    SharedPreferences.Editor editor;
    ApplicationTrans application;

    LinearLayout linearLayout;

    @Override
    protected void onStart() {
        super.onStart();
        ActionBar actionBar = this.getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                Intent intent = new Intent(this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_config);
        serviceAddress = (EditText)findViewById(R.id.ser_address);
        port = (EditText)findViewById(R.id.port);
        testButton = (Button)findViewById(R.id.service_test_btn);
        save = (Button)findViewById(R.id.btn_service);
        application = (ApplicationTrans)getApplication();
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        mySharedPreferences = getSharedPreferences(application.getFILENAME(), application.getMODE());

        String ip = mySharedPreferences.getString("ip","");
        serviceAddress.setText(ip);
        String portS = mySharedPreferences.getString("port","");
        port.setText(portS);

        testButton.setOnClickListener(new TestListener());
        save.setOnClickListener(new SaveListener());

        //关闭输入法
        linearLayout = (LinearLayout)findViewById(R.id.linelayout1);
        linearLayout.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                return imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        });
    }

    private class TestListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            String text = serviceAddress.getText().toString().trim();
            if(!check(text)){
                showMassage("请输入服务器ip!");
                return;
            }
            if(!check(port.getText().toString().trim())){
                showMassage("请输入端口号!");
                return;
            }
            testService();
        }
    }
    private class SaveListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            String ip = serviceAddress.getText().toString().trim();
            if(!check(ip)){
                showMassage("请输入服务器ip!");
                return;
            }
            String portString = port.getText().toString().trim();
            if(!check(portString)){
                showMassage("请输入端口号!");
                return;
            }
            editor = mySharedPreferences.edit();
            String serAddress = serviceAddress.getText().toString().trim()+":"+port.getText().toString().trim();
            editor.putString("serviceAddress", "http://"+serAddress+"/service/rest");
            editor.putString("ip",ip);
            editor.putString("port",portString);
            editor.commit();
            Intent intent = new Intent();
            intent.setClass(ServiceConfigActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
    private void testService() {
        String address = serviceAddress.getText().toString().trim()+":"+port.getText().toString().trim();
        testServiceDialog = ProgressDialog.show(ServiceConfigActivity.this, "测试提示", "...测试中...");
        String serAddress = "http://"+address+"/service/rest/sysUser/testService";
        HttpHelper httpHelper = new HttpHelper(application,ServiceConfigActivity.this,requestQueue,testServiceDialog) {

            @Override
            public void onResponse(JSONObject response) {
                if(testServiceDialog!=null && testServiceDialog.isShowing()){
                    testServiceDialog.dismiss();
                }
                showMessage(ServiceConfigActivity.this, "测试成功！");
            }

            @Override
            public void onErrorResponse(JSONObject response) {
                showMessage(ServiceConfigActivity.this, "测试失败！");
            }
        };
        httpHelper.testService(serAddress);
    }
    private void showMassage(String massage){
        AlertDialog.Builder builder  = new AlertDialog.Builder(ServiceConfigActivity.this);
        builder.setTitle("提示" ) ;
        builder.setMessage(massage) ;
        builder.setPositiveButton("重新操作" ,  null );
        builder.show();
    }

    private boolean check(String value){
        if(value.length()!=0){
           return true;
        }
        return false;
    }
}
