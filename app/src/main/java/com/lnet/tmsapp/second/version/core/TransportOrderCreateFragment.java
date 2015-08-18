package com.lnet.tmsapp.second.version.core;

/**
 * Created by Andy on 2015/5/28.
 */
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.lnet.tmsapp.*;
import com.lnet.tmsapp.application.ApplicationTrans;
import com.lnet.tmsapp.model.HttpArrayHelper;
import com.lnet.tmsapp.model.HttpHelper;
import com.lnet.tmsapp.model.OtdTransportOrder;
import com.lnet.tmsapp.util.ArrayAdapterUtils;
import com.lnet.tmsapp.util.DataItem;
import com.lnet.tmsapp.util.DataUtils;
import com.lnet.tmsapp.util.JsonHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class TransportOrderCreateFragment extends Fragment {

    Spinner clientId;
    EditText clientIdValue;
    EditText clientOrderNumber;
    String number;
    EditText totalItemQuantity;
    EditText totalPackageQuantity;
    EditText totalVolume;
    EditText totalWeight;
    ProgressDialog progressDialog;
    ScrollView scrollView;
    Spinner province1;
    Spinner province2;
    Spinner startCity;
    EditText startCityIdValue;
    Spinner destCity;
    EditText destCityIdValue;
    Button more;
    Spinner dispatchType;
    EditText dispatchTypeValue;
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

    ProgressDialog loading;



    ApplicationTrans application;
    SharedPreferences mySharedPreferences;

    public TransportOrderCreateFragment() {
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
        new AlertDialog.Builder(getActivity()).setTitle("创建运输单").setMessage("确认创建？")
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
        View rootView = inflater.inflate(R.layout.transportorder_create, container, false);
        loading = ProgressDialog.show(getActivity(),"提示", "...加载中...");
        application = (ApplicationTrans)getActivity().getApplication();
        mySharedPreferences =getActivity().getSharedPreferences(application.getFILENAME(), application.getMODE());
        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        clientId = (Spinner)rootView.findViewById(R.id.spinner_client);
        clientIdValue = new EditText(getActivity().getApplicationContext());
        transportType = (Spinner)rootView.findViewById(R.id.t_transportType);
        transportTypeValue = new EditText(getActivity().getApplicationContext());
        calculateType = (Spinner)rootView.findViewById(R.id.t_calculateType);
        calculateTypeValue = new EditText(getActivity().getApplicationContext());
        paymentType = (Spinner)rootView.findViewById(R.id.t_paymentType);
        paymentTypeValue = new EditText(getActivity().getApplicationContext());
        company = (EditText)rootView.findViewById(R.id.t_company);
        receiveMan = (EditText)rootView.findViewById(R.id.t_receiveMan);
        receiveManPhone = (EditText)rootView.findViewById(R.id.t_receiveManPhone);
        receiveAddress = (EditText)rootView.findViewById(R.id.t_receiveManAddress);
        clientOrderNumber = (EditText)rootView.findViewById(R.id.t_number);
        clientOrderNumber.setOnFocusChangeListener(new FocusChangeListener());
        province1 = (Spinner)rootView.findViewById(R.id.t_province1);
        province2 = (Spinner)rootView.findViewById(R.id.t_province2);
        startCity = (Spinner)rootView.findViewById(R.id.t_start_city);
        startCityIdValue = new EditText(getActivity().getApplicationContext());
        destCity = (Spinner)rootView.findViewById(R.id.t_dest_city);
        destCityIdValue = new EditText(getActivity().getApplicationContext());
        totalVolume = (EditText)rootView.findViewById(R.id.t_totalvolume);
        totalWeight = (EditText)rootView.findViewById(R.id.t_totalweight);
        totalItemQuantity = (EditText)rootView.findViewById(R.id.t_totalItemQuantity);
        totalPackageQuantity = (EditText)rootView.findViewById(R.id.t_totalPackageQuantity);
        dispatchType = (Spinner)rootView.findViewById(R.id.t_dispatchType);
        dispatchTypeValue = new EditText(getActivity().getApplicationContext());
        more = (Button)rootView.findViewById(R.id.more);
        moreLine = (LinearLayout)rootView.findViewById(R.id.line_more);
        more.setOnClickListener(new MoreListener());

        //扫描二维码
        clientOrderNumber.setOnTouchListener(new TouchEvent());

        scrollView = (ScrollView)rootView.findViewById(R.id.t_scrollview);
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().getApplicationContext().INPUT_METHOD_SERVICE);
                return imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
            }
        });
        bindProvince();
        getClient();
        ArrayAdapterUtils.adapter(dispatchType, DataUtils.getOrderDispatchType(), getActivity().getApplicationContext(), dispatchTypeValue);
        return rootView;
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
            ArrayAdapterUtils.adapter(transportType, DataUtils.getTransportType(),getActivity().getApplicationContext(),transportTypeValue);
            ArrayAdapterUtils.adapter(calculateType,DataUtils.getCalculateType(),getActivity().getApplicationContext(),calculateTypeValue);
            ArrayAdapterUtils.adapter(paymentType,DataUtils.getPaymentType(),getActivity().getApplicationContext(),paymentTypeValue);
        }
    }

    private class FocusChangeListener implements View.OnFocusChangeListener{

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(!hasFocus){
                String orderNumber = clientOrderNumber.getText().toString().trim();
                if(check(orderNumber)){
                    //检查是否有此客户单号
                    checkNumber(orderNumber);
                }
            }
        }
    }
    private void checkNumber(String orderNumber){
        String httpUrl = mySharedPreferences.getString("serviceAddress", "")+"/order/transportOrder/"+orderNumber;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, httpUrl,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Boolean isSuccess = response.getBoolean("success");
                            if(isSuccess){
                                AlertDialog.Builder builder  = new AlertDialog.Builder(getActivity());
                                builder.setTitle("提示" );
                                builder.setMessage("已经有此单号！" );
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

    private class TouchEvent implements View.OnTouchListener{

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                int size = clientOrderNumber.getRight();
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

    private void saveOrder(){
        if(number!=null&&number.equalsIgnoreCase(clientOrderNumber.getText().toString().trim())){
           showMassage("已经创建此单号!");
            return;
        }
        //判断必需数据
        if(!check(clientIdValue.getText().toString())){
            showMassage("客户为空!");
            return;
        }
        if(!check(clientOrderNumber.getText().toString().trim())){
            showMassage("单号为空!");
            return;
        }
        if(!check(destCityIdValue.getText().toString().trim())){
            showMassage("目的地为空!");
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
        /*if(!check(totalItemQuantity.getText().toString().trim())){
            showMassage("数量为空!");
            return;
        }*/
        if(!check(totalPackageQuantity.getText().toString().trim())){
            showMassage("件数为空!");
            return;
        }
        if(!check(dispatchTypeValue.getText().toString())){
            showMassage("请选择调度类型!");
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
        order.setTotalItemQuantity(Integer.parseInt(totalItemQuantity.getText().toString().trim()));
        order.setTotalPackageQuantity(Integer.parseInt(totalPackageQuantity.getText().toString().trim()));
        order.setOrderDispatchType(Integer.parseInt(dispatchTypeValue.getText().toString()));
        order.update(company.getText().toString().trim(),receiveMan.getText().toString().trim(),receiveManPhone.getText().toString().trim(),receiveAddress.getText().toString().trim());
        order.setOrderType(2);
        progressDialog = ProgressDialog.show(getActivity(), "创建提示", "...创建中...");
        String userId = application.getUserId();
        if(check(startCityIdValue.getText().toString())){
            order.setStartCityId(UUID.fromString(startCityIdValue.getText().toString()));
        }else{
            String cityId = application.getCurrentCityId();
            if(check(cityId)){
                order.setStartCityId(UUID.fromString(cityId));
            }
        }
        order.update(clientOrderNumber.getText().toString().trim(), UUID.fromString(clientIdValue.getText().toString().trim()), UUID.fromString(destCityIdValue.getText().toString())
                , new Double(totalVolume.getText().toString().trim()), new Double(totalWeight.getText().toString().trim()), UUID.fromString(userId));
        Map object = JsonHelper.toMap(order);
        String httpUrl = mySharedPreferences.getString("serviceAddress","")+"/order/transportOrderCreate";
        HttpHelper httpHelper = new HttpHelper(application,getActivity(),requestQueue,progressDialog) {
            @Override
            public void onResponse(JSONObject response) {
                if ( progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                number = clientOrderNumber.getText().toString().trim();
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
                    getCity(superBaseRegionId,"选择目的城市",destCity,destCityIdValue);
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

    private void getClient(){
        //得到用户client信息
        if(application.getClientDatas()!=null){
            ArrayAdapter<DataItem> myaAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(),R.layout.list_item1,application.getClientDatas());
            clientId.setAdapter(myaAdapter);
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
            if(loading!=null && loading.isShowing()){
                loading.dismiss();
            }
        }else {
            final List<DataItem> clientDatas = new ArrayList<>();
            HttpArrayHelper arrayHelper = new HttpArrayHelper(application,getActivity(),requestQueue,loading) {
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
                        ArrayAdapter<DataItem> myaAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(),R.layout.list_item1,clientDatas);
                        clientId.setAdapter(myaAdapter);
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
                        if(loading!=null && loading.isShowing()){
                            loading.dismiss();
                        }
                        application.setClientDatas(clientDatas);
                    }
                }
            };
            String httpUrl = mySharedPreferences.getString("serviceAddress", "") + "/crmClient/getResource";
            arrayHelper.post(httpUrl, new JSONObject());
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //处理扫描结果（在界面上显示）
        if (resultCode == -1) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
            clientOrderNumber.setText(scanResult);
        }
    }

    private void showToast(String massage){
        Toast.makeText(getActivity().getApplicationContext(), massage, Toast.LENGTH_SHORT).show();
    }

    private void showMassage(String massage){
        AlertDialog.Builder builder  = new AlertDialog.Builder(getActivity());
        builder.setTitle("提示" ) ;
        builder.setMessage(massage) ;
        builder.setPositiveButton("重新操作" ,  null );
        builder.show();
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private boolean check(String value){
        if(value.length()!=0){
            return true;
        }
        return false;
    }
}
