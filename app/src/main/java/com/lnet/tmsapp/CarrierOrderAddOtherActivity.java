package com.lnet.tmsapp;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.lnet.tmsapp.application.ApplicationTrans;
import com.lnet.tmsapp.model.HttpArrayHelper;
import com.lnet.tmsapp.model.OtdCarrierOrder;
import com.lnet.tmsapp.model.OtdCarrierOrderBean;
import com.lnet.tmsapp.util.ArrayAdapterUtils;
import com.lnet.tmsapp.util.DataItem;
import com.lnet.tmsapp.util.DataUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Created by Administrator on 2015/7/31.
 */
public class CarrierOrderAddOtherActivity extends FragmentActivity{
    Spinner transportOrganization;
    EditText transportOrganizationValue;
    Spinner paymentType;
    EditText paymentTypeValue;
    EditText receiveMan;
    EditText receiveManPhone;
    EditText receiveAddress;
    EditText goodsName;
    EditText remarks;
    ApplicationTrans application;
    RequestQueue requestQueue;
    SharedPreferences mySharedPreferences;
    String FILENAME = "info";
    int MODE = MODE_PRIVATE;
    int OK = 2;
    ScrollView activity_main;
    OtdCarrierOrderBean order = new OtdCarrierOrderBean();


    @Override
    protected void onStart() {
        super.onStart();
        ActionBar actionBar = this.getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_complete, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                cancel();
                return true;
            case R.id.complete:
                save();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.carrierorder_addmoredata);
        transportOrganization = (Spinner)findViewById(R.id.c_transport_organization);
        transportOrganizationValue = new EditText(getApplicationContext());
        paymentType = (Spinner)findViewById(R.id.c_paymentType);
        paymentTypeValue = new EditText(getApplicationContext());
        receiveMan = (EditText)findViewById(R.id.c_receiveman);
        receiveManPhone = (EditText)findViewById(R.id.c_receive_phone);
        receiveAddress = (EditText)findViewById(R.id.c_receive_address);
        goodsName = (EditText)findViewById(R.id.goods_name);
        remarks = (EditText)findViewById(R.id.remarks);

        application = (ApplicationTrans)getApplication();
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        mySharedPreferences = getSharedPreferences(FILENAME, MODE);
        activity_main=(ScrollView) findViewById(R.id.c_scrollview);
        activity_main.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                return imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        });

        //spinner绑定属性
        bindSpinner();
        bindOrganization();
        Bundle bundle = this.getIntent().getExtras();
        if(bundle!=null){
            OtdCarrierOrderBean order = (OtdCarrierOrderBean)bundle.getSerializable("order");
            if(order!=null){
               reFillData(order);
            }
        }

    }

    private void bindSpinner() {
        //支付
        ArrayAdapter<DataItem> myaAdapter = new ArrayAdapter<>(getApplicationContext(),R.layout.list_item1,DataUtils.getPaymentType());
        paymentType.setAdapter(myaAdapter);
        paymentType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String value = ((DataItem)paymentType.getSelectedItem()).getTextValue();
                if(value.length()>0){
                    order.setPaymentType(Integer.parseInt(value));
                }
                order.setPaymentTypePosition(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
    }

    private void reFillData(OtdCarrierOrderBean order) {
        if(order.getOrganizationIdPosition()!=null){
            transportOrganization.setSelection(order.getOrganizationIdPosition(),true);
        }
        if(order.getPaymentTypePosition()!=null){
            paymentType.setSelection(order.getPaymentTypePosition(),true);
        }
        receiveMan.setText(order.getConsignee());
        receiveManPhone.setText(order.getConsigneePhone());
        receiveAddress.setText(order.getConsigneeAddress());
        goodsName.setText(order.getGoodsName());
        remarks.setText(order.getRemark());
    }

    private void save(){
        //保存
        String organizationValue = transportOrganizationValue.getText().toString();
        UUID organizationId = null;
        if(organizationValue.length()!=0){
            organizationId = UUID.fromString(organizationValue);
        }
        Integer paymentType = null;
        if(paymentTypeValue.getText().length()!=0){
            paymentType = Integer.parseInt(paymentTypeValue.getText().toString());
        }
        String receMan = receiveMan.getText().toString().trim();
        String phone = receiveManPhone.getText().toString().trim();
        String address = receiveAddress.getText().toString().trim();
        String name = goodsName.getText().toString().trim();
        String remark = remarks.getText().toString().trim();
        order.update(organizationId, paymentType, receMan, address, phone, name, remark);

        Intent resultIntent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("order",order);
        resultIntent.putExtras(bundle);
        this.setResult(OK, resultIntent);
        this.finish();
    }

    private void cancel(){
        Intent resultIntent = new Intent();
        Bundle bundle = new Bundle();
        resultIntent.putExtras(bundle);
        this.setResult(OK, resultIntent);
        this.finish();
    }

    private void bindOrganization() {
        //绑定中转站
        if(application.getOrganizationList().size()==0){
            final List<DataItem> organizationList = new ArrayList<>();
            HttpArrayHelper arrayHelper = new HttpArrayHelper(application,CarrierOrderAddOtherActivity.this,requestQueue,null) {
                @Override
                public void onResponse(JSONArray response) {
                    DataItem defaultItem = new DataItem("","请选择中转站：");
                    organizationList.add(defaultItem);
                    for(int i = 0;i<response.length();i++){
                        try {
                            JSONObject object = response.getJSONObject(i);
                            DataItem dataItem = new DataItem(object.getString("organizationId"),object.getString("name"));
                            organizationList.add(dataItem);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ArrayAdapter<DataItem> myaAdapter = new ArrayAdapter<>(getApplicationContext(),R.layout.list_item1,organizationList);
                        transportOrganization.setAdapter(myaAdapter);
                        transportOrganization.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                String textValue = ((DataItem) transportOrganization.getSelectedItem()).getTextValue();
                                if(textValue.length()>0){
                                    order.setTransferOrganizationId(UUID.fromString(textValue));
                                }
                                order.setOrganizationIdPosition(position);
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> arg0) {
                                // TODO Auto-generated method stub
                            }
                        });
                    }
                    application.setOrganizationList(organizationList);
                }
            };
            String organizationUrl = mySharedPreferences.getString("serviceAddress", "")+"/order/getOrganization";
            arrayHelper.get(organizationUrl);
        }else{
            ArrayAdapter<DataItem> myaAdapter = new ArrayAdapter<>(getApplicationContext(),R.layout.list_item1,application.getOrganizationList());
            transportOrganization.setAdapter(myaAdapter);
            transportOrganization.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String textValue = ((DataItem) transportOrganization.getSelectedItem()).getTextValue();
                    transportOrganizationValue.setText(textValue);
                    order.setOrganizationIdPosition(position);
                }
                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub
                }
            });
        }
    }
}
