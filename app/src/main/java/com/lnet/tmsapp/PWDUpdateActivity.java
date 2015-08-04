package com.lnet.tmsapp;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.lnet.tmsapp.application.ApplicationTrans;
import com.lnet.tmsapp.model.HttpHelper;
import com.lnet.tmsapp.model.ServiceResult;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/7/31.
 */
public class PWDUpdateActivity extends FragmentActivity{
    EditText oldPwd;
    EditText newPwd;
    EditText confirmPwd;
    Button updatePwd;
    SharedPreferences mySharedPreferences;
    RequestQueue requestQueue;
    String FILENAME = "info";
    int MODE = MODE_PRIVATE;
    ApplicationTrans application;
    LinearLayout linearLayout;

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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pwd_update);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        application = (ApplicationTrans)getApplication();
        mySharedPreferences = getSharedPreferences(FILENAME,MODE);
        oldPwd = (EditText)findViewById(R.id.old_pwd);
        newPwd = (EditText)findViewById(R.id.new_pwd);
        confirmPwd = (EditText)findViewById(R.id.confirm_new);
        updatePwd = (Button)findViewById(R.id.update_pwd);
        updatePwd.setOnClickListener(new UpdatePWD());
        linearLayout = (LinearLayout)findViewById(R.id.linelayout1);
        linearLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                return imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        });
    }

    private class UpdatePWD implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if(check(oldPwd.getText().toString().trim())){
               showMassage("请输入旧密码！");
                return;
            }
            if(check(newPwd.getText().toString().trim())){
                showMassage("请输入新密码！");
                return;
            }
            if(check(confirmPwd.getText().toString().trim())){
                showMassage("请输入确认密码！");
                return;
            }
            if(!newPwd.getText().toString().trim().equalsIgnoreCase(confirmPwd.getText().toString().trim())){
                showMassage("两次新密码不一样！");
                return;
            }
            ProgressDialog updateDialog = ProgressDialog.show(PWDUpdateActivity.this, "提示", "...更新中...");
            Map<String, String> map = new HashMap<>();
            map.put("userId", application.getUserId());
            map.put("oldPwd", oldPwd.getText().toString().trim());
            map.put("newPwd", newPwd.getText().toString().trim());
            String httpUrl =mySharedPreferences.getString("serviceAddress","")+"/sysUser/updatePwd";
            HttpHelper httpHelper = new HttpHelper(application,PWDUpdateActivity.this,requestQueue,updateDialog) {

                @Override
                public void onResponse(JSONObject response) {
                    Intent intent = new Intent();
                    intent.setClass(PWDUpdateActivity.this,LoginActivity.class);
                    startActivity(intent);
                    showMessage(PWDUpdateActivity.this,"修改成功！请重新登录...");

                }

                @Override
                public void onErrorResponse(JSONObject response) {
                    Gson gson = new Gson();
                    ServiceResult result = gson.fromJson(response.toString(),ServiceResult.class);
                    Map map = result.getMessages();
                    showMessage(PWDUpdateActivity.this, (String) map.get("default"));
                }
            };
            httpHelper.post(httpUrl, new JSONObject(map));
        }
    }

    private boolean check(String value){
        if(value.length()!=0){
            return false;
        }
        return true;
    }
    private void showMassage(String massage){
        AlertDialog.Builder builder  = new AlertDialog.Builder(PWDUpdateActivity.this);
        builder.setTitle("提示" ) ;
        builder.setMessage(massage) ;
        builder.setPositiveButton("重新操作" ,  null );
        builder.show();
    }
}
