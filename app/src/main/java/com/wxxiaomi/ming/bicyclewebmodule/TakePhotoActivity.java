package com.wxxiaomi.ming.bicyclewebmodule;


import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.wxxiaomi.ming.bicyclewebmodule.support.picture.PhotoTakeUtil;
import com.wxxiaomi.ming.bicyclewebmodule.util.ImageUtil;
import com.yalantis.ucrop.callback.BitmapLoadCallback;
import com.yalantis.ucrop.model.ExifInfo;
import com.yalantis.ucrop.util.BitmapLoadUtils;

import java.io.File;
import java.util.List;

import rx.Observer;
import rx.functions.Action1;

/**
 * 测试获取图片的框架demo
 */
public class TakePhotoActivity extends AppCompatActivity implements View.OnClickListener {


    private Button btn_one_no_cut;
    private Button btn_one_cut;
    private Button btn_many;
    private PhotoTakeUtil util;

    private Button btn_crop;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);
        btn_one_no_cut = (Button) findViewById(R.id.btn_one_no_cut);
        btn_one_no_cut.setOnClickListener(this);
        btn_one_cut = (Button) findViewById(R.id.btn_one_cut);
        btn_one_cut.setOnClickListener(this);
        btn_many = (Button) findViewById(R.id.btn_many);
        btn_many.setOnClickListener(this);
        btn_crop = (Button) findViewById(R.id.btn_crop);
        btn_crop.setOnClickListener(this);
        util = new PhotoTakeUtil(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_one_no_cut:
                //单选不要裁剪
                util.takePhoto().subscribe(new Action1<List<String>>() {
                    @Override
                    public void call(List<String> result) {
                        Log.i("wang","result:"+result.get(0));
                    }
                });
                break;
            case R.id.btn_one_cut:
                util.takePhotoCut()
                        .subscribe(new Action1<List<String>>() {
                            @Override
                            public void call(List<String> result) {
                                Log.i("wang","result:"+result.get(0));
                            }
                        });
                break;
            case R.id.btn_many:
                util.takePhotos().subscribe(new Observer<List<String>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<String> strings) {
                        for(String item : strings){
                            Log.i("wang","result:"+item);
                        }
                    }
                });
                break;
            case R.id.btn_crop:
                img();
                break;

        }


    }
    void img(){
        Uri imageUri = Uri.fromFile(new File("/storage/sdcard0/qr.png"));
        Uri outputUri = Uri.fromFile(new File("/Gallery/Pictures/ming.png"));
//        ImageUtil.getCacheImage("storage/sdcard0/qr.png","storage/sdcard0/Gallery/Pictures/ming.png");
//        ImageUtil.compressBitmap(this,"/storage/sdcard0/qr.png",200,200,false);
    }

}
