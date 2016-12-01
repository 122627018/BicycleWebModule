package com.wxxiaomi.ming.bicyclewebmodule.ui_3;

import android.view.ViewGroup;

/**
 * Created by Administrator on 2016/12/1.
 */

public class Director {
    private Builder builder;
    public Director(Builder builder){
        this.builder = builder;
    }

    public ViewGroup construct(){
        return builder.buildView();
    }

    public void PageInit(){
        builder.registerMethod();
        builder.initPageUI();
        builder.initPageData();
    }

    public void onActivityResult(){

    }
}
