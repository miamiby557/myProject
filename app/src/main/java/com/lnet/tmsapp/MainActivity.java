package com.lnet.tmsapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.lnet.tmsapp.Service.LocationService;
import com.lnet.tmsapp.application.ApplicationTrans;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends FragmentActivity implements OnClickListener,View.OnTouchListener,GestureDetector.OnGestureListener{

	//定义2个Fragment的对象
	private Fragment1 fg1;
	private Fragment2 fg2;
	//帧布局对象,就是用来存放Fragment的容器
	private FrameLayout flayout;
	//定义底部导航栏的两个布局
	private RelativeLayout found_layout;
	private RelativeLayout settings_layout;
	//定义底部导航栏中的ImageView与TextView
	private ImageView found_image;
	private ImageView settings_image;
	private TextView settings_text;
	private TextView found_text;

	//定义要用的颜色值
	private int whirt = 0xFFFFFFFF;
	private int gray = 0xFF7597B3;
	private int blue =0xFF0AB2FB;
	//定义FragmentManager对象
	FragmentManager fManager;

	RequestQueue requestQueue;

	ApplicationTrans application;

	GestureDetector mGestureDetector;
	private int verticalMinDistance = 20;
	private int minVelocity = 0;

	int tag = 1;

	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Intent i= new Intent(Intent.ACTION_MAIN);
            i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            i.addCategory(Intent.CATEGORY_HOME);
            startActivity(i);
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		fManager = getSupportFragmentManager();
		mGestureDetector = new GestureDetector(this);
		flayout = (FrameLayout)findViewById(R.id.content);
		flayout.setOnTouchListener(this);
		flayout.setLongClickable(true);
		application = (ApplicationTrans)getApplication();
		requestQueue = Volley.newRequestQueue(getApplicationContext());
		initViews();
		Bundle bundle = this.getIntent().getExtras();
		if(bundle!=null){
			setChioceItem(2);
		}else{
			setChioceItem(1);
		}
		Intent intent = new Intent(getApplicationContext(), LocationService.class);
		getApplicationContext().startService(intent);
	}

	//完成组件的初始化
	public void initViews()
	{
		found_image = (ImageView) findViewById(R.id.found_image);
		settings_image = (ImageView) findViewById(R.id.setting_image);
		found_text = (TextView) findViewById(R.id.found_text);
		settings_text = (TextView) findViewById(R.id.setting_text);
		found_layout = (RelativeLayout) findViewById(R.id.found_layout);
		settings_layout = (RelativeLayout) findViewById(R.id.setting_layout);
		found_layout.setOnClickListener(this);
		settings_layout.setOnClickListener(this);
	}
	
	//重写onClick事件
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
	    case R.id.found_layout:
	    	setChioceItem(1);
	    	break;
	    case R.id.setting_layout:
	    	setChioceItem(2);
	    	break;
	    default:
			break;
		}
		
	}

	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {

	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		if (e1.getX() - e2.getX() > verticalMinDistance && Math.abs(velocityX) > minVelocity) {

			// 向左手势
			if(tag==1){
				setChioceItem(2);
				tag = 2;
			}
//			Toast.makeText(this, "向左手势", Toast.LENGTH_SHORT).show();
		} else if (e2.getX() - e1.getX() > verticalMinDistance && Math.abs(velocityX) > minVelocity) {

			// 向右手势
			if(tag==2){
				setChioceItem(1);
				tag = 1;
			}
//			Toast.makeText(this, "向右手势", Toast.LENGTH_SHORT).show();
		}

		return false;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return mGestureDetector.onTouchEvent(event);
	}

	public void setChioceItem(int index)
	{
		FragmentTransaction transaction = fManager.beginTransaction();
		clearChoice();
		hideFragments(transaction);
		switch (index) {
		case 1:
			found_image.setImageResource(R.drawable.ic_tabbar_found_pressed);  
			found_text.setTextColor(blue);
			found_layout.setBackgroundResource(R.drawable.ic_tabbar_bg_click);
            if (fg1 == null) {
                // 如果fg1为空，则创建一个并添加到界面上
                fg1 = new Fragment1();
                transaction.add(R.id.content, fg1);
            } else {  
                // 如果MessageFragment不为空，则直接将它显示出来  
                transaction.show(fg1);
            }  
            break;      
		
		 case 2:
			settings_image.setImageResource(R.drawable.ic_tabbar_settings_pressed);
			settings_text.setTextColor(blue);
			settings_layout.setBackgroundResource(R.drawable.ic_tabbar_bg_click);
            if (fg2 == null) {
                // 如果fg2为空，则创建一个并添加到界面上
                fg2 = new Fragment2();
                transaction.add(R.id.content, fg2);
            } else {  
                // 如果MessageFragment不为空，则直接将它显示出来  
                transaction.show(fg2);
            }  
            break;                 
		}
		transaction.commit();
	}
	
	//隐藏所有的Fragment,避免fragment混乱
	private void hideFragments(FragmentTransaction transaction) {  
        if (fg1 != null) {
            transaction.hide(fg1);
        }  
        if (fg2 != null) {
            transaction.hide(fg2);
        }  
    }  
		
	
	//定义一个重置所有选项的方法
	public void clearChoice()
	{
		found_image.setImageResource(R.drawable.ic_tabbar_found_normal);
		found_layout.setBackgroundColor(whirt);
		found_text.setTextColor(gray);
		settings_image.setImageResource(R.drawable.ic_tabbar_settings_normal);
		settings_layout.setBackgroundColor(whirt);
		settings_text.setTextColor(gray);
	}

}
