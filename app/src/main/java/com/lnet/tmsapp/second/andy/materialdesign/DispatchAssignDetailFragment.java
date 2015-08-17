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
import com.lnet.tmsapp.model.DispatchAssign;
import com.lnet.tmsapp.model.HttpHelper;
import com.lnet.tmsapp.model.OtdTransportOrder;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;


public class DispatchAssignDetailFragment extends Fragment {

    TextView dispatchAssignNumber;
    TextView vehicleCode;
    TextView driver;
    TextView driverPhone;
    TextView fee;
    TextView totalItemQuantity;
    TextView totalPackageQuantity;
    TextView totalVolume;
    TextView totalWeight;
    TextView startAddress;
    TextView destAddress;
    TextView remark;

    SharedPreferences mySharedPreferences;
    RequestQueue requestQueue;
    ApplicationTrans application;

    public DispatchAssignDetailFragment() {
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
        View rootView = inflater.inflate(R.layout.dispatch_assign_detail, container, false);
        ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle("派车单明细");
        application = (ApplicationTrans)getActivity().getApplication();
        mySharedPreferences =getActivity().getSharedPreferences(application.getFILENAME(), application.getMODE());
        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        dispatchAssignNumber = (TextView)rootView.findViewById(R.id.dispatchAssignNumber);
        vehicleCode = (TextView)rootView.findViewById(R.id.vehicleCode);
        driver = (TextView)rootView.findViewById(R.id.driver);
        driverPhone = (TextView)rootView.findViewById(R.id.driverPhone);
        fee = (TextView)rootView.findViewById(R.id.fee);
        totalItemQuantity = (TextView)rootView.findViewById(R.id.totalItemQuantity);
        totalPackageQuantity = (TextView)rootView.findViewById(R.id.totalPackageQuantity);
        totalVolume = (TextView)rootView.findViewById(R.id.totalVolume);
        totalWeight = (TextView)rootView.findViewById(R.id.totalWeight);
        startAddress = (TextView)rootView.findViewById(R.id.startAddress);
        destAddress = (TextView)rootView.findViewById(R.id.destAddress);
        remark = (TextView)rootView.findViewById(R.id.remark);
        String id = getArguments().getString("id");
        getData(id);
        return rootView;
    }

    private void getData(String orderId) {
        final ProgressDialog dialog = ProgressDialog.show(getActivity(),"提示","正在努力加载...");
        String httpUrl = mySharedPreferences.getString("serviceAddress", "") + "/order/getDispatchAssignById/"+orderId;
        HttpHelper helper = new HttpHelper(application,getActivity(),requestQueue,dialog) {
            @Override
            public void onResponse(JSONObject response) {
                Gson gson = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd HH:mm:ss")
                        .create();
                DispatchAssign assign;
                assign = gson.fromJson(response.toString(),DispatchAssign.class);
                dispatchAssignNumber.setText(assign.getDispatchAssignNumber());
                driver.setText(assign.getDirver());
                driverPhone.setText(assign.getDirverPhone());
                vehicleCode.setText(assign.getVehicleCode());
                fee.setText(assign.getTotalFee() + "RMB");
                totalItemQuantity.setText(assign.getTotalItemQuantity()+"");
                totalPackageQuantity.setText(assign.getTotalPackageQuantity()+"");
                totalVolume.setText(assign.getTotalVolume()+"m³");
                totalWeight.setText(assign.getTotalWeight()+"kg");
                startAddress.setText(assign.getStartAddress());
                destAddress.setText(assign.getDestAddress());
                remark.setText(assign.getRemark());
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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
