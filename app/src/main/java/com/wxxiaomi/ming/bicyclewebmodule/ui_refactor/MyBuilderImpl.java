package com.wxxiaomi.ming.bicyclewebmodule.ui_refactor;

import android.app.Activity;
import android.content.Context;

import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.wxxiaomi.ming.bicyclewebmodule.service.WebMethods;
import com.wxxiaomi.ming.bicyclewebmodule.util.ParsMakeUtil;

import java.util.Map;

import rx.Observable;
import rx.Observer;

/**
 * Created by Administrator on 2016/11/30.
 */

public class MyBuilderImpl implements WebActionBuilder {
    private Activity activity;

    public MyBuilderImpl(Activity activity) {
        this.activity = activity;
    }

    @Override
    public Context getContext() {
        return activity;
    }

    @Override
    public Activity getActivity() {
        return activity;
    }

    @Override
    public void doGet(String data, final CallBackFunction function) {
        doGet(data)
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(String s) {
                        function.onCallBack(s);
                    }
                });
    }

    @Override
    public void doPost(String data, final CallBackFunction function) {
        doPost(data)
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(String s) {
                        function.onCallBack(s);
                    }
                });
    }

    /**
     * 解析url
     *
     * @param data js
     * @return s
     */
    private Observable<String> doGet(String data) {
        Map<String, String> pars = ParsMakeUtil.string2Map(data);
        String url = pars.get("url");
        pars.remove("url");
        return WebMethods.getInstance().sendget(url, pars);
    }

    /**
     * 向服务器post一段json数据
     */
    private Observable<String> doPost(String data) {
        Map<String, String> pars = ParsMakeUtil.string2Map(data);
        String url = pars.get("url");
        pars.remove("url");
        return WebMethods.getInstance().sendPost(url, pars);
    }
}
