package com.jni;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;

/**
 * Created by wangwei on 2019/5/28.
 */

public class CompresActivity extends AppCompatActivity {
    private Button choose_image, upload;
    private ImageView image;

    private static final String IMAGE_FILE_NAME = "temp_head_image.jpg";
    private static final int CODE_GALLERY_REQUEST = 0;// 相册选图标记
    private static final int CODE_CAMERA_REQUEST = 1;   // 相机拍照标记
    private static final int CODE_RESULT_REQUEST = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_compress);
        choose_image = findViewById(R.id.choose_image);
        upload = findViewById(R.id.upload);
        image = findViewById(R.id.image);
        setListener();

    }


    private void setListener() {
        choose_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (PermissionUtils.checkPermission(CompresActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    goPhotoAlbum();
                } else {
                    PermissionUtils.requestPermission(CompresActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE, 100);
                }

            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
// 做优化  第一个decodeFile有可能会内存移除
                // 一般后台会规定尺寸  800  小米 规定了宽度 720
                // 上传的时候可能会多张 for循环 最好用线程池 （2-3）

            }
        });
    }


    //激活相册操作
    private void goPhotoAlbum() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        //设置文件类型  如果限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
        intent.setType("image/*");
        startActivityForResult(intent, CODE_GALLERY_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CODE_GALLERY_REQUEST:
                    if (data != null) {
                        String path = getPhotoFromPhotoAlbum.getRealPathFromUri(this, data.getData());
                        File file = new File(path);

                        Bitmap bitmap = ImageUtil.decodeFile(path);
                        // 调用写好的native方法
                        // 用Bitmap.compress压缩1/10
                        ImageUtil.compressBitmap(bitmap, 75,
                                Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator +
                                        new File(path).getName()
                        );



                    }

                    break;
            }
        }
    }


}
