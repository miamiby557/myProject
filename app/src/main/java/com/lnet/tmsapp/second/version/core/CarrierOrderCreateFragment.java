package com.lnet.tmsapp.second.version.core;

/**
 * Created by Andy on 2015/5/28.
 */
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
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
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.lnet.tmsapp.R;
import com.lnet.tmsapp.application.ApplicationTrans;
import com.lnet.tmsapp.model.FeeOrderPayableJson;
import com.lnet.tmsapp.model.FeeOrderPayables;
import com.lnet.tmsapp.model.HttpArrayHelper;
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
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;


public class CarrierOrderCreateFragment extends Fragment {

    Button calculateButton;
    Button addTransButton;
    Button additionOther;
    Spinner carrierId;
    EditText carrierIdValue;
    EditText carrierOrderNumber;
    String number;
    Spinner province1;
    Spinner province2;
    Spinner startCity;
    EditText startCityIdValue;
    Spinner destCity;
    EditText destCityIdValue;
    EditText totalVolume;
    EditText totalWeight;
    EditText totalItemQuantity;
    EditText totalPackageQuantity;
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
    List<String> numbers = new ArrayList<>();
    Set<OtdCarrierOrderDetail> details;
    OtdCarrierOrderBean otdCarrierOrderBean = new OtdCarrierOrderBean();
    Set<FeeOrderPayableJson> payableJsons;
    ApplicationTrans application;
    SharedPreferences mySharedPreferences;

    OtdCarrierOrderBean orderAddMore;

    ProgressDialog loading;

    RequestQueue requestQueue;
    View rootView;

    public CarrierOrderCreateFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main2, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_create:
                create();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void create(){
        new AlertDialog.Builder(getActivity()).setTitle("创建托运单").setMessage("确认创建？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        saveOrder();
                    }})
                .setNegativeButton("取消",null)
                .show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.carrierorder_create, container, false);
        loading = ProgressDialog.show(getActivity(), "提示", "加载中...");
        application = (ApplicationTrans)getActivity().getApplication();
        mySharedPreferences = getActivity().getSharedPreferences(application.getFILENAME(), application.getMODE());
        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        details=new HashSet<>();
        payableJsons = new HashSet<>();
        calculateButton = (Button)rootView.findViewById(R.id.c_calculate);
        addTransButton = (Button)rootView.findViewById(R.id.add_transorder);
        additionOther = (Button)rootView.findViewById(R.id.addition);
        carrierId = (Spinner)rootView.findViewById(R.id.spinner_carrier);
        carrierIdValue = new EditText(getActivity().getApplicationContext());
        carrierOrderNumber = (EditText)rootView.findViewById(R.id.c_number);
        carrierOrderNumber.setOnTouchListener(new CarrierOrderTouchEvent());
        carrierOrderNumber.setOnFocusChangeListener(new FocusListener());
        province1 = (Spinner)rootView.findViewById(R.id.c_province1);
        province2 = (Spinner)rootView.findViewById(R.id.c_province2);
        startCity = (Spinner)rootView.findViewById(R.id.c_start_city);
        startCityIdValue = new EditText(getActivity().getApplicationContext());
        destCity = (Spinner)rootView.findViewById(R.id.c_dest_city);
        destCityIdValue = new EditText(getActivity().getApplicationContext());
        totalItemQuantity = (EditText)rootView.findViewById(R.id.totalItemQuantity);
        totalPackageQuantity = (EditText)rootView.findViewById(R.id.totalPackageQuantity);
        totalVolume = (EditText)rootView.findViewById(R.id.totalVolume);
        totalWeight = (EditText)rootView.findViewById(R.id.totalWeight);
        transportType = (Spinner)rootView.findViewById(R.id.c_transportType);
        transportTypeValue = new EditText(getActivity().getApplicationContext());
        calculateType = (Spinner)rootView.findViewById(R.id.c_calculateType);
        calculateTypeValue = new EditText(getActivity().getApplicationContext());
        isUpstairs = (CheckBox)rootView.findViewById(R.id.upstairs);
        calculate_Table = (TableLayout)rootView.findViewById(R.id.carrierorder_calculate_table);
        transportOrderTable = (TableLayout)rootView.findViewById(R.id.add_transportorder_table);

        addOrderNumber = (EditText)rootView.findViewById(R.id.add_order);
        addOrderNumber.setOnTouchListener(new AddTransportOrderTouchEvent());

        additionOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //完善订单信息
                Intent openCameraIntent = new Intent(getActivity(),CarrierOrderAddOtherActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("order",orderAddMore);
                openCameraIntent.putExtras(bundle);
                startActivityForResult(openCameraIntent, 1);
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
        activity_main=(ScrollView) rootView.findViewById(R.id.c_scrollview);
        activity_main.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(getActivity().getApplicationContext().INPUT_METHOD_SERVICE);
                return imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
            }
        });

        bindProvince();
        bindCarrier();

        //spinner绑定属性
        ArrayAdapterUtils.adapter(transportType, DataUtils.getTransportType(), getActivity(), transportTypeValue);
        ArrayAdapterUtils.adapter(calculateType, DataUtils.getCalculateType(), getActivity(), calculateTypeValue);

        // Inflate the layout for this fragment
        return rootView;
    }

    private void bindProvince(){
        //绑定省份
        List<DataItem> provinces = application.getProvinces();
        ArrayAdapter<DataItem> provinceAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(),R.layout.list_item1,provinces);
        province1.setAdapter(provinceAdapter);
        province1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String superBaseRegionId = ((DataItem) province1.getSelectedItem()).getTextValue();
                if (superBaseRegionId.length() != 0) {
                    //绑定城市
                    getCity(superBaseRegionId, "选择出发城市", startCity, startCityIdValue);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        province2.setAdapter(provinceAdapter);
        province2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String superBaseRegionId = ((DataItem) province2.getSelectedItem()).getTextValue();
                if (superBaseRegionId.length() != 0) {
                    //绑定城市
                    getCity(superBaseRegionId, "选择目的城市", destCity, destCityIdValue);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getCity(String superBaseRegionId, final String defaultName, final Spinner spinner, final EditText editText){
        spinner.setVisibility(View.VISIBLE);
        final List<DataItem> cities = new ArrayList<>();
        HttpArrayHelper cityArrayHelper = new HttpArrayHelper(application, getActivity(), requestQueue, loading) {
            @Override
            public void onResponse(JSONArray response) {
                DataItem defaultItem = new DataItem("", defaultName);
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
                ArrayAdapter<DataItem> myaAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(), R.layout.list_item1, cities);
                spinner.setAdapter(myaAdapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String textValue = ((DataItem) spinner.getSelectedItem()).getTextValue();
                        editText.setText(textValue);
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

    private void bindCarrier(){
        //绑定承运商
        if(application.getCarrierDatas()!=null){
            ArrayAdapter<DataItem> myaAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(),R.layout.list_item1,application.getCarrierDatas());
            carrierId.setAdapter(myaAdapter);
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
            if(loading!=null && loading.isShowing()){
                loading.dismiss();
            }
        }else {
            final List<DataItem> carrierDatas = new ArrayList<>();
            HttpArrayHelper arrayHelper = new HttpArrayHelper(application,getActivity(),requestQueue,loading) {
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
                        ArrayAdapter<DataItem> myaAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(),R.layout.list_item1,carrierDatas);
                        carrierId.setAdapter(myaAdapter);
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
                        if(loading!=null && loading.isShowing()){
                            loading.dismiss();
                        }
                        application.setCarrierDatas(carrierDatas);
                    }
                }
            };
            String carrierUrl = mySharedPreferences.getString("serviceAddress", "")+"/scmCarrier/getResource";
            arrayHelper.post(carrierUrl, new JSONObject());
        }

    }

    private void saveOrder(){
        if(number!=null&&number.equalsIgnoreCase(carrierOrderNumber.getText().toString().trim())){
            showMassage("已经创建此托运单!");
        }
        //判断必需数据
        if(!check(carrierIdValue.getText().toString())){
            showMassage("承运商为空!");
            return;
        }
        if(!check(carrierOrderNumber.getText().toString().trim())){
            showMassage("托运单号为空!");
            return;
        }
        if(!check(destCityIdValue.getText().toString())){
            showMassage("目的城市为空!");
            return;
        }
        /*if(!check(totalItemQuantity.getText().toString().trim())) {
            showMassage("数量为空!");
            return;
        }*/
        if(!check(totalPackageQuantity.getText().toString().trim())) {
            showMassage("件数为空!");
            return;
        }
        if(!check(totalVolume.getText().toString().trim())){
            showMassage("体积为空!");
            return;
        }
        if(!check(totalWeight.getText().toString().trim())){
            showMassage("重量为空!");
            return;
        }
        if(!check(transportTypeValue.getText().toString())){
            showMassage("请选择运输方式!");
            return;
        }
        if(!check(calculateTypeValue.getText().toString())){
            showMassage("请选择计费方式!");
            return;
        }
        if(numbers.size()==0){
            showMassage("请至少输入一个运输单!");
            return;
        }

        //更新费用数据
        for (int i = 0; i < payableJsons.size(); i++) {
            EditText text = (EditText)rootView.findViewById(i + 100);
            for (FeeOrderPayableJson json : payableJsons) {
                if (i + 100 == json.getIndex()) {
                    json.setExacctMoney(new Double(text.getText().toString().trim()));
                }
            }
        }
        createCarrierOrder();
    }

    private void createCarrierOrder(){
        if(check(startCityIdValue.getText().toString())){
            otdCarrierOrderBean.setStartCityId(UUID.fromString(startCityIdValue.getText().toString()));
        }else {
            String cityId = application.getCurrentCityId();
            if(cityId!=null&&cityId.length()!=0){
                otdCarrierOrderBean.setStartCityId(UUID.fromString(cityId));
            }
        }
        final Gson gson = JsonHelper.getGson();
        String httpUrl = mySharedPreferences.getString("serviceAddress", "") + "/order/carrierOrderCreate";
        final ProgressDialog createCarrierOrderProgressDialog = ProgressDialog.show(getActivity(), "提示", "...创建中...");
        otdCarrierOrderBean.setDetails(details);
        otdCarrierOrderBean.setSendDate(new Date());
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
                    final ProgressDialog updateFeeProgressDialog = ProgressDialog.show(getActivity(), "提示", "...更新费用中...");
                    FeeOrderPayables feeOrderPayables = new FeeOrderPayables(payableJsons);
                    String feeJson = gson.toJson(feeOrderPayables);
                    String updateUrl = mySharedPreferences.getString("serviceAddress","") + "/order/updatePayable/" + orderPayableId;
                    GsonRequest<ServiceResult> gsonRequest1 = new GsonRequest(updateUrl, ServiceResult.class, feeJson, new Response.Listener<ServiceResult>() {
                        @Override
                        public void onResponse(ServiceResult response) {
                            if (updateFeeProgressDialog.isShowing() && updateFeeProgressDialog != null) {
                                updateFeeProgressDialog.dismiss();
                            }
                            showToast("托运单已经创建！");
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

    private void addTransportOrder(){
        closeInput();
        String orderNumber = addOrderNumber.getText().toString().trim();
        if(numbers.contains(orderNumber)){
            showMassage("已经添加此单号!");
            return;
        }
        if(orderNumber.length()==0){
            showMassage("请输入运输单号!");
            return;
        }
        progressDialog = ProgressDialog.show(getActivity(), "提示", "...提取运输订单中...");
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
                                AlertDialog.Builder builder  = new AlertDialog.Builder(getActivity());
                                builder.setTitle("提示" ) ;
                                builder.setMessage("没有此单号!" ) ;
                                builder.setPositiveButton("重新输入" ,  null );
                                builder.show();
                            }else{
                                try {
                                    JSONObject content = response.getJSONObject("content");
                                    String clientOrderNumber = content.getString("clientOrderNumber");
                                    String transportOrderId = content.getString("transportOrderId");
                                    Double totalVolume = content.getDouble("totalVolume");
                                    Double totalWeight = content.getDouble("totalWeight");
                                    OtdCarrierOrderDetail detail = new OtdCarrierOrderDetail();
                                    detail.setTransportOrderId(UUID.fromString(transportOrderId));
                                    detail.setConfirmedVolume(totalVolume);
                                    detail.setConfirmedWeight(totalWeight);
                                    details.add(detail);
                                    numbers.add(clientOrderNumber);
                                    addTransportOrder(transportOrderTable, clientOrderNumber, totalVolume, totalWeight, detail);
                                    showToast("添加运输单成功!");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    showMassage("此单某些数据不完整!");
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
                        showMassage("出现异常，请重新操作!");
                    }
                }
        );
        requestQueue.add(request);
    }

    private void calculateOrder(){
        //判断必需数据
        if(!check(carrierIdValue.getText().toString())){
            showMassage("承运商为空!");
            return;
        }
        if(!check(carrierOrderNumber.getText().toString().trim())){
            showMassage("托运单号为空!");
            return;
        }
        if(!check(destCityIdValue.getText().toString())){
            showMassage("目的城市为空!");
            return;
        }
        /*if(!check(totalItemQuantity.getText().toString().trim())) {
            showMassage("数量为空!");
            return;
        }*/
        if(!check(totalPackageQuantity.getText().toString().trim())) {
            showMassage("件数为空!");
            return;
        }
        if(!check(totalVolume.getText().toString().trim())){
            showMassage("体积为空!");
            return;
        }
        if(!check(totalWeight.getText().toString().trim())){
            showMassage("重量为空!");
            return;
        }
        if(!check(transportTypeValue.getText().toString())){
            showMassage("请选择运输方式!");
            return;
        }
        if(!check(calculateTypeValue.getText().toString())){
            showMassage("请选择计费方式!");
            return;
        }

        //计算0
        //重复计算，先清除原有row
        payableJsons.clear();
        int count = calculate_Table.getChildCount();
        if(count>1){
            for(int i=count;i>0;i--){
                calculate_Table.removeView(calculate_Table.getChildAt(i));
            }
        }
        final ProgressDialog calculateDialog = ProgressDialog.show(getActivity(), "提示", "...计算中...");
        String userId = application.getUserId();
        if(check(startCityIdValue.getText().toString())){
            otdCarrierOrderBean.setStartCityId(UUID.fromString(startCityIdValue.getText().toString()));
        }else{
            otdCarrierOrderBean.setStartCityId(UUID.fromString(application.getCurrentCityId()));
        }
        otdCarrierOrderBean.update(UUID.fromString(carrierIdValue.getText().toString()),
                UUID.fromString(destCityIdValue.getText().toString()),
                carrierOrderNumber.getText().toString(), Integer.parseInt(calculateTypeValue.getText().toString()),
                Integer.parseInt(transportTypeValue.getText().toString()), UUID.fromString(userId), Integer.parseInt(totalPackageQuantity.getText().toString().trim()),
                new Double(totalVolume.getText().toString().trim()), new Double(totalWeight.getText().toString().trim()),
                isUpstairs.isChecked() ? true : false);
        otdCarrierOrderBean.setTotalItemQuantity(Integer.parseInt(totalItemQuantity.getText().toString().trim()));
        Map map = JsonHelper.toMap(otdCarrierOrderBean);
        String httpUrl = mySharedPreferences.getString("serviceAddress","")+"/order/calculate";
        HttpArrayHelper httpHelper = new HttpArrayHelper(application,getActivity(),requestQueue,calculateDialog) {
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
        httpHelper.post(httpUrl, new JSONObject(map));
    }

    private void addTransportOrder(final TableLayout table, final String orderNumber,Double confirmedVolume,Double confirmedWeight, final OtdCarrierOrderDetail detail){
        TableRow row = new TableRow(getActivity());
        row.setId(rowId);
        TextView order = new TextView(getActivity());
        order.setText(orderNumber);
        order.setPadding(3, 3, 3, 3);
        order.setGravity(Gravity.CENTER);

        TextView volume = new TextView(getActivity());
        volume.setText(confirmedVolume + "");
        volume.setPadding(3, 3, 3, 3);
        volume.setGravity(Gravity.CENTER);

        TextView weight = new TextView(getActivity());
        weight.setText(confirmedWeight + "");
        weight.setPadding(3, 3, 3, 3);
        weight.setGravity(Gravity.CENTER);

        Button button = new Button(getActivity());
        button.setWidth(50);
        button.setHeight(40);
        button.setId(rowId + 1000);
        button.setText("删除");
        button.setPadding(3, 3, 3, 3);
        button.setGravity(Gravity.CENTER);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TableRow tableRow = (TableRow)v.getParent();
                table.removeView(tableRow);
                numbers.remove(orderNumber);
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

    }

    private void addResult(TableLayout table,FeeOrderPayableJson json){
        TableRow row = new TableRow(getActivity());
        TextView exacctName = new TextView(getActivity());
        exacctName.setText(json.getExacctName() + ":");
        exacctName.setGravity(Gravity.RIGHT);

        EditText exacctMoney = new EditText(getActivity());
        exacctMoney.setId(json.getIndex());
        exacctMoney.setText(json.getExacctMoney() + "");
        exacctMoney.setWidth(300);
        exacctMoney.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        exacctMoney.setGravity(Gravity.CENTER);
        row.addView(exacctName);
        row.addView(exacctMoney);
        table.addView(row);
    }

    private class CarrierOrderTouchEvent implements View.OnTouchListener{

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                int size = carrierOrderNumber.getRight();
                int x = (int) event.getX();
                if (x > size - 100) {
                    //扫描
                    Intent openCameraIntent = new Intent(getActivity(),CaptureActivity.class);
                    startActivityForResult(openCameraIntent, 0);
                    return true;
                }
                return false;
            }
            return false;
        }
    }

    private class AddTransportOrderTouchEvent implements View.OnTouchListener{

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                int size = carrierOrderNumber.getRight();
                int x = (int) event.getX();
                if (x > size - 250) {
                    //扫描
                    Intent openCameraIntent = new Intent(getActivity(),CaptureActivity.class);
                    startActivityForResult(openCameraIntent, 2);
                    return true;
                }
                return false;
            }
            return false;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 0:
                if(data!=null){
                    Bundle bundle1 = data.getExtras();
                    String scanResult1 = bundle1.getString("result");
                    carrierOrderNumber.setText(scanResult1);
                }
                break;
            case 1:
                Bundle bundle2 = data.getExtras();
                Serializable serializable = bundle2.getSerializable("order");
                if(serializable!=null){
                    orderAddMore = (OtdCarrierOrderBean)serializable;
                    if(orderAddMore!=null){
                        updateOrder();
                    }
                }
                break;
            case 2:
                if(data!=null){
                    Bundle bundle3 = data.getExtras();
                    String scanResult3 = bundle3.getString("result");
                    addOrderNumber.setText(scanResult3);
                    if(numbers.contains(scanResult3)){
                        showMassage("已经添加此单号!");
                        return;
                    }
                    addTransportOrder();
                    Intent openCameraIntent = new Intent(getActivity(),CaptureActivity.class);
                    startActivityForResult(openCameraIntent, 2);
                }
                break;
            default:
                break;
        }
    }
    private void updateOrder() {
        otdCarrierOrderBean.update(orderAddMore);
    }

    private class FocusListener implements View.OnFocusChangeListener{

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            //判断是否有此单号
            if(!hasFocus){
                String orderNumber = carrierOrderNumber.getText().toString().trim();
                if(check(orderNumber)){
                    String httpUrl = mySharedPreferences.getString("serviceAddress", "")+"/order/carrierOrder/"+orderNumber;
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, httpUrl,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        Boolean isSuccess = response.getBoolean("success");
                                        if(isSuccess){
                                            AlertDialog.Builder builder  = new AlertDialog.Builder(getActivity());
                                            builder.setTitle("提示" ) ;
                                            builder.setMessage("已经有此单号!" ) ;
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
                                    showMassage("出现异常，请重新操作!");
                                }
                            }
                    );
                    requestQueue.add(request);
                }
            }
        }
    }

    private void closeInput(){
        View view =getActivity().getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputManger = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private boolean check(String value){
        if(value.length()!=0){
            return true;
        }
        return false;
    }
    private void showMassage(String massage){
        AlertDialog.Builder builder  = new AlertDialog.Builder(getActivity());
        builder.setTitle("提示" ) ;
        builder.setMessage(massage) ;
        builder.setPositiveButton("重新操作" ,  null );
        builder.show();
    }

    private void showToast(String massage){
        Toast.makeText(getActivity().getApplicationContext(), massage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
