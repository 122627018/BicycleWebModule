package com.wxxiaomi.ming.bicyclewebmodule.ui_refactor;

import android.app.Activity;
import android.content.Context;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;

import com.github.lzyzsd.jsbridge.CallBackFunction;

/**
 * Created by Administrator on 2016/11/30.
 */

public interface WebBuilder {
    Context getContext();

    void doGet(String data,CallBackFunction function);

    void doPost(String data,CallBackFunction function);

    String getTargetUrl();

    void doJsInitData(String data);

    void doForwardEvent(String data, CallBackFunction function);

    void doDialogEvent(String data);

    void doFinishEvent(String data);


    WebResourceResponse doInterceptRequest(WebView view, String url);

    String getComeData();

    void showLog(String data);
}
