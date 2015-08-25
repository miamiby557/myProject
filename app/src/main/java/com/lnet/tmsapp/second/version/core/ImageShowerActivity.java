package com.lnet.tmsapp.second.version.core;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.lnet.tmsapp.R;

/**
 * Created by Administrator on 2015/8/24.
 */
public class ImageShowerActivity extends Activity{

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imageshower);
        imageView = (ImageView)findViewById(R.id.image);
        Bundle bundle = getIntent().getExtras();
        String uri = bundle.getString("uri", "");
        BitmapFactory.Options options1 = new BitmapFactory.Options();
        options1.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(uri, options1);
        options1.inSampleSize = calculateInSampleSize(options1, 650, 1000);  //转换后的宽和高，具体值会有些出入
        options1.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(uri, options1);
        imageView.setImageBitmap(bitmap);

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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return true;
    }
}
