package com.wxxiaomi.ming.bicyclewebmodule.ui.base;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
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
import com.wxxiaomi.ming.bicyclewebmodule.action.dialog.AlertAction;
import com.wxxiaomi.ming.bicyclewebmodule.action.dialog.DialogACtion;
import com.wxxiaomi.ming.bicyclewebmodule.action.dialog.DialogTypeAdapter;
import com.wxxiaomi.ming.bicyclewebmodule.action.dialog.LoadingAction;
import com.wxxiaomi.ming.bicyclewebmodule.action.forward.ForwardAction;
import com.wxxiaomi.ming.bicyclewebmodule.action.forward.ForwardTypeAdapter;
import com.wxxiaomi.ming.bicyclewebmodule.action.forward.H5ACtion;
import com.wxxiaomi.ming.bicyclewebmodule.action.forward.ManyH5Action;
import com.wxxiaomi.ming.bicyclewebmodule.action.forward.NativeAction;
import com.wxxiaomi.ming.bicyclewebmodule.service.WebMethods;
import com.wxxiaomi.ming.bicyclewebmodule.support.cache.CacheEngine;
import com.wxxiaomi.ming.bicyclewebmodule.ui.SimpleWebActivity2;
import com.wxxiaomi.ming.bicyclewebmodule.ui.WebTabsActivity;
import com.wxxiaomi.ming.bicyclewebmodule.util.ParsMakeUtil;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.Observer;
import rx.functions.Action1;

/**
 * Created by 12262 on 2016/11/12.
 * webview的基础activity，用于webview的初始化
 * 不能含有toolbar，toolbar的初始化应该是在simpleActivity中
 */
public abstract class BaseWebActivity2 extends AppCompatActivity {
    protected BridgeWebView mWebView;

//    protected final int SHOW_LOADING_DIALOG = 1;
//    protected final int CLOSE_LOADING_DIALOG = 2;
//    protected final int SHOW_MSG_DIALOG = 3;
//    protected final int CLOSE_MSG_DIALOG = 4;

    ProgressDialog dialog;
    AlertDialog alertDialog;
    Map<Integer,String> list = new HashMap<>();

    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            alertDialog.show();;
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int webViewId = initViewAndReutrnWebViewId(savedInstanceState);
        initView(webViewId);
        initCommonUI();
        CommunicateToJs();
        initCommonMethod();
    }

    public void initView(int webViewId){
        mWebView = (BridgeWebView) findViewById(webViewId);
        mWebView.setWebViewClient(new MyWebViewClient(mWebView));
        if(!ConstantValue.isWebCacheOpen){
            mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        }
        initWebView();
    }

    private void initCommonUI() {
        dialog = new ProgressDialog(BaseWebActivity2.this);
        dialog.setTitle("请等待");//设置标题
        dialog.setMessage("正在加载");
        showLoadingDialog();
    }
    public void showLoadingDialog(){
        dialog.show();
    }

    public void closeLoadingDialog(){
        if(dialog.isShowing()){
            dialog.dismiss();
        }
    }

    /**
     * 当别的h5页面或者activity有带数据过来的时候
     * 这个函数负责发给js
     * 并且处理js返回的数据
     * 一般此数据携带ui的处理信息
     */
    private void CommunicateToJs() {
        String data = getIntent().getStringExtra("data");
        mWebView.send(data==null?"":data, new CallBackFunction() {
            @Override
            public void onCallBack(String data) {
                doJsInitData(data);
                closeLoadingDialog();
            }
        });
    }

    /**
     * 向webview注册一些公共函数供js调用
     */
    protected void initCommonMethod() {
        /**
         * 发送一个get请求
         */
        mWebView.registerHandler("sendGet", new BridgeHandler() {
            @Override
            public void handler(String data, final CallBackFunction function) {
                doGet(data)
                        .subscribe(new Observer<String>() {
                            @Override
                            public void onCompleted() {}

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
        });

        /**
         * 发送一个get请求
         */
        mWebView.registerHandler("sendPost", new BridgeHandler() {
            @Override
            public void handler(String data, final CallBackFunction function) {
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
                handlerDialogAction(data);
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
                closeLoadingDialog();
            }
        });

        mWebView.registerHandler("showLog", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                Log.i("wang","js->showLog:"+data);
            }
        });

    }

    /**
     * 处理结束页面的事件
     * @param data js带过来的数据
     */
    private void handleFinishEvent(String data) {
        Map<String, String> map = ParsMakeUtil.string2Map(data);
        if(map.get("isReturn")!=null&&map.get("isReturn").equals("true")){
            String pars = map.get("data");
            Intent intent = new Intent();
            intent.putExtra("value",pars);
            setResult(1,intent);
            finish();
        }else{
            finish();
        }
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
                alertDialog = new AlertDialog.Builder(BaseWebActivity2.this)
                        .setCancelable(false)
                        .setNegativeButton(action.cancelMsg, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setPositiveButton(action.okMsg, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mWebView.callHandler(action.okCallback,"",null);
                            }
                        }).create();
            }else{
                alertDialog = new AlertDialog.Builder(BaseWebActivity2.this)
                        .setPositiveButton(action.okMsg,null).create();

            }
            alertDialog.setTitle(action.title);
            alertDialog.setMessage(action.content);
//            alertDialog.show();
            handler.sendEmptyMessage(20);
        }
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

    protected abstract int initViewAndReutrnWebViewId(Bundle savedInstanceState);
    protected abstract void initWebView();
    protected abstract void doJsInitData(String data);

    /**
     * 处理跳转事件
     */
    private void handleForwardEvent(String data,CallBackFunction function) {
        Gson gson = new GsonBuilder().registerTypeAdapter(ForwardAction.class, new ForwardTypeAdapter()).create();
        ForwardAction forwardAction = gson.fromJson(data, ForwardAction.class);
        Log.i("wang","forwardAction="+forwardAction.toString());
        if(forwardAction instanceof H5ACtion){
            H5ACtion action = (H5ACtion) forwardAction;
            Intent intent = new Intent(BaseWebActivity2.this,SimpleWebActivity2.class);
            if(action.data!=""){
                intent.putExtra("data",action.data);
            }
            if(action.page!=null){
                intent.putExtra("url",action.page);
            }
            if(action.isReturn) {
                String callBack = action.callBack;
                list.put(1,callBack);
                startActivityForResult(intent, 1);
            }else{
                startActivity(intent);
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
            Intent intent = new Intent(BaseWebActivity2.this, WebTabsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("action",action);
            intent.putExtra("value",bundle);
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1){
            if(data!=null) {
                String pars = data.getStringExtra("value");
                if (!pars.equals("")) {
                    String callback = list.get(1);
                    list.remove(String.valueOf(callback));
                    mWebView.callHandler(callback, pars, null);
                }
            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        if(item.getItemId() == android.R.id.home)
        {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
