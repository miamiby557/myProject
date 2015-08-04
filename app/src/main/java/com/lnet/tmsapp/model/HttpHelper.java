package com.lnet.tmsapp.model;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.widget.Button;
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
import com.lnet.tmsapp.R;
import com.lnet.tmsapp.application.ApplicationTrans;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public abstract class HttpHelper {

    private Activity activity;
    private RequestQueue requestQueue;
    private ProgressDialog progressDialog;
    ApplicationTrans application;

    public HttpHelper(ApplicationTrans application,Activity activity, RequestQueue requestQueue,ProgressDialog progressDialog) {
        this.activity = activity;
        this.requestQueue = requestQueue;
        this.progressDialog = progressDialog;
        this.application = application;
    }

    public abstract void onResponse(JSONObject response);

    public abstract void onErrorResponse(JSONObject response);

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
                            if (progressDialog.isShowing() && progressDialog != null) {
                                progressDialog.dismiss();
                            }
                            windowTitle("服务器异常！");
                        }
                    }
            );

            requestQueue.add(request);

        } catch (Exception e) {
            showMessage(activity, e.getLocalizedMessage());
        }
    }

    public void testService(String url) {

        try {
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            resolveServiceTest(response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (progressDialog.isShowing() && progressDialog != null) {
                                progressDialog.dismiss();
                            }
                            windowTitle("测试不成功，请检查IP和端口！");
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
                JSONObject content = response.getJSONObject("content");
                onResponse(content);
            } else{
                if (progressDialog.isShowing() && progressDialog != null) {
                    progressDialog.dismiss();
                }
                onErrorResponse(response);
            }

        } catch (JSONException e) {
            windowTitle("服务器异常！");
        }
    }

    private void resolveServiceTest(JSONObject response) {
        try {
            if(response.getBoolean("success")){
                onResponse(response);
            }else{
               onErrorResponse(response);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("ResourceAsColor")
    private void resolveLogin(JSONObject response,Button loginButton) {
        try {
            if (response.getBoolean("success")) {
                JSONObject content = response.getJSONObject("content");
                onResponse(content);
            } else if (response != null) {
                if (progressDialog.isShowing() && progressDialog != null) {
                    progressDialog.dismiss();
                }
                loginButton.setBackgroundColor(R.color.purple);
                showMessage(activity, "用户名或密码错误！");
            }

        } catch (JSONException e) {
            windowTitle("服务器异常！");
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
                            if (progressDialog.isShowing() && progressDialog != null) {
                                progressDialog.dismiss();
                            }
                            windowTitle("服务器异常！");
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

    public void login(String url, JSONObject data, final Button loginButton) {
        try {
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, data,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            resolveLogin(response,loginButton);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (progressDialog.isShowing() && progressDialog != null) {
                                progressDialog.dismiss();
                            }
                            windowTitle("服务器异常！");
                        }
                    }){
                        @Override
                        protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                            try {

                                Map<String, String> responseHeaders = response.headers;
                                String cookies = responseHeaders.get("Set-Cookie").split(";")[0];
                                application.setCookie(cookies);
                                String dataString = new String(response.data, "UTF-8");
                                JSONObject jsonObject = new JSONObject(dataString);
                                return Response.success(jsonObject, HttpHeaderParser.parseCacheHeaders(response));
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                                return Response.error(new ParseError(e));
                            } catch (JSONException e) {
                                e.printStackTrace();
                                return null;
                            }
                        }

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
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }

    private void windowTitle(String massage){
        AlertDialog.Builder builder  = new AlertDialog.Builder(activity);
        builder.setTitle("提示" ) ;
        builder.setMessage(massage) ;
        builder.setPositiveButton("重新操作！" ,  null );
        builder.show();
    }
}
