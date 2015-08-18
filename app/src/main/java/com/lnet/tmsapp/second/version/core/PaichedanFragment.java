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
import android.support.v4.app.Fragment;
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
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.lnet.tmsapp.model.DispatchAssign;
import com.lnet.tmsapp.model.DispatchAssignDetail;
import com.lnet.tmsapp.model.DispatchVehicle;
import com.lnet.tmsapp.model.HttpArrayHelper;
import com.lnet.tmsapp.model.ServiceResult;
import com.lnet.tmsapp.util.GsonRequest;
import com.lnet.tmsapp.util.JsonHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;


public class PaichedanFragment extends Fragment {

    EditText driver;
    EditText driverPhone;
    Spinner cars;
    EditText startPlace;
    EditText endPlace;
    EditText remark;
    EditText fee;
    EditText vehicleNumber;
    EditText inputNumber;
    Button addTransportOrder;
    TableLayout table;
    LinearLayout linearLayout;
    List<String> transportOrderNumbers = new ArrayList<>();
    int rowId=0;
    ProgressDialog progressDialog;
    ApplicationTrans application;
    SharedPreferences mySharedPreferences;
    RequestQueue requestQueue;

    Set<DispatchAssignDetail> details=new HashSet<DispatchAssignDetail>();
    public PaichedanFragment() {
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
        new AlertDialog.Builder(getActivity()).setTitle("创建派车单").setMessage("确认创建？")
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
        View rootView = inflater.inflate(R.layout.paichedan, container, false);
        application = (ApplicationTrans)getActivity().getApplication();
        mySharedPreferences = getActivity().getSharedPreferences(application.getFILENAME(), application.getMODE());
        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        driver = (EditText)rootView.findViewById(R.id.driver);
        driverPhone = (EditText)rootView.findViewById(R.id.driver_phone);
        startPlace = (EditText)rootView.findViewById(R.id.start_place);
        endPlace = (EditText)rootView.findViewById(R.id.end_place);
        remark = (EditText)rootView.findViewById(R.id.remark);
        fee = (EditText)rootView.findViewById(R.id.fee);
        vehicleNumber = new EditText(getActivity().getApplicationContext());
        cars = (Spinner)rootView.findViewById(R.id.cars);
        inputNumber = (EditText)rootView.findViewById(R.id.input_number);
        inputNumber.setOnTouchListener(new TouchEvent());
        addTransportOrder = (Button)rootView.findViewById(R.id.add_order);
        table = (TableLayout)rootView.findViewById(R.id.add_transportorder_table);
        linearLayout = (LinearLayout)rootView.findViewById(R.id.linear_layout);
        linearLayout.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().getApplicationContext().INPUT_METHOD_SERVICE);
                return imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
            }
        });
        addTransportOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTransportOrder();
            }
        });
        bindCars();
        return rootView;
    }
    private void bindCars(){
        if(application.getCars()==null){
            getCars();
        }else {
            ArrayAdapter<DispatchVehicle> myaAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(), R.layout.list_item1, application.getCars());
            cars.setAdapter(myaAdapter);
            cars.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    DispatchVehicle vehicle = ((DispatchVehicle) cars.getSelectedItem());
                    vehicleNumber.setText(vehicle.getVehicleNumber());
                    driver.setText(vehicle.getDriver());
                    driverPhone.setText(vehicle.getDriverPhone());
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub
                }
            });
        }
    }

    private void getCars(){
        final ProgressDialog dialog = ProgressDialog.show(getActivity(), "提示", "正在努力加载...");
        String httpUrl = mySharedPreferences.getString("serviceAddress", "") + "/order/getCars";
        HttpArrayHelper helper = new HttpArrayHelper(application,getActivity(),requestQueue,dialog){

            @Override
            public void onResponse(JSONArray response) {
                Gson gson = JsonHelper.getGson();
                //TypeToken 不起作用
//                List<DispatchVehicle> vehicles = gson.fromJson(carsString,new TypeToken<List<DispatchVehicle>>(){}.getType());
                List<DispatchVehicle> vehicles = new ArrayList<>();
                DispatchVehicle defaultVehicle = new DispatchVehicle("选择司机:");
                vehicles.add(defaultVehicle);
                for(int i=0;i<response.length();i++){
                    try {
                        JSONObject object = response.getJSONObject(i);
                        DispatchVehicle vehicle = gson.fromJson(object.toString(),DispatchVehicle.class);
                        vehicles.add(vehicle);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                application.setCars(vehicles);
                ArrayAdapter<DispatchVehicle> myaAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(), R.layout.list_item1, vehicles);
                cars.setAdapter(myaAdapter);
                cars.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        DispatchVehicle vehicle = ((DispatchVehicle) cars.getSelectedItem());
                        if (vehicle != null) {
                            vehicleNumber.setText(vehicle.getVehicleNumber());
                            driver.setText(vehicle.getDriver());
                            driverPhone.setText(vehicle.getDriverPhone());
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub
                    }
                });
                if(dialog!=null&& dialog.isShowing()){
                    dialog.dismiss();
                }
            }
        };
        helper.get(httpUrl);
    }

    private void saveOrder(){
        if(!check(vehicleNumber.getText().toString().trim())){
            showMassage("请选择车牌号码!");
            return;
        }
        if(!check(driver.getText().toString().trim())){
            showMassage("请填写司机名称!");
            return;
        }
        if(!check(driverPhone.getText().toString().trim())){
            showMassage("请填写司机电话!");
            return;
        }
        if(!check(startPlace.getText().toString().trim())){
            showMassage("请填写始发地址!");
            return;
        }
        if(!check(endPlace.getText().toString().trim())){
            showMassage("请填写目的地址!");
            return;
        }
        if(transportOrderNumbers.size()==0){
            showMassage("请添加运输单!");
            return;
        }
        final ProgressDialog loading = ProgressDialog.show(getActivity(),"提示","创建中...");
        DispatchAssign assign = new DispatchAssign(1);
        assign.setDirver(driver.getText().toString().trim());
        assign.setDirverPhone(driverPhone.getText().toString().trim());
        assign.setVehicleCode(vehicleNumber.getText().toString());
        assign.setStartAddress(startPlace.getText().toString().trim());
        assign.setDestAddress(endPlace.getText().toString().trim());
        assign.setRemark(remark.getText().toString().trim());
        if(check(fee.getText().toString().trim())){
            assign.setTotalFee(new Double(fee.getText().toString().trim()));
        }
        assign.setDetails(details);
        final Gson gson = JsonHelper.getGson();
        String json = gson.toJson(assign);
        String httpUrl = mySharedPreferences.getString("serviceAddress", "") + "/order/createDispatchAssign";
        GsonRequest<ServiceResult> gsonRequest = new GsonRequest(httpUrl, ServiceResult.class, json, new Response.Listener<ServiceResult>() {
            @Override
            public void onResponse(ServiceResult response) {

                if(loading!=null && loading.isShowing()){
                    loading.dismiss();
                }
                if(response.isSuccess()){
                    showToast("创建成功!");
                }else {
                    showToast("创建失败!");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(loading!=null && loading.isShowing()){
                    loading.dismiss();
                }
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
        String number = inputNumber.getText().toString().trim();
        if(transportOrderNumbers.contains(number)){
            showMassage("已经添加此运输单!");
            return;
        }
        if(number.length()==0){
            showMassage("请输入运输单号！");
            return;
        }
        progressDialog = ProgressDialog.show(getActivity(), "提示", "...提取运输订单中...");
        //从数据库得到运输订单
        String httpUrl = mySharedPreferences.getString("serviceAddress", "")+"/order/transportOrder/"+number;
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
                                    Integer dispatchType = content.getInt("orderDispatchType");
                                    if(1!=dispatchType){
                                        showMassage("此单不符合要求!");
                                        return;
                                    }
                                    DispatchAssignDetail detail = new DispatchAssignDetail();
                                    detail.setOrderId(UUID.fromString(transportOrderId));
                                    details.add(detail);
                                    transportOrderNumbers.add(clientOrderNumber);
                                    addTransportOrder(table, clientOrderNumber, confirmedVolume, confirmedWeight,detail);
                                    showToast("添加运输单成功！");
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

    private void addTransportOrder(final TableLayout table, final String orderNumber,Double confirmedVolume,Double confirmedWeight, final DispatchAssignDetail detail){
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
                table.removeView(table.findViewById(v.getId() - 1000));
                transportOrderNumbers.remove(orderNumber);
                details.remove(detail);

            }
        });

        row.addView(order);
        row.addView(volume);
        row.addView(weight);
        row.addView(button);

        table.addView(row);
        rowId++;
        inputNumber.setText(null);
        inputNumber.setHint("输入运输单号");
        closeInput();
    }

    private class TouchEvent implements View.OnTouchListener{

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                int size = inputNumber.getRight();
                int x = (int) event.getX();
                if (x > size - 100) {
                    //扫描
                    scan();
                    return true;
                }
                return false;
            }
            return false;
        }
    }

    private void scan(){
        Intent openCameraIntent = new Intent(getActivity(),CaptureActivity.class);
        startActivityForResult(openCameraIntent, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == -1) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
            inputNumber.setText(scanResult);
            if(transportOrderNumbers.contains(scanResult)){
                showMassage("已经添加此单号!");
                return;
            }
            addTransportOrder();
            scan();
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
    private void closeInput(){
        View view =getActivity().getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputManger = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
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
