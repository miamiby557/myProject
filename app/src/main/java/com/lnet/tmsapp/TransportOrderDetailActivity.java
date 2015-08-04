package com.lnet.tmsapp;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lnet.tmsapp.application.ApplicationTrans;
import com.lnet.tmsapp.model.HttpHelper;
import com.lnet.tmsapp.model.OtdTransportOrder;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2015/7/31.
 */
public class TransportOrderDetailActivity extends FragmentActivity{

    TextView clientName;
    TextView clientNumber;
    TextView lnetNumber;
    TextView orderDate;
    TextView startCity;
    TextView destCity;
    TextView receiveCompany;
    TextView receiveMan;
    TextView receiveManPhone;
    TextView receiveAddress;
    TextView confirmedItemQuantity;
    TextView confirmedPackageQuantity;
    TextView confirmedVolume;
    TextView confirmedWeight;
    TextView expectedDate;
    TextView billingCycleName;
    TextView transportType;
    TextView paymentTypeName;
    TextView calculateTypeName;
    TextView urgencyLevelName;
    TextView remark;
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
                int version = Integer.valueOf(android.os.Build.VERSION.SDK);
                Intent intent = new Intent(this,TransportOrderListActivity.class);
                startActivity(intent);
                if(version>5){
                    overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transport_order_detail);
        mySharedPreferences = getSharedPreferences(FILENAME, MODE);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        application = (ApplicationTrans)getApplication();
        clientName = (TextView)findViewById(R.id.client_name);
        clientNumber = (TextView)findViewById(R.id.clientOrderNumber);
        lnetNumber = (TextView)findViewById(R.id.lnetOrderNumber);
        orderDate = (TextView)findViewById(R.id.orderDate);
        startCity = (TextView)findViewById(R.id.startCity);
        destCity = (TextView)findViewById(R.id.destCity);
        receiveCompany = (TextView)findViewById(R.id.receiveCompany);
        receiveMan = (TextView)findViewById(R.id.receiveMan);
        receiveManPhone = (TextView)findViewById(R.id.receivePhone);
        receiveAddress = (TextView)findViewById(R.id.receiveAddress);
        confirmedItemQuantity = (TextView)findViewById(R.id.confirmedItemQuantity);
        confirmedPackageQuantity = (TextView)findViewById(R.id.confirmedPackageQuantity);
        confirmedVolume = (TextView)findViewById(R.id.confirmedVolume);
        confirmedWeight = (TextView)findViewById(R.id.confirmedWeight);
        expectedDate = (TextView)findViewById(R.id.expectedDate);
        billingCycleName = (TextView)findViewById(R.id.billcycle);
        transportType = (TextView)findViewById(R.id.transportType);
        paymentTypeName = (TextView)findViewById(R.id.paymentType);
        calculateTypeName = (TextView)findViewById(R.id.calculateType);
        urgencyLevelName = (TextView)findViewById(R.id.urgencyLevel);
        remark = (TextView)findViewById(R.id.remark);
        Bundle bundle = getIntent().getExtras();
        String id = bundle.getString("id");
        getData(id);
    }

    private void getData(String orderId) {
        final ProgressDialog dialog = ProgressDialog.show(TransportOrderDetailActivity.this,"提示","正在努力加载...");
        String httpUrl = mySharedPreferences.getString("serviceAddress", "") + "/order/getTransportOrderById/"+orderId;
        HttpHelper helper = new HttpHelper(application,TransportOrderDetailActivity.this,requestQueue,dialog) {
            @Override
            public void onResponse(JSONObject response) {
                Gson gson = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd HH:mm:ss")
                        .create();
                OtdTransportOrder order;
                order = gson.fromJson(response.toString(),OtdTransportOrder.class);
                clientName.setText(order.getClientName());
                clientNumber.setText(order.getClientOrderNumber());
                lnetNumber.setText(order.getLnetOrderNumber());
                orderDate.setText(order.getOrderDate() == null ? "" : dateHelper(order.getOrderDate()));
                startCity.setText(order.getStartCity());
                destCity.setText(order.getDestCity());
                receiveCompany.setText(order.getReceiveCompany());
                receiveMan.setText(order.getReceiveMan());
                receiveManPhone.setText(order.getReceivePhone());
                receiveAddress.setText(order.getReceiveAddress());
                confirmedItemQuantity.setText(order.getConfirmedItemQuantity()==null?"0":order.getConfirmedItemQuantity()+"");
                confirmedPackageQuantity.setText(order.getConfirmedPackageQuantity()==null?"0":order.getConfirmedPackageQuantity()+"");
                confirmedVolume.setText(order.getConfirmedVolume()+"m³");
                confirmedWeight.setText(order.getConfirmedWeight()+"kg");
                expectedDate.setText(order.getExpectedDate() == null ? "" : dateHelper(order.getExpectedDate()));
                billingCycleName.setText(order.getBillingCycleName());
                transportType.setText(order.getTransportTypeName());
                paymentTypeName.setText(order.getPaymentTypeName());
                calculateTypeName.setText(order.getCalculateTypeName());
                urgencyLevelName.setText(order.getUrgencyLevelName());
                remark.setText(order.getRemark());
                if (dialog.isShowing() && dialog != null) {
                    dialog.dismiss();
                }
            }

            @Override
            public void onErrorResponse(JSONObject response) {

            }
        };
        helper.get(httpUrl);
    }

    private String dateHelper(Date d){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str=sdf.format(d);
        return str;
    }


}
