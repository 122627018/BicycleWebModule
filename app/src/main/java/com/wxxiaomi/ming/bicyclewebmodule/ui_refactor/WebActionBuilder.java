package com.wxxiaomi.ming.bicyclewebmodule.ui_refactor;

import android.app.Activity;
import android.content.Context;

import com.github.lzyzsd.jsbridge.CallBackFunction;

/**
 * Created by Administrator on 2016/11/30.
 */

public interface WebActionBuilder {
    Context getContext();
    Activity getActivity();

    void doGet(String data,CallBackFunction function);
    void doPost(String data,CallBackFunction function);
}
