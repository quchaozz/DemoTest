package com.demo.takephone;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

/**
 * Created by quchao on 2016/7/30 0030.
 * 拍照或者选择相册相片页面
 */
public class TakePicture extends Activity implements View.OnClickListener {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_PICTURE = 101;

    private TextView takePictrue;
    private TextView choosePictrue;
    private TextView cancle;

    private static final String SAVED_IMAGE_DIR_PATH = "xx";
    private String capturePath = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_picture);

        takePictrue = (TextView) findViewById(R.id.take_picture);
        choosePictrue = (TextView) findViewById(R.id.choose_picture);
        cancle = (TextView) findViewById(R.id.cancel);

        takePictrue.setOnClickListener(this);
        choosePictrue.setOnClickListener(this);
        cancle.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.take_picture:
                takePhoto();
                break;
            case R.id.choose_picture:
                selectImg();
                break;
            case R.id.cancel:
                finish();
                break;
        }
    }

    /**
     * 调用系统相机
     */
    private void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }

    }


    private void selectImg() {
        Intent picture = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(picture, REQUEST_PICTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PICTURE) {
            if (data != null) {
                Uri selectedImage = data.getData();
                String[] filePathColumns = {MediaStore.Images.Media.DATA};
                Cursor c = this.getContentResolver().query(selectedImage, filePathColumns, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePathColumns[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Intent intent = new Intent();
                intent.putExtra("url", picturePath);
                setResult(3, intent);
                finish();


                return;
            }
        } else if (requestCode == this.REQUEST_IMAGE_CAPTURE) {
            if (data != null) {
                //拍照后未保存data为null
                if (data.getExtras() != null) {
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    String picturePath = BitmapUtils.saveImageToGallery(this, imageBitmap);
                    Intent intent = new Intent();
                    intent.putExtra("url", picturePath);
                    setResult(3, intent);
                    finish();
                    return;
                }

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
