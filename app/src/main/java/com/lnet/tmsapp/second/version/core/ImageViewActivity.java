package com.lnet.tmsapp.second.version.core;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lnet.tmsapp.R;

/**
 * Created by Administrator on 2015/8/18.
 */
public class ImageViewActivity extends FragmentActivity {
    ImageView imageView;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_view);
        imageView = (ImageView)findViewById(R.id.img);
        linearLayout = (LinearLayout)findViewById(R.id.image_linear);
        Bundle bundle = this.getIntent().getExtras();
        Bitmap bitmap = bundle.getParcelable("bitmap");
        imageView.setImageBitmap(bitmap);
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        linearLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                finish();
                return true;
            }
        });
    }
}
