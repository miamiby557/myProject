package com.lnet.tmsapp.second.andy.materialdesign;

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
import com.lnet.tmsapp.model.OtdTransportOrder;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;


public class TransportOrderDetailFragment extends Fragment {

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
    RequestQueue requestQueue;
    ApplicationTrans application;

    public TransportOrderDetailFragment() {
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
        View rootView = inflater.inflate(R.layout.transport_order_detail, container, false);
        ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle("运输单明细");
        application = (ApplicationTrans)getActivity().getApplication();
        mySharedPreferences =getActivity().getSharedPreferences(application.getFILENAME(), application.getMODE());
        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        clientName = (TextView)rootView.findViewById(R.id.client_name);
        clientNumber = (TextView)rootView.findViewById(R.id.clientOrderNumber);
        lnetNumber = (TextView)rootView.findViewById(R.id.lnetOrderNumber);
        orderDate = (TextView)rootView.findViewById(R.id.orderDate);
        startCity = (TextView)rootView.findViewById(R.id.startCity);
        destCity = (TextView)rootView.findViewById(R.id.destCity);
        receiveCompany = (TextView)rootView.findViewById(R.id.receiveCompany);
        receiveMan = (TextView)rootView.findViewById(R.id.receiveMan);
        receiveManPhone = (TextView)rootView.findViewById(R.id.receivePhone);
        receiveAddress = (TextView)rootView.findViewById(R.id.receiveAddress);
        confirmedItemQuantity = (TextView)rootView.findViewById(R.id.confirmedItemQuantity);
        confirmedPackageQuantity = (TextView)rootView.findViewById(R.id.confirmedPackageQuantity);
        confirmedVolume = (TextView)rootView.findViewById(R.id.confirmedVolume);
        confirmedWeight = (TextView)rootView.findViewById(R.id.confirmedWeight);
        expectedDate = (TextView)rootView.findViewById(R.id.expectedDate);
        billingCycleName = (TextView)rootView.findViewById(R.id.billcycle);
        transportType = (TextView)rootView.findViewById(R.id.transportType);
        paymentTypeName = (TextView)rootView.findViewById(R.id.paymentType);
        calculateTypeName = (TextView)rootView.findViewById(R.id.calculateType);
        urgencyLevelName = (TextView)rootView.findViewById(R.id.urgencyLevel);
        remark = (TextView)rootView.findViewById(R.id.remark);
        String id = getArguments().getString("id");
        getData(id);
        return rootView;
    }

    private void getData(String orderId) {
        final ProgressDialog dialog = ProgressDialog.show(getActivity(),"提示","正在努力加载...");
        String httpUrl = mySharedPreferences.getString("serviceAddress", "") + "/order/getTransportOrderById/"+orderId;
        HttpHelper helper = new HttpHelper(application,getActivity(),requestQueue,dialog) {
            @Override
            public void onResponse(JSONObject response) {
                Gson gson = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd HH:mm:ss")
                        .create();
                OtdTransportOrder order;
                order = gson.fromJson(response.toString(),OtdTransportOrder.class);
                clientName.setText(isNull(order.getClientName()));
                clientNumber.setText(isNull(order.getClientOrderNumber()));
                lnetNumber.setText(isNull(order.getLnetOrderNumber()));
                orderDate.setText(order.getOrderDate() == null ? "" : dateHelper(order.getOrderDate()));
                startCity.setText(isNull(order.getStartCity()));
                destCity.setText(isNull(order.getDestCity()));
                receiveCompany.setText(isNull(order.getReceiveCompany()));
                receiveMan.setText(isNull(order.getReceiveMan()));
                receiveManPhone.setText(isNull(order.getReceivePhone()));
                receiveAddress.setText(isNull(order.getReceiveAddress()));
                confirmedItemQuantity.setText(order.getTotalItemQuantity()==null?"0":order.getTotalItemQuantity()+"");
                confirmedPackageQuantity.setText(order.getTotalPackageQuantity()==null?"0":order.getTotalPackageQuantity()+"");
                confirmedVolume.setText(order.getTotalVolume()==null?"0":order.getTotalVolume()+"m³");
                confirmedWeight.setText(order.getTotalWeight()==null?"0":order.getTotalWeight()+"kg");
                expectedDate.setText(order.getExpectedDate() == null ? "" : dateHelper(order.getExpectedDate()));
                billingCycleName.setText(isNull(order.getBillingCycleName()));
                transportType.setText(isNull(order.getTransportTypeName()));
                paymentTypeName.setText(isNull(order.getPaymentTypeName()));
                calculateTypeName.setText(isNull(order.getCalculateTypeName()));
                urgencyLevelName.setText(isNull(order.getUrgencyLevelName()));
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
