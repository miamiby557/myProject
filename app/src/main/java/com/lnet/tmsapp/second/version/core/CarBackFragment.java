package com.lnet.tmsapp.second.version.core;

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
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.lnet.tmsapp.R;
import com.lnet.tmsapp.application.ApplicationTrans;
import com.lnet.tmsapp.model.HttpHelper;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class CarBackFragment extends Fragment {

    EditText number ;
    Button carBack;
    LinearLayout linearLayout;

    SharedPreferences mySharedPreferences;
    RequestQueue requestQueue;
    ApplicationTrans application;

    public CarBackFragment() {
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
        View rootView = inflater.inflate(R.layout.car_back, container, false);
        application = (ApplicationTrans)getActivity().getApplication();
        mySharedPreferences = getActivity().getSharedPreferences(application.getFILENAME(), application.getMODE());
        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        linearLayout = (LinearLayout)rootView.findViewById(R.id.linelayout);
        linearLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().getApplicationContext().INPUT_METHOD_SERVICE);
                return imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
            }
        });
        number = (EditText)rootView.findViewById(R.id.number);
        number.setOnTouchListener(new TouchEvent());
        carBack = (Button)rootView.findViewById(R.id.car_back);
        carBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carBack();
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

    private void carBack() {
        if(number.length()==0){
            showMassage("请输入派车单号");
            return;
        }
        Map<String,String> map = new HashMap<>();
        map.put("userId",application.getUserId());
        map.put("orderNumber", number.getText().toString().trim());
        ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "提示", "发车中...");
        String url = mySharedPreferences.getString("serviceAddress", "") +"/location/carBack";
        HttpHelper helper = new HttpHelper(application,getActivity(),requestQueue,progressDialog) {
            @Override
            public void onResponse(JSONObject response) {
                number.setText("");
                number.setHint("输入派车单号");
                Toast.makeText(getActivity(), "回场完成！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onErrorResponse(JSONObject response) {

            }
        };
        helper.post(url, new JSONObject(map));
    }

    private class TouchEvent implements View.OnTouchListener{

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                int size = number.getRight();
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
        super.onActivityResult(requestCode, resultCode, data);
        //处理扫描结果（在界面上显示）
        if (resultCode == -1) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
            number.setText(scanResult);
        }
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
