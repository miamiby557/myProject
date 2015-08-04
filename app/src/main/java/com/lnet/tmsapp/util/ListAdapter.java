package com.lnet.tmsapp.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lnet.tmsapp.R;
import com.lnet.tmsapp.TransportOrderDetailActivity;
import com.lnet.tmsapp.model.OtdTransportOrder;

import java.util.List;

/**
 * Created by Administrator on 2015/7/29.
 */
public class ListAdapter extends BaseAdapter {
    private List<DataItem> list;
    private Context context;
    private int layout;
    Intent intent;
    Activity activity;
    Class c;

    private LayoutInflater inflater;

    public ListAdapter(Activity activity,Class c,List<DataItem> list, Context context,int layout)
    {
        this.list = list;
        this.context = context;
        this.layout = layout;
        this.activity = activity;
        this.c = c;

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
                intent = new Intent(activity,c);
                Bundle bundle = new Bundle();
                bundle.putString("id", item.getTextValue());
                intent.putExtras(bundle);
                activity.startActivity(intent);
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
