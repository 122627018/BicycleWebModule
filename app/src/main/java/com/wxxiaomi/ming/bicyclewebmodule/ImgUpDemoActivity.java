package com.wxxiaomi.ming.bicyclewebmodule;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.squareup.leakcanary.RefWatcher;
import com.wxxiaomi.ming.bicyclewebmodule.support.aliyun.OssEngine;
import com.wxxiaomi.ming.bicyclewebmodule.support.aliyun.OssService;
import com.wxxiaomi.ming.bicyclewebmodule.ui_refactor.Demo;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import rx.Observer;
import rx.functions.Action1;

/**
 * 使用oss来上传图片demo
 */
public class ImgUpDemoActivity extends AppCompatActivity {
    private EditText et_img_path;
    private Button btn_img;
    private ImageView imageView;
    private String imgPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_up_demo);

        btn_img = (Button) findViewById(R.id.btn_img);
        et_img_path = (EditText) findViewById(R.id.et_img_path);
        Demo.getInstance(this).demo(et_img_path);
//        btn_img.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                Map<String,String> pars = new HashMap<>();
////                for(int i=0;i<5;i++){
////                    UUID uuid = UUID.randomUUID();
////                    String fileName =uuid+".jpg";
////                    pars.put(fileName,imgPath);
////                }
////                OssEngine.getInstance().upLoadImages(pars)
////                        .subscribe(new Observer<List<String>>() {
////                            @Override
////                            public void onCompleted() {
////
////                            }
////
////                            @Override
////                            public void onError(Throwable e) {
////
////                            }
////
////                            @Override
////                            public void onNext(List<String> results) {
////                                for(int i=0;i<results.size();i++){
////                                    Log.i("wang","results:"+results.get(i));
////                                }
////                            }
////                        });
//                UUID uuid = UUID.randomUUID();
//                    String fileName =uuid+".jpg";
//
//                OssEngine.getInstance().uploadImage(fileName,imgPath)
//                        .subscribe(new Action1<String>() {
//                            @Override
//                            public void call(String s) {
//                                Log.i("wang","results:"+s);
//                            }
//                        });
//            }
//        });
//        imageView = (ImageView) findViewById(R.id.imageview);
//        String path = et_img_path.getText().toString().trim();
//        imgPath = path;
//        try {
//            FileInputStream fis = new FileInputStream(path);
//            Bitmap bitmap = BitmapFactory.decodeStream(fis);
//            imageView.setImageBitmap(bitmap);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        OssEngine.getInstance().initOssEngine(this);
    }

    @Override
    protected void onDestroy() {
        //director = null;
        super.onDestroy();
       // Log.i("wang","onDestroy");
        RefWatcher refWatcher = MyApplication.sRefWatcher;
        refWatcher.watch(this);
    }
}
