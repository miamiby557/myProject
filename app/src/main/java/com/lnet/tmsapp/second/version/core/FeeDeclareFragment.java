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
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Base64;
import android.view.Display;
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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.lnet.tmsapp.R;
import com.lnet.tmsapp.application.ApplicationTrans;
import com.lnet.tmsapp.model.FeeDeclare;
import com.lnet.tmsapp.model.HttpHelper;
import com.lnet.tmsapp.model.ServiceResult;
import com.lnet.tmsapp.util.DataItem;
import com.lnet.tmsapp.util.GsonRequest;
import com.lnet.tmsapp.util.JsonHelper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FeeDeclareFragment extends Fragment {

    View rootView;

    Button addFeeDeclare;
    Button selectPic;
    Button pickUpPic;
    TableLayout table;
    EditText declareNumber;
    List<String> numbers = new ArrayList<>();
    EditText remark;
    ImageView imageView;
    ScrollView scrollView;
    int count = 100;//标记table内EditText
    List<DataItem> items = new ArrayList<>();
    private String capturePath = null;
    SharedPreferences mySharedPreferences;
    ApplicationTrans application;
    RequestQueue requestQueue;
    Bitmap bitmap;
    FeeDeclare feeDeclare = new FeeDeclare();
    ProgressDialog loading;
    Display display;
    float scaleWidth;
    float scaleHeight;
    boolean num=true;

    private final static String ALBUM_PATH
            = Environment.getExternalStorageDirectory() + "/pic/";

    public FeeDeclareFragment() {
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
        new AlertDialog.Builder(getActivity()).setTitle("创建费用申报").setMessage("确认创建？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        saveFeeDeclare();
                    }})
                .setNegativeButton("取消",null)
                .show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fee_declare, container, false);
        application = (ApplicationTrans)getActivity().getApplication();
        mySharedPreferences = getActivity().getSharedPreferences(application.getFILENAME(), application.getMODE());
        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        addFeeDeclare = (Button)rootView.findViewById(R.id.add_fee_declare);
        display = getActivity().getWindowManager().getDefaultDisplay();

        selectPic = (Button)rootView.findViewById(R.id.select_pic);
        table = (TableLayout)rootView.findViewById(R.id.add_fee_declare_table);
        declareNumber = (EditText)rootView.findViewById(R.id.number);
        remark = (EditText)rootView.findViewById(R.id.remark);
        declareNumber.setOnTouchListener(new TouchEvent());
        pickUpPic = (Button)rootView.findViewById(R.id.pick_up);
        imageView = (ImageView)rootView.findViewById(R.id.imageView);

        scrollView = (ScrollView)rootView.findViewById(R.id.t_scrollview);
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().getApplicationContext().INPUT_METHOD_SERVICE);
                return imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
            }
        });
        addFeeDeclare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFeeDeclare();
            }
        });
        selectPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPic();
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
                int size = declareNumber.getRight();
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

    //增加费用科目
    private void addFeeDeclare(){
        TableRow row = new TableRow(getActivity());
        EditText feeDeclareName = new EditText(getActivity());
        feeDeclareName.setId(count++);
        feeDeclareName.setFocusable(true);
        feeDeclareName.setWidth(240);

        EditText money = new EditText(getActivity());
        money.setId(count++);
        money.setWidth(200);
        money.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        Button button = new Button(getActivity());
        button.setWidth(50);
        button.setHeight(40);
        button.setText("删除");
        button.setGravity(Gravity.CENTER);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TableRow tableRow = (TableRow) v.getParent();
                table.removeView(tableRow);
            }
        });

        row.addView(feeDeclareName);
        row.addView(money);
        row.addView(button);
        table.addView(row);
    }

    private void saveFeeDeclare(){
        if(numbers.contains(declareNumber.getText().toString().trim())){
            showToast("此订单号已申报!");
            return;
        }
        if(!check(declareNumber.getText().toString().trim())){
            showToast("输入所申报的订单号!");
            return;
        }
        items.clear();
        //遍历table，得到所有费用申报
        for(int i=100;i<count;i++){
            EditText editText = (EditText)rootView.findViewById(new Integer(i));
            if(editText!=null){
                String name = editText.getText().toString().trim();
                EditText money = (EditText)rootView.findViewById(i+1);
                DataItem item = new DataItem();
                item.setTextName(name);
                item.setTextValue(money.getText().toString().trim());
                items.add(item);
                i++;
            }else {
                i++;
                continue;
            }
        }
        if(items.size()==0){
            showToast("请添加费用科目!");
            return;
        }

        loading = ProgressDialog.show(getActivity(), "提示", "请等待几秒钟...");
        String photo = bitMapToString();
        feeDeclare.setImagesString(photo);
        feeDeclare.setFeeDeclares(items);
        feeDeclare.setRemark(remark.getText().toString().trim());
        feeDeclare.setDeclareOrderNumber(declareNumber.getText().toString().trim());
        final Gson gson = JsonHelper.getGson();
        String json = gson.toJson(feeDeclare);
        String httpUrl = mySharedPreferences.getString("serviceAddress", "") + "/order/feeDeclare";
        GsonRequest<ServiceResult> gsonRequest = new GsonRequest(httpUrl, ServiceResult.class, json, new Response.Listener<ServiceResult>() {
            @Override
            public void onResponse(ServiceResult response) {

                if(loading!=null && loading.isShowing()){
                   loading.dismiss();
                }
                if(response.isSuccess()){
                    showToast("创建成功!");
                    numbers.add(declareNumber.getText().toString().trim());
                }else {
                    showToast("创建失败!");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(loading!=null && loading.isShowing()){
                    loading.dismiss();
                }
            }
        });
        requestQueue.add(gsonRequest);
    }

    //选择图片
    private void selectPic(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");//相片类型
        startActivityForResult(intent, 1);
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
        startActivityForResult(getImageByCamera, 2);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                break;
            case 2:
                if(capturePath.length()!=0){
                    BitmapFactory.Options options1 = new BitmapFactory.Options();
                    options1.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(capturePath, options1);
                    options1.inSampleSize = calculateInSampleSize(options1, 520, 560);  //转换后的宽和高，具体值会有些出入
                    options1.inJustDecodeBounds = false;
                    bitmap = BitmapFactory.decodeFile(capturePath, options1);
                    if(bitmap!=null){
                        imageView.setImageBitmap(bitmap);
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(),ImageShowerActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("uri",capturePath);
                                intent.putExtras(bundle);
                                getActivity().startActivity(intent);
                            }
                        });
                        showToast("图片已保存在pic文件夹下面!");
                    }
                }
                break;
            case 3:
                if(data!=null){
                    Bundle bundle = data.getExtras();
                    String scanResult = bundle.getString("result");
                    declareNumber.setText(scanResult);
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

    private String bitMapToString(){
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
