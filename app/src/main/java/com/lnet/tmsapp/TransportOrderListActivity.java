package com.lnet.tmsapp;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.lnet.tmsapp.application.ApplicationTrans;
import com.lnet.tmsapp.model.HttpArrayHelper;
import com.lnet.tmsapp.model.OtdTransportOrder;
import com.lnet.tmsapp.model.TransportOrderList;
import com.lnet.tmsapp.util.DataItem;
import com.lnet.tmsapp.util.ListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/7/29.
 */
public class TransportOrderListActivity extends Activity {

    private ListView listView;

    ApplicationTrans application;
    RequestQueue requestQueue;
    SharedPreferences mySharedPreferences;
    String FILENAME = "info";
    int MODE = MODE_PRIVATE;

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
                Intent intent = new Intent(this, MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("tag",2);
                intent.putExtras(bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            case R.id.action_refresh:
                closeInput();
                getData();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        SearchView searchView = (SearchView)menu.findItem(R.id.action_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                selectByNumber(newText);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_main);
        listView=(ListView)findViewById(R.id.list);
        application = (ApplicationTrans) getApplication();
        mySharedPreferences = getSharedPreferences(FILENAME,MODE);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        getData();

    }
    //远程拿数据
    private void getData() {
        final List<DataItem> list = new ArrayList<>();
        String userId = application.getUserId();
        String httpUrl = mySharedPreferences.getString("serviceAddress", "") + "/order/transportOrderList/" + userId;
        HttpArrayHelper httpHelper = new HttpArrayHelper(application,TransportOrderListActivity.this, requestQueue, null) {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject object = response.getJSONObject(i);
                        String transportOrderId = object.getString("transportOrderId");
                        String clientOrderNumber = object.getString("clientOrderNumber");
                        DataItem item = new DataItem(transportOrderId,clientOrderNumber);
                        list.add(item);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                ListAdapter adapter = new ListAdapter(TransportOrderListActivity.this,TransportOrderDetailActivity.class,list,getApplicationContext(),R.layout.list);
                listView.setAdapter(adapter);
            }
        };
        httpHelper.get(httpUrl);
    }

    private void selectByNumber(String number) {
        final List<DataItem> list = new ArrayList<>();
        String userId = application.getUserId();
        String httpUrl = mySharedPreferences.getString("serviceAddress","")+ "/order/transportOrderListByNumber";
        Map<String, String> map = new HashMap<>();
        map.put("userId",userId);
        map.put("number",number);
        HttpArrayHelper httpHelper = new HttpArrayHelper(application,TransportOrderListActivity.this, requestQueue, null) {
            @Override
            public void onResponse(JSONArray response) {
                if(response.length()==0){
                    showToast("没有记录！");
                }else{
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject object = response.getJSONObject(i);
                            String transportOrderId = object.getString("transportOrderId");
                            String clientOrderNumber = object.getString("clientOrderNumber");
                            DataItem item = new DataItem(transportOrderId,clientOrderNumber);
                            list.add(item);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    ListAdapter adapter = new ListAdapter(TransportOrderListActivity.this,TransportOrderDetailActivity.class,list,getApplicationContext(),R.layout.list);
                    listView.setAdapter(adapter);
                }

            }
        };
        httpHelper.post(httpUrl, new JSONObject(map));

    }

    private void closeInput(){
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputManger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    private void showToast(String massage){
        Toast.makeText(getApplicationContext(), massage, Toast.LENGTH_SHORT).show();
    }

}
