package com.lnet.tmsapp.second.version.core;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.lnet.tmsapp.R;
import com.lnet.tmsapp.application.ApplicationTrans;
import com.lnet.tmsapp.model.HttpHelper;
import com.lnet.tmsapp.model.OtdCarrierOrderBean;
import com.lnet.tmsapp.model.OtdCarrierOrderDetail;
import com.lnet.tmsapp.model.OtdCarrierOrderDetailView;
import com.lnet.tmsapp.util.DataItem;
import com.lnet.tmsapp.util.DataUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Administrator on 2015/8/25.
 */
public class AddTransportOrderActivity extends FragmentActivity{
    EditText addOrderNumber;
    Button addTransportOrder;
    List<String> numbers = new ArrayList<>();
    Set<OtdCarrierOrderDetail> details;
    Set<OtdCarrierOrderDetailView> detailViews;
    TextView totalVolume;
    TextView totalWeight;
    TextView totalItemQuantity;
    TextView totalPackageQuantity;
    TextView receivePageNumber;
    OtdCarrierOrderBean otdCarrierOrderBean = new OtdCarrierOrderBean();
    ProgressDialog progressDialog;
    TextView orderCount;
    ApplicationTrans application;
    SharedPreferences mySharedPreferences;
    RequestQueue requestQueue;
    TableLayout transportOrderTable;
    int num = 100;
    Button save;

    ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_transport_order);
        application = (ApplicationTrans)getApplication();
        mySharedPreferences = getSharedPreferences(application.getFILENAME(), application.getMODE());
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        details=new HashSet<>();
        detailViews = new HashSet<>();
        addOrderNumber = (EditText)findViewById(R.id.add_order);
        addTransportOrder = (Button)findViewById(R.id.add_transorder);
        addTransportOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTransportOrder();
            }
        });
        addOrderNumber.setOnTouchListener(new AddTransportOrderTouchEvent());
        totalItemQuantity = new TextView(getApplicationContext());
        totalPackageQuantity = (TextView)findViewById(R.id.total_package_count);
        totalVolume = (TextView)findViewById(R.id.total_volume);
        receivePageNumber = (TextView)findViewById(R.id.receive_page_count);
        totalWeight = (TextView)findViewById(R.id.total_weight);
        orderCount = (TextView)findViewById(R.id.order_count);
        transportOrderTable = (TableLayout)findViewById(R.id.add_transportorder_table);
        save = (Button)findViewById(R.id.button_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
        Bundle bundle = this.getIntent().getExtras();
        if(bundle!=null){
            OtdCarrierOrderBean order = (OtdCarrierOrderBean)bundle.getSerializable("order");
            if(order!=null){
                reFillData(order);
            }
        }
        scrollView = (ScrollView)findViewById(R.id.c_scrollview);
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager)getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
                return imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        });

    }

    private void save() {
        otdCarrierOrderBean.setDetails(details);
        otdCarrierOrderBean.setDetailViews(detailViews);
        otdCarrierOrderBean.setTotalItemQuantity(Integer.parseInt(totalItemQuantity.getText().toString().trim()));
        otdCarrierOrderBean.setTotalVolume(new Double(totalVolume.getText().toString()));
        otdCarrierOrderBean.setTotalWeight(new Double(totalWeight.getText().toString()));
        otdCarrierOrderBean.setTotalItemQuantity(Integer.parseInt(totalItemQuantity.getText().toString()));
        otdCarrierOrderBean.setTotalPackageQuantity(Integer.parseInt(totalPackageQuantity.getText().toString()));
        otdCarrierOrderBean.setReceiptPageNumber(Integer.parseInt(receivePageNumber.getText().toString()));
        otdCarrierOrderBean.setNumbers(numbers);
        Intent resultIntent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("order", otdCarrierOrderBean);
        resultIntent.putExtras(bundle);
        setResult(3, resultIntent);
        try {
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void reFillData(OtdCarrierOrderBean order) {
        ProgressDialog progressDialog = ProgressDialog.show(AddTransportOrderActivity.this,"提示","填充数据中...");
        int size = order.getDetails().size();
        this.numbers = order.getNumbers();
        this.details = order.getDetails();
        this.detailViews = order.getDetailViews();
        orderCount.setText(size + "");
        Set<OtdCarrierOrderDetail> details = order.getDetails();
        Set<OtdCarrierOrderDetailView> detailViews = order.getDetailViews();
        for(OtdCarrierOrderDetail detail:details){
            for(OtdCarrierOrderDetailView detailView:detailViews){
                if(detail.getTransportOrderId().equals(detailView.getTransportOrderId())){
                    addTransportOrder(transportOrderTable,detail.getTransportOrderNumber(),detail.getTransportOrderId().toString(),detail.getConfirmedVolume(),detail.getConfirmedWeight(),detail.getConfirmedItemQuantity(),detail.getConfirmedPackageQuantity(),detail.getReceivePageNumber(),detail,detailView);
                    break;
                }
            }
        }
        totalVolume.setText(order.getTotalVolume()==null?"0":order.getTotalVolume()+"");
        totalWeight.setText(order.getTotalWeight()==null?"0":order.getTotalWeight()+"");
        totalItemQuantity.setText(order.getTotalItemQuantity()==null?"0":order.getTotalItemQuantity()+"");
        totalPackageQuantity.setText(order.getTotalPackageQuantity()==null?"0":order.getTotalPackageQuantity()+"");
        receivePageNumber.setText(order.getReceiptPageNumber()==null?"0":order.getReceiptPageNumber()+"");
        progressDialog.dismiss();
    }

    private class AddTransportOrderTouchEvent implements View.OnTouchListener{

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                int size = addOrderNumber.getRight();
                int x = (int) event.getX();
                if (x > size - 250) {
                    //扫描
                    Intent openCameraIntent = new Intent(AddTransportOrderActivity.this,CaptureActivity.class);
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
            case 2:
                if(data!=null){
                    Bundle bundle3 = data.getExtras();
                    String scanResult3 = bundle3.getString("result");
                    addOrderNumber.setText(scanResult3);
                    addTransportOrder();
                    Intent openCameraIntent = new Intent(AddTransportOrderActivity.this,CaptureActivity.class);
                    startActivityForResult(openCameraIntent, 2);
                }
                break;
            default:
                break;
        }

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
        progressDialog = ProgressDialog.show(AddTransportOrderActivity.this, "提示", "...提取运输订单中...");
        //从数据库得到运输订单

        String httpUrl = mySharedPreferences.getString("serviceAddress", "")+"/order/getTransportOrderByCarrierOrder/"+orderNumber;
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
                                AlertDialog.Builder builder  = new AlertDialog.Builder(AddTransportOrderActivity.this);
                                builder.setTitle("提示" ) ;
                                builder.setMessage("此单号不符合要求或没有此单号!" ) ;
                                builder.setPositiveButton("重新输入" ,  null );
                                builder.show();
                            }else{
                                JSONArray array = response.getJSONArray("content");
                                if(array.length()==1){
                                    JSONObject content = array.getJSONObject(0);
                                    getParameters(content);
                                }else {
                                    int length = array.length();
                                    final HelpItem[] items = new HelpItem[length];
                                    for(int i=0;i<length;i++){
                                        JSONObject content = array.getJSONObject(i);
                                        String transportOrderId = content.getString("transportOrderId");
                                        String clientName = content.getString("clientName");
                                        String orderNumber = content.getString("orderNumber");
                                        HelpItem item = new HelpItem(transportOrderId,clientName,orderNumber);
                                        items[i] = item;
                                    }
                                    final AlertDialog.Builder builder = new AlertDialog.Builder(AddTransportOrderActivity.this);
                                    builder.setIcon(android.R.drawable.ic_dialog_info);
                                    builder.setTitle("请选择一个订单");
                                    ArrayAdapter<HelpItem> myaAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.list_item2, items);
                                    builder.setSingleChoiceItems(myaAdapter, 0, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            HelpItem item = items[which];
                                            getData(item.getTransportOrderId(),item.getOrderNumber());
                                            dialog.dismiss();
                                        }
                                    }).setNegativeButton("取消",null).show();
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

    private void getData(String orderId,String number) {
        if(numbers.contains(number+orderId)){
            showMassage("已经添加此单号!");
            return;
        }
        final ProgressDialog dialog = ProgressDialog.show(AddTransportOrderActivity.this,"提示","正在努力加载...");
        String httpUrl = mySharedPreferences.getString("serviceAddress", "") + "/order/getTransportOrderById/"+orderId;
        HttpHelper helper = new HttpHelper(application,AddTransportOrderActivity.this,requestQueue,dialog) {
            @Override
            public void onResponse(JSONObject content) {
                getParameters(content);
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

    private void getParameters(JSONObject content){
        try {
            String clientOrderNumber = content.getString("clientOrderNumber");
            String transportOrderId = content.getString("transportOrderId");
            double totalVolume = content.getDouble("totalVolume");
            double totalWeight = content.getDouble("totalWeight");
            int totalItemQuantity = content.getInt("totalItemQuantity");
            int totalPackageQuantity = content.getInt("totalPackageQuantity");
            String startCityId = content.getString("startCityId");
            String startCity = content.getString("startCity");
            String destCityId = content.getString("destCityId");
            String destCity = content.getString("destCity");
            otdCarrierOrderBean.setStartCityId(UUID.fromString(startCityId));
            otdCarrierOrderBean.setDestCityId(UUID.fromString(destCityId));
            otdCarrierOrderBean.setStartCity(startCity);
            otdCarrierOrderBean.setDestCity(destCity);
            String receiveMan = content.getString("receiveMan");
            String receiveManPhone = content.getString("receivePhone");
            String receiveManAddress = content.getString("receiveAddress");
            otdCarrierOrderBean.setConsignee(receiveMan);
            otdCarrierOrderBean.setConsigneePhone(receiveManPhone);
            otdCarrierOrderBean.setConsigneeAddress(receiveManAddress);
            OtdCarrierOrderDetail detail = new OtdCarrierOrderDetail();
            detail.setTransportOrderId(UUID.fromString(transportOrderId));
            detail.setConfirmedVolume(totalVolume);
            detail.setTransportOrderNumber(clientOrderNumber);
            detail.setConfirmedWeight(totalWeight);
            detail.setConfirmedItemQuantity(totalItemQuantity);
            detail.setConfirmedPackageQuantity(totalPackageQuantity);
            detail.setReceivePageNumber(0);
            details.add(detail);
            OtdCarrierOrderDetailView detailView = new OtdCarrierOrderDetailView();
            detailView.setTransportOrderId(detail.getTransportOrderId());
            detailView.setReceiptPageNumber(detail.getReceivePageNumber());
            detailViews.add(detailView);

            numbers.add(clientOrderNumber+transportOrderId);
            addTransportOrder(transportOrderTable, clientOrderNumber,transportOrderId, totalVolume, totalWeight, totalItemQuantity, totalPackageQuantity, 0, detail, detailView);
            showToast("添加运输单成功!");
            updateUI(totalVolume, totalWeight, totalItemQuantity, totalPackageQuantity, 0);
            String num = orderCount.getText().toString();
            orderCount.setText(add(num,1)+"");
        } catch (JSONException e) {
            e.printStackTrace();
            showMassage("此单某些数据不完整!");
        }
    }

    private void addTransportOrder(final TableLayout table, final String orderNumber, final String transportOrderId,Double confirmedVolume,Double confirmedWeight,int totalItemQuantity,int totalPackageQuantity,int receiptPageNumber, final OtdCarrierOrderDetail detail, final OtdCarrierOrderDetailView detailView){

        String id = detail.getTransportOrderId().toString();
        TableRow row = new TableRow(getApplicationContext());
        TextView order = new TextView(getApplicationContext());
        order.setTextColor(Color.BLACK);
        order.setText(orderNumber);
        order.setPadding(3, 3, 3, 3);
        order.setGravity(Gravity.CENTER);

        TextView volume = new TextView(getApplicationContext());
        int n1 = num++;
        volume.setId(n1);
        volume.setText(confirmedVolume + "");
        volume.setTextColor(Color.BLACK);
        volume.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        volume.setPadding(3, 3, 3, 3);
        volume.setOnClickListener(new OnClick(volume.getText().toString()));
        volume.setGravity(Gravity.CENTER);

        TextView weight = new TextView(getApplicationContext());
        int n2 = num++;
        weight.setId(n2);
        weight.setText(confirmedWeight + "");
        weight.setTextColor(Color.BLACK);
        weight.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        weight.setPadding(3, 3, 3, 3);
        weight.setOnClickListener(new OnClick(weight.getText().toString()));
        weight.setGravity(Gravity.CENTER);

        TextView totalPackageCount = new TextView(getApplicationContext());
        int n4 = num++;
        totalPackageCount.setId(n4);
        totalPackageCount.setText(totalPackageQuantity + "");
        totalPackageCount.setTextColor(Color.BLACK);
        totalPackageCount.setInputType(InputType.TYPE_CLASS_NUMBER);
        totalPackageCount.setPadding(3, 3, 3, 3);
        totalPackageCount.setOnClickListener(new OnClick(totalPackageCount.getText().toString()));
        totalPackageCount.setGravity(Gravity.CENTER);

        TextView pageNumber = new TextView(getApplicationContext());
        int n5 = num++;
        pageNumber.setId(n5);
        pageNumber.setText(receiptPageNumber + "");
        pageNumber.setTextColor(Color.BLACK);
        pageNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
        pageNumber.setPadding(3, 3, 3, 3);
        pageNumber.setOnClickListener(new OnClick(pageNumber.getText().toString()));
        pageNumber.setGravity(Gravity.CENTER);

        //spinner
        final Spinner wrapType = new Spinner(getApplicationContext());
        ArrayAdapter<DataItem> mAdapter = new ArrayAdapter<>(getApplicationContext(),R.layout.list_item2, DataUtils.getWrapType());
        wrapType.setAdapter(mAdapter);
        wrapType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                DataItem dataItem = (DataItem) wrapType.getSelectedItem();
                detailView.setWrapType(Integer.parseInt(dataItem.getTextValue()));
                detailView.setWrapTypePosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if(detailView.getWrapTypePosition()!=null){
            wrapType.setSelection(detailView.getWrapTypePosition(),true);
        }

        Button button = new Button(getApplicationContext());
        button.setWidth(50);
        button.setHeight(40);
        button.setText("删除");
        button.setPadding(3, 3, 3, 3);
        button.setGravity(Gravity.CENTER);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TableRow tableRow = (TableRow) v.getParent();
                table.removeView(tableRow);
                numbers.remove(orderNumber+transportOrderId);
                details.remove(detail);
                detailViews.remove(detailView);
                updateUI(detail);
                String num = orderCount.getText().toString();
                orderCount.setText(sub(num,1)+"");
            }
        });

        row.addView(order);
        row.addView(volume);
        row.addView(weight);
        row.addView(totalPackageCount);
        row.addView(pageNumber);
        row.addView(wrapType);
        row.addView(button);

        table.addView(row);

        volume.addTextChangedListener(new TransportDetailChange(id, 2, n1));
        weight.addTextChangedListener(new TransportDetailChange(id, 3, n2));
        totalPackageCount.addTextChangedListener(new TransportDetailChange(id, 5, n4));
        pageNumber.addTextChangedListener(new TransportDetailChange(id,6,n5));

        addOrderNumber.setText(null);
    }

    private class OnClick implements View.OnClickListener{
        private String oldValue;

        public OnClick(String oldValue) {
            this.oldValue = oldValue;
        }

        @Override
        public void onClick(final View v) {
            final EditText editText = new EditText(getApplicationContext());
            editText.setText(oldValue);
            editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            AlertDialog.Builder builder = new AlertDialog.Builder(AddTransportOrderActivity.this);
            builder.setTitle("请输入");
            builder.setView(editText);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String newValue = editText.getText().toString().trim();
                    TextView textView = (TextView) v;
                    textView.setText(newValue);
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("取消", null);
            builder.show();
        }
    }

    private class TransportDetailChange implements TextWatcher {
        private String oldValue;
        private String id;
        private int position;
        int i;
        public TransportDetailChange(String id, int position,int i) {
            this.id = id;
            this.position = position;
            this.i = i;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            oldValue = s.toString();
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if(s.length()!=0){
                String value = s.toString();
                ProgressDialog update = ProgressDialog.show(AddTransportOrderActivity.this,"提示","更新中...");
                for(OtdCarrierOrderDetail detail:details){
                    if(detail.getTransportOrderId().toString().equalsIgnoreCase(id)){
                        if(2 == position){
                            String tVolume = totalVolume.getText().toString();
                            double count = sub(tVolume,new Double(oldValue.length()==0?"0":oldValue));
                            totalVolume.setText(add(value,count)+"");
                            detail.setConfirmedVolume(new Double(value));
                        }else if(3 == position){
                            String tWeight = totalWeight.getText().toString();
                            double count = sub(tWeight,new Double(oldValue.length()==0?"0":oldValue));
                            totalWeight.setText(add(value,count)+"");
                            detail.setConfirmedWeight(new Double(value));
                        }else if(4 == position){
                            String tItemCount = totalItemQuantity.getText().toString();
                            int count = sub(tItemCount,Integer.parseInt(oldValue.length()==0?"0":oldValue));
                            totalItemQuantity.setText(add(value,count)+"");
                            detail.setConfirmedItemQuantity(Integer.parseInt(value));
                        }
                        else if(5 == position){
                            String tPackageCount = totalPackageQuantity.getText().toString();
                            int count = sub(tPackageCount,Integer.parseInt(oldValue.length() == 0 ? "0" : oldValue));
                            totalPackageQuantity.setText(add(value,count)+"");
                            detail.setConfirmedPackageQuantity(Integer.parseInt(value));
                        }
                        else if(6 == position){
                            String receivePageCount = receivePageNumber.getText().toString();
                            int count = sub(receivePageCount,Integer.parseInt(oldValue.length() == 0 ? "0" : oldValue));
                            receivePageNumber.setText(add(value,count)+"");
                            detail.setReceivePageNumber(Integer.parseInt(value));
                        }
                        if(update!=null&&update.isShowing()){
                            update.dismiss();
                        }
                        break;
                    }
                }
            }

        }
    }

    private void updateUI(OtdCarrierOrderDetail detail) {
        String num1 = totalVolume.getText().toString();
        totalVolume.setText(sub(num1, detail.getConfirmedVolume()) + "");
        String num2 = totalWeight.getText().toString();
        totalWeight.setText(sub(num2, detail.getConfirmedWeight()) + "");
        String num3 = totalItemQuantity.getText().toString();
        totalItemQuantity.setText(sub(num3,detail.getConfirmedItemQuantity())+"");
        String num4 = totalPackageQuantity.getText().toString();
        totalPackageQuantity.setText(sub(num4,detail.getConfirmedPackageQuantity())+"");
        String num5 = receivePageNumber.getText().toString();
        receivePageNumber.setText(sub(num5, detail.getReceivePageNumber())+"");
    }

    private class HelpItem{
        private String transportOrderId;
        private String clientName;
        private String orderNumber;

        public HelpItem(String transportOrderId, String clientName, String orderNumber) {
            this.transportOrderId = transportOrderId;
            this.clientName = clientName;
            this.orderNumber = orderNumber;
        }

        public String getTransportOrderId() {
            return transportOrderId;
        }

        public void setTransportOrderId(String transportOrderId) {
            this.transportOrderId = transportOrderId;
        }

        public String getOrderNumber() {
            return orderNumber;
        }

        public void setOrderNumber(String orderNumber) {
            this.orderNumber = orderNumber;
        }

        @Override
        public String toString() {
            return clientName +
                    "," + orderNumber;
        }
    }
    private double add(String x, double y){
        BigDecimal add1 = new BigDecimal(x);
        BigDecimal add2 = new BigDecimal(y+"");
        return  add1.add(add2).doubleValue() ;
    }

    private int add(String x,int i){
        if(x.length()==0){
            return i;
        }
        return Integer.parseInt(x)+i;
    }
    private double sub(String x, double y){
        BigDecimal add1 = new BigDecimal(x);
        if(add1.equals(BigDecimal.ZERO)){
            return new Double(0);
        }
        BigDecimal add2 = new BigDecimal(y+"");
        return  add1.subtract(add2).doubleValue() ;
    }

    private int sub(String x,int i){
        return Integer.parseInt(x)-i;
    }



    private void updateUI(double tVolume, double tWeight, int tItemQuantity, int tPackageQuantity, int receiptPNumber) {
        String num1 = totalVolume.getText().toString();
        totalVolume.setText(add(num1, tVolume) + "");
        String num2 = totalWeight.getText().toString();
        totalWeight.setText(add(num2, tWeight) + "");
        String num3 = totalItemQuantity.getText().toString();
        totalItemQuantity.setText(add(num3,tItemQuantity)+"");
        String num4 = totalPackageQuantity.getText().toString();
        totalPackageQuantity.setText(add(num4,tPackageQuantity)+"");
        String num5 = receivePageNumber.getText().toString();
        receivePageNumber.setText(add(num5, receiptPNumber) + "");
    }

    private void closeInput(){
        View view =getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputManger = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void showMassage(String massage){
        AlertDialog.Builder builder  = new AlertDialog.Builder(AddTransportOrderActivity.this);
        builder.setTitle("提示" ) ;
        builder.setMessage(massage) ;
        builder.setPositiveButton("重新操作" ,  null );
        builder.show();
    }

    private void showToast(String massage){
        Toast.makeText(getApplicationContext(), massage, Toast.LENGTH_SHORT).show();
    }
}
