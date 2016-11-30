package com.wxxiaomi.ming.bicyclewebmodule.ui_refactor;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.BridgeWebViewClient;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wxxiaomi.ming.bicyclewebmodule.ConstantValue;
import com.wxxiaomi.ming.bicyclewebmodule.action.forward.ForwardAction;
import com.wxxiaomi.ming.bicyclewebmodule.action.forward.ForwardTypeAdapter;
import com.wxxiaomi.ming.bicyclewebmodule.action.forward.H5ACtion;
import com.wxxiaomi.ming.bicyclewebmodule.action.forward.ManyH5Action;
import com.wxxiaomi.ming.bicyclewebmodule.action.forward.NativeAction;
import com.wxxiaomi.ming.bicyclewebmodule.support.cache.CacheEngine;
import com.wxxiaomi.ming.bicyclewebmodule.ui.SimpleWebActivity2;
import com.wxxiaomi.ming.bicyclewebmodule.ui.WebTabsActivity;
import com.wxxiaomi.ming.bicyclewebmodule.ui.base.BaseWebActivity2;
import com.wxxiaomi.ming.bicyclewebmodule.util.ParsMakeUtil;

import java.io.ByteArrayInputStream;
import java.util.Map;

import rx.Observer;
import rx.functions.Action1;

/**
 * Created by Administrator on 2016/11/30.
 */

public class WebViewDirector {
    protected WebActionBuilder builder;
    protected BridgeWebView mWebView;
    public WebViewDirector(WebActionBuilder builder){
        this.builder = builder;
        mWebView = new BridgeWebView(builder.getContext());
    }

    public BridgeWebView getWview(){
        return mWebView;
    }

    public void openInit(){
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.setWebViewClient(new MyWebViewClient(mWebView));
        if(!ConstantValue.isWebCacheOpen){
            mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        }
        String url = getTargetUrl();
        mWebView.loadUrl(url);

        String data = builder.getActivity().getIntent().getStringExtra("data");
        mWebView.send(data==null?"":data, new CallBackFunction() {
            @Override
            public void onCallBack(String data) {
//                doJsInitData(data);
//                closeLoadingDialog();
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
                handleForwardEvent(data,function);
            }
        });

        mWebView.registerHandler("dialog", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
//                handlerDialogAction(data);
            }
        });


        mWebView.registerHandler("getUserId", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                int id = 25;
                Log.i("wang","native->getUserId");
                function.onCallBack(id+"");
            }
        });
        mWebView.registerHandler("getUser", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                Log.i("wang","native->getUserId");
                String json = "{\"id\":\"25\",\"name\":\"1226270181\",\"head\":\"sdsd\"}";
                function.onCallBack(json);
            }
        });

        mWebView.registerHandler("finish", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                handleFinishEvent(data);
            }
        });

        mWebView.registerHandler("loadComplete", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
//                closeLoadingDialog();
            }
        });

        mWebView.registerHandler("showLog", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                Log.i("wang","js->showLog:"+data);
            }
        });
    }

    private void handleForwardEvent(String data, CallBackFunction function) {
        Gson gson = new GsonBuilder().registerTypeAdapter(ForwardAction.class, new ForwardTypeAdapter()).create();
        ForwardAction forwardAction = gson.fromJson(data, ForwardAction.class);
        Log.i("wang","forwardAction="+forwardAction.toString());
        if(forwardAction instanceof H5ACtion){
            H5ACtion action = (H5ACtion) forwardAction;
            Intent intent = new Intent(builder.getActivity(),SimpleWebActivity2.class);
            if(action.data!=""){
                intent.putExtra("data",action.data);
            }
            if(action.page!=null){
                intent.putExtra("url",action.page);
            }
            if(action.isReturn) {
                String callBack = action.callBack;
//                list.put(1,callBack);
                builder.getActivity().startActivityForResult(intent, 1);
            }else{
                builder.getActivity().startActivity(intent);
            }
        }else if(forwardAction instanceof NativeAction){
            NativeAction action = (NativeAction) forwardAction;
            if(action.page.equals("UserInfoAct")){
//                Intent intent = new Intent(this, UserInfoAct.class);
//                intent.putExtra("value",action.data);
//                startActivity(intent);
            }
        }else if(forwardAction instanceof ManyH5Action){
            ManyH5Action action = (ManyH5Action) forwardAction;
            Log.i("wang","action:"+action.toString());
            Intent intent = new Intent(builder.getActivity(), WebTabsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("action",action);
            intent.putExtra("value",bundle);
            builder.getActivity().startActivity(intent);
        }

    }

    private void handleFinishEvent(String data) {
        Map<String, String> map = ParsMakeUtil.string2Map(data);
        if(map.get("isReturn")!=null&&map.get("isReturn").equals("true")){
            String pars = map.get("data");
            Intent intent = new Intent();
            intent.putExtra("value",pars);
            builder.getActivity().setResult(1,intent);
            builder.getActivity().finish();
        }else{
            builder.getActivity().finish();
        }
    }

    public String getTargetUrl(){
        return builder.getActivity().getIntent().getStringExtra("url");
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
                String path = "";
                final WebResourceResponse[] response = {null};
                path = url.replace("http://localhost","");
                Log.i("wang","path:"+path);
                CacheEngine.getInstance().getImage(path)
                        .subscribe(new Action1<byte[]>() {
                            @Override
                            public void call(byte[] bytes) {
                                response[0] = new WebResourceResponse("image/png", "UTF-8", new ByteArrayInputStream(bytes));
                            }
                        });
                return  response[0];
            }else {
                return super.shouldInterceptRequest(view, url);
            }
        }
    }
}
