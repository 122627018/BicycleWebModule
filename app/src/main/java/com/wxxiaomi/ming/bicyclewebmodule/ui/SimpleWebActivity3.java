package com.wxxiaomi.ming.bicyclewebmodule.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wxxiaomi.ming.bicyclewebmodule.R;
import com.wxxiaomi.ming.bicyclewebmodule.action.net.SendUpAction;
import com.wxxiaomi.ming.bicyclewebmodule.action.ui.UiAction;
import com.wxxiaomi.ming.bicyclewebmodule.action.ui.UiActionWithFloat;
import com.wxxiaomi.ming.bicyclewebmodule.action.ui.UiTypeAdapter;
import com.wxxiaomi.ming.bicyclewebmodule.service.WebMethods;
import com.wxxiaomi.ming.bicyclewebmodule.support.aliyun.OssEngine;
import com.wxxiaomi.ming.bicyclewebmodule.support.cache.CacheEngine;
import com.wxxiaomi.ming.bicyclewebmodule.support.picture.PhotoTakeUtil;
import com.wxxiaomi.ming.bicyclewebmodule.ui.base.BaseWebActivity3;
import com.wxxiaomi.ming.bicyclewebmodule.util.ImageUtil;
import com.wxxiaomi.ming.bicyclewebmodule.util.ParsMakeUtil;
import com.wxxiaomi.ming.bicyclewebmodule.util.ToolUtils;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by 12262 on 2016/11/12.
 * 处理只有一个webview控件的公共activity
 */
public class SimpleWebActivity3 extends BaseWebActivity3 {
    private Toolbar toolbar1;
    private Button btnRight;
    private FloatingActionButton float_Btn;
    private PhotoTakeUtil util;
    private List<byte[]> imgDatas;

    @Override
    protected int initViewAndReutrnWebViewId(Bundle savedInstanceState) {
        setContentView(R.layout.activity_simple_webview);
        toolbar1 = (Toolbar) findViewById(R.id.toolbar);
        return R.id.web_view;
    }

    @Override
    protected void initWebView() {
        String url = getIntent().getStringExtra("url");
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.loadUrl(url);
    }

    @Override
    protected void initToolBar(String data) {
        btnRight = (Button) toolbar1.findViewById(R.id.btnRight);
        float_Btn = (FloatingActionButton) findViewById(R.id.float_Btn);
        setSupportActionBar(toolbar1);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (data != "") {
            handlerUiInitEvent(data);
        }
    }

    @Override
    protected void initCommonMethod() {
        super.initCommonMethod();
        mWebView.registerHandler("sendUP", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                handlerSumbitEvent(data, function);
            }
        });
        mWebView.registerHandler("takePic", new BridgeHandler() {
            @Override
            public void handler(String data, final CallBackFunction function) {
                handlerPictureTakeEvent(data, function);
            }
        });
        mWebView.registerHandler("showSnack", new BridgeHandler() {
            @Override
            public void handler(String data, final CallBackFunction function) {
//                handlerPictureTakeEvent(data, function);
                Snackbar.make(float_Btn,data, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 处理ui初始化事件
     *
     * @param data
     */
    private void handlerUiInitEvent(String data) {
        Gson gson = new GsonBuilder().registerTypeAdapter(UiAction.class, new UiTypeAdapter()).create();
        final UiAction uiAction = gson
                .fromJson(data, UiAction.class);
        toolbar1.setTitle(uiAction.title);
        if (uiAction instanceof UiActionWithFloat) {
            final UiActionWithFloat action = (UiActionWithFloat) uiAction;
            float_Btn.setVisibility(View.VISIBLE);
            float_Btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mWebView.callHandler(action.floatBtn.callback, "", null);
//                    Snackbar.make(float_Btn,"ceshi", Snackbar.LENGTH_LONG).show();
                }
            });
        } else {
            float_Btn.setVisibility(View.GONE);
        }
        if (uiAction.right.icon != "") {
            handlerToolBarInitEvent("", ToolUtils.getResource1(getApplicationContext(), uiAction.right.icon), new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mWebView.callHandler(uiAction.right.callback, "", null);
                }
            });
        }
    }


    /**
     * 处理信息发布的事件
     * 其中可能包含图片上传操作
     *
     * @param data
     * @param function
     */
    private void handlerSumbitEvent(String data, final CallBackFunction function) {
        final SendUpAction action = new Gson().fromJson(data, SendUpAction.class);
        if (imgDatas != null) {
            OssEngine.getInstance().initOssEngine(getApplicationContext());
            OssEngine.getInstance().upLoadObjsWithoutName(imgDatas)
                    .flatMap(new Func1<List<String>, Observable<String>>() {
                        @Override
                        public Observable<String> call(List<String> result) {
                            String params = ParsMakeUtil.makeUpParamLikePic(action.pars, action.picsname, result);
                            return WebMethods.getInstance().sendPost(action.url, ParsMakeUtil.string2Map(params));
                        }
                    }).subscribe(new Action1<String>() {
                @Override
                public void call(String s) {
                    function.onCallBack(s);
                }
            });
        } else {
            action.pars += "&pics=";
            WebMethods.getInstance().sendPost(action.url, ParsMakeUtil.string2Map(action.pars))
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            function.onCallBack(s);
                        }
                    });
        }
    }

    /**
     * 处理获取图片的事件
     *
     * @param data
     * @param function
     */
    private void handlerPictureTakeEvent(String data, final CallBackFunction function) {
        if (util == null) {
            util = new PhotoTakeUtil(SimpleWebActivity3.this);
        }
        if ("one".equals(data)) {
            util.takePhoto()
                    .subscribe(new Action1<List<String>>() {
                        @Override
                        public void call(final List<String> strings) {
                            handlerPicWork(strings, function);
                        }
                    });
        } else if ("one_cut".equals(data)) {
            util.takePhotoCut()
                    .subscribe(new Action1<List<String>>() {
                        @Override
                        public void call(final List<String> strings) {
                            handlerPicWork(strings, function);
                        }
                    });
        } else if ("many".equals(data)) {
            util.takePhotos()
                    .subscribe(new Action1<List<String>>() {
                        @Override
                        public void call(List<String> infos) {
                            Log.i("wang","takePhotos,currentThread:"+Thread.currentThread().getName());
                            handlerPicWork(infos, function);
                        }
                    });
        }
    }

    /**
     * 处理toolbar ui设置事件
     *
     * @param text
     * @param icon
     * @param btnClick
     */
    protected void handlerToolBarInitEvent(String text, @Nullable Integer icon, View.OnClickListener btnClick) {
        if (text != null) {
            btnRight.setVisibility(View.VISIBLE);
            btnRight.setText(text);
        }
        if (icon != null) {
            btnRight.setVisibility(View.VISIBLE);
            btnRight.setBackgroundResource(icon.intValue());
            ViewGroup.LayoutParams linearParams = btnRight.getLayoutParams();
            linearParams.height = ToolUtils.dp2px(getApplicationContext(), 28);
            linearParams.width = ToolUtils.dp2px(getApplicationContext(), 28);
            btnRight.setLayoutParams(linearParams);
        }
        btnRight.setOnClickListener(btnClick);
    }

    /**
     * 拿到本地的图片地址，接下来要做的：
     * 1.从缓存目录查看是否存在这些图片的压缩图(CacheEngine已经帮我们完成)
     * 2.
     *
     * @param imgs
     */
//    protected void handlerPicWork(List<String> imgs, final CallBackFunction function) {
//        CacheEngine.getInstance().getLocalImgs(imgs)
//                .map(new Func1<List<byte[]>, String>() {
//                    @Override
//                    public String call(List<byte[]> bytes) {
//                        imgDatas = bytes;
//                        String result = "{\"path\":[";
//                        for (int i = 0; i < bytes.size(); i++) {
//                            String temp = Base64.encodeToString(bytes.get(i), Base64.DEFAULT);
//                            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
//                            Matcher m = p.matcher(temp);
//                            temp = m.replaceAll("");
//                            if (i == 0)
//                                result += "\"" + temp + "\"";
//                            else
//                                result += ",\"" + temp + "\"";
//                        }
//                        result += "]}";
//                        return result;
//                    }
//                })
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Action1<String>() {
//                    @Override
//                    public void call(String s) {
//                        function.onCallBack(s);
//                    }
//                });
//    }


    /**
     * 拿到本地的图片地址，接下来要做的：
     * 1.从缓存目录查看是否存在这些图片的压缩图(CacheEngine已经帮我们完成)
     * @param imgs
     */
    protected void handlerPicWork(List<String> imgs, final CallBackFunction function) {
        imgDatas = new ArrayList<>();
        CacheEngine.getInstance().getImages_Form(imgs)
                .flatMap(new Func1<byte[], Observable<String>>() {
                    @Override
                    public Observable<String> call(byte[] bytes) {
                        imgDatas.add(bytes);
                        return ImageUtil.byte2Base64(bytes);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        mWebView.callHandler("addPicture",s,null);
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(util!=null){
            util.cancel();
            util = null;
        }
    }
}
