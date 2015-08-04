package com.lnet.tmsapp.model;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.lnet.tmsapp.application.ApplicationTrans;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public abstract class HttpArrayHelper {

    private Activity activity;
    private RequestQueue requestQueue;
    private ProgressDialog progressDialog;
    ApplicationTrans application;


    public HttpArrayHelper(ApplicationTrans application,Activity activity, RequestQueue requestQueue, ProgressDialog progressDialog) {
        this.activity = activity;
        this.requestQueue = requestQueue;
        this.progressDialog = progressDialog;
        this.application = application;
    }

    public abstract void onResponse(JSONArray response);

    public void get(String url) {

        try {
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            resolve(response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (progressDialog!=null&&progressDialog.isShowing() && progressDialog != null) {
                                progressDialog.dismiss();
                            }
                            windowTitle(activity);
                        }
                    }
            );

            requestQueue.add(request);

        } catch (Exception e) {
            showMessage(activity, e.getLocalizedMessage());
        }
    }

    private void resolve(JSONObject response) {
        try {
            if (response.getBoolean("success")) {
                JSONArray array = response.getJSONArray("content");
                onResponse(array);
            } else if (response != null) {
                showMessage(activity, "出现异常，请重新操作！");
            }

        } catch (JSONException e) {
            showMessage(activity, e.getLocalizedMessage());
        }
    }

    public void post(String url, JSONObject data) {
        try {
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, data,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            resolve(response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (progressDialog!=null&&progressDialog.isShowing() && progressDialog != null) {
                                progressDialog.dismiss();
                            }
                            windowTitle(activity);
                        }
                    }){
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map localHashMap = new HashMap();
                            localHashMap.put("Cookie", application.getCookie());
                            return localHashMap;
                        }
            };

            requestQueue.add(request);

        } catch (Exception e) {
            showMessage(activity, e.getLocalizedMessage());
        }
    }

    public static void showMessage(Activity activity, String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
    }
    private void windowTitle(Activity activity){
        AlertDialog.Builder builder  = new AlertDialog.Builder(activity);
        builder.setTitle("提示" ) ;
        builder.setMessage("服务器异常！" ) ;
        builder.setPositiveButton("重新操作！" ,  null );
        builder.show();
    }
}
