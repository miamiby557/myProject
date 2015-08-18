package com.lnet.tmsapp.second.version.core;

/**
 * Created by Andy on 2015/5/28.
 */
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lnet.tmsapp.R;
import com.lnet.tmsapp.application.ApplicationTrans;
import com.lnet.tmsapp.model.HttpHelper;
import com.lnet.tmsapp.model.OtdCarrierOrder;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;


public class CarrierOrderDetailFragment extends Fragment {

    TextView carrierName;
    TextView carrierOrderNumber;
    TextView transferOrganization;
    TextView sentDate;
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
    RequestQueue requestQueue;
    ApplicationTrans application;

    public CarrierOrderDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_back, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_back:
                back();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void back(){
        getFragmentManager().popBackStack();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.carrier_order_detail, container, false);
        ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle("运输单明细");
        application = (ApplicationTrans)getActivity().getApplication();
        mySharedPreferences =getActivity().getSharedPreferences(application.getFILENAME(), application.getMODE());
        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        carrierName = (TextView)rootView.findViewById(R.id.carrier_name);
        carrierOrderNumber = (TextView)rootView.findViewById(R.id.carrierOrderNumber);
        transferOrganization = (TextView)rootView.findViewById(R.id.transferOrganization);
        sentDate = (TextView)rootView.findViewById(R.id.sendDate);
        startCity = (TextView)rootView.findViewById(R.id.startCity);
        destCity = (TextView)rootView.findViewById(R.id.destCity);
        receiveMan = (TextView)rootView.findViewById(R.id.receiveMan);
        receiveManPhone = (TextView)rootView.findViewById(R.id.receivePhone);
        receiveAddress = (TextView)rootView.findViewById(R.id.receiveAddress);
        totalItemQuantity = (TextView)rootView.findViewById(R.id.totalItemQuantity);
        totalPackageQuantity = (TextView)rootView.findViewById(R.id.totalPackageQuantity);
        totalVolume = (TextView)rootView.findViewById(R.id.totalVolume);
        totalWeight = (TextView)rootView.findViewById(R.id.totalWeight);
        expectedDate = (TextView)rootView.findViewById(R.id.expectedDate);
        billingCycleName = (TextView)rootView.findViewById(R.id.billcycle);
        transportType = (TextView)rootView.findViewById(R.id.transportType);
        paymentTypeName = (TextView)rootView.findViewById(R.id.paymentType);
        calculateTypeName = (TextView)rootView.findViewById(R.id.calculateType);
        remark = (TextView)rootView.findViewById(R.id.remark);
        String id = getArguments().getString("id");
        getData(id);
        return rootView;
    }

    private void getData(String orderId) {
        final ProgressDialog dialog = ProgressDialog.show(getActivity(), "提示", "正在努力加载...");
        String httpUrl = mySharedPreferences.getString("serviceAddress", "") + "/order/getCarrierOrderById/"+orderId;
        HttpHelper helper = new HttpHelper(application,getActivity(),requestQueue,dialog) {
            @Override
            public void onResponse(JSONObject response) {
                Gson gson = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd HH:mm:ss")
                        .create();
                OtdCarrierOrder order;
                order = gson.fromJson(response.toString(),OtdCarrierOrder.class);
                carrierName.setText(isNull(order.getCarrierName()));
                carrierOrderNumber.setText(isNull(order.getCarrierOrderNumber()));
                transferOrganization.setText(isNull(order.getTransferOrganizationValue()));
                sentDate.setText(order.getSendDate() == null ? "" : dateHelper(order.getSendDate()));
                startCity.setText(isNull(order.getTransferOrganizationValue()));
                destCity.setText(isNull(order.getDestCity()));
                receiveMan.setText(isNull(order.getConsignee()));
                receiveManPhone.setText(isNull(order.getConsigneePhone()));
                receiveAddress.setText(isNull(order.getConsigneeAddress()));
                totalItemQuantity.setText(order.getTotalItemQuantity()==null?"0":order.getTotalItemQuantity()+"");
                totalPackageQuantity.setText(order.getTotalPackageQuantity()==null?"0":order.getTotalPackageQuantity()+"");
                totalVolume.setText(order.getTotalVolume()==null?"0":order.getTotalVolume()+"m³");
                totalWeight.setText(order.getTotalWeight()==null?"0":order.getTotalWeight()+"kg");
                expectedDate.setText(order.getExpectedDate() == null ? "" : dateHelper(order.getExpectedDate()));
                billingCycleName.setText(isNull(order.getBillingCycleName()));
                transportType.setText(isNull(order.getTransportTypeName()));
                paymentTypeName.setText(isNull(order.getPaymentTypeName()));
                calculateTypeName.setText(isNull(order.getCalculateTypeName()));
                remark.setText(isNull(order.getRemark()));
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

    private String isNull(String value){
       if(value==null){
          return "无";
       }
        return value;
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
