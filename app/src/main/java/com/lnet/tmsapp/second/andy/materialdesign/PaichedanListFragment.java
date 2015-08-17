package com.lnet.tmsapp.second.andy.materialdesign;

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
import android.support.v4.view.MenuItemCompat;
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
import android.widget.ListView;
import android.widget.SearchView;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lnet.tmsapp.CaptureActivity;
import com.lnet.tmsapp.R;
import com.lnet.tmsapp.application.ApplicationTrans;
import com.lnet.tmsapp.model.DispatchVehicle;
import com.lnet.tmsapp.model.HttpArrayHelper;
import com.lnet.tmsapp.model.HttpHelper;
import com.lnet.tmsapp.util.DataItem;
import com.lnet.tmsapp.util.ListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PaichedanListFragment extends Fragment {

    private ListView listView;
    LinearLayout linearLayout;
    ApplicationTrans application;
    SharedPreferences mySharedPreferences;
    RequestQueue requestQueue;
    public PaichedanListFragment() {
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

    private void refresh(){
        getData();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.list_main, container, false);
        application = (ApplicationTrans)getActivity().getApplication();
        mySharedPreferences = getActivity().getSharedPreferences(application.getFILENAME(), application.getMODE());
        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        listView=(ListView)rootView.findViewById(R.id.list);
        linearLayout = (LinearLayout)rootView.findViewById(R.id.linearlayout);
        linearLayout.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().getApplicationContext().INPUT_METHOD_SERVICE);
                return imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
            }
        });
        fillData();
        return rootView;
    }

    private void fillData() {
        if(application.getDispatchAssignList()!=null){
            ListAdapter adapter = new ListAdapter(getActivity(), new DispatchAssignDetailFragment(), application.getDispatchAssignList(), getActivity().getApplicationContext(), R.layout.list);
            listView.setAdapter(adapter);
        }else {
            //远程拿数据
            getData();
        }
    }

    private void getData(){
        final List<DataItem> list = new ArrayList<>();
        String userId = application.getUserId();
        final ProgressDialog dialog = ProgressDialog.show(getActivity(), "提示", "正在努力加载...");
        String httpUrl = mySharedPreferences.getString("serviceAddress", "") + "/order/getDispatchAssignList/" + userId;
        HttpArrayHelper httpHelper = new HttpArrayHelper(application, getActivity(), requestQueue, dialog) {
            @Override
            public void onResponse(JSONArray response) {
                if (response.length() == 0) {
                    showToast("没有记录！");
                } else {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject object = response.getJSONObject(i);
                            String dispatchAssignId = object.getString("dispatchAssignId");
                            String dispatchAssignNumber = object.getString("dispatchAssignNumber");
                            DataItem item = new DataItem(dispatchAssignId, dispatchAssignNumber);
                            list.add(item);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    ListAdapter adapter = new ListAdapter(getActivity(), new DispatchAssignDetailFragment(), list, getActivity().getApplicationContext(), R.layout.list);
                    listView.setAdapter(adapter);
                    application.setDispatchAssignList(list);
                }
                if (dialog.isShowing() && dialog != null) {
                    dialog.dismiss();
                }
            }
        };
        httpHelper.get(httpUrl);
    }

    private void selectByNumber(String number) {
        final List<DataItem> list = new ArrayList<>();
        String userId = application.getUserId();
        String httpUrl = mySharedPreferences.getString("serviceAddress", "") + "/order/getDispatchAssignListByNumber";
        Map<String, String> map = new HashMap<>();
        map.put("userId", userId);
        map.put("number", number);
        HttpArrayHelper httpHelper = new HttpArrayHelper(application, getActivity(), requestQueue, null) {

            @Override
            public void onResponse(JSONArray response) {
                if(response.length()==0){
                    ListAdapter adapter = new ListAdapter(getActivity(),new DispatchAssignDetailFragment(),list,getActivity().getApplicationContext(),R.layout.list);
                    listView.setAdapter(adapter);
                    showToast("没有记录！");
                }else{
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject object = response.getJSONObject(i);
                            String dispatchAssignId = object.getString("dispatchAssignId");
                            String dispatchAssignNumber = object.getString("dispatchAssignNumber");
                            DataItem item = new DataItem(dispatchAssignId, dispatchAssignNumber);
                            list.add(item);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    ListAdapter adapter = new ListAdapter(getActivity(), new DispatchAssignDetailFragment(), list, getActivity().getApplicationContext(), R.layout.list);
                    listView.setAdapter(adapter);
                }
            }
        };

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
