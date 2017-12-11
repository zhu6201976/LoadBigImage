package com.example.administrator.test;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * 加载一张大图
 * 2017年12月11日15:40:49
 */

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ImageView iv;
    private int mScreenWidth;
    private int mScreenHeight;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iv = (ImageView) findViewById(R.id.iv);

        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        mScreenWidth = wm.getDefaultDisplay().getWidth();
        mScreenHeight = wm.getDefaultDisplay().getHeight();
        Log.d(TAG, "onCreate: mScreenWidth:" + mScreenWidth + ",mScreenHeight:" + mScreenHeight);

    }

    // 点击按钮,加载一张大图
    public void loadBigImage(View view) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;// 仅仅解析大小
        BitmapFactory.decodeResource(this.getResources(), R.drawable.dog, options);
        int imageWidth = options.outWidth;
        int imageHeight = options.outHeight;
        Log.d(TAG, "loadBigImage: imageWidth:" + imageWidth + ",imageHeight:" + imageHeight);

        int scale = 1;
        int scaleX = imageWidth / mScreenWidth;
        int scaleY = imageHeight / mScreenHeight;
        if (scaleX > scaleY && scaleX > scale) {
            scale = scaleX;
        } else if (scaleY > scaleX && scaleY > scale) {
            scale = scaleY;
        }

        options.inJustDecodeBounds = false;// 开始真正解析图片
        options.inSampleSize = scale;// 设置缩放比
        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.dog, options);
        iv.setImageBitmap(bitmap);

        // 保存图片到私有目录
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(this.getFilesDir() + "/dog.jpg");
            boolean compress = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            if (compress) {
                Toast.makeText(this, "文件成功保存到私有目录", Toast.LENGTH_SHORT).show();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
