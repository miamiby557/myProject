package com.lnet.tmsapp.second.andy.materialdesign;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.lnet.tmsapp.LoginActivity;
import com.lnet.tmsapp.R;
import com.lnet.tmsapp.application.ApplicationTrans;
import com.lnet.tmsapp.model.HttpHelper;

import org.json.JSONObject;

/**
 * Created by Administrator on 2015/8/17.
 */
public class ServiceConfigFrament extends Fragment{
    EditText serviceAddress;
    EditText port;
    Button testButton;
    ProgressDialog testServiceDialog;

    RequestQueue requestQueue;
    SharedPreferences mySharedPreferences;
    SharedPreferences.Editor editor;
    ApplicationTrans application;

    LinearLayout linearLayout;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_complete2, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_back:
                save();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void save(){
        String ip = serviceAddress.getText().toString().trim();
        if(!check(ip)){
            showMassage("请输入服务器ip!");
            return;
        }
        String portString = port.getText().toString().trim();
        if(!check(portString)){
            showMassage("请输入端口号!");
            return;
        }
        editor = mySharedPreferences.edit();
        String serAddress = serviceAddress.getText().toString().trim()+":"+port.getText().toString().trim();
        editor.putString("serviceAddress", "http://"+serAddress+"/service/rest");
        editor.putString("ip",ip);
        editor.putString("port", portString);
        editor.commit();
        Intent intent = new Intent();
        intent.setClass(getActivity(), LoginActivity.class);
        startActivity(intent);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.service_config, container, false);
        serviceAddress = (EditText)view.findViewById(R.id.ser_address);
        port = (EditText)view.findViewById(R.id.port);
        testButton = (Button)view.findViewById(R.id.service_test_btn);
        application = (ApplicationTrans)getActivity().getApplication();
        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        mySharedPreferences =getActivity().getSharedPreferences(application.getFILENAME(), application.getMODE());

        String ip = mySharedPreferences.getString("ip","");
        serviceAddress.setText(ip);
        String portS = mySharedPreferences.getString("port","");
        port.setText(portS);

        testButton.setOnClickListener(new TestListener());

        //关闭输入法
        linearLayout = (LinearLayout)view.findViewById(R.id.linelayout1);
        linearLayout.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().getApplicationContext().INPUT_METHOD_SERVICE);
                return imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
            }
        });
        return view;
    }

    private class TestListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            String text = serviceAddress.getText().toString().trim();
            if(!check(text)){
                showMassage("请输入服务器ip!");
                return;
            }
            if(!check(port.getText().toString().trim())){
                showMassage("请输入端口号!");
                return;
            }
            testService();
        }
    }

    private void testService() {
        String address = serviceAddress.getText().toString().trim()+":"+port.getText().toString().trim();
        testServiceDialog = ProgressDialog.show(getActivity(), "测试提示", "...测试中...");
        String serAddress = "http://"+address+"/service/rest/sysUser/testService";
        HttpHelper httpHelper = new HttpHelper(application,getActivity(),requestQueue,testServiceDialog) {

            @Override
            public void onResponse(JSONObject response) {
                if(testServiceDialog!=null && testServiceDialog.isShowing()){
                    testServiceDialog.dismiss();
                }
                showMessage(getActivity(), "测试成功！");
            }

            @Override
            public void onErrorResponse(JSONObject response) {
                showMessage(getActivity(), "测试失败！");
            }
        };
        httpHelper.testService(serAddress);
    }
    private void showMassage(String massage){
        AlertDialog.Builder builder  = new AlertDialog.Builder(getActivity());
        builder.setTitle("提示" ) ;
        builder.setMessage(massage) ;
        builder.setPositiveButton("重新操作" ,  null );
        builder.show();
    }

    private boolean check(String value){
        if(value.length()!=0){
            return true;
        }
        return false;
    }

}
