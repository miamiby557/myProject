package com.lnet.tmsapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.googlecode.tesseract.android.TessBaseAPI;
import com.lnet.tmsapp.util.ImgPretreatmentHelper;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2015/7/23.
 */
public class ORCActivity extends Activity {

    private Button back;

    private static final int PHOTO_CAPTURE = 0x11;// 拍照
    private static final int PHOTO_RESULT = 0x12;// 结果
    private static TextView tvResult;
    private static ImageView ivSelected;
    private static Button btnCamera;
    private static RadioGroup langRadioGroup;
    private static String textResult;
    private static Bitmap bitmapSelected;
    private static Bitmap bitmapTreated;
    private static final int SHOWRESULT = 0x101;
    private static final int SHOWTREATEDIMG = 0x102;

    private static String LANGUAGE = "eng";
    private static String IMG_PATH = getSDPath() + java.io.File.separator
            + "ocrtest";


    // 该handler用于处理修改结果的任务
    public static Handler myHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOWRESULT:
                    if (textResult.equals(""))
                        tvResult.setText("识别失败");
                    else
                        tvResult.setText(textResult);
                    break;
                case SHOWTREATEDIMG:
                    tvResult.setText("识别中......");
                    break;
            }
            super.handleMessage(msg);
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orc_main);
        tvResult = (TextView) findViewById(R.id.result_text);
        ivSelected = (ImageView) findViewById(R.id.iv_selected);
        btnCamera = (Button) findViewById(R.id.btn_camera);
        langRadioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        back = (Button)findViewById(R.id.orc_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnCamera.setOnClickListener(new cameraButtonListener());

        // 用于设置解析语言
        langRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_en:
                        LANGUAGE = "eng";
                        break;
                    case R.id.rb_ch:
                        LANGUAGE = "chi_sim";
                        break;
                }
            }

        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_CANCELED)
            return;

        if (requestCode == PHOTO_CAPTURE) {
            tvResult.setText("abc");
            startPhotoCrop(Uri.fromFile(new File(IMG_PATH, "temp.jpg")));
        }

        // 处理结果
        if (requestCode == PHOTO_RESULT) {
            bitmapSelected = decodeUriAsBitmap(Uri.fromFile(new File(IMG_PATH,
                    "temp_cropped.jpg")));
                tvResult.setText("识别中......");
            // 显示选择的图片
            showPicture(ivSelected, bitmapSelected);

            // 新线程来处理识别
            new Thread(new Runnable() {
                @Override
                public void run() {
                    bitmapTreated = ImgPretreatmentHelper
                            .doPretreatment(bitmapSelected);
                    Message msg = new Message();
                    msg.what = SHOWTREATEDIMG;
                    myHandler.sendMessage(msg);
                    textResult = doOcr(bitmapTreated, LANGUAGE);
                }
            }).start();

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    // 拍照识别
    class cameraButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View arg0) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(new File(IMG_PATH, "temp.jpg")));
            startActivityForResult(intent, PHOTO_CAPTURE);
        }
    };

    // 将图片显示在view中
    public static void showPicture(ImageView iv, Bitmap bmp){
        iv.setImageBitmap(bmp);
    }


    //文字识别
    public String doOcr(Bitmap bitmap, String language) {
        TessBaseAPI baseApi = new TessBaseAPI();

        baseApi.init(getSDPath(), language);

        // 必须加此行，tess-two要求BMP必须为此配置
        bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

        baseApi.setImage(bitmap);

        String text = baseApi.getUTF8Text();

        baseApi.clear();
        baseApi.end();

        return text;
    }

    /**
     * 调用系统图片编辑进行裁剪
     */
    public void startPhotoCrop(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(new File(IMG_PATH, "temp_cropped.jpg")));
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent, PHOTO_RESULT);
    }
    /**
     * 获取sd卡的路径
     *
     * @return 路径的字符串
     */
    public static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();// 获取外存目录
        }
        return sdDir.toString();
    }
    //根据URL获取位图
    private Bitmap decodeUriAsBitmap(Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(getContentResolver()
                    .openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

    public File bitmap2File(Bitmap bitmap){
        File file=new File(IMG_PATH,
                "temp_transfer.jpg");//将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
