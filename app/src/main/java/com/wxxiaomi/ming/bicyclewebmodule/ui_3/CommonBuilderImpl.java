package com.wxxiaomi.ming.bicyclewebmodule.ui_3;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.github.lzyzsd.jsbridge.BridgeWebView;

/**
 * Created by Administrator on 2016/12/1.
 */

public abstract class CommonBuilderImpl implements Builder {
    protected Context context;
    protected BridgeWebView mWebView;

    protected CommonBuilderImpl(Context context){
        this.context = context;
    }

    @Override
    public ViewGroup buildView() {
        mWebView = new BridgeWebView(context);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        mWebView.setLayoutParams(p);
        return mWebView;
    }

    @Override
    public void registerMethod() {

    }
}
