package com.wxxiaomi.ming.bicyclewebmodule;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.BridgeWebViewClient;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.wxxiaomi.ming.bicyclewebmodule.support.aliyun.OssEngine;
import com.wxxiaomi.ming.bicyclewebmodule.ui.base.BaseWebActivity;
import com.wxxiaomi.ming.bicyclewebmodule.support.cache.DiskCache;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import rx.functions.Action1;

/**
 * 使用diskLruCache+base64编码来展示图片demo
 */
public class DiskCache_Base64_Activity extends BaseWebActivity implements View.OnClickListener {

    private Button btn_cut_down;
    private Button btn_show;
    protected BridgeWebView mWebView;
    private ImageView imageview;
    //最开始图片的位置
    private String imgPath = "/storage/sdcard0/qr.png";
    private DiskCache cache;
    private Button btn_upload;

    private List<byte[]> imgList = new ArrayList<>();


    @Override
    protected int initViewAndReutrnWebViewId(Bundle savedInstanceState) {
        setContentView(R.layout.activity_disk_cache__base64_);
        btn_cut_down = (Button) findViewById(R.id.btn_cut_down);
        btn_show = (Button) findViewById(R.id.btn_show);
        mWebView = (BridgeWebView) findViewById(R.id.web_view);
        mWebView.setWebViewClient(new MyWebViewClient(mWebView));
        btn_cut_down.setOnClickListener(this);
        btn_show.setOnClickListener(this);
        imageview = (ImageView) findViewById(R.id.imageview);
        btn_upload = (Button) findViewById(R.id.btn_upload);
        btn_upload.setOnClickListener(this);
        return R.id.web_view;
    }

    @Override
    protected void initWebView() {
        mWebView.loadUrl("file:///android_asset/base64.html");
        mWebView.registerHandler("getUserId", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                int id = 25;
                Log.i("wang", "native->getUserId");
                function.onCallBack(id + "");
            }
        });
    }

    @Override
    protected void initToolBar(String data) {
//        if (cache == null) {
//            cache = new DiskCache(getApplicationContext());
//            cache.open();
//        }
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_cut_down:
                //压缩并保存

//                cache.savaImg2(imgPath)
//                        .subscribe(new Action1<Boolean>() {
//                            @Override
//                            public void call(Boolean aBoolean) {
//                                Log.i("wang", "保存的结果：" + aBoolean);
//                            }
//                        });
                break;
            case R.id.btn_show:
                Log.i("wang", "btn_show is click");
                //显示图片到webview中
//                cache.getImg(imgPath)
//                        .flatMap(new Func1<byte[], Observable<String>>() {
//                            @Override
//                            public Observable<String> call(byte[] bytes) {
//                                imgList.clear();
//                                imgList.add(bytes);
//                                String imgageBase64 = Base64.encodeToString(bytes, Base64.DEFAULT);
//                                return Observable.just(imgageBase64);
//                            }
//                        })

//                        .flatMap(new Func1<Bitmap, Observable<String>>() {
//                            @Override
//                            public Observable<String> call(Bitmap bitmap) {
//
//                                //imageview.setImageBitmap(bitmap);
//                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
//                                byte[] byteArray = byteArrayOutputStream.toByteArray();
//                                String imgageBase64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
//                                return Observable.just(imgageBase64);
//                            }
//                        })
//                        .subscribe(new Action1<String>() {
//                            @Override
//                            public void call(String s) {
//                                Pattern p = Pattern.compile("\\s*|\t|\r|\n");
//                                Matcher m = p.matcher(s);
//                                s = m.replaceAll("");
//                                Log.i("wang","要传给h5的数据："+s);
//                                String result = "{\"paths\":[";
//                                for(int i=0;i<5;i++){
//                                    if(i==0)
//                                        result += "\""+s+"\"";
//                                    else
//                                        result += ",\""+s+"\"";
//                                }
//                                result +="]}";
//                                sendToH5(result);
//                            }
//                        });
                break;
            case R.id.btn_upload:
                upLoad();
                break;
        }
    }

    private void upLoad() {
        UUID uuid = UUID.randomUUID();
        String fileName =uuid+".jpg";
        OssEngine.getInstance().initOssEngine(this);
        OssEngine.getInstance().upLoadObj(fileName,imgList.get(0))
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.i("wang","上传后的结果："+s);
                    }
                });
    }

    private void sendToH5(String base64) {
        try {
            String img = "<img src=\""+base64+"\" height=\"70\" width=\"70\"/>";
//            base64 = base64.replace("\n")

            mWebView.callHandler("myHere", base64, null);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 自定义的WebViewClient
     */
    protected class MyWebViewClient extends BridgeWebViewClient {
        public MyWebViewClient(BridgeWebView webView) {
            super(webView);
        }
    }
}
