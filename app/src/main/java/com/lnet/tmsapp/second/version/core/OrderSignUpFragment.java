package com.lnet.tmsapp.second.version.core;

/**
 * Created by Andy on 2015/5/28.
 */
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.lnet.tmsapp.R;
import com.lnet.tmsapp.application.ApplicationTrans;
import com.lnet.tmsapp.model.OtdCarrierOrderDetail;
import com.lnet.tmsapp.model.OtdOrderSign;
import com.lnet.tmsapp.model.ServiceResult;
import com.lnet.tmsapp.util.DataItem;
import com.lnet.tmsapp.util.GsonRequest;
import com.lnet.tmsapp.util.JsonHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;


public class OrderSignUpFragment extends Fragment {

    View rootView;

    Button pickUpPic;
    TableLayout table;
    EditText orderNumber;
    List<String> numbers = new ArrayList<>();
    EditText signUpPerson;
    EditText remark;
    ScrollView scrollView;
    private String capturePath = null;
    SharedPreferences mySharedPreferences;
    ApplicationTrans application;
    RequestQueue requestQueue;
    List<Bitmap> bitmaps = new ArrayList<>();
    OtdOrderSign orderSign = new OtdOrderSign();
    TableLayout picTable;
    Map<Integer,String> photoUris = new HashMap<>();
    int picNum = 1;

    private final static String ALBUM_PATH
            = Environment.getExternalStorageDirectory() + "/pic/";

    public OrderSignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main2, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_create:
                create();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void create(){
        new AlertDialog.Builder(getActivity()).setTitle("签收订单").setMessage("确认签收？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        saveOrderSignIn();
                    }})
                .setNegativeButton("取消",null)
                .show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.order_sign_up, container, false);
        application = (ApplicationTrans)getActivity().getApplication();
        mySharedPreferences = getActivity().getSharedPreferences(application.getFILENAME(), application.getMODE());
        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        table = (TableLayout)rootView.findViewById(R.id.add_fee_declare_table);
        orderNumber = (EditText)rootView.findViewById(R.id.number);
        orderNumber.setOnFocusChangeListener(new FocusListener());
        orderNumber.setOnTouchListener(new TouchEvent());
        signUpPerson = (EditText)rootView.findViewById(R.id.signup_person);
        remark = (EditText)rootView.findViewById(R.id.remark);
        pickUpPic = (Button)rootView.findViewById(R.id.pick_up);
        picTable = (TableLayout)rootView.findViewById(R.id.pic_table);

        scrollView = (ScrollView)rootView.findViewById(R.id.t_scrollview);
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().getApplicationContext().INPUT_METHOD_SERVICE);
                return imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
            }
        });
        pickUpPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickup();
            }
        });
        return rootView;
    }
    private class TouchEvent implements View.OnTouchListener{

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                int size = orderNumber.getRight();
                int x = (int) event.getX();
                if (x > size - 100) {
                    //扫描
                    Intent openCameraIntent = new Intent(getActivity(),CaptureActivity.class);
                    startActivityForResult(openCameraIntent, 3);
                    return true;
                }
                return false;
            }
            return false;
        }
    }

    private class FocusListener implements View.OnFocusChangeListener{

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(!hasFocus){
                String number = orderNumber.getText().toString().trim();
                if(check(number)){
                    //从数据库得到运输订单
                    String httpUrl = mySharedPreferences.getString("serviceAddress", "")+"/order/transportOrder/"+number;
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, httpUrl,
                            new Response.Listener<JSONObject>(){
                                @Override
                                public void onResponse(JSONObject response){
                                    try {
                                        Boolean isSuccess = response.getBoolean("success");
                                        if(!isSuccess){
                                            AlertDialog.Builder builder  = new AlertDialog.Builder(getActivity());
                                            builder.setTitle("提示" ) ;
                                            builder.setMessage("没有此单号!" ) ;
                                            builder.setPositiveButton("重新输入" ,  null );
                                            builder.show();
                                        }else{

                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            }
                    );
                    requestQueue.add(request);
                }
            }
        }
    }

    private void saveOrderSignIn(){
        if(numbers.contains(orderNumber.getText().toString().trim())){
            showToast("此单号已签收!");
            return;
        }
        if(!check(orderNumber.getText().toString().trim())){
            showToast("输入签收单号!");
            return;
        }

        if(!check(signUpPerson.getText().toString().trim())){
            showToast("输入签收人!");
            return;
        }
        final ProgressDialog loading = ProgressDialog.show(getActivity(), "提示", "请等待几秒钟...");
        Gson gson = JsonHelper.getGson();
        List<String> photoStrings = new ArrayList<>();
        for(Bitmap bitmap:bitmaps){
            String photo = bitMapToString(bitmap);
            if(photo!=null){
                photoStrings.add(photo);
            }
        }
        orderSign.setPhotoStrings(photoStrings);
        orderSign.setTransportOrderNumber(orderNumber.getText().toString().trim());
        orderSign.setSignMan(signUpPerson.getText().toString().trim());
        orderSign.setRemark(remark.getText().toString().trim());
        String userId = application.getUserId();
        orderSign.setCreateUserId(UUID.fromString(userId));
        String json = gson.toJson(orderSign);
        String httpUrl = mySharedPreferences.getString("serviceAddress", "") + "/order/orderSignUp";
        GsonRequest<ServiceResult> gsonRequest = new GsonRequest(httpUrl, ServiceResult.class, json, new Response.Listener<ServiceResult>() {
            @Override
            public void onResponse(ServiceResult response) {

                if(loading!=null && loading.isShowing()){
                   loading.dismiss();
                }
                if(response.isSuccess()){
                    showToast("签收成功!");
                    numbers.add(orderNumber.getText().toString().trim());
                }else {
                    showToast("签收失败!");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(loading!=null && loading.isShowing()){
                    loading.dismiss();
                }
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map localHashMap = new HashMap();
                localHashMap.put("Cookie", application.getCookie());
                return localHashMap;
            }
        };
        requestQueue.add(gsonRequest);
    }

    //拍照图片
    private void pickup(){
        Intent getImageByCamera = new Intent("android.media.action.IMAGE_CAPTURE");
        String out_file_path = ALBUM_PATH;
        File dir = new File(out_file_path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        capturePath = ALBUM_PATH + System.currentTimeMillis() + ".jpg";
        getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(capturePath)));
        getImageByCamera.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        photoUris.put(picNum, capturePath);
        startActivityForResult(getImageByCamera, 2);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 2:
                if(capturePath.length()!=0){
                    BitmapFactory.Options options1 = new BitmapFactory.Options();
                    options1.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(capturePath, options1);
                    options1.inSampleSize = calculateInSampleSize(options1, 300, 400);  //转换后的宽和高，具体值会有些出入
                    options1.inJustDecodeBounds = false;
                    final Bitmap bitmap = BitmapFactory.decodeFile(capturePath, options1);
                    if(bitmap!=null){
                        bitmaps.add(bitmap);
                        TableRow row = new TableRow(getActivity());
                        row.setId(picNum+0);
                        row.setPadding(0, 5, 0, 0);
                        ImageView view = new ImageView(getActivity());
                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                TableRow tRow = (TableRow)v.getParent();
                                int num = tRow.getId();
                                Intent intent = new Intent(getActivity(),ImageShowerActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("uri",photoUris.get(num));
                                intent.putExtras(bundle);
                                getActivity().startActivity(intent);

                            }
                        });
                        view.setImageBitmap(bitmap);
                        Button delete = new Button(getActivity());
                        delete.setGravity(Gravity.CENTER);
                        delete.setText("删除");
                        delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(final View v) {
                                new AlertDialog.Builder(getActivity()).setTitle("删除图片").setMessage("确认删除？")
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                TableRow tableRow = (TableRow) v.getParent();
                                                picTable.removeView(tableRow);
                                                bitmaps.remove(bitmap);
                                                if (picTable.getChildCount() > 0) {
                                                    pickUpPic.setText("再拍一张");
                                                } else {
                                                    pickUpPic.setText("拍一张");
                                                }
                                            }
                                        })
                                        .setNegativeButton("取消", null)
                                        .show();

                            }
                        });
                        row.addView(view);
                        row.addView(delete);
                        picTable.addView(row);
                        picNum++;
                        pickUpPic.setText("再拍一张");
                        showToast("图片已保存在pic文件夹下面!");
                        Handler mHandler = new Handler();
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                            }
                        });
                    }
                }
                break;
            case 3:
                if(data!=null){
                    Bundle bundle = data.getExtras();
                    String scanResult = bundle.getString("result");
                    orderNumber.setText(scanResult);
                }
                break;
            default:
                break;
        }
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {

        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            inSampleSize = heightRatio < widthRatio ? widthRatio : heightRatio;
        }

        return inSampleSize;
    }

    private String bitMapToString(Bitmap bitmap){
        if(bitmap!=null){
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
            try {
                baos.close();
                byte[] buffer = baos.toByteArray();
                String photo = Base64.encodeToString(buffer,0,buffer.length,Base64.DEFAULT);
                return photo;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }else {
            return null;
        }
    }

    private boolean check(String value){
        if(value.length()!=0){
            return true;
        }
        return false;
    }

    private void showToast(String massage){
        Toast.makeText(getActivity().getApplicationContext(), massage, Toast.LENGTH_LONG).show();
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
