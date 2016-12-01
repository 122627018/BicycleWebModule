package com.wxxiaomi.ming.bicyclewebmodule.ui_3;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.wxxiaomi.ming.bicyclewebmodule.R;

/**
 * Created by Administrator on 2016/12/1.
 */

public class ToolbarBuilder extends CommonBuilderImpl {
    private ViewGroup allView;
    private Toolbar toolbar1;
    private FloatingActionButton float_Btn;
    private Button btnRight;

    protected ToolbarBuilder(Context context) {
        super(context);
    }

    @Override
    public ViewGroup buildView() {
        super.buildView();
        LayoutInflater lf = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        allView = (ViewGroup) lf.inflate(R.layout.view_toolbar_webview,null);
        LinearLayout ll_content = (LinearLayout) allView.findViewById(R.id.ll_content);
        ll_content.addView(mWebView);
        toolbar1 = (Toolbar) allView.findViewById(R.id.toolbar);
        float_Btn = (FloatingActionButton) allView.findViewById(R.id.float_Btn);
        btnRight = (Button) toolbar1.findViewById(R.id.btnRight);
        //Fragment fragment = new Fragment();
        return allView;
    }

    @Override
    public void registerMethod() {

    }

    @Override
    public void initPageUI() {

    }

    @Override
    public void initPageData() {

    }
}
