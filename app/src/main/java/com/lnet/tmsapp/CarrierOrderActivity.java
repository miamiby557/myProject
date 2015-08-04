package com.lnet.tmsapp;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.text.InputType;
import android.view.Gravity;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.lnet.tmsapp.application.ApplicationTrans;
import com.lnet.tmsapp.model.FeeOrderPayableJson;
import com.lnet.tmsapp.model.FeeOrderPayables;
import com.lnet.tmsapp.model.HttpArrayHelper;
import com.lnet.tmsapp.model.OtdCarrierOrder;
import com.lnet.tmsapp.model.OtdCarrierOrderBean;
import com.lnet.tmsapp.model.OtdCarrierOrderDetail;
import com.lnet.tmsapp.model.ServiceResult;
import com.lnet.tmsapp.util.ArrayAdapterUtils;
import com.lnet.tmsapp.util.DataItem;
import com.lnet.tmsapp.util.DataUtils;
import com.lnet.tmsapp.util.GsonRequest;
import com.lnet.tmsapp.util.JsonHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Created by admin on 2015/7/4.
 */
public class CarrierOrderActivity extends FragmentActivity{

    Button calculateButton;
    Button addTransButton;
    Button additionOther;
    Spinner carrierId;
    EditText carrierIdValue;
    EditText carrierOrderNumber;
    Spinner province;
    Spinner destCity;
    EditText destCityIdValue;
    EditText totalPackageQuantity;
    EditText totalVolume;
    EditText totalWeight;
    Spinner transportType;
    EditText transportTypeValue;
    Spinner calculateType;
    EditText calculateTypeValue;
    CheckBox isUpstairs;
    EditText addOrderNumber;
    TableLayout transportOrderTable;
    TableLayout calculate_Table;
    int rowId=0;
    ScrollView activity_main;
    ProgressDialog progressDialog;
    List<String> ids = new ArrayList<>();
    Set<OtdCarrierOrderDetail> details;
//    OtdCarrierOrder otdCarrierOrder;
    OtdCarrierOrderBean otdCarrierOrderBean = new OtdCarrierOrderBean();
    Set<FeeOrderPayableJson> payableJsons;
    ApplicationTrans application;
    SharedPreferences mySharedPreferences;
    String FILENAME = "info";
    int MODE = MODE_PRIVATE;

    OtdCarrierOrderBean orderAddMore;

    RequestQueue requestQueue;

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
        setContentView(R.layout.carrierorder_create);
        application = (ApplicationTrans)getApplication();
        mySharedPreferences = getSharedPreferences(FILENAME, MODE);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        details=new HashSet<>();
        payableJsons = new HashSet<>();
        calculateButton = (Button)findViewById(R.id.c_calculate);
        addTransButton = (Button)findViewById(R.id.add_transorder);
        additionOther = (Button)findViewById(R.id.addition);
        carrierId = (Spinner)findViewById(R.id.spinner_carrier);
        carrierIdValue = new EditText(getApplicationContext());
        carrierOrderNumber = (EditText)findViewById(R.id.c_number);
        carrierOrderNumber.setOnTouchListener(new TouchEvent());
        carrierOrderNumber.setOnFocusChangeListener(new FocusListener());
        province = (Spinner)findViewById(R.id.c_province);
        destCity = (Spinner)findViewById(R.id.c_city);
        destCityIdValue = new EditText(getApplicationContext());
        totalPackageQuantity = (EditText)findViewById(R.id.totalPackageQuantity);
        totalVolume = (EditText)findViewById(R.id.c_totalvolume);
        totalWeight = (EditText)findViewById(R.id.c_totalweight);
        transportType = (Spinner)findViewById(R.id.c_transportType);
        transportTypeValue = new EditText(getApplicationContext());
        calculateType = (Spinner)findViewById(R.id.c_calculateType);
        calculateTypeValue = new EditText(getApplicationContext());
        isUpstairs = (CheckBox)findViewById(R.id.upstairs);
        calculate_Table = (TableLayout)findViewById(R.id.carrierorder_calculate_table);
        transportOrderTable = (TableLayout)findViewById(R.id.add_transportorder_table);

        addOrderNumber = (EditText)findViewById(R.id.add_order);

        additionOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //完善订单信息
                Intent openCameraIntent = new Intent(CarrierOrderActivity.this,CarrierOrderAddOtherActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("order",orderAddMore);
                openCameraIntent.putExtras(bundle);
                startActivityForResult(openCameraIntent, 0);
            }
        });


        addTransButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTransportOrder();
            }
        });
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateOrder();
            }
        });
        activity_main=(ScrollView) findViewById(R.id.c_scrollview);
        activity_main.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                return imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        });

        bindProvince();
        bindCarrier();

        //spinner绑定属性
        ArrayAdapterUtils.adapter(transportType, DataUtils.getTransportType(), this, transportTypeValue);
        ArrayAdapterUtils.adapter(calculateType, DataUtils.getCalculateType(), this, calculateTypeValue);

    }

    private class TouchEvent implements View.OnTouchListener{

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                int size = carrierOrderNumber.getRight();
                int x = (int) event.getX();
                if (x > size - 100) {
                    //扫描
                    Intent openCameraIntent = new Intent(CarrierOrderActivity.this,CaptureActivity.class);
                    startActivityForResult(openCameraIntent, 0);
                    return true;
                }
                return false;
            }
            return false;
        }
    }

    private class FocusListener implements View.OnFocusChangeListener{

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            //判断是否有此单号
            if(!hasFocus){
                String orderNumber = carrierOrderNumber.getText().toString().trim();
               if(!check(orderNumber)){
                   String httpUrl = mySharedPreferences.getString("serviceAddress", "")+"/order/carrierOrder/"+orderNumber;
                   JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, httpUrl,
                           new Response.Listener<JSONObject>() {
                               @Override
                               public void onResponse(JSONObject response) {
                                   try {
                                       Boolean isSuccess = response.getBoolean("success");
                                       if(isSuccess){
                                           AlertDialog.Builder builder  = new AlertDialog.Builder(CarrierOrderActivity.this);
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
        if(check(carrierIdValue.getText().toString())){
            showMassage("承运商为空！");
        }
        if(check(carrierOrderNumber.getText().toString().trim())){
            showMassage("托运单号为空！");
            return;
        }
        if(check(destCityIdValue.getText().toString())){
            showMassage("目的城市为空！");
            return;
        }
        if(check(totalPackageQuantity.getText().toString().trim())) {
            showMassage("件数为空！");
            return;
        }
        if(check(totalVolume.getText().toString().trim())){
            showMassage("体积为空！");
            return;
        }
        if(check(totalWeight.getText().toString().trim())){
            showMassage("重量为空！");
            return;
        }
        if(check(transportTypeValue.getText().toString())){
            showMassage("请选择运输方式！");
            return;
        }
        if(check(calculateTypeValue.getText().toString())){
            showMassage("请选择计费方式！");
            return;
        }
        if(ids.size()==0){
            showMassage("请至少输入一个运输单！");
            return;
        }

        //更新费用数据
        for (int i = 0; i < payableJsons.size(); i++) {
            EditText text = (EditText) findViewById(i + 100);
            for (FeeOrderPayableJson json : payableJsons) {
                if (i + 100 == json.getIndex()) {
                    json.setExacctMoney(new Double(text.getText().toString().trim()));
                }
            }
        }
        //得到当前城市ID
        String cityId = application.getCurrentCityId();
        createCarrierOrder(cityId);
    }

    private void addTransportOrder(){
        String orderNumber = addOrderNumber.getText().toString().trim();
        if(ids.contains(orderNumber)){
            showMassage("已经添加此单号！");
            return;
        }
        if(orderNumber.length()==0){
            showMassage("请输入运输单号！");
            return;
        }
        progressDialog = ProgressDialog.show(CarrierOrderActivity.this, "提示", "...提取运输订单中...");
        //从数据库得到运输订单
        String httpUrl = mySharedPreferences.getString("serviceAddress", "")+"/order/transportOrder/"+orderNumber;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, httpUrl,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (progressDialog.isShowing() && progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        try {
                            Boolean isSuccess = response.getBoolean("success");
                            if(!isSuccess){
                                AlertDialog.Builder builder  = new AlertDialog.Builder(CarrierOrderActivity.this);
                                builder.setTitle("提示" ) ;
                                builder.setMessage("没有此单号！" ) ;
                                builder.setPositiveButton("重新输入" ,  null );
                                builder.show();
                            }else{
                                try {
                                    JSONObject content = response.getJSONObject("content");
                                    String clientOrderNumber = content.getString("clientOrderNumber");
                                    String transportOrderId = content.getString("transportOrderId");
                                    Double confirmedVolume = content.getDouble("confirmedVolume");
                                    Double confirmedWeight = content.getDouble("confirmedWeight");
                                    OtdCarrierOrderDetail detail = new OtdCarrierOrderDetail();
                                    detail.setTransportOrderId(UUID.fromString(transportOrderId));
                                    detail.setConfirmedVolume(confirmedVolume);
                                    detail.setConfirmedWeight(confirmedWeight);
                                    details.add(detail);
                                    ids.add(clientOrderNumber);
                                    addTransportOrder(transportOrderTable, clientOrderNumber, confirmedVolume, confirmedWeight, detail);
                                    shoeToast("添加运输单成功！");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    showMassage("此单某些数据不完整！");
                                }
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

    private void calculateOrder(){
        //判断必需数据
        if(check(carrierIdValue.getText().toString())){
            showMassage("承运商为空！");
        }
        if(check(carrierOrderNumber.getText().toString().trim())){
            showMassage("托运单号为空！");
            return;
        }
        if(check(destCityIdValue.getText().toString())){
            showMassage("目的城市为空！");
            return;
        }
        if(check(totalPackageQuantity.getText().toString().trim())) {
            showMassage("件数为空！");
            return;
        }
        if(check(totalVolume.getText().toString().trim())){
            showMassage("体积为空！");
            return;
        }
        if(check(totalWeight.getText().toString().trim())){
            showMassage("重量为空！");
            return;
        }
        if(check(transportTypeValue.getText().toString())){
            showMassage("请选择运输方式！");
            return;
        }
        if(check(calculateTypeValue.getText().toString())){
            showMassage("请选择计费方式！");
            return;
        }

        //计算
        //重复计算，先清除原有row
        payableJsons.clear();
        int count = calculate_Table.getChildCount();
        if(count>1){
            for(int i=count;i>0;i--){
                calculate_Table.removeView(calculate_Table.getChildAt(i));
            }
        }
        final ProgressDialog calculateDialog = ProgressDialog.show(CarrierOrderActivity.this, "提示", "...计算中...");
        String userId = application.getUserId();
        otdCarrierOrderBean.update(UUID.fromString(carrierIdValue.getText().toString()), null,
                UUID.fromString(destCityIdValue.getText().toString()),
                carrierOrderNumber.getText().toString(), Integer.parseInt(calculateTypeValue.getText().toString()),
                Integer.parseInt(transportTypeValue.getText().toString()), UUID.fromString(userId), Integer.parseInt(totalPackageQuantity.getText().toString().trim()),
                new Double(totalVolume.getText().toString().trim()), new Double(totalWeight.getText().toString().trim()),
                isUpstairs.isChecked() ? true : false);
        Map map = JsonHelper.toMap(otdCarrierOrderBean);
        String httpUrl = mySharedPreferences.getString("serviceAddress","")+"/order/calculate";
        HttpArrayHelper httpHelper = new HttpArrayHelper(application,CarrierOrderActivity.this,requestQueue,calculateDialog) {
            @Override
            public void onResponse(JSONArray response) {
                for(int i =0;i<response.length();i++){
                    try {
                        JSONObject object = response.getJSONObject(i);
                        String exacctName = object.getString("exacctName");
                        Double exacctMoney = object.getDouble("exacctMoney");
                        String exacctId = object.getString("exacctId");
                        FeeOrderPayableJson json = new FeeOrderPayableJson(i+100,exacctName,exacctMoney,exacctId);
                        payableJsons.add(json);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                for(FeeOrderPayableJson json:payableJsons){
                    addResult(calculate_Table,json);
                }
                if (calculateDialog.isShowing() && calculateDialog != null) {
                    calculateDialog.dismiss();
                }
                Handler mHandler = new Handler();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        activity_main.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                });
            }
        };
        httpHelper.post(httpUrl,new JSONObject(map));
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
                if (superBaseRegionId.length() != 0) {
                    //绑定城市
                    destCity.setVisibility(View.VISIBLE);
                    final List<DataItem> cities = new ArrayList<>();
                    HttpArrayHelper cityArrayHelper = new HttpArrayHelper(application, CarrierOrderActivity.this, requestQueue, null) {
                        @Override
                        public void onResponse(JSONArray response) {
                            DataItem defaultItem = new DataItem("", "选择城市");
                            cities.add(defaultItem);
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject object = response.getJSONObject(i);
                                    DataItem dataItem = new DataItem(object.getString("regionId"), object.getString("name"));
                                    cities.add(dataItem);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            //绑定城市
                            ArrayAdapter<DataItem> myaAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.list_item1, cities);
                            destCity.setAdapter(myaAdapter);
                            destCity.setVisibility(View.VISIBLE);
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
                    String cityUrl = mySharedPreferences.getString("serviceAddress", "") + "/order/getChildBaseRegion/" + superBaseRegionId;
                    cityArrayHelper.get(cityUrl);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void bindCarrier(){
        //绑定承运商
        final List<DataItem> carrierDatas = new ArrayList<>();
        HttpArrayHelper arrayHelper = new HttpArrayHelper(application,CarrierOrderActivity.this,requestQueue,null) {
            @Override
            public void onResponse(JSONArray response) {
                DataItem defaultItem = new DataItem("","请选择承运商");
                carrierDatas.add(defaultItem);
                for(int i = 0;i<response.length();i++){
                    try {
                        JSONObject object = response.getJSONObject(i);
                        DataItem dataItem = new DataItem(object.getString("carrierId"),object.getString("name"));
                        carrierDatas.add(dataItem);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    ArrayAdapter<DataItem> myaAdapter = new ArrayAdapter<>(getApplicationContext(),R.layout.list_item1,carrierDatas);
                    carrierId.setAdapter(myaAdapter);
                    carrierId.setVisibility(View.VISIBLE);
                    carrierId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String textValue = ((DataItem) carrierId.getSelectedItem()).getTextValue();
                            carrierIdValue.setText(textValue);
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {
                            // TODO Auto-generated method stub
                        }
                    });
                }
            }
        };
        String carrierUrl = mySharedPreferences.getString("serviceAddress", "")+"/scmCarrier/getResource";
        arrayHelper.post(carrierUrl, new JSONObject());
    }

    private void addTransportOrder(final TableLayout table, final String orderNumber,Double confirmedVolume,Double confirmedWeight, final OtdCarrierOrderDetail detail){
        TableRow row = new TableRow(this);
        row.setId(rowId);
        TextView order = new TextView(this);
        order.setText(orderNumber);
        order.setPadding(3, 3, 3, 3);
        order.setGravity(Gravity.CENTER);

        TextView volume = new TextView(this);
        volume.setText(confirmedVolume + "");
        volume.setPadding(3, 3, 3, 3);
        volume.setGravity(Gravity.CENTER);

        TextView weight = new TextView(this);
        weight.setText(confirmedWeight + "");
        weight.setPadding(3, 3, 3, 3);
        weight.setGravity(Gravity.CENTER);

        Button button = new Button(this);
        button.setWidth(50);
        button.setHeight(40);
        button.setId(rowId + 1000);
        button.setText("删除");
        button.setPadding(3, 3, 3, 3);
        button.setGravity(Gravity.CENTER);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                table.removeView(table.findViewById(v.getId() - 1000));
                ids.remove(orderNumber);
                details.remove(detail);
            }
        });

        row.addView(order);
        row.addView(volume);
        row.addView(weight);
        row.addView(button);

        table.addView(row);
        rowId++;
        addOrderNumber.setText(null);
        addOrderNumber.setHint("输入运输单号");
        closeInput();
    }

    private void addResult(TableLayout table,FeeOrderPayableJson json){
        TableRow row = new TableRow(this);
        TextView exacctName = new TextView(this);
        exacctName.setText(json.getExacctName() + ":");
        exacctName.setGravity(Gravity.RIGHT);

        EditText exacctMoney = new EditText(this);
        exacctMoney.setId(json.getIndex());
        exacctMoney.setText(json.getExacctMoney() + "");
        exacctMoney.setWidth(300);
        exacctMoney.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        exacctMoney.setGravity(Gravity.CENTER);
        row.addView(exacctName);
        row.addView(exacctMoney);
        table.addView(row);
    }

    private void createCarrierOrder(String cityId){
        final Gson gson = JsonHelper.getGson();
        String httpUrl = mySharedPreferences.getString("serviceAddress", "") + "/order/carrierOrderCreate";
        final ProgressDialog createCarrierOrderProgressDialog = ProgressDialog.show(CarrierOrderActivity.this, "提示", "...创建中...");
        if(!check(cityId)){
            otdCarrierOrderBean.setStartCityId(UUID.fromString(cityId));
        }
        otdCarrierOrderBean.setDetails(details);
        String json = gson.toJson(otdCarrierOrderBean);
        GsonRequest<ServiceResult> gsonRequest = new GsonRequest(httpUrl, ServiceResult.class, json, new Response.Listener<ServiceResult>() {
            @Override
            public void onResponse(ServiceResult response) {
                if (createCarrierOrderProgressDialog.isShowing() && createCarrierOrderProgressDialog != null) {
                    createCarrierOrderProgressDialog.dismiss();
                }
                details.clear();
                Map<String, String> map = (Map<String, String>) response.getContent();
                if (map != null) {
                    //创建成功后 ，更新费用
                    String orderPayableId = map.get("orderPayableId");
                    final ProgressDialog updateFeeProgressDialog = ProgressDialog.show(CarrierOrderActivity.this, "提示", "...更新费用中...");
                    FeeOrderPayables feeOrderPayables = new FeeOrderPayables(payableJsons);
                    String feeJson = gson.toJson(feeOrderPayables);
                    String updateUrl = mySharedPreferences.getString("serviceAddress","") + "/order/updatePayable/" + orderPayableId;
                    GsonRequest<ServiceResult> gsonRequest1 = new GsonRequest(updateUrl, ServiceResult.class, feeJson, new Response.Listener<ServiceResult>() {
                        @Override
                        public void onResponse(ServiceResult response) {
                            if (updateFeeProgressDialog.isShowing() && updateFeeProgressDialog != null) {
                                updateFeeProgressDialog.dismiss();
                            }
                            finish();
                            shoeToast("托运单已经创建！");
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (updateFeeProgressDialog.isShowing() && updateFeeProgressDialog != null) {
                                updateFeeProgressDialog.dismiss();
                            }
                            showMassage("服务器更新费用异常！");
                        }
                    }){
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map localHashMap = new HashMap();
                            localHashMap.put("Cookie", application.getCookie());
                            return localHashMap;
                        }
                    };
                    requestQueue.add(gsonRequest1);
                } else {
                    Map<String, String> error = response.getMessages();
                    String errorMassage = error.get("default");
                    showMassage(errorMassage);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (createCarrierOrderProgressDialog.isShowing() && createCarrierOrderProgressDialog != null) {
                    createCarrierOrderProgressDialog.dismiss();
                }
                showMassage("服务器创建托运单异常！");
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map localHashMap = new HashMap();
                localHashMap.put("Cookie", application.getCookie());
                return localHashMap;
            }
        };
        requestQueue.add(gsonRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode){
            case RESULT_OK:
                Bundle bundle = data.getExtras();
                String scanResult = bundle.getString("result");
                carrierOrderNumber.setText(scanResult);
                break;
            case 2:
                Bundle bundle2 = data.getExtras();
                Serializable serializable = bundle2.getSerializable("order");
                if(serializable!=null){
                    orderAddMore = (OtdCarrierOrderBean)serializable;
                    if(orderAddMore!=null){
                        updateOrder();
                    }
                }
                break;
            default:

                break;
        }
    }

    private void updateOrder() {
      otdCarrierOrderBean.update(orderAddMore);
    }


    private void shoeToast(String massage){
        Toast.makeText(getApplicationContext(), massage, Toast.LENGTH_SHORT).show();
    }

    private void closeInput(){
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputManger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void showMassage(String massage){
        AlertDialog.Builder builder  = new AlertDialog.Builder(CarrierOrderActivity.this);
        builder.setTitle("提示" ) ;
        builder.setMessage(massage) ;
        builder.setPositiveButton("重新操作" ,  null );
        builder.show();
    }

    private boolean check(String value){
        if(value.length()!=0){
            return false;
        }
        return true;
    }

}
