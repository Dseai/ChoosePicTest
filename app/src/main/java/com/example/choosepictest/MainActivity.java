package com.example.choosepictest;

import android.app.Activity;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.*;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.PublicKey;

public class MainActivity extends Activity{
    public static final int TAKE_PHOTO=1;
    public static final int CROP_PHOTO=2;
    private Button takePhoto;
    private ImageView picture;
    private Uri imageUri;
    private Button chooseFromAlbum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        takePhoto = (Button) findViewById(R.id.take_photo);
        picture = (ImageView) findViewById(R.id.picture);
        chooseFromAlbum=(Button)findViewById(R.id.choose_from_album);
        chooseFromAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File outputImage = new File(Environment.getExternalStorageDirectory(), "SYN.jpg");
                try {
                    if (outputImage.exists()) {
                        outputImage.delete();
                    }
                    outputImage.createNewFile();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                imageUri = Uri.fromFile(outputImage);
                Intent intent = new Intent("android.media.action.GET_CONTENT");
                intent.setType("image/*");
                intent.putExtra("crop",true);
                intent.putExtra("scale",true);
                intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                startActivityForResult(intent,CROP_PHOTO);


            }
        });
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Environment.getExternalStorageDirectory()获得SD卡的根目录
                File outputImage = new File(Environment.getExternalStorageDirectory(), "SYN.jpg");
                try {
                    if (outputImage.exists()) {
                        outputImage.delete();
                    }
                    outputImage.createNewFile();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                //调用fromFile方法将file对象转换为Uri对象 指定唯一地址
                imageUri = Uri.fromFile(outputImage);
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, TAKE_PHOTO);
            }

        });
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        switch (requestCode){
            case TAKE_PHOTO:
                if(requestCode==RESULT_OK){
                    Intent intent=new Intent("com.android.camera.action.CROP");
                    intent.setDataAndType(imageUri,"image/*");
                    intent.putExtra("scale",true);
                    //指定输出地址
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                    //拍照完会返回一个result到onActivityResult()方法中
                    // 如果发现拍照成功 会再次构造一个Intent对象并把action指定为com.android.camera.action.CROP剪裁
                    startActivityForResult(intent,CROP_PHOTO);
                }
                break;
            case CROP_PHOTO:
                if(requestCode==RESULT_OK){
                    try {
                        Bitmap bitmap= BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        picture.setImageBitmap(bitmap);
                    }catch (FileNotFoundException e){
                        e.printStackTrace();
                    }
                }break;
            default:break;
        }
    }


}
