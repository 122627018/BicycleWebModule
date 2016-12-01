package com.wxxiaomi.ming.bicyclewebmodule.ui_refactor;

import android.view.ViewGroup;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.BridgeWebViewClient;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.wxxiaomi.ming.bicyclewebmodule.ConstantValue;
import com.wxxiaomi.ming.bicyclewebmodule.ui_refactor.builder.WebBuilder;

/**
 * Created by Administrator on 2016/11/30.
 * 导演类
 */
public class WebDirector {
    protected WebBuilder builder;
    protected BridgeWebView mWebView;
    public WebDirector(WebBuilder builder){
        this.builder = builder;
        mWebView = builder.buildWebView();
    }

    public ViewGroup getWview(){
        return builder.getContentView();
    }

    public void openInit(){
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.setWebViewClient(new MyWebViewClient(mWebView));
        if(!ConstantValue.isWebCacheOpen){
            mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        }
        String url = builder.getTargetUrl();
        mWebView.loadUrl(url);
        String data = builder.getComeData();
        mWebView.send(data==null?"":data, new CallBackFunction() {
            @Override
            public void onCallBack(String data) {
                builder.doJsInitData(data);
            }
        });

        /**
         * 发送一个get请求
         */
        mWebView.registerHandler("sendGet", new BridgeHandler() {
            @Override
            public void handler(String data, final CallBackFunction function) {
                builder.doGet(data, function);
            }
        });

        /**
         * 发送一个get请求
         */
        mWebView.registerHandler("sendPost", new BridgeHandler() {
            @Override
            public void handler(String data, final CallBackFunction function) {
                builder.doPost(data, function);
            }
        });


        //跳转
        mWebView.registerHandler("forward", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                builder.doForwardEvent(data,function);
            }
        });

        mWebView.registerHandler("dialog", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                builder.doDialogEvent(data);
            }
        });

        mWebView.registerHandler("finish", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                builder.doFinishEvent(data);
            }
        });

        mWebView.registerHandler("loadComplete", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
            }
        });

        mWebView.registerHandler("showLog", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                builder.showLog(data);
            }
        });
    }


    /**
     * 自定义的WebViewClient
     */
    protected class MyWebViewClient extends BridgeWebViewClient {
        public MyWebViewClient(BridgeWebView webView) {
            super(webView);
        }
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
            if(url.contains("http://localhost")){
                return  builder.doInterceptRequest(view, url);
            }else {
                return super.shouldInterceptRequest(view, url);
            }
        }
    }
}
