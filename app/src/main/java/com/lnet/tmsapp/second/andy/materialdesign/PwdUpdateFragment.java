package com.lnet.tmsapp.second.andy.materialdesign;

/**
 * Created by Andy on 2015/5/28.
 */
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.lnet.tmsapp.LoginActivity;
import com.lnet.tmsapp.R;
import com.lnet.tmsapp.application.ApplicationTrans;
import com.lnet.tmsapp.model.HttpHelper;
import com.lnet.tmsapp.model.ServiceResult;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class PwdUpdateFragment extends Fragment {

    EditText oldPwd;
    EditText newPwd;
    EditText confirmPwd;
    Button updatePwd;
    Button cancel;
    SharedPreferences mySharedPreferences;
    RequestQueue requestQueue;
    ApplicationTrans application;
    LinearLayout linearLayout;

    public PwdUpdateFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.pwd_update, container, false);
        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        application = (ApplicationTrans)getActivity().getApplication();
        mySharedPreferences = getActivity().getSharedPreferences(application.getFILENAME(), application.getMODE());
        oldPwd = (EditText)rootView.findViewById(R.id.old_pwd);
        newPwd = (EditText)rootView.findViewById(R.id.new_pwd);
        confirmPwd = (EditText)rootView.findViewById(R.id.confirm_new);
        updatePwd = (Button)rootView.findViewById(R.id.update_pwd);
        updatePwd.setOnClickListener(new UpdatePWD());
        linearLayout = (LinearLayout)rootView.findViewById(R.id.linelayout1);
        linearLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().getApplicationContext().INPUT_METHOD_SERVICE);
                return imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
            }
        });
        cancel = (Button)rootView.findViewById(R.id.button_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getActivity().getSupportFragmentManager().popBackStack();
                getFragmentManager().popBackStack();
                ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle("个人信息");
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }
    private class UpdatePWD implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if(!check(oldPwd.getText().toString().trim())){
                showMassage("请输入旧密码！");
                return;
            }
            if(!check(newPwd.getText().toString().trim())){
                showMassage("请输入新密码！");
                return;
            }
            if(!check(confirmPwd.getText().toString().trim())){
                showMassage("请输入确认密码！");
                return;
            }
            if(!newPwd.getText().toString().trim().equalsIgnoreCase(confirmPwd.getText().toString().trim())){
                showMassage("两次新密码不一样！");
                return;
            }
            ProgressDialog updateDialog = ProgressDialog.show(getActivity(), "提示", "...更新中...");
            Map<String, String> map = new HashMap<>();
            map.put("userId", application.getUserId());
            map.put("oldPwd", oldPwd.getText().toString().trim());
            map.put("newPwd", newPwd.getText().toString().trim());
            String httpUrl =mySharedPreferences.getString("serviceAddress","")+"/sysUser/updatePwd";
            HttpHelper httpHelper = new HttpHelper(application,getActivity(),requestQueue,updateDialog) {

                @Override
                public void onResponse(JSONObject response) {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(),LoginActivity.class);
                    startActivity(intent);
                    showMessage(getActivity(),"修改成功！请重新登录...");

                }

                @Override
                public void onErrorResponse(JSONObject response) {
                    Gson gson = new Gson();
                    ServiceResult result = gson.fromJson(response.toString(),ServiceResult.class);
                    Map map = result.getMessages();
                    showMessage(getActivity(), (String) map.get("default"));
                }
            };
            httpHelper.post(httpUrl, new JSONObject(map));
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
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
