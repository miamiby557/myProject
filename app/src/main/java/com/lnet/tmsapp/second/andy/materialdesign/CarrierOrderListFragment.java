package com.lnet.tmsapp.second.andy.materialdesign;

/**
 * Created by Andy on 2015/5/28.
 */
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.lnet.tmsapp.R;
import com.lnet.tmsapp.application.ApplicationTrans;
import com.lnet.tmsapp.model.HttpArrayHelper;
import com.lnet.tmsapp.util.DataItem;
import com.lnet.tmsapp.util.ListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CarrierOrderListFragment extends Fragment {

    private ListView listView;
    LinearLayout linearLayout;
    ApplicationTrans application;
    RequestQueue requestQueue;
    SharedPreferences mySharedPreferences;

    public CarrierOrderListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView sv = new SearchView(((MainActivity) getActivity()).getSupportActionBar().getThemedContext());
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        MenuItemCompat.setActionView(item, sv);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                selectByNumber(newText);
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_refresh:
                closeInput();
                refresh();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.list_main, container, false);
        listView=(ListView)rootView.findViewById(R.id.list);
        linearLayout = (LinearLayout)rootView.findViewById(R.id.linearlayout);
        linearLayout.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().getApplicationContext().INPUT_METHOD_SERVICE);
                return imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
            }
        });
        application = (ApplicationTrans)getActivity().getApplication();
        mySharedPreferences = getActivity().getSharedPreferences(application.getFILENAME() , application.getMODE());
        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        fillData();

        // Inflate the layout for this fragment
        return rootView;
    }
    private void fillData() {
        if(application.getCarrierOrderItem()!=null){
            ListAdapter adapter = new ListAdapter(getActivity(),new CarrierOrderDetailFragment(),application.getCarrierOrderItem(),getActivity().getApplicationContext(),R.layout.list);
            listView.setAdapter(adapter);
        }else {
            getData();
        }
    }
    private void refresh(){
        getData();
    }

    private void getData(){
        final List<DataItem> list = new ArrayList<>();
        String userId = application.getUserId();
        final ProgressDialog dialog = ProgressDialog.show(getActivity(), "提示", "正在努力加载...");
        String httpUrl = mySharedPreferences.getString("serviceAddress","") + "/order/carrierOrderList/" + userId;
        HttpArrayHelper httpHelper = new HttpArrayHelper(application,getActivity(), requestQueue, null) {
            @Override
            public void onResponse(JSONArray response) {
                if(response.length()==0){
                    showToast("没有记录！");
                }else{
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject object = response.getJSONObject(i);
                            String carrierOrderId = object.getString("carrierOrderId");
                            String carrierOrderNumber = object.getString("carrierOrderNumber");
                            DataItem item = new DataItem(carrierOrderId,carrierOrderNumber);
                            list.add(item);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    ListAdapter adapter = new ListAdapter(getActivity(),new CarrierOrderDetailFragment(),list,getActivity().getApplicationContext(),R.layout.list);
                    listView.setAdapter(adapter);
                    application.setCarrierOrderItem(list);
                }
                if (dialog.isShowing() && dialog != null) {
                    dialog.dismiss();
                }
            }
        };
        httpHelper.get(httpUrl);
    }


    private void selectByNumber(String newText) {
        /*final List<DataItem> list = new ArrayList<>();
        String httpUrl = mySharedPreferences.getString("serviceAddress", "") + "/order/carrierOrderListByNumber";
        String userId = application.getUserId();
        Map<String, String> map = new HashMap<>();
        map.put("userId",userId);
        map.put("number", newText);
        HttpArrayHelper httpHelper = new HttpArrayHelper(application,getActivity(), requestQueue, null) {
            @Override
            public void onResponse(JSONArray response) {
                if(response.length()==0){
                    ListAdapter adapter = new ListAdapter(getActivity(),new CarrierOrderListFragment(),list,getActivity().getApplicationContext(),R.layout.list);
                    listView.setAdapter(adapter);
                    showToast("没有记录！");
                }else{
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject object = response.getJSONObject(i);
                            String carrierOrderId = object.getString("carrierOrderId");
                            String carrierOrderNumber = object.getString("carrierOrderNumber");
                            DataItem item = new DataItem(carrierOrderId,carrierOrderNumber);
                            list.add(item);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    ListAdapter adapter = new ListAdapter(getActivity(),new CarrierOrderListFragment(),list,getActivity().getApplicationContext(),R.layout.list);
                    listView.setAdapter(adapter);
                }
            }
        };
        httpHelper.post(httpUrl, new JSONObject(map));*/
        List<DataItem> items = getItemsByNumber(newText);
        ListAdapter adapter = new ListAdapter(getActivity(),new CarrierOrderListFragment(),items,getActivity().getApplicationContext(),R.layout.list);
        listView.setAdapter(adapter);
    }

    private List<DataItem> getItemsByNumber(String number){
        List<DataItem> items = application.getCarrierOrderItem();
        List<DataItem> list = new ArrayList<>();
        for(DataItem item:items){
            if(item.getTextName().contains(number)){
               list.add(item);
            }
        }
        return list;
    }

    private void closeInput(){
        View view =getActivity().getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputManger = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    private void showToast(String massage){
        Toast.makeText(getActivity().getApplicationContext(), massage, Toast.LENGTH_LONG).show();
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
