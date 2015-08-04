package com.lnet.tmsapp;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.lnet.tmsapp.application.ApplicationTrans;
import com.lnet.tmsapp.model.HttpArrayHelper;
import com.lnet.tmsapp.model.HttpHelper;
import com.lnet.tmsapp.model.OtdCarrierOrderDetail;
import com.lnet.tmsapp.model.OtdTransportOrder;
import com.lnet.tmsapp.util.ArrayAdapterUtils;
import com.lnet.tmsapp.util.DataItem;
import com.lnet.tmsapp.util.DataUtils;
import com.lnet.tmsapp.util.JsonHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by admin on 2015/7/4.
 */
public class TransportOrderActivity extends FragmentActivity{

    Spinner clientId;
    EditText clientIdValue;
    EditText clientOrderNumber;
    EditText destCityIdValue;
    EditText totalVolume;
    EditText totalWeight;
    ProgressDialog progressDialog;
    ScrollView scrollView;
    Spinner province;
    Spinner destCity;
    Button more;
    LinearLayout moreLine;

    Spinner transportType;
    EditText transportTypeValue;
    Spinner calculateType;
    EditText calculateTypeValue;
    Spinner paymentType;
    EditText paymentTypeValue;
    EditText company;
    EditText receiveMan;
    EditText receiveManPhone;
    EditText receiveAddress;
    RequestQueue requestQueue;



    ApplicationTrans application;
    SharedPreferences mySharedPreferences;
    String FILENAME = "info";
    int MODE = MODE_PRIVATE;

    @Override
    protected void onStart() {
        super.onStart();
        ActionBar actionBar = this.getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_create, menu);
        return super.onCreateOptionsMenu(menu);
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
            case R.id.create_pic:
                saveOrder();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transportorder_create);
        application = (ApplicationTrans)getApplication();
        mySharedPreferences = getSharedPreferences(FILENAME, MODE);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        clientId = (Spinner)findViewById(R.id.spinner_client);
        clientIdValue = new EditText(getApplicationContext());
        transportType = (Spinner)findViewById(R.id.t_transportType);
        transportTypeValue = new EditText(getApplicationContext());
        calculateType = (Spinner)findViewById(R.id.t_calculateType);
        calculateTypeValue = new EditText(getApplicationContext());
        paymentType = (Spinner)findViewById(R.id.t_paymentType);
        paymentTypeValue = new EditText(getApplicationContext());
        company = (EditText)findViewById(R.id.t_company);
        receiveMan = (EditText)findViewById(R.id.t_receiveMan);
        receiveManPhone = (EditText)findViewById(R.id.t_receiveManPhone);
        receiveAddress = (EditText)findViewById(R.id.t_receiveManAddress);
        clientOrderNumber = (EditText)findViewById(R.id.t_number);
        clientOrderNumber.setOnFocusChangeListener(new FocusChangeListener());
        province = (Spinner)findViewById(R.id.t_province);
        destCity = (Spinner)findViewById(R.id.t_city);
        destCityIdValue = new EditText(getApplicationContext());
        totalVolume = (EditText)findViewById(R.id.t_totalvolume);
        totalWeight = (EditText)findViewById(R.id.t_totalweight);
        more = (Button)findViewById(R.id.more);
        moreLine = (LinearLayout)findViewById(R.id.line_more);
        more.setOnClickListener(new MoreListener());

        //扫描二维码
        clientOrderNumber.setOnTouchListener(new TouchEvent());

        scrollView = (ScrollView)findViewById(R.id.t_scrollview);
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                return imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        });
        bindProvince();
        getClient();
    }

    private class TouchEvent implements View.OnTouchListener{

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                int size = clientOrderNumber.getRight();
                int x = (int) event.getX();
                if (x > size - 100) {
                    //扫描
                    Intent openCameraIntent = new Intent(TransportOrderActivity.this,CaptureActivity.class);
                    startActivityForResult(openCameraIntent, 0);
                    return true;
                }
                return false;
            }
            return false;
        }
    }

    private class FocusChangeListener implements View.OnFocusChangeListener{

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(!hasFocus){
                String orderNumber = clientOrderNumber.getText().toString().trim();
               if(check(orderNumber)){
                   //检查是否有此客户单号
                   String httpUrl = mySharedPreferences.getString("serviceAddress", "")+"/order/transportOrder/"+orderNumber;
                   JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, httpUrl,
                           new Response.Listener<JSONObject>() {
                               @Override
                               public void onResponse(JSONObject response) {
                                   try {
                                       Boolean isSuccess = response.getBoolean("success");
                                       if(isSuccess){
                                           AlertDialog.Builder builder  = new AlertDialog.Builder(TransportOrderActivity.this);
                                           builder.setTitle("提示" ) ;
                                           builder.setMessage("已经有此单号！" ) ;
                                           builder.setPositiveButton("重新输入" ,  null );
                                           builder.show();
                                       }
                                   } catch (JSONException e) {
                                       e.printStackTrace();
                                   }
                               }
                           },
                           new Response.ErrorListener() {
                               @Override
                               public void onErrorResponse(VolleyError error) {
                                   if (progressDialog.isShowing() && progressDialog != null) {
                                       progressDialog.dismiss();
                                   }
                                   showMassage("出现异常，请重新操作！");
                               }
                           }
                   );
                   requestQueue.add(request);
               }
            }
        }
    }

    private void saveOrder(){
        //判断必需数据
        if(!check(clientIdValue.getText().toString())){
            showMassage("客户为空！");
            return;
        }
        if(!check(clientOrderNumber.getText().toString().trim())){
            showMassage("单号为空！");
            return;
        }
        if(!check(destCityIdValue.getText().toString().trim())){
            showMassage("目的地为空！");
            return;
        }
        if(!check(totalVolume.getText().toString().trim())){
            showMassage("体积为空！");
            return;
        }
        if(!check(totalWeight.getText().toString().trim())){
            showMassage("重量为空！");
            return;
        }
        OtdTransportOrder order = new OtdTransportOrder();
        if(check(transportTypeValue.getText().toString())){
            order.setTransportType(Integer.parseInt(transportTypeValue.getText().toString()));
        }
        if(check(calculateTypeValue.getText().toString())){
            order.setCalculateType(Integer.parseInt(calculateTypeValue.getText().toString()));
        }
        if(check(paymentTypeValue.getText().toString())){
            order.setPaymentType(Integer.parseInt(paymentTypeValue.getText().toString()));
        }
        order.update(company.getText().toString().trim(),receiveMan.getText().toString().trim(),receiveManPhone.getText().toString().trim(),receiveAddress.getText().toString().trim());
        progressDialog = ProgressDialog.show(TransportOrderActivity.this, "创建提示", "...创建中...");
        String userId = application.getUserId();
        String cityId = application.getCurrentCityId();
        if(check(cityId)){
            order.setStartCityId(UUID.fromString(cityId));
        }
        order.update(clientOrderNumber.getText().toString().trim(), UUID.fromString(clientIdValue.getText().toString().trim()), UUID.fromString(destCityIdValue.getText().toString())
                , new Double(totalVolume.getText().toString().trim()), new Double(totalWeight.getText().toString().trim()), UUID.fromString(userId));
        Map object = JsonHelper.toMap(order);
        String httpUrl = mySharedPreferences.getString("serviceAddress","")+"/order/transportOrderCreate";
        HttpHelper httpHelper = new HttpHelper(application,TransportOrderActivity.this,requestQueue,progressDialog) {
            @Override
            public void onResponse(JSONObject response) {
                if (progressDialog.isShowing() && progressDialog != null) {
                    progressDialog.dismiss();
                }
                finish();
                showToast("创建运输单成功！");
            }

            @Override
            public void onErrorResponse(JSONObject response) {

            }
        };
        httpHelper.post(httpUrl,new JSONObject(object));
    }

    private void bindProvince(){
        //绑定省份
        List<DataItem> provinces = application.getProvinces();
        ArrayAdapter<DataItem> endAdapter = new ArrayAdapter<>(getApplicationContext(),R.layout.list_item1,provinces);
        province.setAdapter(endAdapter);
        province.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String superBaseRegionId = ((DataItem) province.getSelectedItem()).getTextValue();
                if(superBaseRegionId.length()!=0){
                    //绑定城市
                    destCity.setVisibility(View.VISIBLE);
                    final List<DataItem> cities = new ArrayList<>();
                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    HttpArrayHelper cityArrayHelper = new HttpArrayHelper(application,TransportOrderActivity.this,queue,null) {
                        @Override
                        public void onResponse(JSONArray response) {
                            DataItem defaultItem = new DataItem("","选择城市");
                            cities.add(defaultItem);
                            for(int i = 0;i<response.length();i++){
                                try {
                                    JSONObject object = response.getJSONObject(i);
                                    DataItem dataItem = new DataItem(object.getString("regionId"),object.getString("name"));
                                    cities.add(dataItem);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            //绑定城市
                            ArrayAdapter<DataItem> myaAdapter = new ArrayAdapter<>(getApplicationContext(),R.layout.list_item1,cities);
                            destCity.setAdapter(myaAdapter);
                            destCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    String textValue = ((DataItem) destCity.getSelectedItem()).getTextValue();
                                    destCityIdValue.setText(textValue);
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> arg0) {
                                    // TODO Auto-generated method stub
                                }
                            });
                        }
                    };
                    String cityUrl = mySharedPreferences.getString("serviceAddress","")+"/order/getChildBaseRegion/"+superBaseRegionId;
                    cityArrayHelper.get(cityUrl);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getClient(){
        //得到用户client信息
        final List<DataItem> clientDatas = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        HttpArrayHelper arrayHelper = new HttpArrayHelper(application,TransportOrderActivity.this,requestQueue,null) {
            @Override
            public void onResponse(JSONArray response) {
                DataItem defaultItem = new DataItem("","请选择客户：");
                clientDatas.add(defaultItem);
                for(int i = 0;i<response.length();i++){
                    try {
                        JSONObject object = response.getJSONObject(i);
                        DataItem dataItem = new DataItem(object.getString("clientId"),object.getString("name"));
                        clientDatas.add(dataItem);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    ArrayAdapter<DataItem> myaAdapter = new ArrayAdapter<>(getApplicationContext(),R.layout.list_item1,clientDatas);
                    clientId.setAdapter(myaAdapter);
                    clientId.setVisibility(View.VISIBLE);
                    clientId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String textValue = ((DataItem) clientId.getSelectedItem()).getTextValue();
                            clientIdValue.setText(textValue);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {
                            // TODO Auto-generated method stub
                        }
                    });
                }
            }
        };
        String httpUrl = mySharedPreferences.getString("serviceAddress", "") + "/crmClient/getResource";
        arrayHelper.post(httpUrl, new JSONObject());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //处理扫描结果（在界面上显示）
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
            clientOrderNumber.setText(scanResult);
        }
    }

    private class MoreListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if(View.GONE==moreLine.getVisibility()){
                moreLine.setVisibility(View.VISIBLE);
                more.setText("隐藏更多...");
            }else if(View.VISIBLE==moreLine.getVisibility()){
                moreLine.setVisibility(View.GONE);
                more.setText("显示更多...");
            }
            ArrayAdapterUtils.adapter(transportType, DataUtils.getTransportType(),getApplicationContext(),transportTypeValue);
            ArrayAdapterUtils.adapter(calculateType,DataUtils.getCalculateType(),getApplicationContext(),calculateTypeValue);
            ArrayAdapterUtils.adapter(paymentType,DataUtils.getPaymentType(),getApplicationContext(),paymentTypeValue);
        }
    }

    private boolean check(String value){
        if(value.length()!=0){
            return true;
        }
        return false;
    }

    private void showMassage(String massage){
        AlertDialog.Builder builder  = new AlertDialog.Builder(TransportOrderActivity.this);
        builder.setTitle("提示" ) ;
        builder.setMessage(massage) ;
        builder.setPositiveButton("重新输入" ,  null );
        builder.show();
    }

    private void showToast(String massage){
        Toast.makeText(getApplicationContext(), massage, Toast.LENGTH_LONG).show();
    }

}
