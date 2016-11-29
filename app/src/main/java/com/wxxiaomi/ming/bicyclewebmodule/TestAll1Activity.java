package com.wxxiaomi.ming.bicyclewebmodule;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.wxxiaomi.ming.bicyclewebmodule.support.cache.DiskCache;
import com.wxxiaomi.ming.bicyclewebmodule.support.picture.PhotoTakeUtil;
import com.wxxiaomi.ming.bicyclewebmodule.ui.base.BaseWebActivity;
import com.wxxiaomi.ming.bicyclewebmodule.util.ImageUtil;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * 测试的流程：
 * 1.从本地取得图片
 * 2.检查缓存中是否存在，存在则返回
 * 3.不存在，则进行压缩，然后缓存后再返回
 * 4.得到返回的数据后，展示一张在imageview中，展示一张在webview中
 */
public class TestAll1Activity extends BaseWebActivity {

    private Button btn_take;
    private PhotoTakeUtil takeUtil;

    @Override
    protected int initViewAndReutrnWebViewId(Bundle savedInstanceState) {
        setContentView(R.layout.activity_test_all1);
        btn_take = (Button) findViewById(R.id.btn_take);
        btn_take.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pictureTake();
            }
        });
        return R.id.web_view1;
    }

    private void pictureTake() {
        takeUtil.takePhoto().subscribe(new Action1<List<String>>() {
            @Override
            public void call(List<String> strings) {
                afterTakePhoto(strings);
            }
        });

//        final Observable<byte[]> doCache = ImageUtil.compressImg("", 300, 300);
////        takeUtil.takePhoto().zipWith(diskCache,doCache, new Func2<byte[], List<String>, String>() {
////            @Override
////            public String call(byte[] bytes, List<String> strings) {
////                return null;
////            }
////        });
//
////                .flatMap(new Func1<byte[], Observable<byte[]>>() {
////                    @Override
////                    public Observable<byte[]> call(final byte[] bytes) {
////                        Log.i("wang", "(3)压缩完成：");
////                        return DiskCache.getInstance().toDiskCache("", bytes)
////                                .flatMap(new Func1<Boolean, Observable<byte[]>>() {
////                                    @Override
////                                    public Observable<byte[]> call(Boolean aBoolean) {
////                                        Log.i("wang", "(4)存储完成aBoolean：" + aBoolean);
////                                        return Observable.just(bytes);
////                                    }
////                                });
////
////                    }
////                });
//        //取得照片
//        takeUtil.takePhoto()
//
//                .flatMap(new Func1<List<String>, Observable<byte[]>>() {
//                    @Override
//                    public Observable<byte[]> call(final List<String> list) {
//                        Log.i("wang", "(1)从本地取得图片地址：" + list.get(0));
//                        return DiskCache.getInstance().getDiskCache(list.get(0))
//                                .flatMap(new Func1<byte[], Observable<byte[]>>() {
//                                    @Override
//                                    public Observable<byte[]> call(final byte[] bytes) {
//                                        if (bytes == null) {
//                                            Log.i("wang", "(2)从本地取不到缓存：");
//                                            return doCache;
//                                        } else {
//                                            Log.i("wang", "(2)从本地去得到缓存：");
//                                            return Observable.just(bytes);
//                                        }
//
//                                    }
//                                });
//                    }
//                })
////                .flatMap(new Func1<byte[], Observable<byte[]>>() {
////            @Override
////            public Observable<byte[]> call(final byte[] bytes) {
////                UUID uuid = UUID.randomUUID();
////                String fileName = uuid + ".jpg";
////                return OssEngine.getInstance().upLoadObj(fileName, bytes)
////                        .flatMap(new Func1<String, Observable<byte[]>>() {
////                            @Override
////                            public Observable<byte[]> call(String s) {
////                                Log.i("wang", "（5）上传完成result:" + s);
////                                return Observable.just(bytes);
////                            }
////                        });
////            }
////        })
//                .subscribe(new Action1<byte[]>() {
//
//                    @Override
//                    public void call(byte[] bytes) {
//                        String result = Base64.encodeToString(bytes, Base64.DEFAULT);
//                        Pattern p = Pattern.compile("\\s*|\t|\r|\n");
//                        Matcher m = p.matcher(result);
//                        result = m.replaceAll("");
//                        String hh = "{\"paths\":[";
//                        for (int i = 0; i < 5; i++) {
//                            if (i == 0)
//                                hh += "\"" + result + "\"";
//                            else
//                                hh += ",\"" + result + "\"";
//                        }
//                        hh += "]}";
//                        handlerH5Img(hh);
//                    }
//                });


    }

    private void afterTakePhoto(final List<String> pathList) {
//        Observable<byte[]> diskCache = DiskCache.getInstance().getDiskCache(pathList.get(0));
//        Observable<byte[]> doCache = ImageUtil.compressImg(pathList.get(0), 300, 300)
//                .flatMap(new Func1<byte[], Observable<byte[]>>() {
//                    @Override
//                    public Observable<byte[]> call(final byte[] bytes) {
//                        //存入缓存
//                        return DiskCache.getInstance().toDiskCache(pathList.get(0), bytes)
//                                .map(new Func1<Boolean, byte[]>() {
//                                    @Override
//                                    public byte[] call(Boolean aBoolean) {
//                                        return bytes;
//                                    }
//                                });
//                    }
//                });

//        Observable.concat(diskCache,doCache)
//                .first(new Func1<byte[], Boolean>() {
//                    @Override
//                    public Boolean call(byte[] bytes) {
//                        return bytes != null;
//                    }
//                })
//                .subscribe(new Action1<byte[]>() {
//                    @Override
//                    public void call(byte[] bytes) {
//                        String result = Base64.encodeToString(bytes, Base64.DEFAULT);
//                        Pattern p = Pattern.compile("\\s*|\t|\r|\n");
//                        Matcher m = p.matcher(result);
//                        result = m.replaceAll("");
//                        String hh = "{\"paths\":[";
//                        for (int i = 0; i < 5; i++) {
//                            if (i == 0)
//                                hh += "\"" + result + "\"";
//                            else
//                                hh += ",\"" + result + "\"";
//                        }
//                        hh += "]}";
//                        handlerH5Img(hh);
//                    }
//                });


    }

    private void handlerH5Img(String result) {
        mWebView.callHandler("myHere", result, null);
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
        takeUtil = new PhotoTakeUtil(this);
    }

    @Override
    protected void initToolBar(String data) {

    }
}
