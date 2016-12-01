package com.wxxiaomi.ming.bicyclewebmodule.ui_refactor.builder;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wxxiaomi.ming.bicyclewebmodule.R;
import com.wxxiaomi.ming.bicyclewebmodule.action.dialog.AlertAction;
import com.wxxiaomi.ming.bicyclewebmodule.action.dialog.DialogACtion;
import com.wxxiaomi.ming.bicyclewebmodule.action.dialog.DialogTypeAdapter;
import com.wxxiaomi.ming.bicyclewebmodule.action.dialog.LoadingAction;
import com.wxxiaomi.ming.bicyclewebmodule.action.ui.UiAction;
import com.wxxiaomi.ming.bicyclewebmodule.action.ui.UiActionWithFloat;
import com.wxxiaomi.ming.bicyclewebmodule.action.ui.UiTypeAdapter;
import com.wxxiaomi.ming.bicyclewebmodule.util.ToolUtils;


/**
 * Created by Administrator on 2016/11/30.
 * 带toolbar的builder
 */
public class MyBuilderImpl extends BaseBuilderImpl {
    private ViewGroup allView;
    private Toolbar toolbar1;
    private FloatingActionButton float_Btn;
    private Button btnRight;
    ProgressDialog dialog;
    AlertDialog alertDialog;

    public MyBuilderImpl(Context context) {
        super(context);
        init();
    }

    @Override
    public BridgeWebView buildWebView() {
        super.buildWebView();
        LayoutInflater lf = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        allView = (ViewGroup) lf.inflate(R.layout.view_toolbar_webview,null);
        LinearLayout ll_content = (LinearLayout) allView.findViewById(R.id.ll_content);
        ll_content.addView(mWebView);
        toolbar1 = (Toolbar) allView.findViewById(R.id.toolbar);
        float_Btn = (FloatingActionButton) allView.findViewById(R.id.float_Btn);
        btnRight = (Button) toolbar1.findViewById(R.id.btnRight);
        return  mWebView;
    }

    @Override
    public ViewGroup getContentView() {
        return allView;
    }

    private void init() {
        dialog = new ProgressDialog(context);
        dialog.setTitle("请等待");//设置标题
        dialog.setMessage("正在加载");
    }

    @Override
    public void doJsInitData(String data) {
        if (data != "") {
            handlerUiInitEvent(data);
        }
    }
    /**
     * 处理ui初始化事件
     *
     * @param data
     */
    private void handlerUiInitEvent(String data) {
        Gson gson = new GsonBuilder().registerTypeAdapter(UiAction.class, new UiTypeAdapter()).create();
        final UiAction uiAction = gson
                .fromJson(data, UiAction.class);
        toolbar1.setTitle(uiAction.title);
        if (uiAction instanceof UiActionWithFloat) {
            final UiActionWithFloat action = (UiActionWithFloat) uiAction;
            float_Btn.setVisibility(View.VISIBLE);
            float_Btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mWebView.callHandler(action.floatBtn.callback, "", null);
                }
            });
        } else {
            float_Btn.setVisibility(View.GONE);
        }
        if(uiAction.right!=null){
            ((AppCompatActivity)context).setSupportActionBar(toolbar1);
            ((AppCompatActivity)context).getSupportActionBar().setHomeButtonEnabled(true);
            ((AppCompatActivity)context).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        if (uiAction.right.icon != "") {
            handlerToolBarInitEvent("", ToolUtils.getResource1(context.getApplicationContext(), uiAction.right.icon), new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mWebView.callHandler(uiAction.right.callback, "", null);
                }
            });
        }
    }

    /**
     * 处理toolbar ui设置事件
     * @param text
     * @param icon
     * @param btnClick
     */
    protected void handlerToolBarInitEvent(String text, @Nullable Integer icon, View.OnClickListener btnClick) {
        if (text != null) {
            btnRight.setVisibility(View.VISIBLE);
            btnRight.setText(text);
        }
        if (icon != null) {
            btnRight.setVisibility(View.VISIBLE);
            btnRight.setBackgroundResource(icon.intValue());
            ViewGroup.LayoutParams linearParams = btnRight.getLayoutParams();
            linearParams.height = ToolUtils.dp2px(context.getApplicationContext(), 28);
            linearParams.width = ToolUtils.dp2px(context.getApplicationContext(), 28);
            btnRight.setLayoutParams(linearParams);
        }
        btnRight.setOnClickListener(btnClick);
    }



    @Override
    public void doDialogEvent(String data) {
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
                alertDialog = new AlertDialog.Builder(context)
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
                alertDialog = new AlertDialog.Builder(context)
                        .setPositiveButton(action.okMsg,null).create();

            }
            alertDialog.setTitle(action.title);
            alertDialog.setMessage(action.content);
            alertDialog.show();
        }

    }




}
