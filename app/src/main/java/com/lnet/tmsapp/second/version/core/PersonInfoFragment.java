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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.lnet.tmsapp.R;
import com.lnet.tmsapp.application.ApplicationTrans;


public class PersonInfoFragment extends Fragment {

    TextView loginName;
    Button pwdUpdate;
    ApplicationTrans application;

    public PersonInfoFragment() {
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
        View rootView = inflater.inflate(R.layout.person_info, container, false);
        application = (ApplicationTrans)getActivity().getApplication();
        loginName = (TextView)rootView.findViewById(R.id.login_name);
        loginName.setText(application.getLoginName());
        pwdUpdate = (Button)rootView.findViewById(R.id.password_update);
        pwdUpdate.setOnClickListener(new PwdUpdate());

        // Inflate the layout for this fragment
        return rootView;
    }
    private class PwdUpdate implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Fragment fragment = new PwdUpdateFragment();
            if(fragment!=null){
                FragmentManager fragmentManager =getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                // set the toolbar title
                ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle("修改密码");
            }
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
