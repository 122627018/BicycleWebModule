package com.wxxiaomi.ming.bicyclewebmodule.ui.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.BridgeWebViewClient;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wxxiaomi.ming.bicyclewebmodule.ConstantValue;
import com.wxxiaomi.ming.bicyclewebmodule.action.dialog.AlertAction;
import com.wxxiaomi.ming.bicyclewebmodule.action.dialog.DialogACtion;
import com.wxxiaomi.ming.bicyclewebmodule.action.dialog.DialogTypeAdapter;
import com.wxxiaomi.ming.bicyclewebmodule.action.dialog.LoadingAction;
import com.wxxiaomi.ming.bicyclewebmodule.service.WebMethods;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.Observer;

/**
 * Created by 12262 on 2016/11/10.
 * webfragemnt的基类
 */

public abstract class BaseWebFragment extends Fragment {
    protected BridgeWebView mWebView;
    private ProgressDialog dialog;
    AlertDialog alertDialog;
    protected final int SHOW_LOADING_DIALOG = 1;
    protected final int CLOSE_LOADING_DIALOG = 2;
    protected Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case SHOW_LOADING_DIALOG:
                    dialog.show();;
                    break;
                case CLOSE_LOADING_DIALOG:
                    dialog.dismiss();
                    break;
            }
        }
    };
    private String message = "未知错误";
    private Context ct;

    @Override
    public void onStart() {
        super.onStart();
        mWebView.setWebViewClient(new MyWebViewClient(mWebView));
        dialog = new ProgressDialog(ct);
        dialog.setTitle("请等待");//设置标题
        dialog.setMessage("正在加载");
        /**
         * 显示loading dialog
         */
        mWebView.registerHandler("showLoading", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                handler.sendEmptyMessage(SHOW_LOADING_DIALOG);
            }
        });

        /**
         * 关闭loading dialog
         */
        mWebView.registerHandler("closeLoading", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                handler.sendEmptyMessage(CLOSE_LOADING_DIALOG);
            }
        });

        /**
         * 显示js中的log
         */
        mWebView.registerHandler("showLog", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                Log.i("wang","js->log:"+data);
            }
        });

        /**
         * 发送一个get请求
         */
        mWebView.registerHandler("sendGet", new BridgeHandler() {
            @Override
            public void handler(String data, final CallBackFunction function) {
//                Log.i("wang","sendGet");
                parseGetRequest(data)
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
                                Log.i("wang","native->sendGEt->result:"+s);
                                function.onCallBack(s);
                            }
                        });
            }
        });

        mWebView.registerHandler("dialog", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                handlerDialogAction(data);
            }
        });


        /**
         * 获取用户id
         */
        mWebView.registerHandler("getUserId", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                //将data转化成UserInfo
                int id = 25;
                function.onCallBack(String.valueOf(id));
            }
        });

        initData();

        mWebView.send("hello", new CallBackFunction() {
            @Override
            public void onCallBack(String data) {
                Log.i("wang","data:"+data);
            }
        });
        mWebView.callHandler("bridgeInit", "", new CallBackFunction() {
            @Override
            public void onCallBack(String data) {}
        });

    }

    /**
     * 解析url
     * @param data
     * @return
     */
    private Observable<String> parseGetRequest(String data) {
        Map<String, String> pars = parseData(data);
        String url = pars.get("url");
        pars.remove("url");
        return WebMethods.getInstance().sendget(url,pars);
    }

    /**
     * 对js传过来的String类型的参数进行解析成Map
     * @param data
     * @return
     */
    protected Map<String,String> parseData(String data){
        Map<String,String> datas = new HashMap<>();
        String[] split = data.split("&");
        for(String item : split){
            datas.put(item.substring(0,item.indexOf("=")),item.substring(item.indexOf("=")+1,item.length()));
        }
        return datas;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = initView(inflater);
        int webViewId = getWebViewId();
        mWebView = (BridgeWebView) view.findViewById(webViewId);
        if(!ConstantValue.isWebCacheOpen){
            mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        }
        return view;
    }

    protected abstract int getWebViewId();

    protected abstract View initView(LayoutInflater inflater);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        ct = getActivity();
        super.onCreate(savedInstanceState);
    }

    /**
     * 自定义的WebViewClient
     */
    protected class MyWebViewClient extends BridgeWebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            if(dialog.isShowing()){
                dialog.dismiss();
            }
            super.onPageFinished(view, url);
        }
        public MyWebViewClient(BridgeWebView webView) {
            super(webView);
        }
    }

    protected abstract void initData();

    /**
     * 处理弹框事件
     */
    private void handlerDialogAction(String data) {
        Gson gson = new GsonBuilder().registerTypeAdapter(DialogACtion.class, new DialogTypeAdapter()).create();
        DialogACtion dialogAction = gson.fromJson(data, DialogACtion.class);
        if(dialogAction instanceof LoadingAction){
            LoadingAction action = (LoadingAction)dialogAction;
            String title = action.title;
            String content = action.content;
            dialog.setTitle(title);
            dialog.setMessage(content);
            dialog.show();
        }else if(dialogAction instanceof AlertAction){
            final AlertAction action = (AlertAction)dialogAction;
            Log.i("wang","AlertAction.toString()="+action.toString());
            if(!action.okCallback.equals("")){
                alertDialog = new AlertDialog.Builder(ct)
//                        .setCancelable(false)
                        .setNegativeButton(action.cancelMsg, null)
                        .setPositiveButton(action.okMsg, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mWebView.callHandler(action.okCallback,"",null);
                            }
                        }).create();
            }else{
                alertDialog = new AlertDialog.Builder(ct)
//                        .setCancelable(true)
//                        .setNegativeButton(action.cancelMsg,null)
                        .setPositiveButton(action.okMsg,null).create();

            }
            alertDialog.setTitle(action.title);
            alertDialog.setMessage(action.content);
            alertDialog.show();
        }
    }
}
