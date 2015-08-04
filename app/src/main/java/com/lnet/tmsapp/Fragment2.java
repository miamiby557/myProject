package com.lnet.tmsapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.lnet.tmsapp.application.ApplicationTrans;

public class Fragment2 extends Fragment {

    TextView loginName;
    Button logoutButton;
    Button pwdUpdate;
    Button paichedan;
    Button transportOrderCheck;
    Button carrierOrderCheck;
    ApplicationTrans application;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fg2, container,false);
        loginName = (TextView)view.findViewById(R.id.login_name);
        application = (ApplicationTrans)getActivity().getApplication();
        loginName.setText(application.getLoginName());
        logoutButton = (Button)view.findViewById(R.id.logout);
        pwdUpdate = (Button)view.findViewById(R.id.password_reset);
        paichedan = (Button)view.findViewById(R.id.paichedan);
        transportOrderCheck = (Button)view.findViewById(R.id.tranportorder);
        carrierOrderCheck = (Button)view.findViewById(R.id.carrierorder);
        carrierOrderCheck.setOnClickListener(new CarrierOrderCheck());
        transportOrderCheck.setOnClickListener(new TransportOrderCheck());
        paichedan.setOnClickListener(new Paichedan());
        pwdUpdate.setOnClickListener(new PwdUpdate());
        logoutButton.setOnClickListener(new Logout());
		return view;
	}

    private class Logout implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            new AlertDialog.Builder(getActivity()).setTitle("退出登录").setMessage("确认退出？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent();
                            intent.setClass(getActivity(), LoginActivity.class);
                            startActivity(intent);
                        }})
                    .setNegativeButton("取消",null)
                    .show();
        }
    }

    private class PwdUpdate implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(getActivity(), PWDUpdateActivity.class);
            startActivity(intent);
        }
    }
    private class Paichedan implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(getActivity(), PaichedanActivity.class);
            startActivity(intent);
        }
    }
    private class TransportOrderCheck implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(getActivity(), TransportOrderListActivity.class);
            startActivity(intent);
        }
    }
    private class CarrierOrderCheck implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(getActivity(), CarrierOrderListActivity.class);
            startActivity(intent);
        }
    }
}
