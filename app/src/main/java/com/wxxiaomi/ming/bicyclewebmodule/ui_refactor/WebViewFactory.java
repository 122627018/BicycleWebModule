package com.wxxiaomi.ming.bicyclewebmodule.ui_refactor;

import com.github.lzyzsd.jsbridge.BridgeWebView;

/**
 * Created by Administrator on 2016/11/30.
 */

public class WebViewFactory {

    public static BridgeWebView create(WebActionBuilder builder){
        return new WebViewDirector(builder).getWview();
    }


}
