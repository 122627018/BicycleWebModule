package com.wxxiaomi.ming.bicyclewebmodule.ui;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.wxxiaomi.ming.bicyclewebmodule.ui.base.BaseWebFragment;
import com.wxxiaomi.ming.bicyclewebmodule.R;

/**
 * Created by 12262 on 2016/11/10.
 */

public class WebFragment extends BaseWebFragment {



    @Override
    protected int getWebViewId() {
        return R.id.web_view;
    }

    @Override
    protected View initView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.view_simple_webview,null);
    }

    @Override
    protected void initData() {
        String url = getArguments().getString("url");
        Log.i("wang","url="+url);
        mWebView.loadUrl(url);
    }
}
