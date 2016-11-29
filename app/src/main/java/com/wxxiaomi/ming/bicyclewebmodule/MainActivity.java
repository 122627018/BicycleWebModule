package com.wxxiaomi.ming.bicyclewebmodule;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.wxxiaomi.ming.bicyclewebmodule.support.aliyun.OssEngine;
import com.wxxiaomi.ming.bicyclewebmodule.support.aliyun.OssService;
import com.wxxiaomi.ming.bicyclewebmodule.support.aliyun.STSGetter;
import com.wxxiaomi.ming.bicyclewebmodule.ui.SimpleWebActivity;
import com.wxxiaomi.ming.bicyclewebmodule.ui.SimpleWebActivity2;
import com.wxxiaomi.ming.bicyclewebmodule.ui.SimpleWebActivity3;

public class MainActivity extends AppCompatActivity {

    private Button btn;
    private Button btn_img_upload;
    private Button btn_take_photo;
    private Button btn_cache_base64;

    private Button btn_test1;
    private Button btn_rx;

    private Button btn_new_webview1;

    private Button btn_go_test_page;
    private Button btn_oom_test;
    private Button btn_new_thread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runWebAct();
            }
        });
        btn_img_upload = (Button) findViewById(R.id.btn_img_upload);
        btn_img_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runImpUpAct();
            }
        });
        btn_take_photo = (Button) findViewById(R.id.btn_take_photo);
        btn_take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runTakePhoAct();
            }
        });

        btn_cache_base64 = (Button) findViewById(R.id.btn_cache_base64);
        btn_cache_base64.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runBase64Act();
            }
        });

        btn_test1 = (Button) findViewById(R.id.btn_test1);
        btn_test1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runTest1();
            }
        });
        btn_rx = (Button) findViewById(R.id.btn_rx);
        btn_rx.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                runRxAct();
            }
        });

        btn_new_webview1 = (Button) findViewById(R.id.btn_new_webview1);
        btn_new_webview1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runNew1();
            }
        });

        btn_go_test_page = (Button) findViewById(R.id.btn_go_test_page);
        btn_go_test_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goTestPage1();
            }
        });

        btn_oom_test = (Button) findViewById(R.id.btn_oom_test);
        btn_oom_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runOOMTest();
            }
        });
        btn_new_thread = (Button) findViewById(R.id.btn_new_thread);
        btn_new_thread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runNewThreadWebView();
            }
        });
    }

    private void runNewThreadWebView() {
        Intent intent = new Intent(this, SimpleWebActivity3.class);
        intent.putExtra("url",ConstantValue.SERVER_URL+"/app/topicList_1.html");
        startActivity(intent);
    }

    private void runOOMTest() {
        Intent intent = new Intent(this, OOMTestActivity.class);
        intent.putExtra("url",ConstantValue.SERVER_URL+"/app/topic_publish2.html");
        startActivity(intent);
    }

    private void goTestPage1() {
        Intent intent = new Intent(this, SimpleWebActivity2.class);
        intent.putExtra("url",ConstantValue.SERVER_URL+"/app/topic_publish2.html");
        startActivity(intent);
    }

    private void runNew1() {
        Intent intent = new Intent(this, WebViewNewTry.class);
        startActivity(intent);
    }

    private void runRxAct() {
        Intent intent = new Intent(this, RxActivity.class);
        startActivity(intent);
    }

    private void runTest1() {
        Intent intent = new Intent(this, TestAll1Activity.class);
        startActivity(intent);
    }

    private void runBase64Act() {
        Intent intent = new Intent(this, DiskCache_Base64_Activity.class);
        startActivity(intent);
    }

    private void runTakePhoAct() {
        Intent intent = new Intent(this, TakePhotoActivity.class);
        startActivity(intent);
    }

    private void runImpUpAct() {
        Intent intent = new Intent(this, ImgUpDemoActivity.class);
        startActivity(intent);
    }

    public void runWebAct(){
        Intent intent = new Intent(this, SimpleWebActivity2.class);
        intent.putExtra("url",ConstantValue.SERVER_URL+"/app/topicList_1.html");
        startActivity(intent);
    }

//https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2033765348,1346395611&fm=116&gp=0.jpg
    //<img src="url('file:///storage/sdcard0/DCIM/Camera/IMG_20161123_152042.jpg')"/>
}
