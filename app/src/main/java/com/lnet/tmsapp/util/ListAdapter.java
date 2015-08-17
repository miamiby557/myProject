package com.lnet.tmsapp.util;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lnet.tmsapp.R;

import java.util.List;

/**
 * Created by Administrator on 2015/7/29.
 */
public class ListAdapter extends BaseAdapter {
    private List<DataItem> list;
    private Context context;
    private int layout;
    FragmentActivity activity;
    Fragment fragment;

    private LayoutInflater inflater;

    public ListAdapter(FragmentActivity activity,Fragment fragment,List<DataItem> list, Context context,int layout)
    {
        this.list = list;
        this.context = context;
        this.layout = layout;
        this.activity = activity;
        this.fragment = fragment;

        inflater =(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    /*
     * 获取总行数
     * @see android.widget.Adapter#getCount()
     */
    public int getCount() {
        return list.size();
    }

    /*
     * 根据索引获取数据
     * @see android.widget.Adapter#getItem(int)
     */
    public Object getItem(int position) {
        return list.get(position);
    }

    /*
     * item的id，很少使用到
     * @see android.widget.Adapter#getItemId(int)
     */
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder cache;
        if(convertView==null)
        {
            convertView = inflater.inflate(layout, null);
            cache=new ViewHolder();
            cache.info=(TextView)convertView.findViewById(R.id.info);
            cache.viewBtn=(Button)convertView.findViewById(R.id.view_btn);
            convertView.setTag(cache);
        }
        else
        {
            cache=(ViewHolder)convertView.getTag();
        }
        final DataItem item=list.get(position);
        cache.info.setText(item.getTextName());
        cache.viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager =activity.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Bundle bundle = new Bundle();
                bundle.putString("id", item.getTextValue());
                fragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        return convertView;
    }

    public final class ViewHolder{
        public TextView info;
        public Button viewBtn;
    }

    private void showToast(String massage){
        Toast.makeText(context, massage, Toast.LENGTH_SHORT).show();
    }
}
