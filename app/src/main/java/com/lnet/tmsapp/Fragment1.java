package com.lnet.tmsapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.lnet.tmsapp.view.MyImageView;

public class Fragment1 extends Fragment {

    MyImageView transportOrderCreate;
    MyImageView carrierOrderCreate;
    MyImageView carSend;
    MyImageView carBack;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fg1, container,false);
        transportOrderCreate = (MyImageView)view.findViewById(R.id.transportorder_create);
        carrierOrderCreate = (MyImageView)view.findViewById(R.id.carrierorder_create);
        carSend = (MyImageView)view.findViewById(R.id.car_send);
        carBack = (MyImageView)view.findViewById(R.id.car_back);

        transportOrderCreate.setOnClickIntent(new MyImageView.OnViewClickListener() {
            @Override
            public void onViewClick(MyImageView view) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), TransportOrderActivity.class);
                startActivity(intent);
            }
        });
        carrierOrderCreate.setOnClickIntent(new MyImageView.OnViewClickListener() {
            @Override
            public void onViewClick(MyImageView view) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), CarrierOrderActivity.class);
                startActivity(intent);
            }
        });

        carSend.setOnClickIntent(new MyImageView.OnViewClickListener() {
            @Override
            public void onViewClick(MyImageView view) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), CarSendActivity.class);
                startActivity(intent);
            }
        });

        carBack.setOnClickIntent(new MyImageView.OnViewClickListener() {
            @Override
            public void onViewClick(MyImageView view) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), CarBackActivity.class);
                startActivity(intent);
            }
        });

		return view;
	}
}
