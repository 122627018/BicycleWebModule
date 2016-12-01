package com.wxxiaomi.ming.bicyclewebmodule.ui_refactor.builder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.wxxiaomi.ming.bicyclewebmodule.R;

/**
 * Created by Administrator on 2016/12/1.
 */

public class FragmentBuilder extends BaseBuilderImpl {
    public FragmentBuilder(Context context) {
        super(context);
    }
    @Override
    public BridgeWebView buildWebView() {
        mWebView = new BridgeWebView(context);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        mWebView.setLayoutParams(p);
        return mWebView;
    }

    @Override
    public ViewGroup getContentView() {
        return mWebView;
    }

    @Override
    public void doJsInitData(String data) {

    }

    @Override
    public void doDialogEvent(String data) {

    }
}
