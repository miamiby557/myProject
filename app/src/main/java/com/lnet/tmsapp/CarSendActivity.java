package com.lnet.tmsapp;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.lnet.tmsapp.application.ApplicationTrans;
import com.lnet.tmsapp.model.HttpHelper;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/7/30.
 */
public class CarSendActivity  extends FragmentActivity{
    EditText number ;
    Button carSend;
    LinearLayout linearLayout;

    SharedPreferences mySharedPreferences;
    String FILENAME = "info";
    int MODE = MODE_PRIVATE;
    RequestQueue requestQueue;
    ApplicationTrans application;
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
                Intent intent = new Intent(this, MainActivity.class);
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
        setContentView(R.layout.car_send);
        mySharedPreferences = getSharedPreferences(FILENAME, MODE);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        application = (ApplicationTrans)getApplication();
        number = (EditText)findViewById(R.id.order_number);
        linearLayout = (LinearLayout)findViewById(R.id.linelayout);
        linearLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                return imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        });
        number.setOnTouchListener(new TouchEvent());
        carSend = (Button)findViewById(R.id.car_send);
        carSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carSend();
            }
        });
    }

    private void carSend(){
        if(number.length()==0){
            showMassage("请输入派车单号");
            return;
        }
        Map<String,String> map = new HashMap<>();
        map.put("userId",application.getUserId());
        map.put("orderNumber", number.getText().toString().trim());
        ProgressDialog progressDialog = ProgressDialog.show(getApplicationContext(), "提示", "发车中...");
        String url = mySharedPreferences.getString("serviceAddress", "") +"/location/carSend";
                HttpHelper helper = new HttpHelper(application,CarSendActivity.this,requestQueue,progressDialog) {
            @Override
            public void onResponse(JSONObject response) {
                number.setText("");
                number.setHint("输入派车单号");
                Toast.makeText(CarSendActivity.this, "发车成功！", Toast.LENGTH_SHORT).show();
            }

                    @Override
                    public void onErrorResponse(JSONObject response) {

                    }
                };
        helper.post(url,new JSONObject(map));
    }

    private class TouchEvent implements View.OnTouchListener{

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                int size = number.getRight();
                int x = (int) event.getX();
                if (x > size - 100) {
                    //扫描
                    scan();
                    return true;
                }
                return false;
            }
            return false;
        }
    }

    private void scan(){
        Intent openCameraIntent = new Intent(CarSendActivity.this,CaptureActivity.class);
        startActivityForResult(openCameraIntent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //处理扫描结果（在界面上显示）
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
            number.setText(scanResult);
        }
    }

    private void showMassage(String massage){
        AlertDialog.Builder builder  = new AlertDialog.Builder(CarSendActivity.this);
        builder.setTitle("提示" ) ;
        builder.setMessage(massage) ;
        builder.setPositiveButton("重新操作" ,  null );
        builder.show();
    }
}
