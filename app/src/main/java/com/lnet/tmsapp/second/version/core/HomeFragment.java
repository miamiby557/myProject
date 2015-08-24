package com.lnet.tmsapp.second.version.core;

/**
 * Created by Andy on 2015/5/28.
 */
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.lnet.tmsapp.R;
import com.lnet.tmsapp.view.MyImageView;


public class HomeFragment extends Fragment {

    MyImageView transportOrderCreate;
    MyImageView carrierOrderCreate;
    MyImageView carSend;
    MyImageView carBack;
    MyImageView paichedan;
    MyImageView orderSignUp;
    Fragment fragment;

    public HomeFragment() {
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu_create, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        transportOrderCreate = (MyImageView)view.findViewById(R.id.transportorder_create);
        carrierOrderCreate = (MyImageView)view.findViewById(R.id.carrierorder_create);
        carSend = (MyImageView)view.findViewById(R.id.car_send);
        carBack = (MyImageView)view.findViewById(R.id.car_back);
        paichedan = (MyImageView)view.findViewById(R.id.paichedan_create);
        orderSignUp = (MyImageView)view.findViewById(R.id.order_sign_up);
        transportOrderCreate.setOnClickIntent(new MyImageView.OnViewClickListener() {
            @Override
            public void onViewClick(MyImageView view) {
                fragment = new TransportOrderCreateFragment();
                String title = getString(R.string.title_transport_order_create);
                changeFragment(title);
            }
        });
        carrierOrderCreate.setOnClickIntent(new MyImageView.OnViewClickListener() {
            @Override
            public void onViewClick(MyImageView view) {
                fragment = new CarrierOrderCreateFragment();
                String title = getString(R.string.title_carrier_order_create);
                changeFragment(title);
            }
        });

        carSend.setOnClickIntent(new MyImageView.OnViewClickListener() {
            @Override
            public void onViewClick(MyImageView view) {
                fragment = new CarSendFragment();
                String title = getString(R.string.title_car_send);
                changeFragment(title);
            }
        });

        carBack.setOnClickIntent(new MyImageView.OnViewClickListener() {
            @Override
            public void onViewClick(MyImageView view) {
                fragment = new CarBackFragment();
                String title = getString(R.string.title_car_back);
                changeFragment(title);
            }
        });
        paichedan.setOnClickIntent(new MyImageView.OnViewClickListener() {
            @Override
            public void onViewClick(MyImageView view) {
                fragment = new PaichedanFragment();
                String title = "派车单创建";
                changeFragment(title);
            }
        });
        orderSignUp.setOnClickIntent(new MyImageView.OnViewClickListener() {
            @Override
            public void onViewClick(MyImageView view) {
                fragment = new OrderSignUpFragment();
                String title = "订单签收";
                changeFragment(title);
            }
        });
        return view;
    }

    private void changeFragment(String title){
        if (fragment != null) {
            FragmentManager fragmentManager =getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

            // set the toolbar title
            ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle(title);
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
