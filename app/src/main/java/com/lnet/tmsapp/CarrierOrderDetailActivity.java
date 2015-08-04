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
import com.lnet.tmsapp.model.OtdCarrierOrder;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2015/7/31.
 */
public class CarrierOrderDetailActivity extends FragmentActivity{

    TextView carrierName;
    TextView carrierOrderNumber;
    TextView transferOrganization;
    TextView sendDate;
    TextView startCity;
    TextView destCity;
    TextView receiveMan;
    TextView receiveManPhone;
    TextView receiveAddress;
    TextView totalItemQuantity;
    TextView totalPackageQuantity;
    TextView totalVolume;
    TextView totalWeight;
    TextView expectedDate;
    TextView billingCycleName;
    TextView transportType;
    TextView paymentTypeName;
    TextView calculateTypeName;
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
                Intent intent = new Intent(this, CarrierOrderListActivity.class);
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
        setContentView(R.layout.carrier_order_detail);
        mySharedPreferences = getSharedPreferences(FILENAME, MODE);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        application = (ApplicationTrans)getApplication();

        carrierName = (TextView)findViewById(R.id.carrier_name);
        carrierOrderNumber = (TextView)findViewById(R.id.carrierOrderNumber);
        transferOrganization = (TextView)findViewById(R.id.transferOrganization);
        sendDate = (TextView)findViewById(R.id.sendDate);
        startCity = (TextView)findViewById(R.id.startCity);
        destCity = (TextView)findViewById(R.id.destCity);
        receiveAddress = (TextView)findViewById(R.id.receiveAddress);
        receiveMan = (TextView)findViewById(R.id.receiveMan);
        receiveManPhone = (TextView)findViewById(R.id.receivePhone);
        totalItemQuantity = (TextView)findViewById(R.id.totalItemQuantity);
        totalPackageQuantity = (TextView)findViewById(R.id.totalPackageQuantity);
        totalVolume = (TextView)findViewById(R.id.totalVolume);
        totalWeight = (TextView)findViewById(R.id.totalWeight);
        expectedDate = (TextView)findViewById(R.id.expectedDate);
        billingCycleName = (TextView)findViewById(R.id.billcycle);
        transportType = (TextView)findViewById(R.id.transportType);
        paymentTypeName = (TextView)findViewById(R.id.paymentType);
        calculateTypeName = (TextView)findViewById(R.id.calculateType);
        remark = (TextView)findViewById(R.id.remark);

        Bundle bundle = getIntent().getExtras();
        String id = bundle.getString("id");
        getData(id);
    }

    private void getData(String orderId) {
        final ProgressDialog dialog = ProgressDialog.show(CarrierOrderDetailActivity.this,"提示","正在努力加载...");
        String httpUrl = mySharedPreferences.getString("serviceAddress", "") + "/order/getCarrierOrderById/"+orderId;
        HttpHelper helper = new HttpHelper(application,CarrierOrderDetailActivity.this,requestQueue,dialog) {
            @Override
            public void onResponse(JSONObject response) {
                Gson gson = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd HH:mm:ss")
                        .create();
                OtdCarrierOrder order;
                order = gson.fromJson(response.toString(),OtdCarrierOrder.class);
                carrierName.setText(order.getCarrierName());
                carrierOrderNumber.setText(order.getCarrierOrderNumber());
                transferOrganization.setText(order.getTransferOrganizationValue());
                sendDate.setText(order.getSendDate() == null ? "" : dateHelper(order.getSendDate()));
                startCity.setText(order.getStartCity());
                destCity.setText(order.getDestCity());
                receiveMan.setText(order.getConsignee());
                receiveManPhone.setText(order.getConsigneePhone());
                receiveAddress.setText(order.getConsigneeAddress());
                totalItemQuantity.setText(order.getTotalItemQuantity() ==null?"0":order.getTotalItemQuantity() + "");
                totalPackageQuantity.setText(order.getTotalPackageQuantity()==null?"0":order.getTotalPackageQuantity()+"");
                totalVolume.setText(order.getTotalVolume() + "m³");
                totalWeight.setText(order.getTotalWeight()+"kg");
                expectedDate.setText(order.getExpectedDate() == null ? "" : dateHelper(order.getExpectedDate()));
                billingCycleName.setText(order.getBillingCycleName());
                transportType.setText(order.getTransportTypeName());
                paymentTypeName.setText(order.getPaymentTypeName());
                calculateTypeName.setText(order.getCalculateTypeName());
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
